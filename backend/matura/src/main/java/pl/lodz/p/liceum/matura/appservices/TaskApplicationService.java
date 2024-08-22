package pl.lodz.p.liceum.matura.appservices;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.lodz.p.liceum.matura.appservices.verifier.AuthVerifyTask;
import pl.lodz.p.liceum.matura.domain.submission.Submission;
import pl.lodz.p.liceum.matura.domain.submission.VerificationType;
import pl.lodz.p.liceum.matura.domain.subtask.Subtask;
import pl.lodz.p.liceum.matura.domain.task.*;
import pl.lodz.p.liceum.matura.domain.template.Template;
import pl.lodz.p.liceum.matura.domain.template.TemplateService;
import pl.lodz.p.liceum.matura.domain.workspace.Workspace;

import java.time.Clock;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log
public class TaskApplicationService {
    private final TaskService taskService;
    private final IAuthenticationFacade authenticationFacade;
    private final TemplateService templateService;
    private final Workspace workspace;
    private final Clock clock;
    private final SubmissionApplicationService submissionService;
    private final TaskExecutor taskExecutor;

    public Map<String, Object> readTaskDefinitionFile(Integer taskId) {
        String workspaceUrl = getWorkspaceUrl(taskId);
        return workspace.readTaskDefinitionFile(workspaceUrl);
    }

    public byte[] readFile(Integer taskId) {
        String workspaceUrl = getWorkspaceUrl(taskId);
        String relativePath = getRelativeFilePath(taskId);
        return workspace.readFile(workspaceUrl, relativePath);
    }

    public void writeFile(Integer taskId, byte[] bytes) {
        String workspaceUrl = getWorkspaceUrl(taskId);
        String relativePath = getRelativeFilePath(taskId);
        workspace.writeFile(workspaceUrl, relativePath, bytes);
    }

    public String getWorkspaceUrl(Integer taskId) {
        return taskService.findById(taskId).getWorkspaceUrl();
    }

    public String getRelativeFilePath(Integer taskId) {
        Map<String, Object> taskDefinition = readTaskDefinitionFile(taskId);
        String relativePath = (String) taskDefinition.get("sourceFile");
        return relativePath;
    }

    public String getFileName(Integer taskId) {
        String relativePath = getRelativeFilePath(taskId);
        String fileName = relativePath.substring(relativePath.lastIndexOf('/') + 1);
        return fileName;
    }

    @Transactional
    public Task taskSaveTransaction(Task taskToSave) {
        taskToSave.setCreatedAt(ZonedDateTime.now(clock));
        taskToSave.setCreatedBy(authenticationFacade.getLoggedInUserId());
        return taskService.save(taskToSave);
    }

    @Transactional
    public void taskUpdateTransaction(Task taskToUpdate) {
        taskService.update(taskToUpdate);
    }

    @Transactional
    public void taskRemoveByIdTransaction(Integer id) {
        taskService.removeById(id);
    }

    public Task save(Task taskToSave) {
        try {
            Template template = templateService.findById(taskToSave.getTemplateId());
            String workspaceUrl = workspace.createWorkspace(template.getSourceUrl());

            taskToSave.setWorkspaceUrl(workspaceUrl);
            taskToSave.setState(TaskState.CREATED);

            return taskSaveTransaction(taskToSave);
        } catch (DataIntegrityViolationException ex) {
            log.warning("Task " + taskToSave.toString() + " already exits in db");
            throw new TaskAlreadyExistsException();
        }
    }

    public void update(Task taskToUpdate) {
        try {
            taskUpdateTransaction(taskToUpdate);
        } catch (DataIntegrityViolationException ex) {
            log.warning("Error while updating task " + taskToUpdate.toString());
            throw new TaskNotFoundException();
        }
    }

    @AuthVerifyTask
    public void removeById(Integer id) {
        try {
            taskRemoveByIdTransaction(id);
        } catch (DataIntegrityViolationException ex) {
            log.warning("Error while removing task with id  " + id);
            throw new TaskNotFoundException();
        }
    }

    public Task findById(Integer id) {
        return taskService.findById(id);
    }

    public List<Task> findByCreatedBy(Integer createdAtId) {
        return taskService.findByCreatedBy(createdAtId);
    }

    public List<Task> findByUserId(Integer userId) {
        return taskService.findByUserId(userId);
    }

    public PageTask findByUserIdAndStateIn(final Integer userId, List<TaskState> taskStates, Pageable pageable) {
        return taskService.findByUserIdAndStateIn(userId, taskStates, pageable);
    }

    public Submission executeSubtask(Integer taskId, Integer subtaskId, VerificationType verificationType) {
        Task task = taskService.findById(taskId);
        task.setState(TaskState.PROCESSING);
        update(task);
        Submission submission = submissionService.save(
                new Submission(null, task.getId(), verificationType, null, null)
        );
        taskExecutor.executeSubtask(new Subtask(submission.getId(), task.getId(), subtaskId, verificationType));
        return submission;
    }

    public Submission executeTask(Integer taskId) {
        Task task = taskService.findById(taskId);
        task.setState(TaskState.PROCESSING);
        update(task);
        Submission submission = submissionService.save(
                new Submission(null, task.getId(), VerificationType.FULL, null, null)
        );
        taskExecutor.executeTask(task, submission);
        return submission;
    }

    public Optional<Task> findPendingTaskForUser(Integer userId, Integer templateId) {
        return taskService.findByTemplateIdAndUserId(templateId, userId);
    }
}

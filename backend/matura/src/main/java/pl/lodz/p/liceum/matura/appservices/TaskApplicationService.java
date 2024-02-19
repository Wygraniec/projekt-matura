package pl.lodz.p.liceum.matura.appservices;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import pl.lodz.p.liceum.matura.appservices.verifier.AuthVerifyTask;
import pl.lodz.p.liceum.matura.domain.task.Task;
import pl.lodz.p.liceum.matura.domain.task.TaskAlreadyExistsException;
import pl.lodz.p.liceum.matura.domain.task.TaskNotFoundException;
import pl.lodz.p.liceum.matura.domain.task.TaskService;
import pl.lodz.p.liceum.matura.domain.template.Template;
import pl.lodz.p.liceum.matura.domain.template.TemplateAlreadyExistsException;
import pl.lodz.p.liceum.matura.domain.template.TemplateNotFoundException;

import java.time.Clock;
import java.time.ZonedDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log
public class TaskApplicationService {
    private final TaskService taskService;
    private final IAuthenticationFacade authenticationFacade;
    private final Clock clock;

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
}

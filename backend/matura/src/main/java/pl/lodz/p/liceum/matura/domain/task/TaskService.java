package pl.lodz.p.liceum.matura.domain.task;

import lombok.RequiredArgsConstructor;
import pl.lodz.p.liceum.matura.domain.template.Template;
import pl.lodz.p.liceum.matura.domain.template.TemplateNotFoundException;
import pl.lodz.p.liceum.matura.domain.template.TemplateService;
import pl.lodz.p.liceum.matura.domain.workspace.Workspace;

import java.util.List;

@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final TemplateService templateService;
    private final Workspace workspace;

    public Task save(Task task) {
        Template template = templateService.findById(task.getTemplateId());
        String workspaceUrl = workspace.createWorkspace(template.getSourceUrl());

        task.setWorkspaceUrl(workspaceUrl);
        task.setState(TaskState.CREATED);
        return taskRepository.save(task);
    }

    public void update(Task task) {
        taskRepository.update(task);
    }

    public void removeById(Integer id) {
        taskRepository.remove(id);
    }

    public Task findById(Integer id) {
        return taskRepository
                .findById(id)
                .orElseThrow(TaskNotFoundException::new);
    }

    public List<Task> findByCreatedBy(Integer createdAtId) {
        return taskRepository.findByCreatedBy(createdAtId);
    }

    public List<Task> findByUserId(Integer userId) {
        return taskRepository.findByUserId(userId);
    }
}

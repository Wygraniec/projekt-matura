package pl.lodz.p.liceum.matura.domain.task;

import lombok.RequiredArgsConstructor;
import pl.lodz.p.liceum.matura.domain.template.Template;
import pl.lodz.p.liceum.matura.domain.template.TemplateNotFoundException;
import pl.lodz.p.liceum.matura.domain.template.TemplateService;
import pl.lodz.p.liceum.matura.domain.workspace.Workspace;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;

    public Task save(Task task) {
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

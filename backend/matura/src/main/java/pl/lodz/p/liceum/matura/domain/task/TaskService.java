package pl.lodz.p.liceum.matura.domain.task;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

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
    public PageTask findByUserIdAndStateIn(final Integer userId, List<TaskState> taskStates, Pageable pageable) {
        return taskRepository.findByUserIdAndStateIn(userId, taskStates, pageable);
    }
    public Optional<Task> findByTemplateIdAndUserIdAndStateIn(Integer templateId, Integer userId, List<TaskState> taskStates) {
        return taskRepository.findByTemplateIdAndUserIdAndStateIn(templateId, userId, taskStates);
    }
}

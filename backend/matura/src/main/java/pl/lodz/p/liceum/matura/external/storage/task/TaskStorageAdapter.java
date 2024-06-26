package pl.lodz.p.liceum.matura.external.storage.task;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.lodz.p.liceum.matura.domain.task.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Log
public class TaskStorageAdapter implements TaskRepository {

    private final JpaTaskRepository repository;
    private final TaskEntityMapper mapper;

    @Override
    public Task save(final Task task) {
        try {
            TaskEntity saved = repository.save(mapper.toEntity(task));
            log.info("Saved entity " + saved);
            return mapper.toDomain(saved);
        } catch (DataIntegrityViolationException e) {
            log.warning("Task " + task.getId() + " already exists");
            throw new TaskAlreadyExistsException();
        }
    }

    @Override
    public void update(final Task task) {
        repository
                .findById(task.getId())
                .ifPresent(taskEntity -> repository.save(mapper.toEntity(task)));
    }

    @Override
    public void remove(final Integer id) {
        repository
                .findById(id)
                .ifPresent(taskEntity -> repository.deleteById(id));
    }

    @Override
    public Optional<Task> findById(final Integer id) {
        return repository
                .findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public List<Task> findByCreatedBy(final Integer createdAtId) {
        return repository
                .findByCreatedBy(createdAtId)
                .stream()
                .map(mapper::toDomain)
                .toList();

    }
    @Override
    public PageTask findByUserIdAndStateIn(final Integer userId, List<TaskState> taskStates, Pageable pageable) {
        Page<TaskEntity> pageOfTemplateEntity = repository.findByUserIdAndStateIn(userId, taskStates, pageable);
        List<Task> tasksOnCurrentPage = pageOfTemplateEntity.getContent().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
        return new PageTask(
                tasksOnCurrentPage,
                pageable.getPageNumber() + 1,
                pageOfTemplateEntity.getTotalPages(),
                pageOfTemplateEntity.getTotalElements()
        );
    }
    @Override
    public List<Task> findByUserId(final Integer userId) {
        return repository
                .findByUserId(userId)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }
    @Override
    public Optional<Task> findByTemplateIdAndUserId(final Integer templateId, final Integer userId) {
        return repository
                .findByTemplateIdAndUserId(templateId, userId)
                .map(mapper::toDomain);
    }
}

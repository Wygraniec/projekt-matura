package pl.lodz.p.liceum.matura.external.storage.task;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.dao.DataIntegrityViolationException;
import pl.lodz.p.liceum.matura.domain.task.Task;
import pl.lodz.p.liceum.matura.domain.task.TaskAlreadyExistsException;
import pl.lodz.p.liceum.matura.domain.task.TaskRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Log
public class TaskStorageAdapter implements TaskRepository {
    private final JpaTaskRepository repository;
    private final TaskEntityMapper mapper;


    @Override
    public Task save(Task task) {
        try {
            TaskEntity saved = repository.save(mapper.toEntity(task));
            log.info("Saved entity " + saved);
            return mapper.toDomain(saved);
        } catch (DataIntegrityViolationException e) {
            log.warning("Template " + task.getId() + " already exists");
            throw new TaskAlreadyExistsException();
        }
    }

    @Override
    public void update(Task task) {
        repository
                .findById(task.getId())
                .ifPresent(taskEntity -> repository.save(mapper.toEntity(task)));
    }

    @Override
    public void remove(Integer id) {
        repository
                .findById(id)
                .ifPresent(taskEntity -> repository.deleteById(id));
    }

    @Override
    public Optional<Task> findById(Integer id) {
        return repository
                .findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public List<Task> findByCreatedBy(Integer createdAtId) {
        return repository
                .findByCreatedBy(createdAtId)
                .stream()
                .map(mapper::toDomain)
                .toList();

    }

    @Override
    public List<Task> findByUserId(Integer userId) {
        return repository
                .findByUserId(userId)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }
}

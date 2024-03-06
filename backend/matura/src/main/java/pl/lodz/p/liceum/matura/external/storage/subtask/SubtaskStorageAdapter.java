package pl.lodz.p.liceum.matura.external.storage.subtask;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.dao.DataIntegrityViolationException;
import pl.lodz.p.liceum.matura.domain.subtask.Subtask;
import pl.lodz.p.liceum.matura.domain.subtask.SubtaskAlreadyExistsException;
import pl.lodz.p.liceum.matura.domain.subtask.SubtaskRepository;

import java.util.Optional;

@RequiredArgsConstructor
@Log
public class SubtaskStorageAdapter implements SubtaskRepository {

    private final JpaSubtaskRepository repository;
    private final SubtaskEntityMapper mapper;

    @Override
    public Subtask save(final Subtask subtask) {
        try {
            SubtaskEntity saved = repository.save(mapper.toEntity(subtask));
            log.info("Saved entity " + saved);
            return mapper.toDomain(saved);
        } catch (DataIntegrityViolationException e) {
            log.warning("Subtask " + subtask.getId() + " already exists");
            throw new SubtaskAlreadyExistsException();
        }
    }

    @Override
    public void update(final Subtask task) {
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
    public Optional<Subtask> findById(final Integer id) {
        return repository
                .findById(id)
                .map(mapper::toDomain);
    }
}

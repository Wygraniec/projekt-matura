package pl.lodz.p.liceum.matura.external.storage.result;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.dao.DataIntegrityViolationException;
import pl.lodz.p.liceum.matura.domain.result.Result;
import pl.lodz.p.liceum.matura.domain.result.ResultAlreadyExistsException;
import pl.lodz.p.liceum.matura.domain.result.ResultRepository;

import java.util.Optional;

@RequiredArgsConstructor
@Log
public class ResultStorageAdapter implements ResultRepository {

    private final JpaResultRepository repository;
    private final ResultEntityMapper mapper;

    @Override
    public Result save(final Result result) {
        try {
            ResultEntity saved = repository.save(mapper.toEntity(result));
            log.info("Saved entity " + saved);
            return mapper.toDomain(saved);
        } catch (DataIntegrityViolationException e) {
            log.warning("Subtask " + result.getId() + " already exists");
            throw new ResultAlreadyExistsException();
        }
    }

    @Override
    public void update(final Result result) {
        repository
                .findById(result.getId())
                .ifPresent(taskEntity -> repository.save(mapper.toEntity(result)));
    }

    @Override
    public void remove(final Integer id) {
        repository
                .findById(id)
                .ifPresent(resultEntity -> repository.deleteById(id));
    }

    @Override
    public Optional<Result> findById(final Integer id) {
        return repository
                .findById(id)
                .map(mapper::toDomain);
    }
}

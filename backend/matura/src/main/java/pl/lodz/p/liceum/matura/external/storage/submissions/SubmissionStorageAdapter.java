package pl.lodz.p.liceum.matura.external.storage.submissions;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.dao.DataIntegrityViolationException;
import pl.lodz.p.liceum.matura.domain.submission.Submission;
import pl.lodz.p.liceum.matura.domain.subtask.Subtask;
import pl.lodz.p.liceum.matura.domain.submission.SubmissionAlreadyExistsException;
import pl.lodz.p.liceum.matura.domain.submission.SubmissionRepository;

import java.util.Optional;

@RequiredArgsConstructor
@Log
public class SubmissionStorageAdapter implements SubmissionRepository {

    private final JpaSubmissionRepository repository;
    private final SubmissionEntityMapper mapper;

    @Override
    public Submission save(final Submission submission) {
        try {
            SubmissionEntity saved = repository.save(mapper.toEntity(submission));
            log.info("Saved entity " + saved);
            return mapper.toDomain(saved);
        } catch (DataIntegrityViolationException e) {
            log.warning("Subtask " + submission.getId() + " already exists");
            throw new SubmissionAlreadyExistsException();
        }
    }

    @Override
    public void update(final Submission submission) {
        repository
                .findById(submission.getId())
                .ifPresent(taskEntity -> repository.save(mapper.toEntity(submission)));
    }

    @Override
    public void remove(final Integer id) {
        repository
                .findById(id)
                .ifPresent(submissionEntity -> repository.deleteById(id));
    }

    @Override
    public Optional<Submission> findById(final Integer id) {
        return repository
                .findById(id)
                .map(mapper::toDomain);
    }
}

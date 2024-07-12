package pl.lodz.p.liceum.matura.domain.result;

import lombok.RequiredArgsConstructor;
import pl.lodz.p.liceum.matura.domain.submission.Submission;
import pl.lodz.p.liceum.matura.domain.task.TaskNotFoundException;

import java.util.List;

@RequiredArgsConstructor
public class ResultService {
    private final ResultRepository resultRepository;

    public Result save(Result result) {
        return resultRepository.save(result);
    }

    public void update(Result result) {
        resultRepository.update(result);
    }

    public void removeById(Integer id) {
        resultRepository.remove(id);
    }

    public Result findById(Integer id) {
        return resultRepository
                .findById(id)
                .orElseThrow(ResultNotFoundException::new);
    }
    public List<Result> findBySubmissionId(Integer submissionId) {
        return resultRepository.findBySubmissionId(submissionId);
    }
}

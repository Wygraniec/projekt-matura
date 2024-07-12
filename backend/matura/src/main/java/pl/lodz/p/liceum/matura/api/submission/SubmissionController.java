package pl.lodz.p.liceum.matura.api.submission;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.liceum.matura.api.result.ResultDto;
import pl.lodz.p.liceum.matura.api.result.ResultDtoMapper;
import pl.lodz.p.liceum.matura.domain.result.Result;
import pl.lodz.p.liceum.matura.domain.result.ResultService;
import pl.lodz.p.liceum.matura.domain.submission.Submission;
import pl.lodz.p.liceum.matura.domain.submission.SubmissionService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/api/v1/submissions")
public class SubmissionController {

    private final SubmissionService submissionService;
    private final SubmissionDtoMapper submissionMapper;
    private final ResultService resultService;
    private final ResultDtoMapper resultMapper;

    @GetMapping(path = "/{id}")
    public ResponseEntity<SubmissionDto> getSubmission(@PathVariable Integer id) {
        Submission submission = submissionService.findById(id);
        return ResponseEntity.ok(submissionMapper.toDto(submission));
    }

    @GetMapping(path = "/{id}/results")
    public ResponseEntity<List<ResultDto>> getResults(@PathVariable Integer id) {
        List<Result> results = resultService.findBySubmissionId(id);
        return ResponseEntity.ok(
                results.stream()
                        .map(resultMapper::toDto)
                        .toList());
    }
}

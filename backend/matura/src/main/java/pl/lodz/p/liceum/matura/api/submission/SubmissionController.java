package pl.lodz.p.liceum.matura.api.submission;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.liceum.matura.domain.submission.Submission;
import pl.lodz.p.liceum.matura.domain.submission.SubmissionService;
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/api/v1/submissions")
public class SubmissionController {

    private final SubmissionService submissionService;
    private final SubmissionDtoMapper mapper;

    @GetMapping(path = "/{id}")
    public ResponseEntity<SubmissionDto> getSubmission(@PathVariable Integer id) {
        Submission submission = submissionService.findById(id);
        return ResponseEntity.ok(mapper.toDto(submission));
    }
}

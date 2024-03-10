package pl.lodz.p.liceum.matura.api.submission;

import pl.lodz.p.liceum.matura.domain.submission.VerificationType;

import java.time.ZonedDateTime;

public record SubmissionDto(
        Integer id,
        Integer taskId,
        VerificationType type,
        Integer submittedBy,
        ZonedDateTime submittedAt
) {
}

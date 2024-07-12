package pl.lodz.p.liceum.matura.api.result;

import java.time.ZonedDateTime;

public record ResultDto(
        Integer id,
        Integer submissionId,
        Integer subtaskNumber,
        String description,
        Integer score,
        ZonedDateTime createdAt
) {
}

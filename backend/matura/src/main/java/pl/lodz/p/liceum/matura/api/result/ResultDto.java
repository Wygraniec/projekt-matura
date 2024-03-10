package pl.lodz.p.liceum.matura.api.result;

public record ResultDto(
        Integer id,
        Integer submissionId,
        Integer subtaskNumber,
        String description,
        Integer score
) {
}

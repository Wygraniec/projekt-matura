package pl.lodz.p.liceum.matura.api.submission;

import org.mapstruct.Mapper;
import pl.lodz.p.liceum.matura.domain.submission.Submission;

@Mapper(componentModel = "spring")
public interface SubmissionDtoMapper {
    SubmissionDto toDto(Submission domain);
    Submission toDomain(SubmissionDto dto);
}

package pl.lodz.p.liceum.matura.external.storage.submissions;

import org.mapstruct.Mapper;
import pl.lodz.p.liceum.matura.domain.submission.Submission;

@Mapper(componentModel = "spring")
public interface SubmissionEntityMapper {
    SubmissionEntity toEntity(Submission domain);
    Submission toDomain(SubmissionEntity entity);
}

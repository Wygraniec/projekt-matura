package pl.lodz.p.liceum.matura.external.storage.subtask;

import org.mapstruct.Mapper;
import pl.lodz.p.liceum.matura.domain.subtask.Subtask;

@Mapper(componentModel = "spring")
public interface SubtaskEntityMapper {
    SubtaskEntity toEntity(Subtask domain);
    Subtask toDomain(SubtaskEntity entity);
}

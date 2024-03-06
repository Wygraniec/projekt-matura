package pl.lodz.p.liceum.matura.api.subtask;

import org.mapstruct.Mapper;
import pl.lodz.p.liceum.matura.domain.subtask.Subtask;

@Mapper(componentModel = "spring")
public interface SubtaskDtoMapper {
    SubtaskDto toDto(Subtask domain);
    Subtask toDomain(SubtaskDto dto);
}

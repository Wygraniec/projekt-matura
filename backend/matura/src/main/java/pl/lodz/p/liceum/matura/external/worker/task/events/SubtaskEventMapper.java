package pl.lodz.p.liceum.matura.external.worker.task.events;

import org.mapstruct.Mapper;
import pl.lodz.p.liceum.matura.domain.subtask.Subtask;

@Mapper(componentModel = "spring")
public interface SubtaskEventMapper {

    SubtaskEvent toDto(Subtask subtask);

    Subtask toDomain(SubtaskEvent taskEvent);

}

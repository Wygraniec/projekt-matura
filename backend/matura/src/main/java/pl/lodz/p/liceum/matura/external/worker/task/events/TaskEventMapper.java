package pl.lodz.p.liceum.matura.external.worker.task.events;

import org.mapstruct.Mapper;
import pl.lodz.p.liceum.matura.domain.task.Subtask;
import pl.lodz.p.liceum.matura.domain.task.Task;

@Mapper(componentModel = "spring")
public interface TaskEventMapper {

    TaskEvent toDto(Task subtask);

    Task toDomain(TaskEvent taskEvent);

}

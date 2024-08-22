package pl.lodz.p.liceum.matura.external.worker.task.events;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.lodz.p.liceum.matura.domain.task.Task;

@Mapper(componentModel = "spring")
public interface TaskEventMapper {
    @Mapping(target = "taskId", source = "task.id")
    TaskEvent toDto(Task task);
    @Mapping(target = "id", source = "taskEvent.taskId")
    Task toDomain(TaskEvent taskEvent);

}

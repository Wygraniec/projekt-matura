package pl.lodz.p.liceum.matura.api.task;

import org.mapstruct.Mapper;
import pl.lodz.p.liceum.matura.domain.task.Task;

@Mapper(componentModel = "spring")
public interface TaskDtoMapper {
    TaskDto toDto(Task domain);
    Task toDomain(TaskDto dto);
}

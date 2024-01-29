package pl.lodz.p.liceum.matura.external.storage.task;

import org.mapstruct.Mapper;
import pl.lodz.p.liceum.matura.domain.task.Task;

@Mapper(componentModel = "spring")
public interface TaskEntityMapper {
    TaskEntity toEntity(Task domain);
    Task toDomain(TaskEntity entity);
}

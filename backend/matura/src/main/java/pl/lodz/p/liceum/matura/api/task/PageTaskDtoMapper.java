package pl.lodz.p.liceum.matura.api.task;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import pl.lodz.p.liceum.matura.domain.task.PageTask;
import pl.lodz.p.liceum.matura.domain.task.Task;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PageTaskDtoMapper {

    @Mapping(target = "tasks", qualifiedByName = "toTaskDtoList")
    PageTaskDto toPageDto(PageTask domain);

    @Named("toTaskDtoList")
    @IterableMapping(qualifiedByName = "taskToTaskDto")
    List<TaskDto> toListDto(List<Task> tasks);

    @Named("taskToTaskDto")
    TaskDto toDto(Task domain);
}
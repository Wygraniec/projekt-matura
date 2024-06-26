package pl.lodz.p.liceum.matura.api.task;

import java.util.List;

public record PageTaskDto(
        List<TaskDto> tasks,
        Integer currentPage,
        Integer totalPages,
        Long totalElements
) {
}

package pl.lodz.p.liceum.matura.api.subtask;

import pl.lodz.p.liceum.matura.domain.task.TestType;

public record SubtaskDto(
        Integer id,
        Integer taskId,
        String name,
        TestType type
) {
}

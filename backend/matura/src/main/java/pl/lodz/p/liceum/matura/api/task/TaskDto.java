package pl.lodz.p.liceum.matura.api.task;

import pl.lodz.p.liceum.matura.domain.task.TaskState;

import java.time.ZonedDateTime;

public record TaskDto(
        Integer id,
        Integer userId,
        Integer templateId,
        Integer numberOfSubtasks,
        String workspaceUrl,
        TaskState state,
        Integer createdBy,
        ZonedDateTime createdAt
) {
}

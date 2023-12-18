package pl.lodz.p.liceum.matura.api.task;

import pl.lodz.p.liceum.matura.domain.task.TestType;

public record ExecuteCommand(
        String workspaceUrl,
        String name,
        String type
) {}

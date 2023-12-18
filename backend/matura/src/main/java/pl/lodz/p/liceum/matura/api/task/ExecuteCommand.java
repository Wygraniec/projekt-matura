package pl.lodz.p.liceum.matura.api.task;

public record ExecuteCommand(
        String workspaceUrl,
        String name,
        String type
) {}

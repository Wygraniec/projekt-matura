package pl.lodz.p.liceum.matura.external.worker.task.definition;

import lombok.Data;

import java.util.Map;

@Data
public class TaskDefinition {
    TaskEnvironment environment;
    CheckData verification;
    Map<String, Subtask> tasks;
}

package pl.lodz.p.liceum.matura.external.worker.task;

import pl.lodz.p.liceum.matura.external.worker.task.definition.TaskDefinition;

public interface TaskDefinitionParser {

    TaskDefinition parse(String taskDefinitionPath);
}

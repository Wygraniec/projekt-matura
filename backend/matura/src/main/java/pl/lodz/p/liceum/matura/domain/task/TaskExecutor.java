package pl.lodz.p.liceum.matura.domain.task;

import pl.lodz.p.liceum.matura.domain.subtask.Subtask;

public interface TaskExecutor {

    void executeTask(Task task);
    void executeSubtask(Subtask subtask);

}

package pl.lodz.p.liceum.matura.domain;

public interface TaskExecutor {

    ExecutionStatus executeTask(Task task);
    ExecutionStatus executeSubtask(Subtask subtask);
}
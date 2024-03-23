package pl.lodz.p.liceum.matura.domain;

public interface TaskExecutor {

    Result executeTask(Task task);
    Result executeSubtask(Subtask subtask);
}
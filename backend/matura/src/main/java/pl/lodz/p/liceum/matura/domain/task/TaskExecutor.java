package pl.lodz.p.liceum.matura.domain.task;

public interface TaskExecutor {

    void executeTask(Task task);
    void executeSubtask(Subtask subtask);

}

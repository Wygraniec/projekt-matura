package pl.lodz.p.liceum.matura.domain;

public interface TaskExecutor {

    ExecutionStatus execute(Task task);

}
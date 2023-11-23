package pl.lodz.p.liceum.domain;

public interface TaskExecutor {

    ExecutionStatus execute(Task task);

}
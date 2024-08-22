package pl.lodz.p.liceum.matura.domain.task;

import pl.lodz.p.liceum.matura.domain.submission.Submission;
import pl.lodz.p.liceum.matura.domain.subtask.Subtask;

public interface TaskExecutor {

    void executeTask(Task task, Submission submission);
    void executeSubtask(Subtask subtask);

}

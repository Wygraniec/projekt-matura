package pl.lodz.p.liceum.matura.external.worker;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import pl.lodz.p.liceum.matura.domain.task.Subtask;
import pl.lodz.p.liceum.matura.domain.task.Task;
import pl.lodz.p.liceum.matura.domain.task.TaskExecutor;
import pl.lodz.p.liceum.matura.domain.task.TestType;
import pl.lodz.p.liceum.matura.external.worker.kafka.KafkaTaskEvent;
import pl.lodz.p.liceum.matura.external.worker.task.events.SubtaskSentForFastProcessingEvent;
import pl.lodz.p.liceum.matura.external.worker.task.events.SubtaskSentForFullProcessingEvent;
import pl.lodz.p.liceum.matura.external.worker.task.events.SubtaskEventMapper;
import pl.lodz.p.liceum.matura.external.worker.task.events.TaskSentForProcessingEvent;

@RequiredArgsConstructor
@Log
public class TaskWorkerAdapter implements TaskExecutor {

   private final KafkaTaskEvent kafkaTaskEvent;
   private final SubtaskEventMapper subtaskEventMapper;

    @Override
    public void executeTask(Task task) {
        kafkaTaskEvent.send(new TaskSentForProcessingEvent(task.getWorkspaceUrl()));
    }

    @Override
    public void executeSubtask(Subtask subtask) {
        if(subtask.getType() == TestType.FULL)
            kafkaTaskEvent.send(new SubtaskSentForFullProcessingEvent(subtask.getWorkspaceUrl(), subtask.getName()));

        if(subtask.getType() == TestType.FAST)
            kafkaTaskEvent.send(new SubtaskSentForFastProcessingEvent(subtask.getWorkspaceUrl(), subtask.getName()));
    }
}

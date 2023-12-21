package pl.lodz.p.liceum.matura.external.worker;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import pl.lodz.p.liceum.matura.domain.task.Task;
import pl.lodz.p.liceum.matura.domain.task.TaskExecutor;
import pl.lodz.p.liceum.matura.domain.task.TestType;
import pl.lodz.p.liceum.matura.external.worker.kafka.KafkaTaskEvent;
import pl.lodz.p.liceum.matura.external.worker.task.events.SubtaskSentForFastProcessingEvent;
import pl.lodz.p.liceum.matura.external.worker.task.events.SubtaskSentForFullProcessingEvent;
import pl.lodz.p.liceum.matura.external.worker.task.events.TaskEventMapper;
import pl.lodz.p.liceum.matura.external.worker.task.events.TaskSentForProcessingEvent;

@RequiredArgsConstructor
@Log
public class TaskWorkerAdapter implements TaskExecutor {

   private final KafkaTaskEvent kafkaTaskEvent;
   private final TaskEventMapper taskEventMapper;

//    @Override
//    public void execute(Task task) {
////        kafkaTaskEvent.send(taskEventMapper.toDto(task));
//        // TODO implement sending different subtasks
//        kafkaTaskEvent.send(new SubtaskSentForFastProcessingEvent(task.getWorkspaceUrl(), task.getName()));
//    }

    @Override
    public void executeTask(Task task) {
        kafkaTaskEvent.send(new TaskSentForProcessingEvent(task.getWorkspaceUrl()));
    }

    @Override
    public void executeSubtask(Task task) {
        if(task.getType() == TestType.FULL)
            kafkaTaskEvent.send(new SubtaskSentForFullProcessingEvent(task.getWorkspaceUrl(), task.getName()));

        if(task.getType() == TestType.FAST)
            kafkaTaskEvent.send(new SubtaskSentForFastProcessingEvent(task.getWorkspaceUrl(), task.getName()));
    }
}

package pl.lodz.p.liceum.matura.external.worker;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import pl.lodz.p.liceum.matura.domain.task.Task;
import pl.lodz.p.liceum.matura.domain.task.TaskExecutor;
import pl.lodz.p.liceum.matura.external.worker.kafka.KafkaTaskEvent;
import pl.lodz.p.liceum.matura.external.worker.task.events.SubtaskSentForFastProcessingEvent;
import pl.lodz.p.liceum.matura.external.worker.task.events.TaskEventMapper;
import pl.lodz.p.liceum.matura.external.worker.task.events.TaskSentForProcessingEvent;

@RequiredArgsConstructor
@Log
public class TaskWorkerAdapter implements TaskExecutor {

   private final KafkaTaskEvent kafkaTaskEvent;
   private final TaskEventMapper taskEventMapper;

    @Override
    public void execute(Task task) {
//        kafkaTaskEvent.send(taskEventMapper.toDto(task));
        // TODO implement sending different subtasks
        kafkaTaskEvent.send(new SubtaskSentForFastProcessingEvent(task.getWorkspaceUrl(), task.getName()));
    }

}

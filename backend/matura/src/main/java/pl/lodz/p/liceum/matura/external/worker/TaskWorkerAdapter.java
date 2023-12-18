package pl.lodz.p.liceum.matura.external.worker;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import pl.lodz.p.liceum.matura.domain.task.Task;
import pl.lodz.p.liceum.matura.domain.task.TaskExecutor;
import pl.lodz.p.liceum.matura.external.worker.kafka.KafkaTaskEvent;
import pl.lodz.p.liceum.matura.external.worker.task.TaskEventMapper;
import pl.lodz.p.liceum.matura.external.worker.task.TaskSentForProcessingEvent;

@RequiredArgsConstructor
@Log
public class TaskWorkerAdapter implements TaskExecutor {

   private final KafkaTaskEvent kafkaTaskEvent;
   private final TaskEventMapper taskEventMapper;

    @Override
    public void execute(Task task) {
//        kafkaTaskEvent.send(taskEventMapper.toDto(task));
        kafkaTaskEvent.send(new TaskSentForProcessingEvent(task.getWorkspaceUrl()));
    }

}

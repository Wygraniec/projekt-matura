package pl.lodz.p.liceum.matura.external;


import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import pl.lodz.p.liceum.matura.config.KafkaConfiguration;
import pl.lodz.p.liceum.matura.domain.Task;
import pl.lodz.p.liceum.matura.domain.TaskExecutor;
import pl.lodz.p.liceum.matura.domain.TestType;
import pl.lodz.p.liceum.matura.external.worker.task.events.SubtaskSentForFastProcessingEvent;
import pl.lodz.p.liceum.matura.external.worker.task.events.SubtaskSentForFullProcessingEvent;
import pl.lodz.p.liceum.matura.external.worker.task.events.TaskEvent;
import pl.lodz.p.liceum.matura.external.worker.task.events.TaskEventMapper;
import pl.lodz.p.liceum.matura.external.worker.task.events.TaskSentForProcessingEvent;

@Log
@Service
@RequiredArgsConstructor
public class KafkaTaskProcessor {

    private final KafkaTemplate<String, TaskEvent> kafkaTemplate;
    private final TaskExecutor taskExecutor;
    private final TaskEventMapper taskEventMapper;

//    @KafkaListener(topics = KafkaConfiguration.TASKS_INBOUND_TOPIC, groupId = KafkaConfiguration.KAFKA_GROUP_ID, containerFactory = "taskKafkaListenerFactory")
//    public void onReceive(TaskEvent taskEvent) {
//        log.info("Received TaskEvent: " + taskEventMapper.toDomain(taskEvent));
//        var executionResult = taskExecutor.execute(taskEventMapper.toDomain(taskEvent));
//        var task = taskEventMapper.toDomain(taskEvent);
//        kafkaTemplate.send(KafkaConfiguration.TASKS_OUTBOUND_TOPIC, taskEventMapper.toDto(task));
//        log.info("Sent TaskEvent: " + task);
//    }

    @KafkaListener(topics = KafkaConfiguration.TASKS_INBOUND_TOPIC, groupId = KafkaConfiguration.KAFKA_GROUP_ID, containerFactory = "taskKafkaListenerFactory")
    public void onReceive(TaskEvent taskEvent) {
//        log.info("Received TaskEvent: " + taskEventMapper.toDomain(taskEvent));
//        var executionResult = taskExecutor.execute(taskEventMapper.toDomain(taskEvent));
//        var task = taskEventMapper.toDomain(taskEvent);
//        kafkaTemplate.send(KafkaConfiguration.TASKS_OUTBOUND_TOPIC, taskEventMapper.toDto(task));
//        log.info("Sent TaskEvent: " + task);

        if (taskEvent instanceof TaskSentForProcessingEvent) {
            log.info("Received TaskSentForProcessingEvent: " + taskEvent);
        } else if (taskEvent instanceof SubtaskSentForFastProcessingEvent subtaskSentForFastProcessingEvent) {
            log.info("Received SubtaskSentForFastProcessingEvent: " + taskEvent);
            Task task = new Task(taskEvent.getWorkspaceUrl(), subtaskSentForFastProcessingEvent.getName(), TestType.FAST);
            taskExecutor.execute(task);
        } else if (taskEvent instanceof SubtaskSentForFullProcessingEvent subtaskSentForFullProcessingEvent) {
            log.info("Received SubtaskSentForFullProcessingEvent: " + taskEvent);
            Task task = new Task(taskEvent.getWorkspaceUrl(), subtaskSentForFullProcessingEvent.getName(), TestType.FULL);
            taskExecutor.execute(task);
        } else {
            log.info("Received TaskEvent: " + taskEvent);
        }
    }

}

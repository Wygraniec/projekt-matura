package pl.lodz.p.liceum.matura.external;


import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import pl.lodz.p.liceum.matura.config.KafkaConfiguration;
import pl.lodz.p.liceum.matura.domain.*;
import pl.lodz.p.liceum.matura.external.worker.task.events.*;

@Log
@Service
@RequiredArgsConstructor
public class KafkaTaskProcessor {

    private final KafkaTemplate<String, TaskEvent> kafkaTemplate;
    private final TaskExecutor taskExecutor;
    private final TaskEventMapper taskEventMapper;



    @KafkaListener(topics = KafkaConfiguration.TASKS_INBOUND_TOPIC, groupId = KafkaConfiguration.KAFKA_GROUP_ID, containerFactory = "taskKafkaListenerFactory")
    public void onReceive(TaskEvent taskEvent) {
        if (taskEvent instanceof TaskSentForProcessingEvent) {
            log.info("Received TaskSentForProcessingEvent: " + taskEvent);
            Task task = new Task(taskEvent.getWorkspaceUrl());
            ExecutionStatus status = taskExecutor.executeTask(task);

            if(status == ExecutionStatus.FAILED) {
                kafkaTemplate.send(
                        KafkaConfiguration.TASKS_OUTBOUND_TOPIC,
                        new TaskProcessingFailedEvent(task.getWorkspaceUrl())
                );
                return;
            }

            kafkaTemplate.send(
                    KafkaConfiguration.TASKS_OUTBOUND_TOPIC,
                    new TaskProcessingCompleteEvent(task.getWorkspaceUrl())
            );

        } else if (taskEvent instanceof SubtaskSentForFastProcessingEvent subtaskSentForFastProcessingEvent) {
            log.info("Received SubtaskSentForFastProcessingEvent: " + taskEvent);
            Subtask subtask = new Subtask(taskEvent.getWorkspaceUrl(), subtaskSentForFastProcessingEvent.getName(), TestType.FAST);
            ExecutionStatus status = taskExecutor.executeSubtask(subtask);

            if(status == ExecutionStatus.FAILED) {
                kafkaTemplate.send(
                        KafkaConfiguration.TASKS_OUTBOUND_TOPIC,
                        new SubtaskProcessingFailedEvent(subtask.getWorkspaceUrl(), subtask.getName())
                );
                return;
            }

            kafkaTemplate.send(
                    KafkaConfiguration.TASKS_OUTBOUND_TOPIC,
                    new SubtaskFastProcessingCompleteEvent(subtask.getWorkspaceUrl(), subtask.getName())
            );

        } else if (taskEvent instanceof SubtaskSentForFullProcessingEvent subtaskSentForFullProcessingEvent) {
            log.info("Received SubtaskSentForFullProcessingEvent: " + taskEvent);
            Subtask subtask = new Subtask(taskEvent.getWorkspaceUrl(), subtaskSentForFullProcessingEvent.getName(), TestType.FULL);
            ExecutionStatus status = taskExecutor.executeSubtask(subtask);

            if(status == ExecutionStatus.FAILED) {
                kafkaTemplate.send(
                        KafkaConfiguration.TASKS_OUTBOUND_TOPIC,
                        new SubtaskProcessingFailedEvent(subtask.getWorkspaceUrl(), subtask.getName())
                );
                return;
            }

            kafkaTemplate.send(
                    KafkaConfiguration.TASKS_OUTBOUND_TOPIC,
                    new SubtaskFullProcessingCompleteEvent(subtask.getWorkspaceUrl(), subtask.getName())
            );

        } else {
            log.info("Received TaskEvent: " + taskEvent);
        }
    }

}

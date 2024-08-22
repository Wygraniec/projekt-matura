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
        if (taskEvent instanceof TaskSentForProcessingEvent taskSentForProcessingEvent) {
            log.info("Received TaskSentForProcessingEvent: " + taskEvent);

            Task task = new Task(taskEvent.getTaskId(), taskEvent.getWorkspaceUrl());

            for (int i = 1; i <= taskSentForProcessingEvent.getNumberOfSubtasks(); i++) {
                Subtask subtask = new Subtask(taskEvent.getWorkspaceUrl(), i, TestType.FULL);
                Result result = taskExecutor.executeSubtask(subtask);

                if (result.getExecutionStatus() == ExecutionStatus.FAILED) {
                    kafkaTemplate.send(
                            KafkaConfiguration.TASKS_OUTBOUND_TOPIC,
                            new SubtaskProcessingFailedEvent(taskEvent.getTaskId(), taskSentForProcessingEvent.getSubmissionId(), subtask.getWorkspaceUrl(), subtask.getNumber())
                    );
                    return;
                }

                kafkaTemplate.send(
                        KafkaConfiguration.TASKS_OUTBOUND_TOPIC,
                        new SubtaskFullProcessingCompleteEvent(taskEvent.getTaskId(), taskSentForProcessingEvent.getSubmissionId(), subtask.getWorkspaceUrl(), subtask.getNumber(), result.getScore())
                );
            }

            kafkaTemplate.send(
                    KafkaConfiguration.TASKS_OUTBOUND_TOPIC,
                    new TaskProcessingCompleteEvent(task.getTaskId(), taskSentForProcessingEvent.getSubmissionId(), task.getWorkspaceUrl())
            );

        } else if (taskEvent instanceof SubtaskSentForFastProcessingEvent subtaskSentForFastProcessingEvent) {
            log.info("Received SubtaskSentForFastProcessingEvent: " + taskEvent);
            Subtask subtask = new Subtask(taskEvent.getWorkspaceUrl(), subtaskSentForFastProcessingEvent.getNumber(), TestType.FAST);
            Result result = taskExecutor.executeSubtask(subtask);

            if (result.getExecutionStatus() == ExecutionStatus.FAILED) {
                kafkaTemplate.send(
                        KafkaConfiguration.TASKS_OUTBOUND_TOPIC,
                        new SubtaskProcessingFailedEvent(taskEvent.getTaskId(), subtaskSentForFastProcessingEvent.getSubmissionId(), subtask.getWorkspaceUrl(), subtask.getNumber())
                );
                return;
            }

            kafkaTemplate.send(
                    KafkaConfiguration.TASKS_OUTBOUND_TOPIC,
                    new SubtaskFastProcessingCompleteEvent(taskEvent.getTaskId(), subtaskSentForFastProcessingEvent.getSubmissionId(), subtask.getWorkspaceUrl(), subtask.getNumber(), result.getScore())
            );

        } else if (taskEvent instanceof SubtaskSentForFullProcessingEvent subtaskSentForFullProcessingEvent) {
            log.info("Received SubtaskSentForFullProcessingEvent: " + taskEvent);
            Subtask subtask = new Subtask(taskEvent.getWorkspaceUrl(), subtaskSentForFullProcessingEvent.getNumber(), TestType.FULL);
            Result result = taskExecutor.executeSubtask(subtask);

            if (result.getExecutionStatus() == ExecutionStatus.FAILED) {
                kafkaTemplate.send(
                        KafkaConfiguration.TASKS_OUTBOUND_TOPIC,
                        new SubtaskProcessingFailedEvent(taskEvent.getTaskId(), subtaskSentForFullProcessingEvent.getSubmissionId(), subtask.getWorkspaceUrl(), subtask.getNumber())
                );
                return;
            }

            kafkaTemplate.send(
                    KafkaConfiguration.TASKS_OUTBOUND_TOPIC,
                    new SubtaskFullProcessingCompleteEvent(taskEvent.getTaskId(), subtaskSentForFullProcessingEvent.getSubmissionId(), subtask.getWorkspaceUrl(), subtask.getNumber(), result.getScore())
            );

        } else {
            log.info("Received TaskEvent: " + taskEvent);
        }
    }
}

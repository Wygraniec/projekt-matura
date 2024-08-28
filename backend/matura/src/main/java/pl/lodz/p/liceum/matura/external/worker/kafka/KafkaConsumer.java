package pl.lodz.p.liceum.matura.external.worker.kafka;

import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import pl.lodz.p.liceum.matura.config.KafkaConfiguration;
import pl.lodz.p.liceum.matura.domain.result.Result;
import pl.lodz.p.liceum.matura.domain.result.ResultService;
import pl.lodz.p.liceum.matura.domain.subtask.Subtask;
import pl.lodz.p.liceum.matura.domain.task.Task;
import pl.lodz.p.liceum.matura.domain.task.TaskService;
import pl.lodz.p.liceum.matura.domain.task.TaskState;
import pl.lodz.p.liceum.matura.domain.template.Template;
import pl.lodz.p.liceum.matura.domain.template.TemplateService;
import pl.lodz.p.liceum.matura.external.worker.task.events.*;

import java.time.Clock;
import java.time.ZonedDateTime;
import java.util.List;


@Log
@AllArgsConstructor
@Service
public class KafkaConsumer {

    private final SubtaskEventMapper subtaskEventMapper;
    private final TaskEventMapper taskEventMapper;
    private final ResultService resultService;
    private final TemplateService templateService;
    private final TaskService taskService;
    private final Clock clock;

    @KafkaListener(topics = KafkaConfiguration.TASKS_INBOUND_TOPIC, groupId = KafkaConfiguration.KAFKA_GROUP_ID,
            containerFactory = "taskKafkaListenerFactory")
    public void onReceive(TaskEvent taskEvent) {

        if (taskEvent instanceof TaskProcessingCompleteEvent) {
            Task task = taskService.findById(taskEvent.getTaskId());
            Template template = templateService.findById(task.getTemplateId());
            log.info("Processing of task completed successfully at " + task.getWorkspaceUrl());
            List<Result> results = resultService.findBySubmissionId(taskEvent.getSubmissionId());
            if (results.size() == template.getNumberOfSubtasks() && results.stream().allMatch(r -> r.getScore() == 100)) {
                updateTaskState(task, TaskState.FINISHED);
            }
            else {
                updateTaskState(task, TaskState.CREATED);
            }
        } else if (taskEvent instanceof SubtaskFastProcessingCompleteEvent event) {
            Subtask subtask = subtaskEventMapper.toDomain(event);
            log.info(String.format("Fast processing of subtask %s completed successfully with score %d", subtask.getNumber(), event.getScore()));
            Result result = new Result(null, subtask.getSubmissionId(), subtask.getNumber(), event.getDescription(), event.getScore(), ZonedDateTime.now(clock));
            resultService.save(result);
        } else if (taskEvent instanceof SubtaskFullProcessingCompleteEvent event) {
            Subtask subtask = subtaskEventMapper.toDomain(event);
            log.info(String.format("Full processing of subtask %s completed successfully with score %d", subtask.getNumber(), event.getScore()));
            Result result = new Result(null, subtask.getSubmissionId(), subtask.getNumber(), event.getDescription(), event.getScore(), ZonedDateTime.now(clock));
            resultService.save(result);
        } else if (taskEvent instanceof TaskProcessingFailedEvent) {
            Task task = taskEventMapper.toDomain(taskEvent);
            log.info("Processing of task failed at " + task.getWorkspaceUrl());
        } else if (taskEvent instanceof SubtaskProcessingFailedEvent event) {
            Subtask subtask = subtaskEventMapper.toDomain(event);
            log.info(String.format("Processing of subtask %s failed", subtask.getNumber()));
        } else {
            log.info("Received taskEvent: " + taskEvent);
        }

    }
    private void updateTaskState(Task task, TaskState state) {
        task.setState(state);
        taskService.update(task);
    }
}

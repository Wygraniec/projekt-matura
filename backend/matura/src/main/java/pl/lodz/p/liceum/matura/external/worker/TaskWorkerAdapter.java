package pl.lodz.p.liceum.matura.external.worker;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import pl.lodz.p.liceum.matura.domain.submission.Submission;
import pl.lodz.p.liceum.matura.domain.submission.VerificationType;
import pl.lodz.p.liceum.matura.domain.subtask.Subtask;
import pl.lodz.p.liceum.matura.domain.task.Task;
import pl.lodz.p.liceum.matura.domain.task.TaskExecutor;
import pl.lodz.p.liceum.matura.domain.task.TaskService;
import pl.lodz.p.liceum.matura.domain.template.Template;
import pl.lodz.p.liceum.matura.domain.template.TemplateService;
import pl.lodz.p.liceum.matura.external.worker.kafka.KafkaTaskEvent;
import pl.lodz.p.liceum.matura.external.worker.task.events.SubtaskSentForFastProcessingEvent;
import pl.lodz.p.liceum.matura.external.worker.task.events.SubtaskSentForFullProcessingEvent;
import pl.lodz.p.liceum.matura.external.worker.task.events.TaskSentForProcessingEvent;

@RequiredArgsConstructor
@Log
public class TaskWorkerAdapter implements TaskExecutor {

    private final KafkaTaskEvent kafkaTaskEvent;
    private final TaskService taskService;
    private final TemplateService templateService;

    @Override
    public void executeTask(Task task, Submission submission) {
        Template template = templateService.findById(task.getTemplateId());
        kafkaTaskEvent.send(new TaskSentForProcessingEvent(task.getId(), submission.getId(), template.getNumberOfSubtasks(), task.getWorkspaceUrl()));
    }

    @Override
    public void executeSubtask(Subtask subtask) {
        Task task = taskService.findById(subtask.getTaskId());
        if (subtask.getType() == VerificationType.FULL)
            kafkaTaskEvent.send(new SubtaskSentForFullProcessingEvent(task.getId(), subtask.getSubmissionId(), task.getWorkspaceUrl(), subtask.getNumber()));

        if (subtask.getType() == VerificationType.FAST)
            kafkaTaskEvent.send(new SubtaskSentForFastProcessingEvent(task.getId(), subtask.getSubmissionId(), task.getWorkspaceUrl(), subtask.getNumber()));
    }
}

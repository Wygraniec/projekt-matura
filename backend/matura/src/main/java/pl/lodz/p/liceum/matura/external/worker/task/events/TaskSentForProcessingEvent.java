package pl.lodz.p.liceum.matura.external.worker.task.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskSentForProcessingEvent extends TaskEvent {
    Integer taskId;
    Integer submissionId;
    Integer numberOfSubtasks;
    String workspaceUrl;
}

package pl.lodz.p.liceum.matura.external.worker.task.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubtaskFastProcessingCompleteEvent extends SubtaskEvent {
    Integer taskId;
    Integer submissionId;
    String workspaceUrl;
    Integer number;
    Integer score;
    String description;
}

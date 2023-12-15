package pl.lodz.p.liceum.matura.external.worker.task;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubtaskFullProcessingCompleteEvent extends TaskEvent {
    String workspaceUrl;
    String name;
}

package pl.lodz.p.liceum.matura.external.worker.task.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.liceum.matura.domain.task.TestType;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskEvent {

    String workspaceUrl;

}


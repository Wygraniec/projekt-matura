package pl.lodz.p.liceum.matura.domain.subtask;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.liceum.matura.domain.task.TestType;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Subtask {

    Integer id;
    Integer taskId;
    String name;
    TestType type;
}

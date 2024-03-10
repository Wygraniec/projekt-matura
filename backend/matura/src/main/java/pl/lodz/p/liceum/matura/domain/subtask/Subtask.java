package pl.lodz.p.liceum.matura.domain.subtask;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.liceum.matura.domain.submission.VerificationType;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Subtask {
    Integer taskId;
    String name;
    VerificationType type;
}

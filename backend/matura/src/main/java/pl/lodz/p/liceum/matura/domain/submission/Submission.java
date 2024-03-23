package pl.lodz.p.liceum.matura.domain.submission;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Submission {

    private Integer id;
    private Integer taskId;
    private VerificationType verification;
    private Integer submittedBy;
    private ZonedDateTime submittedAt;
}

package pl.lodz.p.liceum.matura.domain.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result {
    private Integer id;
    private Integer submissionId;
    private Integer subtaskNumber;
    private String description;
    private Integer score;
    private ZonedDateTime createdAt;

}

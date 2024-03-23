package pl.lodz.p.liceum.matura.domain;

import lombok.Data;

@Data
public class Result {
    private ExecutionStatus executionStatus;
    private Integer score;
    private String description;
}

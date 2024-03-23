package pl.lodz.p.liceum.matura.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Subtask extends Task {
    Integer index;
    TestType type;

    public Subtask(String workspaceUrl, Integer index, TestType type) {
        this(index, type);
        this.workspaceUrl = workspaceUrl;
    }
}

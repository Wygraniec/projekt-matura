package pl.lodz.p.liceum.matura.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Subtask extends Task {
    String name;
    TestType type;

    public Subtask(String workspaceUrl, String name, TestType type) {
        this(name, type);
        this.workspaceUrl = workspaceUrl;
    }
}

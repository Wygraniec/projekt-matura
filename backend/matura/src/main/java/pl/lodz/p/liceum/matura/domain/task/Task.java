package pl.lodz.p.liceum.matura.domain.task;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Task {

    String workspaceUrl;
    String name;
    TestType type;

}

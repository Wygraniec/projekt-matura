package pl.lodz.p.liceum.matura.external.worker.task.definition;

import lombok.Data;
import pl.lodz.p.liceum.matura.domain.TestType;

import java.util.List;
import java.util.Map;

@Data
public class Subtask {
    List<String> files;

    Map<String, CheckData> checkTypes;

    List<String> results;
}

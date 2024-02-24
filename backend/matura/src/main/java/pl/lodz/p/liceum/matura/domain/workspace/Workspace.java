package pl.lodz.p.liceum.matura.domain.workspace;

import java.util.List;
import java.util.Map;

public interface Workspace {

    String createWorkspace(String sourceRepositoryUrl);

    Map<String, Object> readTaskDefinitionFile(String rootPathUrl);

    void writeFile(String rootPathUrl, String path, byte[] bytes);

    byte[] readFile(String rootPathUrl, String path);

    void commitChanges(String rootPathUrl);

}

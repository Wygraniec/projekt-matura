package pl.lodz.p.liceum.matura.external.worker.task;

import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import pl.lodz.p.liceum.matura.external.worker.task.definition.TaskDefinition;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;


public class YamlTaskDefinitionParser implements TaskDefinitionParser {

    @Override
    public TaskDefinition parse(String taskDefinitionPath) {
        try {
            InputStream inputStream = new FileInputStream(taskDefinitionPath);

            var loaderOptions = new LoaderOptions();
            loaderOptions.setEnumCaseSensitive(false);
            Yaml yaml = new Yaml(new Constructor(TaskDefinition.class, loaderOptions));

            return yaml.load(inputStream);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}

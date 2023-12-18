package pl.lodz.p.liceum.matura.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.lodz.p.liceum.matura.external.worker.task.DockerComposeGenerator;
import pl.lodz.p.liceum.matura.external.worker.task.TaskDefinitionParser;
import pl.lodz.p.liceum.matura.external.worker.task.YamlTaskDefinitionParser;

@Configuration
@ConfigurationProperties("domain.properties")
public class DomainConfiguration {

    @Bean
    public TaskDefinitionParser taskDefinitionParser() {
        return new YamlTaskDefinitionParser();
    }
    @Bean
    public DockerComposeGenerator dockerComposeGenerator() {
        return new DockerComposeGenerator();
    }
}

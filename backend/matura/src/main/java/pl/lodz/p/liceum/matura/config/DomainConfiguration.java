package pl.lodz.p.liceum.matura.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.lodz.p.liceum.matura.domain.task.TaskExecutor;
import pl.lodz.p.liceum.matura.domain.user.EncodingService;
import pl.lodz.p.liceum.matura.domain.user.UserRepository;
import pl.lodz.p.liceum.matura.domain.user.UserService;
import pl.lodz.p.liceum.matura.external.worker.TaskWorkerAdapter;
import pl.lodz.p.liceum.matura.external.worker.kafka.KafkaTaskEvent;
import pl.lodz.p.liceum.matura.external.worker.task.TaskEventMapper;
import pl.lodz.p.liceum.matura.external.storage.user.JpaUserRepository;
import pl.lodz.p.liceum.matura.external.storage.user.UserEntityMapper;
import pl.lodz.p.liceum.matura.external.storage.user.UserStorageAdapter;

import java.time.Clock;

@Configuration
@ConfigurationProperties("domain.properties")
public class DomainConfiguration {

    @Bean
    public Clock clock() {
        return Clock.systemDefaultZone();
    }

    @Bean
    public UserRepository userRepository(JpaUserRepository jpaUserRepository, UserEntityMapper mapper) {
        return new UserStorageAdapter(jpaUserRepository, mapper);
    }

    @Bean
    public UserService userService(UserRepository userRepository, EncodingService encoder, Clock clock) {
        return new UserService(userRepository, encoder, clock);
    }

    @Bean
    public TaskExecutor taskExecutor1(KafkaTaskEvent kafkaTaskEvent, TaskEventMapper taskEventMapper) {
        return new TaskWorkerAdapter(kafkaTaskEvent, taskEventMapper);
    }

}

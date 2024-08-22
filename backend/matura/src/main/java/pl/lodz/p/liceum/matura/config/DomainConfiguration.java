package pl.lodz.p.liceum.matura.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.lodz.p.liceum.matura.domain.result.ResultRepository;
import pl.lodz.p.liceum.matura.domain.result.ResultService;
import pl.lodz.p.liceum.matura.domain.submission.SubmissionRepository;
import pl.lodz.p.liceum.matura.domain.submission.SubmissionService;
import pl.lodz.p.liceum.matura.external.storage.result.JpaResultRepository;
import pl.lodz.p.liceum.matura.external.storage.result.ResultEntityMapper;
import pl.lodz.p.liceum.matura.external.storage.result.ResultStorageAdapter;
import pl.lodz.p.liceum.matura.external.storage.submissions.JpaSubmissionRepository;
import pl.lodz.p.liceum.matura.external.storage.submissions.SubmissionEntityMapper;
import pl.lodz.p.liceum.matura.external.storage.submissions.SubmissionStorageAdapter;
import pl.lodz.p.liceum.matura.external.workspace.WorkspaceService;
import pl.lodz.p.liceum.matura.domain.task.TaskExecutor;
import pl.lodz.p.liceum.matura.domain.task.TaskRepository;
import pl.lodz.p.liceum.matura.domain.task.TaskService;
import pl.lodz.p.liceum.matura.domain.template.TemplateRepository;
import pl.lodz.p.liceum.matura.domain.template.TemplateService;
import pl.lodz.p.liceum.matura.domain.user.EncodingService;
import pl.lodz.p.liceum.matura.domain.user.UserRepository;
import pl.lodz.p.liceum.matura.domain.user.UserService;
import pl.lodz.p.liceum.matura.domain.workspace.Workspace;
import pl.lodz.p.liceum.matura.external.storage.task.JpaTaskRepository;
import pl.lodz.p.liceum.matura.external.storage.task.TaskEntityMapper;
import pl.lodz.p.liceum.matura.external.storage.task.TaskStorageAdapter;
import pl.lodz.p.liceum.matura.external.storage.template.JpaTemplateRepository;
import pl.lodz.p.liceum.matura.external.storage.template.TemplateEntityMapper;
import pl.lodz.p.liceum.matura.external.storage.template.TemplateStorageAdapter;
import pl.lodz.p.liceum.matura.external.worker.TaskWorkerAdapter;
import pl.lodz.p.liceum.matura.external.worker.kafka.KafkaTaskEvent;
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
    public UserService userService(UserRepository userRepository, EncodingService encoder) {
        return new UserService(userRepository, encoder);
    }

    @Bean
    public TaskExecutor taskExecutor1(KafkaTaskEvent kafkaTaskEvent, TaskService taskService, TemplateService templateService) {
        return new TaskWorkerAdapter(kafkaTaskEvent, taskService, templateService);
    }

    @Bean
    public TemplateRepository templateRepository(JpaTemplateRepository jpaTemplateRepository, TemplateEntityMapper mapper) {
        return new TemplateStorageAdapter(jpaTemplateRepository, mapper);
    }

    @Bean
    public TemplateService templateService(TemplateRepository templateRepository) {
        return new TemplateService(templateRepository);
    }

    @Bean
    public TaskRepository taskRepository(JpaTaskRepository jpaTaskRepository, TaskEntityMapper mapper) {
        return new TaskStorageAdapter(jpaTaskRepository, mapper);
    }

    @Bean
    public TaskService taskService(TaskRepository taskRepository) {
        return new TaskService(taskRepository);
    }

    @Bean
    public Workspace workspace() {
        return new WorkspaceService("UserWorkspaces");
    }

    @Bean
    public SubmissionRepository submissionRepository(JpaSubmissionRepository jpaSubmissionRepository, SubmissionEntityMapper mapper) {
        return new SubmissionStorageAdapter(jpaSubmissionRepository, mapper);
    }

    @Bean
    public SubmissionService submissionService(SubmissionRepository submissionRepository) {
        return new SubmissionService(submissionRepository);
    }
    @Bean
    public ResultRepository resultRepository(JpaResultRepository jpaResultRepository, ResultEntityMapper mapper) {
        return new ResultStorageAdapter(jpaResultRepository, mapper);
    }

    @Bean
    public ResultService resultService(ResultRepository resultRepository) {
        return new ResultService(resultRepository);
    }
}

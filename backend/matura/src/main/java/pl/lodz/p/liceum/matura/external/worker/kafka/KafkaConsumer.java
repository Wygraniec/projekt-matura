package pl.lodz.p.liceum.matura.external.worker.kafka;

import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import pl.lodz.p.liceum.matura.config.KafkaConfiguration;
import pl.lodz.p.liceum.matura.domain.task.Task;
import pl.lodz.p.liceum.matura.external.worker.task.events.TaskEvent;
import pl.lodz.p.liceum.matura.external.worker.task.events.TaskEventMapper;


@Log
@AllArgsConstructor
@Service
public class KafkaConsumer {

    private final TaskEventMapper taskEventMapper;

    @KafkaListener(topics = KafkaConfiguration.TASKS_INBOUND_TOPIC, groupId = KafkaConfiguration.KAFKA_GROUP_ID,
            containerFactory = "taskKafkaListenerFactory")
    public void onReceive(TaskEvent taskEvent) {
        Task task = taskEventMapper.toDomain(taskEvent);
        log.info("Received TaskEvent: " + task);
    }
}

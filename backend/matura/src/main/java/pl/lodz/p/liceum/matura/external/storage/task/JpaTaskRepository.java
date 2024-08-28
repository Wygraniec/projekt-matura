package pl.lodz.p.liceum.matura.external.storage.task;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.lodz.p.liceum.matura.domain.task.TaskState;

import javax.swing.plaf.nimbus.State;
import java.util.List;
import java.util.Optional;

public interface JpaTaskRepository extends JpaRepository<TaskEntity, Integer> {
    List<TaskEntity> findByUserId(Integer usedId);
    List<TaskEntity> findByCreatedBy(Integer usedId);
    Page<TaskEntity> findByUserIdAndStateIn(Integer usedId, List<TaskState> taskStates, Pageable pageable);
    Optional<TaskEntity> findByTemplateIdAndUserIdAndStateIn(Integer templateId, Integer userId, List<TaskState> taskStates);

}

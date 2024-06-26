package pl.lodz.p.liceum.matura.domain.task;

import org.springframework.data.domain.Pageable;
import pl.lodz.p.liceum.matura.domain.user.PageUser;
import pl.lodz.p.liceum.matura.domain.user.User;

import java.util.List;
import java.util.Optional;

public interface TaskRepository {
    Task save(Task task);

    void update(Task task);

    void remove(Integer id);


    Optional<Task> findById(Integer id);
    List<Task> findByCreatedBy(Integer createdAtId);
    List<Task> findByUserId(Integer userId);
    PageTask findByUserIdAndStateIn(final Integer userId, List<TaskState> taskStates, Pageable pageable);
    Optional<Task> findByTemplateIdAndUserId(Integer templateId, Integer userId);
}

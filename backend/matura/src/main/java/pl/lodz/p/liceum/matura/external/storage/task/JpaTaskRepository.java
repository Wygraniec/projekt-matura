package pl.lodz.p.liceum.matura.external.storage.task;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaTaskRepository extends JpaRepository<TaskEntity, Integer> {
    List<TaskEntity> findByUserId(Integer usedId);
    List<TaskEntity> findByCreatedBy(Integer usedId);
}

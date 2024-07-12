package pl.lodz.p.liceum.matura.external.storage.result;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaResultRepository extends JpaRepository<ResultEntity, Integer> {
    List<ResultEntity> findBySubmissionId(Integer submissionId);
}

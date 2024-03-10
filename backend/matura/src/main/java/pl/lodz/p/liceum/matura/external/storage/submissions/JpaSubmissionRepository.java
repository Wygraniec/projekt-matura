package pl.lodz.p.liceum.matura.external.storage.submissions;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaSubmissionRepository extends JpaRepository<SubmissionEntity, Integer> {

}

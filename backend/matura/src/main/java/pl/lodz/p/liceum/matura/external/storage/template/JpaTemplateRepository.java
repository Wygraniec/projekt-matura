package pl.lodz.p.liceum.matura.external.storage.template;

import io.micrometer.observation.ObservationFilter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaTemplateRepository extends JpaRepository<TemplateEntity, Integer> {
    Optional<TemplateEntity> findBySourceUrl(String sourceUrl);
}

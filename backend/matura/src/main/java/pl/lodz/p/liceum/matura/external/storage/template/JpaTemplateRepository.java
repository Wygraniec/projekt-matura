package pl.lodz.p.liceum.matura.external.storage.template;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.lodz.p.liceum.matura.domain.template.TaskLanguage;

import java.util.Optional;

public interface JpaTemplateRepository extends JpaRepository<TemplateEntity, Integer> {
    Optional<TemplateEntity> findBySourceUrl(String sourceUrl);
    Page<TemplateEntity> findByTaskLanguage(TaskLanguage taskLanguage, Pageable pageable);
    Page<TemplateEntity> findBySourceLike(String source, Pageable pageable);
    Page<TemplateEntity> findByTaskLanguageAndSourceLike(TaskLanguage taskLanguage, String source, Pageable pageable);
}

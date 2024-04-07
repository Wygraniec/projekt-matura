package pl.lodz.p.liceum.matura.domain.template;

import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface TemplateRepository {
    Template save(Template template);
    void update(Template template);
    void remove(Integer id);
    Optional<Template> findBySourceUrl(String sourceUrl);
    Optional<Template> findById(Integer id);
    PageTemplate findAll(Pageable pageable);
    PageTemplate findByTaskLanguage(TaskLanguage taskLanguage, Pageable pageable);
    PageTemplate findBySource(String source, Pageable pageable);
}

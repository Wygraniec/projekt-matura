package pl.lodz.p.liceum.matura.domain.template;

import org.springframework.data.domain.Pageable;
import pl.lodz.p.liceum.matura.domain.user.PageUser;
import pl.lodz.p.liceum.matura.domain.user.User;

import java.util.Optional;

public interface TemplateRepository {
    Template save(Template template);
    void update(Template template);
    void remove(Integer id);
    Optional<Template> findBySourceUrl(String sourceUrl);
    Optional<Template> findById(Integer id);
    PageTemplate findAll(Pageable pageable);
}

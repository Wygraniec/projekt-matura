package pl.lodz.p.liceum.matura.domain.template;

import pl.lodz.p.liceum.matura.domain.user.User;

import java.util.Optional;

public interface TemplateRepository {
    Template save(Template template);
    void update(Template template);
    void remove(Integer id);
    Optional<Template> findBySourceUrl(String sourceUrl);
    Optional<Template> findById(Integer id);
}

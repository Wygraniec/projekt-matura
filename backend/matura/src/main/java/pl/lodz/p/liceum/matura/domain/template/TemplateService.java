package pl.lodz.p.liceum.matura.domain.template;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TemplateService {
    private final TemplateRepository templateRepository;
    public Template save(Template template) {
        return templateRepository.save(template);
    }

    public void update(Template template) {
        templateRepository.update(template);
    }

    public void removeById(Integer id) {
        templateRepository.remove(id);
    }

    public Template findById(Integer id) {
        return templateRepository
                .findById(id)
                .orElseThrow(TemplateNotFoundException::new);
    }
}

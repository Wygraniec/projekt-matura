package pl.lodz.p.liceum.matura.domain.template;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

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
    public Template findBySourceUrl(String sourceUrl) {
        return templateRepository
                .findBySourceUrl(sourceUrl)
                .orElseThrow(TemplateNotFoundException::new);
    }

    public Template findById(Integer id) {
        return templateRepository
                .findById(id)
                .orElseThrow(TemplateNotFoundException::new);
    }
    public PageTemplate findAll(Pageable pageable) {
        return templateRepository.findAll(pageable);
    }
    public PageTemplate findByTaskLanguage(TaskLanguage taskLanguage, Pageable pageable) {
        return templateRepository.findByTaskLanguage(taskLanguage, pageable);
    }
}

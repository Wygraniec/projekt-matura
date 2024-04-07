package pl.lodz.p.liceum.matura.external.storage.template;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.lodz.p.liceum.matura.domain.template.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Log
public class TemplateStorageAdapter implements TemplateRepository {

    private final JpaTemplateRepository templateRepository;
    private final TemplateEntityMapper mapper;

    @Override
    public Template save(final Template template) {
        try {
            TemplateEntity saved = templateRepository.save(mapper.toEntity(template));
            log.info("Saved entity " + saved);
            return mapper.toDomain(saved);
        } catch (DataIntegrityViolationException ex) {
            log.warning("Template " + template.getId() + " already exits in db");
            throw new TemplateAlreadyExistsException();
        }
    }

    @Override
    public void update(final Template template) {
        templateRepository
                .findById(template.getId())
                .ifPresent(userEntity -> templateRepository.save(mapper.toEntity(template)));
    }

    @Override
    public void remove(final Integer id) {
        templateRepository
                .findById(id)
                .ifPresent(templateEntity -> templateRepository.deleteById(id));
    }

    @Override
    public Optional<Template> findBySourceUrl(final String sourceUrl) {
        return templateRepository
                .findBySourceUrl(sourceUrl)
                .map(mapper::toDomain);
    }
    @Override
    public Optional<Template> findById(final Integer id) {
        return templateRepository
                .findById(id)
                .map(mapper::toDomain);
    }
    @Override
    public PageTemplate findAll(final Pageable pageable) {
        Page<TemplateEntity> pageOfTemplateEntity = templateRepository.findAll(pageable);
        List<Template> templatesOnCurrentPage = pageOfTemplateEntity.getContent().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
        return new PageTemplate(
                templatesOnCurrentPage,
                pageable.getPageNumber() + 1,
                pageOfTemplateEntity.getTotalPages(),
                pageOfTemplateEntity.getTotalElements()
        );
    }
    @Override
    public PageTemplate findByTaskLanguage(final TaskLanguage taskLanguage, Pageable pageable) {
        Page<TemplateEntity> pageOfTemplateEntity = templateRepository.findByTaskLanguage(taskLanguage, pageable);
        List<Template> templatesOnCurrentPage = pageOfTemplateEntity.getContent().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
        return new PageTemplate(
                templatesOnCurrentPage,
                pageable.getPageNumber() + 1,
                pageOfTemplateEntity.getTotalPages(),
                pageOfTemplateEntity.getTotalElements()
        );
    }
}

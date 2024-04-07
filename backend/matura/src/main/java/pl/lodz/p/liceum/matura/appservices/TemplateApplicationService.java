package pl.lodz.p.liceum.matura.appservices;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.lodz.p.liceum.matura.appservices.verifier.AuthVerifyTemplate;
import pl.lodz.p.liceum.matura.domain.template.*;
import pl.lodz.p.liceum.matura.domain.user.PageUser;

import java.time.Clock;
import java.time.ZonedDateTime;

@Service
@RequiredArgsConstructor
@Log
public class TemplateApplicationService {
    private final TemplateService templateService;
    private final IAuthenticationFacade authenticationFacade;
    private final Clock clock;

    @Transactional
    public Template templateSaveTransaction(Template templateToSave) {
        templateToSave.setCreatedAt(ZonedDateTime.now(clock));
        templateToSave.setCreatedBy(authenticationFacade.getLoggedInUserId());
        return templateService.save(templateToSave);
    }

    @Transactional
    public void templateUpdateTransaction(Template templateToUpdate) {
        templateService.update(templateToUpdate);
    }

    @Transactional
    public void templateRemoveByIdTransaction(Integer id) {
        templateService.removeById(id);
    }

    public Template save(Template templateToSave) {
        try {
            return templateSaveTransaction(templateToSave);
        } catch (DataIntegrityViolationException ex) {
            log.warning("Template " + templateToSave.toString() + " already exits in db");
            throw new TemplateAlreadyExistsException();
        }
    }

    public void update(Template templateToUpdate) {
        try {
            templateUpdateTransaction(templateToUpdate);
        } catch (DataIntegrityViolationException ex) {
            log.warning("Error while updating template " + templateToUpdate.toString());
            throw new TemplateNotFoundException();
        }
    }

    @AuthVerifyTemplate
    public void removeById(Integer id) {
        try {
            templateRemoveByIdTransaction(id);
        } catch (DataIntegrityViolationException ex) {
            log.warning("Error while removing template with id  " + id);
            throw new TemplateNotFoundException();
        }
    }

    public Template findBySourceUrl(String sourceUrl) {
        return templateService.findBySourceUrl(sourceUrl);
    }
    public Template findById(Integer id) {
        return templateService.findById(id);
    }
    public PageTemplate findAll(Pageable pageable) {
        return templateService.findAll(pageable);
    }
    public PageTemplate findByTaskLanguage(TaskLanguage taskLanguage, Pageable pageable) {
        return templateService.findByTaskLanguage(taskLanguage, pageable);
    }

}

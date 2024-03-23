package pl.lodz.p.liceum.matura.appservices;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import pl.lodz.p.liceum.matura.domain.submission.Submission;
import pl.lodz.p.liceum.matura.domain.submission.SubmissionAlreadyExistsException;
import pl.lodz.p.liceum.matura.domain.submission.SubmissionNotFoundException;
import pl.lodz.p.liceum.matura.domain.submission.SubmissionService;
import pl.lodz.p.liceum.matura.domain.template.Template;
import pl.lodz.p.liceum.matura.domain.template.TemplateNotFoundException;

import java.time.Clock;
import java.time.ZonedDateTime;

@Service
@RequiredArgsConstructor
@Log
public class SubmissionApplicationService {
    private final SubmissionService submissionService;
    private final IAuthenticationFacade authenticationFacade;
    private final Clock clock;

    @Transactional
    public Submission submissionSaveTransaction(Submission submissionToSave) {
        submissionToSave.setSubmittedAt(ZonedDateTime.now(clock));
        submissionToSave.setSubmittedBy(authenticationFacade.getLoggedInUserId());
        return submissionService.save(submissionToSave);
    }

    @Transactional
    public void submissionUpdateTransaction(Submission submissionToUpdate) {
        submissionService.update(submissionToUpdate);
    }

    @Transactional
    public void submissionRemoveByIdTransaction(Integer id) {
        submissionService.removeById(id);
    }

    public Submission save(Submission submissionToSave) {
        try {
            return submissionSaveTransaction(submissionToSave);
        } catch (DataIntegrityViolationException ex) {
            log.warning("Submission " + submissionToSave.toString() + " already exits in db");
            throw new SubmissionAlreadyExistsException();
        }
    }

    public void update(Submission submissionToUpdate) {
        try {
            submissionUpdateTransaction(submissionToUpdate);
        } catch (DataIntegrityViolationException ex) {
            log.warning("Error while updating submission " + submissionToUpdate.toString());
            throw new SubmissionNotFoundException();
        }
    }

    public void removeById(Integer id) {
        try {
            submissionRemoveByIdTransaction(id);
        } catch (DataIntegrityViolationException ex) {
            log.warning("Error while removing submission with id  " + id);
            throw new SubmissionNotFoundException();
        }
    }

    public Submission findById(Integer id) {
        return submissionService.findById(id);
    }

}

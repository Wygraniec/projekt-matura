package pl.lodz.p.liceum.matura.appservices;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.liceum.matura.domain.user.*;

import java.time.Clock;
import java.time.ZonedDateTime;

@Service
@RequiredArgsConstructor
@Log
public class UserApplicationService {

    private final UserService userService;
    private final IAuthenticationFacade authenticationFacade;
    private final Clock clock;

    @Transactional
    public User userSaveTransaction(User userToSave) {
        ZonedDateTime createdAt = ZonedDateTime.now(clock);
        userToSave.setCreatedAt(createdAt);
        userToSave.setCreatedBy(authenticationFacade.getLoggedInUserId());
        return userService.save(userToSave);
    }
    @Transactional
    public void userUpdateTransaction(User userToUpdate) {
        userService.update(userToUpdate);
    }
    @Transactional
    public void userRemoveByIdTransaction(Integer id) {
        userService.removeById(id);
    }

    public User save(User userToSave) {
        try {
            return userSaveTransaction(userToSave);
        } catch (DataIntegrityViolationException ex) {
            log.warning("User " + userToSave.toString() + " already exits in db");
            throw new UserAlreadyExistsException();
        }
    }

    public void update(User userToUpdate) {
        try {
            userUpdateTransaction(userToUpdate);
        } catch (DataIntegrityViolationException ex) {
            log.warning("Error while updating user " + userToUpdate.toString());
            throw new UserNotFoundException();
        }
    }

    public void removeById(Integer id) {
        try {
            userRemoveByIdTransaction(id);
        } catch (DataIntegrityViolationException ex) {
            log.warning("Error while removing user with id  " + id);
            throw new UserNotFoundException();
        }
    }

    public User findByEmail(String email) {
        return userService.findByEmail(email);
    }

    public User findById(Integer id) {
        return userService.findById(id);
    }

    public PageUser findAll(Pageable pageable) {
        return userService.findAll(pageable);
    }
}

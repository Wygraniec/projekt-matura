package pl.lodz.p.liceum.matura.appservices.verifier;

public class UserIsNotAuthorizedToThisTemplateException extends RuntimeException{

    public UserIsNotAuthorizedToThisTemplateException(String message) {
        super(message);
    }

}
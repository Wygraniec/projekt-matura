package pl.lodz.p.liceum.matura.appservices.verifier;

public class UserIsNotAuthorizedToThisTaskException extends RuntimeException{

    public UserIsNotAuthorizedToThisTaskException(String message) {
        super(message);
    }

}
package mk.ukim.finki.ib.authentication.model.exceptions;

public class UserAlreadyExistsException extends RuntimeException{
    public UserAlreadyExistsException(String message) {
        super(String.format("User with %s already exists", message));
    }
}

package exception;

public class EmptyItemSetException extends RuntimeException {
    public EmptyItemSetException(String message) {
        super(message);
    }
}

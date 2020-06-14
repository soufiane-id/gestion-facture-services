package ma.sid.exceptions;

public class NonUniqueDocumentException extends RuntimeException {
    public NonUniqueDocumentException(String exception) {
        super(exception);
    }
}

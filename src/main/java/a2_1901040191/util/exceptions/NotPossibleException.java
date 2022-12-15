package a2_1901040191.util.exceptions;

/**
 * @Overview A class denoting a failure in creating new object
 * @version 1.0
 * @author Phan Quang Tuan
 */
public class NotPossibleException extends Exception {
    public NotPossibleException() {
        super();
    }

    public NotPossibleException(String message) {
        super(message);
    }
}

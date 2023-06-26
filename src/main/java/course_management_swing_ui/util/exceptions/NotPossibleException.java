package course_management_swing_ui.util.exceptions;

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

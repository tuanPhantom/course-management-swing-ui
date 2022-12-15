package a2_1901040191.util.exceptions;

/**
 * @Overview A class denoting there is an invalid argument for the method, or there is a wrong number of method's parameters.
 * @version 1.0
 * @author Phan Quang Tuan
 */
public class InvalidArgumentException extends Exception {
    public InvalidArgumentException() {
        super();
    }

    public InvalidArgumentException(String message) {
        super(message);
    }
}

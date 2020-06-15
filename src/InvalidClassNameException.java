/*
 * Custom exception for an invalid class name
 */

public class InvalidClassNameException extends Throwable {

    /*
     *  Constructor uses the supers constructor
     */
    public InvalidClassNameException(String s) {
        super(s);
    }
}

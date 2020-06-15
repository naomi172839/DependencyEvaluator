/*
 * Custom exception saying that a cycle was detected.
 */
public class CycleDetectedException extends Throwable {
    //Constructor uses the supers construction
    public CycleDetectedException(String s) {
        super(s);
    }
}

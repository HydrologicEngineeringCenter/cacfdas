/*
 * ControlInputFormatException.java
 *
 * Created on March 21, 2006, 3:40 PM
 */

package cacfdas.exception;

/**
 *
 * @author b4edhdwj
 */
public class ControlInputFormatException extends DataCardException {
    
    /** Creates a new instance of ControlInputFormatException */
    public ControlInputFormatException() {
    }
    
    public String getMessage() {
        return "Invalid Control String";
    }
    
}

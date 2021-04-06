package cacfdas.exception;

/*
 * ReachHeaderInputFormatException.java
 *
 * Created on March 20, 2006, 11:37 AM
 */

/**
 *
 * @author b4edhdwj
 */
public class ReachHeaderInputFormatException extends DataCardException {
    
    /** Creates a new instance of ReachHeaderInputFormatException */
    public ReachHeaderInputFormatException() {
    }
    
    public String getMessage()
    {
        return "Invalid input string used to construct reach header";
    }
}

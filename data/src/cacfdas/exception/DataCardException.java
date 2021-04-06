package cacfdas.exception;
/*
 * DataCardException.java
 *
 * Created on March 20, 2006, 11:33 AM
 */

/**
 *
 * @author b4edhdwj
 */

import java.lang.Exception;

public class DataCardException extends Exception {
    
    /** Creates a new instance of DataCardException */
    public DataCardException() {
    }
    
    public String getMessage()
    {
        return "Data Card Exception";
    }
    
}



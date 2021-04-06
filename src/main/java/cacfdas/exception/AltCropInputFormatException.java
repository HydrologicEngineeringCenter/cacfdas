/*
 * AltCropInputFormatException.java
 *
 * Created on March 21, 2006, 10:02 AM
 */

package cacfdas.exception;

/**
 *
 * @author b4edhdwj
 */
public class AltCropInputFormatException extends DataCardException {
    
    /** Creates a new instance of AltCropInputFormatException */
    public AltCropInputFormatException() {
    }
    
    
    public String getMessage()
    {
        return "Invalid Alternate Crop String";
    }    
    
}

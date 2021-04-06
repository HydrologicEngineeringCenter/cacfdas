/*
 * CropRevenueInputFormatException.java
 *
 * Created on March 21, 2006, 8:15 AM
 */

package cacfdas.exception;

/**
 *
 * @author b4edhdwj
 */
public class CropRevenueInputFormatException extends DataCardException {
    
    /** Creates a new instance of CropRevenueInputFormatException */
    public CropRevenueInputFormatException() {
    }
     
    public String getMessage() {
        return "Invalid Crop Revenue String";
    }    
    
}

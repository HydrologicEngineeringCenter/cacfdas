/*
 * CropPercentageInputFormatError.java
 *
 * Created on March 20, 2006, 2:13 PM
 */

package cacfdas.exception;

/**
 *
 * @author b4edhdwj
 */
public class CropPercentageInputFormatError extends DataCardException {
    
    /** Creates a new instance of CropPercentageInputFormatError */
    public CropPercentageInputFormatError() {
    }
    
    public String getMessage() {
        return "Invalid Crop Percentage String";
    }
}

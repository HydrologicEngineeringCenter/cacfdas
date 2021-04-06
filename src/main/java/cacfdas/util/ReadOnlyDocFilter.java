/*
 * ReadOnlyDocFilter.java
 *
 * Created on April 19, 2006, 7:51 AM
 */

package cacfdas.util;

/**
 *
 * @author b4edhdwj
 */

import javax.swing.text.*;

public class ReadOnlyDocFilter extends DocumentFilter{
    
    /** Creates a new instance of ReadOnlyDocFilter */
    public ReadOnlyDocFilter() 
    {
    }
    
    /** Do not allow insertion */
    public void insertString(DocumentFilter.FilterBypass fb, int offset, String string, AttributeSet attr)
    {
        return;
    }
    
    /** Do not allow removal */
    public void remove(DocumentFilter.FilterBypass fb, int offset, int length)
    {
        return;
    }

    /** Do not allow replacement */
    public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
    {
        return;
    }
}

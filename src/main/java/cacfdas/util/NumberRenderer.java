/*
 * NumberRenderer.java
 *
 * Created on April 20, 2006, 2:01 PM
 */

package cacfdas.util;

/**
 *
 * @author b4edhdwj
 */

import java.awt.*;

import javax.swing.*;
import javax.swing.table.*;

public class NumberRenderer extends DefaultTableCellRenderer
{
    public NumberRenderer()
    {
        setHorizontalAlignment(NumberRenderer.RIGHT);
    }
        
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
    {
        JLabel cell = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
           
        cell.setHorizontalAlignment(NumberRenderer.RIGHT);
            
        return cell;
    }
        
    public void setValue(Object value) 
    {           
        setText(value.toString());
    }
}

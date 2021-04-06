/**
 * FloodTableModel.java
 *
 * Created on April 18, 2006, 2:21 PM
 */

package cacfdas.model;

/**
 *
 * @author b4edhdwj
 */

import cacfdas.util.*;

import java.util.*;

import javax.swing.*;
import javax.swing.table.*;

/** This class is the table model to display the floods of a data band */

public class FloodTableModel extends AbstractTableModel
{
    
    /** Creates a new instance of FloodTableModel */
    public FloodTableModel()
    {
        data = null;
    }
    
    public FloodTableModel(ArrayList<Flood> d) 
    {
        data = d;
    }
    
    /** Return the value for the input row and column */
    public Object getValueAt(int row, int column)
    {
        if ( data != null)
        {
            switch(column)
            {
                case 0:
                    return new HecTimeAdaptor(data.get(row).startDate());
                
                case 1:
                    return new HecTimeAdaptor(data.get(row).stopDate());
                
                default:
                    return "";
            }
        }
        else
        {
            return "";
        }
    }
   
    /** Return the data type of the columns */
    public Class<?> getColumnClass(int column)
    {
        return new HecTimeAdaptor().getClass();
    }
    
    /** Return the number of columns */ 
    public int getColumnCount()
    {
        return 2;
    }
    
    /** Return the name of the input column */
    public String getColumnName(int column)
    {
        switch(column)
        {
            case 0:
                return "Start Date";
            case 1:
                return "Stop Date";
            default:
                return "";
        }
    }
    
    /** Return the number of rows */
    public int getRowCount()
    {
        if ( data != null )
        {
            return data.size();
        }
        else
        {
            return 0;
        }
    }
    
    ArrayList<Flood> data;
}

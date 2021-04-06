/*
 * StatusTableModel.java
 *
 * Created on April 14, 2006, 9:24 AM
 */

package cacfdas.model;

/**
 *
 * @author b4edhdwj
 */

import java.util.*;

import javax.swing.*;
import javax.swing.table.*;


import cacfdas.util.*;

public class StatusTableModel extends AbstractTableModel {
    
    /** Creates a new instance of StatusTableModel */
    public StatusTableModel(DataBand input) {
        band = input;
    }
    
    public Object getValueAt(int row, int column)
    {
        if ( band != null && row >= 0 && row < band.getStatusList().size() )
        {
            switch(column)
            {
                case 0:
                    return new HecTimeAdaptor(band.getDateList().get(row)); 
                    
                case 1:
                    return band.getStatusList().get(row);
                    
                default:
                    return "";
            }
        }
        else
        {
            return "";
        }
    }
    
    public Class<?> getColumnClass(int column)
    {
        switch(column)
        {
            case 0:
                return new HecTimeAdaptor().getClass();
            
            case 1:
                return DataBand.Status.class;
                
            default:
                return Object.class;
        }
    }
    
    public int getColumnCount()
    {
        return 2;
    }
    
    public String getColumnName(int column)
    {
        switch(column)
        {
            case 0:
                return "Date";
                
            case 1:
                return "Status";
                
            default:
                return "";
        }
    }
    
    public int getRowCount()
    {
        if ( band != null )
        {
            return band.getStatusList().size();
        }
        else
        {
            return 0;
        }
    }
    
    DataBand band;
    
}

/*
 * EventTableModel.java
 *
 * Created on April 13, 2006, 4:22 PM
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

import cacfdas.reachinfo.*;

/** This class is a table model to display the event log for a data band */

public class EventTableModel extends AbstractTableModel {
    
    /** Creates a new instance of EventTableModel */
    public EventTableModel(DataBand input, CropInfoTable cropInfo)
    {
       band = input;
       info = cropInfo;
    }
    
    /** Get the value for the input row and column */
    public Object getValueAt(int row, int column)
    {
        if ( row >= 0 && row < band.getEventList().size() )
        {
            switch( column )
            {
                case 0:
                    return new HecTimeAdaptor(band.getEventList().get(row).getDate());
                case 1:
                    return info.getInfo(band.getEventList().get(row).getCropNumber());
                case 2:
                    return band.getEventList().get(row).getCost();
                case 3:
                    return band.getEventList().get(row).getRevenue();
                default:
                    return "";
            }
        }
        else
        {
            return "";
        }
    }

    /** Get the column names */
    public String getColumnName(int column)
    {
        switch(column)
        {
            case 0:
                return "Date";
            
            case 1:
                return "Crop";
                
            case 2:
                return "Cost";
                
            case 3:
                return "Revenue";
                    
            default:
                return "";
        }
    }
    
    /** Get the data type of the columns */
    public Class<?> getColumnClass(int column)
    {
        switch(column)
        {
            case 0:
                return new HecTimeAdaptor().getClass();
            
            case 1:
                return String.class;
                
            case 2:
                return Double.class;
                
            case 3:
                return Double.class;
                    
            default:
                return Object.class;
        }        
    }
    
    /** Get the number of columns */
    public int getColumnCount()
    {
        return 4;
    }
    
    /** Get the nuber of rows */
    public int getRowCount()
    {
        return band.getEventList().size();
    }
    
    /** The data band to display the event of */
    DataBand band;
    
    /** Table that stores information about the possible crops */
    CropInfoTable info;
}

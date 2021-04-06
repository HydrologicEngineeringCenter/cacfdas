/*
 * CropInfoTableModel.java
 *
 * Created on March 22, 2006, 8:14 AM
 */

package cacfdas.reachinfo;

import java.util.*;
import javax.swing.*;
import java.io.*;
import javax.swing.table.*;


import cacfdas.*;
import cacfdas.stagedata.*;
import cacfdas.exception.*;

import cacfdas.util.*;

/**
 *
 * @author b4edhdwj
 */
public class CropInfoTableModel extends AbstractTableModel{
    
    public CropInfoTableModel()
    {
        infoTable = new CropInfoTable();
    }  
    
    /** Creates a new instance of CropInfoTableModel */
    public CropInfoTableModel(CropInfoTable table) 
    {
        infoTable = table;
    }
    
    public void clear()
    {
        infoTable.clear();
        fireTableDataChanged();
    }
    
    public Object getValueAt(int rowIndex, int columnIndex)
    {
        int cropNum = infoTable.getCrop(rowIndex);
        
        switch(columnIndex)
        {
            case 0:
                return cropNum;
            
            case 1:
                return infoTable.getInfo(cropNum);
                
            case 2:
                return infoTable.getPercentage(cropNum);
                
            case 3:
                return infoTable.getNetRevenue(cropNum);
                
            case 4:
                return infoTable.getGrossRevenue(cropNum);
                
            case 5:
                return infoTable.getReplantList(cropNum);
                
            case 6:
                return new DayMonth(infoTable.getLastPlantingDate(cropNum)); 
            
            case 7:
                return new DayMonth(infoTable.getMinOperationDate(cropNum)); 
            
            default:
                return "";
            
        }
    }
    
    public int getColumnCount() 
    { 
        return 8;
    }
    
    public String getColumnName(int column)
    {
        switch(column)
        {
            case 0:
                return "Crop Number";
            
            case 1:
                return "Name";
            
            case 2:
                return "Percent";
            
            case 3:
                return "Net Revenue";
                
            case 4:
                return "Gross Revenue";
                
            case 5:
                return "Replant list";
                
            case 6:
                return "Last Planting Date";
                
            case 7:
                return "Last Preparation Date";
                
            default:
                return "";
        }
    }
    
    public int getRowCount()
    {
        if ( infoTable == null )
        {
            return 0;
        }
        else
        {
            return infoTable.getMap().size();
        }
    }
    
    public Class<?> getColumnClass(int columnIndex)
    {
        switch( columnIndex )
        {
            case 0:
                return Integer.class;
                
            case 1:
                return String.class; 
                
            case 2:
            case 3:
            case 4:
                return Double.class;
                
            case 5:
                return new IntegerArrayListAdaptor().getClass();
                
            case 6:
            case 7:
                return DayMonth.class;
                
            default:
                return String.class;
        }
    }
    
    CropInfoTable getTable()
    {
        return infoTable;
    }
    
    public boolean isCellEditable(int rowIndex, int columnIndex)
    {
        return true;
    }
    
    public void load(File file) throws FileNotFoundException, IOException, DataCardException
    {
        infoTable.load(file);
        fireTableDataChanged();
        
    }
    
    void setTable(CropInfoTable t)
    {
        infoTable = t;
    }
    
    public void setValueAt(Object aValue, int rowIndex, int columnIndex)
    {
        Integer val;
        Double num;
        String s;
        
        int cropNum = infoTable.getCrop(rowIndex);
        
        switch(columnIndex)
        {
            case 0:
                val = (Integer) aValue;
                infoTable.setCrop(rowIndex,val);
                break;
                
            case 1:
                s = (String) aValue;
                infoTable.setInfo(cropNum, s);
                break;
                
            case 2:
                num = (Double) aValue;
                infoTable.setPercentage(cropNum, num);
                break;
            
            case 3:
                 num = (Double) aValue;
                infoTable.setNetRevenue(cropNum, num);
                break;
            
            case 4:
                 num = (Double) aValue;
                infoTable.setGrossRevenue(cropNum, num);
                break;
                
            case 5:
                 ArrayList<Integer> list = ((IntegerArrayListAdaptor) aValue).getList();
                 infoTable.setReplantList(cropNum, list);
                 break;
                 
            case 6:
                DayMonth dm1  = (DayMonth) aValue;
                infoTable.setLastPlantingDate(cropNum, dm1.toDate());
                break;
                
            case 7:
                DayMonth dm2  = (DayMonth) aValue;
                infoTable.setMinOperationDate(cropNum, dm2.toDate());
                break;
                
            default:

        }
        
        fireTableCellUpdated(rowIndex, columnIndex);
    }
    
    CropInfoTable infoTable;
}

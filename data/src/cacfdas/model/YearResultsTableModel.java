/*
 * YearResultsTableModel.java
 *
 * Created on April 13, 2006, 1:33 PM
 */

package cacfdas.model;

/**
 *
 * @author b4edhdwj
 */

import java.util.*;
import java.text.*;
import java.io.*;

import javax.swing.*;
import javax.swing.table.*;

import cacfdas.reachinfo.*;
import cacfdas.util.*;

public class YearResultsTableModel  extends AbstractTableModel {
    
    /** Creates a new instance of YearResultsTableModel */
    
    public YearResultsTableModel()
    {
         map = null;
         info = null;
        
        df1 = (DecimalFormat) DecimalFormat.getInstance();
        df1.setMaximumFractionDigits(1);
        df1.setMinimumFractionDigits(1);
        
        df2 = (DecimalFormat) DecimalFormat.getInstance();
        df2.setMaximumFractionDigits(2);
        df2.setMinimumFractionDigits(2);
    }
    
    public YearResultsTableModel(HashMap< Integer, ArrayList<DataBand> > data, CropInfoTable cropInfo) {
        map = data;
        info = cropInfo;
        
        df1 = (DecimalFormat) DecimalFormat.getInstance();
        df1.setMaximumFractionDigits(1);
        df1.setMinimumFractionDigits(1);
        
        df2 = (DecimalFormat) DecimalFormat.getInstance();
        df2.setMaximumFractionDigits(2);
        df2.setMinimumFractionDigits(2);
    }
    
    public Object getValueAt(int row, int column)
    {       
        if ( map != null )
        {
            Iterator<ArrayList<DataBand>> iterator = map.values().iterator();

            int pos = row;
            ArrayList<DataBand> bandList = iterator.next();

            while( pos >= bandList.size() )
            {
                if ( iterator.hasNext() )
                {
                    pos -= bandList.size();
                    bandList = iterator.next();
                }
                else
                {
                    return "";
                }
            }

            switch(column)
            {
                case 0:
                    return info.getInfo(bandList.get(pos).getCropNumber());

                case 1:
                    return df1.format(bandList.get(pos).getMinElevation()) + " - " + df1.format(bandList.get(pos).getMaxElevation());
                
                case 2:
                    return bandList.get(pos).getNumFloods();
                    
                case 3:
                    return new Double(bandList.get(pos).getAcres());
                    
                case 4:
                    return new CurrencyContainer(bandList.get(pos).getTotalCost());

                case 5:
                    return new CurrencyContainer(bandList.get(pos).getTotalRevenue());
                    
                case 6:
                    return new CurrencyContainer(bandList.get(pos).getLostRevenue());

                default:
                    return "";
            }
        }
        else
        {
            return "";
        }
    }
    
    public int getColumnCount()
    {
        return 7;
    }
    
    public Class<?> getColumnClass(int column)
    {
        switch(column)
        {
            case 0:
                return String.class;
                
            case 1:
                return String.class;
            
            case 2:
                return Integer.class;
                
            case 3:
                return Double.class;
                
            case 4:
                return CurrencyContainer.class;
                
            case 5:
                return CurrencyContainer.class;
                
            case 6:
                return CurrencyContainer.class;
            
            default:
                return String.class;
        }
    }
    
    public String getColumnName(int column)
    {
        switch( column )
        {
            case 0:
                return "Crop";
            
            case 1:
                return "Range";
                
            case 2:
                return "Floods";
                
            case 3:
                return "Acres";
                
            case 4:
                return "Cost";
                
            case 5:
                return "Revenue";
                
            case 6:
                return "Lost Revenue";
                
            default:
                return "";
        }
    }
    
    public int getRowCount()
    {
        if ( map != null)
        {
            int sum = 0;
            Iterator<ArrayList<DataBand>> iterator = map.values().iterator();

            while( iterator.hasNext() )
            {
                sum += iterator.next().size();
            }

            return sum;
        }
        else
        {
            return 0;
        }
    }
    
    public void viewActionList(int pos)
    {
        Iterator<ArrayList<DataBand>> iterator = map.values().iterator();
        
        ArrayList<DataBand> bandList = iterator.next();
        
        while(pos >= bandList.size() )
        {
            if ( iterator.hasNext() )
            {
                pos -= bandList.size();
                bandList = iterator.next();
            }
            else
            {
                return;
            }
        }
        
            
        EventTableFrame frame = new EventTableFrame(bandList.get(pos),info);
        frame.setVisible(true);        
    }
    
    public void viewStatusChangeList(int pos)
    {
         Iterator<ArrayList<DataBand>> iterator = map.values().iterator();
        
        ArrayList<DataBand> bandList = iterator.next();
        
        while(pos >= bandList.size() )
        {
            if ( iterator.hasNext() )
            {
                pos -= bandList.size();
                bandList = iterator.next();
            }
            else
            {
                return;
            }

        }  
           
        StatusTableFrame frame = new StatusTableFrame(bandList.get(pos),info);
        frame.setVisible(true);
    }
    
    public void viewFloodList(int pos)
    {
        Iterator<ArrayList<DataBand>> iterator = map.values().iterator();
        
        ArrayList<DataBand> bandList = iterator.next();
        
        while(pos >= bandList.size() )
        {
            if ( iterator.hasNext() )
            {
                pos -= bandList.size();
                bandList = iterator.next();
            }
            else
            {
                return;
            }

        }  
           
        FloodTableFrame frame = new FloodTableFrame(bandList.get(pos),info); 
        frame.setVisible(true);      
    }
    
    public void writeToStream(Writer writer) throws IOException
    {
        int i;
        int j; 
        
        // in the first row output the column names
        for( i = 0; i < getColumnCount() -1; ++i )
        {
            writer.write(getColumnName(i));
            writer.write("\t");
        }
        writer.write(getColumnName(i));
        writer.write("\n");
        
        // output each table row
        
        for( i = 0; i < getRowCount(); ++i )
        {
            for( j = 0; j < getColumnCount()-1; ++j)
            {
                writer.write(getValueAt(i,j).toString());
                writer.write("\t");
            }
            
            writer.write(getValueAt(i,j).toString());
            writer.write("\n");
        }        
    }
    
    public void writeToFile(File file) throws IOException
    {
        if ( file.exists() )
        {
            file.delete();
        }
        
        FileWriter writer = new FileWriter(file);
        
        writeToStream(writer);
        
        writer.close();
    }
    
    HashMap< Integer, ArrayList<DataBand> > map;
    CropInfoTable info;
    
    DecimalFormat df1;
    DecimalFormat df2;
    
    
}

/*
 * ModelSummaryTableModel.java
 *
 * Created on April 21, 2006, 7:58 AM
 */

package cacfdas.model;

import java.util.*;
import java.io.*;

import javax.swing.*;
import javax.swing.table.*;

import cacfdas.reachinfo.*;
import cacfdas.util.*;

/**
 *
 * @author b4edhdwj
 */
public class ModelSummaryTableModel extends AbstractTableModel 
{
    
    /** Creates a new instance of ModelSummaryTableModel */
    public ModelSummaryTableModel(int startYear, ArrayList<HashMap<Integer,ArrayList<DataBand>>> data, CropInfoTable info) 
    {
        mData = data;
        mStartYear = startYear;
        
        mNumCrops = data.get(0).size();
        mCropList = new Integer[mNumCrops];
        mCropList = data.get(0).keySet().toArray(mCropList);
        
        mInfo = info;
    }
    
    public Object getValueAt(int row, int column)
    {
        int pos1 = row / mNumCrops;
        int pos2 = row % mNumCrops;
        
        ArrayList<DataBand> bands;
        
        double num = 0.0;
        
        switch(column)
        {
            case 0:
                return new Integer(mStartYear + pos1);
                
            case 1:
                return mInfo.getInfo(mCropList[pos2]);
                
            case 2:
                bands = mData.get(pos1).get(mCropList[pos2]);
                
                for(int i = 0; i < bands.size(); ++i)
                {
                    if (  bands.get(i).getNumFloods() >= 1 ) 
                    {
                        num += bands.get(i).getAcres();
                    }
                }
                
                return Math.rint(num);
                
            case 3:
                bands = mData.get(pos1).get(mCropList[pos2]);
                
                for(int i = 0; i < bands.size(); ++i)
                {
                    num += bands.get(i).getBandedAcres();
                }
                
                 return Math.rint(num);
                
                
            case 4:
                bands = mData.get(pos1).get(mCropList[pos2]);
                
                for(int i = 0; i < bands.size(); ++i)
                {
                    num += bands.get(i).getAcres() * bands.get(i).getDamageCount();
                }
                
                return Math.rint(num);
                
            case 5:
                bands = mData.get(pos1).get(mCropList[pos2]);
                
                for(int i = 0; i < bands.size(); ++i)
                {
                    if (  bands.get(i).isLost() ) 
                    {
                        num += bands.get(i).getAcres();
                    }
                }
                
                return Math.rint(num);
                
            case 6:
                bands = mData.get(pos1).get(mCropList[pos2]);
                
                for(int i = 0; i < bands.size(); ++i)
                {
                    num += bands.get(i).getTotalRevenue();
                }
                
                return new CurrencyContainer(Math.rint(num));
                
            case 7:
                bands = mData.get(pos1).get(mCropList[pos2]);
                
                for(int i = 0; i < bands.size(); ++i)
                {
                    num += bands.get(i).getTotalCost();
                }
                
                return new CurrencyContainer(Math.rint(num));
                
            case 8:
                bands = mData.get(pos1).get(mCropList[pos2]);
                
                for(int i = 0; i < bands.size(); ++i)
                {
                    num += bands.get(i).getLostRevenue();
                }
                
                return new CurrencyContainer(Math.rint(num));
                
            default:
                return "";
            
        }
    }
    
    public int getColumnCount()
    {
        return 9;
    }
    
    public String getColumnName(int column)
    {
        switch(column)
        {
            case 0:
                return "Year";
                
            case 1:
                return "Crop";
                
            case 2:
                return "Peak Acres Flooded";
                
            case 3:
                return "Banded Acres Flooded";
                
            case 4:
                return "Acres Damaged";
                
            case 5:
                return "Acres Lost";
                
            case 6:
                return "Total Revenue";
            
            case 7:
                return "Total Production Cost";
                
            case 8:
                return "Lost Revenue Damages";
                
            default:
                return "";
        }
    }
    
    public Class<?> getColumnClass(int column)
    {
        switch(column)
        {
            case 0:
                return Integer.class;
            case 1:
                return String.class;
                
            case 2:
                return Double.class;
                
            case 3:
                return Double.class;
                
            case 4:
                return Double.class;
                
            case 5:
                return Double.class;
                
            case 6:
            case 7:
            case 8:
                return CurrencyContainer.class;
                
            default:
                return String.class;
        }       
    }
    
    public int getRowCount()
    {
        return mData.size() * mNumCrops;
    }
    
    public void writeToFile(File file) throws IOException
    {
        if ( file.exists() )
        {
            file.delete();
        }
        
        FileWriter writer = new FileWriter(file);
        
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
        
        writer.close();
        
        
    }
    
    
    private int mStartYear;
    private ArrayList<HashMap<Integer,ArrayList<DataBand>>> mData;
    private CropInfoTable mInfo;
    
    private int mNumCrops;
    private Integer[] mCropList;
}

/*
 * StageDataTable.java
 *
 * Created on March 23, 2006, 7:43 AM
 */

package cacfdas.stagedata;

import cacfdas.util.*;

import java.util.*;
import java.io.*;
import java.lang.*;
import java.text.*;

import javax.swing.*;
import javax.swing.table.*;

import hec.io.*;
import hec.heclib.dss.*;
import hec.heclib.util.*;

import cacfdas.model.*;

/**
 *
 * @author b4edhdwj
 */
public class StageDataTable extends AbstractTableModel {

    
    /** Creates a new instance of StageDataTable */
    public StageDataTable(String dataStrings, String areaStrings)
    {
        super();
        init(dataStrings,areaStrings);
    }
    
    private void init(String dataStrings, String areaStrings)
    {
        String[] parts;
        
        mDataCPartList = new ArrayList<String>();
        parts = dataStrings.split(",");
        for(int i = 0; i < parts.length; ++i)
        {
            mDataCPartList.add(parts[i]);
        }
        
        
        mAreaCPartList = new ArrayList<String>();
        parts = areaStrings.split(",");
        for(int i = 0; i < parts.length; ++i)
        {
            mAreaCPartList.add(parts[i]);
        }         
    }

    public void clear()
    {
        mDataCPartList.clear();
        mAreaCPartList.clear();
        
        stageAreaCurve = new PairedDataContainer();
     
        minGage = 0.0;
        maxGage = 0.0;
    
        // variables for controling the data window
        mWindowActive = false;
        mWindowStartDate = null;
        mWindowStopDate = null;
        mWindowStartIndex = 0;
        mWindowStopIndex = 0;
        
        //variables for DSS
        mDSSFile = null;
        mDSSStagePath = "";
        mDSSStageAreaPath = "";
    
        ts = new HecTimeSeries();
        pd = new HecPairedData();
    
        time = new HecTimeArray();
        value = new HecDoubleArray();
        
        fireTableDataChanged();
        mStageAreaTableModel.fireTableDataChanged();
    }
    
    public double getAreaForStage(double stage,int num)
    {
        int pos1, pos2;
        double low_stage, high_stage;
            
        int hStage;
        int lStage;
            
        double val1, val2;
        
        if ( num >= stageAreaCurve.yOrdinates.length )
        {
            num = 0;
        }
            
        if ( stage == (int) stage )
        {
            pos1 = java.util.Arrays.binarySearch(stageAreaCurve.xOrdinates,(int) stage);
            return (pos1 >= 0) ? stageAreaCurve.yOrdinates[num][pos1] : 0;
        }
        else
        {
           // get the integer bounding indecies of the stage, the curve hsould contain at least the integers
           pos1 = java.util.Arrays.binarySearch(stageAreaCurve.xOrdinates,(int) stage);
           pos2 = java.util.Arrays.binarySearch(stageAreaCurve.xOrdinates,(int) stage+1);
           
           if ( pos1 >= 0 && pos2 >= 0)
           {
               double low_error = stage - stageAreaCurve.xOrdinates[pos1];
               double high_error = stageAreaCurve.xOrdinates[pos2] - stage;

               // check to see if the curve records any intermediate points between the integers
               for( int i = pos1 +1; i < pos2; ++i)
               {
                    double val = stageAreaCurve.xOrdinates[i];
                    if ( val < stage )
                    {
                        double new_low_error = stage - val;

                        if ( new_low_error < low_error )
                        {
                            low_error = new_low_error;
                            pos1 = i;
                        }
                    }
                    else if ( val > stage )
                    {
                        double new_high_error = val - stage;

                        if ( new_high_error < high_error )
                        {
                            high_error = new_high_error;
                            pos2 = i;
                        }
                    }
                    else    // it is possible that the curve has the exact value
                    {
                        return stageAreaCurve.yOrdinates[num][i];
                    }                  
               }
               
               low_stage = stageAreaCurve.xOrdinates[pos1];
               high_stage = stageAreaCurve.xOrdinates[pos2];
               
               val1 = stageAreaCurve.yOrdinates[num][pos1];
               val2 = stageAreaCurve.yOrdinates[num][pos2];
               
               double l = high_stage - low_stage; 
               double scale = (stage - low_stage) / 1;
               double iscale = 1 - scale;
                
               return (iscale * val1) + (scale * val2);                 
           }
           else if ( pos1 >= 0 )
           {
               return stageAreaCurve.yOrdinates[num][pos1];
           }
           else if ( pos2 >= 0 )
           {
               return stageAreaCurve.yOrdinates[num][pos2];
           }
           else
           {
               return 0;
           }
       }   
    }    
    
    public int getColumnCount()
    {
        return 2;
    }
    
    public Class<?> getColumnClass(int column)
    {
         switch(column)
        {
            case 0:
            return HecTimeAdaptor.class;
            
            case 1:
            return HecDouble.class;
            
            default:
            return String.class;
        }       
    }

    public String getColumnName(int column)
    {
        switch(column)
        {
            case 0:
            return "Date";
            
            case 1:
            return "Stage";
            
            default:
            return "";
        }
    }
    
    public HecTime getFirstDate()
    {
        if ( time.numberDefinedElements() > 0 )
        {
            return time.element(0);
        }
        else
        {
            return null;
        }
    }
    
    public HecTime getLastDate()
    {
        if ( time.numberDefinedElements() > 0 )
        {
            return time.element(time.lastDefined());
        }
        else
        {
            return null;
        }
    }
    
    public double getMinStage()
    {
        return minGage;
    }
    
    public double getMaxStage()
    {
        return maxGage;
    }
    
    public File getDSSFile()
    {
        return mDSSFile;
    }
    
    public int getRowCount()
    {
        return value.numberElements();
    }
    
    public String getStagePath()
    {
        return mDSSStagePath;
    }
    
    public String getStageAreaPath()
    {
        return mDSSStageAreaPath;
    }
    
    public PairedDataContainer getStageAreaCurve()
    {
        return stageAreaCurve;
    }
    
    public StageAreaTableModel getStageAreaTableModel()
    {
        if ( mStageAreaTableModel == null)
        {
            mStageAreaTableModel = new StageAreaTableModel();
        }
        
        return mStageAreaTableModel;
    }
    
    /** Get the starting and ending dates of the data */
    
    public void getTimeRange(Date start, Date stop)
    {
        if ( mWindowStartDate != null && mWindowStopDate != null )
        {
            start.setTime(mWindowStartDate.getTimeInMillis());
            stop.setTime(mWindowStopDate.getTimeInMillis());
        }
    }
    
    public int getWindowTimeRange(HecTime start, HecTime stop)
    {
        return 0;
    }
    
    public HecTimeSeries getTimeSeries()
    {
        return ts;
    }
    
    public Object getValueAt(int row, int column)
    {
        switch ( column )
        {
            case 0:
            return new HecTimeAdaptor(time.element(row));

            case 1:
            return value.element(row); 

            default:
            return "";
        }


        
    }
    
    public boolean isCellEditable(int rowIndex, int columnIndex)
    {
        switch(columnIndex)
        {
            case 0:
            return false;
            
            case 1:
            return true;
            
            default:
            return false;
        }
    }
    
    public void load(File file) throws IOException
    {
        String s = file.getName();
        s = s.toLowerCase();
        
        if ( s.endsWith(".xml") )
        {
            loadXML(file);
        }
        else if ( s.endsWith(".txt") || s.endsWith(".text") || s.indexOf('.') == -1)
        {
            loadText(file);
        }
    }
    
    public void loadText(File file) throws IOException
    {       
        StageDataImporter sdc = new StageDataImporter();
        sdc.importText(file);
        sdc.write();
        
        mDSSFile = sdc.getDSSFile();
        mDSSStagePath = sdc.getDSSStageDataPath();
        mDSSStageAreaPath = sdc.getDSSStageAreaPath();
        
        minGage = sdc.getMinStage();
        maxGage = sdc.getMaxStage();
        
        mWindowStartDate = sdc.getFirstDate(); 
        mWindowStopDate = sdc.getLastDate(); 
        
        ts = new HecTimeSeries();
        ts.setDSSFileName(mDSSFile.getAbsolutePath());
        ts.setPathname(mDSSStagePath);
             
        pd = new HecPairedData();
        pd.setDSSFileName(mDSSFile.getAbsolutePath());
        pd.setPathname(mDSSStageAreaPath);
        
        ts.setTimeWindow(mWindowStartDate, mWindowStopDate );
        
        time = new HecTimeArray();
        value = new HecDoubleArray();
        ts.read(time,value);
        
        pd.read(stageAreaCurve);
        
        fireTableDataChanged();
        
        getStageAreaTableModel().fireTableDataChanged();
    }
    
    public void loadDSS(File file, String dataPath, String areaPath) throws IOException
    {
            // record the file and path names
            mDSSFile = file;
            mDSSStagePath = dataPath;
            mDSSStageAreaPath = areaPath;
            
            ts = new HecTimeSeries();
            ts.setDSSFileName(file.getAbsolutePath());
            
            pd = new HecPairedData();
            pd.setDSSFileName(file.getAbsolutePath());
            
            // set the path names
            ts.setPathname(mDSSStagePath);
            pd.setPathname(mDSSStageAreaPath);
            
            // get meta data
            mWindowStartDate = new HecTime();
            mWindowStopDate = new HecTime();
            doubleContainer minimum = new doubleContainer();
            doubleContainer maximum = new doubleContainer();
            doubleContainer average = new doubleContainer();
            intContainer numValues = new intContainer();
            intContainer missingValues = new intContainer();
            
            ts.getSeriesTimeRange(mWindowStartDate, mWindowStopDate,0);
            ts.setTimeWindow(mWindowStartDate, mWindowStopDate);
            
            HecTime t1 = new HecTime();
            HecTime t2 = new HecTime();
            ts.getStatistics(minimum, t1, maximum, t2, average, numValues, missingValues);
            
            minGage = minimum.value;
            maxGage = maximum.value;
           
            // set the time window
            ts.setTimeWindow(mWindowStartDate, mWindowStopDate );
            
            // read the current time window
            time = new HecTimeArray();
            value = new HecDoubleArray();
            ts.read(time,value);

            // read the stage area curve
            pd.read(stageAreaCurve);

            // update any displays
            fireTableDataChanged();

            // update any stage area displays
            getStageAreaTableModel().fireTableDataChanged();            
        
    }
    
    void loadXML(File File)
    {
        
    }
    
    public void setWindowTimeRange(HecTime date1, HecTime date2)
    {
        if ( date1 != null && date2 != null && 
             (date1.compareTimes(mWindowStartDate) != 0 ||
             date2.compareTimes(mWindowStopDate) != 0 ) )
        {
            mWindowStartDate = new HecTime(date1);
            mWindowStopDate = new HecTime(date2);
            
            if ( time.numberElements() >= 1 && value.numberElements() >= 1)
            {
                ts.setTimes(time);
                ts.setData(value);
                ts.write();
                ts.saveDataToDisk();
            }
            
            ts.setTimeWindow(mWindowStartDate, mWindowStopDate );

            time = new HecTimeArray();
            value = new HecDoubleArray();
            ts.read(time,value);

            fireTableDataChanged();
        }
    }
    

    public void setValueAt(Object aValue, int row, int column)
    {

            switch ( column )
            {
                case 0:
                HecTimeAdaptor date = (HecTimeAdaptor) aValue;
                time.set(row,date.value() );

                case 1:
                HecDouble val = (HecDouble) aValue;
                value.set(row,val);

                default:
            }

    }
    
    public void setStageAreaPath(String newPath)
    {
        if ( pd.fileOpened() && newPath.compareToIgnoreCase(mDSSStageAreaPath) != 0)
        {
            if ( pd.pathname().compareTo("") != 0 )
            {
                writeStageAreaData();
            }
            
            mDSSStageAreaPath = newPath;
            pd.setPathname(newPath);
            
            readStageAreaData();
        }
    }
    
    public void setStagePath(String newPath)
    {
        if ( ts.fileOpened() && newPath.compareToIgnoreCase(mDSSStagePath) != 0 )
        {
            if ( ts.pathname().compareTo("") != 0 && value.numberElements() != 0 && time.numberElements() != 0 )
            {
                ts.setData(value);
                ts.setTimes(time);
                ts.write();
                ts.saveDataToDisk();
            }
            
            mDSSStagePath = newPath;
            time = new HecTimeArray();
            value = new HecDoubleArray();
            
            ts.setPathname(newPath);
            ts.read(time,value);
            
            fireTableDataChanged();
        }
    }
    
    private void readStageAreaData()
    {
        stageAreaCurve = new PairedDataContainer();
        
        pd.read(stageAreaCurve);
        
        mStageAreaTableModel.fireTableDataChanged();
    }
    
    private void writeStageAreaData()
    {               
        pd.write(stageAreaCurve);
        pd.saveDataToDisk();       
    }
    
    private PairedDataContainer stageAreaCurve = new PairedDataContainer();
     
    private double minGage = 0.0;
    private double maxGage = 0.0;
    
    // variables for controling the data window
    boolean mWindowActive = false;
    HecTime mWindowStartDate = null;
    HecTime mWindowStopDate = null;
    int mWindowStartIndex = 0;
    int mWindowStopIndex = 0;

    //variables for DSS
    File mDSSFile;
    String mDSSStagePath;
    String mDSSStageAreaPath;
    
    HecTimeSeries ts;
    HecPairedData pd;
    
    HecTimeArray time = new HecTimeArray();
    HecDoubleArray value = new HecDoubleArray();
    
    class StageAreaTableModel extends AbstractTableModel
    {
        public StageAreaTableModel()
        {
            super();
        }
        
        public int getColumnCount()
        {
            return 2;
        }
        
        public Class<?> getColumnClass(int column)
        {
            switch(column)
            {
                case 0:
                case 1:
                    return Double.class;
                
                default:
                    return Object.class;
            }
            
        }
        
        public String getColumnName(int column)
        {
            switch(column)
            {
                case 0:
                return "Stage";

                case 1:
                return "Area";

                default:
                return "";
            }
        }
        
        public int getRowCount()
        {
            return stageAreaCurve.numberOrdinates;
        }
        
        public Object getValueAt(int row, int column)
        {            
           switch ( column )
            {
                case 0:
                    return stageAreaCurve.xOrdinates[row];
                    
                case 1:
                    return stageAreaCurve.yOrdinates[0][row];
                    
                default:
                    return "";   
            }
        }
        
        public boolean isCellEditable(int rowIndex, int columnIndex)
        {
            switch ( columnIndex )
            {
                case 1:
                    return true;
                
                default:
                    return false;
            }
        }      
        
        public void setValueAt(Object aValue, int row, int column)
        {
            switch ( column  )
            {
                case 1:

                    stageAreaCurve.yOrdinates[0][row] = (Double) aValue;
                    fireTableCellUpdated(row,column);
                    
                default:
                    return;
            } 
        }
    }
    
    StageAreaTableModel mStageAreaTableModel;
    
    static final Comparator dateCompare = new Comparator<StageData>()
    {
        public int compare(StageData e1, StageData e2)
        {        
            
            Date date1 = e1.getDate();
            Date date2 = e2.getDate();
            
            cal.setTime(date1);
            int y1 = cal.get(Calendar.YEAR);
            int d1 = cal.get(Calendar.DAY_OF_YEAR);
 
             cal.setTime(date2);
            int y2 = cal.get(Calendar.YEAR);
            int d2 = cal.get(Calendar.DAY_OF_YEAR);
            
            int val = y1 - y2;
            val *= 1024;
            val += d1;
            val -= d2;
            
            return val;
        }
    };
    
    static Calendar cal = Calendar.getInstance();
    
    ArrayList<String> mDataCPartList;
    ArrayList<String> mAreaCPartList;
}

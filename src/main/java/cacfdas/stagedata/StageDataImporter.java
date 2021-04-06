/*
 * StageDataTable.java
 *
 * Created on March 23, 2006, 7:43 AM
 */

package cacfdas.stagedata;

import java.util.*;
import java.io.*;
import java.lang.*;

import javax.swing.*;
import javax.swing.table.*;

import hec.io.*;
import hec.heclib.dss.*;
import hec.heclib.util.*;

/**
 *
 * @author b4edhdwj
 */
public class StageDataImporter {
    
    /** Creates a new instance of StageDataTable */
    public StageDataImporter()
    {
        super();
    }

    public void finalize() throws Throwable
    {
        super.finalize();
    }
    
    HecTime getFirstDate()
    {
        return new HecTime(mStageData.get(0).getDate(), 0);
    }
    
    HecTime getLastDate()
    {
        return new HecTime(mStageData.get(mStageData.size()-1).getDate(), 0);
    }
    
    double getMinStage()
    {
        return minGage;
    }
    
    double getMaxStage()
    {
        return maxGage;
    }
    
    /** Get the starting and ending dates of the data */
    
    public void getTimeRange(Date start, Date stop)
    {
        start.setTime(getFirstDate().getTimeInMillis());
        stop.setTime(getLastDate().getTimeInMillis());
    }
    
    void importText(File file) throws IOException
    {        
        srcFile = file;
        
        BufferedReader input = new BufferedReader( new FileReader(file));
        
        String line;
        String temp;
        
        int day;
        int month;
        int year;
        
        double gageValue;
        double floodedArea;
        
        boolean first = true;
        
        Date date;
        
        mStageData.clear();
        stageAreaCurve.clear();
        
        while(input.ready())
        {
            line = input.readLine();
            
            // get the month
            temp = line.substring(0,2).trim();
            month = Integer.parseInt(temp);
            
            // get the day
            temp = line.substring(2, 4).trim();
            day = Integer.parseInt(temp);
            
            // get the year
            temp = line.substring(4,6);
            year = Integer.parseInt(temp);
            
            // y2k correction
            year += (year < 30) ? 2000 : 1900;
            
            // get the gage reading
            temp = line.substring(10, 20).trim();
            gageValue = Double.parseDouble(temp);
            
            // get the flooded elevation
            temp = line.substring(32, 40).trim();
            floodedArea = Double.parseDouble(temp);
            
            // get a java date object for the day month year
            cal.set(year,month-1,day,0,0,0);
            date = cal.getTime();
            
            mStageData.add(new StageData(date,gageValue));
            
            // update the mapping of gagaeValue to floodedArea
            if ( ! stageAreaCurve.containsKey(gageValue) )
            {
                stageAreaCurve.put(gageValue,floodedArea);
            }
            
            // find the min and maxStage
            if ( first )
            {
                minGage = gageValue;
                maxGage = gageValue;
                first = false;
            }
            else
            {
                if ( gageValue < minGage )
                {
                    minGage = gageValue;
                }
                
                if ( gageValue > maxGage )
                {
                    maxGage = gageValue;
                }
            }    
        }
        
        
        double startVal = minGage;
        double stopVal = minGage;
        
        // make sure the stage area curve does not have any gaps
        for(double val = minGage; val <= maxGage; val = Math.rint((val+0.1)*10) / 10.0 )
        {     
            // we do not need to check that val < maxGage b/c maxGage will always contain a value
            while(! stageAreaCurve.containsKey(val) )
            {
                val = Math.rint((val+0.1)*10) / 10.0;
            }
            
            stopVal = val;
            
            // if a gap was found fill it by linairly interpolating to the neighboring entries
            if ( stopVal - startVal  - 0.1 > 0.0001  )
            {
                double startArea = stageAreaCurve.get(startVal); 
                double stopArea = stageAreaCurve.get(stopVal); 

                double num = val = Math.rint((startVal+0.1)*10) / 10.0;
                while( num < stopVal )
                {
                    double s1 = (stopVal - num) / (stopVal - startVal);
                    double s2 = 1 - s1;

                    double d = (s1 * startArea) + (s2 * stopArea);

                    stageAreaCurve.put(num,d);

                    num = val = Math.rint((num+0.1)*10) / 10.0;
                }
            }
            
            startVal = val;
        }
    }
    
    public void write() throws IOException
    {
        HecDSSDataAttributes da = new HecDSSDataAttributes();

        
        // determine the name for the DSS File
        calcDSSFileName();
        
        // open the DSS File this will create the file if it doesnt exist
        da.setDSSFileName(mDSSFilePath);
        
        // create the stage data path and the stage area path
        calcStagePath();       
        calcStageAreaPath();
        
        if ( da.fileOpened() )
        {       
            
            if ( da.recordExists(mDSSStagePath))
            {
                ts = new HecTimeSeries();
                ts.setDSSFileName(mDSSFilePath);
                ts.setPathname(mDSSStagePath);
            }
            else
            {
                ts = da.createTimeSeriesObject(mDSSStagePath);
            }

            if ( da.recordExists(mDSSStageAreaPath))
            {
                pd = new HecPairedData();
                pd.setDSSFileName(mDSSFilePath);
                pd.setPathname(mDSSStageAreaPath);
            }
            else
            {
                pd = da.createPairedDataObject(mDSSStageAreaPath);
            }
            
            int t1 = da.recordType(mDSSStagePath);
            int t2 = da.recordType(mDSSStageAreaPath);

            // write the stage data
            writeStages();

            // write the stage area curve
            writeStageAreaCurve();

            ts.done();
            ts.close();
            ts = null;

            pd.done();
            pd.close();
            pd = null;
        }
        else
        {
            throw new IOException("Failed to open file " + da.DSSFileName());
        }

    }
    
    private void calcDSSFileName()
    {
        String dir = srcFile.getParent();
        mDSSFileName = srcFile.getName();
        String baseName;
        int pos = mDSSFileName.lastIndexOf('.');
        if ( pos != - 1)
        {
            mBaseName = mDSSFileName.substring(0,pos);
        }
        else
        {
            mBaseName = mDSSFileName;
        }
        
        mDSSFileName = mBaseName + ".dss";
        mDSSFilePath = dir + "/" + mDSSFileName;        
    }
    
    private void calcStagePath()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append("/");
        buffer.append(mBaseName);                                    // A Part
        buffer.append("/");
        buffer.append(mBaseName);                                    // B Part
        buffer.append("/");
        buffer.append("STAGE");                                      // C Part
        buffer.append("/");
        buffer.append(getFirstDate().date(5));                      // D Part
        buffer.append("/");
        buffer.append("1DAY");                                       // E Part
        buffer.append("/");
        buffer.append("BASE");                                       // F Part
        buffer.append("/");
        
        mDSSStagePath =  buffer.toString();
    }
    
    private void calcStageAreaPath()
    {
        StringBuffer buffer = new StringBuffer();
        
        buffer.append("/");
        buffer.append(mBaseName);                                    // A Part
        buffer.append("/");
        buffer.append(mBaseName);                                    // B Part
        buffer.append("/");
        buffer.append("STAGE-AREA");                                 // C Part
        buffer.append("/");
        buffer.append("");                                           // D Part
        buffer.append("/");
        buffer.append("STAGE AREA CURVE");                           // E Part
        buffer.append("/");
        buffer.append("TOTAL");                                      // F Part
        buffer.append("/");
        
        mDSSStageAreaPath = buffer.toString();
    }
    
    public File getDSSFile() { return new File(mDSSFilePath); }
    
    public String getDSSStageDataPath() { return mDSSStagePath; }
    
    public String getDSSStageAreaPath() { return mDSSStageAreaPath; }
    
    public HashMap<Double,Double> getStageAreaCurve() { return stageAreaCurve; }
    
    private void writeStages()
    {
        int rval;
        
        HecTimeArray time;
        HecDoubleArray value;
        
        HecTime start;
        HecTime stop;
        HecTime t1;
        HecTime t2;
        
        for( int i = 0; i < mStageData.size();)
        {
            start = new HecTime(mStageData.get(i).getDate(),0);
            
            int j;
            for( j = i+1; j < mStageData.size(); ++j)
            {
                t1 = new HecTime(mStageData.get(j-1).getDate(),0);                
                t2 = new HecTime(mStageData.get(j).getDate(),0);
                
                int val = t2.dayOfYear() - t1.dayOfYear();
                
                if ( val != 1) 
                {
                    break;
                }
            }
            
            stop = new HecTime(mStageData.get(j-1).getDate(),0);
            
            ts.setTimeWindow(start,stop);
            
            int len = j - i;
            
            value = new HecDoubleArray(len);
            
            for(int k = 0; k < len; ++k)
            {
                value.set(k,mStageData.get(i+k).getStage());
            }
            
            ts.setData(value);
            rval = ts.saveDataToDisk();
            
            i = j;
            
        }
        
        //rval = ts.setTimes(time);     
        
        
    }
    
    private void writeStageAreaCurve()
    {
        int num = stageAreaCurve.size();
        
        PairedDataContainer pdc = new PairedDataContainer();
        
        HecDoubleArray xData = new HecDoubleArray(num);
        HecDoubleArray yData = new HecDoubleArray(num);
 
        Double[] keys = new Double[num];
        stageAreaCurve.keySet().toArray(keys);
        Arrays.sort(keys);
        
        for(int i = 0; i < num; ++i)
        {
            xData.set(i,keys[i]);
            yData.set(i,stageAreaCurve.get(keys[i]));
        }
        
        pd.write(xData,yData, true);
        
        int rval = pd.saveDataToDisk();
        
    }
    
    private ArrayList<StageData> mStageData = new ArrayList<StageData>(); 
    private HashMap<Double,Double> stageAreaCurve = new HashMap<Double,Double>();
     
    private double minGage = 0.0;
    private double maxGage = 0.0;
    
    File srcFile;
    
    //variables for the DSS File
    
    HecTimeSeries ts;
    HecPairedData pd;
    
    String mDSSFilePath;
    String mDSSFileName;
    String mBaseName;
    
    String mDSSStagePath;
    String mDSSStageAreaPath;
    
    static Calendar cal = Calendar.getInstance();
                
}
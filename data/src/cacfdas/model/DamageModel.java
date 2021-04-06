/*
 * DamageModel.java
 *
 * Created on April 4, 2006, 9:18 AM
 */

package cacfdas.model;

import java.util.*;
import java.util.prefs.*;

import cacfdas.*;
import cacfdas.reachinfo.*;
import cacfdas.stagedata.*;

import hec.heclib.dss.*;
import hec.heclib.util.*;

/**
 *
 * @author b4edhdwj
 */

/** This class takes input stages, dates, crop planting and damage information and
 *  estimates dmages, costs, and revenues based on the input information */

public class DamageModel {
    
    /** Creates a new instance of DamageModel */
    public DamageModel() 
    {
        init();
    }
    
    private void init()
    {
        prefs = Preferences.userNodeForPackage(this.getClass());
        
        dataStrings = prefs.get("DATA STRINGS", "ELEV,STAGE");
        areaStrings = prefs.get("AREA STRINGS", "ELEV-AREA,STAGE-AREA");
        
        mCropInfo = new CropInfoTable();
        mStageData = new StageDataTable(dataStrings,areaStrings);
        mFloodDamage = new FloodDamageTable();        
    }
    
    ArrayList< HashMap<Integer, ArrayList<DataBand> > > getBands()
    {
        return data;
    }
    
    public CropInfoTable getCropInfoTable()
    {
        return mCropInfo;
    }
    
    public FloodDamageTable getFloodDamageTable()
    {
        return mFloodDamage;
    }
    
    public StageDataTable getStageDataTable()
    {
        return mStageData;
    }
    
    public boolean ready()
    {
        return (mCropInfo != null &&
                mFloodDamage != null &&
                mStageData != null );
    }
    
    void runRange(int startYear, int stopYear)
    {
        data = new ArrayList< HashMap<Integer, ArrayList<DataBand> > >();
        mStartYear = startYear;
        mStopYear = stopYear;
        
        if ( ready() )
        {   
            ts = new HecTimeSeries();
            ts.setDSSFileName(mStageData.getDSSFile().getAbsolutePath());
            ts.setPathname(mStageData.getStagePath());

            for(int i = startYear; i <= stopYear; ++i)
            {
                HecTime startTime = new HecTime("01JAN"+i,"8:00");
                HecTime stopTime = new HecTime("31DEC"+i,"8:00");
                
                HecTime winterStart = new HecTime("01AUG"+i,"8:00");
                HecTime winterStop = new HecTime("31JUL"+i+1,"8:00");

                day = new HecTimeArray();
                winterDay = new HecTimeArray();
                stage = new HecDoubleArray();
                winterStage = new HecDoubleArray();

                ts.setTimeWindow(startTime,stopTime);
                ts.read(day,stage);
                
                if ( getFloodDamageTable().hasWinterCrops() )
                {
                    
                    ts.setTimeWindow(winterStart,winterStop); 
                    ts.read(winterDay,winterStage);
                }

                runYear(i);
            }
            

        }
    }
    
    /** Set the crop info table for this model */
    
    public void setCropInfoTable(CropInfoTable table)
    {
        mCropInfo = table;
    }
    
    /** Set the crop damage table for this model */
    
    public void setFloodDamageTable(FloodDamageTable table)
    {
        mFloodDamage = table;
    }
    
    /** Set the stage data table for this model */
    
    public void setStageDataTable(StageDataTable table)
    {
        mStageData = table;
    }
    
    /** Calculate the counts of the current stage data */
    
    private void calcBandBounds()
    {
        double min = mStageData.getMinStage();
        double max = mStageData.getMaxStage();
        
        min = Math.rint(min*10)/10;
        max = Math.rint(max*10)/10;
        
        int num = (int) ((max - min) * 10) + 1;
        
        bounds = new double[num];
        
        for( int i = 0; i < num; ++i )
        {
            bounds[i] = min + (i/10.0);
            bounds[i] *= 10.0;
            bounds[i] = Math.rint(bounds[i]);
            bounds[i] /= 10.0;
        }
    }
    
    /** Find the flood events in the current time period */
    
    private void calcFloodWindows()
    {
        floodList = new ArrayList<ArrayList<Flood>>(bounds.length-1);
        
        for(int i = 0; i < bounds.length -1; ++i)
        {
            boolean floodStarted = false;
            int dry = 0;
            
            ArrayList<Flood> bandList = new ArrayList<Flood>();
            
            Flood flood = null;
            
            for(int j =0; j < day.numberElements(); ++j)
            {
                // if the current days stage is defined and is greater than the min value for the band
                if ( stage.element(j).isDefined() && stage.element(j).value() >= bounds[i])
                {
                    // check to see if a flood event has been started
                    if ( floodStarted == false )
                    {
                        // start a new flood event
                        floodStarted = true;
                        flood = new Flood();
                        flood.setMinElevation(bounds[i]);
                        flood.start(day.element(j)); 
                    }
                    else
                    {
                        // increase the duration of the current event by 1 day
                        flood.addDay(true);
                    }
                    
                    dry = 0;
                }
                else
                {
                    dry += 1;
                    
                    // check to see if a flood is started
                    if ( floodStarted )
                    {
                        // has the dryout period been reached
                        if ( dry >= mCropInfo.getHeader().getDryOutPeriod() )
                        {
                            // terminate the current flood event 
                            floodStarted = false;
                            
                            flood.stop();
                            bandList.add(flood);
                            flood = null;
                        }
                        else
                        {
                            // increase the duration by one nonflooded day
                            flood.addDay(false);
                        }                        
                    }

                }
            }
                        
            if ( flood != null )
            {
                bandList.add(flood);
                flood = null;
            }            
            
            floodList.add(bandList);
        }
    }
    
    /** Create the elevation data bands for the current year and crop */
    
    private void initBands(int cropNum)
    {
        // get the min and max stage values
        double min = mStageData.getMinStage();
        double max = mStageData.getMaxStage();
        
        // round the values to the nearest 10th
        
        min = Math.rint(min*10)/10;
        max = Math.rint(max*10)/10;
        
        // determin the number of data bands
        int num = (int) ((max - min) * 10);
        
        // initalize the bands
        band = new ArrayList<DataBand>(num);
        
        // create databands between each 2 rounded values
        for(int i = 0; i < num; ++i)
        {
            // get the bounds values
            double val1 = bounds[i];
            double val2 = bounds[i+1];
            
            // get the areas for the bounds
            double a1 = mStageData.getAreaForStage(val1,0);
            double a2 = mStageData.getAreaForStage(val2,0);
            
            // determine the band area
            double acres = a2 - a1;
            
            // add the band if it has a non 0 area
            if ( acres > 0 )
            {
                DataBand data = new DataBand(cropNum,val1,val2,acres,getCropInfoTable());
                band.add(data);
            }
        }
    }
    
    /** Run one ywar of data and find cost, revenue, and damages */
    
    private void runYear(int year)
    {
        HashMap<Integer, ArrayList<DataBand> > map = new HashMap<Integer,ArrayList<DataBand> >();
        
        Integer[] cropList = mCropInfo.getCropList();
        
        calcBandBounds();
        calcFloodWindows();
        
        for(int i = 0; i < cropList.length; ++i)
        {
            if ( mCropInfo.getPercentage(cropList[i]) > 0.0 )
            {
                initBands(cropList[i]);
                
                if ( getFloodDamageTable().getEntryForCropNumber(cropList[i]).isWinterCrop())
                {
                    for(int j = 0; j < band.size(); ++j )
                    {   
                        band.get(j).calculateRevenue(winterDay,winterStage,floodList.get(j),mFloodDamage); 

                        map.put(cropList[i],band);
                    }                    
                }
                else
                {
                    for(int j = 0; j < band.size(); ++j )
                    {   
                        band.get(j).calculateRevenue(day,stage,floodList.get(j),mFloodDamage); 

                        map.put(cropList[i],band);
                    }
                }
            }
        }
        
        data.add(map);
    }
    
    String[] getDataCPartStrings()
    {
        return dataStrings.split(",");
    }
    
    String[] getAreaCPartStrings()
    {
        return areaStrings.split(",");
    }
    
    HecTimeSeries ts;
    
    CropInfoTable mCropInfo;
    StageDataTable mStageData;
    FloodDamageTable mFloodDamage;
    
    HecTimeArray day;
    HecTimeArray winterDay;
    HecDoubleArray stage;
    HecDoubleArray winterStage;
    
    double[] bounds;
    ArrayList<ArrayList<Flood>> floodList;
    
    ArrayList<DataBand> band;
    
    int mStartYear;
    int mStopYear;
    
    ArrayList< HashMap<Integer, ArrayList<DataBand> > > data;
    
    Preferences prefs;
    String dataStrings;
    String areaStrings;
    
}

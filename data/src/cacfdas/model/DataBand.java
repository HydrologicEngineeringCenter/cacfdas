/*
 * DataBand.java
 *
 * Created on April 5, 2006, 7:33 AM
 */

package cacfdas.model;

import cacfdas.*;
import cacfdas.reachinfo.*;

/**
 *
 * @author b4edhdwj
 */

import java.util.*;

import hec.heclib.dss.*;
import hec.heclib.util.*;

/** This class maintains statistics and stores cost, revenue, and damages for an area of land between to elevations */

public class DataBand {
    
    /** Creates a new instance of DataBand */
    public DataBand(int cropNum, double min, double max, double acres, CropInfoTable cropInfo) {
        mCropNumber = cropNum;
        info = cropInfo;
        
        mMaxElevation = max;
        mMinElevation = min;
        mAcres = acres;
    }
    
    
    public void init()
    {
        mDaysWet = 0;
        mDaysDry = 0;
        mDamageCount = 0;
        mFloodCount = 0;
        
        mDate = new ArrayList<HecTime>();
        mDate.ensureCapacity(20);
        
        mStatus = new ArrayList<Status>();
        mStatus.ensureCapacity(20);
        
        mFloodList = new ArrayList<Flood>();
        mFloodList.ensureCapacity(10);        
    }
    
    /** Add a new status change to this band */
    
    public void addStatusChange(HecTime when, Status what)
    {

        mDate.add(when);
        mStatus.add(what);
    }

    /** Determine the cost, revenue, and damages for this data band throught the input time, stages and flood events */
    
    public void calculateRevenue(HecTimeArray time, HecDoubleArray stage, ArrayList<Flood> floodList, FloodDamageTable floodTable)
    {
        init();
        
        mEventList = new ArrayList<Event>();
        
        int pos1 = 0;
        int pos2 = 0;
        
        int flood_index = 0;
        int replant_index = 0;
        
        bandLost = false;
        boolean planted = false;
        boolean damageDone = false;
        
        replantList = info.getReplantList(mCropNumber);
        damageTable = floodTable.getEntryForCropNumber(getCropNumber());
        cropData = info.getData(getCropNumber());       
        
        int t1, t2;
        
        for( int i = 0; i < stage.numberElements(); ++i )
        {
            // first determine the current critcal duration            

           t1 = time.element(i).dayOfYear();
           cal.setTime(damageTable.getRecord(pos1).getDate());
           t2 = cal.get(Calendar.DAY_OF_YEAR);

           while( pos1 +1 < damageTable.numRecords() && t1 > t2 )
           {
                pos1 += 1;
                cal.setTime(damageTable.getRecord(pos1).getDate());
                t2 = cal.get(Calendar.DAY_OF_YEAR);                 
           }
           
           critDur = damageTable.getRecord(pos1).getDuration();
            
            // see if there is an interaction with a flood
            
            if ( floodList.size() > 0 )
            {
                // find the index of the current flood
                t2 = floodList.get(flood_index).stopDate().dayOfYear(); 
                while( flood_index + 1 < floodList.size() && t1 > t2)
                {
                    t2 = floodList.get(++flood_index).stopDate().dayOfYear(); 
                }

                // get the current position in the current flood
                Flood flood = floodList.get(flood_index);   
                int j = t1 - flood.startDate().dayOfYear();

                // if the position is not past the end of the flood
                if ( j < flood.length() && j >= 0 )
                {
                    // if this is the first day of the flood set the band status to flooded
                    // and increment the days wet
                    if ( j == 0 )
                    {
                        damageDone = false;
                        setDaysWet(1);
                        setDaysDry(0);
                        addStatusChange(time.element(i),Status.Flooded);
                    }

                    // if this is the last day of the flood set the band status acording to 
                    // the number of damages and if the band was lost

                   else if ( j == flood.length() - 1)
                   {
                       incrementDaysDry(1);

                       if ( !planted)
                       {
                           addStatusChange(time.element(i),Status.Dry);
                       }
                       else if (bandLost && getStatus() != Status.Lost )
                       {
                           addStatusChange(time.element(i),Status.Lost);
                       }
                       else if ( !damageDone )
                       {
                           addStatusChange(time.element(i),Status.Planted);
                       } 
                       else if ( getStatus() != Status.Damaged)
                       {
                           addStatusChange(time.element(i),Status.Damaged);
                       }
                   }

                   // if this is an interior day of the flood check to see if it is wet or dry
                   // update the count of the coorisponding type and check for damages if wet

                   else
                   {
                       if ( flood.isWet(j) )
                       {
                           incrementDaysWet(1);

                           // see if the field has been wet long enough to cause damages
                           if ( getDaysWet() >= critDur && critDur > 0 && !damageDone)
                           {
                                // increase the damage count
                                if ( !bandLost )
                                {
                                    mDamageCount += 1;
                                }

                                // see if any replants are avaliable
                                if ( replantList == null || replant_index >= replantList.size() )
                                {
                                    // there are no possible replants if band is not allready in failed state place it there
                                    if ( !bandLost )
                                    {
                                        addStatusChange(time.element(i),Status.Lost);
                                        bandLost = true;
                                    }                                    
                                }
                                else
                                {
                                    Date d = info.getLastPlantingDate(replantList.get(replant_index)); 
                                    cal.setTime(d);
                                    t2 = cal.get(Calendar.DAY_OF_YEAR);
                                    while ( replant_index + 1< replantList.size() && t1 > t2) 
                                    {
                                        replant_index += 1;
                                        d = info.getLastPlantingDate(replantList.get(replant_index)); 
                                        cal.setTime(d);
                                        t2 = cal.get(Calendar.DAY_OF_YEAR);
                                    }                    

                                    // see if a replant was found the band is either damaged or lost
                                    if ( t1 < t2 && !bandLost )
                                    {
                                        damageTable = floodTable.getEntryForCropNumber(replantList.get(replant_index));
                                        cropData = info.getData(replantList.get(replant_index));
                                        addStatusChange(time.element(i),Status.Damaged);
                                        planted = false;
                                    }
                                    else
                                    {
                                        if ( !bandLost )
                                        {
                                            addStatusChange(time.element(i),Status.Lost);
                                            bandLost = true;
                                        }
                                    }
                                }

                                damageDone = true;
                           }
                           else
                           {
                               if ( bandLost  && getStatus() != Status.Lost )
                               {
                                   addStatusChange(time.element(i),Status.Lost);
                               }
                               else if ( damageDone && getStatus() != Status.Damaged && getStatus() != Status.Lost )
                               {
                                   addStatusChange(time.element(i),Status.Damaged);
                                   planted = false;
                               }
                               else if ( getStatus() != Status.Flooded && getStatus() != Status.Damaged && getStatus() != Status.Lost )
                               {
                                   addStatusChange(time.element(i),Status.Flooded);
                               }                               
                               
                           }
                       }
                       else
                       {
                           incrementDaysDry(1);

                           if ( getStatus() != Status.Drying )
                           {
                               addStatusChange(time.element(i),Status.Drying);
                           }
                       }
                   }
               }

            }
            else
            {
               incrementDaysDry(1);
            }           
            
            // Check to see if planting can happen on this day
            if ( getDaysDry() >= 1 && !bandLost)
            {                
                if ( mDamageCount == 0 && !planted )
                {
                    cal.setTime(cropData.getMinOperationDate());
                    int startDay = cal.get(Calendar.DAY_OF_YEAR);
                    
                    cal.setTime(cropData.getLastPlantingDate());
                    int stopDay = cal.get(Calendar.DAY_OF_YEAR);

                    if ( startDay <= t1 && t1 <= stopDay && !planted )
                    {
                        addStatusChange(time.element(i),Status.Planted);
                        planted = true;
                        damageDone = false;
                    }
                }
                else if ( mDamageCount == 1)
                {
                    cal.setTime(cropData.getLastPlantingDate());
                    int startDay = cal.get(Calendar.DAY_OF_YEAR);

                    if ( t1 <= startDay && getStatus() != Status.Replant1 )
                    {
                        addStatusChange(time.element(i),Status.Replant1);
                        planted = true;
                        damageDone = false;
                    }
                }
                else if ( mDamageCount == 2)
                {
                    cal.setTime(cropData.getLastPlantingDate() );
                    int startDay = cal.get(Calendar.DAY_OF_YEAR);

                    if ( t1 <= startDay  && getStatus() != Status.Replant2 )
                    {
                        addStatusChange(time.element(i),Status.Replant2);
                        planted = true;
                        damageDone = false;
                    }
                }
            }
            else if ( !planted )
            {
                // see if the last day for planting has passed
                
                cal.setTime(cropData.getLastPlantingDate() );
                t2 = cal.get(Calendar.DAY_OF_YEAR);
                
                // the last chance to plant the current crop was missed
                if ( t1 >= t2 )
                {
                    boolean replant_found = false;
            
                    while( replantList != null && replantList.size() > replant_index +1 )
                    {
                        replant_index += 1;
                        
                        cal.setTime(info.getLastPlantingDate(replantList.get(replant_index)));
                        t2 = cal.get(Calendar.DAY_OF_YEAR);
                        
                        if ( t2 > t1)
                        {
                            replant_found = true;
                            break;
                        }
                    }
                    
                    if ( replant_found )
                    {
                        if ( !bandLost )
                        {
                            addStatusChange(time.element(i),Status.Damaged);
                            damageTable = floodTable.getEntryForCropNumber(replantList.get(replant_index));
                            cropData = info.getData(replantList.get(replant_index));
                            damageDone = true;
                            mDamageCount += 1;
                        }
                    }
                    else
                    {
                        if( !bandLost )
                        {
                            addStatusChange(time.element(i),Status.Lost);
                            bandLost = true;
                            damageDone = true;
                            mDamageCount += 1;
                        }
                    } 
                }
            }
           
            // check the to see if any cost or revenues occure on this day
            Date day2 = damageTable.getRecord(pos2).getDate();
            
            cal.setTime(day2);
            t2 = cal.get(Calendar.DAY_OF_YEAR);
            
            // advance to the next event to occure
            while ( t2 < t1 && pos2 + 1 < damageTable.numRecords() )
            {
                pos2 += 1;
                
                day2 = damageTable.getRecord(pos2).getDate();
                cal.setTime(day2);
                t2 = cal.get(Calendar.DAY_OF_YEAR);                
            }
            
            // if the next event is on this day record revenue change
            if ( t1 == t2 && (!bandLost || t1 == 1) )
            {
                 Event e = new Event();
                
                e.date = time.element(i);
                e.mCost = damageTable.getRecord(pos2).getCost();
                e.mRevenue = damageTable.getRecord(pos2).getRevenue();
                e.mFloodDamage = false;
                e.mCropNumber = damageTable.getCropNumber();
                
                mEventList.add(e);
            }            
        }
        
        mFloodList = floodList;
        mFloodCount = mFloodList.size();
    }
    
    /** Get the crop number for this data band */
    
    public int getCropNumber()
    {
        return mCropNumber;
    }
    
    public double getMinElevation()
    {
        return mMinElevation;
    }
    
    public double getMaxElevation()
    {
        return mMaxElevation;
    }
    
    
    /** return the last recorded status */
    
    Status getStatus()
    {
        if ( mStatus.size() == 0)
        {
            return Status.Flooded;
        }
        else
        {
            return mStatus.get(mStatus.size()-1);
        }
    }
    
    /** Status getStatus(HecTime t) 
     *  Determine the status of this band at the query time. If the input time is before any recorded
     *  status, the status Flooded is recorded. If the input time is between 2 status change events the 
     *  status set by the preceding event is returned. If the input time is on a status change event the
     *  status of that event is returned. If the input time is after the last recorded event then the status
     * of the last event is returned */
    
    Status getStatus(HecTime t)
    {
        // check all recorded status until the correct window is found
        for(int i = 0; i < mDate.size(); ++i)
        {
            int val = t.compareTimes(mDate.get(i));
            
            // on an exact match return the current event
            if ( val == 0)
            {
                return mStatus.get(i);
            }
            // we have found the end of the enclosing window return the previous status or flooded if
            // there is no previous record
            else if ( val > 0 )
            {
                if ( i > 0 )
                {
                    return mStatus.get(i-1);
                }
                else
                {
                    return Status.Flooded;
                }
            }
        }
        
        // no window could be found return the last status
        return mStatus.get(mStatus.size()-1);
    }
    
    /* Return the number oif acres in this data band this is calculated by multiplying the
     * number of acres in the elevation range, by the coverage percentage of this bands inital crop */ 
    
    public double getAcres()
    {
        return mAcres * info.getPercentage(mCropNumber);
    }
    
    /** Get the number of acres times the number of flood events for this band */
    
    public double getBandedAcres()
    {
        return getAcres() * mFloodCount;
    }
    
    /** Get the number of consecutive days that this band has been dry */
    
    public int getDaysDry()
    {
        return mDaysDry;
    }
    
    /** Get the number of consecutive days that this band has been wet */
    
    public int getDaysWet()
    {
        return mDaysWet;
    }
    
    /** Get the list of events that has occured in this band durring the last time period. Cost
     *  and revenue are generated by events */
    
    ArrayList<Event> getEventList()
    {
        return mEventList;
    }
    
    /** Get a list of the flood records for the last time period. Flood records record the begining date of
     *  a flood, its dryout date, and the daily status of wither flooding is occuring on each day within the record */
    
    ArrayList<Flood> getFloodList()
    {
        return mFloodList;
    }
    
    /** Get the list of status changes for this data band */
    
    ArrayList<Status> getStatusList()
    {
        return mStatus;
    }
    
    /** Get the list of the dates that status changes occured */
    
    ArrayList<HecTime> getDateList()
    {
        return mDate;
    }
    
    /** return the number of floods in the last time period */
    
    public int getNumFloods()
    {
        return mFloodList.size();
    }
    
    /** Return the number of times this band has been damaged */
    
    public int getDamageCount()
    {
        return mDamageCount;
    }
    
    /** indicates if this band was lost due to flood damage and no possible replants */
    
    public boolean isLost()
    {
        return bandLost;
    }
    
    /** Get the cost of all events that occured in the last time period */
    
    public double getTotalCost()
    {
        Iterator<Event> iter = mEventList.iterator();
        double cost = 0.0;
        
        while( iter.hasNext() )
        {
            Event e = iter.next();
            cost += e.mCost;
        }
        
        return cost * getAcres();
    }
    
    /** Get the revenue generated by all events in the last period */
    
    public double getTotalRevenue()
    {
        Iterator<Event> iter = mEventList.iterator();
        double cost = 0.0;
        
        while( iter.hasNext() )
        {
            Event e = iter.next();
            cost += (e.mRevenue*info.getGrossRevenue(e.mCropNumber));
        }
        
        cost *= getAcres();
        
        return cost;
    }
    
    /** Get Lost Reveneue */
    
    public double getLostRevenue()
    {
        double initalNetRevenue = info.getNetRevenue(getCropNumber()) * getAcres();
        
        switch( mDamageCount )
        {
            case 0:
                return 0;
                
            default:
                return initalNetRevenue - (getTotalRevenue() - getTotalCost());        
            /*    
            case 2:
                return (getAcres() * info.getGrossRevenue(getCropNumber())) + 
                       (getAcres() * info.getGrossRevenue(info.getReplantList(getCropNumber()).get(0))) -
                        getTotalRevenue();
                
            case 3:
                 return (getAcres() * info.getGrossRevenue(getCropNumber())) + 
                        (getAcres() * info.getGrossRevenue(info.getReplantList(getCropNumber()).get(0))) +
                        (getAcres() * info.getGrossRevenue(info.getReplantList(getCropNumber()).get(1))) -
                         getTotalRevenue();
                 
            default:
                return 0;*/
                               
        }
    }
    
    /** Increase the count of dry days by num */
    
    void incrementDaysDry(int num)
    {
        mDaysDry += num;
    }
    
    /** Increase the count of wet days by num */
    
    void incrementDaysWet(int num)
    {
        mDaysWet += num;
        mDaysDry = 0;
    }
    
    /** Set the minimum elevation that is part of this band */
    
    public void setMinElevation(double val)
    {
        mMinElevation = val;
    }
    
    /** Set the maximum elevation that is part of this band */
    
    public void setMaxElevation(double val)
    {
        mMaxElevation = val;
    }
    
    /** Set the number of acres between the min and max elevation values */
    
    public void setBaseAcres(double val)
    {
        mAcres = val;
    }
    
    /** Set the number of consecutive days that this band has bben dry */
    
    public void setDaysDry(int num)
    {
        mDaysDry = num;
    }
    
    /** Set the number of consecutive days that this band has been wet */
    
    public void setDaysWet(int num)
    {
        mDaysWet = num;
    }
    
    public String toString()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[DataBand min = ");
        buffer.append(mMinElevation);
        buffer.append(" max =");
        buffer.append(mMaxElevation);
        buffer.append(" crop = ");
        buffer.append(info.getInfo(mCropNumber));
        
        return buffer.toString();
    }
    
    /** The possible states of a data band */
    enum Status { Flooded, Dry, Drying, Planted, Damaged, Replant1, Replant2, Lost };
    
    
    private double mMaxElevation;
    private double mMinElevation;
    private double mAcres;
    
    private int mCropNumber;
    private CropInfoTable info;
    
    private int mDaysWet;
    private int mDaysDry;
    private int mDamageCount;
    private int mFloodCount;
    
    private ArrayList<HecTime> mDate;
    private ArrayList<Status> mStatus;
    
    private boolean bandLost;
    
    private Calendar cal = Calendar.getInstance();
    
    private ArrayList<Integer> replantList;
    private CropDamageTable damageTable;
    private CropInfoTable.TableData cropData;
    private int critDur;
    
    class Event
    {
        public HecTime getDate() { return date; }
        public double getRevenue() { return mRevenue; }
        public double getCost() { return mCost; }
        public boolean getFloodDamage() { return mFloodDamage; }
        public int getCropNumber() { return mCropNumber; }
        
        private HecTime date;
        private double mRevenue;
        private double mCost;
        private boolean mFloodDamage;
        private int mCropNumber;
    }
    
    private ArrayList<Event> mEventList;
    private ArrayList<Flood> mFloodList;
}

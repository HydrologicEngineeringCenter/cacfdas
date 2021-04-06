/*
 * Flood.java
 *
 * Created on April 25, 2006, 4:03 PM
 */

package cacfdas.model;

/**
 *
 * @author b4edhdwj
 */

import hec.heclib.util.*;

import java.util.*;


/** This class represents a single flood event from the inital day of flooding until the dryout period */

public class Flood
{
    /** Create a new flood instance */
    
    public Flood()
    {
        status = new ArrayList<Boolean>();
        
        mStartDate = new HecTime();
        mStopDate = new HecTime();
    }
    
    /** Set the start time of a flood */
    
    public void start(HecTime t)
    {
        mStartDate.set(t);
        mStopDate.set(t);
        
        mStartDate.setTimeIncrement(HecTime.DAY_INCREMENT);
        mStopDate.setTimeIncrement(HecTime.DAY_INCREMENT);        
        
        status.clear();
        status.add(true);
        
        mDaysWet = 1;
    }
    
    /** Adds a final day to the flood */
    
    public void stop()
    {
        mStopDate.add(1);
        status.add(false);
    }
    
    /** Add a day record to the flood the input states if flooding occures on this dy */
    
    public void addDay(boolean b)
    {
        mStopDate.add(1);
        status.add(b);
        
        if ( true )
        {
            mDaysWet += 1;
        }
    }
    
    public int daysWet()
    {
        return mDaysWet;
    }
    
    /** Does flooding occure on this day of the flood */
    
    boolean isWet(int day)
    {
        if ( day < status.size() && day >= 0)
        {
            return status.get(day);
        }
        else
        {
            return false;
        }
    }
    
    public void setMinElevation(double val)
    {
        minElevation = val;
    }
    
    /** Return the first day of the flood */
    
    HecTime startDate()
    {
        return mStartDate;
    }
    
    /** Return the last day of the dryout period that ends the flood */
    
    HecTime stopDate()
    {
        return mStopDate;
    }
    
    /** Return the number of days in the flood */
    
    int length()
    {
        return status.size();
    }
    
    /** Return a string representation of the event */
    
    public String toString()
    {
        return "[Flood start=" + mStartDate.date(1) + ". stop=" + mStopDate.date(1) + "]"; 
    }
        
    HecTime mStartDate;
    HecTime mStopDate;
    int mDaysWet;
    double minElevation;
    
    ArrayList<Boolean> status;
}


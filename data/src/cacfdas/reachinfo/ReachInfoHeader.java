package cacfdas.reachinfo;
/*
 * ReachInfo.java
 *
 * Created on March 20, 2006, 11:27 AM
 */

/**
 *
 * @author b4edhdwj
 */

import cacfdas.exception.*;

import java.text.*;

public class ReachInfoHeader {
    
    /** Creates a new instance of ReachInfo */
    public ReachInfoHeader() 
    {
        clear();
    }
    
    public ReachInfoHeader(String s) throws ReachHeaderInputFormatException
    {
        String temp;
        
        if ( s.substring(0,1).equals("1") )
        {
            // read the flags
            temp = (s.substring(2,3));
            if ( temp.equals("1"))
            {
                mMode = Mode.Debug;
            }
            else if ( temp.equals("2"))
            {
                mMode = Mode.Brief;
            }
            else if ( temp.equals("0") || temp.equals(" "))
            {
                mMode = Mode.Report;
            }
            else
            {
                throw new ReachHeaderInputFormatException();
            }
              
            temp = s.substring(3,4);
            if ( temp.equals("1"))
            {
                mFreeFormat = true;
            }
            else if ( temp.equals(" ") || temp.equals("0"))
            {
                mFreeFormat = false;
            }
            else
            {
                throw new ReachHeaderInputFormatException();
            }

            temp = s.substring(4,5);
            if ( temp.equals("1"))
            {
                mOverBooking = true;
            }
            else if ( temp.equals(" ") || temp.equals("0"))
            {
                mOverBooking = false;
            }
            else
            {
                throw new ReachHeaderInputFormatException();
            }                
                
            try
            {
                             // read the integer variables
                mNumYears = Integer.parseInt(s.substring(5,7).trim());
                mDryOut = Integer.parseInt(s.substring(7,9).trim());
            }
            catch (NumberFormatException e)
            {
               throw new ReachHeaderInputFormatException(); 
            }
            
            try
            {
                // get the partial damage percentage
                mPartialPercentage = Double.parseDouble(s.substring(9,13));      
            }
            catch(NumberFormatException e)
            {
                mPartialPercentage = 0.0;
            }
            
                            
             // get the reach name
             mReachName = s.substring(15);
        }
        else
        {
            throw new ReachHeaderInputFormatException();
        }
    }
    
    public enum Mode 
    {
        Report,
        Debug,
        Brief
    };
    
    public void clear()
    {
        mMode = Mode.Debug;
        mFreeFormat = false;
        mOverBooking = false;
        
        mNumYears = 0;
        mDryOut = 0;
        
        mPartialPercentage = 0.0;
        
        mReachName = "";
        
        
    }
    
    String getFileString()
    {
        StringBuffer buffer = new StringBuffer();
        String temp;
        
        // the reach header line begins with a 1
        buffer.append("1 ");
        
        // add the mode flag
        switch(mMode)
        {
            case Report:
                buffer.append("0");
            break;
            
            case Debug:
                buffer.append("1");
            break;
            
            case Brief:
                buffer.append("2");
            break;
        }
        
        // add the free format flag
        if ( mFreeFormat )
        {
            buffer.append("1");
        }
        else
        {
            buffer.append(" ");
        }
        
        // add the ober blooking flag
        if ( mOverBooking )
        {
            buffer.append("1");
        }
        else
        {
            buffer.append(" ");
        }
        
        NumberFormat nf1 = new DecimalFormat(); 
        nf1.setMinimumIntegerDigits(2);
        nf1.setMaximumFractionDigits(2);
        nf1.setMinimumFractionDigits(0);
        nf1.setMaximumFractionDigits(0);
        
        NumberFormat nf2 = new DecimalFormat(); 
        nf2.setMinimumIntegerDigits(1);
        nf2.setMaximumIntegerDigits(1);
        nf2.setMinimumFractionDigits(2);
        nf2.setMaximumFractionDigits(2);        
        
        // add the number of years
        // note the text format will not suport more than 99 years
        buffer.append(nf1.format(mNumYears));
        
        // add the dryout period
        buffer.append(nf1.format(mDryOut));
        
        // add the partial damage coeficent
        buffer.append(nf2.format(mPartialPercentage));
        
        buffer.append(" ");
        
        buffer.append(mReachName);
        
        return buffer.toString();
    }
    
    public Mode getMode() { return mMode; }
    public boolean getFreeFormat() { return mFreeFormat; }
    public boolean getOverBooking() { return mOverBooking; }
    
    public String getName() { return mReachName; }
    
    public int getNumYears() { return mNumYears; }
    public int getDryOutPeriod() { return mDryOut; }
    
    public double getPartialPercentage() { return mPartialPercentage; }
    
    public void setMode(Mode m) { mMode = m; }
    public void setFreeFormat(boolean b) { mFreeFormat = b; }
    public void setOverBooking(boolean b) { mOverBooking = b; }
    
    public void setName(String s) { mReachName = s; }
    
    public void setNumYears(int num) { mNumYears = num; }
    public void setBryOutPeriod(int num) { mDryOut = num; }
    public void setPartialPercentage(double d) { mPartialPercentage = d; }
    
    private Mode mMode;
    private boolean mFreeFormat;
    private boolean mOverBooking;
    
    private int mNumYears;
    private int mDryOut;
    
    private double mPartialPercentage;
    
    private String mReachName;
}

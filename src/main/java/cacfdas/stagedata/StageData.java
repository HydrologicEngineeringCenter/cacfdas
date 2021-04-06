/*
 * StageData.java
 *
 * Created on March 28, 2006, 1:06 PM
 */

package cacfdas.stagedata;

/**
 *
 * @author b4edhdwj
 */

import java.util.*;

public class StageData {
    
    public StageData(Date date, double gage)
    {
        mDate = date;
        mGage = gage;
    }
        
    public Date getDate() { return mDate; }
    public double getStage() { return mGage; }
        
    public void setDate(Date date) { mDate = date; }
    public void setStage(double val) { mGage = val; }        
        
    private Date mDate;
    private double mGage;
    
}

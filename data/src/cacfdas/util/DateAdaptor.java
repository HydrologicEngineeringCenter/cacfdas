/*
 * DateAdaptor.java
 *
 * Created on April 6, 2006, 9:02 AM
 */

package cacfdas.util;

/**
 *
 * @author b4edhdwj
 */

import java.util.*;
import java.text.*;

public class DateAdaptor {
    
    /** Creates a new instance of DateAdaptor */
    public DateAdaptor(Date date) 
    {
        mDate = date;
    }
    
    public DateAdaptor(String s) throws ParseException
    {
        if ( s.trim().equals("") )
        {
            mDate = null;
            return;
        }
        
        try
        {
            mDate = sFormat.parse(s);
        }
        catch ( ParseException e1)
        {
            try
            {
                mDate = mFormat.parse(s);
            }
            catch( ParseException e2)
            {
                mDate = lFormat.parse(s);
            }
        }
    }
    
    public Date getDate() { return mDate; }
    public void setDate(Date date) { mDate = date; }
    
    public String toString()
    {
        if ( mDate != null)
        {
            return mFormat.format(mDate);
        }
        else
        {
            return "";
        }
    }
    
    private Date mDate = new Date();
    
    static private DateFormat sFormat = DateFormat.getDateInstance(DateFormat.SHORT);
    static private DateFormat mFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
    static private DateFormat lFormat = DateFormat.getDateInstance(DateFormat.LONG);
}

/*
 * HecTimeAdaptor.java
 *
 * Created on April 13, 2006, 4:41 PM
 */

package cacfdas.util;

/**
 *
 * @author b4edhdwj
 */

import hec.heclib.util.*;

import java.text.*;

public class HecTimeAdaptor
{
    public HecTimeAdaptor()
    {
        HecTime t = new HecTime();
    }
        
    public HecTimeAdaptor(HecTime time)
    {
        t = new HecTime(time);
    }
        
    public HecTimeAdaptor(String s) throws ParseException
    {
        t = new HecTime(s);
            
        if ( ! t.isDefined() )
        {
            throw new ParseException(s,0);
        }
    }
        
    public String toString()
    {
        return t.date(1);
    }
        
    public HecTime value()
    {
        return t;
    }
        
    private HecTime t;
        
    
}
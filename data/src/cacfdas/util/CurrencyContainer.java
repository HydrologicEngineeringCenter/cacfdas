/*
 * CurrenyContainer.java
 *
 * Created on May 1, 2006, 2:18 PM
 */

package cacfdas.util;

/**
 *
 * @author b4edhdwj
 */

import java.util.*;
import java.text.*;
             
public class CurrencyContainer 
{
    public CurrencyContainer(double val)
    {
        mVal = val;
    }
        
    public CurrencyContainer(String s) throws ParseException 
    {
        mf.parse(s);
    }
    
    public double getValue()
    {
        return mVal;
    }
    
    public String toString()
    {
        return mf.format(mVal);
    }
        
    private double mVal;
        
    private NumberFormat mf = DecimalFormat.getCurrencyInstance(); 
}

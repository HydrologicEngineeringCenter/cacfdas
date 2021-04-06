/*
 * ArrayAdaptor.java
 *
 * Created on April 6, 2006, 10:09 AM
 */

package cacfdas.util;

/**
 *
 * @author b4edhdwj
 */

import java.util.*;
import java.text.*;

public class IntegerArrayListAdaptor { 
    
    /** Creates a new instance of ArrayAdaptor */
    public IntegerArrayListAdaptor()
    {
        mData = new ArrayList<Integer>();
    }
    
    public IntegerArrayListAdaptor(ArrayList<Integer> data) 
    {
        mData = data;
    }
    
    public IntegerArrayListAdaptor(String s) 
        throws ParseException, 
            java.lang.ClassNotFoundException,
            java.lang.NoSuchMethodException,
            java.lang.InstantiationException,
            java.lang.IllegalAccessException,
            java.lang.reflect.InvocationTargetException
    {
        s = s.trim();
        
        // if string is null return we have an empty list
        if ( s.equals("") )
        {
            return;
        }
        
        if ( s.length() < 2 )
        {
            throw new ParseException("String is insufficent length",0); 
        }
        
        if ( s.charAt(0) != '[')
        {
            throw new ParseException("Expect '['",0); 
        }
        
        if ( !s.endsWith("]") )
        {
            throw new ParseException("Expected ']'",s.length()-1);
        }
        
        s = s.substring(1,s.length()-1);
        
        // check to see if the list is null
        if ( s.equals(""))
        {
            return;
        }
        
        mData = new ArrayList<Integer>();
        String[] parts = s.split(","); 
        
        for(int i = 0; i < parts.length; ++i)
        {
            Integer val = new Integer(parts[i].trim());
            mData.add(val);
        }
        

    }
    
    public ArrayList<Integer> getList() { return mData; }
    
    public String toString()
    {
        return mData.toString();
    }
    
    private ArrayList<Integer> mData;
    
}

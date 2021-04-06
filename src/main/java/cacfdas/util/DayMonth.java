/*
 * DayMonth.java
 *
 * Created on March 15, 2006, 9:30 AM
 */

package cacfdas.util;

import java.util.*;
import java.text.*;

/**
 *
 * @author b4edhdwj
 */
public class DayMonth 
{
    public DayMonth(Date d)
    {
        if ( d != null)
        {
            StringBuffer buffer1 = new StringBuffer();
            FieldPosition pos1 = new FieldPosition(DateFormat.MONTH_FIELD);
            dateFormat.format(d,buffer1,pos1);

            month = Integer.parseInt(buffer1.substring(pos1.getBeginIndex(),pos1.getEndIndex()).toString());

            StringBuffer buffer2 = new StringBuffer();
            FieldPosition pos2 = new FieldPosition(DateFormat.DATE_FIELD);
            dateFormat.format(d,buffer2,pos2);

            day = Integer.parseInt(buffer2.substring(pos2.getBeginIndex(),pos2.getEndIndex()).toString());
            
            mNull = false;
        }
        else
        {
            month = -1;
            day = -1;
            mNull = true;
        }
    }
    
    public DayMonth(String s) throws ParseException
    {
        // remove and unessessary spaces
        s = s.trim();
        
        if ( s.equals("") )
        {
            mNull = true;
            day = -1;
            month = -1;
            return;
        }
        
        // find the index of potental seperators
        int spaceIndex = s.indexOf(' ');
        int dashIndex = s.indexOf('-');
        int slashIndex = s.indexOf('/');
            
        String delim;
            
        if ( spaceIndex != -1 && dashIndex == -1 && slashIndex == -1)
        {
            delim = " ";
        }
        else if ( spaceIndex == -1 && dashIndex != -1 && slashIndex == -1)
        {
            delim = "-";
        }
        else if ( spaceIndex == -1 && dashIndex == -1 && slashIndex != -1)
        {
            delim = "/";
        }
        else if ( spaceIndex != -1 && dashIndex != -1 && slashIndex == -1)
        {
            delim = (spaceIndex < dashIndex) ? " " : "-";
        }
        else if ( spaceIndex != -1 && dashIndex == -1 && slashIndex != -1)
        {
            delim = (spaceIndex < slashIndex) ? " " : "/";
        }
        else if ( spaceIndex == -1 && dashIndex != -1 && slashIndex != -1)
        {
            delim = (dashIndex < slashIndex) ? "-" : "/";
        }
        else if ( spaceIndex != -1 && dashIndex != -1 && slashIndex != -1)
        {
            if ( spaceIndex < dashIndex && spaceIndex < slashIndex)
            {
                delim = " ";
            }
            else if ( dashIndex < spaceIndex && dashIndex < slashIndex)
            {
                delim = "-";
            }
            else
            {
                delim = "/";
            }
        }        
        else
        {
            throw new ParseException("No Delimeter Found in String",s.length()-1);
        }
            
        StringTokenizer parser = new StringTokenizer(s,delim,false);
            
        if ( parser.countTokens() < 2 )
        {
            throw new ParseException("Unexpected end of string",s.length()-1);
        }
            
        String tol1 = parser.nextToken().trim();
        String tol2 = parser.nextToken().trim();
            
        // first check for numeric tolkens
        
        boolean firstNumeric;
        boolean secondNumeric;
        
        int num1 = -1;
        int num2 = -1;
        
        try
        {
            num1 = Integer.parseInt(tol1);
            firstNumeric = true;
        }
        catch(NumberFormatException e)
        {
            firstNumeric = false;    
        }
        
        try
        {
            num2 = Integer.parseInt(tol2);
            secondNumeric = true;
        }
        catch(NumberFormatException e)
        {
            secondNumeric = false;    
        }       
        
        int dayNum = -1;
        
        // if both tolkens are numbers then the date format most be numeric month then year
        if (firstNumeric && secondNumeric)
        {
            month = num1;
            dayNum = num2;
        }
        // if the first tolken is numeric the format most be day month
        else if (firstNumeric)
        {
            // try to find a matching month
            boolean found = false;
            for(int i = 0; i < 12; ++i)
            {
                if ( tol2.equalsIgnoreCase(monthsShort[i]) || tol2.equalsIgnoreCase(monthsLong[i]))
                {
                    found = true;
                    month = i + 1;
                }
            }
            
            // if a month was found set the day
            if ( found )
            {
                dayNum = num1;
            }
            else
            {
                throw new ParseException("Month Expected",s.length()-1);
            }
        }
        else if (secondNumeric)
        {
            // try to find a matching month
            boolean found = false;
            for(int i = 0; i < 12; ++i)
            {
                if ( tol1.equalsIgnoreCase(monthsShort[i]) || tol1.equalsIgnoreCase(monthsLong[i]))
                {
                    found = true;
                    month = i + 1;
                    break;
                }
            }
            
            // if a month was found set the day
            if ( found )
            {
                dayNum = num2;
            }
            else
            {
                throw new ParseException("Month Expected",s.length()-1);
            }            
        }
        else
        {
            throw new ParseException("Day Expected",s.length()-1);
        }
        
        switch (month)
        {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
 
            if ( dayNum <= 31 )
            {
                day = dayNum;
                mNull = false;
                return;
            }
            else
            {
                throw new ParseException("Invalid Day",s.length()-1);
            }                        
                                     
            case 2:
                        
            if ( dayNum <= 29 )
            {
                day = dayNum;
                mNull = false;
                return;
            }
            else
            {
               throw new ParseException("Invalid Day",s.length()-1);
            }
                    
            case 4:
            case 6:
            case 9:
            case 11:
                    
            if ( dayNum <= 30 )
            {
                day = dayNum;
                mNull = false;
                return;
            }
            else
            {
                throw new ParseException("Invalid Day",s.length()-1);
            }

        }          
             
    }
    
    public boolean equals(Object o)
    {
        if (o.getClass() == DayMonth.class)
        {
            DayMonth dm = (DayMonth) o;
            
            return dm.day == day && dm.month == month;
        }
        else
        {
            return false;
        }
    }
    
    public int hashCode()
    {
        return month * 100 + day;
    }
    
    public String toString()
    {
        return toString(MEDIUM);
    }
    
    public String toString(int style)
    {
        if (! mNull )
        {
            StringBuffer buffer = new StringBuffer();

            switch(style)
            {
                case SHORT:
                    buffer.append(month);
                    buffer.append('/');
                    buffer.append(day);
                    return buffer.toString();
                case MEDIUM:
                    buffer.append(monthsShort[month-1]);
                    buffer.append(" ");
                    buffer.append(day);
                    return buffer.toString();
                case LONG:
                    buffer.append(monthsLong[month-1]);
                    buffer.append(" ");
                    buffer.append(day);
                    return buffer.toString();
                default:
                    buffer.append(monthsShort[month-1]);
                    buffer.append(" ");
                    buffer.append(day);
                    return buffer.toString();
            }
        }
        else
        {
            return "";
        }
    }
    
    public Date toDate()
    {
        if (!mNull)
        {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.MONTH,month-1);
            cal.set(Calendar.DATE,day);

            return cal.getTime();
        }
        else
        {
            return null;
        }
    }
    
    private boolean mNull;
    
    private int month;
    private int day;
    
    private DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT);
    
    static final int SHORT = 0;
    static final int MEDIUM = 1;
    static final int LONG  = 2;
        
    static String[] monthsShort = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
    static String[] monthsLong = {"January","February","March","April","May","June","July","August","September","October","November","December"}; 
}
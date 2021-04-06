/*
 * CropDamageTable.java
 *
 * Created on March 13, 2006, 10:50 AM
 */

package cacfdas;

import java.util.*;
import java.io.*;

/**
 *
 * @author b4edhdwj
 */
public class CropDamageTable {
    
    /** Creates a new instance of CropDamageTable */
    public CropDamageTable()
    {
        mEntryList = new ArrayList<TableEntry>();
        
        mDirty = false;
    }
    
    public CropDamageTable(int cropnum, String cropname, boolean winter)
    {
        mEntryList = new ArrayList<TableEntry>();
        
        mCropNumber = cropnum;
        mCropName = cropname;
        mWinter = winter;
        mDirty = false;
    }
    
    public void addRecord(Date date, double cost, double revenue, int duration)
    {
        mEntryList.add(new TableEntry(date,cost,revenue,duration));
        
        mDirty = true;
    }
    
    public void addRecord(Date date, double cost, double revenue, int duration, String comment)
    {
        mEntryList.add(new TableEntry(date,cost,revenue,duration,comment));
        
        mDirty = true;
    }
    
    public String getCropName() 
    { 
        return mCropName; 
    }
    
    public int getCropNumber() 
    { 
        return mCropNumber; 
    }
    
    public TableEntry getLastRecord()
    {
        return mEntryList.get(mEntryList.size() - 1);
    }
    
    public TableEntry getRecord(int i)
    {
        return mEntryList.get(i);
    }
    
    public TableEntry getRecordByDate(Date d)
    {
        cal.setTime(d);
        int day = cal.get(Calendar.DAY_OF_YEAR);
        
        Iterator<TableEntry> iter = mEntryList.iterator();
        
        while (iter.hasNext() )
        {
            TableEntry entry = iter.next();
            cal.setTime(entry.getDate());
            
            if ( day == cal.get(Calendar.DAY_OF_YEAR))
            {
                return entry;
            }
        }
        
        return null;
    }
    
    public boolean isDirty()
    {
        return mDirty;
    }
    
    public boolean isWinterCrop()
    {
        return mWinter;
    }
    
    public int numRecords()
    {
        return mEntryList.size();
    }
    
    public void removeRecord(int i)
    {
        mEntryList.remove(i);
        
        mDirty = true;
    }
    
    public void removeRecords(int start, int stop)
    {
        int num = stop - start + 1;
        
        for( int i = 0; i < num; ++i )
        {
            mEntryList.remove(start);
        }
        
        mDirty = true;
    }
    
    public void setCropName(String s) 
    { 
        mCropName = s; 
    }
    
    public void setCropNumber(int n) 
    { 
        mCropNumber = n; 
    }
    
    public void setIsDirty(boolean b)
    {
        mDirty = b;
    }
    
    public void setIsWinterCrop(boolean b)
    {
        mWinter = b;
    }
    
    public void sortRecords()
    {
        java.util.Collections.sort(mEntryList, new Comparator<TableEntry>() {
            public int compare(TableEntry a,TableEntry b) {
                return a.getDate().compareTo(b.getDate());
            }   
        });
    }
    
    public String toString()
    {
        StringBuffer buffer = new StringBuffer();
        
        buffer.append(mCropName);
        buffer.append("\nCrop Number: ");
        buffer.append(mCropNumber);
        if ( mWinter )
        {
            buffer.append("\nWinter Crop");
        }
        else
        {
            buffer.append("\nStandard Crop");
        }
        buffer.append("\nNum events: ");
        buffer.append(mEntryList.size());
        
        return buffer.toString();
        
    }
    
    /** This class holds the information about a single dated entry to  *
     *  the crop damage table                                           */
    
    public class TableEntry
    {
        public TableEntry()
        {
            mDate = new Date();
            mCost = 0;
            mRevenue = 0;
            mDuration = 0;
        }

        public TableEntry(Date date, double cost, double revenue, int duration)
        {
            mDate = (Date) date.clone();
            mCost = cost;
            mRevenue = revenue;
            mDuration = duration;
        }

        public TableEntry(Date date, double cost, double revenue, int duration, String comment)
        {
            mDate = (Date) date.clone();
            mCost = cost;
            mRevenue = revenue;
            mDuration = duration;
            mComment = comment;
        }        
        
        
        public Date getDate() { return mDate; }
        public double getCost() { return mCost; }
        public double getRevenue() { return mRevenue; }
        public int getDuration() { return mDuration; }
        public String getComment() { return mComment; }
        
        public void setDate(Date date) { mDate = date; }
        public void setCost(double cost) { mCost = cost; }
        public void setRevenue(double revenue) { mRevenue = revenue; }
        public void setDuration(int duration) { mDuration = duration; }
        public void setComment(String comment) { mComment = comment; }
        
        private Date mDate;
        private double mCost;
        private double mRevenue;
        private int mDuration;
        private String mComment;
    }
    
    private int mCropNumber;
    private String mCropName;
    private boolean mWinter;
    private boolean mDirty;
    
    private ArrayList<TableEntry> mEntryList;
    
    private Calendar cal = Calendar.getInstance();
}

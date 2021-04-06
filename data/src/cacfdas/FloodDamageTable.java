/*
 * FloodDamageTable.java
 *
 * Created on March 13, 2006, 10:49 AM
 */

package cacfdas;

import java.util.*;
import java.io.*;
import java.text.*;

/**
 *
 * @author b4edhdwj
 */
public class FloodDamageTable {
    
    /** Creates a new instance of FloodDamageTable */
    public FloodDamageTable() 
    {
        cropDamageList = new ArrayList<CropDamageTable>();
        
        mDirty = false;
        
        source = null;
    }
    
    /** Add a new crop damage table to the set */
    
    public void addEntry()
    {
        cropDamageList.add(new CropDamageTable());
    }
    
    /** Read one input string and add the contained information to the tables */
    
    public void addLine(String s) throws java.text.ParseException
    {
        String[] part = s.split("\t");
        
        int num = Integer.parseInt(part[0].trim());
        
        CropDamageTable entry = getEntryForCropNumber(num);
        
        if ( part.length >= 3 && entry == null )
        {
           /* Format for the first line is cropnumber <tab> winterflag <tab> crop name */

           String cropname = part[2].trim();
           boolean winter = (part[1].compareToIgnoreCase("w") == 0 ) ? true : false;            

           cropDamageList.add(new CropDamageTable(num,cropname,winter));
        }
        else
        {
            /* format for other lines is cropnumber <tab> date <tab> cost 
             <tab> revenue <tab> critical duration <tab> comments */ 
            
            if ( part.length >= 5 )
            {
                DateFormat df = DateFormat.getDateInstance();

                Date d;
                try
                {
                    d = df.parse(part[1].trim());
                }
                catch ( java.text.ParseException ex)
                {
                    int day = Integer.parseInt(part[1].trim());
                    
                    Calendar cal = java.util.Calendar.getInstance();
                    
                    cal.set(Calendar.DAY_OF_YEAR,day);
                    d = cal.getTime();
                }

                double cost = Double.parseDouble(part[2].trim());
                double rev = Double.parseDouble(part[3].trim());

                int dur = Integer.parseInt(part[4].trim());
                String comment = ( part.length < 6) ? part[5] : "";

                CropDamageTable.TableEntry rec = entry.getRecordByDate(d);
                
                if ( rec == null )
                {
                    entry.addRecord(d, cost, rev, dur, comment);
                    entry.sortRecords();
                }
                else
                {
                    rec.setCost(cost);
                    rec.setDuration(dur);
                    rec.setRevenue(rev);
                    rec.setComment(comment);
                }
            }
        }
        
        lastLineNum = num;
    }
    
    /** Clear all tables */
    
    public void clear()
    {
        cropDamageList.clear();
        mDirty = true;
        source = null;
    }
    
    /** Outout the current table to a file */
    
    public void exportFile(File file) throws IOException
    {
        String name = file.getName();
        String ext = name.substring(name.lastIndexOf('.') + 1);
        
        if ( ext.equals("txt") || ext.equals("tect") )
        {
            exportText(file);
        }
        else
        {
            exportXML(file);
        }
    }
    
    /** Output the current tables to a text file */
    
    public void exportText(File file) throws IOException
    {   
        char marker = '4';
        String cropNumString;
        String dateString;
        String winterString;
        String nameString;
        String costString;
        String revenueString;
        String durationString;
            
        FileWriter writer = new FileWriter(file);

        Calendar cal = Calendar.getInstance();
        
        NumberFormat df1 = NumberFormat.getInstance();
        df1.setMinimumIntegerDigits(6);
        df1.setMaximumIntegerDigits(6);
        df1.setMinimumFractionDigits(2);
        df1.setMaximumFractionDigits(2);
        df1.setGroupingUsed(false);
        
        NumberFormat df2 = NumberFormat.getInstance();
        df2.setMinimumIntegerDigits(4);
        df2.setMaximumIntegerDigits(4);
        df2.setMinimumFractionDigits(4);
        df2.setMaximumFractionDigits(4);
        df2.setGroupingUsed(false);
        
        
        NumberFormat df3 = NumberFormat.getInstance();
        df3.setMinimumIntegerDigits(2);
        df3.setMaximumIntegerDigits(2);
        df3.setMinimumFractionDigits(0);
        df3.setMaximumFractionDigits(0);
        df3.setGroupingUsed(false);
        df3.setParseIntegerOnly(true);
        
        NumberFormat df4 = NumberFormat.getInstance();
        df4.setMinimumIntegerDigits(3);
        df4.setMaximumIntegerDigits(3);
        df4.setMinimumFractionDigits(0);
        df4.setMaximumFractionDigits(0);
        df4.setGroupingUsed(false);
        df4.setParseIntegerOnly(true);
               
        
        for(int i = 0; i < cropDamageList.size(); ++i)
        {
            CropDamageTable cdt = cropDamageList.get(i);
            
            cropNumString = Integer.toString(cdt.getCropNumber());
            if ( cropNumString.length() == 1)
            {
                cropNumString = "0" + cropNumString;
            }
            else if (cropNumString.length() > 2)
            {
                cropNumString = cropNumString.substring(0,2);
            }
            
            dateString = "000";
            
            winterString = ( cdt.isWinterCrop() ) ? "W" : " ";
            
            nameString = cdt.getCropName();
            
            writer.write(marker);
            writer.write(" ");
            writer.write(cropNumString);
            writer.write(" ");
            writer.write(dateString);
            writer.write(" ");
            writer.write(winterString);
            writer.write(" ");
            writer.write(nameString);
            writer.write("\n");
            
            for( int j = 0; j < cdt.numRecords(); ++j )
            {
                CropDamageTable.TableEntry e = cdt.getRecord(j);
                
                Date d = e.getDate();
                cal.setTime(d);
 
                dateString = df4.format(cal.get(Calendar.DAY_OF_YEAR));
                costString = df1.format(e.getCost());
                revenueString = df2.format(e.getRevenue());
                durationString = df3.format(e.getDuration());
                
                writer.write(marker);
                writer.write(" ");
                writer.write(cropNumString);
                writer.write(" ");
                writer.write(dateString);
                writer.write(" ");
                writer.write(costString);
                writer.write(" ");
                writer.write(revenueString);
                writer.write(" ");
                writer.write(durationString);
                writer.write("\n");
                        
            }
            
            cdt.setIsDirty(false);
        }
        
        source = file;
        mDirty = false;
        
        writer.flush();
        
    }
    
    public void exportXML(File file)
    {
        
    }
    
    /** Return the current file source */
    
    public File getSource()
    {
        return source;
    }
    
    /** Attempt to load data from the input file */
    
    public void load(File file) throws java.io.FileNotFoundException, java.io.IOException
    {
        if ( file.exists() && file.canRead() )
        {
            String name = file.getName();
            String ext = name.substring(name.lastIndexOf('.') + 1);
            
            if ( ext.equalsIgnoreCase("txt") || ext.equalsIgnoreCase("text") )
            {
                importText(file);
            }
            else if ( ext.equalsIgnoreCase("xml"))
            {
                importXML(file);
            }
                
        }
        else
        {
            throw new java.io.FileNotFoundException(file.getAbsolutePath());
        }
    }
    
    /** Does this table contain any winter crops */
    
    public boolean hasWinterCrops()
    {
        Iterator<CropDamageTable> iter = cropDamageList.iterator();
        
        while ( iter.hasNext() )
        {
            if ( iter.next().isWinterCrop() )
            {
                return true;
            }
        }
        
        return false;
    }
    
    /** Read the damage tables from the input text file */
    
    private void importText(File file) throws java.io.FileNotFoundException, java.io.IOException
    {
        String line;
        
        int lastnum = -1;   // the last crop number incountered
        int num;            // the current lines crop number
        
        int cropnum;
        String cropname;
        boolean winter;
        CropDamageTable table;
        
        double cost;
        double revenue;
        int duration;
        Date date;
        
        Calendar cal = Calendar.getInstance();
        BufferedReader input = new BufferedReader( new FileReader(file) );
        
        do
        {
            line = input.readLine();
            num = Integer.parseInt( line.substring(2, 4).trim() );
            
            if ( num != lastnum )
            {
                // add a new crop damage table
                
                cropnum = num;
                cropname = line.substring(10).trim();
                winter = (line.charAt(9) == 'W') ? true : false;
                
                table = new CropDamageTable(cropnum,cropname,winter);
                cropDamageList.add(table);
                
                lastnum = num;
                
            }
            else
            {
                // add a recrod to last crop damage table
                
                cal.set(Calendar.DAY_OF_YEAR,Integer.parseInt(line.substring(5, 8).trim()));
                date = cal.getTime();
                
                cost = Double.parseDouble(line.substring(8, 18).trim());
                revenue = Double.parseDouble(line.substring(18,28).trim());
                duration = Integer.parseInt(line.substring(28,31).trim());
                
                cropDamageList.get(cropDamageList.size()-1).addRecord(date,cost,revenue,duration);

            }
            
        } while ( input.ready() );
        
        mDirty = false;
        
        for( int i = 0; i < cropDamageList.size(); ++i )
        {
            cropDamageList.get(i).setIsDirty(false);
        }
        
        source = file;
        
        
    }
    
    /** Has the main table or any of the contained subtables been changed */
    
    public boolean isDirty()
    {
        if ( mDirty )
        {
            return true;
        }

        for( int i = 0; i < cropDamageList.size(); ++i )
        {
            if ( cropDamageList.get(i).isDirty() )
            {
                return true;
            }
        }
        
        return false;
        
    }
    
    private void importXML(File file) throws java.io.FileNotFoundException
    {
        
    }
    
    /** Get the ith stored crop damage table */
    
    public CropDamageTable getEntry(int i)
    {
        return cropDamageList.get(i);
    }
    
    /** Get the crop damage table for the crop with the input crop number */
    
    public CropDamageTable getEntryForCropNumber(int cropNum)
    {
        for( int i = 0; i < cropDamageList.size(); ++i)
        {
            if ( cropDamageList.get(i).getCropNumber() == cropNum)
            {
                return cropDamageList.get(i);
            }
        }
        
        return null;
    }
    
    /** Return the source file for this flood damage table */
    
    public File getFile()
    {
        return source;
    }
    
    /** Remove the table stored at position i */
    
    public void remove(int pos)
    {
        cropDamageList.remove(pos);
        
        mDirty = true;
    }
    
    /** Remove the tables stored in positions pos1 to pos2 */
    
    public void remove(int pos1, int pos2)
    {
        int num = pos2 - pos1 + 1;
        
        for(int i = 0; i < num; ++i)
        {
            cropDamageList.remove(pos1);
        }
        
        mDirty = true;
    }
    
    /** Return the number of stored tables */
    
    public int size()
    {
        return cropDamageList.size();
    }
    
    /** The list of stored CropDamageTables */
    ArrayList<CropDamageTable> cropDamageList;
    
    /** Has the main table changed since the last save */
    boolean mDirty;
    
    /** The file representation for the the tables, this is only accurates if mBirty == false */
    File source;
    
    int lastLineNum = -1;
    
}

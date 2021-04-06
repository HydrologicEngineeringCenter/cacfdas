/*
 * CropRevenueTable.java
 *
 * Created on March 20, 2006, 4:01 PM
 */

package cacfdas.reachinfo;

import cacfdas.exception.*;

/**
 *
 * @author b4edhdwj
 */

import java.util.*;
import java.io.*;

import java.text.*;

public class CropInfoTable {
    
    /** Creates a new instance of CropRevenueTable */
    public CropInfoTable() 
    {
    }

                
    /** Parse one line of a data card file. 
     *  
     *  WARNING correct Handling of NULL string for Net Revenue is unkown.
     *  Current assumption is NULL = 0.0 " */
    
    public void addInfoString(String s) throws ReachHeaderInputFormatException, CropRevenueInputFormatException, CropPercentageInputFormatError, AltCropInputFormatException, ControlInputFormatException
    {
        String temp;
        int id;
        try
        {
            id = Integer.parseInt(s.substring(0,1));
        }
        catch( NumberFormatException e)
        {
            return;
        }
        
        switch(id)
        {
            case 1:
            
            mHeader = new ReachInfoHeader(s);
                
            break;
            
            case 2:
        
            try
            {
                int pos = 2;
                
                while ( pos <= s.length() - 5)
                {
                    int cropNum = Integer.parseInt(s.substring(pos,pos+2).trim());
                    double percent = Integer.parseInt(s.substring(pos+3,pos+5).trim()) * 0.01;
                    
                    setPercentage(cropNum, percent);
                    
                    pos += 6;
                            
                }
            }
            catch(NumberFormatException e)
            {
                throw new CropPercentageInputFormatError();
            }
            
            break;
        
            case 3:
        
            try
            {
                int cropNum = Integer.parseInt(s.substring(2,4).trim());
                
                if ( ! infoTable.containsKey(cropNum) )
                {
                    infoTable.put(cropNum, new TableData()); 
                }
                
                TableData data = infoTable.get(cropNum);
                
                temp = s.substring(5,13).trim();
                if ( temp.equals(""))
                {
                    temp = "0.0";
                }
                data.setNetRevenue(Double.parseDouble(temp));
                
                data.setGrossRevenue(Double.parseDouble(s.substring(13,21).trim()));
                data.setInfo(s.substring(21));
                
            }
            catch (NumberFormatException e)
            {
                new CropRevenueInputFormatException();
            }
            
            break;
            
            case 5:
            
            try
            {
                int num;
                int cropNum = Integer.parseInt(s.substring(2,4).trim());
                ArrayList<Integer> list = new ArrayList<Integer>();
                
                num = Integer.parseInt(s.substring(5,7).trim());
                list.add(num);
                
                if ( s.length() >= 10 )
                {
                    num = Integer.parseInt(s.substring(8,10).trim());
                    list.add(num);
                }
                
                setReplantList(cropNum,list);
            }
            catch (NumberFormatException e)
            {
                throw new AltCropInputFormatException();
            }
                
            break;
                
            case 6:
            
            try
            {
                int cropNum = Integer.parseInt(s.substring(2,4).trim());
                
                int num1 = Integer.parseInt(s.substring(5,8).trim());
                int num2;
                if ( s.length() >= 9)
                {    
                    num2 = Integer.parseInt(s.substring(9,12).trim());
                }
                else
                {
                    num2 = -1;
                }
                
                if ( ! infoTable.containsKey(cropNum) )
                {
                    infoTable.put(cropNum, new TableData()); 
                }
                
                TableData data = infoTable.get(cropNum);
                
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.DAY_OF_YEAR,num1);
                data.setLastPlantingDate(cal.getTime());
                
                if ( num2 != -1 )
                {
                    cal.set(Calendar.DAY_OF_YEAR,num2);
                    data.setMinOperationDate(cal.getTime());
                }
                else
                {
                    data.setMinOperationDate(null);
                }
                                
            }
            catch (NumberFormatException e)
            {
                throw new ControlInputFormatException();
            }
                
            break;
        }

    }
    
    public void addTabbedCropString(String s)
    {
        String[] part = s.split("\t");
        
        int cropNum;
        String cropName;
        double percent;
        double grossRev;
        double netRev;
        ArrayList<Integer> replantList;
        Date lastPlantDate;
        Date minOperationDate;
        
        try
        {
            cropNum = Integer.parseInt(part[0].trim());
            cropName = part[1].trim();
            percent = Double.parseDouble(part[2].trim());
            netRev = Double.parseDouble(part[3].trim());
            grossRev = Double.parseDouble(part[4].trim());
            
            if ( 2 < part[5].length() )
            {
                String[] replantStrings = part[5].substring(1,part[5].length()-1).split(",");
                replantList = new ArrayList<Integer>(replantStrings.length);

                for(int i = 0; i < replantStrings.length; ++i )
                {
                    replantList.add(Integer.parseInt(replantStrings[i].trim()));
                }
            }
            else
            {
                replantList = null;
            }
            
            cal.setTime(new Date());
            DateFormat df1 = DateFormat.getDateInstance(DateFormat.SHORT);
            DateFormat df2 = DateFormat.getDateInstance(DateFormat.MEDIUM);
            DateFormat df3 = DateFormat.getDateInstance(DateFormat.LONG);            
            
            
            if ( 6 < part.length )
            {
                String p[] = part[6].split("-");
                part[6] = p[1] + " " + p[0] + ", " + cal.get(Calendar.YEAR);
                part[6] = part[6].replace('/', '-');

                try
                {
                    lastPlantDate = df1.parse(part[6]);
                }
                catch (ParseException e1)
                {
                    try
                    {
                        lastPlantDate = df2.parse(part[6]);
                    }
                    catch (ParseException e2)
                    {
                        lastPlantDate = df3.parse(part[6]);
                    }
                }
            }
            else
            {
                lastPlantDate = null;
            }
            
            if ( 7 < part.length && ! part[7].trim().equals("") )
            {
                String[] p = part[7].split("-"); 
                part[7] = p[1] + " " + p[0] + ", " + cal.get(Calendar.YEAR);
                part[7] = part[7].replace('/', '-');
                
                try
                {
                    minOperationDate = df1.parse(part[7]);
                }
                catch (ParseException e1)
                {
                    try
                    {
                        minOperationDate = df2.parse(part[7]);
                    }
                    catch (ParseException e2)
                    {
                        minOperationDate = df3.parse(part[7]);
                    }
                }
            }
            else
            {
                minOperationDate = null;
            }
            
            TableData rec;
            if ( infoTable.containsKey(cropNum) )
            {
                rec = infoTable.get(cropNum);
            }
            else
            {
                rec = new TableData();
                infoTable.put(cropNum, rec);
            }
            
            rec.setInfo(cropName);
            rec.setPercentage(percent);
            rec.setGrossRevenue(grossRev);
            rec.setNetRevenue(netRev);
            rec.setLastPlantingDate(lastPlantDate);
            rec.setMinOperationDate(minOperationDate);
            rec.setReplantList(replantList);
        }
        catch (ParseException e)
        {
            
        }
    }
    
    public void addTabbedInfoString(String s) throws ReachHeaderInputFormatException, CropRevenueInputFormatException, 
            CropPercentageInputFormatError, AltCropInputFormatException, ControlInputFormatException
    {
        String part[] = s.split("\t");
        
        int id = Integer.parseInt(part[0].trim());
        int num;
        double percent;
        String temp;
        
        switch (id)
        {
            case 1:
                mHeader = new ReachInfoHeader(s); 
            break;
            
            case 2:  
                for( int i = 1; i < part.length -1; i += 2)
                {
                    num = Integer.parseInt(part[i].trim());
                    percent = Double.parseDouble(part[i+1].trim());
                    
                    this.setPercentage(num, percent);
                }            
            break;
            
            case 3:
                try
                {
                    num = Integer.parseInt(part[1].trim());

                    if ( ! infoTable.containsKey(num) )
                    {
                        infoTable.put(num, new TableData()); 
                    }

                    TableData data = infoTable.get(num);

                    temp = part[2].trim();
                    if ( temp.equals(""))
                    {
                        temp = "0.0";
                    }
                    
                    data.setNetRevenue(Double.parseDouble(temp));
                    data.setGrossRevenue(Double.parseDouble(part[3].trim()));
                    data.setInfo(part[4]);

                }
                catch (NumberFormatException e)
                {
                    new CropRevenueInputFormatException();
                }
                
            break;
            
            case 5:
                try
                {
                    num = Integer.parseInt(part[1].trim());
                    ArrayList<Integer> list = new ArrayList<Integer>();

                    list.add(Integer.parseInt(part[2].trim()));

                    if ( 3 < part.length )
                    {
                        list.add(Integer.parseInt(part[3].trim()));
                    }

                    setReplantList(num,list);
                }
                catch (NumberFormatException e)
                {
                    throw new AltCropInputFormatException();
                }                
            break;
            
            case 6:
                try
                {
                    num = Integer.parseInt(part[1].trim());

                    int val1 = Integer.parseInt(part[2].trim());
                    int val2;
                    
                    if ( 3 < part.length )
                    {
                        if ( part[3].trim().equals("") )
                        {
                            val2 = -1;
                        }
                        else
                        {
                            val2 = Integer.parseInt(part[3].trim());
                        }
                    }
                    else
                    {
                        val2 = -1;
                    }

                    if ( ! infoTable.containsKey(num) )
                    {
                        infoTable.put(num, new TableData()); 
                    }

                    TableData data = infoTable.get(num);

                    Calendar cal = Calendar.getInstance();
                    cal.set(Calendar.DAY_OF_YEAR,val1);
                    data.setLastPlantingDate(cal.getTime());

                    if ( val2 != -1 )
                    {
                        cal.set(Calendar.DAY_OF_YEAR,val2);
                        data.setMinOperationDate(cal.getTime());
                    }
                    else
                    {
                        data.setMinOperationDate(null);
                    }

                }
                catch (NumberFormatException e)
                {
                    throw new ControlInputFormatException();
                }                
            break;
        }
    }
    
    /** Reset the table to its inital state */
    
    public void clear()
    {
        mHeader.clear();
        infoTable.clear();
        keyList = null;
        mSrcFile = null;
    }
    
    public File getFile()
    {
        return mSrcFile;
    }
    
    public Integer getCrop(int index)
    {
        if ( keyList == null || keyList.length != infoTable.size() )
        {
            makeCropList();
        }
        
        return keyList[index];
    }
    
    public Integer[] getCropList()
    {
        if ( keyList == null || keyList.length != infoTable.size() )
        {
            makeCropList();
        }
        
        return keyList;
    }
    
    public void makeCropList()
    {
        keyList = new Integer[infoTable.size()];
        infoTable.keySet().toArray(keyList);
        java.util.Arrays.sort(keyList);  
    }
    
    public TableData getData(int cropNum)
    {
        return infoTable.get(cropNum);
    }

    public double getGrossRevenue(int cropNum)
    {
        return infoTable.get(cropNum).getGrossRevenue();
    }
    
    public ReachInfoHeader getHeader() 
    { 
        return mHeader; 
    }
    
    public String getInfo(int cropNum)
    {
        return infoTable.get(cropNum).getInfo();
    }
    
    public Date getLastPlantingDate(int cropNum)
    {
        return infoTable.get(cropNum).getLastPlantingDate();
    }
    
    HashMap<Integer,TableData> getMap()
    {
        return infoTable;
    }
    
    public Date getMinOperationDate(int cropNum)
    {
        return infoTable.get(cropNum).getMinOperationDate();
    }
    
    public double getNetRevenue(int cropNum)
    {
        return infoTable.get(cropNum).getNetRevenue();
    }
    
    public double getPercentage(int cropNum)
    {
        return infoTable.get(cropNum).getPercentage();
    }
    
    public ArrayList<Integer> getReplantList(int cropNum)      
    { 
        return infoTable.get(cropNum).getReplantList(); 
    }
    
    public void load(File file) throws FileNotFoundException, IOException, ReachHeaderInputFormatException, CropRevenueInputFormatException, CropPercentageInputFormatError, AltCropInputFormatException, ControlInputFormatException
    {  
        String line;
        

        BufferedReader input = new BufferedReader( new FileReader(file) );
            
        do
        {
            line = input.readLine();

            addInfoString(line);

        } while( input.ready());
        
        mSrcFile = file;

    }
    
    public void setCrop(int index, int val)
    {
        Integer key = getCrop(index);
        TableData data = infoTable.get(key);
        infoTable.remove(key);
        
        key = new Integer(val);
        infoTable.put(key, data);
        
        keyList[index] = key;
    }
    
    public void setData(int cropNum, TableData d) {
        infoTable.put(cropNum,d);
    }

    public void setHeader(ReachInfoHeader h) 
    { 
        mHeader = h; 
    }    
    
    public void setInfo(int cropNum, String s)
    {
        if ( infoTable.containsKey(cropNum))
        {
            infoTable.get(cropNum).setInfo(s);
        }
        else
        {
            TableData data = new TableData();
            data.setInfo(s);
            infoTable.put(cropNum,data);
        }
    }    
    
    public void setGrossRevenue(int cropNum, double val)
    {
        if ( infoTable.containsKey(cropNum))
        {
            infoTable.get(cropNum).setGrossRevenue(val);
        }
        else
        {
            TableData data = new TableData();
            data.setGrossRevenue(val);
            infoTable.put(cropNum,data);
        }
    }
   
    public void setLastPlantingDate(int cropNum, Date date)
    {
        if ( infoTable.containsKey(cropNum))
        {
            infoTable.get(cropNum).setLastPlantingDate(date);
        }
        else
        {
            TableData data = new TableData();
            data.setLastPlantingDate(date);
            infoTable.put(cropNum,data);
        }
    }    

   
    public void setMinOperationDate(int cropNum, Date date)
    {
        if ( infoTable.containsKey(cropNum))
        {
            infoTable.get(cropNum).setMinOperationDate(date);
        }
        else
        {
            TableData data = new TableData();
            data.setMinOperationDate(date);
            infoTable.put(cropNum,data);
        }
    }    
    
    public void setNetRevenue(int cropNum, double val)
    {
        if ( infoTable.containsKey(cropNum))
        {
            infoTable.get(cropNum).setNetRevenue(val);
        }
        else
        {
            TableData data = new TableData();
            data.setNetRevenue(val);
            infoTable.put(cropNum,data);
        }
    }
    
    public void setPercentage(int cropNum, double val)
    {
        if ( infoTable.containsKey(cropNum))
        {
            infoTable.get(cropNum).setPercentage(val);
        }
        else
        {
            TableData data = new TableData();
            data.setPercentage(val);
            infoTable.put(cropNum,data);
        }        
    }
    
    public void setReplantList(int cropNum, ArrayList<Integer> list)
    {
        if ( infoTable.containsKey(cropNum))
        {
            infoTable.get(cropNum).setReplantList(list);
        }
        else
        {
            TableData data = new TableData();
            data.setReplantList(list);
            infoTable.put(cropNum,data);
        }        
    }
    
    public void save() throws IOException
    {
        writeToFile(mSrcFile);
    }
    
    public void writeToFile(File file) throws IOException
    {
        if ( file.getAbsolutePath().endsWith(".txt") )
        {
            writeToText(file);
        }
        else if ( file.getAbsolutePath().endsWith(".xml") ) 
        {
            
        }
            
    }
    
    private void writeToText(File file) throws IOException
    {
        if ( file.exists() )
        {
            file.delete();
        }
        
        NumberFormat nf1 = new DecimalFormat();
        nf1.setMinimumIntegerDigits(2);
        nf1.setMaximumIntegerDigits(2);
        nf1.setMinimumFractionDigits(0);
        nf1.setMaximumFractionDigits(0);
        
        NumberFormat nf2 = new DecimalFormat();
        nf2.setMinimumIntegerDigits(1);
        nf2.setMaximumIntegerDigits(5);
        nf2.setMinimumFractionDigits(2);
        nf2.setMaximumFractionDigits(2);
        nf2.setGroupingUsed(false);
        
        NumberFormat nf3 = new DecimalFormat();
        nf3.setMinimumIntegerDigits(3);
        nf3.setMaximumIntegerDigits(3);
        nf3.setMinimumFractionDigits(0);
        nf3.setMaximumFractionDigits(0);           
        
        FileWriter writer = new FileWriter(file);
        
        Integer[] keys = new Integer[infoTable.size()];
        infoTable.keySet().toArray(keys);
        Arrays.sort(keys);
        
        // write the reach header
        writer.write(mHeader.getFileString());
        writer.write("\n");
        
        // write the percentage data for all crops
        writer.write("2 ");
        for( int i = 0; i < keys.length; ++i )
        {
           
           writer.write(nf1.format(keys[i]));
           writer.write(" ");
           
           writer.write(nf1.format(getPercentage(keys[i]) * 100));
           
           // either start a new line record or move to the next record spot
           if ( i + 1 % 13 == 0 || i + 1 == keys.length )
           {
               writer.write("\n");
               if ( i + 1 % 13 == 0 && !(i+1 == keys.length) )
               {
                   writer.write("2 ");
               }
           }
           else
           {
               writer.write(" ");
           }
           
        }
        
        // write the revenue crads
        String spaces = new String("        ");
        String temp;
        int num;
        
        for ( int i = 0; i < keys.length; ++i )
        {
            writer.write("3 ");
            writer.write(nf1.format(keys[i]));
            writer.write(" ");
            
            temp = nf2.format(getNetRevenue(keys[i]));
            num = 8 - temp.length();
            writer.write(spaces.substring(0,num)+temp);
            
            temp = nf2.format(getGrossRevenue(keys[i]));
            num = 8 - temp.length();
            writer.write(spaces.substring(0,num)+temp);
            
            writer.write(getInfo(keys[i]));
            writer.write("\n");
            
        }
        
        // write the alternative crop cards
        for ( int i = 0; i < keys.length; ++i )
        {
            ArrayList<Integer> list = getReplantList(keys[i]);
            if ( list != null && list.size() > 0 )
            {
                writer.write("5 ");
                writer.write(nf1.format(keys[i]));
                
                writer.write(" ");
                writer.write(nf1.format(list.get(0)));
                
                if ( list.size() > 1 )
                {
                    writer.write(" ");
                    writer.write(nf1.format(list.get(1)));
                }
                
                writer.write("\n");
                
            }
        }
        
        // write the control cards
        Calendar cal = Calendar.getInstance();
        
        for( int i = 0; i < keys.length; ++i)
        {
            if ( getLastPlantingDate(keys[i]) != null )
            {
                writer.write("6 ");
                writer.write(nf1.format(keys[i]));
                writer.write(" ");

                cal.setTime(getLastPlantingDate(keys[i]));
                num = cal.get(Calendar.DAY_OF_YEAR);

                writer.write(nf3.format(num));
            }
                       
            if ( getMinOperationDate(keys[i]) != null )
            {
                writer.write(" ");
                cal.setTime(getMinOperationDate(keys[i]));
                num = cal.get(Calendar.DAY_OF_YEAR);
                writer.write(nf3.format(num));
            }
            
            if ( getLastPlantingDate(keys[i]) != null || getMinOperationDate(keys[i]) != null )
            {
                writer.write("\n");
            }
            
        }
        
        writer.close();
        writer = null;
        
        mSrcFile = file;
    }
    
    public class TableData
    {
        public TableData() {
        }
        
        public double getGrossRevenue() { return mGrossRevenue; }
        public String getInfo() { return mInfo; }
        public Date getLastPlantingDate() { return mLastPlantingDate; }
        public Date getMinOperationDate() { return mMinOperationsDate; }
        public double getNetRevenue() { return mNetRevenue; }
        public double getPercentage() { return mPercentage; }
        public ArrayList<Integer> getReplantList() { return mReplantList; }
                
        public void setGrossRevenue(double val) { mGrossRevenue = val; }
        public void setInfo(String s) { mInfo = s; }
        public void setLastPlantingDate(Date date) { mLastPlantingDate = date; }
        public void setMinOperationDate(Date date) { mMinOperationsDate = date; }        
        public void setNetRevenue(double val) { mNetRevenue = val; }
        public void setPercentage(double val) { mPercentage = val; }
        public void setReplantList(ArrayList<Integer> list) { mReplantList = list; }
        
        private double mPercentage;
        private double mNetRevenue;
        private double mGrossRevenue;
        private String mInfo;
        private ArrayList<Integer> mReplantList;
        private Date mLastPlantingDate;
        private Date mMinOperationsDate;
    }
    
    private ReachInfoHeader mHeader = new ReachInfoHeader();
    private HashMap<Integer,TableData> infoTable = new HashMap<Integer,TableData>();
    private Integer[] keyList;
    
    private File mSrcFile;
    
    private Calendar cal = Calendar.getInstance();
}

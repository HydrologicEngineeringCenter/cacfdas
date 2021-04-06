/*
 * DataBandListModel.java
 *
 * Created on April 13, 2006, 9:57 AM
 */

package cacfdas.model;

import cacfdas.reachinfo.*;

import java.util.*;

import javax.swing.*;
import javax.swing.event.*;

import java.awt.event.*;

/**
 *
 * @author b4edhdwj
 */
public class ModelResultsListModel extends AbstractListModel implements ListSelectionListener, MouseListener{
    
    /** Creates a new instance of DataBandListModel */
    public ModelResultsListModel() {
        data = new ArrayList< HashMap<Integer, ArrayList<DataBand> > >();
        int startYear = 0;
    }
    
    public ModelResultsListModel(int start, ArrayList< HashMap<Integer, ArrayList<DataBand> > > input, CropInfoTable cropInfo)
    {
        data = input;
        startYear = start;
        cropInfo = info;
    }
    
    public ArrayList< HashMap<Integer, ArrayList<DataBand> > > getData() 
    { 
        return data; 
    }
    
    public Object getElementAt(int index)
    {
        return new Integer(startYear + index);
    }
    
    public int getSize()
    {
        return data.size();
    }
    
    public void valueChanged(ListSelectionEvent e)
    {
        if ( e.getValueIsAdjusting() )
        {
            return;
        }
        else
        {
            selectedIndex = e.getFirstIndex();
        }
    }
    
    public void mouseClicked(MouseEvent evt)
    {
    }
    
    public void mouseEntered(MouseEvent evt) {}
    public void mouseExited(MouseEvent evt) {}
    public void mouseReleased(MouseEvent evt) {}
    public void mousePressed(MouseEvent evt) {}
    
    
    private ArrayList< HashMap<Integer, ArrayList<DataBand> > > data;
    private CropInfoTable info;
    private int startYear;
    
    private int selectedIndex;
}

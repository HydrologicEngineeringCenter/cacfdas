/*
 * StageDataForm.java
 *
 * Created on March 24, 2006, 9:01 AM
 */

package cacfdas.stagedata;

/**
 *
 * @author  b4edhdwj
 */

import java.util.*;
import java.util.prefs.*;
import java.io.*;
import java.awt.*;

import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;
import javax.swing.border.*;


import hec.heclib.dss.*;
import hec.heclib.util.*;
import cacfdas.util.*;

public class StageDataFrame extends javax.swing.JFrame implements TableModelListener {
    
    public StageDataFrame(StageDataTable t) {
        stageTable = t;
        
        init();
        initComponents();
        addListiners();
        addFileFilters();
    }    
    
    void init()
    {
        String tmp;
        
        // get the last open file
        tmp = prefs.get("Last Open File", "");
        if ( tmp.equals("") == false )
        {
            lastOpenFile = new File(tmp);
        }        
    }    
    
    private void initFromTable()
    {
        Vector<String> paths = new Vector<String>();
        Vector<String> dataPaths = new Vector<String>();
        Vector<String> areaPaths = new Vector<String>();
        
        if ( stageTable.getDSSFile() != null )
        {
            ts = new HecTimeSeries();
            ts.setDSSFileName(stageTable.getDSSFile().getAbsolutePath());
            ts.setPathname(stageTable.getStagePath());
            ts.searchDSSCatalog("",paths);
            
            getDataPaths(paths,dataPaths);
            getAreaPaths(paths,areaPaths);
        }
                        
        DefaultComboBoxModel dataModel = new DefaultComboBoxModel(dataPaths);
        dataModel.setSelectedItem(dataModel.getElementAt(0));
        jComboBox1.setModel(dataModel);
                          
        DefaultComboBoxModel areaModel = new DefaultComboBoxModel(areaPaths);
        areaModel.setSelectedItem(areaModel.getElementAt(0));
        jComboBox2.setModel(areaModel);
                
                
        mDateModel1.setValueAt(stageTable.getFirstDate(),0,0);
        mDateModel2.setValueAt(stageTable.getLastDate(),0,0);
        
    }

    private int loadDSS(File file) throws java.io.IOException
    {
        HecTimeSeries ts;
        
        /// open the time series and paired data structures
        ts = new HecTimeSeries();
        ts.setDSSFileName(file.getAbsolutePath());
        
        //pd = new HecPairedData();
        //pd.setDSSFileName(file.getAbsolutePath());
        
        // get a list of possible path names
        Vector<String> paths = new Vector<String>();
        ts.searchDSSCatalog("","","","","","", paths);
        
        cacfdas.model.DSSFrame frame = null;
        
        // make a dialog to choose the stage and area paths
        outer: for ( int i = 0; i < paths.size(); ++i )
        {
            // get the selected path and added it to the display list
            String selectedPath = paths.get(i);
            
            // break the selected path into segments
            String[] selectedParts = selectedPath.substring(1).split("/");
            
            ArrayList<String> legalCParts = stageTable.mDataCPartList;
            // make sure the c part is a legal part for stage data
            boolean legal = false;
            for( int j = 0; j < legalCParts.size(); ++j)
            {
                if(legalCParts.get(j).equals(selectedParts[2]))
                {
                    legal = true;
                    break;
                }
            }
            
            // if the cpart isnt legal go to the next path
            if ( legal == false)
            {
                continue;
            }           
            
           String currentPath; 
           String[] currentParts; 
           
           ++i;
           do
           {    
            //advance to the next path and get it
            currentPath = (String) paths.get(i);
            currentParts = currentPath.substring(1).split("/");
           } while ( selectedParts[0].equals(currentParts[0]) &&
                     selectedParts[1].equals(currentParts[1]) &&
                     selectedParts[2].equals(currentParts[2]) &&
                     selectedParts[4].equals(currentParts[4]) &&
                     selectedParts[5].equals(currentParts[5]) &&
                     ++i < paths.size() );
           
           --i;
           currentPath = paths.get(i);
           currentParts = currentPath.substring(1).split("/");
           
           // make the display string that hold D range
           java.util.Vector path = new java.util.Vector();
           path.add(selectedParts[0]);
           path.add(selectedParts[1]);
           path.add(selectedParts[2]);
           path.add(selectedParts[3] + " - " + currentParts[3]);
           path.add(selectedParts[4]);
           path.add(selectedParts[5]); 
           
           if ( frame == null )
           {
               ArrayList<String> areaString = stageTable.mAreaCPartList;              
               frame = new cacfdas.model.DSSFrame(this,true,ts,areaString); 
           }
           
           frame.addDSSPath(selectedParts[0],selectedParts[1],selectedParts[2],selectedParts[3],currentParts[3],selectedParts[4],selectedParts[5]);
           
        }
        
        frame.setDataSelection(0);
        frame.setVisible(true);
        
        if ( !frame.userCancel() ) 
        {
            String areaPath = frame.getAreaPath();
            String dataPath = frame.getDataPath();
        
            stageTable.loadDSS(file,dataPath,areaPath); 
            
            return 0;
        }
        else
        {
            return -1;
        }
            
     
    }    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        fc = new javax.swing.JFileChooser();
        op = new javax.swing.JOptionPane();
        jPanel12 = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        jPanel18 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jPanel19 = new javax.swing.JPanel();
        jComboBox1 = new javax.swing.JComboBox();
        jPanel20 = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        jPanel21 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jPanel22 = new javax.swing.JPanel();
        jComboBox2 = new javax.swing.JComboBox();
        jPanel23 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTable3 = new javax.swing.JTable();
        jPanel10 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jTable4 = new javax.swing.JTable();
        jPanel11 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel16 = new javax.swing.JPanel();
        jSplitPane1 = new javax.swing.JSplitPane();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        jPanel17 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jFileMenu = new javax.swing.JMenu();
        jNewItem = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JSeparator();
        jOpenItem = new javax.swing.JMenuItem();

        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.Y_AXIS));

        addWindowFocusListener(new java.awt.event.WindowFocusListener() {
            public void windowGainedFocus(java.awt.event.WindowEvent evt) {
                formWindowGainedFocus(evt);
            }
            public void windowLostFocus(java.awt.event.WindowEvent evt) {
            }
        });

        jPanel12.setLayout(new javax.swing.BoxLayout(jPanel12, javax.swing.BoxLayout.Y_AXIS));

        jPanel12.setMaximumSize(new java.awt.Dimension(32767, 45));
        jPanel12.setMinimumSize(new java.awt.Dimension(10, 45));
        jPanel12.setPreferredSize(new java.awt.Dimension(10, 45));
        jPanel13.setLayout(new javax.swing.BoxLayout(jPanel13, javax.swing.BoxLayout.X_AXIS));

        jPanel13.setMaximumSize(new java.awt.Dimension(32767, 20));
        jPanel13.setMinimumSize(new java.awt.Dimension(10, 20));
        jPanel13.setPreferredSize(new java.awt.Dimension(10, 20));
        jPanel18.setMaximumSize(new java.awt.Dimension(10, 32767));
        jPanel13.add(jPanel18);

        jLabel3.setText("Stage Path");
        jLabel3.setMaximumSize(new java.awt.Dimension(100, 14));
        jLabel3.setMinimumSize(new java.awt.Dimension(100, 14));
        jLabel3.setPreferredSize(new java.awt.Dimension(100, 14));
        jPanel13.add(jLabel3);

        jPanel19.setMaximumSize(new java.awt.Dimension(10, 32767));
        jPanel13.add(jPanel19);

        jComboBox1.setMinimumSize(new java.awt.Dimension(23, 20));
        jComboBox1.setPreferredSize(new java.awt.Dimension(27, 20));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jPanel13.add(jComboBox1);

        jPanel20.setMaximumSize(new java.awt.Dimension(10, 32767));
        jPanel13.add(jPanel20);

        jPanel12.add(jPanel13);

        jPanel15.setMaximumSize(new java.awt.Dimension(32767, 5));
        jPanel15.setMinimumSize(new java.awt.Dimension(10, 5));
        jPanel15.setPreferredSize(new java.awt.Dimension(10, 5));
        jPanel12.add(jPanel15);

        jPanel14.setLayout(new javax.swing.BoxLayout(jPanel14, javax.swing.BoxLayout.X_AXIS));

        jPanel14.setMaximumSize(new java.awt.Dimension(32767, 20));
        jPanel14.setMinimumSize(new java.awt.Dimension(10, 20));
        jPanel14.setPreferredSize(new java.awt.Dimension(10, 20));
        jPanel21.setMaximumSize(new java.awt.Dimension(10, 32767));
        jPanel14.add(jPanel21);

        jLabel4.setText("Stage Area Path");
        jLabel4.setMaximumSize(new java.awt.Dimension(100, 14));
        jLabel4.setMinimumSize(new java.awt.Dimension(100, 14));
        jLabel4.setPreferredSize(new java.awt.Dimension(100, 14));
        jPanel14.add(jLabel4);

        jPanel22.setMaximumSize(new java.awt.Dimension(10, 32767));
        jPanel14.add(jPanel22);

        jComboBox2.setPreferredSize(new java.awt.Dimension(27, 20));
        jComboBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox2ActionPerformed(evt);
            }
        });

        jPanel14.add(jComboBox2);

        jPanel23.setMaximumSize(new java.awt.Dimension(10, 32767));
        jPanel14.add(jPanel23);

        jPanel12.add(jPanel14);

        getContentPane().add(jPanel12);

        jPanel1.setMaximumSize(new java.awt.Dimension(32767, 10));
        getContentPane().add(jPanel1);

        jPanel7.setLayout(new javax.swing.BoxLayout(jPanel7, javax.swing.BoxLayout.X_AXIS));

        jPanel7.setMaximumSize(new java.awt.Dimension(32767, 20));
        jPanel7.setMinimumSize(new java.awt.Dimension(10, 20));
        jPanel7.setPreferredSize(new java.awt.Dimension(10, 20));
        jPanel7.add(jPanel8);

        jLabel1.setText("Start Date");
        jLabel1.setMaximumSize(new java.awt.Dimension(60, 14));
        jLabel1.setMinimumSize(new java.awt.Dimension(60, 14));
        jLabel1.setPreferredSize(new java.awt.Dimension(60, 14));
        jPanel7.add(jLabel1);

        jTable3.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0)));
        jTable3.setModel(mDateModel1);
        jTable3.setEditingColumn(0);
        jTable3.setEditingRow(0);
        jTable3.setIntercellSpacing(new java.awt.Dimension(0, 0));
        jTable3.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        jTable3.setMinimumSize(new java.awt.Dimension(10, 0));
        jTable3.setPreferredSize(new java.awt.Dimension(10, 10));
        jTable3.setRowHeight(20);
        jTable3.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTable3FocusLost(evt);
            }
        });

        jPanel7.add(jTable3);

        jPanel10.setMinimumSize(new java.awt.Dimension(40, 10));
        jPanel10.setPreferredSize(new java.awt.Dimension(40, 10));
        jPanel7.add(jPanel10);

        jLabel2.setText("Stop Date");
        jLabel2.setMaximumSize(new java.awt.Dimension(60, 14));
        jLabel2.setMinimumSize(new java.awt.Dimension(60, 14));
        jLabel2.setPreferredSize(new java.awt.Dimension(60, 14));
        jPanel7.add(jLabel2);

        jTable4.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0)));
        jTable4.setModel(mDateModel2);
        jTable4.setIntercellSpacing(new java.awt.Dimension(0, 0));
        jTable4.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        jTable4.setMinimumSize(new java.awt.Dimension(10, 0));
        jTable4.setPreferredSize(new java.awt.Dimension(10, 10));
        jTable4.setRowHeight(20);
        jTable4.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTable4FocusLost(evt);
            }
        });

        jPanel7.add(jTable4);

        jPanel7.add(jPanel11);

        getContentPane().add(jPanel7);

        jPanel9.setLayout(new java.awt.BorderLayout());

        jPanel4.setMaximumSize(new java.awt.Dimension(10, 32767));
        jPanel9.add(jPanel4, java.awt.BorderLayout.NORTH);

        jPanel9.add(jPanel16, java.awt.BorderLayout.EAST);

        jSplitPane1.setDividerLocation(200);
        jSplitPane1.setDividerSize(6);
        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane1.setMaximumSize(new java.awt.Dimension(32767, 32767));
        jSplitPane1.setPreferredSize(new java.awt.Dimension(0, 0));
        jPanel3.setLayout(new javax.swing.BoxLayout(jPanel3, javax.swing.BoxLayout.X_AXIS));

        jTable1.setModel(stageTable);
        jTable1.setDefaultEditor(HecDouble.class,new HecDoubleEditor() );
        jTable1.setDefaultRenderer(HecDouble.class,new HecDoubleRenderer() );
        jScrollPane1.setViewportView(jTable1);

        jPanel3.add(jScrollPane1);

        jSplitPane1.setTopComponent(jPanel3);

        jPanel6.setLayout(new javax.swing.BoxLayout(jPanel6, javax.swing.BoxLayout.X_AXIS));

        jTable2.setModel(stageTable.getStageAreaTableModel());
        jScrollPane2.setViewportView(jTable2);

        jPanel6.add(jScrollPane2);

        jSplitPane1.setBottomComponent(jPanel6);

        jPanel9.add(jSplitPane1, java.awt.BorderLayout.CENTER);

        jPanel5.setMaximumSize(new java.awt.Dimension(10, 32767));
        jPanel9.add(jPanel5, java.awt.BorderLayout.SOUTH);

        jPanel9.add(jPanel17, java.awt.BorderLayout.WEST);

        getContentPane().add(jPanel9);

        jPanel2.setMaximumSize(new java.awt.Dimension(32767, 10));
        getContentPane().add(jPanel2);

        jFileMenu.setText("File");
        jNewItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        jNewItem.setMnemonic('n');
        jNewItem.setText("New");
        jNewItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jNewItemActionPerformed(evt);
            }
        });

        jFileMenu.add(jNewItem);

        jFileMenu.add(jSeparator1);

        jOpenItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        jOpenItem.setMnemonic('o');
        jOpenItem.setText("Open");
        jOpenItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jOpenItemActionPerformed(evt);
                jImportItemActionPerformed(evt);
            }
        });

        jFileMenu.add(jOpenItem);

        jMenuBar1.add(jFileMenu);

        setJMenuBar(jMenuBar1);

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-500)/2, (screenSize.height-474)/2, 500, 474);
    }//GEN-END:initComponents

    private void jImportItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jImportItemActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jImportItemActionPerformed

    private void jNewItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jNewItemActionPerformed
        stageTable.clear();
        
        jTable1.setModel(stageTable);
        jTable2.setModel(stageTable.getStageAreaTableModel());
        
        initFromTable();
    }//GEN-LAST:event_jNewItemActionPerformed

    private void formWindowGainedFocus(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowGainedFocus
        initFromTable();
    }//GEN-LAST:event_formWindowGainedFocus

    private void jComboBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox2ActionPerformed
        String selection = (String) jComboBox2.getSelectedItem();
        
        if ( selection.compareToIgnoreCase(stageTable.getStageAreaPath()) != 0 )
        {
            stageTable.setStageAreaPath(selection);
        }
    }//GEN-LAST:event_jComboBox2ActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        String selection = (String) jComboBox1.getSelectedItem();
        
        if ( selection.compareToIgnoreCase(stageTable.getStagePath()) != 0 )
        {
            stageTable.setStagePath(selection);
        }
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jTable4FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTable4FocusLost
        jTable4.clearSelection();
    }//GEN-LAST:event_jTable4FocusLost

    private void jTable3FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTable3FocusLost
        jTable3.clearSelection();     
    }//GEN-LAST:event_jTable3FocusLost

    private void jOpenItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jOpenItemActionPerformed
        // setup the file chooser
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fc.setAcceptAllFileFilterUsed(false);   // do not display all files
        fc.setCurrentDirectory(lastOpenFile);
        fc.setFileFilter(FileFilters.dss);
            
        int rval = fc.showOpenDialog(this);
        
        if (rval == fc.CANCEL_OPTION || rval == fc.ERROR_OPTION)
        {
            return;
        }
        else
        {
            File selection = fc.getSelectedFile();
            
            try
            {
                if ( selection.getName().toLowerCase().endsWith(".dss"))
                {
                    this.loadDSS(selection);
                }
                else
                {
                    stageTable.load(selection);
                    ts = stageTable.getTimeSeries();

                    Vector<String> paths = new Vector<String>();
                    Vector<String> dataPaths = new Vector<String>();
                    ts.searchDSSCatalog("",paths);

                    getDataPaths(paths,dataPaths);

                    DefaultComboBoxModel dataModel = new DefaultComboBoxModel(dataPaths);
                    dataModel.setSelectedItem(dataModel.getElementAt(0));
                    jComboBox1.setModel(dataModel);

                    Vector<String> areaPaths = new Vector<String>();
                    getAreaPaths(paths,areaPaths);

                    DefaultComboBoxModel areaModel = new DefaultComboBoxModel(areaPaths);
                    areaModel.setSelectedItem(areaModel.getElementAt(0));
                    jComboBox2.setModel(areaModel);


                    mDateModel1.setValueAt(stageTable.getFirstDate(),0,0);
                    mDateModel2.setValueAt(stageTable.getLastDate(),0,0);
                }
            }
            catch(IOException e)
            {
                op.showMessageDialog(this,e.getMessage(),"IOError",op.ERROR_MESSAGE);
            }
            
            lastOpenFile = selection;
            prefs.put("Last Open File", selection.getPath());
        }
    }//GEN-LAST:event_jOpenItemActionPerformed

    private void addListiners()
    {
        jTable3.getModel().addTableModelListener(this);
        jTable4.getModel().addTableModelListener(this);
    }
    
    private void addFileFilters()
    {         
        
        fc.addChoosableFileFilter(FileFilters.universal);       
        fc.addChoosableFileFilter(FileFilters.dss);
        fc.addChoosableFileFilter(FileFilters.text);          
    }        
    
    public void tableChanged(TableModelEvent e) 
    {
        Date d1 = mDateModel1.getDate();
        Date d2 = mDateModel2.getDate();
        
        if ( d1 != null && d2 != null )
        {
            stageTable.setWindowTimeRange(new HecTime(d1,0),new HecTime(d2,0));
        }
    }
    
    public class SingleDateTableModel extends AbstractTableModel
    {
        
        public int getColumnCount()
        {
            return 1;
        }
        
        public Class<?> getColumnClass(int columnIndex) 
        {
            return DateAdaptor.class;
        }
        
        public Date getDate()
        {
            return date;
        }
        
        public int getRowCount()
        {
            return 1;
        }
        
        public Object getValueAt(int row, int col)
        {
            return new DateAdaptor(date);
        }
        
        public boolean isCellEditable(int rowIndex, int columnIndex)
        {
            return true;
        }
        
        public void setValueAt(Object aValue, int rowIndex, int columnIndex)
        {
            if ( aValue == null )
            {
                date = null;
            }
            else
            {
                if ( aValue.getClass() == DateAdaptor.class)
                {
                    DateAdaptor adaptor = (DateAdaptor) aValue;
                    date = adaptor.getDate();
                }
                else if ( aValue.getClass() == HecTime.class )
                {
                    HecTime time = (HecTime) aValue;
                    if ( time.isDefined() )
                    {
                        date = time.getJavaDate(0);
                    }
                    else
                    {
                        date = null;
                    }
                }
            }
            
            fireTableCellUpdated(rowIndex, columnIndex);
        }
        
        private Date date;
    }
    
    
    private void getAreaPaths(Vector<String> inputPaths, Vector<String> areaPaths)
    {
        String[] areaDataStr = {"STAGE-AREA"};
        
        String[] stageParts = stageTable.getStageAreaPath().split("/");
        
        for( int i = 0; i < inputPaths.size(); ++i )
        {
            String[] parts = inputPaths.elementAt(i).split("/");
            
            if ( stageParts[1].equals(parts[1]) && 
                 stageParts[2].equals(parts[2])  )
            {
                boolean legal = false;
                for(int j = 0; j < areaDataStr.length; ++j )
                {
                    if ( areaDataStr[j].equals(parts[3]) )
                    {
                        legal = true;
                    }
                }
                
                if (!legal)
                {
                    continue;
                }
                
                areaPaths.add(inputPaths.elementAt(i));
            }
            
            
        }
    }
    
    private void getDataPaths(Vector<String> inputPaths, Vector<String> outputPaths)
    {
        String[] stageDataStr = {"STAGE"};
        
        outer: for ( int i = 0; i < inputPaths.size(); ++i )
        {
            // get the selected path and added it to the display list
            String selectedPath = inputPaths.elementAt(i);
            
            // break the selected path into segments
            String[] selectedParts = selectedPath.substring(1).split("/");
            
            // make sure the c part is a legal part for stage data
            boolean legal = false;
            for( int j = 0; j < stageDataStr.length; ++j)
            {
                if(stageDataStr[j].equals(selectedParts[2]))
                {
                    legal = true;
                    break;
                }
            }
            
            // if the cpart isnt legal go to the next path
            if ( legal == false)
            {
                continue;
            }           
            
           String currentPath; 
           String[] currentParts; 
           
           ++i;
           do
           {    
            //advance to the next path and get it
            currentPath = (String) inputPaths.elementAt(i);
            currentParts = currentPath.substring(1).split("/");
           } while ( selectedParts[0].equals(currentParts[0]) &&
                     selectedParts[1].equals(currentParts[1]) &&
                     selectedParts[2].equals(currentParts[2]) &&
                     selectedParts[4].equals(currentParts[4]) &&
                     selectedParts[5].equals(currentParts[5]) &&
                     ++i < inputPaths.size() );
           
           --i;
           currentPath = inputPaths.elementAt(i);
           currentParts = currentPath.substring(1).split("/");
           
           // make the display string that hold D range
           StringBuffer buffer = new StringBuffer(); 
           buffer.append("/");
           buffer.append(selectedParts[0]);
           buffer.append("/");
           buffer.append(selectedParts[1]);
           buffer.append("/");
           buffer.append(selectedParts[2]);
           buffer.append("/");
           buffer.append(selectedParts[3] + " - " + currentParts[3]);
           buffer.append("/");
           buffer.append(selectedParts[4]);
           buffer.append("/");
           buffer.append(selectedParts[5]);
           buffer.append("/");
           
           outputPaths.add(buffer.toString());
           
        }        
    }
    
    class HecDoubleEditor extends DefaultCellEditor
    {
        public HecDoubleEditor()
        {
            super(new JTextField());
            editField = (JTextField) this.getComponent();
            editField.setHorizontalAlignment(JTextField.RIGHT);
            editField.setBorder(blackline);
        }
        
        public Object getCellEditorValue()
        {
            return mVal;
        }
        
        public boolean stopCellEditing()
        {
            try
            {
                mVal.set(Double.parseDouble(editField.getText()));
                if (swapborder)
                {
                    editField.setBorder(blackline); 
                    swapborder = false;
                }                
                fireEditingStopped();
                return true;
            }
            catch ( NumberFormatException e)
            {
                if ( editField.getText().trim().equals("") )
                {
                    mVal.set(editField.getText());
                    if (swapborder)
                    {
                        editField.setBorder(blackline);
                        swapborder = false;
                    }
                    fireEditingStopped();
                    return true;
                }
                else
                {
                    editField.setBorder(redline);
                    swapborder = true;
                    return false;
                }
            }
            
        }
        
        
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column)
        { 
            mTable = table;
            
            mVal = new HecDouble((HecDouble) value);
            editField.setText(mVal.string(2,false));
            
            editField.select(0,editField.getText().length());
 
            if (swapborder)
            {
                   editField.setBorder(blackline);
                   swapborder = false;
            }            
            
            return editField;
            
        }
   
        Border redline = BorderFactory.createLineBorder(Color.red);
        Border blackline = BorderFactory.createLineBorder(Color.black);
        
        HecDouble mVal = new HecDouble();
        
        JTextField editField = new JTextField();
        JTable mTable;
        
        boolean swapborder = false;
    }

    class HecDoubleRenderer extends DefaultTableCellRenderer
    {
        HecDoubleRenderer()
        {
        }
        
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
        {
            JLabel cell = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            
            this.setHorizontalAlignment(HecDoubleRenderer.RIGHT);
            
            return cell;
        }
        
        public void setValue(Object value) 
        {           
            setText(value.toString());
        }
    }
 
    
    SingleDateTableModel mDateModel1 = new SingleDateTableModel();
    SingleDateTableModel mDateModel2 = new SingleDateTableModel();
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JFileChooser fc;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JMenu jFileMenu;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jNewItem;
    private javax.swing.JMenuItem jOpenItem;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    private javax.swing.JTable jTable4;
    private javax.swing.JOptionPane op;
    // End of variables declaration//GEN-END:variables
    
    StageDataTable stageTable;
    HecTimeSeries ts;
    
    Preferences prefs = Preferences.userNodeForPackage(StageDataFrame.class);
    File lastOpenFile = null;
}

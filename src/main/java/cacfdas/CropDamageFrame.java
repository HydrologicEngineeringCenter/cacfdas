package cacfdas;

import java.util.*;
import java.util.prefs.*;
import java.text.*;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.datatransfer.*;
import java.io.*;

import cacfdas.util.DayMonth;

/*
 * CropDamageFrame.java
 *
 * Created on March 14, 2006, 9:45 AM
 */

/**
 *
 * @author  b4edhdwj
 */



public class CropDamageFrame extends javax.swing.JFrame {
    
    /** Creates new form CropDamageFrame */
    public CropDamageFrame() {
        init();
        initComponents();
        
        addListeners();
    }
    
    public CropDamageFrame(CropDamageTable table, FloodDamageFrame parent) {
        init(table,parent);
        initComponents();
        
        setComponents();
        
        addListeners();
    }
    
    private void init()
    {
        mTable = null;
        mParent = null;
        
        dateFormat.setLenient(true);
    }
    
    private void init(CropDamageTable table, FloodDamageFrame parent)
    {
        mTable = table;
        mParent = parent;
        
        dateFormat.setLenient(true);
    }
    
    private void setComponents()
    {
        cropNameField.setText(mTable.getCropName());
        cropNumberField.setText(Integer.toString(mTable.getCropNumber()));
        this.winterCropCBox.setSelected(mTable.isWinterCrop());
        
    }
    
    private void addListeners()
    {
        PasteAdaptor paste = new PasteAdaptor(jTable1);
        
        jEditMenu.add(paste);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        jPanel8 = new javax.swing.JPanel();
        jCropNamePanel = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        cropNameField = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jCropNumberPanel = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        cropNumberField = new javax.swing.JTextField();
        jPanel7 = new javax.swing.JPanel();
        winterCropCBox = new javax.swing.JCheckBox();
        jPanel6 = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        jTablePanel = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jTable1.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        jPanel10 = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        jMenuBar1 = new javax.swing.JMenuBar();
        mFileMenu = new javax.swing.JMenu();
        jNewRecordItem = new javax.swing.JMenuItem();
        jRemoveRecordItem = new javax.swing.JMenuItem();
        jEditMenu = new javax.swing.JMenu();

        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.Y_AXIS));

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jPanel8.setMaximumSize(new java.awt.Dimension(32767, 10));
        getContentPane().add(jPanel8);

        jCropNamePanel.setLayout(new javax.swing.BoxLayout(jCropNamePanel, javax.swing.BoxLayout.X_AXIS));

        jCropNamePanel.setMaximumSize(new java.awt.Dimension(2147483647, 19));
        jCropNamePanel.setPreferredSize(new java.awt.Dimension(64, 19));
        jPanel2.setMaximumSize(new java.awt.Dimension(10, 32767));
        jCropNamePanel.add(jPanel2);

        jLabel1.setText("Crop Name");
        jCropNamePanel.add(jLabel1);

        jPanel3.setMaximumSize(new java.awt.Dimension(20, 32767));
        jPanel3.setMinimumSize(new java.awt.Dimension(20, 10));
        jPanel3.setPreferredSize(new java.awt.Dimension(20, 10));
        jCropNamePanel.add(jPanel3);

        cropNameField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cropNameFieldActionPerformed(evt);
            }
        });
        cropNameField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                cropNameFieldFocusLost(evt);
            }
        });

        jCropNamePanel.add(cropNameField);

        jPanel4.setMaximumSize(new java.awt.Dimension(10, 32767));
        jCropNamePanel.add(jPanel4);

        getContentPane().add(jCropNamePanel);

        jPanel11.setMaximumSize(new java.awt.Dimension(32767, 5));
        jPanel11.setMinimumSize(new java.awt.Dimension(10, 5));
        jPanel11.setPreferredSize(new java.awt.Dimension(10, 5));
        getContentPane().add(jPanel11);

        jCropNumberPanel.setLayout(new javax.swing.BoxLayout(jCropNumberPanel, javax.swing.BoxLayout.X_AXIS));

        jCropNumberPanel.setMaximumSize(new java.awt.Dimension(2147483647, 19));
        jCropNumberPanel.setMinimumSize(new java.awt.Dimension(63, 19));
        jCropNumberPanel.setPreferredSize(new java.awt.Dimension(63, 19));
        jPanel1.setMaximumSize(new java.awt.Dimension(10, 32767));
        jCropNumberPanel.add(jPanel1);

        jLabel2.setText("Crop Number");
        jCropNumberPanel.add(jLabel2);

        jCropNumberPanel.add(jPanel5);

        cropNumberField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        cropNumberField.setMaximumSize(new java.awt.Dimension(32767, 32767));
        cropNumberField.setMinimumSize(new java.awt.Dimension(60, 19));
        cropNumberField.setPreferredSize(new java.awt.Dimension(60, 19));
        cropNumberField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cropNumberFieldActionPerformed(evt);
            }
        });
        cropNumberField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                cropNumberFieldFocusLost(evt);
            }
        });

        jCropNumberPanel.add(cropNumberField);

        jPanel7.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        jCropNumberPanel.add(jPanel7);

        winterCropCBox.setText("Winter Crop");
        winterCropCBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                winterCropCBoxActionPerformed(evt);
            }
        });

        jCropNumberPanel.add(winterCropCBox);

        jCropNumberPanel.add(jPanel6);

        getContentPane().add(jCropNumberPanel);

        jPanel12.setMaximumSize(new java.awt.Dimension(32767, 10));
        getContentPane().add(jPanel12);

        jTablePanel.setLayout(new javax.swing.BoxLayout(jTablePanel, javax.swing.BoxLayout.X_AXIS));

        jPanel9.setMaximumSize(new java.awt.Dimension(10, 32767));
        jTablePanel.add(jPanel9);

        jTable1.setModel(new CropDamageTableModel(mTable));
        jTable1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTable1KeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTable1KeyTyped(evt);
            }
        });

        jScrollPane1.setViewportView(jTable1);

        jTablePanel.add(jScrollPane1);

        jPanel10.setMaximumSize(new java.awt.Dimension(10, 32767));
        jTablePanel.add(jPanel10);

        getContentPane().add(jTablePanel);

        jPanel13.setMaximumSize(new java.awt.Dimension(32767, 10));
        getContentPane().add(jPanel13);

        mFileMenu.setMnemonic('T');
        mFileMenu.setText("Table");
        mFileMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mFileMenuActionPerformed(evt);
            }
        });

        jNewRecordItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        jNewRecordItem.setMnemonic('n');
        jNewRecordItem.setText("New Record");
        jNewRecordItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jNewRecordItemActionPerformed(evt);
            }
        });

        mFileMenu.add(jNewRecordItem);

        jRemoveRecordItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.CTRL_MASK));
        jRemoveRecordItem.setMnemonic('R');
        jRemoveRecordItem.setText("Remove Record");
        jRemoveRecordItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRemoveRecordItemActionPerformed(evt);
            }
        });

        mFileMenu.add(jRemoveRecordItem);

        jMenuBar1.add(mFileMenu);

        jEditMenu.setMnemonic('e');
        jEditMenu.setText("Edit");
        jMenuBar1.add(jEditMenu);

        setJMenuBar(jMenuBar1);

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-476)/2, (screenSize.height-384)/2, 476, 384);
    }//GEN-END:initComponents

    private void mFileMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mFileMenuActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_mFileMenuActionPerformed

    private void jRemoveRecordItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRemoveRecordItemActionPerformed
        removeSelectedRecords();
    }//GEN-LAST:event_jRemoveRecordItemActionPerformed

    private void jTable1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTable1KeyPressed
        int num = evt.getKeyCode();
        
        if ( num == evt.VK_DELETE )
        {
            removeSelectedRecords();
            
            evt.consume();
        }
    }//GEN-LAST:event_jTable1KeyPressed

    private void jTable1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTable1KeyTyped

    }//GEN-LAST:event_jTable1KeyTyped

    private void jNewRecordItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jNewRecordItemActionPerformed

        Calendar cal = Calendar.getInstance();
        if ( mTable.numRecords() > 0 )
        {
            cal.setTime(mTable.getLastRecord().getDate());
            cal.add(Calendar.DAY_OF_MONTH, 1);
        }
        else
        {
            cal.setTime(new Date());
            cal.set(Calendar.DAY_OF_MONTH,1);
            cal.set(Calendar.MONTH,0);
        }
        
        CropDamageTableModel cdtm = (CropDamageTableModel) jTable1.getModel();
        cdtm.addRecord(cal.getTime(), 0.0, 0.0, 0);

    }//GEN-LAST:event_jNewRecordItemActionPerformed

    private void winterCropCBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_winterCropCBoxActionPerformed
        mTable.setIsWinterCrop(winterCropCBox.isSelected());
    }//GEN-LAST:event_winterCropCBoxActionPerformed

    private void cropNumberFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cropNumberFieldFocusLost
        int num = Integer.parseInt(cropNumberField.getText());
        if ( num != mTable.getCropNumber() )
        {
            mTable.setCropNumber(num);
        }
    }//GEN-LAST:event_cropNumberFieldFocusLost

    private void cropNameFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cropNameFieldFocusLost
        if ( ! cropNameField.getText().equals(mTable.getCropName()))
        {
            mTable.setCropName(cropNameField.getText());
        }
    }//GEN-LAST:event_cropNameFieldFocusLost

    private void cropNameFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cropNameFieldActionPerformed
        if ( ! cropNameField.getText().equals(mTable.getCropName()))
        {
            mTable.setCropName(cropNameField.getText());
        }
    }//GEN-LAST:event_cropNameFieldActionPerformed

    private void cropNumberFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cropNumberFieldActionPerformed
        int num = Integer.parseInt(cropNumberField.getText());
        if ( num != mTable.getCropNumber() )
        {
            mTable.setCropNumber(num);
        }
    }//GEN-LAST:event_cropNumberFieldActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        if ( mParent != null )
        {
            mParent.notifyClose(mTable);
        }
    }//GEN-LAST:event_formWindowClosing
    
    private void removeSelectedRecords()
    {
        int pos1 = jTable1.getSelectionModel().getMinSelectionIndex();
        int pos2 = jTable1.getSelectionModel().getMaxSelectionIndex();
        int num = pos2 - pos1 + 1;
            
        CropDamageTableModel cdtm = (CropDamageTableModel) jTable1.getModel();
        
        cdtm.removeRecords(pos1,pos2);
        jTable1.removeEditor(); 
        
        pos1 -= num;
        pos2 -= num;
        
        if ( pos1 < 0 )
        {
            pos1 = 0;
        }
        
        if ( pos2 < 0 )
        {
            pos2 = 0;
        }
            
        if ( mTable.numRecords() > 0 )
        {
            jTable1.getSelectionModel().setSelectionInterval(pos1,pos2);
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new CropDamageFrame().setVisible(true);
            }
        });
    }
    
    public class CropDamageTableModel extends AbstractTableModel
    {
        CropDamageTableModel()
        {
            super();
        }
        
        CropDamageTableModel(CropDamageTable table)
        {
            super();
            
            mTable = table;
        }
        
        public void addRecord(Date date, double cost, double revenue, int duration)
        {
            mTable.addRecord(date, cost, revenue, duration);
            int val = mTable.numRecords()-1;
            fireTableRowsInserted(val,val);            
        }
        
        public boolean isCellEditable(int rowIndex, int columnIndex)
        {
            return true;
        }
        
        public Object getValueAt(int i, int j)
        {
            if ( i < mTable.numRecords() )
            {
                switch(j)
                {
                    case 0:
                        /*Date d = mTable.getRecord(i).getDate();
                        StringBuffer buffer1 = new StringBuffer();
                        FieldPosition pos1 = new FieldPosition(DateFormat.MONTH_FIELD);
                        dateFormat.format(d,buffer1,pos1);
 
                        StringBuffer buffer2 = new StringBuffer();
                        FieldPosition pos2 = new FieldPosition(DateFormat.DATE_FIELD);
                        dateFormat.format(d,buffer2,pos2);
                        
                        StringBuffer buffer3 = new StringBuffer();
                        buffer3.append(buffer1.substring(pos1.getBeginIndex(),pos1.getEndIndex()));
                        buffer3.append(" ");
                        buffer3.append(buffer2.substring(pos2.getBeginIndex(),pos2.getEndIndex()));
                        
                        return buffer3.toString();*/
                        
                        return new DayMonth(mTable.getRecord(i).getDate());
                    
                    case 1:
                        return mTable.getRecord(i).getCost();
                        
                    case 2:
                        return mTable.getRecord(i).getRevenue();
                    
                    case 3:
                        return mTable.getRecord(i).getDuration();
                        
                    default:
                        return "";
                }
            }
            else
            {
                return "";
            }
        }
        
        public int getColumnCount()
        {
            return 4;
        }
        
        public int getRowCount()
        {
            if ( mTable == null )
            {
                return 0;
            }
            else
            {
                return mTable.numRecords();
            }
        }
        
        public String getColumnName(int coulumn)
        {
            switch(coulumn)
            {
                case 0:
                return "Date";    
                
                case 1:
                return "Cost";
                
                case 2:
                return "Revenue";
                
                case 3:
                return "Crit Val";
                
                default:
                return "";
            }
        }
 
        public Class getColumnClass(int columnIndex) 
        {
            switch(columnIndex)
            {
                case 0:
                    return DayMonth.class;

                case 1:
                case 2:
                    return Double.class;

                case 3:
                    return Integer.class;

                default:
                    return String.class;
            }
        }
        
        public void removeRecords(int pos1, int pos2)
        {
            mTable.removeRecords(pos1,pos2);
            fireTableRowsDeleted(pos1,pos2);           
        }
        
        public void setValueAt(Object aValue, int rowIndex, int columnIndex)
        {
            double val;
            int ival;
            
            switch( columnIndex )
            {
                case 0:
                    DayMonth dm1 = (DayMonth) aValue;
                    DayMonth dm2 = new DayMonth(mTable.getRecord(rowIndex).getDate());
                    
                    if ( ! dm1.equals(dm2) )
                    {
                        Date d = dm1.toDate();
                        CropDamageTable.TableEntry e = mTable.getRecord(rowIndex);
                        
                        e.setDate(d);
                        mTable.setIsDirty(true);
                        mTable.sortRecords();
                        
                        int pos = jTable1.getSelectedRow();
                        for(int i = 0; i < mTable.numRecords(); ++i)
                        {
                            if ( mTable.getRecord(i).equals(e) )
                            {
                                pos = i;
                                break;
                            }
                        }
                        
                        jTable1.getSelectionModel().setSelectionInterval(pos,pos);
  
                        fireTableRowsUpdated(0, mTable.numRecords() -1);
                    }
                break;
                
                case 1:
                    val = (Double) aValue;
                    if ( val != mTable.getRecord(rowIndex).getCost() )
                    {
                        mTable.getRecord(rowIndex).setCost((Double) aValue );
                        mTable.setIsDirty(true);
                    }
                break;
                
                case 2:
                    val = (Double) aValue;
                    if ( val != mTable.getRecord(rowIndex).getRevenue() )
                    {
                        mTable.getRecord(rowIndex).setRevenue((Double) aValue );
                        mTable.setIsDirty(true);
                    }
                break;
                
                case 3:
                    ival = (Integer) aValue;
                    if ( ival != mTable.getRecord(rowIndex).getDuration() )
                    {
                        mTable.getRecord(rowIndex).setDuration((Integer) aValue );
                        mTable.setIsDirty(true);
                    }
                break;
                
            }
        }
        
        CropDamageTable mTable;
    }
    
    class PasteAdaptor extends AbstractAction 
    {
        public PasteAdaptor(JComponent c)
        {
            putValue(Action.NAME,"Paste");
            putValue(Action.MNEMONIC_KEY,KeyEvent.VK_A);
            
            KeyStroke paste = KeyStroke.getKeyStroke(KeyEvent.VK_V,ActionEvent.CTRL_MASK,false);
            putValue(Action.ACCELERATOR_KEY,paste);
            
            component = c;
            component.getInputMap().put(paste, "Paste");
            component.getActionMap().put("Paste",this);
            
            system = Toolkit.getDefaultToolkit().getSystemClipboard();
            
        }
        
        public void actionPerformed(ActionEvent e)
        {
            try
            {
                String clipString = (String) system.getData(DataFlavor.stringFlavor);
                
                String[] lineString = clipString.split("\n");
                
                for( int i = 0; i < lineString.length; ++i )
                {
                    String part[] = lineString[i].split("\t");
                    
                    if ( part.length >= 4 )
                    {
                        Date d;
                        
                        try
                        {
                            DateFormat df = DateFormat.getDateInstance();
                            d = df.parse(part[0].trim());
                        }
                        catch ( ParseException e3 )
                        {
                            int day = Integer.parseInt(part[0].trim());
                            Calendar cal = Calendar.getInstance();
                            
                            cal.set(Calendar.DAY_OF_YEAR,day);
                            d = cal.getTime();
                        }
                        
                        double cost = Double.parseDouble(part[1].trim());
                        double rev = Double.parseDouble(part[2].trim());
                        int crit_dur = Integer.parseInt(part[3].trim());
                        
                        String comment = ( part.length >= 5 ) ? part[4] : "";
                        
                        CropDamageTable.TableEntry entry = mTable.getRecordByDate(d); 
                        
                        if ( entry == null )
                        {
                            mTable.addRecord(d,cost,rev,crit_dur, comment);
                        }
                        else
                        {
                            entry.setCost(cost);
                            entry.setDuration(crit_dur);
                            entry.setRevenue(rev);
                            entry.setComment(comment); 
                        }
                        
                    }
                }
            }
            catch( UnsupportedFlavorException e1 )
            {
                // this exception means the clipboard did not have text no further action is nessessary
            }
            catch (Exception e2)
            {
                e2.printStackTrace();
            }
            
            mTable.sortRecords();
            
            CropDamageTableModel model = (CropDamageTableModel) jTable1.getModel();
            model.fireTableDataChanged();
        }
        
        private JComponent component;
        private Clipboard system;
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField cropNameField;
    private javax.swing.JTextField cropNumberField;
    private javax.swing.JPanel jCropNamePanel;
    private javax.swing.JPanel jCropNumberPanel;
    private javax.swing.JMenu jEditMenu;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jNewRecordItem;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JMenuItem jRemoveRecordItem;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JPanel jTablePanel;
    private javax.swing.JMenu mFileMenu;
    private javax.swing.JCheckBox winterCropCBox;
    // End of variables declaration//GEN-END:variables
    
    private CropDamageTable mTable;
    private FloodDamageFrame mParent;
    
    private DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
}

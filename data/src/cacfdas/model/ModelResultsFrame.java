/*
 * ModelResultsFrame.java
 *
 * Created on April 13, 2006, 10:04 AM
 */

package cacfdas.model;

import cacfdas.reachinfo.*;
import cacfdas.util.*;


import java.util.*;
import java.util.prefs.*;
import java.io.*;

import javax.swing.event.*;
import javax.swing.table.*;

/**
 *
 * @author  b4edhdwj
 */
public class ModelResultsFrame extends javax.swing.JFrame implements ListSelectionListener {
    
    /** Creates new form ModelResultsFrame */
    public ModelResultsFrame() {
        init();
        initComponents();
        addListeners();
    }
    
    public ModelResultsFrame(int startYear, ArrayList<HashMap <Integer,ArrayList<DataBand> > > input, CropInfoTable cropInfo) {
        init(startYear,input,cropInfo);
        initComponents();
        addListeners();
    }
    
    private void init()
    {
        model = new ModelResultsListModel();
        yearModel = new YearResultsTableModel();
        info = new CropInfoTable();
        
        String temp = prefs.get(lastFilePath,"");
        if ( !temp.equals("") )
        {
            lastFile = new File(temp);
        }
    }
    
    private void init(int startYear, ArrayList<HashMap <Integer,ArrayList<DataBand> > > input, CropInfoTable cropInfo) {
        model = new ModelResultsListModel(startYear,input,cropInfo);
        yearModel = new YearResultsTableModel();
        info = cropInfo;
    }
    
    private void addListeners()
    {
        jList1.addListSelectionListener(this);
        
        jList1.setSelectedIndex(0);
        //jList1.addMouseListener(model);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        fc = new javax.swing.JFileChooser();
        op = new javax.swing.JOptionPane();
        jSplitPane1 = new javax.swing.JSplitPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jScrollPane2 = new javax.swing.JScrollPane();
        jYearTable = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jFileMenu = new javax.swing.JMenu();
        jSaveItem = new javax.swing.JMenuItem();
        jViewMenu = new javax.swing.JMenu();
        jEventsMenuItem = new javax.swing.JMenuItem();
        jStatusChangeItem = new javax.swing.JMenuItem();
        jFloodItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Year Results");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jSplitPane1.setDividerLocation(100);
        jList1.setModel(model);
        jList1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jList1.setMaximumSize(new java.awt.Dimension(150, 0));
        jScrollPane1.setViewportView(jList1);

        jSplitPane1.setLeftComponent(jScrollPane1);

        jYearTable.setModel(yearModel);
        jYearTable.setDefaultRenderer(CurrencyContainer.class,nr);
        jScrollPane2.setViewportView(jYearTable);

        jSplitPane1.setRightComponent(jScrollPane2);

        getContentPane().add(jSplitPane1, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel1, java.awt.BorderLayout.NORTH);

        getContentPane().add(jPanel2, java.awt.BorderLayout.SOUTH);

        getContentPane().add(jPanel3, java.awt.BorderLayout.EAST);

        getContentPane().add(jPanel4, java.awt.BorderLayout.WEST);

        jFileMenu.setText("File");
        jSaveItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        jSaveItem.setMnemonic('s');
        jSaveItem.setText("Save");
        jSaveItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jSaveItemActionPerformed(evt);
            }
        });

        jFileMenu.add(jSaveItem);

        jMenuBar1.add(jFileMenu);

        jViewMenu.setText("View");
        jEventsMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jEventsMenuItem.setMnemonic('e');
        jEventsMenuItem.setText("Events");
        jEventsMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jEventsMenuItemActionPerformed(evt);
            }
        });

        jViewMenu.add(jEventsMenuItem);

        jStatusChangeItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jStatusChangeItem.setMnemonic('s');
        jStatusChangeItem.setText("Status Chages");
        jStatusChangeItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jStatusChangeItemActionPerformed(evt);
            }
        });

        jViewMenu.add(jStatusChangeItem);

        jFloodItem.setText("Floods");
        jFloodItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jFloodItemActionPerformed(evt);
            }
        });

        jViewMenu.add(jFloodItem);

        jMenuBar1.add(jViewMenu);

        setJMenuBar(jMenuBar1);

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-637)/2, (screenSize.height-476)/2, 637, 476);
    }//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        if ( ! mSaved )
        {
            int rval = op.showConfirmDialog(this, "Save Table", "Table not Saved", op.YES_NO_OPTION);
            
            if ( rval == op.OK_OPTION )
            {
                fc.setSelectedFile(lastFile);
                rval = fc.showSaveDialog(this);

                if ( rval == fc.APPROVE_OPTION )
                {
                    save(fc.getSelectedFile());
                }                
            }
            else
            {
                
            }            
        }
    }//GEN-LAST:event_formWindowClosing

    private void jSaveItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jSaveItemActionPerformed
        fc.setSelectedFile(lastFile);
        int rval = fc.showSaveDialog(this);
        
        if ( rval == fc.APPROVE_OPTION )
        {
            save(fc.getSelectedFile());
        }
    }//GEN-LAST:event_jSaveItemActionPerformed

    void save(File file)
    {
        try
        {
            YearResultsTableModel currentYear;

            // remove the file if it allready exists
            if ( file.exists() )
            {
                file.delete();
            }

            // make a writer for the new file
            FileWriter writer = new FileWriter(file);

            // write each years table to the file
            for(int i = 0; i < model.getSize(); ++i )
            {
                currentYear = new YearResultsTableModel(model.getData().get(i),info);

                currentYear.writeToStream(writer);

                if ( i != model.getSize() - 1)
                {
                    writer.write("\n\n");
                }

            }
        }
        catch(IOException e)
        {
            op.showMessageDialog(this,e.getMessage(),"IOError",op.ERROR_MESSAGE);
        }
        
        lastFile = file;
        prefs.put(lastFilePath,file.getAbsolutePath());
        
        mSaved = true;
    }
    
    private void jFloodItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jFloodItemActionPerformed
        int index = jYearTable.getSelectedRow();
        
        yearModel.viewFloodList(index);
    }//GEN-LAST:event_jFloodItemActionPerformed

    private void jStatusChangeItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jStatusChangeItemActionPerformed
        int index = jYearTable.getSelectedRow();
        
        yearModel.viewStatusChangeList(index);
    }//GEN-LAST:event_jStatusChangeItemActionPerformed

    private void jEventsMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jEventsMenuItemActionPerformed
        int index = jYearTable.getSelectedRow();
        
        yearModel.viewActionList(index);
    }//GEN-LAST:event_jEventsMenuItemActionPerformed
    
    public void valueChanged(ListSelectionEvent e)
    {
        if ( e.getValueIsAdjusting() )
        {
            return;
        }
        else
        {
            int pos = jList1.getSelectedIndex();
            yearModel = new YearResultsTableModel(model.getData().get(pos),info);
            jYearTable.setModel(yearModel);
            jYearTable.getSelectionModel().setSelectionInterval(0,0);
        }
    }    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ModelResultsFrame().setVisible(true);
            }
        });
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JFileChooser fc;
    private javax.swing.JMenuItem jEventsMenuItem;
    private javax.swing.JMenu jFileMenu;
    private javax.swing.JMenuItem jFloodItem;
    private javax.swing.JList jList1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JMenuItem jSaveItem;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JMenuItem jStatusChangeItem;
    private javax.swing.JMenu jViewMenu;
    private javax.swing.JTable jYearTable;
    private javax.swing.JOptionPane op;
    // End of variables declaration//GEN-END:variables
     
    private ModelResultsListModel model;
    private YearResultsTableModel yearModel;
    private CropInfoTable info;
    
    private NumberRenderer nr = new NumberRenderer();
    
    private Preferences prefs = Preferences.userNodeForPackage(ModelResultsFrame.class);
    
    private static final String lastFilePath = "Model Results Last File";
    File lastFile;
    
    boolean mSaved = false;
}

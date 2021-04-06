/*
 * CropDamageFrame.java
 *
 * Created on March 13, 2006, 3:45 PM
 */

package cacfdas;

import java.util.*;
import java.util.prefs.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.datatransfer.*;
import java.io.*;

/**
 *
 * @author  b4edhdwj
 */
public class FloodDamageFrame extends javax.swing.JFrame implements ClipboardOwner  {
    
    /** Creates new form CropDamageFrame */
    public FloodDamageFrame() {
        mTable = new FloodDamageTable();
        
        init();
        initComponents();
        addListeners();
        addFileFilters();
    }

    public FloodDamageFrame(FloodDamageTable t) {
        mTable = t;
        
        init();
        initComponents();
        addListeners();
        addFileFilters();
    }    
    
    private void init()
    {
        String tmp;
        
        openItems = new HashSet();
        
        prefs = Preferences.userNodeForPackage(FloodDamageFrame.class);
        
        // get the last open file
        tmp = prefs.get("Last Open File", "");
        if ( tmp.equals("") == false )
        {
            lastOpenFile = new File(tmp);
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
        jPanel1 = new javax.swing.JPanel();
        jTablePanel = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        cropList = new javax.swing.JList();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jFileMenu = new javax.swing.JMenu();
        jNewTableItem = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JSeparator();
        jOpenItem = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JSeparator();
        jSaveItem = new javax.swing.JMenuItem();
        jSaveAsItem = new javax.swing.JMenuItem();
        jSeparator4 = new javax.swing.JSeparator();
        jCloseItem = new javax.swing.JMenuItem();
        jEditMenu = new javax.swing.JMenu();
        jCopyItem = new javax.swing.JMenuItem();
        jCropMenu = new javax.swing.JMenu();
        jNewCropItem = new javax.swing.JMenuItem();
        jRemoveCropItem = new javax.swing.JMenuItem();

        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.Y_AXIS));

        jPanel1.setMaximumSize(new java.awt.Dimension(32767, 10));
        getContentPane().add(jPanel1);

        jTablePanel.setLayout(new javax.swing.BoxLayout(jTablePanel, javax.swing.BoxLayout.X_AXIS));

        jPanel2.setMaximumSize(new java.awt.Dimension(10, 32767));
        jTablePanel.add(jPanel2);

        cropList.setModel(new FloodDamageListModel(mTable));
        cropList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        cropList.setCellRenderer(new FloodDamageRenderer());
        cropList.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cropListKeyPressed(evt);
            }
        });

        jScrollPane1.setViewportView(cropList);

        jTablePanel.add(jScrollPane1);

        jPanel3.setMaximumSize(new java.awt.Dimension(10, 32767));
        jTablePanel.add(jPanel3);

        getContentPane().add(jTablePanel);

        jPanel4.setMaximumSize(new java.awt.Dimension(32767, 10));
        getContentPane().add(jPanel4);

        jFileMenu.setMnemonic('F');
        jFileMenu.setText("File");
        jFileMenu.addMenuListener(new javax.swing.event.MenuListener() {
            public void menuCanceled(javax.swing.event.MenuEvent evt) {
            }
            public void menuDeselected(javax.swing.event.MenuEvent evt) {
            }
            public void menuSelected(javax.swing.event.MenuEvent evt) {
                jFileMenuMenuSelected(evt);
            }
        });

        jNewTableItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        jNewTableItem.setMnemonic('N');
        jNewTableItem.setText("New");
        jNewTableItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jNewTableItemActionPerformed(evt);
            }
        });

        jFileMenu.add(jNewTableItem);

        jFileMenu.add(jSeparator1);

        jOpenItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        jOpenItem.setText("Open");
        jOpenItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jOpenItemActionPerformed(evt);
            }
        });

        jFileMenu.add(jOpenItem);

        jFileMenu.add(jSeparator2);

        jSaveItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        jSaveItem.setMnemonic('s');
        jSaveItem.setText("Save");
        jSaveItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jSaveItemActionPerformed(evt);
            }
        });

        jFileMenu.add(jSaveItem);

        jSaveAsItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jSaveAsItem.setMnemonic('s');
        jSaveAsItem.setText("Save As");
        jSaveAsItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jSaveAsItemActionPerformed(evt);
            }
        });

        jFileMenu.add(jSaveAsItem);

        jFileMenu.add(jSeparator4);

        jCloseItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_W, java.awt.event.InputEvent.CTRL_MASK));
        jCloseItem.setMnemonic('o');
        jCloseItem.setText("Close");
        jCloseItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCloseItemActionPerformed(evt);
            }
        });

        jFileMenu.add(jCloseItem);

        jMenuBar1.add(jFileMenu);

        jEditMenu.setMnemonic('E');
        jEditMenu.setText("Edit");
        jCopyItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.CTRL_MASK));
        jCopyItem.setMnemonic('c');
        jCopyItem.setText("Copy");
        jCopyItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCopyItemActionPerformed(evt);
            }
        });

        jEditMenu.add(jCopyItem);

        jMenuBar1.add(jEditMenu);

        jCropMenu.setText("Crops");
        jNewCropItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jNewCropItem.setMnemonic('n');
        jNewCropItem.setText("New Crop");
        jNewCropItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jNewCropItemActionPerformed(evt);
            }
        });

        jCropMenu.add(jNewCropItem);

        jRemoveCropItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.CTRL_MASK));
        jRemoveCropItem.setMnemonic('R');
        jRemoveCropItem.setText("Remove Crop");
        jRemoveCropItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRemoveCropItemActionPerformed(evt);
            }
        });

        jCropMenu.add(jRemoveCropItem);

        jMenuBar1.add(jCropMenu);

        setJMenuBar(jMenuBar1);

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-509)/2, (screenSize.height-409)/2, 509, 409);
    }//GEN-END:initComponents

    private void cropListKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cropListKeyPressed
        if ( evt.getKeyCode() == evt.VK_DELETE || evt.getKeyCode() == evt.VK_BACK_SPACE)
        {
            removeCrop();
            
            evt.consume();
        }
    }//GEN-LAST:event_cropListKeyPressed

    private void jRemoveCropItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRemoveCropItemActionPerformed
        removeCrop();
    }//GEN-LAST:event_jRemoveCropItemActionPerformed

    private void jNewCropItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jNewCropItemActionPerformed
        mTable.addEntry();
        cropList.setModel(new FloodDamageListModel(mTable));     
    }//GEN-LAST:event_jNewCropItemActionPerformed

    private void jCopyItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCopyItemActionPerformed
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        String s = cropList.getModel().getElementAt(cropList.getSelectedIndex()).toString();
        StringSelection selection = new StringSelection(s);
        clipboard.setContents(selection, this);
    }//GEN-LAST:event_jCopyItemActionPerformed

    private void jFileMenuMenuSelected(javax.swing.event.MenuEvent evt) {//GEN-FIRST:event_jFileMenuMenuSelected
        if ( mTable.getFile() != null )
        {
            jSaveItem.setEnabled(mTable.isDirty());
        }
        else
        {
            jSaveItem.setEnabled(false);
        }
    }//GEN-LAST:event_jFileMenuMenuSelected

    private void jSaveAsItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jSaveAsItemActionPerformed
        // setup the file chooser
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fc.setAcceptAllFileFilterUsed(false);   // do not display all files
        fc.setCurrentDirectory(lastOpenFile);
        
        int rval = fc.showSaveDialog(this);
        
        if (rval == fc.CANCEL_OPTION )
        {
            return;
        }
        else if ( rval == fc.APPROVE_OPTION )
        {
            try
            {
                mTable.exportFile(fc.getSelectedFile());
            }
            catch(IOException e)
            {
                op.showMessageDialog(this,e.getMessage(),"IOError",op.ERROR_MESSAGE);
            }           
        }
        else if ( rval == fc.ERROR_OPTION )
        {
            op.showMessageDialog(this,"File could not be saved","Save Error",op.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jSaveAsItemActionPerformed

    private void jSaveItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jSaveItemActionPerformed
        try
        {
            mTable.exportFile(mTable.getSource());
        }
        catch(IOException e)
        {
            op.showMessageDialog(this,e.getMessage(),"IOError",op.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jSaveItemActionPerformed

    private void jCloseItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCloseItemActionPerformed
        dispose();
    }//GEN-LAST:event_jCloseItemActionPerformed

    private void jNewTableItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jNewTableItemActionPerformed
        mTable.clear(); 
        cropList.setModel(new FloodDamageListModel(mTable));
    }//GEN-LAST:event_jNewTableItemActionPerformed

    private void jOpenItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jOpenItemActionPerformed
        // setup the file chooser
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fc.setAcceptAllFileFilterUsed(false);   // do not display all files
        fc.setCurrentDirectory(lastOpenFile);     
            
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
                mTable.load(selection);
                cropList.setModel(new FloodDamageListModel(mTable));
            }
            catch(FileNotFoundException e)
            {
                op.showMessageDialog(this,e.getMessage(),"File no found",op.ERROR_MESSAGE);
            }
            catch(IOException e)
            {
                op.showMessageDialog(this,e.getMessage(),"IOError",op.ERROR_MESSAGE);
            }
            
            lastOpenFile = selection;
            prefs.put("Last Open File", selection.getPath());
        }
    }//GEN-LAST:event_jOpenItemActionPerformed
    
    private void addListeners()
    {
        MouseListener mouseListener = new MouseAdapter() {
             public void mouseClicked(MouseEvent e) {
                 if (e.getClickCount() == 2) {
                     int index = cropList.locationToIndex(e.getPoint());
                     if ( openItems.contains(mTable.getEntry(index)) == false )
                     {
                         openItems.add(mTable.getEntry(index));
                         CropDamageFrame window = new CropDamageFrame(mTable.getEntry(index),self);
                         window.setVisible(true);
                     }
                  }
             }
         };
         
         cropList.addMouseListener(mouseListener);
         
         pa = new PasteAdaptor(cropList);
         jEditMenu.add(pa);
    }
    
    private void addFileFilters()
    {         
        fc.addChoosableFileFilter(new javax.swing.filechooser.FileFilter(){ 
                public boolean accept(java.io.File f)
                {
                    String ext = getExtension(f);
                    
                    if ( f.isDirectory() )
                        return true;
                    else if ( ext == null)
                    {
                        return false;
                    }
                    else if ( ext.equals("txt") || ext.equals("text"))
                        return true;
                    else
                        return false;
                }
                
                public String getDescription()
                {
                    return new String("*.txt, *.text");
                }
                
                private String getExtension(java.io.File f) 
                {
                    String ext = null;
                    String s = f.getName();
                    int i = s.lastIndexOf('.');

                    if (i > 0 &&  i < s.length() - 1) {
                        ext = s.substring(i+1).toLowerCase();
                    }
                    return ext;
               }                
        }); 
        
        fc.addChoosableFileFilter(new javax.swing.filechooser.FileFilter(){ 
                public boolean accept(java.io.File f)
                {
                    String ext = getExtension(f);
                    
                    if ( f.isDirectory() )
                        return true;
                    else if ( ext == null)
                    {
                        return false;
                    }
                    else if ( ext.equals("xml") )
                        return true;
                    else
                        return false;
                }
                
                public String getDescription()
                {
                    return new String("*.xml");
                }
                
                private String getExtension(java.io.File f) 
                {
                    String ext = null;
                    String s = f.getName();
                    int i = s.lastIndexOf('.');

                    if (i > 0 &&  i < s.length() - 1) {
                        ext = s.substring(i+1).toLowerCase();
                    }
                    return ext;
               }                
        });        
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FloodDamageFrame().setVisible(true);
            }
        });
    }
    
    public FloodDamageTable getTable()
    {
        return mTable;
    }

    public void lostOwnership(Clipboard clip, Transferable tr) 
    {
        
    } 
    
    public void notifyClose(CropDamageTable table)
    {        
        openItems.remove(table);
    }    
    
    public void removeCrop()
    {
        int pos1 = cropList.getSelectionModel().getMinSelectionIndex();
        int pos2 = cropList.getSelectionModel().getMaxSelectionIndex();
        
        if ( pos1 == -1 || pos2 == -1 )
        {
            return;
        }
        
        int num = pos2 - pos1 + 1;
        
        FloodDamageListModel fdlm = (FloodDamageListModel) cropList.getModel();
        fdlm.remove(pos1, pos2);
        
        ListSelectionModel m = cropList.getSelectionModel();
        pos1 -= num;
        pos2 -= num;
        
        if (pos1 < 0 ) pos1 = 0;
        if (pos2 < 0 ) pos2 = 0;
        
        m.setSelectionInterval(pos1,pos2);        
    }
    
    /**
     *  The model for the displayed list 
     */
    
    private class FloodDamageListModel extends AbstractListModel
    {
        FloodDamageListModel()
        {
            super();
        }
        
        FloodDamageListModel(FloodDamageTable table)
        {
            super();
            
            mTable = table;
        }
        
        public Object getElementAt(int i) {
           if ( mTable == null )
           {
               return "";
           }
           else
           {
               return mTable.getEntry(i);
           }
        }
        
        public FloodDamageTable getTable()
        {
            return mTable;
        }
        
        public int getSize()
        {
            if ( mTable == null )
            {
                return 0;
            }
            else
            {
                return mTable.size();
            }
        }
        
        public void remove(int pos1, int pos2)
        {
            mTable.remove(pos1,pos2);
            
            fireIntervalRemoved(this,pos1, pos2);
        }
        
        public void setTable(FloodDamageTable t)
        {
            mTable = t;
        }
        
        FloodDamageTable mTable;
        
    }
    
    private class FloodDamageRenderer extends JTextArea implements ListCellRenderer {

         // This is the only method defined by ListCellRenderer.
         // We just reconfigure the JLabel each time we're called.
       
         public Component getListCellRendererComponent(
           JList list,
           Object value,            // value to display
           int index,               // cell index
           boolean isSelected,      // is the cell selected
           boolean cellHasFocus)    // the list and the cell have the focus
           
         {
            String s = value.toString();
            setText(s);
            if (isSelected) {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            }
            else {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }
            setEnabled(list.isEnabled());
            setFont(list.getFont());
            setOpaque(true);
            setBorder(BorderFactory.createRaisedBevelBorder());
            return this;
        }
    }
    
    private class PasteAdaptor extends AbstractAction 
    {
        public PasteAdaptor(JComponent c)
        {
            putValue(Action.NAME,"Paste");
            
            KeyStroke paste = KeyStroke.getKeyStroke(KeyEvent.VK_V,ActionEvent.CTRL_MASK,false);
            putValue(Action.ACCELERATOR_KEY,paste);
            
            putValue(Action.MNEMONIC_KEY,KeyEvent.VK_A);
            
            component = c;
            component.getInputMap().put(paste,"Paste");
            component.getActionMap().put("Paste",this);
            
            
            //component.registerKeyboardAction(this,"Paste", paste, c.WHEN_FOCUSED);
            
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
                    mTable.addLine(lineString[i]);
                }
                
                cropList.setModel(new FloodDamageListModel(mTable));
            }
            catch( UnsupportedFlavorException e1 )
            {
                // this exception means the clipboard did not have text no further action is nessessary
            }
            catch (Exception e2)
            {
                e2.printStackTrace();
            }
        }
        
        private JComponent component;
        private Clipboard system;
        
        private FloodDamageTable table;
        
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList cropList;
    private javax.swing.JFileChooser fc;
    private javax.swing.JMenuItem jCloseItem;
    private javax.swing.JMenuItem jCopyItem;
    private javax.swing.JMenu jCropMenu;
    private javax.swing.JMenu jEditMenu;
    private javax.swing.JMenu jFileMenu;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jNewCropItem;
    private javax.swing.JMenuItem jNewTableItem;
    private javax.swing.JMenuItem jOpenItem;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JMenuItem jRemoveCropItem;
    private javax.swing.JMenuItem jSaveAsItem;
    private javax.swing.JMenuItem jSaveItem;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JPanel jTablePanel;
    private javax.swing.JOptionPane op;
    // End of variables declaration//GEN-END:variables
    
    private FloodDamageTable mTable;
    
    private HashSet openItems;
    private FloodDamageFrame self = this;
    
    private Preferences prefs;
    private File lastOpenFile;
    
    private PasteAdaptor pa;
    
}

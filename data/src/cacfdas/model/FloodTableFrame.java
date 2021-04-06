/*
 * FloodTableFrame.java
 *
 * Created on April 18, 2006, 2:41 PM
 */

package cacfdas.model;

/**
 *
 * @author  b4edhdwj
 */

import java.util.*;

import cacfdas.reachinfo.*;

/** This class is the GUI for displaying a table containing the flood event
 *  start and stop dates for a particular data band */

public class FloodTableFrame extends javax.swing.JFrame {
    
    /** Creates new form FloodTableFrame */
    public FloodTableFrame(DataBand band, CropInfoTable info) {
        init(band);
        initComponents();
        
        setTitle("Flood Log for " + info.getInfo(band.getCropNumber()).trim() + 
                " elev " + band.getMinElevation() + " - " + band.getMaxElevation() + 
                " year " + band.getDateList().get(0).dayOfYear() ); 
    }
    
    void init(DataBand band)
    {
        model = new FloodTableModel(band.getFloodList());
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        jTable1.setModel(model);
        jScrollPane1.setViewportView(jTable1);

        getContentPane().add(jScrollPane1, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel1, java.awt.BorderLayout.NORTH);

        getContentPane().add(jPanel2, java.awt.BorderLayout.SOUTH);

        getContentPane().add(jPanel3, java.awt.BorderLayout.EAST);

        getContentPane().add(jPanel4, java.awt.BorderLayout.WEST);

        pack();
    }//GEN-END:initComponents
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
    
    FloodTableModel model;
}
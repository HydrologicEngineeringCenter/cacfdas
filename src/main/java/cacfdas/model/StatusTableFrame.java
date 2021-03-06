/*
 * StatusTableFrame.java
 *
 * Created on April 14, 2006, 9:49 AM
 */

package cacfdas.model;

/**
 *
 * @author  b4edhdwj
 */

import javax.swing.*;

import cacfdas.reachinfo.*;

public class StatusTableFrame extends javax.swing.JFrame {
    
    /** Creates new form StatusTableFrame */
    public StatusTableFrame(DataBand band, CropInfoTable info) {
        init(band);
        initComponents();
        
        setTitle("Status Log for " + info.getInfo(band.getCropNumber()).trim() + 
                " elev " + band.getMinElevation() + " - " + band.getMaxElevation() + 
                " year " + band.getDateList().get(0).dayOfYear() ); 
    }
    
    private void init(DataBand band)
    {
        model = new StatusTableModel(band); 
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
    
    private StatusTableModel model;
}

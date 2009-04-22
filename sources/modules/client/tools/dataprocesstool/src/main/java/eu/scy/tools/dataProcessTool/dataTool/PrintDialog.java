/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * PrintDialog.java
 *
 * Created on 29 janv. 2009, 16:05:14
 */

package eu.scy.tools.dataProcessTool.dataTool;

import eu.scy.tools.dataProcessTool.utilities.CopexReturn;

/**
 * print Dialog
 * @author Marjolaine
 */
public class PrintDialog extends javax.swing.JDialog {
    // PROPERTY
    /*owner */
    private MainDataToolPanel owner;

    public PrintDialog(MainDataToolPanel owner) {
        super();
        this.owner = owner;
        initComponents();
    }

    /** Creates new form PrintDialog */
    public PrintDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        labelToPrint = new javax.swing.JLabel();
        rbDsSel = new javax.swing.JRadioButton();
        rbAllDs = new javax.swing.JRadioButton();
        rbVisSel = new javax.swing.JRadioButton();
        rbAllVis = new javax.swing.JRadioButton();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(owner.getBundleString("TITLE_DIALOG_PRINT"));
        setModal(true);
        setResizable(false);

        labelToPrint.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        labelToPrint.setText(owner.getBundleString("LABEL_TO_PRINT"));

        rbDsSel.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        rbDsSel.setSelected(true);
        rbDsSel.setText(owner.getBundleString("LABEL_PRINT_DS_SEL"));
        rbDsSel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbDsSelActionPerformed(evt);
            }
        });

        rbAllDs.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        rbAllDs.setText(owner.getBundleString("LABEL_PRINT_ALL_DS"));
        rbAllDs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbAllDsActionPerformed(evt);
            }
        });

        rbVisSel.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        rbVisSel.setSelected(true);
        rbVisSel.setText(owner.getBundleString("LABEL_PRINT_VIS_SEL"));
        rbVisSel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbVisSelActionPerformed(evt);
            }
        });

        rbAllVis.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        rbAllVis.setText(owner.getBundleString("LABEL_PRINT_ALL_VIS"));
        rbAllVis.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbAllVisActionPerformed(evt);
            }
        });

        jButton1.setText(owner.getBundleString("BUTTON_OK"));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText(owner.getBundleString("BUTTON_CANCEL"));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(labelToPrint, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(rbDsSel)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(21, 21, 21)
                                .addComponent(jButton1))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(21, 21, 21)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(rbVisSel)
                                    .addComponent(rbAllVis))
                                .addGap(6, 6, 6)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(43, 43, 43)
                                .addComponent(rbAllDs))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(52, 52, 52)
                                .addComponent(jButton2)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labelToPrint)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rbDsSel)
                    .addComponent(rbAllDs))
                .addGap(18, 18, 18)
                .addComponent(rbVisSel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(rbAllVis)
                .addGap(36, 36, 36)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addContainerGap(32, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void rbDsSelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbDsSelActionPerformed
        this.rbDsSel.setSelected(true);
        this.rbAllDs.setSelected(false);
        this.rbVisSel.setEnabled(true);
        this.rbAllVis.setEnabled(true);
    }//GEN-LAST:event_rbDsSelActionPerformed

    private void rbAllDsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbAllDsActionPerformed
        this.rbDsSel.setSelected(false);
        this.rbAllDs.setSelected(true);
        this.rbVisSel.setEnabled(false);
        this.rbAllVis.setEnabled(false);
    }//GEN-LAST:event_rbAllDsActionPerformed

    private void rbVisSelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbVisSelActionPerformed
        this.rbVisSel.setSelected(true);
        this.rbAllVis.setSelected(false);
    }//GEN-LAST:event_rbVisSelActionPerformed

    private void rbAllVisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbAllVisActionPerformed
        this.rbVisSel.setSelected(false);
        this.rbAllVis.setSelected(true);
    }//GEN-LAST:event_rbAllVisActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        this.dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        boolean isAllDSSel = this.rbAllDs.isSelected();
        boolean isAllVisSel = this.rbAllVis.isSelected();
        boolean isOk = owner.printDataset(isAllDSSel, isAllVisSel);
        if (isOk)
            this.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                PrintDialog dialog = new PrintDialog(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel labelToPrint;
    private javax.swing.JRadioButton rbAllDs;
    private javax.swing.JRadioButton rbAllVis;
    private javax.swing.JRadioButton rbDsSel;
    private javax.swing.JRadioButton rbVisSel;
    // End of variables declaration//GEN-END:variables

}

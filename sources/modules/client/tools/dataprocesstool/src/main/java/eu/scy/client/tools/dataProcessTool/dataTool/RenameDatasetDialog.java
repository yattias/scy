/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * RenameDatasetDialog.java
 *
 * Created on 10 sept. 2009, 16:38:10
 */

package eu.scy.client.tools.dataProcessTool.dataTool;

import eu.scy.client.tools.dataProcessTool.utilities.CopexReturn;
import eu.scy.client.tools.dataProcessTool.utilities.DataConstants;
import eu.scy.client.tools.dataProcessTool.utilities.MyUtilities;

/**
 * rename dataset
 * @author Marjolaine
 */
public class RenameDatasetDialog extends javax.swing.JDialog {
    private FitexToolPanel fitex;
    private String name;


    public RenameDatasetDialog(FitexToolPanel fitex, String name) {
        super();
        this.fitex = fitex;
        this.name = name;
        initComponents();
        initGUI();
    }


    /** Creates new form RenameDatasetDialog */
    public RenameDatasetDialog(java.awt.Frame parent, boolean modal) {
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

        labelName = new javax.swing.JLabel();
        fieldName = new javax.swing.JTextField();
        buttonOk = new javax.swing.JButton();
        buttonCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(fitex.getBundleString("TITLE_DIALOG_RENAME_DS"));
        setModal(true);
        setResizable(false);

        labelName.setFont(new java.awt.Font("Tahoma", 1, 11));
        labelName.setText(fitex.getBundleString("LABEL_NAME"));

        fieldName.setPreferredSize(new java.awt.Dimension(6, 27));

        buttonOk.setText(fitex.getBundleString("BUTTON_OK"));
        buttonOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonOkActionPerformed(evt);
            }
        });

        buttonCancel.setText(fitex.getBundleString("BUTTON_CANCEL"));
        buttonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonCancelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labelName)
                .addGap(47, 47, 47)
                .addComponent(fieldName, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(32, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addComponent(buttonOk)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 53, Short.MAX_VALUE)
                .addComponent(buttonCancel)
                .addGap(44, 44, 44))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelName)
                    .addComponent(fieldName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(33, 33, 33)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonOk)
                    .addComponent(buttonCancel))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void buttonOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonOkActionPerformed
        validDialog();
    }//GEN-LAST:event_buttonOkActionPerformed

    private void buttonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonCancelActionPerformed
        this.dispose();
    }//GEN-LAST:event_buttonCancelActionPerformed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                RenameDatasetDialog dialog = new RenameDatasetDialog(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton buttonCancel;
    private javax.swing.JButton buttonOk;
    private javax.swing.JTextField fieldName;
    private javax.swing.JLabel labelName;
    // End of variables declaration//GEN-END:variables

    private void initGUI(){
        setLocationRelativeTo(fitex);
        this.fieldName.setText(name);
        this.labelName.setSize(MyUtilities.lenghtOfString(this.labelName.getText(), getFontMetrics(this.labelName.getFont())), this.labelName.getHeight());
    }

    private void validDialog(){
        String n = this.fieldName.getText();
        if (controlLenght() && n.length() > DataConstants.MAX_LENGHT_DATASET_NAME){
            String msg = fitex.getBundleString("MSG_LENGHT_MAX");
            msg  = MyUtilities.replace(msg, 0, fitex.getBundleString("LABEL_NAME"));
            msg = MyUtilities.replace(msg, 1, ""+DataConstants.MAX_LENGHT_DATASET_NAME);
            fitex.displayError(new CopexReturn(msg, false), fitex.getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }
        if (n.length() == 0){
            String msg = fitex.getBundleString("MSG_ERROR_FIELD_NULL");
            msg  = MyUtilities.replace(msg, 0, fitex.getBundleString("LABEL_NAME"));
            fitex.displayError(new CopexReturn(msg ,false), fitex.getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }
        if(n.equals(name))
            this.dispose();
        boolean isOk = fitex.renameDataset(n);
        if(isOk)
            this.dispose();
    }

    private boolean controlLenght(){
        return fitex.controlLenght();
    }
}

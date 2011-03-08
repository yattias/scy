/*
 * CreateDataVisualDialog.java
 *
 * Created on 18 novembre 2008, 13:23
 */

package eu.scy.tools.dataProcessTool.dataTool;

import eu.scy.tools.dataProcessTool.common.TypeVisualization;
import eu.scy.tools.dataProcessTool.utilities.CopexReturn;
import eu.scy.tools.dataProcessTool.utilities.ScyUtilities;
import eu.scy.tools.dataProcessTool.utilities.DataConstants;

/**
 * Dialog which allows to the user to choose between the different types of visual
 * @author  Marjolaine Bodin
 */
public class CreateDataVisualDialog extends javax.swing.JDialog {

    // PROPERTY 
    /* owner */
    private MainDataToolPanel owner ;
    
    /* liste des choix possibles */
    private TypeVisualization[] tabTypes;

    
    // CONSTRUCTOR
    public CreateDataVisualDialog(MainDataToolPanel owner, TypeVisualization[] tabTypes) {
        super();
        this.owner = owner;
        this.tabTypes = tabTypes;
        this.setLocationRelativeTo(owner);
        this.setModal(true);
        this.setResizable(false);
        initComponents();
        init();
        
    }
    
    
    /** Creates new form CreateDataVisualDialog */
    public CreateDataVisualDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    
    // METHOD 
    private void init(){
        int l = 30;
       int n = this.tabTypes.length;
       for (int i=0; i<n; i++){
           cbType.addItem(this.tabTypes[i].getName());
           int tl = ScyUtilities.lenghtOfString(this.tabTypes[i].getName(), getFontMetrics(cbType.getFont()));
           l = Math.max(l, tl);
       }
       if (n > 0)
        this.cbType.setSelectedIndex(0);
      cbType.setSize(l+25, 20);
    }
    
    /* creation d'un nouveau type */
    private void createTypeVisual(){
        // recupere le nom
        String name = fieldName.getText() ;
        if (name == null || name.length() == 0){
            String msg = owner.getBundleString("MSG_ERROR_FIELD_NULL") ;
            msg  = ScyUtilities.replace(msg, 0, owner.getBundleString("LABEL_NAME"));
            owner.displayError(new CopexReturn(msg, false), owner.getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }
         if (name.length() > DataConstants.MAX_LENGHT_GRAPH_NAME){
            String msg = owner.getBundleString("MSG_LENGHT_MAX");
             msg  = ScyUtilities.replace(msg, 0, owner.getBundleString("LABEL_NAME"));
             msg = ScyUtilities.replace(msg, 1, ""+DataConstants.MAX_LENGHT_GRAPH_NAME);
            owner.displayError(new CopexReturn(msg, false), owner.getBundleString("TITLE_DIALOG_ERROR"));
            return;
         }
        // recupere le code 
        int id = cbType.getSelectedIndex() ;
        if (id != -1){
            boolean isOk = owner.createTypeVisual(tabTypes[id], name);
            if (isOk)
                this.dispose();
        }
    }
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        labelType = new javax.swing.JLabel();
        cbType = new javax.swing.JComboBox();
        buttonOk = new javax.swing.JButton();
        buttonCancel = new javax.swing.JButton();
        labelName = new javax.swing.JLabel();
        fieldName = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(this.owner.getBundleString("TITLE_DIALOG_CREATE_VISUAL"));

        labelType.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        labelType.setText(this.owner.getBundleString("LABEL_TYPE")+" :");

        buttonOk.setText(this.owner.getBundleString("BUTTON_OK"));
        buttonOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonOkActionPerformed(evt);
            }
        });

        buttonCancel.setText(this.owner.getBundleString("BUTTON_CANCEL"));
        buttonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonCancelActionPerformed(evt);
            }
        });

        labelName.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        labelName.setText(this.owner.getBundleString("LABEL_NAME")+" :");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelName)
                    .addComponent(labelType))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(fieldName)
                    .addComponent(cbType, 0, 185, Short.MAX_VALUE))
                .addContainerGap(44, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(buttonOk)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 85, Short.MAX_VALUE)
                .addComponent(buttonCancel)
                .addGap(29, 29, 29))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelName)
                    .addComponent(fieldName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelType)
                    .addComponent(cbType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 33, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonOk)
                    .addComponent(buttonCancel))
                .addGap(21, 21, 21))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void buttonOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonOkActionPerformed
    createTypeVisual();
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
                CreateDataVisualDialog dialog = new CreateDataVisualDialog(new javax.swing.JFrame(), true);
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
    private javax.swing.JComboBox cbType;
    private javax.swing.JTextField fieldName;
    private javax.swing.JLabel labelName;
    private javax.swing.JLabel labelType;
    // End of variables declaration//GEN-END:variables

}
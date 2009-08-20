/*
 * CreateDataVisualDialog.java
 *
 * Created on 18 novembre 2008, 13:23
 */

package eu.scy.tools.dataProcessTool.dataTool;

import eu.scy.tools.dataProcessTool.common.DataHeader;
import eu.scy.tools.dataProcessTool.common.TypeVisualization;
import eu.scy.tools.dataProcessTool.utilities.CopexReturn;
import eu.scy.tools.dataProcessTool.utilities.MyUtilities;
import eu.scy.tools.dataProcessTool.utilities.DataConstants;

/**
 * Dialog which allows to the user to choose between the different types of visual
 * @author  Marjolaine Bodin
 */
public class CreateDataVisualDialog extends javax.swing.JDialog {

    // PROPERTY 
    /* owner */
    private DataProcessToolPanel owner ;
    /* liste des choix possibles */
    private TypeVisualization[] tabTypes;
    /* liste des colonnes */
    private DataHeader[] listCol;
    /* liste des colonnes pour le deuxieme axe*/
    private DataHeader[] listCol2;

    
    // CONSTRUCTOR
    public CreateDataVisualDialog(DataProcessToolPanel owner, TypeVisualization[] tabTypes, DataHeader[] listCol) {
        super();
        this.owner = owner;
        this.tabTypes = tabTypes;
        this.listCol = listCol;
        initComponents();
        init();
        this.setLocationRelativeTo(owner);
        this.setModal(true);
        this.setResizable(false);
    }
    
    
    
    
    // METHOD 
    private void init(){
        int l = 30;
       int n = this.tabTypes.length;
       for (int i=0; i<n; i++){
           cbType.addItem(this.tabTypes[i].getName());
           int tl = MyUtilities.lenghtOfString(this.tabTypes[i].getName(), getFontMetrics(cbType.getFont()));
           l = Math.max(l, tl);
       }
       
      cbType.setSize(l+25, 20);
      this.labelAxisChoice.setSize(MyUtilities.lenghtOfString(this.labelAxisChoice.getText(), getFontMetrics(this.labelAxisChoice.getFont())), this.labelAxisChoice.getHeight());
      for (int i=0; i<listCol.length; i++){
            if(listCol[i] != null){
                cbAxis1.addItem(listCol[i].getValue());
                //cbAxis2.addItem(listCol[i].getValue());
            }
        }
      if (n > 0)
        this.cbType.setSelectedIndex(0);
      if (n == 1)
          this.cbType.setEnabled(false);
      repaint();

    }

    private void selectVisualType(){
        int id = cbType.getSelectedIndex() ;
        if (id != -1){
            TypeVisualization typeVis = tabTypes[id];
            if (typeVis.getNbColParam() < 2){
                this.remove(this.cbAxis2);
            }else{
                this.add(this.cbAxis2);
            }
        }
    }

    /* mise a jour des axes */
    private void updateCbAxis2(){
        this.listCol2 = new DataHeader[this.listCol.length - 1];
        int id1 = cbAxis1.getSelectedIndex() ;
        int j=0;
        for (int i=0; i<this.listCol.length; i++){
            if (i != id1){
                this.listCol2[j] = this.listCol[i];
            }
        }
        cbAxis2.removeAllItems();
        for (int i=0; i<listCol2.length; i++){
            if(listCol2[i] != null){
                cbAxis2.addItem(listCol2[i].getValue());
            }
        }
        cbAxis2.setSelectedIndex(0);
        repaint();
    }


    /* creation d'un nouveau type */
    private void createTypeVisual(){
        // recupere le nom
        String name = fieldName.getText() ;
        if (name == null || name.length() == 0){
            String msg = owner.getBundleString("MSG_ERROR_FIELD_NULL") ;
            msg  = MyUtilities.replace(msg, 0, owner.getBundleString("LABEL_NAME"));
            owner.displayError(new CopexReturn(msg, false), owner.getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }
         if (name.length() > DataConstants.MAX_LENGHT_GRAPH_NAME){
            String msg = owner.getBundleString("MSG_LENGHT_MAX");
             msg  = MyUtilities.replace(msg, 0, owner.getBundleString("LABEL_NAME"));
             msg = MyUtilities.replace(msg, 1, ""+DataConstants.MAX_LENGHT_GRAPH_NAME);
            owner.displayError(new CopexReturn(msg, false), owner.getBundleString("TITLE_DIALOG_ERROR"));
            return;
         }
        // recupere le type de vis
        int id = cbType.getSelectedIndex() ;
        TypeVisualization typeVis= tabTypes[id];
        // recupere les axes
        int id1 = cbAxis1.getSelectedIndex() ;
        DataHeader dataHeader = listCol[id1];
        DataHeader dataHeader2 = null;
        if (typeVis.getNbColParam() > 1){
            int id2 = cbAxis2.getSelectedIndex() ;
            dataHeader2 = listCol2[id2];
//            if (id1 == id2){
//                owner.displayError(new CopexReturn(owner.getBundleString("MSG_ERROR_AXIS"), false), owner.getBundleString("TITLE_DIALOG_ERROR"));
//                return ;
//            }
        }
        boolean isOk = owner.createVisualization(name, typeVis, dataHeader, dataHeader2);
        if (isOk)
            this.dispose();
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
        labelAxisChoice = new javax.swing.JLabel();
        cbAxis1 = new javax.swing.JComboBox();
        cbAxis2 = new javax.swing.JComboBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(this.owner.getBundleString("TITLE_DIALOG_CREATE_VISUAL"));
        setModal(true);
        setResizable(false);

        labelType.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        labelType.setText(this.owner.getBundleString("LABEL_TYPE"));

        cbType.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbTypeItemStateChanged(evt);
            }
        });

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
        labelName.setText(this.owner.getBundleString("LABEL_NAME"));

        labelAxisChoice.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        labelAxisChoice.setText(owner.getBundleString("LABEL_AXIS_CHOICE"));

        cbAxis1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbAxis1ItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(buttonOk)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 85, Short.MAX_VALUE)
                .addComponent(buttonCancel)
                .addGap(42, 42, 42))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(labelAxisChoice, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 75, Short.MAX_VALUE)
                    .addComponent(labelName, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelType, javax.swing.GroupLayout.Alignment.LEADING))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(fieldName)
                    .addComponent(cbType, 0, 185, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(cbAxis1, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(42, 42, 42)
                        .addComponent(cbAxis2, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(57, 57, 57))
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
                .addGap(28, 28, 28)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelAxisChoice)
                    .addComponent(cbAxis1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbAxis2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 27, Short.MAX_VALUE)
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

private void cbAxis1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbAxis1ItemStateChanged
    updateCbAxis2();
}//GEN-LAST:event_cbAxis1ItemStateChanged

private void cbTypeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbTypeItemStateChanged
    selectVisualType();
}//GEN-LAST:event_cbTypeItemStateChanged

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonCancel;
    private javax.swing.JButton buttonOk;
    private javax.swing.JComboBox cbAxis1;
    private javax.swing.JComboBox cbAxis2;
    private javax.swing.JComboBox cbType;
    private javax.swing.JTextField fieldName;
    private javax.swing.JLabel labelAxisChoice;
    private javax.swing.JLabel labelName;
    private javax.swing.JLabel labelType;
    // End of variables declaration//GEN-END:variables

}

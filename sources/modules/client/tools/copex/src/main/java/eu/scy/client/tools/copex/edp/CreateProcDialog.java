/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * CreateProcDialog.java
 *
 * Created on 3 mars 2009, 14:05:52
 */

package eu.scy.client.tools.copex.edp;

import eu.scy.client.tools.copex.common.*;
import eu.scy.client.tools.copex.controller.ControllerInterface;
import eu.scy.client.tools.copex.utilities.CopexReturn;
import eu.scy.client.tools.copex.utilities.CopexUtilities;
import eu.scy.client.tools.copex.utilities.MyConstants;
import java.awt.Cursor;
import java.util.List;
import java.util.Locale;

/**
 * ouverture editeur de proc, si aucun proc dans la mission et si plusieurs proc initiaux
 * on propose a l'etudiant le type de proc qu'il souhaite ouvrir
 * ressemble a AddProcDialog (1 ligne)
 * @author Marjolaine
 */
public class CreateProcDialog extends javax.swing.JDialog {

    // PROPERTY 
    /* editeur de protocole */
    private CopexPanel edP;
    /* controller */
    private ControllerInterface controller;
    /* liste des proc initiaux */
    private List<InitialProcedure> listInitialProc;

    private boolean setDefaultProcName = true;

    // CONSTRUCTOR
    public CreateProcDialog(CopexPanel edP, ControllerInterface controller, List<InitialProcedure> listInitialProc) {
        super(edP.getOwnerFrame());
        setLocationRelativeTo(edP);
        this.edP = edP;
        this.controller = controller;
        this.listInitialProc = listInitialProc;
        initComponents();
        setModal(true);
        setLocation(edP.getLocationDialog());
        init();
        setIconImage(edP.getIconDialog());
    }


    /** Creates new form CreateProcDialog */
    public CreateProcDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    // METHODES
    private void init(){
        // initialisation types de proc initiaux
        int nb = this.listInitialProc.size();
        for (int i=0; i<nb; i++){
            InitialProcedure initProc = listInitialProc.get(i);
            //cbInitialProc.addItem(initProc.getCode());
            cbInitialProc.addItem(initProc.getName(edP.getLocale()));
        }
        if (nb > 0){
            cbInitialProc.setSelectedIndex(0);
            if(setDefaultProcName)
                this.fieldProcName.setText(this.listInitialProc.get(0).getName(edP.getLocale()));
        }
        // resize des elements
        resizeElements();
        repaint();
    }

    /*
     * permet de resizer les elements de la fenetre en fonction de la longueur des textes
     * variable selon la langue
     */
   private void resizeElements(){
       // label
       this.labelCreateProc.setSize(CopexUtilities.lenghtOfString(this.labelCreateProc.getText(), getFontMetrics(this.labelCreateProc.getFont())), this.labelCreateProc.getHeight());
       this.labelNameProc.setSize(CopexUtilities.lenghtOfString(this.labelNameProc.getText(), getFontMetrics(this.labelNameProc.getFont())), this.labelNameProc.getHeight());
       // bouton Ok
       this.buttonOk.setSize(CopexUtilities.lenghtOfString(this.buttonOk.getText(), getFontMetrics(this.buttonOk.getFont())), this.buttonOk.getHeight());
       // bouton Annuler
       this.buttonCancel.setSize(CopexUtilities.lenghtOfString(this.buttonCancel.getText(), getFontMetrics(this.buttonCancel.getFont())), this.buttonCancel.getHeight());
      
   }

   /* selection d'un proc initial => par defaut on met le nom du proc initial */
   private void selectInitProc(){
       if(setDefaultProcName){
        int id = this.cbInitialProc.getSelectedIndex() ;
        if(id > 0){
            fieldProcName.setText(listInitialProc.get(id).getName(edP.getLocale()));
        }
       }
   }

   private boolean controlLenght(){
        return edP.controlLenght();
    }

   private void validDialog(){
        setCursor(new Cursor(Cursor.WAIT_CURSOR));
        // creation d'un nouveau protocole
        String name = fieldProcName.getText();
        if (name.length() == 0){
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            String msg = edP.getBundleString("MSG_ERROR_FIELD_NULL");
            msg = CopexUtilities.replace(msg, 0, edP.getBundleString("LABEL_PROC_NAME"));
            edP.displayError(new CopexReturn(msg, false), edP.getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }
        if (controlLenght() && name.length() > MyConstants.MAX_LENGHT_PROC_NAME){
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            String msg = edP.getBundleString("MSG_LENGHT_MAX");
            msg  = CopexUtilities.replace(msg, 0, edP.getBundleString("LABEL_PROC_NAME"));
            msg = CopexUtilities.replace(msg, 1, ""+MyConstants.MAX_LENGHT_PROC_NAME);
            edP.displayError(new CopexReturn(msg, false), edP.getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }
        // recupere le proc initial
        InitialProcedure initProc = listInitialProc.get(0);
        int id = this.cbInitialProc.getSelectedIndex() ;
        if (id == -1){
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            edP.displayError(new CopexReturn(edP.getBundleString("MSG_ERROR_OPEN_PROC"), false), edP.getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }
        initProc = this.listInitialProc.get(id);
        CopexReturn cr = this.controller.createProc(name, initProc);
        if (cr.isError()){
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            edP.displayError(cr, edP.getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        this.setVisible(false);
        this.dispose();
        edP.setQuestionDialog();
}

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        labelCreateProc = new javax.swing.JLabel();
        cbInitialProc = new javax.swing.JComboBox();
        labelNameProc = new javax.swing.JLabel();
        fieldProcName = new javax.swing.JTextField();
        buttonOk = new javax.swing.JButton();
        buttonCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(edP.getBundleString("TITLE_DIALOG_CREATE_PROC"));
        setModal(true);
        setResizable(false);

        labelCreateProc.setFont(new java.awt.Font("Tahoma", 1, 11));
        labelCreateProc.setText(edP.getBundleString("LABEL_ADD_NEW_PROC_INIT"));
        labelCreateProc.setName("labelCreateProc"); // NOI18N

        cbInitialProc.setName("cbInitialProc"); // NOI18N
        cbInitialProc.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbInitialProcItemStateChanged(evt);
            }
        });

        labelNameProc.setFont(new java.awt.Font("Tahoma", 1, 11));
        labelNameProc.setText(edP.getBundleString("LABEL_PROC_NAME"));
        labelNameProc.setName("labelNameProc"); // NOI18N

        fieldProcName.setName("fieldProcName"); // NOI18N
        fieldProcName.setPreferredSize(new java.awt.Dimension(6, 27));
        fieldProcName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fieldProcNameActionPerformed(evt);
            }
        });

        buttonOk.setText(edP.getBundleString("BUTTON_OK"));
        buttonOk.setName("buttonOk"); // NOI18N
        buttonOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonOkActionPerformed(evt);
            }
        });

        buttonCancel.setText(edP.getBundleString("BUTTON_CANCEL"));
        buttonCancel.setName("buttonCancel"); // NOI18N
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
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labelCreateProc)
                            .addComponent(cbInitialProc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(labelNameProc)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 85, Short.MAX_VALUE)
                                .addComponent(fieldProcName, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(47, 47, 47)
                        .addComponent(buttonOk)
                        .addGap(87, 87, 87)
                        .addComponent(buttonCancel)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labelCreateProc)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cbInitialProc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelNameProc)
                    .addComponent(fieldProcName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(32, 32, 32)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonOk)
                    .addComponent(buttonCancel))
                .addContainerGap(28, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void fieldProcNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fieldProcNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fieldProcNameActionPerformed

    private void buttonOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonOkActionPerformed
        validDialog();
    }//GEN-LAST:event_buttonOkActionPerformed

    private void buttonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonCancelActionPerformed
        this.setVisible(false);
        this.dispose();
    }//GEN-LAST:event_buttonCancelActionPerformed

    private void cbInitialProcItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbInitialProcItemStateChanged
        selectInitProc();
    }//GEN-LAST:event_cbInitialProcItemStateChanged

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                CreateProcDialog dialog = new CreateProcDialog(new javax.swing.JFrame(), true);
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
    private javax.swing.JComboBox cbInitialProc;
    private javax.swing.JTextField fieldProcName;
    private javax.swing.JLabel labelCreateProc;
    private javax.swing.JLabel labelNameProc;
    // End of variables declaration//GEN-END:variables

}

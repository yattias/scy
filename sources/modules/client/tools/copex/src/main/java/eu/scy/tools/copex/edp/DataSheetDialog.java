/*
 * DataSheetDialog.java
 *
 * Created on 5 aoa�t 2008, 13:26
 */

package eu.scy.tools.copex.edp;

import eu.scy.tools.copex.common.DataSheet;
import eu.scy.tools.copex.common.LearnerProcedure;
import eu.scy.tools.copex.utilities.CopexReturn;
import eu.scy.tools.copex.utilities.CopexUtilities;
import eu.scy.tools.copex.utilities.MyConstants;
import java.util.ArrayList;
import javax.swing.*;

/**
 * fenetre de dialogue qui permet de creer un tableau pour la feuille de donnees
 * @author  MBO
 */
public class DataSheetDialog extends JDialog {

    // ATTRIBUTS
    /* fenetre mere */
    private EdPPanel edP;
    /* protocole */
    private LearnerProcedure proc;
    /* nombre de lignes */
    private int nbL;
    /* nombre de colonnes */
    private int nbC;
    /* mode creation ou modification */
    private boolean modeCreate;
    
    // CONSTRUCTEURS
    /** Creates new form DataSheetDialog */
    public DataSheetDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    /* constructeur sans donnees initiales */
    public DataSheetDialog(EdPPanel edP, LearnerProcedure proc) {
        super();
        this.edP = edP;
        this.proc = proc;
        this.modeCreate = true;
        this.nbC = 1;
        this.nbL = 1;
        this.setLocationRelativeTo(edP);
        initComponents();
        setModal(true);
        init();
    }
    /* constructeur sans donnees initiales */
    public DataSheetDialog(EdPPanel edP, LearnerProcedure proc,  int nbL, int nbC) {
        super();
        this.edP = edP;
        this.proc = proc;
        this.nbL = nbL;
        this.nbC = nbC;
        this.modeCreate = false;
        this.setLocationRelativeTo(edP);
        initComponents();
        init();
    }
    
    
    // METHODS
    private void init(){
        resizeElements();
        if (!modeCreate){
            this.setTitle(edP.getBundleString("TITLE_DIALOG_DATASHEET_M"));
            // initialisation des nombres de lignes / colonnes
            spinnerNbL.setValue(nbL);
            spinnerNbC.setValue(nbC);
        }
        repaint();
    }
    private void resizeElements(){
       // label nbl
       this.labelNbL.setSize(CopexUtilities.lenghtOfString(this.labelNbL.getText(), getFontMetrics(this.labelNbL.getFont())), this.labelNbL.getHeight());
       // label nbC
       this.labelNbC.setSize(CopexUtilities.lenghtOfString(this.labelNbC.getText(), getFontMetrics(this.labelNbC.getFont())), this.labelNbC.getHeight());
       // bouton Ok
       this.buttonOk.setSize(60+CopexUtilities.lenghtOfString(this.buttonOk.getText(), getFontMetrics(this.buttonOk.getFont())), this.buttonOk.getHeight());
       // bouton Annuler
       this.buttonCancel.setSize(60+CopexUtilities.lenghtOfString(this.buttonCancel.getText(), getFontMetrics(this.buttonCancel.getFont())), this.buttonCancel.getHeight());
   }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        labelNbL = new javax.swing.JLabel();
        labelNbC = new javax.swing.JLabel();
        spinnerNbL = new javax.swing.JSpinner();
        spinnerNbC = new javax.swing.JSpinner();
        buttonOk = new javax.swing.JButton();
        buttonCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(edP.getBundleString("TITLE_DIALOG_DATASHEET"));
        setMinimumSize(new java.awt.Dimension(300, 200));
        setModal(true);
        setResizable(false);
        getContentPane().setLayout(null);

        labelNbL.setFont(new java.awt.Font("Tahoma", 1, 11));
        labelNbL.setText(edP.getBundleString("LABEL_NB_ROWS"));
        labelNbL.setName("labelNbL"); // NOI18N
        getContentPane().add(labelNbL);
        labelNbL.setBounds(10, 20, 150, 14);

        labelNbC.setFont(new java.awt.Font("Tahoma", 1, 11));
        labelNbC.setText(edP.getBundleString("LABEL_NB_COLUMN"));
        labelNbC.setName("labelNbC"); // NOI18N
        getContentPane().add(labelNbC);
        labelNbC.setBounds(10, 60, 150, 14);

        spinnerNbL.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(1), Integer.valueOf(1), null, Integer.valueOf(1)));
        spinnerNbL.setName("spinnerNbL"); // NOI18N
        getContentPane().add(spinnerNbL);
        spinnerNbL.setBounds(170, 20, 40, 20);

        spinnerNbC.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(1), Integer.valueOf(1), null, Integer.valueOf(1)));
        spinnerNbC.setName("spinnerNbC"); // NOI18N
        getContentPane().add(spinnerNbC);
        spinnerNbC.setBounds(170, 60, 40, 20);

        buttonOk.setText(edP.getBundleString("BUTTON_OK"));
        buttonOk.setName("buttonOk"); // NOI18N
        buttonOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonOkActionPerformed(evt);
            }
        });
        getContentPane().add(buttonOk);
        buttonOk.setBounds(40, 110, 99, 23);

        buttonCancel.setText(edP.getBundleString("BUTTON_CANCEL"));
        buttonCancel.setName("buttonCancel"); // NOI18N
        buttonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonCancelActionPerformed(evt);
            }
        });
        getContentPane().add(buttonCancel);
        buttonCancel.setBounds(170, 110, 99, 23);

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void buttonOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonOkActionPerformed
    // on controle les donnees  
    int newNbR = 1;
    int newNbC = 1;
    try{
        newNbR =  ((Integer)(spinnerNbL.getValue())).intValue();
    }catch(Exception e){
        edP.displayError(new CopexReturn(edP.getBundleString("MSG_VALEUR_LIGNE_NUMERIQUE"), false), edP.getBundleString("TITLE_DIALOG_ERROR"));
        return;
    }
    if (newNbR < 1){
        edP.displayError(new CopexReturn(edP.getBundleString("MSG_VALEUR_LIGNE_SUP"), false) , edP.getBundleString("TITLE_DIALOG_ERROR"));
        return;
    }
    try{
        newNbC =  ((Integer)(spinnerNbC.getValue())).intValue();
    }catch(Exception e){
        edP.displayError(new CopexReturn(edP.getBundleString("MSG_VALEUR_COLONNE_NUMERIQUE"),false) , edP.getBundleString("TITLE_DIALOG_ERROR"));
        return;
    }
    if (newNbC < 1){
        edP.displayError(new CopexReturn(edP.getBundleString("MSG_VALEUR_COLONNE_SUP"), false) , edP.getBundleString("TITLE_DIALOG_ERROR"));
        return;
    }
    
    // mode creation 
    if (modeCreate){
        // on ajoute une ligne :
        newNbR += 1;
        ArrayList v = new ArrayList();
        CopexReturn r = edP.getController().createDataSheet(proc, newNbR, newNbC,MyConstants.NOT_UNDOREDO, v);
        if (r.isError()){
            edP.displayError(r , edP.getBundleString("TITLE_DIALOG_ERROR"));
        }
        DataSheet dataSheet = (DataSheet)v.get(0);
        edP.addEdit_createDataSheet(newNbR, newNbC, dataSheet);
    }else{
        // mode modif 
        if (newNbR != this.nbL || newNbC != this.nbC){
            boolean confirm = true;
            if (newNbR < nbL || newNbC < nbC){
                CopexReturn warningC = new CopexReturn(edP.getBundleString("MSG_CONFIRM_UPDATE_DATASHEET"), true);
                warningC.setConfirm(edP.getBundleString("MSG_CONFIRM_UPDATE_DATASHEET"));
                confirm = edP.displayError(warningC, edP.getBundleString("TITLE_DIALOG_WARNING"));
            }
            if (confirm){
                CopexReturn r = edP.getController().modifyDataSheet(proc, newNbR, newNbC, MyConstants.NOT_UNDOREDO);
                if (r.isError()){
                    edP.displayError(r , edP.getBundleString("TITLE_DIALOG_ERROR"));
                }
                edP.addEdit_updateDataSheet(proc.getDataSheet(), nbL, nbC, newNbR, newNbC);
            }
        }
    }
    this.dispose();
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
                DataSheetDialog dialog = new DataSheetDialog(new javax.swing.JFrame(), true);
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
    private javax.swing.JLabel labelNbC;
    private javax.swing.JLabel labelNbL;
    private javax.swing.JSpinner spinnerNbC;
    private javax.swing.JSpinner spinnerNbL;
    // End of variables declaration//GEN-END:variables

}

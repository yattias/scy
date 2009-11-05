/*
 * EditProcDialog.java
 *
 * Created on 20 aoat 2008, 12:53
 */

package eu.scy.tools.copex.edp;



import eu.scy.tools.copex.common.CopexMission;
import eu.scy.tools.copex.common.LearnerProcedure;
import eu.scy.tools.copex.controller.ControllerInterface;
import eu.scy.tools.copex.utilities.CopexReturn;
import eu.scy.tools.copex.utilities.CopexUtilities;
import eu.scy.tools.copex.utilities.MyConstants;
import java.awt.Dimension;
import java.awt.event.KeyEvent;

/**
 * fenetre de dialogue permettant de modifier le nom d'un protocole
 * on affiche pour info la mission du protocole
 * @author  MBO
 */
public class EditProcDialog extends javax.swing.JDialog {

    // ATTRIBUTS
    /* edP */
    private EdPPanel edP;
    /* controlleur */
    private ControllerInterface controller;
    /* droit sur la fenetre */
    private char right = MyConstants.EXECUTE_RIGHT;
    /* protocole */
    private LearnerProcedure proc;
    private boolean isMission;
    
    
    // CONSTRUCTEURS
    /** Creates new form EditProcDialog */
    public EditProcDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    public EditProcDialog(EdPPanel edP, boolean isMission, ControllerInterface controller, LearnerProcedure proc) {
        super();
        this.edP = edP;
        this.controller = controller;
        this.proc = (LearnerProcedure)proc.clone();
        this.right = proc.getRight();
        this.isMission = isMission;
        this.setLocationRelativeTo(edP);
        initComponents();
        setModal(true);
        setLocation(edP.getLocationDialog());
        init();
        setIconImage(edP.getIconDialog());
    }

    private void init(){
        // nom du protocole 
        textFieldProc.setText(this.proc.getName(edP.getLocale()));
        // mission : nom + description 
        CopexMission mission = proc.getMission();
        String m = mission.getName();
        if (mission.getSumUp() != null && mission.getSumUp().length() != 0)
            m += "\n"+mission.getSumUp();
        textAreaMission.setText(m);
        textAreaMission.setLineWrap(true);
        textAreaMission.setWrapStyleWord(true);
        resizeElements();
        if (right == MyConstants.NONE_RIGHT)
            setDisabled();
        if(!isMission){
            this.remove(labelMission);
            this.remove(jScrollPane1);
            labelMission = null;
            jScrollPane1 = null;
            textAreaMission = null;
            this.labelProc.setBounds(10,10,labelProc.getWidth(), labelProc.getHeight());
            this.textFieldProc.setBounds(labelProc.getX()+labelProc.getWidth()+5, labelProc.getY()-3, textFieldProc.getWidth(), textFieldProc.getHeight());
            if(buttonOk != null){
                buttonOk.setBounds(buttonOk.getX(), textFieldProc.getY()+textFieldProc.getHeight()+20, buttonOk.getWidth(), buttonOk.getHeight());
            }
            buttonCancel.setBounds(buttonCancel.getX(), textFieldProc.getY()+textFieldProc.getHeight()+20, buttonCancel.getWidth(), buttonCancel.getHeight());
            int h = buttonCancel.getY()+buttonCancel.getHeight()+40;
            Dimension dim = new Dimension(this.getWidth(), h);
            setMinimumSize(dim);
            setSize(dim);
            setPreferredSize(dim);
        }
        repaint();
    }
    
    
    /*
     * permet de resizer les elements de la fenetre en fonction de la longueur des textes
     * variable selon la langue
     */
   private void resizeElements(){
       // label mission
       this.labelMission .setSize(CopexUtilities.lenghtOfString(this.labelMission.getText(), getFontMetrics(this.labelMission.getFont())), 14);
       // label proc
       this.labelProc.setSize(CopexUtilities.lenghtOfString(this.labelProc.getText(), getFontMetrics(this.labelProc.getFont())), 14);
       // bouton Ok
       this.buttonOk.setSize(60+CopexUtilities.lenghtOfString(this.buttonOk.getText(), getFontMetrics(this.buttonOk.getFont())), 23);
       // bouton Annuler
       this.buttonCancel.setSize(60+CopexUtilities.lenghtOfString(this.buttonCancel.getText(), getFontMetrics(this.buttonCancel.getFont())), 23);
   }
   
   private void validDialog(){
        // recupere les donnees 
    // recupere les donnees : 
   String p = this.textFieldProc.getText();
   if (p.length() > MyConstants.MAX_LENGHT_PROC_NAME){
       String msg = edP.getBundleString("MSG_LENGHT_MAX");
       msg  = CopexUtilities.replace(msg, 0, edP.getBundleString("LABEL_PROC"));
       msg = CopexUtilities.replace(msg, 1, ""+MyConstants.MAX_LENGHT_PROC_NAME);
       edP.displayError(new CopexReturn(msg, false), edP.getBundleString("TITLE_DIALOG_ERROR")); 
       return;
   }
   if (p.length() == 0){
       String msg = edP.getBundleString("MSG_ERROR_FIELD_NULL");
       msg  = CopexUtilities.replace(msg, 0, edP.getBundleString("LABEL_PROC"));
       edP.displayError(new CopexReturn(msg ,false), edP.getBundleString("TITLE_DIALOG_ERROR")); 
       return;
   }
   if (!p.equals(proc.getName(edP.getLocale()))){
       CopexReturn cr = this.controller.updateProcName(proc, p, MyConstants.NOT_UNDOREDO);
       if (cr.isError()){
           edP.displayError(cr, edP.getBundleString("TITLE_DIALOG_ERROR"));
           return;
       }
       edP.addEdit_renameProc(proc, p);
   } // rien si le nom est identique
    this.dispose();
   }
   /* permet de rendre disabled tous les elements, ne laisse qu'un bouton pour fermer  */
    private void setDisabled(){
        this.textFieldProc.setEnabled(false);
        this.textAreaMission.setEnabled(false);
        // on ne laisse que le bouton annuler, on change le texte et on le centre
        this.remove(buttonOk);
        this.buttonCancel.setText(edP.getBundleString("BUTTON_OK"));
        this.buttonCancel.setBounds((this.getWidth() - this.buttonCancel.getWidth())/2, this.buttonCancel.getY(), this.buttonCancel.getWidth(), this.buttonCancel.getHeight());
    }
   
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        labelMission = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        textAreaMission = new javax.swing.JTextArea();
        labelProc = new javax.swing.JLabel();
        textFieldProc = new javax.swing.JTextField();
        buttonOk = new javax.swing.JButton();
        buttonCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(edP.getBundleString("TITLE_DIALOG_PROC"));
        setMinimumSize(new java.awt.Dimension(380, 230));
        setModal(true);
        setResizable(false);
        getContentPane().setLayout(null);

        labelMission.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        labelMission.setText(edP.getBundleString("LABEL_MISSION"));
        labelMission.setName("labelMission"); // NOI18N
        getContentPane().add(labelMission);
        labelMission.setBounds(20, 10, 75, 14);

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        textAreaMission.setColumns(20);
        textAreaMission.setEditable(false);
        textAreaMission.setLineWrap(true);
        textAreaMission.setRows(5);
        textAreaMission.setWrapStyleWord(true);
        textAreaMission.setEnabled(false);
        textAreaMission.setName("textAreaMission"); // NOI18N
        textAreaMission.setPreferredSize(new java.awt.Dimension(300, 70));
        jScrollPane1.setViewportView(textAreaMission);

        getContentPane().add(jScrollPane1);
        jScrollPane1.setBounds(110, 10, 250, 90);

        labelProc.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        labelProc.setText(edP.getBundleString("LABEL_PROC"));
        labelProc.setName("labelProc"); // NOI18N
        getContentPane().add(labelProc);
        labelProc.setBounds(20, 110, 75, 14);

        textFieldProc.setName("textFieldProc"); // NOI18N
        textFieldProc.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                textFieldProcKeyReleased(evt);
            }
        });
        getContentPane().add(textFieldProc);
        textFieldProc.setBounds(110, 110, 250, 20);

        buttonOk.setText(edP.getBundleString("BUTTON_OK"));
        buttonOk.setName("buttonOk"); // NOI18N
        buttonOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonOkActionPerformed(evt);
            }
        });
        getContentPane().add(buttonOk);
        buttonOk.setBounds(60, 160, 99, 23);

        buttonCancel.setText(edP.getBundleString("BUTTON_CANCEL"));
        buttonCancel.setName("buttonCancel"); // NOI18N
        buttonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonCancelActionPerformed(evt);
            }
        });
        getContentPane().add(buttonCancel);
        buttonCancel.setBounds(220, 160, 99, 23);

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void buttonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonCancelActionPerformed
    this.dispose();
}//GEN-LAST:event_buttonCancelActionPerformed

private void buttonOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonOkActionPerformed
   validDialog();
}//GEN-LAST:event_buttonOkActionPerformed

private void textFieldProcKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textFieldProcKeyReleased
    if (evt.getKeyCode() == KeyEvent.VK_ENTER)
        validDialog();
}//GEN-LAST:event_textFieldProcKeyReleased

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                EditProcDialog dialog = new EditProcDialog(new javax.swing.JFrame(), true);
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
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel labelMission;
    private javax.swing.JLabel labelProc;
    private javax.swing.JTextArea textAreaMission;
    private javax.swing.JTextField textFieldProc;
    // End of variables declaration//GEN-END:variables

}

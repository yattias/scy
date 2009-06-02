/*
 * StepDialog.java
 *
 * Created on 28 juillet 2008, 15:35
 */

package eu.scy.tools.copex.edp;

import eu.scy.tools.copex.common.Step;
import eu.scy.tools.copex.utilities.ActionComment;
import eu.scy.tools.copex.utilities.CommentsPanel;
import eu.scy.tools.copex.utilities.CopexReturn;
import eu.scy.tools.copex.utilities.CopexUtilities;
import eu.scy.tools.copex.utilities.MyConstants;
import javax.swing.*;

/**
 * fenetre d'ajout d'une etape
 * @author  MBO
 */
public class StepDialog extends JDialog implements ActionComment{
    // ATTRIBUTS
    private EdPPanel edP;
     /* mode de visualisation  : ajout / modification */
    private boolean modeAdd;
    /* droit sur la fenetre */
    private char right = MyConstants.EXECUTE_RIGHT;
    /* droit proc */
    private char procRight = MyConstants.EXECUTE_RIGHT;
    /* nom etape */
    private String name;
    /* commentaires */
    private String comments;
    /* icone */
    private ImageIcon taskImage;
    /* panelComments */
    private CommentsPanel panelComments;
    /* commentaire en cours */
    private String comment;
    
    
    /** Creates new form StepDialog */
    public StepDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    public StepDialog(EdPPanel edP) {
        super();
        this.edP = edP;
        this.modeAdd = true;
        this.comment = "";
        this.setLocationRelativeTo(edP);
        initComponents();
        setModal(true);
        setLocation(edP.getLocationDialog());
        init();
    }

     /* constructeur de la fenetre d'edition de l'action */
    public StepDialog(EdPPanel edP, boolean modeAdd, String name, String comments, ImageIcon taskImage, char right, char procRight ) {
        super();
        this.edP = edP;
        this.modeAdd = modeAdd;
        this.name = name;
        this.comments = comments;
        this.right = right;
        this.procRight = procRight;
        this.taskImage = taskImage;
        this.setLocationRelativeTo(edP);
        initComponents();
        setModal(true);
        setLocation(edP.getLocationDialog());
        init();
    }
    public void init(){
        getContentPane().add(getPanelComments());
        if (!modeAdd){
            // mode edit
            this.setTitle(edP.getBundleString("TITLE_DIALOG_STEP"));
            this.textAreaDescription.setText(this.name);
            this.panelComments.setComments(this.comments);
            this.comment = this.comments ;
            // image 
            if (this.taskImage != null ){
                this.labelImage.setIcon(this.taskImage);
            }
            // gestion des droits 
            if (right == MyConstants.NONE_RIGHT)
               setDisabled();
            if (procRight == MyConstants.NONE_RIGHT)
                setAllDisabled();
        }
        textAreaDescription.setLineWrap(true);
        textAreaDescription.setWrapStyleWord(true);
        
        resizeElements();
        actionComment();
    }

    private CommentsPanel getPanelComments(){
        JPanel p = new JPanel();
        p.setSize(320,150);
        p.setPreferredSize(getSize());
        panelComments = new CommentsPanel(edP, p, edP.getBundleString("LABEL_ADD_COMMENT"), 300);
        panelComments.addActionComment(this);
        panelComments.setBounds(20,120,300,panelComments.getHeight());
        return panelComments ;
    }

    /* permet de rendre disabled tous les elements, ne laisse qu'un bouton pour fermer 
     * MBO le 09/10/08/ : seul le champ description n'est pas accessible
     */
    private void setDisabled(){
        this.textAreaDescription.setEnabled(false);
    }
    
    private void setAllDisabled(){
        this.textAreaDescription.setEnabled(false);
        this.panelComments.setEnabled(false);
        remove(this.buttonOk);
        this.buttonOk = null;
        this.buttonCancel.setText(edP.getBundleString("BUTTON_OK"));
        this.buttonCancel.setSize(60+CopexUtilities.lenghtOfString(this.buttonCancel.getText(), getFontMetrics(this.buttonCancel.getFont())), this.buttonCancel.getHeight());
        this.buttonCancel.setBounds((this.getWidth()- this.buttonCancel.getWidth()) /2 , this.buttonCancel.getY(), this.buttonCancel.getWidth(), this.buttonCancel.getHeight());
    
    }
    
    /*
     * permet de resizer les elements de la fenetre en fonction de la longueur des textes
     * variable selon la langue
     */
   private void resizeElements(){
       // label nom
       this.labelName.setSize(CopexUtilities.lenghtOfString(this.labelName.getText(), getFontMetrics(this.labelName.getFont())), this.labelName.getHeight());
        // label image 
       if (this.taskImage != null )
        this.labelImage.setSize(this.taskImage.getIconWidth(), this.taskImage.getIconHeight());
       if (procRight != MyConstants.NONE_RIGHT){
        // bouton Ok
        this.buttonOk.setSize(60+CopexUtilities.lenghtOfString(this.buttonOk.getText(), getFontMetrics(this.buttonOk.getFont())), this.buttonOk.getHeight());
        // bouton Annuler
        this.buttonCancel.setSize(60+CopexUtilities.lenghtOfString(this.buttonCancel.getText(), getFontMetrics(this.buttonCancel.getFont())), this.buttonCancel.getHeight());
       }
       if (this.taskImage != null){
           int height = this.taskImage.getIconHeight() + 20; 
           int maxWidth = Math.max(this.getWidth(), this.taskImage.getIconWidth()+this.labelImage.getX());
           this.buttonOk.setBounds(this.buttonOk.getX(), this.buttonOk.getY()+ height, this.buttonOk.getWidth(), this.buttonOk.getHeight());
           this.buttonCancel.setBounds(this.buttonCancel.getX(), this.buttonCancel.getY() + height, this.buttonCancel.getWidth(), this.buttonCancel.getHeight());
           this.setSize(maxWidth, this.getHeight() +height);
           this.setPreferredSize(this.getSize());
       }
   }
   
   private void validDialog(){
       this.panelComments.setPanelDetailsShown();
        // recupere les données : 
   String d = this.textAreaDescription.getText();
   if (d.length() > MyConstants.MAX_LENGHT_TASK_DESCRIPTION){
       String msg = edP.getBundleString("MSG_LENGHT_MAX");
       msg  = CopexUtilities.replace(msg, 0, edP.getBundleString("LABEL_DESCRIPTION"));
       msg = CopexUtilities.replace(msg, 1, ""+MyConstants.MAX_LENGHT_TASK_DESCRIPTION);
       edP.displayError(new CopexReturn(msg, false), edP.getBundleString("TITLE_DIALOG_ERROR")); 
       return;
   }
   if (d.length() == 0){
       String msg = edP.getBundleString("MSG_ERROR_FIELD_NULL");
       msg  = CopexUtilities.replace(msg, 0, edP.getBundleString("LABEL_DESCRIPTION"));
       edP.displayError(new CopexReturn(msg ,false), edP.getBundleString("TITLE_DIALOG_ERROR")); 
       return;
   }
   String c = this.panelComments.getComments();
   if (c.length() > MyConstants.MAX_LENGHT_TASK_COMMENTS){
       String msg = edP.getBundleString("MSG_LENGHT_MAX");
       msg  = CopexUtilities.replace(msg, 0, edP.getBundleString("LABEL_COMMENTS"));
       msg = CopexUtilities.replace(msg, 1, ""+MyConstants.MAX_LENGHT_TASK_COMMENTS);
       edP.displayError(new CopexReturn(msg, false) , edP.getBundleString("TITLE_DIALOG_ERROR")); 
       return;
   }
   Step newStep = new Step(d, c) ;
   if (modeAdd){
        // Créé l'étpae 
        CopexReturn cr = edP.addStep(newStep);
        if (cr.isError()){
            edP.displayError( cr , edP.getBundleString("TITLE_DIALOG_ERROR")); 
            return;
        }
   }else{
       // mode modification
       CopexReturn cr = edP.updateStep(newStep);
        if (cr.isError()){
           edP.displayError(cr, edP.getBundleString("TITLE_DIALOG_ERROR")); 
            return;
        }
   }
  
    this.dispose();
   }

   /* sauvegarde des commentaires */
    public void saveComment(){
        this.comment = panelComments.getComments() ;
    }

    /* met à jour le texte des commenraires */
    public void setComment(){
        this.panelComments.setComments(this.comment);
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
        buttonOk = new javax.swing.JButton();
        buttonCancel = new javax.swing.JButton();
        labelImage = new javax.swing.JLabel();
        jScrollPaneDescription = new javax.swing.JScrollPane();
        textAreaDescription = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(edP.getBundleString("TITLE_DIALOG_ADD_STEP"));
        setIconImage(null);
        setMinimumSize(new java.awt.Dimension(350, 250));
        setModal(true);
        setResizable(false);
        getContentPane().setLayout(null);

        labelName.setFont(new java.awt.Font("Tahoma", 1, 11));
        labelName.setText(edP.getBundleString("LABEL_NAME_STEP"));
        labelName.setName("labelName"); // NOI18N
        getContentPane().add(labelName);
        labelName.setBounds(20, 20, 75, 14);

        buttonOk.setText(edP.getBundleString("BUTTON_OK"));
        buttonOk.setName("buttonOk"); // NOI18N
        buttonOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonOkActionPerformed(evt);
            }
        });
        getContentPane().add(buttonOk);
        buttonOk.setBounds(40, 280, 99, 23);

        buttonCancel.setText(edP.getBundleString("BUTTON_CANCEL"));
        buttonCancel.setName("buttonCancel"); // NOI18N
        buttonCancel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                buttonCancelMouseClicked(evt);
            }
        });
        buttonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonCancelActionPerformed(evt);
            }
        });
        getContentPane().add(buttonCancel);
        buttonCancel.setBounds(200, 280, 99, 23);

        labelImage.setName("labelImage"); // NOI18N
        getContentPane().add(labelImage);
        labelImage.setBounds(30, 250, 30, 20);

        jScrollPaneDescription.setName("jScrollPaneDescription"); // NOI18N

        textAreaDescription.setColumns(20);
        textAreaDescription.setLineWrap(true);
        textAreaDescription.setRows(3);
        textAreaDescription.setName("textAreaDescription"); // NOI18N
        jScrollPaneDescription.setViewportView(textAreaDescription);

        getContentPane().add(jScrollPaneDescription);
        jScrollPaneDescription.setBounds(20, 40, 300, 70);

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void buttonCancelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_buttonCancelMouseClicked
// TODO add your handling code here:
}//GEN-LAST:event_buttonCancelMouseClicked

private void buttonOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonOkActionPerformed

   validDialog();
}//GEN-LAST:event_buttonOkActionPerformed

private void buttonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonCancelActionPerformed
// TODO add your handling code here:
    this.dispose();
}//GEN-LAST:event_buttonCancelActionPerformed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                StepDialog dialog = new StepDialog(new javax.swing.JFrame(), true);
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
    private javax.swing.JScrollPane jScrollPaneDescription;
    private javax.swing.JLabel labelImage;
    private javax.swing.JLabel labelName;
    private javax.swing.JTextArea textAreaDescription;
    // End of variables declaration//GEN-END:variables

    public void actionComment() {
           if (this.buttonOk != null)
                this.buttonOk.setBounds(this.buttonOk.getX(), panelComments.getHeight()+panelComments.getY()+20, this.buttonOk.getWidth(), this.buttonOk.getHeight());
           this.buttonCancel.setBounds(this.buttonCancel.getX(), panelComments.getHeight()+panelComments.getY()+20, this.buttonCancel.getWidth(), this.buttonCancel.getHeight());
           int h = this.buttonCancel.getY() + this.buttonCancel.getHeight() +50;
           this.setSize(this.getWidth(), h);
           this.setPreferredSize(this.getSize());
           this.repaint();
    }

}

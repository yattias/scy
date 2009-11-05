/*
 * QuestionDialog.java
 *
 * Created on 5 aoat 2008, 08:09
 */

package eu.scy.tools.copex.edp;



import eu.scy.tools.copex.common.Question;
import eu.scy.tools.copex.utilities.ActionComment;
import eu.scy.tools.copex.utilities.CommentsPanel;
import eu.scy.tools.copex.utilities.CopexReturn;
import eu.scy.tools.copex.utilities.CopexUtilities;
import eu.scy.tools.copex.utilities.MyConstants;
import java.awt.Color;
import java.awt.Font;
import javax.swing.*;
/**
 * fenetre de dialogue qui permet d'ajouter une sous question ou d'editer une
 * question ou une sous question
 * @author  MBO
 */
public class QuestionDialog extends JDialog implements ActionComment {

    // CONSTANTE 
    private static final Font defaultTextFont=  new Font("Monospaced", Font.ITALIC, 11);
    private static final Color defaultTextColor = new Color(153,153,153);
    private static final Font areaFont = new Font("Monospaced", Font.PLAIN, 11);
    private static final Color areaTextColor = Color.black;

    // ATTRIBUTS
    /* fenetre mere */
    private EdPPanel edP;
    /* mode creation ou edition */
    private boolean modeAdd;
    /* sous question */
    private String question;
    /* commentaires */
    private String comments;
    /* droit sur la fenetre */
    private char right = MyConstants.EXECUTE_RIGHT;
    /* droit proc*/
    private char procRight = MyConstants.EXECUTE_RIGHT;
     /* panelComments */
    private CommentsPanel panelComments;
    /* commentaire en cours */
    private String comment;
    
    // CONSTRUCTEURS 
    /** Creates new form QuestionDialog */
    public QuestionDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    // mode ajout d'une sous question
    public QuestionDialog(EdPPanel edP) {
        super();
        this.edP = edP;
        this.modeAdd = true;
        this.setLocationRelativeTo(edP);
        initComponents();
        setModal(true);
        setLocation(edP.getLocationDialog());
        init();
        setIconImage(edP.getIconDialog());
    }

    // constructeur edition
    public QuestionDialog(EdPPanel edP, char right, boolean modeAdd,  String question, String comments, char procRight) {
        super();
        this.edP = edP;
        this.right = right;
        this.modeAdd = modeAdd;
        this.question = question;
        this.comments = comments;
        this.procRight = procRight;
        this.comment = "";
        if(edP.isShowing())
            this.setLocationRelativeTo(edP);
        initComponents();
        setModal(true);
        if(edP.isShowing())
            setLocation(edP.getLocationDialog());
        init();
    }

    // METHODES 
    private void init(){
        setSize(340,400);
        setPreferredSize(getSize());
        getContentPane().add(getPanelComments());
        if (!modeAdd){
            // mode edit
            this.setTitle(edP.getBundleString("TITLE_DIALOG_QUESTION"));
            this.textAreaDescription.setText(this.question);
            this.panelComments.setComments(this.comments);
            this.comment = this.comments ;
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
        setResizable(false);
        if(buttonOk != null)
            buttonOk.requestFocus();
        setDefaultText();
        repaint();
    }

   
    private CommentsPanel getPanelComments(){
        JPanel p = new JPanel();
        p.setSize(320,150);
        p.setPreferredSize(getSize());
        panelComments = new CommentsPanel(edP, p, edP.getBundleString("LABEL_ADD_COMMENT"), 300);
        panelComments.addActionComment(this);
        panelComments.setBounds(20,jScrollPaneDescription.getY()+jScrollPaneDescription.getHeight()+20,300,panelComments.getHeight());
        return panelComments ;
    }
    
    /* permet de rendre disabled tous les elements, ne laisse qu'un bouton pour fermer 
     * MBO le 09/10/08 : seul la description n'est pas accessible
     */
    private void setDisabled(){
        this.textAreaDescription.setEnabled(false);
        this.textAreaDescription.setFont(new Font("Monospaced", Font.BOLD, 11));
        this.textAreaDescription.setBackground(new Color(240,240,240));
    }
    private void setAllDisabled(){
        this.textAreaDescription.setEnabled(false);
        this.textAreaDescription.setFont(new Font("Monospaced", Font.BOLD, 11));
        this.textAreaDescription.setBackground(new Color(240,240,240));
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
       // label question
       this.labelQuestion.setSize(CopexUtilities.lenghtOfString(this.labelQuestion.getText(), getFontMetrics(this.labelQuestion.getFont())), this.labelQuestion.getHeight());
       if (this.procRight != MyConstants.NONE_RIGHT){
            // bouton Ok
            this.buttonOk.setSize(60+CopexUtilities.lenghtOfString(this.buttonOk.getText(), getFontMetrics(this.buttonOk.getFont())), this.buttonOk.getHeight());
            // bouton Annuler
            this.buttonCancel.setSize(60+CopexUtilities.lenghtOfString(this.buttonCancel.getText(), getFontMetrics(this.buttonCancel.getFont())), this.buttonCancel.getHeight());
       }
           // remonte les boutons 
           int m = 50;
           if (this.procRight != MyConstants.NONE_RIGHT){
            this.buttonOk.setBounds(this.buttonOk.getX(), this.buttonOk.getY() - m, this.buttonOk.getWidth(), this.buttonOk.getHeight());
           }
           this.buttonCancel.setBounds(this.buttonCancel.getX(), this.buttonCancel.getY() - m, this.buttonCancel.getWidth(), this.buttonCancel.getHeight());
           this.setSize(this.getWidth(), this.getHeight() - m);
           this.setPreferredSize(this.getSize());
   }
    
    private void validDialog(){
        removeDefaultText();
        this.panelComments.setPanelDetailsShown();
        // recupere les donnees : 
        String d = this.textAreaDescription.getText();
        if (d.length() > MyConstants.MAX_LENGHT_TASK_DESCRIPTION){
           String msg = edP.getBundleString("MSG_LENGHT_MAX");
            msg  = CopexUtilities.replace(msg, 0, edP.getBundleString("LABEL_QUESTION"));
            msg = CopexUtilities.replace(msg, 1, ""+MyConstants.MAX_LENGHT_TASK_DESCRIPTION);
            edP.displayError(new CopexReturn(msg ,false), edP.getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }
        if (d.length() == 0){
            String msg = edP.getBundleString("MSG_ERROR_FIELD_NULL");
            msg  = CopexUtilities.replace(msg, 0, edP.getBundleString("LABEL_QUESTION"));
            edP.displayError(new CopexReturn(msg ,false), edP.getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }
        String c = this.panelComments.getComments();
        if (c.length() > MyConstants.MAX_LENGHT_TASK_COMMENTS){
            String msg = edP.getBundleString("MSG_LENGHT_MAX");
            msg  = CopexUtilities.replace(msg, 0, edP.getBundleString("LABEL_COMMENTS"));
            msg = CopexUtilities.replace(msg, 1, ""+MyConstants.MAX_LENGHT_TASK_COMMENTS);
            edP.displayError(new CopexReturn(msg ,false), edP.getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }
        Question newQuestion = new Question(edP.getLocale(),d, c) ;
        CopexReturn cr = edP.updateQuestion(newQuestion);
        if (cr.isError()){
            edP.displayError(cr ,edP.getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }
        this.dispose();
    }

    /* affichage ou non des commentaires*/
    @Override
    public void actionComment() {
           if (this.buttonOk != null)
                this.buttonOk.setBounds(this.buttonOk.getX(), panelComments.getHeight()+panelComments.getY()+20, this.buttonOk.getWidth(), this.buttonOk.getHeight());
           this.buttonCancel.setBounds(this.buttonCancel.getX(), panelComments.getHeight()+panelComments.getY()+20, this.buttonCancel.getWidth(), this.buttonCancel.getHeight());
           int h = this.buttonCancel.getY() + this.buttonCancel.getHeight() +50;
           this.setSize(this.getWidth(), h);
           this.setPreferredSize(this.getSize());
           this.repaint();
    }

    /* sauvegarde des commentaires */
    @Override
    public void saveComment(){
        this.comment = panelComments.getComments() ;
    }

    /* met a jour le texte des commenraires */
    @Override
    public void setComment(){
        this.panelComments.setComments(this.comment);
    }

    /* affiche texte par defaut */
    private void setDefaultText(){
        String s = textAreaDescription.getText();
        if (s == null || s.length() == 0 || s.trim().length() == 0){
            textAreaDescription.setFont(defaultTextFont);
            textAreaDescription.setForeground(defaultTextColor);
            textAreaDescription.setText(edP.getBundleString("DEFAULT_TEXT_QUESTION"));
        }
    }
    /* enleve texte par default description */
    private void removeDefaultTextQuestion(){
        String s = textAreaDescription.getText();
        if (s != null && s.equals(edP.getBundleString("DEFAULT_TEXT_QUESTION"))){
            textAreaDescription.setText("");
        }
        textAreaDescription.setFont(areaFont);
        textAreaDescription.setForeground(areaTextColor);
    }
    

    /*enleve les textes par default */
    private void removeDefaultText(){
        removeDefaultTextQuestion();
    }

    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonOk = new javax.swing.JButton();
        buttonCancel = new javax.swing.JButton();
        labelQuestion = new javax.swing.JLabel();
        jScrollPaneDescription = new javax.swing.JScrollPane();
        textAreaDescription = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(edP.getBundleString("TITLE_DIALOG_QUESTION"));
        setMinimumSize(new java.awt.Dimension(200, 200));
        setModal(true);
        getContentPane().setLayout(null);

        buttonOk.setText(edP.getBundleString("BUTTON_OK"));
        buttonOk.setName("buttonOk"); // NOI18N
        buttonOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonOkActionPerformed(evt);
            }
        });
        getContentPane().add(buttonOk);
        buttonOk.setBounds(60, 170, 99, 23);

        buttonCancel.setText(edP.getBundleString("BUTTON_CANCEL"));
        buttonCancel.setName("buttonCancel"); // NOI18N
        buttonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonCancelActionPerformed(evt);
            }
        });
        getContentPane().add(buttonCancel);
        buttonCancel.setBounds(190, 170, 99, 23);

        labelQuestion.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        labelQuestion.setText(edP.getBundleString("LABEL_QUESTION"));
        labelQuestion.setName("labelQuestion"); // NOI18N
        getContentPane().add(labelQuestion);
        labelQuestion.setBounds(20, 10, 90, 14);

        jScrollPaneDescription.setName("jScrollPaneDescription"); // NOI18N
        jScrollPaneDescription.setPreferredSize(new java.awt.Dimension(146, 90));

        textAreaDescription.setColumns(20);
        textAreaDescription.setRows(3);
        textAreaDescription.setName("textAreaDescription"); // NOI18N
        textAreaDescription.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                textAreaDescriptionFocusGained(evt);
            }
        });
        jScrollPaneDescription.setViewportView(textAreaDescription);

        getContentPane().add(jScrollPaneDescription);
        jScrollPaneDescription.setBounds(20, 30, 300, 80);

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void buttonOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonOkActionPerformed
   validDialog();
}//GEN-LAST:event_buttonOkActionPerformed

private void buttonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonCancelActionPerformed
    this.dispose();
}//GEN-LAST:event_buttonCancelActionPerformed

private void textAreaDescriptionFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_textAreaDescriptionFocusGained
    removeDefaultTextQuestion();
}//GEN-LAST:event_textAreaDescriptionFocusGained

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                QuestionDialog dialog = new QuestionDialog(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
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
    private javax.swing.JLabel labelQuestion;
    private javax.swing.JTextArea textAreaDescription;
    // End of variables declaration//GEN-END:variables

    

}

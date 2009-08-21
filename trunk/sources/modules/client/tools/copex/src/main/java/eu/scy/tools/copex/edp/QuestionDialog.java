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
    /* hypotheses */
    private String hypothesis;
    /* commentaires */
    private String comments;
    /* principe generaux */
    private String principle;
    /* image */
    private ImageIcon taskImage;
    /* droit sur la fenetre */
    private char right = MyConstants.EXECUTE_RIGHT;
    /* droit proc*/
    private char procRight = MyConstants.EXECUTE_RIGHT;
     /* panelComments */
    private CommentsPanel panelComments;
    /*boolean qui indique si affiche hypotheses ou non */
    private boolean displayHypothesis;
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
        this.displayHypothesis = edP.isDisplayHypothesis();
        init();
    }

    // constructeur edition
    public QuestionDialog(EdPPanel edP, char right, boolean modeAdd,  String question, String hypothesis, String comments, String principle, ImageIcon taskImage, char procRight) {
        super();
        this.edP = edP;
        this.right = right;
        this.modeAdd = modeAdd;
        this.question = question;
        this.hypothesis = hypothesis;
        this.principle = principle;
        this.comments = comments;
        this.taskImage = taskImage ;
        this.procRight = procRight;
        this.comment = "";
        this.displayHypothesis = edP.isDisplayHypothesis();
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
        setSize(340,600);
        setPreferredSize(getSize());
        getContentPane().add(getPanelComments());
        if (!modeAdd){
            // mode edit
            this.setTitle(edP.getBundleString("TITLE_DIALOG_QUESTION"));
            this.textAreaDescription.setText(this.question);
            if(displayHypothesis)
                this.textAreaHypothesis.setText(this.hypothesis);
            this.panelComments.setComments(this.comments);
            this.comment = this.comments ;
            this.textAreaGeneralPrinciple.setText(this.principle);
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
        textAreaGeneralPrinciple.setLineWrap(true);
        textAreaGeneralPrinciple.setWrapStyleWord(true);
        textAreaHypothesis.setLineWrap(true);
        textAreaHypothesis.setWrapStyleWord(true);
        if (!displayHypothesis){
            remove(this.scrollPaneHypothesis);
            remove(this.labelHypothesis);
            labelHypothesis = null;
            scrollPaneHypothesis = null;
            textAreaHypothesis = null;
        }
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
        panelComments.setBounds(20,370,300,panelComments.getHeight());
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
        this.textAreaGeneralPrinciple.setEnabled(false);
        this.textAreaGeneralPrinciple.setFont(new Font("Monospaced", Font.BOLD, 11));
        this.textAreaGeneralPrinciple.setBackground(new Color(240,240,240));
        if(displayHypothesis){
            this.textAreaHypothesis.setEnabled(false);
            this.textAreaHypothesis.setFont(new Font("Monospaced", Font.BOLD, 11));
            this.textAreaHypothesis.setBackground(new Color(240,240,240));
        }
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
       // label hypothese
       if(displayHypothesis){
        this.labelHypothesis.setSize(CopexUtilities.lenghtOfString(this.labelHypothesis.getText(), getFontMetrics(this.labelHypothesis.getFont())), this.labelHypothesis.getHeight());
       }
       this.labelGeneralPrinciple.setSize(CopexUtilities.lenghtOfString(this.labelGeneralPrinciple.getText(), getFontMetrics(this.labelGeneralPrinciple.getFont())), this.labelGeneralPrinciple.getHeight());
       // label image 
       if (this.taskImage != null )
            this.labelImage.setSize(this.taskImage.getIconWidth(), this.taskImage.getIconHeight());
       if (this.procRight != MyConstants.NONE_RIGHT){
            // bouton Ok
            this.buttonOk.setSize(60+CopexUtilities.lenghtOfString(this.buttonOk.getText(), getFontMetrics(this.buttonOk.getFont())), this.buttonOk.getHeight());
            // bouton Annuler
            this.buttonCancel.setSize(60+CopexUtilities.lenghtOfString(this.buttonCancel.getText(), getFontMetrics(this.buttonCancel.getFont())), this.buttonCancel.getHeight());
       }
       if (this.taskImage == null){
           // remonte les boutons 
           int m = 50;
           if (this.procRight != MyConstants.NONE_RIGHT){
            this.buttonOk.setBounds(this.buttonOk.getX(), this.buttonOk.getY() - m, this.buttonOk.getWidth(), this.buttonOk.getHeight());
           }
           this.buttonCancel.setBounds(this.buttonCancel.getX(), this.buttonCancel.getY() - m, this.buttonCancel.getWidth(), this.buttonCancel.getHeight());
           this.setSize(this.getWidth(), this.getHeight() - m);
           this.setPreferredSize(this.getSize());
       }
       if(!displayHypothesis){
           int m = 130;
           this.labelGeneralPrinciple.setBounds(this.labelGeneralPrinciple.getX(), this.labelGeneralPrinciple.getY()-m, this.labelGeneralPrinciple.getWidth(), this.labelGeneralPrinciple.getHeight());
           this.scrollPaneGeneralPrinciple.setBounds(this.scrollPaneGeneralPrinciple.getX(), this.scrollPaneGeneralPrinciple.getY()-m, this.scrollPaneGeneralPrinciple.getWidth(), this.scrollPaneGeneralPrinciple.getHeight());
           this.panelComments.setBounds(this.panelComments.getX(), this.panelComments.getY()-m, this.panelComments.getWidth(), this.panelComments.getHeight());
           if (this.procRight != MyConstants.NONE_RIGHT){
            this.buttonOk.setBounds(this.buttonOk.getX(), this.buttonOk.getY() - m, this.buttonOk.getWidth(), this.buttonOk.getHeight());
           }
           this.buttonCancel.setBounds(this.buttonCancel.getX(), this.buttonCancel.getY() - m, this.buttonCancel.getWidth(), this.buttonCancel.getHeight());
           this.setSize(this.getWidth(), this.getHeight() - m);
           this.setPreferredSize(this.getSize());
       }
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
   String h = "";
   if (displayHypothesis){
       h = this.textAreaHypothesis.getText();
        if (h.length() > MyConstants.MAX_LENGHT_QUESTION_HYPOTHESIS){
            String msg = edP.getBundleString("MSG_LENGHT_MAX");
            msg  = CopexUtilities.replace(msg, 0, edP.getBundleString("LABEL_HYPOTHESIS"));
            msg = CopexUtilities.replace(msg, 1, ""+MyConstants.MAX_LENGHT_QUESTION_HYPOTHESIS);
            edP.displayError(new CopexReturn(msg ,false), edP.getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }
   }
   Question newQuestion = new Question(d, h, c) ;
   String p = "";
   p = this.textAreaGeneralPrinciple.getText();
   if (p.length() > MyConstants.MAX_LENGHT_QUESTION_GENERAL_PRINCIPLE){
        String msg = edP.getBundleString("MSG_LENGHT_MAX");
        msg  = CopexUtilities.replace(msg, 0, edP.getBundleString("LABEL_GENERAL_PRINCIPLE"));
        msg = CopexUtilities.replace(msg, 1, ""+MyConstants.MAX_LENGHT_QUESTION_GENERAL_PRINCIPLE);
        edP.displayError(new CopexReturn(msg ,false), edP.getBundleString("TITLE_DIALOG_ERROR")); 
        return; 
    }
   newQuestion.setGeneralPrinciple(p);
   if (modeAdd){
        // Cree la question 
        CopexReturn cr = edP.addQuestion(newQuestion);
        if (cr.isError()){
            edP.displayError(cr , edP.getBundleString("TITLE_DIALOG_ERROR")); 
            return;
        }
   }else{
       // mode modification
       CopexReturn cr = edP.updateQuestion(newQuestion);
        if (cr.isError()){
            edP.displayError(cr ,edP.getBundleString("TITLE_DIALOG_ERROR")); 
            return;
        }
   }
  
    this.dispose();
    }

    /* affichage ou non des commentaires*/
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
    public void saveComment(){
        this.comment = panelComments.getComments() ;
    }

    /* met a jour le texte des commenraires */
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
        if(displayHypothesis){
            s = textAreaHypothesis.getText();
            if (s == null || s.length() == 0){
                textAreaHypothesis.setFont(defaultTextFont);
                textAreaHypothesis.setForeground(defaultTextColor);
                textAreaHypothesis.setText(edP.getBundleString("DEFAULT_TEXT_HYPOTHESIS"));
            }
        }
        s = textAreaGeneralPrinciple.getText();
        if (s == null || s.length() == 0){
            textAreaGeneralPrinciple.setFont(defaultTextFont);
            textAreaGeneralPrinciple.setForeground(defaultTextColor);
            textAreaGeneralPrinciple.setText(edP.getBundleString("DEFAULT_TEXT_PRINCIPLE"));
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
    /* enleve texte par default hypotheses */
    private void removeDefaultTextHypothesis(){
        if (!displayHypothesis)
            return;
        String s = textAreaHypothesis.getText();
        if (s != null && s.equals(edP.getBundleString("DEFAULT_TEXT_HYPOTHESIS"))){
            textAreaHypothesis.setText("");
        }
        textAreaHypothesis.setFont(areaFont);
        textAreaHypothesis.setForeground(areaTextColor);
    }
    /* enleve texte par default principle */
    private void removeDefaultTextPrinciple(){
        String s = textAreaGeneralPrinciple.getText();
        if (s != null && s.equals(edP.getBundleString("DEFAULT_TEXT_PRINCIPLE"))){
            textAreaGeneralPrinciple.setText("");
        }
        textAreaGeneralPrinciple.setFont(areaFont);
        textAreaGeneralPrinciple.setForeground(areaTextColor);
    }

    /*enleve les textes par default */
    private void removeDefaultText(){
        removeDefaultTextQuestion();
        removeDefaultTextHypothesis();
        removeDefaultTextPrinciple();
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
        labelHypothesis = new javax.swing.JLabel();
        labelGeneralPrinciple = new javax.swing.JLabel();
        scrollPaneGeneralPrinciple = new javax.swing.JScrollPane();
        textAreaGeneralPrinciple = new javax.swing.JTextArea();
        labelImage = new javax.swing.JLabel();
        scrollPaneHypothesis = new javax.swing.JScrollPane();
        textAreaHypothesis = new javax.swing.JTextArea();
        jScrollPaneDescription = new javax.swing.JScrollPane();
        textAreaDescription = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(edP.getBundleString("TITLE_DIALOG_ADD_QUESTION"));
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
        buttonOk.setBounds(40, 370, 99, 23);

        buttonCancel.setText(edP.getBundleString("BUTTON_CANCEL"));
        buttonCancel.setName("buttonCancel"); // NOI18N
        buttonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonCancelActionPerformed(evt);
            }
        });
        getContentPane().add(buttonCancel);
        buttonCancel.setBounds(180, 370, 99, 23);

        labelQuestion.setFont(new java.awt.Font("Tahoma", 1, 11));
        labelQuestion.setText(edP.getBundleString("LABEL_QUESTION"));
        labelQuestion.setName("labelQuestion"); // NOI18N
        getContentPane().add(labelQuestion);
        labelQuestion.setBounds(20, 10, 90, 14);

        labelHypothesis.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        labelHypothesis.setText(edP.getBundleString("LABEL_HYPOTHESIS"));
        labelHypothesis.setName("labelHypothesis"); // NOI18N
        getContentPane().add(labelHypothesis);
        labelHypothesis.setBounds(20, 120, 110, 14);

        labelGeneralPrinciple.setFont(new java.awt.Font("Tahoma", 1, 11));
        labelGeneralPrinciple.setText(edP.getBundleString("LABEL_GENERAL_PRINCIPLE")+" :");
        labelGeneralPrinciple.setName("labelGeneralPrinciple"); // NOI18N
        getContentPane().add(labelGeneralPrinciple);
        labelGeneralPrinciple.setBounds(20, 250, 110, 14);

        scrollPaneGeneralPrinciple.setName("scrollPaneGeneralPrinciple"); // NOI18N

        textAreaGeneralPrinciple.setColumns(20);
        textAreaGeneralPrinciple.setRows(5);
        textAreaGeneralPrinciple.setName("textAreaGeneralPrinciple"); // NOI18N
        textAreaGeneralPrinciple.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                textAreaGeneralPrincipleFocusGained(evt);
            }
        });
        scrollPaneGeneralPrinciple.setViewportView(textAreaGeneralPrinciple);

        getContentPane().add(scrollPaneGeneralPrinciple);
        scrollPaneGeneralPrinciple.setBounds(20, 270, 300, 86);

        labelImage.setName("labelImage"); // NOI18N
        getContentPane().add(labelImage);
        labelImage.setBounds(20, 330, 70, 10);

        scrollPaneHypothesis.setName("scrollPaneHypothesis"); // NOI18N
        scrollPaneHypothesis.setPreferredSize(new java.awt.Dimension(146, 90));

        textAreaHypothesis.setColumns(20);
        textAreaHypothesis.setRows(5);
        textAreaHypothesis.setName("textAreaHypothesis"); // NOI18N
        textAreaHypothesis.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                textAreaHypothesisFocusGained(evt);
            }
        });
        scrollPaneHypothesis.setViewportView(textAreaHypothesis);

        getContentPane().add(scrollPaneHypothesis);
        scrollPaneHypothesis.setBounds(20, 140, 300, 90);

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

private void textAreaHypothesisFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_textAreaHypothesisFocusGained
    removeDefaultTextHypothesis();
}//GEN-LAST:event_textAreaHypothesisFocusGained

private void textAreaGeneralPrincipleFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_textAreaGeneralPrincipleFocusGained
    removeDefaultTextPrinciple();
}//GEN-LAST:event_textAreaGeneralPrincipleFocusGained

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
    private javax.swing.JLabel labelGeneralPrinciple;
    private javax.swing.JLabel labelHypothesis;
    private javax.swing.JLabel labelImage;
    private javax.swing.JLabel labelQuestion;
    private javax.swing.JScrollPane scrollPaneGeneralPrinciple;
    private javax.swing.JScrollPane scrollPaneHypothesis;
    private javax.swing.JTextArea textAreaDescription;
    private javax.swing.JTextArea textAreaGeneralPrinciple;
    private javax.swing.JTextArea textAreaHypothesis;
    // End of variables declaration//GEN-END:variables

}

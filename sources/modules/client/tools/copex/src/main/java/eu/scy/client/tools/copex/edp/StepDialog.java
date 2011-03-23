/*
 * StepDialog.java
 *
 * Created on 28 juillet 2008, 15:35
 */

package eu.scy.client.tools.copex.edp;

import eu.scy.client.tools.copex.common.InitialActionParam;
import eu.scy.client.tools.copex.common.InitialOutput;
import eu.scy.client.tools.copex.common.Step;
import eu.scy.client.tools.copex.common.TaskRepeat;
import eu.scy.client.tools.copex.utilities.ActionComment;
import eu.scy.client.tools.copex.utilities.CommentsPanel;
import eu.scy.client.tools.copex.utilities.CopexReturn;
import eu.scy.client.tools.copex.utilities.CopexUtilities;
import eu.scy.client.tools.copex.utilities.MyConstants;
import java.awt.Cursor;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import javax.swing.*;

/**
 * fenetre d'ajout d'une etape
 * @author  MBO
 */
public class StepDialog extends JDialog implements ActionComment, ActionTaskRepeat{
    private EdPPanel edP;
     /* mode de visualisation  : ajout / modification */
    private boolean modeAdd;
    /* mode etape ou mode tache */
    private boolean stepMode;
    /* droit sur la fenetre */
    private char right = MyConstants.EXECUTE_RIGHT;
    /* droit proc */
    private char procRight = MyConstants.EXECUTE_RIGHT;
    /* etape */
    private Step step;
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
    /* etape peut etre repetee */
    private boolean isTaskRepeat;
     /* repetition */
    private TaskRepeat taskRepeat;

    private TaskRepeatPanel taskRepeatPanel;
    private char insertIn;
    
    /** Creates new form StepDialog */
    public StepDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    public StepDialog(EdPPanel edP, boolean isTaskRepeat, boolean stepMode, char insertIn) {
        super(edP.getOwnerFrame());
        this.edP = edP;
        this.modeAdd = true;
        this.stepMode = stepMode;
        this.comment = "";
        this.isTaskRepeat = isTaskRepeat;
        //this.isTaskRepeat = false;
        this.taskRepeat = null;
        this.insertIn = insertIn;
        this.setLocationRelativeTo(edP);
        initComponents();
        setLocation(edP.getLocationDialog());
        setModal(true);
        init();
        setIconImage(edP.getIconDialog());
    }

     /* constructeur de la fenetre d'edition de l'action */
    public StepDialog(EdPPanel edP, boolean modeAdd, boolean stepMode, Step step,  ImageIcon taskImage, boolean isTaskRepeat, char right, char procRight ) {
        super(edP.getOwnerFrame());
        this.edP = edP;
        this.modeAdd = modeAdd;
        this.stepMode = stepMode;
        this.step = step;
        this.name = step.getDescription(edP.getLocale());
        this.comments = step.getComments(edP.getLocale());
        this.right = right;
        this.procRight = procRight;
        this.taskImage = taskImage;
        this.isTaskRepeat = isTaskRepeat;
        //this.isTaskRepeat = false;
        this.taskRepeat = step.getTaskRepeat();
        this.insertIn = MyConstants.INSERT_TASK_UNDEF;
        this.setLocationRelativeTo(edP);
        initComponents();
        setLocation(edP.getLocationDialog());
        setModal(true);
        setIconImage(edP.getIconDialog());
        init();
    }
    private void init(){
        getContentPane().add(getPanelComments());
        if(isTaskRepeat)
            setPanelTaskRepeat();
        if(stepMode){
            setTitle(edP.getBundleString("TITLE_DIALOG_ADD_STEP"));
            labelName.setText(edP.getBundleString("LABEL_NAME_STEP"));
        }else{
            setTitle(edP.getBundleString("TITLE_DIALOG_ADD_TASK"));
            labelName.setText(edP.getBundleString("LABEL_NAME_TASK"));
        }
        if (!modeAdd){
            // mode edit
            if(stepMode){
                this.setTitle(edP.getBundleString("TITLE_DIALOG_STEP"));
            }else{
                this.setTitle(edP.getBundleString("TITLE_DIALOG_TASK"));
            }
            this.textAreaDescription.setText(this.name);
            this.panelComments.setComments(this.comments);
            this.comment = this.comments ;
            // image 
            if (this.taskImage != null ){
                this.labelImage.setIcon(this.taskImage);
            }
            // repetition
            if (taskRepeat != null)
                this.taskRepeatPanel.setTaskRepeat(taskRepeat);
            // gestion des droits 
            if (right == MyConstants.NONE_RIGHT)
               setDisabled();
            if (procRight == MyConstants.NONE_RIGHT)
                setAllDisabled();
        }
        textAreaDescription.setLineWrap(true);
        textAreaDescription.setWrapStyleWord(true);
        
        resizeElements();
        resizeDialog();
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

    /* panel task repeat */
    private TaskRepeatPanel getTaskRepeatPanel(){
        if(taskRepeatPanel == null){
            ArrayList[] list = getStepInitialParam();
            ArrayList<InitialActionParam> listStepInitialParam = list[0];
            ArrayList<Long> listStepInitialParamDbKeyAction = list[1];
            list = getStepInitialOutput();
            ArrayList<InitialOutput> listStepInitialOutput = list[0];
            ArrayList<Long> listStepInitialOutputDbKeyAction = list[1];
            taskRepeatPanel = new TaskRepeatPanel(edP, listStepInitialParam,listStepInitialParamDbKeyAction ,listStepInitialOutput, listStepInitialOutputDbKeyAction,  false, modeAdd);
            taskRepeatPanel.addActionTaskRepeat(this);
            taskRepeatPanel.setName("taskRepeatPanel");
            if(!modeAdd && this.taskRepeat != null){
                taskRepeatPanel.setTaskRepeat(taskRepeat);
            }
            int y = panelComments.getHeight()+panelComments.getY()+20 ;
            if(labelImage != null){
                y = labelImage.getY()+labelImage.getHeight()+20;
            }
            taskRepeatPanel.setBounds(20, y, 300, taskRepeatPanel.getHeight());
        }
        return taskRepeatPanel ;
    }

    /* retourne la liste des parametres des actions de l'etape */
    private ArrayList[] getStepInitialParam(){
        if(modeAdd){
            ArrayList[] list = new ArrayList[2];
            list[0] = new ArrayList();
            list[1] = new ArrayList();
            return list;
        } else {
            return edP.getStepInitialParam(step);
        }
    }


    /* retourne la liste des output des actions de l'etape */
    private ArrayList[] getStepInitialOutput(){
        if(modeAdd){
            ArrayList[] list = new ArrayList[2];
            list[0] = new ArrayList();
            list[1] = new ArrayList();
            return list;
        } else {
            return edP.getStepInitialOutput(step);
        }
    }


    
    /* mise en place du panel task repeat */
    private void setPanelTaskRepeat(){
        if(this.panelComments == null)
            return;
        removePanelTaskRepeat();
        getContentPane().add(getTaskRepeatPanel());
    }

    private void removePanelTaskRepeat(){
        if(taskRepeatPanel != null){
            getContentPane().remove(taskRepeatPanel);
            taskRepeatPanel = null;
        }
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
        if(this.taskRepeatPanel != null)
            taskRepeatPanel.setDisabled();
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
       }
       // bouton Annuler
       this.buttonCancel.setSize(60+CopexUtilities.lenghtOfString(this.buttonCancel.getText(), getFontMetrics(this.buttonCancel.getFont())), this.buttonCancel.getHeight());
 
   }

    private void resizeDialog(){
        int width = getWidth() ;
        int height = panelComments.getY() + panelComments.getHeight()+60 ;
       if (this.taskImage != null){
           int imgH = this.taskImage.getIconHeight()+20;
           height += imgH ;
           width = Math.max(this.getWidth(), this.taskImage.getIconWidth()+this.labelImage.getX());
           this.labelImage.setBounds(labelImage.getX(), labelImage.getY(), this.taskImage.getIconWidth(), this.taskImage.getIconHeight());
       }
       int y = panelComments.getY() + panelComments.getHeight()+10;
       if (this.taskRepeatPanel != null){
           height += this.taskRepeatPanel.getHeight()+30;
           this.taskRepeatPanel.setBounds(this.taskRepeatPanel.getX(), y, taskRepeatPanel.getWidth(), taskRepeatPanel.getHeight());
       }
        y = height - this.buttonCancel.getHeight() - 30 ;
        if(this.buttonOk != null)
            this.buttonOk.setBounds(this.buttonOk.getX(), y, this.buttonOk.getWidth(), this.buttonOk.getHeight());
        this.buttonCancel.setBounds(this.buttonCancel.getX(), y, this.buttonCancel.getWidth(), this.buttonCancel.getHeight());

        setSize(width, height);
        setPreferredSize(getSize());
        repaint();
   }
   
   private void validDialog(){
       setCursor(new Cursor(Cursor.WAIT_CURSOR));
       String champ = edP.getBundleString("LABEL_NAME_STEP");
       if(!stepMode)
           champ = edP.getBundleString("LABEL_NAME_TASK");
       this.panelComments.setPanelDetailsShown();
        // recupere les donnees :
        String d = this.textAreaDescription.getText();
        if (controlLenght() && d.length() > MyConstants.MAX_LENGHT_TASK_DESCRIPTION){
            String msg = edP.getBundleString("MSG_LENGHT_MAX");
            msg  = CopexUtilities.replace(msg, 0, champ);
            msg = CopexUtilities.replace(msg, 1, ""+MyConstants.MAX_LENGHT_TASK_DESCRIPTION);
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            edP.displayError(new CopexReturn(msg, false), edP.getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }
        if (d.length() == 0){
            String msg = edP.getBundleString("MSG_ERROR_FIELD_NULL");
            msg  = CopexUtilities.replace(msg, 0, champ);
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            edP.displayError(new CopexReturn(msg ,false), edP.getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }
        String c = this.panelComments.getComments();
        if (controlLenght() && c.length() > MyConstants.MAX_LENGHT_TASK_COMMENTS){
            String msg = edP.getBundleString("MSG_LENGHT_MAX");
            msg  = CopexUtilities.replace(msg, 0, edP.getBundleString("LABEL_COMMENTS"));
            msg = CopexUtilities.replace(msg, 1, ""+MyConstants.MAX_LENGHT_TASK_COMMENTS);
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            edP.displayError(new CopexReturn(msg, false) , edP.getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }
        int nbTaskRepeat = 1;
        if(taskRepeatPanel != null){
            nbTaskRepeat = taskRepeatPanel.getNbRepeat();
        }
        if(nbTaskRepeat > 1){
           ArrayList v2 = new ArrayList();
           long oldKey = -1;
            if (taskRepeat != null)
                oldKey = taskRepeat.getDbKey();
           CopexReturn cr=  taskRepeatPanel.getTaskRepeat(v2);
           if(cr.isError()){
               edP.displayError(cr, edP.getBundleString("TITLE_DIALOG_ERROR"));
               return;
           }
           taskRepeat = (TaskRepeat)v2.get(0);
           taskRepeat.setDbKey(oldKey);
        }else
            taskRepeat = null;
        
        Step newStep = new Step(CopexUtilities.getLocalText(d, edP.getLocale()), CopexUtilities.getLocalText(c, edP.getLocale()));
        newStep.setTaskRepeat(taskRepeat);
        if (modeAdd){
            // Cree l'etpae
            CopexReturn cr = edP.addStep(newStep, insertIn);
            if (cr.isError()){
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                edP.displayError( cr , edP.getBundleString("TITLE_DIALOG_ERROR"));
                return;
            }
        }else{
            // mode modification
            CopexReturn cr = edP.updateStep(newStep);
            if (cr.isError()){
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                edP.displayError(cr, edP.getBundleString("TITLE_DIALOG_ERROR"));
                return;
            }
        }
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        this.dispose();
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

    private boolean controlLenght(){
        return edP.controlLenght();
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
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });
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
        textAreaDescription.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                textAreaDescriptionKeyPressed(evt);
            }
        });
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

private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
    if (evt.getKeyCode() == KeyEvent.VK_ENTER){
        validDialog();
    }
}//GEN-LAST:event_formKeyPressed

private void textAreaDescriptionKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textAreaDescriptionKeyPressed
    if (evt.getKeyCode() == KeyEvent.VK_ENTER){
        validDialog();
    }
}//GEN-LAST:event_textAreaDescriptionKeyPressed

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
//           if (this.buttonOk != null)
//                this.buttonOk.setBounds(this.buttonOk.getX(), panelComments.getHeight()+panelComments.getY()+20, this.buttonOk.getWidth(), this.buttonOk.getHeight());
//           this.buttonCancel.setBounds(this.buttonCancel.getX(), panelComments.getHeight()+panelComments.getY()+20, this.buttonCancel.getWidth(), this.buttonCancel.getHeight());
//           int h = this.buttonCancel.getY() + this.buttonCancel.getHeight() +50;
//           this.setSize(this.getWidth(), h);
//           this.setPreferredSize(this.getSize());
//           this.repaint();
        resizeDialog();
    }

    @Override
    public void actionResize() {
        resizeDialog();
    }

    @Override
    public void actionUpdateNbRepeat(int nbRepeat) {
        
    }

    @Override
    public void actionSetSelected(Object oldSel, Object newSel) {
        
    }

    @Override
    public void enterKeyPressed() {
       validDialog();
    }

   
    
    
}
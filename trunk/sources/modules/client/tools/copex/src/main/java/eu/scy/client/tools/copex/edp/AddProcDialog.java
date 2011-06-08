/*
 * AddProcDialog.java
 *
 * Created on 1 aoat 2008, 11:04
 */

package eu.scy.client.tools.copex.edp;


import eu.scy.client.tools.copex.common.CopexMission;
import eu.scy.client.tools.copex.common.ExperimentalProcedure;
import eu.scy.client.tools.copex.common.InitialProcedure;
import eu.scy.client.tools.copex.common.LearnerProcedure;
import eu.scy.client.tools.copex.controller.ControllerInterface;
import eu.scy.client.tools.copex.utilities.CopexReturn;
import eu.scy.client.tools.copex.utilities.CopexUtilities;
import eu.scy.client.tools.copex.utilities.MyConstants;
import java.awt.Cursor;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import javax.swing.*;

/**
 * dialog called when click on the + button,
 * allows the user to choose between:
 * - create a new proc,
 * - create a new proc base on an existing proc
 * - open a proc
 * @author  Marjolaine
 */

public class AddProcDialog extends JDialog {

    /* owner */
    private EdPPanel edP;
    /* controller */
    private ControllerInterface controller;
    /*  list of initial proc */
    private ArrayList<InitialProcedure> listInitialProc;
    /* list of proc. of the current mission  */
    private ArrayList<LearnerProcedure> listProcMission;
    /* list of missions */
    private ArrayList<CopexMission> listMission;
    /* list of proc. of all missions  */
    private ArrayList<ArrayList<LearnerProcedure>> listAllProc;
    private boolean onlyOneInitProc;

    private boolean setDefaultProcName = false;

    public AddProcDialog(EdPPanel edP, ControllerInterface controller, ArrayList<LearnerProcedure> listProcMission, ArrayList<CopexMission> listMission, ArrayList<ArrayList<LearnerProcedure>> listAllProc, ArrayList<InitialProcedure> listInitialProc) {
        super(edP.getOwnerFrame());
        setLocationRelativeTo(edP);
        this.edP = edP;
        this.controller = controller;
        this.listProcMission = listProcMission;
        this.listMission = listMission;
        this.listAllProc = listAllProc;
        this.listInitialProc = listInitialProc ;
        int nb = listInitialProc.size();
        onlyOneInitProc = (nb == 1);
        initComponents();
        setModal(true);
        setLocation(edP.getLocationDialog());
        init();
        setIconImage(edP.getIconDialog());
    }
    
    private void init(){
        setSize(500,350);
        // inistialization of types of initial proc
        if (onlyOneInitProc){
            remove(this.cbProcInit);
            cbProcInit = null;
            this.rbNewProc.setText(edP.getBundleString("LABEL_ADD_NEW_PROC"));
        }else{
            this.rbNewProc.setText(edP.getBundleString("LABEL_ADD_NEW_PROC_INIT"));
            int nb = this.listInitialProc.size();
            for (int i=0; i<nb; i++){
                InitialProcedure initProc = listInitialProc.get(i);
                cbProcInit.addItem(initProc.getCode());
            }
            if (nb > 0){
                cbProcInit.setSelectedIndex(0);
                if(setDefaultProcName)
                    textFieldProcName.setText(listInitialProc.get(0).getName(edP.getLocale()));
            }
        }
        // inistialization of list of proc of the current mission
        int nb = listProcMission.size();
        for (int i=0; i<nb; i++){
            ExperimentalProcedure p = (ExperimentalProcedure)listProcMission.get(i);
            cbListProc.addItem(p.getName(edP.getLocale()));
        }
        if (listProcMission.size() > 0)
            cbListProc.setSelectedIndex(0);
        else
            rbCopyProc.setEnabled(false);
        // initialization of list of missions
        nb = listMission.size();
        for (int i=0; i<nb; i++){
            CopexMission m = (CopexMission)listMission.get(i);
            cbListMission.addItem(m.getName());
        }
        if (listMission.size() > 0)
            cbListMission.setSelectedIndex(0);
        else
            rbOpenProc.setEnabled(false);
        // resize elements
        resizeElements();
        repaint();
    }
    
    /*
     * allows to resize the elements of the dialog, depending of the lenght of the string
     * depending of the language
     */
   private void resizeElements(){
       // label
       this.labelProcNameCreate.setSize(CopexUtilities.lenghtOfString(this.labelProcNameCreate.getText(), getFontMetrics(this.labelProcNameCreate.getFont())), this.labelProcNameCreate.getHeight());
       this.labelProcNameCopy.setSize(CopexUtilities.lenghtOfString(this.labelProcNameCopy.getText(), getFontMetrics(this.labelProcNameCopy.getFont())), this.labelProcNameCopy.getHeight());
       this.labelOpenProcMission.setSize(CopexUtilities.lenghtOfString(this.labelOpenProcMission.getText(), getFontMetrics(this.labelOpenProcMission.getFont())), this.labelOpenProcMission.getHeight());
       this.labelOpenProcProc.setSize(CopexUtilities.lenghtOfString(this.labelOpenProcProc.getText(), getFontMetrics(this.labelOpenProcProc.getFont())), this.labelOpenProcProc.getHeight());
       // rb new
       this.rbNewProc.setSize(70+CopexUtilities.lenghtOfString(this.rbNewProc.getText(), getFontMetrics(this.rbNewProc.getFont())), this.rbNewProc.getHeight());
       // rb copy
       this.rbCopyProc.setSize(70+CopexUtilities.lenghtOfString(this.rbCopyProc.getText(), getFontMetrics(this.rbCopyProc.getFont())), this.rbCopyProc.getHeight());
       // rb open
       this.rbOpenProc.setSize(70+CopexUtilities.lenghtOfString(this.rbOpenProc.getText(), this.rbOpenProc.getFontMetrics(this.rbOpenProc.getFont())), this.rbOpenProc.getHeight());
       // button Ok
       this.buttonOk.setSize(70+CopexUtilities.lenghtOfString(this.buttonOk.getText(), getFontMetrics(this.buttonOk.getFont())), this.buttonOk.getHeight());
       // button cancel
       this.buttonCancel.setSize(70+CopexUtilities.lenghtOfString(this.buttonCancel.getText(), getFontMetrics(this.buttonCancel.getFont())), this.buttonCancel.getHeight());
       //place elements
       int x = 10;
       int decalY = 1;
       this.rbNewProc.setBounds(x, 5, this.rbNewProc.getWidth(), this.rbNewProc.getHeight());
       if (!onlyOneInitProc){
           this.cbProcInit.setBounds(this.rbNewProc.getX()+this.rbNewProc.getWidth() +10, this.rbNewProc.getY()+decalY, this.cbProcInit.getWidth(), this.cbProcInit.getHeight());
       }
       this.labelProcNameCreate.setBounds(x, this.rbNewProc.getY()+this.rbNewProc.getHeight()+10, this.labelProcNameCreate.getWidth(), this.labelProcNameCreate.getHeight());
       this.textFieldProcName.setBounds(this.labelProcNameCreate.getX()+this.labelProcNameCreate.getWidth()+10, this.labelProcNameCreate.getY()+decalY, this.textFieldProcName.getWidth(), this.textFieldProcName.getHeight());

       this.rbCopyProc.setBounds(x, labelProcNameCreate.getY()+labelProcNameCreate.getHeight()+20, this.rbCopyProc.getWidth(), this.rbCopyProc.getHeight());
       this.cbListProc.setBounds(this.rbCopyProc.getX()+this.rbCopyProc.getWidth() +10, this.rbCopyProc.getY()+decalY, this.cbListProc.getWidth(), this.cbListProc.getHeight());
       this.labelProcNameCopy.setBounds(x, this.rbCopyProc.getY()+this.rbCopyProc.getHeight()+10, this.labelProcNameCopy.getWidth(), this.labelProcNameCopy.getHeight());
       this.textFieldProcNameCopy.setBounds(this.labelProcNameCopy.getX()+this.labelProcNameCopy.getWidth()+10, this.labelProcNameCopy.getY()+decalY, this.textFieldProcNameCopy.getWidth(), this.textFieldProcNameCopy.getHeight());
       
       this.rbOpenProc.setBounds(x, labelProcNameCopy.getY()+labelProcNameCopy.getHeight()+20, this.rbOpenProc.getWidth(), this.rbOpenProc.getHeight());
       this.labelOpenProcMission.setBounds(x, rbOpenProc.getY()+rbOpenProc.getHeight()+10, labelOpenProcMission.getWidth(), labelOpenProcProc.getHeight());
       this.cbListMission.setBounds(this.labelOpenProcMission.getX()+this.labelOpenProcMission.getWidth()+10, this.labelOpenProcMission.getY()+decalY, this.cbListMission.getWidth(), this.cbListMission.getHeight());
       this.labelOpenProcProc.setBounds(x, labelOpenProcMission.getY()+labelOpenProcMission.getHeight()+10, labelOpenProcProc.getWidth(), labelOpenProcProc.getHeight());
       this.cbListMissionProc.setBounds(this.labelOpenProcProc.getX()+this.labelOpenProcProc.getWidth()+10, this.labelOpenProcProc.getY()+decalY, this.cbListMissionProc.getWidth(), this.cbListMissionProc.getHeight());

       resizeWidth();
       
   }

   private void resizeWidth(){
       int nb = this.listAllProc.size();
       int m = 0;
       for (int i=0; i<nb; i++){
           int n1 = this.listAllProc.get(i).size();
           for (int j=0; j<n1; j++){
               int s = CopexUtilities.lenghtOfString(this.listAllProc.get(i).get(j).getName(edP.getLocale()),getFontMetrics(this.cbListMissionProc.getFont()) );
               m = Math.max(m, s);
           }
       }
       int width = Math.max(getWidth(),m+ this.cbListMissionProc.getX()+20);
       setSize(width, getHeight());
       setPreferredSize(getSize());
   }
   
    /** Creates new form AddProcDialog */
    public AddProcDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    /* selection of a initial proc => by default, set the name of the initial proc   */
   private void selectInitProc(){
       if(setDefaultProcName){
        int id = this.cbProcInit.getSelectedIndex() ;
        if(id > 0){
           textFieldProcName.setText(listInitialProc.get(id).getName(edP.getLocale()));
        }
       }
   }
   
    /*  dialog validation */
    private void validDialog(){
        // gets data
        setCursor(new Cursor(Cursor.WAIT_CURSOR));
        if (this.rbNewProc.isSelected()){
            // creation of a new proc.
            String name = textFieldProcName.getText();
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
            // gets initial proc
            InitialProcedure initProc = listInitialProc.get(0);
            if(!onlyOneInitProc){
                int id = this.cbProcInit.getSelectedIndex() ;
                if (id == -1){
                    setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    edP.displayError(new CopexReturn(edP.getBundleString("MSG_ERROR_OPEN_PROC"), false), edP.getBundleString("TITLE_DIALOG_ERROR"));
                    return;
                }
                initProc = this.listInitialProc.get(id);
            }
            CopexReturn cr = this.controller.createProc(name, initProc);
            if (cr.isError()){
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                edP.displayError(cr, edP.getBundleString("TITLE_DIALOG_ERROR"));
                return;
            }
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            this.dispose();
            edP.setQuestionDialog();
            return;
        }else if (this.rbCopyProc.isSelected()){
            // copy of an existing proc in the mission
            // copy name
            String name = textFieldProcNameCopy.getText();
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
            // gets the proc to copy
            int id = this.cbListProc.getSelectedIndex();
            if (id == -1){
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                edP.displayError(new CopexReturn(edP.getBundleString("MSG_ERROR_COPY_PROC"), false), edP.getBundleString("TITLE_DIALOG_ERROR"));
                return;
            }
            LearnerProcedure procToCopy = listProcMission.get(id);
            if (procToCopy == null){
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                edP.displayError(new CopexReturn(edP.getBundleString("MSG_ERROR_COPY_PROC"), false), edP.getBundleString("TITLE_DIALOG_ERROR"));
                return;
            }
            CopexReturn cr = this.controller.copyProc(name, procToCopy);
            if (cr.isError()){
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                edP.displayError(cr, edP.getBundleString("TITLE_DIALOG_ERROR"));
                return;
            }
        
        }else if (this.rbOpenProc.isSelected()){
            // gets the mission and the selected proc
            int idM = this.cbListMission.getSelectedIndex();
            if (idM == -1){
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                edP.displayError(new CopexReturn(edP.getBundleString("MSG_ERROR_OPEN_PROC"), false), edP.getBundleString("TITLE_DIALOG_ERROR"));
                return;
            }
            CopexMission mission = listMission.get(idM);
            if (mission == null){
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                edP.displayError(new CopexReturn(edP.getBundleString("MSG_ERROR_OPEN_PROC"), false), edP.getBundleString("TITLE_DIALOG_ERROR"));
                return;
            }
            int idP = this.cbListMissionProc.getSelectedIndex();
            if (idP == -1){
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                edP.displayError(new CopexReturn(edP.getBundleString("MSG_ERROR_OPEN_PROC"), false), edP.getBundleString("TITLE_DIALOG_ERROR"));
                return;
            }
            LearnerProcedure procToOpen = listAllProc.get(idM).get(idP);
            if (procToOpen == null){
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                edP.displayError(new CopexReturn(edP.getBundleString("MSG_ERROR_OPEN_PROC"), false), edP.getBundleString("TITLE_DIALOG_ERROR"));
                return;
            }
            CopexReturn cr = this.controller.openProc(mission, procToOpen);
            if (cr.isError()){
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                edP.displayError(cr, edP.getBundleString("TITLE_DIALOG_ERROR"));
                return;
            }
        }
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        this.dispose();
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

        buttonOk = new javax.swing.JButton();
        buttonCancel = new javax.swing.JButton();
        rbNewProc = new javax.swing.JRadioButton();
        rbCopyProc = new javax.swing.JRadioButton();
        rbOpenProc = new javax.swing.JRadioButton();
        cbListProc = new javax.swing.JComboBox();
        cbListMission = new javax.swing.JComboBox();
        cbListMissionProc = new javax.swing.JComboBox();
        textFieldProcName = new javax.swing.JTextField();
        textFieldProcNameCopy = new javax.swing.JTextField();
        labelProcNameCreate = new javax.swing.JLabel();
        labelProcNameCopy = new javax.swing.JLabel();
        labelOpenProcMission = new javax.swing.JLabel();
        labelOpenProcProc = new javax.swing.JLabel();
        cbProcInit = new javax.swing.JComboBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(edP.getBundleString("TITLE_DIALOG_ADD_PROC"));
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
        buttonOk.setBounds(100, 280, 99, 23);

        buttonCancel.setText(edP.getBundleString("BUTTON_CANCEL"));
        buttonCancel.setName("buttonCancel"); // NOI18N
        buttonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonCancelActionPerformed(evt);
            }
        });
        getContentPane().add(buttonCancel);
        buttonCancel.setBounds(340, 280, 99, 23);

        rbNewProc.setFont(new java.awt.Font("Tahoma", 1, 11));
        rbNewProc.setSelected(true);
        rbNewProc.setText(edP.getBundleString("LABEL_ADD_NEW_PROC"));
        rbNewProc.setMaximumSize(new java.awt.Dimension(300, 23));
        rbNewProc.setName("rbNewProc"); // NOI18N
        rbNewProc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbNewProcActionPerformed(evt);
            }
        });
        getContentPane().add(rbNewProc);
        rbNewProc.setBounds(10, 10, 250, 23);

        rbCopyProc.setFont(new java.awt.Font("Tahoma", 1, 11));
        rbCopyProc.setText(edP.getBundleString("LABEL_ADD_NEW_PROC_COPY"));
        rbCopyProc.setMaximumSize(new java.awt.Dimension(300, 23));
        rbCopyProc.setName("rbCopyProc"); // NOI18N
        rbCopyProc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbCopyProcActionPerformed(evt);
            }
        });
        getContentPane().add(rbCopyProc);
        rbCopyProc.setBounds(10, 80, 250, 23);

        rbOpenProc.setFont(new java.awt.Font("Tahoma", 1, 11));
        rbOpenProc.setText(edP.getBundleString("LABEL_OPEN_PROC"));
        rbOpenProc.setName("rbOpenProc"); // NOI18N
        rbOpenProc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbOpenProcActionPerformed(evt);
            }
        });
        getContentPane().add(rbOpenProc);
        rbOpenProc.setBounds(10, 150, 270, 23);

        cbListProc.setEnabled(false);
        cbListProc.setName("cbListProc"); // NOI18N
        getContentPane().add(cbListProc);
        cbListProc.setBounds(340, 80, 60, 20);

        cbListMission.setEnabled(false);
        cbListMission.setName("cbListMission"); // NOI18N
        cbListMission.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbListMissionActionPerformed(evt);
            }
        });
        getContentPane().add(cbListMission);
        cbListMission.setBounds(170, 190, 28, 20);

        cbListMissionProc.setEnabled(false);
        cbListMissionProc.setName("cbListMissionProc"); // NOI18N
        getContentPane().add(cbListMissionProc);
        cbListMissionProc.setBounds(170, 220, 28, 20);

        textFieldProcName.setToolTipText(edP.getBundleString("TOOLTIPTEXT_CREATE_PROC"));
        textFieldProcName.setName("textFieldProcName"); // NOI18N
        textFieldProcName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                textFieldProcNameKeyReleased(evt);
            }
        });
        getContentPane().add(textFieldProcName);
        textFieldProcName.setBounds(190, 40, 280, 20);

        textFieldProcNameCopy.setToolTipText(edP.getBundleString("TOOLTIPTEXT_CREATE_PROC"));
        textFieldProcNameCopy.setName("textFieldProcNameCopy"); // NOI18N
        textFieldProcNameCopy.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                textFieldProcNameCopyKeyReleased(evt);
            }
        });
        getContentPane().add(textFieldProcNameCopy);
        textFieldProcNameCopy.setBounds(190, 110, 280, 20);

        labelProcNameCreate.setFont(new java.awt.Font("Tahoma", 1, 11));
        labelProcNameCreate.setText(edP.getBundleString("LABEL_PROC_NAME"));
        labelProcNameCreate.setName("labelProcNameCreate"); // NOI18N
        getContentPane().add(labelProcNameCreate);
        labelProcNameCreate.setBounds(30, 40, 75, 14);

        labelProcNameCopy.setFont(new java.awt.Font("Tahoma", 1, 11));
        labelProcNameCopy.setText(edP.getBundleString("LABEL_PROC_NAME"));
        labelProcNameCopy.setName("labelProcNameCopy"); // NOI18N
        getContentPane().add(labelProcNameCopy);
        labelProcNameCopy.setBounds(40, 110, 75, 14);

        labelOpenProcMission.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        labelOpenProcMission.setText(edP.getBundleString("LABEL_MISSION"));
        labelOpenProcMission.setName("labelOpenProcMission"); // NOI18N
        getContentPane().add(labelOpenProcMission);
        labelOpenProcMission.setBounds(30, 190, 75, 14);

        labelOpenProcProc.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        labelOpenProcProc.setText(edP.getBundleString("LABEL_PROC"));
        labelOpenProcProc.setName("labelOpenProcProc"); // NOI18N
        getContentPane().add(labelOpenProcProc);
        labelOpenProcProc.setBounds(30, 220, 75, 14);

        cbProcInit.setName("cbProcInit"); // NOI18N
        cbProcInit.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbProcInitItemStateChanged(evt);
            }
        });
        getContentPane().add(cbProcInit);
        cbProcInit.setBounds(330, 10, 70, 20);

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void buttonOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonOkActionPerformed
    validDialog();
}//GEN-LAST:event_buttonOkActionPerformed

private void buttonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonCancelActionPerformed
    this.dispose();
}//GEN-LAST:event_buttonCancelActionPerformed

private void rbNewProcActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbNewProcActionPerformed
    rbNewProc.setSelected(true);
    textFieldProcName.setEnabled(true);
    if(cbProcInit != null)
        cbProcInit.setEnabled(true);
    rbCopyProc.setSelected(false);
    rbOpenProc.setSelected(false);
    textFieldProcNameCopy.setEnabled(false);
    cbListMissionProc.setEnabled(false);
    cbListMission.setEnabled(false);
    cbListProc.setEnabled(false);
}//GEN-LAST:event_rbNewProcActionPerformed

private void rbCopyProcActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbCopyProcActionPerformed
    rbNewProc.setSelected(false);
    textFieldProcName.setEnabled(false);
     if(cbProcInit != null)
        cbProcInit.setEnabled(false);
    rbCopyProc.setSelected(true);
    textFieldProcNameCopy.setEnabled(true);
    rbOpenProc.setSelected(false);
    cbListMissionProc.setEnabled(false);
    cbListMission.setEnabled(false);
    cbListProc.setEnabled(true);
}//GEN-LAST:event_rbCopyProcActionPerformed

private void rbOpenProcActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbOpenProcActionPerformed
    rbNewProc.setSelected(false);
    textFieldProcName.setEnabled(false);
     if(cbProcInit != null)
        cbProcInit.setEnabled(false);
    rbCopyProc.setSelected(false);
    textFieldProcNameCopy.setEnabled(false);
    rbOpenProc.setSelected(true);
    cbListMissionProc.setEnabled(true);
    cbListMission.setEnabled(true);
    cbListProc.setEnabled(false);
}//GEN-LAST:event_rbOpenProcActionPerformed

private void cbListMissionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbListMissionActionPerformed
    // update the list of proc. depending of the selected mission 
    cbListMissionProc.removeAllItems();
    int id = cbListMission.getSelectedIndex();
    if (id != -1){
        ArrayList<LearnerProcedure> list = this.listAllProc.get(id);
        if (list != null){
            // initialization of the list of missions
            int nb = list.size();
            for (int i=0; i<nb; i++){
                LearnerProcedure p = list.get(i);
                cbListMissionProc.addItem(p.getName(edP.getLocale()));
            }
            if (list.size() > 0)
                cbListMissionProc.setSelectedIndex(0);
           }
    }
    repaint();
}//GEN-LAST:event_cbListMissionActionPerformed

private void textFieldProcNameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textFieldProcNameKeyReleased
    if (evt.getKeyCode() == KeyEvent.VK_ENTER)
        validDialog();
}//GEN-LAST:event_textFieldProcNameKeyReleased

private void textFieldProcNameCopyKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textFieldProcNameCopyKeyReleased
 if (evt.getKeyCode() == KeyEvent.VK_ENTER)
        validDialog();
}//GEN-LAST:event_textFieldProcNameCopyKeyReleased

private void cbProcInitItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbProcInitItemStateChanged
    selectInitProc();
}//GEN-LAST:event_cbProcInitItemStateChanged

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                AddProcDialog dialog = new AddProcDialog(new javax.swing.JFrame(), true);
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
    private javax.swing.JComboBox cbListMission;
    private javax.swing.JComboBox cbListMissionProc;
    private javax.swing.JComboBox cbListProc;
    private javax.swing.JComboBox cbProcInit;
    private javax.swing.JLabel labelOpenProcMission;
    private javax.swing.JLabel labelOpenProcProc;
    private javax.swing.JLabel labelProcNameCopy;
    private javax.swing.JLabel labelProcNameCreate;
    private javax.swing.JRadioButton rbCopyProc;
    private javax.swing.JRadioButton rbNewProc;
    private javax.swing.JRadioButton rbOpenProc;
    private javax.swing.JTextField textFieldProcName;
    private javax.swing.JTextField textFieldProcNameCopy;
    // End of variables declaration//GEN-END:variables

}

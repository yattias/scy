/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * EditDataHeaderDialog.java
 *
 * Created on 6 aout 2009, 15:52:50
 */

package eu.scy.client.tools.dataProcessTool.dataTool;
import eu.scy.client.tools.dataProcessTool.common.DataHeader;
import eu.scy.client.tools.dataProcessTool.common.Dataset;
import eu.scy.client.tools.dataProcessTool.utilities.CopexReturn;
import eu.scy.client.tools.dataProcessTool.utilities.DataConstants;
import eu.scy.client.tools.dataProcessTool.utilities.MyTextFieldCompletion;
import eu.scy.client.tools.dataProcessTool.utilities.MyUtilities;
import java.awt.Dimension;
import java.util.List;

/**
 * edit data header : name and unit, description
 * @author Marjolaine
 */
public class EditDataHeaderDialog extends javax.swing.JDialog {
    private FitexToolPanel owner;
    private char right;
    private DataHeader header;
    private Dataset dataset;
    private int noCol;
    private boolean isUnitMode;

    private MyTextFieldCompletion fieldFormula;
    private List<String> words;


    public EditDataHeaderDialog(FitexToolPanel owner, Dataset dataset, DataHeader header, int noCol, char right, List<String> words) {
        super();
        this.owner = owner;
        this.header = header;
        this.right = right;
        this.dataset = dataset;
        this.noCol = noCol;
        this.isUnitMode = true;
        this.words = words;
        setLocationRelativeTo(owner);
        initComponents();
        initGUI();
        setLocationRelativeTo(owner);
    }


    
    /** Creates new form EditDataHeaderDialog */
    public EditDataHeaderDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }


    /* initialization */
    private void initGUI(){
        fieldFormula = new MyTextFieldCompletion(words);
        getContentPane().add(fieldFormula);
        //fieldFormula.setBounds(120, 125, 210, 30);
        fieldFormula.setBounds(120, 185, 210, 25);
        // resize
        this.labelHeaderName.setSize(MyUtilities.lenghtOfString(this.labelHeaderName.getText(), labelHeaderName.getFontMetrics(this.labelHeaderName.getFont())), this.labelHeaderName.getHeight());
        this.labelUnit.setSize(MyUtilities.lenghtOfString(this.labelUnit.getText(), labelUnit.getFontMetrics(this.labelUnit.getFont())), this.labelUnit.getHeight());
        this.labelDescription.setSize(MyUtilities.lenghtOfString(this.labelDescription.getText(), labelDescription.getFontMetrics(this.labelUnit.getFont())), this.labelDescription.getHeight());
        this.labelType.setSize(MyUtilities.lenghtOfString(this.labelType.getText(), labelType.getFontMetrics(this.labelType.getFont())), this.labelType.getHeight());
        this.rbTypeDouble.setSize(40+MyUtilities.lenghtOfString(this.rbTypeDouble.getText(), rbTypeDouble.getFontMetrics(this.rbTypeDouble.getFont())), this.rbTypeDouble.getHeight());
        this.rbTypeString.setSize(40+MyUtilities.lenghtOfString(this.rbTypeString.getText(), rbTypeString.getFontMetrics(this.rbTypeString.getFont())), this.rbTypeString.getHeight());
        this.labelValue.setSize(MyUtilities.lenghtOfString(this.labelValue.getText(), labelValue.getFontMetrics(this.labelValue.getFont())), this.labelValue.getHeight());
        this.rbValueFree.setSize(40+MyUtilities.lenghtOfString(this.rbValueFree.getText(), rbValueFree.getFontMetrics(this.rbValueFree.getFont())), this.rbValueFree.getHeight());
        this.rbValueFormula.setSize(40+MyUtilities.lenghtOfString(this.rbValueFormula.getText(), rbValueFormula.getFontMetrics(this.rbValueFormula.getFont())), this.rbValueFormula.getHeight());
        this.cbScientificNotation.setSize(40+MyUtilities.lenghtOfString(this.cbScientificNotation.getText(), this.cbScientificNotation.getFontMetrics(this.cbScientificNotation.getFont())), this.cbScientificNotation.getHeight());
        this.labelSignificantDigit.setSize(MyUtilities.lenghtOfString(this.labelSignificantDigit.getText(), labelSignificantDigit.getFontMetrics(this.labelSignificantDigit.getFont())), this.labelSignificantDigit.getHeight());
        int posx = Math.max(labelHeaderName.getX()+labelHeaderName.getWidth(), labelUnit.getX()+labelUnit.getWidth());
        posx = Math.max(posx, labelDescription.getX()+labelDescription.getWidth());
        posx = Math.max(posx, labelType.getX()+labelType.getWidth());
        posx = Math.max(posx, labelValue.getX()+labelValue.getWidth());
        posx = Math.max(posx, labelSignificantDigit.getX()+labelSignificantDigit.getWidth());
        posx += 10;
        this.textFieldHeaderName.setBounds(posx, textFieldHeaderName.getY()-4, textFieldHeaderName.getWidth(), textFieldHeaderName.getHeight());
        this.textFieldUnit.setBounds(posx, textFieldUnit.getY()-4, textFieldUnit.getWidth(), textFieldUnit.getHeight());
        this.rbTypeDouble.setBounds(posx, rbTypeDouble.getY(), rbTypeDouble.getWidth(), rbTypeDouble.getHeight());
        this.rbTypeString.setBounds(rbTypeDouble.getX()+rbTypeDouble.getWidth()+5, rbTypeString.getY(), rbTypeString.getWidth(), rbTypeString.getHeight());
        this.rbValueFree.setBounds(posx, rbValueFree.getY(), rbValueFree.getWidth(), rbValueFree.getHeight());
        this.rbValueFormula.setBounds(rbValueFree.getX()+rbValueFree.getWidth()+5, rbValueFormula.getY(), rbValueFormula.getWidth(), rbValueFormula.getHeight());
        this.labelEqual.setBounds(posx, labelEqual.getY(), labelEqual.getWidth(), labelEqual.getHeight());
        this.fieldFormula.setBounds(labelEqual.getX()+labelEqual.getWidth()+5, fieldFormula.getY(), fieldFormula.getWidth(), fieldFormula.getHeight());
        this.scrollPaneDescription.setBounds(posx, scrollPaneDescription.getY(), scrollPaneDescription.getWidth(), scrollPaneDescription.getHeight());
        //this.textFieldSignificantDigit.setBounds(Math.max(rbValueFormula.getX(), this.labelSignificantDigit.getX()+labelSignificantDigit.getWidth()), textFieldSignificantDigit.getY(), textFieldSignificantDigit.getWidth(), textFieldSignificantDigit.getHeight());
        this.textFieldSignificantDigit.setBounds(posx, textFieldSignificantDigit.getY(), textFieldSignificantDigit.getWidth(), textFieldSignificantDigit.getHeight());
        resizeDialog();
        if (right == DataConstants.NONE_RIGHT){
            this.textFieldHeaderName.setEnabled(false);
            this.textFieldUnit.setEnabled(false);
            this.areaDescription.setEnabled(false);
            this.rbTypeDouble.setEnabled(false);
            this.rbTypeString.setEnabled(false);
            this.rbValueFormula.setEnabled(false);
            this.rbValueFree.setEnabled(false);
            this.fieldFormula.setEnabled(false);
            this.cbScientificNotation.setEnabled(false);
            this.textFieldSignificantDigit.setEnabled(false);
            this.remove(this.buttonOk);
            buttonOk = null;
            this.buttonCancel.setText(owner.getBundleString("BUTTON_OK"));
            posx =  (getWidth() - buttonCancel.getWidth()) /2 ;
            this.buttonCancel.setBounds(posx, this.buttonCancel.getY(), this.buttonCancel.getWidth(), this.buttonCancel.getHeight());
        }
        initHeader();
        repaint();
    }

    private void resizeDialog(){
        int width = 300;
        width = Math.max(textFieldHeaderName.getX()+textFieldHeaderName.getWidth(), textFieldUnit.getX()+textFieldUnit.getWidth());
        width = Math.max(width, rbValueFormula.getX()+rbValueFormula.getWidth());
        width = Math.max(width, rbTypeString.getX()+rbTypeString.getWidth());
        width = Math.max(width, labelSignificantDigit.getX()+labelSignificantDigit.getWidth());
        width = Math.max(width, textFieldSignificantDigit.getX()+textFieldSignificantDigit.getWidth());
        width = Math.max(width, scrollPaneDescription.getX()+scrollPaneDescription.getWidth());
        width += 20;
        this.setSize(width, 180);
        this.setPreferredSize(getSize());
    }
    private void initHeader(){
        if(header != null){
            this.textFieldHeaderName.setText(header.getValue());
            if(header.getUnit() != null)
                this.textFieldUnit.setText(header.getUnit());
            this.rbValueFormula.setSelected(header.isFormula());
            rbValueFree.setSelected(!header.isFormula());
            fieldFormula.setEnabled(header.isFormula());
            if(header.getFormulaValue() != null )
                fieldFormula.setText(header.getFormulaValue());
            this.cbScientificNotation.setSelected(header.isScientificNotation());
            String s = "";
            if(header.isScientificNotation()){
                s = (header.getNbSignificantDigits() == DataConstants.NB_SIGNIFICANT_DIGITS_UNDEFINED) ? "" :Integer.toString(header.getNbSignificantDigits());
            }else{
                s = (header.getNbShownDecimals() == DataConstants.NB_DECIMAL_UNDEFINED) ? "" :Integer.toString(header.getNbShownDecimals());
            }
            this.textFieldSignificantDigit.setText(s);
            this.areaDescription.setText(header.getDescription());
            if(header.getType().equalsIgnoreCase(DataConstants.TYPE_DOUBLE)){
                rbTypeDouble.setSelected(true);
                rbTypeString.setSelected(false);
            }else if (header.getType().equalsIgnoreCase(DataConstants.TYPE_STRING)){
                rbTypeDouble.setSelected(false);
                rbTypeString.setSelected(true );
            }
            updateScientificNotation();
            updateType();
        }
    }
    /* mise a jour du type */
    private void updateType(){
        isUnitMode = rbTypeDouble.isSelected();
        labelUnit.setVisible(isUnitMode);
        textFieldUnit.setVisible(isUnitMode);
        labelValue.setVisible(isUnitMode);
        rbValueFree.setVisible(isUnitMode);
        rbValueFormula.setVisible(isUnitMode);
        labelEqual.setVisible(isUnitMode);
        fieldFormula.setVisible(isUnitMode);
        cbScientificNotation.setVisible(isUnitMode);
        labelSignificantDigit.setVisible(isUnitMode);
        textFieldSignificantDigit.setVisible(isUnitMode);
        int posy = labelType.getY()+labelType.getHeight()+10;
        if(isUnitMode){
            //posy = labelSignificantDigit.getY()+labelSignificantDigit.getHeight()+10;
            posy = labelEqual.getY()+labelEqual.getHeight()+10;
        }
        labelDescription.setBounds(labelDescription.getX(), posy, labelDescription.getWidth(), labelDescription.getHeight());
        scrollPaneDescription.setBounds(scrollPaneDescription.getX(), posy, scrollPaneDescription.getWidth(), scrollPaneDescription.getHeight());
        if(buttonOk != null)
            buttonOk.setBounds(buttonOk.getX(), scrollPaneDescription.getY()+scrollPaneDescription.getHeight()+20, buttonOk.getWidth(), buttonOk.getHeight());
        buttonCancel.setBounds(buttonCancel.getX(), scrollPaneDescription.getY()+scrollPaneDescription.getHeight()+20, buttonCancel.getWidth(), buttonCancel.getHeight());
        Dimension dim = new Dimension(this.getWidth(), buttonCancel.getY()+buttonCancel.getHeight()+40);
        this.setMinimumSize(dim);
        this.setSize(dim);
        repaint();
    }

    private void updateScientificNotation(){
        this.labelSignificantDigit.setText(cbScientificNotation.isSelected() ? owner.getBundleString("LABEL_SIGNIFICANT_DIGITS") : owner.getBundleString("LABEL_DECIMAL_NUMBER"));
        this.labelSignificantDigit.setSize(MyUtilities.lenghtOfString(this.labelSignificantDigit.getText(), labelSignificantDigit.getFontMetrics(this.labelSignificantDigit.getFont())), this.labelSignificantDigit.getHeight());
        //this.textFieldSignificantDigit.setBounds(Math.max(rbValueFormula.getX(), this.labelSignificantDigit.getX()+labelSignificantDigit.getWidth()), textFieldSignificantDigit.getY(), textFieldSignificantDigit.getWidth(), textFieldSignificantDigit.getHeight());
        
        resizeDialog();
    }

    /* validation */
    private void validEditDataHeader(){
        // header name
        String name = textFieldHeaderName.getText();
        if (controlLenght() && name.length() > DataConstants.MAX_LENGHT_DATAHEADER_NAME){
            String msg = owner.getBundleString("MSG_LENGHT_MAX");
            msg  = MyUtilities.replace(msg, 0, owner.getBundleString("LABEL_HEADER_NAME"));
            msg = MyUtilities.replace(msg, 1, ""+DataConstants.MAX_LENGHT_DATAHEADER_NAME);
            owner.displayError(new CopexReturn(msg, false), owner.getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }
        if (name.length() == 0){
            String msg = owner.getBundleString("MSG_ERROR_FIELD_NULL");
            msg  = MyUtilities.replace(msg, 0, owner.getBundleString("LABEL_HEADER_NAME"));
            owner.displayError(new CopexReturn(msg ,false), owner.getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }
        // unit
        String unit = textFieldUnit.getText();
        if (controlLenght() && unit.length() > DataConstants.MAX_LENGHT_UNIT){
            String msg = owner.getBundleString("MSG_LENGHT_MAX");
            msg  = MyUtilities.replace(msg, 0, owner.getBundleString("LABEL_HEADER_UNIT"));
            msg = MyUtilities.replace(msg, 1, ""+DataConstants.MAX_LENGHT_UNIT);
            owner.displayError(new CopexReturn(msg, false), owner.getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }
        // formula
        boolean isFormula = rbValueFormula.isSelected();
        String formula = null;
        if(isFormula){
            formula = fieldFormula.getText();
            if (formula.length() == 0){
                String msg = owner.getBundleString("MSG_ERROR_FIELD_NULL");
                msg  = MyUtilities.replace(msg, 0, owner.getBundleString("LABEL_HEADER_VALUE_FORMULA"));
                owner.displayError(new CopexReturn(msg ,false), owner.getBundleString("TITLE_DIALOG_ERROR"));
                return;
            }
            if (controlLenght() && formula.length() > DataConstants.MAX_LENGHT_DATAHEADER_FORMULA){
                String msg = owner.getBundleString("MSG_LENGHT_MAX");
                msg  = MyUtilities.replace(msg, 0, owner.getBundleString("LABEL_HEADER_VALUE_FORMULA"));
                msg = MyUtilities.replace(msg, 1, ""+DataConstants.MAX_LENGHT_DATAHEADER_FORMULA);
                owner.displayError(new CopexReturn(msg, false), owner.getBundleString("TITLE_DIALOG_ERROR"));
                return;
            }
        }
        // description
        String description = areaDescription.getText();
        if(controlLenght() && description.length() > DataConstants.MAX_LENGHT_DATAHEADER_DESCRIPTION){
            String msg = owner.getBundleString("MSG_LENGHT_MAX");
            msg  = MyUtilities.replace(msg, 0, owner.getBundleString("LABEL_HEADER_DESCRIPTION"));
            msg = MyUtilities.replace(msg, 1, ""+DataConstants.MAX_LENGHT_DATAHEADER_DESCRIPTION);
            owner.displayError(new CopexReturn(msg, false), owner.getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }
        // type
        String type = DataConstants.TYPE_DOUBLE;
        if(rbTypeString.isSelected()){
            type =  DataConstants.TYPE_STRING;
            unit = null;
        }
        // scientificNotation
        boolean scientificNotation = cbScientificNotation.isSelected();
        int nbShownDecimals = DataConstants.NB_DECIMAL_UNDEFINED;
        int nbSignificantDigits = DataConstants.NB_SIGNIFICANT_DIGITS_UNDEFINED;
        String s = textFieldSignificantDigit.getText();
        if(s != null && !s.trim().isEmpty()){
            try{
                int nb = Integer.parseInt(s);
                if(scientificNotation){
                    if(nb <= 0){
                        owner.displayError(new CopexReturn(owner.getBundleString("MSG_ERROR_SIGNIFICANT_DIGITS_POS"), false), owner.getBundleString("TITLE_DIALOG_ERROR"));
                        return;
                    }
                    nbSignificantDigits = nb;
                }else{
                    if(nb < 0){
                        owner.displayError(new CopexReturn(owner.getBundleString("MSG_ERROR_SHOWN_DECIMALS_POS"), false), owner.getBundleString("TITLE_DIALOG_ERROR"));
                        return;
                    }
                    nbShownDecimals = nb;
                }
            }catch(NumberFormatException e){
                String msg = owner.getBundleString("MSG_ERROR_NB_SHOWN_DECIMALS");
                if(scientificNotation)
                    msg = owner.getBundleString("MSG_ERROR_NB_SIGNIFICANT_DIGITS");
                    owner.displayError(new CopexReturn(msg, false), owner.getBundleString("TITLE_DIALOG_ERROR"));
                    return;
            }
        }
        // validation
        boolean isOk = owner.updateDataHeader(dataset, name, unit, noCol, description, type, formula, scientificNotation, nbShownDecimals, nbSignificantDigits, true);
        if(isOk)
            this.dispose();
    }

    private boolean controlLenght(){
        return owner.controlLenght();
    }



    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        labelHeaderName = new javax.swing.JLabel();
        textFieldHeaderName = new javax.swing.JTextField();
        labelUnit = new javax.swing.JLabel();
        textFieldUnit = new javax.swing.JTextField();
        buttonCancel = new javax.swing.JButton();
        buttonOk = new javax.swing.JButton();
        labelDescription = new javax.swing.JLabel();
        labelType = new javax.swing.JLabel();
        scrollPaneDescription = new javax.swing.JScrollPane();
        areaDescription = new javax.swing.JTextArea();
        labelValue = new javax.swing.JLabel();
        rbValueFree = new javax.swing.JRadioButton();
        rbValueFormula = new javax.swing.JRadioButton();
        labelEqual = new javax.swing.JLabel();
        rbTypeDouble = new javax.swing.JRadioButton();
        rbTypeString = new javax.swing.JRadioButton();
        cbScientificNotation = new javax.swing.JCheckBox();
        labelSignificantDigit = new javax.swing.JLabel();
        textFieldSignificantDigit = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(owner.getBundleString("TITLE_DIALOG_EDIT_DATAHEADER"));
        setMinimumSize(new java.awt.Dimension(350, 280));
        setModal(true);
        setResizable(false);
        getContentPane().setLayout(null);

        labelHeaderName.setFont(new java.awt.Font("Tahoma", 1, 11));
        labelHeaderName.setText(owner.getBundleString("LABEL_HEADER_NAME"));
        getContentPane().add(labelHeaderName);
        labelHeaderName.setBounds(10, 10, 75, 14);

        textFieldHeaderName.setPreferredSize(new java.awt.Dimension(6, 22));
        getContentPane().add(textFieldHeaderName);
        textFieldHeaderName.setBounds(110, 5, 180, 25);

        labelUnit.setFont(new java.awt.Font("Tahoma", 1, 11));
        labelUnit.setText(owner.getBundleString("LABEL_HEADER_UNIT"));
        getContentPane().add(labelUnit);
        labelUnit.setBounds(10, 130, 75, 14);

        textFieldUnit.setPreferredSize(new java.awt.Dimension(6, 22));
        getContentPane().add(textFieldUnit);
        textFieldUnit.setBounds(110, 125, 180, 25);

        buttonCancel.setText(owner.getBundleString("BUTTON_CANCEL"));
        buttonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonCancelActionPerformed(evt);
            }
        });
        getContentPane().add(buttonCancel);
        buttonCancel.setBounds(210, 300, 99, 23);

        buttonOk.setText(owner.getBundleString("BUTTON_OK"));
        buttonOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonOkActionPerformed(evt);
            }
        });
        getContentPane().add(buttonOk);
        buttonOk.setBounds(50, 300, 99, 23);

        labelDescription.setFont(new java.awt.Font("Tahoma", 1, 11));
        labelDescription.setText(owner.getBundleString("LABEL_HEADER_DESCRIPTION"));
        getContentPane().add(labelDescription);
        labelDescription.setBounds(10, 230, 75, 14);

        labelType.setFont(new java.awt.Font("Tahoma", 1, 11));
        labelType.setText(owner.getBundleString("LABEL_HEADER_TYPE"));
        getContentPane().add(labelType);
        labelType.setBounds(10, 40, 75, 14);

        areaDescription.setColumns(20);
        areaDescription.setLineWrap(true);
        areaDescription.setRows(3);
        areaDescription.setWrapStyleWord(true);
        scrollPaneDescription.setViewportView(areaDescription);

        getContentPane().add(scrollPaneDescription);
        scrollPaneDescription.setBounds(110, 230, 220, 60);

        labelValue.setFont(new java.awt.Font("Tahoma", 1, 11));
        labelValue.setText(owner.getBundleString("LABEL_HEADER_VALUE"));
        getContentPane().add(labelValue);
        labelValue.setBounds(10, 170, 75, 14);

        rbValueFree.setSelected(true);
        rbValueFree.setText(owner.getBundleString("LABEL_HEADER_VALUE_FREE"));
        rbValueFree.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbValueFreeActionPerformed(evt);
            }
        });
        getContentPane().add(rbValueFree);
        rbValueFree.setBounds(110, 165, 91, 23);

        rbValueFormula.setText(owner.getBundleString("LABEL_HEADER_VALUE_FORMULA"));
        rbValueFormula.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbValueFormulaActionPerformed(evt);
            }
        });
        getContentPane().add(rbValueFormula);
        rbValueFormula.setBounds(210, 165, 91, 23);

        labelEqual.setFont(new java.awt.Font("Tahoma", 1, 11));
        labelEqual.setText("=");
        getContentPane().add(labelEqual);
        labelEqual.setBounds(110, 190, 10, 14);

        rbTypeDouble.setSelected(true);
        rbTypeDouble.setText(owner.getBundleString("LABEL_HEADER_TYPE_DOUBLE"));
        rbTypeDouble.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbTypeDoubleActionPerformed(evt);
            }
        });
        getContentPane().add(rbTypeDouble);
        rbTypeDouble.setBounds(110, 35, 91, 23);

        rbTypeString.setText(owner.getBundleString("LABEL_HEADER_TYPE_STRING"));
        rbTypeString.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbTypeStringActionPerformed(evt);
            }
        });
        getContentPane().add(rbTypeString);
        rbTypeString.setBounds(210, 35, 91, 23);

        cbScientificNotation.setFont(new java.awt.Font("Tahoma", 1, 11));
        cbScientificNotation.setText(owner.getBundleString("LABEL_SCIENTIFIC_NOTATION"));
        cbScientificNotation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbScientificNotationActionPerformed(evt);
            }
        });
        getContentPane().add(cbScientificNotation);
        cbScientificNotation.setBounds(10, 60, 110, 23);

        labelSignificantDigit.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        labelSignificantDigit.setText(owner.getBundleString("LABEL_DECIMAL_NUMBER"));
        getContentPane().add(labelSignificantDigit);
        labelSignificantDigit.setBounds(10, 90, 100, 14);
        getContentPane().add(textFieldSignificantDigit);
        textFieldSignificantDigit.setBounds(110, 85, 70, 25);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void buttonOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonOkActionPerformed
        validEditDataHeader();
    }//GEN-LAST:event_buttonOkActionPerformed

    private void buttonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonCancelActionPerformed
        this.dispose();
    }//GEN-LAST:event_buttonCancelActionPerformed

    private void rbValueFreeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbValueFreeActionPerformed
        rbValueFree.setSelected(true);
        rbValueFormula.setSelected(false);
        fieldFormula.setText("");
        fieldFormula.setEnabled(false);
    }//GEN-LAST:event_rbValueFreeActionPerformed

    private void rbValueFormulaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbValueFormulaActionPerformed
        rbValueFree.setSelected(false);
        rbValueFormula.setSelected(true);
        fieldFormula.setEnabled(true);
    }//GEN-LAST:event_rbValueFormulaActionPerformed

    private void rbTypeDoubleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbTypeDoubleActionPerformed
        rbTypeDouble.setSelected(true);
        rbTypeString.setSelected(false);
        updateType();
    }//GEN-LAST:event_rbTypeDoubleActionPerformed

    private void rbTypeStringActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbTypeStringActionPerformed
        rbTypeDouble.setSelected(false);
        rbTypeString.setSelected(true);
        updateType();
    }//GEN-LAST:event_rbTypeStringActionPerformed

    private void cbScientificNotationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbScientificNotationActionPerformed
        updateScientificNotation();
    }//GEN-LAST:event_cbScientificNotationActionPerformed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                EditDataHeaderDialog dialog = new EditDataHeaderDialog(new javax.swing.JFrame(), true);
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
    private javax.swing.JTextArea areaDescription;
    private javax.swing.JButton buttonCancel;
    private javax.swing.JButton buttonOk;
    private javax.swing.JCheckBox cbScientificNotation;
    private javax.swing.JLabel labelDescription;
    private javax.swing.JLabel labelEqual;
    private javax.swing.JLabel labelHeaderName;
    private javax.swing.JLabel labelSignificantDigit;
    private javax.swing.JLabel labelType;
    private javax.swing.JLabel labelUnit;
    private javax.swing.JLabel labelValue;
    private javax.swing.JRadioButton rbTypeDouble;
    private javax.swing.JRadioButton rbTypeString;
    private javax.swing.JRadioButton rbValueFormula;
    private javax.swing.JRadioButton rbValueFree;
    private javax.swing.JScrollPane scrollPaneDescription;
    private javax.swing.JTextField textFieldHeaderName;
    private javax.swing.JTextField textFieldSignificantDigit;
    private javax.swing.JTextField textFieldUnit;
    // End of variables declaration//GEN-END:variables

}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * EditDataHeaderDialog.java
 *
 * Created on 6 aout 2009, 15:52:50
 */

package eu.scy.tools.dataProcessTool.dataTool;

import eu.scy.tools.dataProcessTool.common.DataHeader;
import eu.scy.tools.dataProcessTool.common.Dataset;
import eu.scy.tools.dataProcessTool.utilities.CopexReturn;
import eu.scy.tools.dataProcessTool.utilities.DataConstants;
import eu.scy.tools.dataProcessTool.utilities.MyUtilities;
import java.awt.Dimension;

/**
 * edit data header : name and unit
 * @author Marjolaine
 */
public class EditDataHeaderDialog extends javax.swing.JDialog {
    private FitexToolPanel owner;
    private char right;
    private DataHeader header;
    private Dataset dataset;
    private int noCol;
    private boolean isUnitMode;


    public EditDataHeaderDialog(FitexToolPanel owner, Dataset dataset, DataHeader header, int noCol, char right) {
        super();
        this.owner = owner;
        this.header = header;
        this.right = right;
        this.dataset = dataset;
        this.noCol = noCol;
        this.isUnitMode = true;
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


    /* initialisation */
    private void initGUI(){
        // init of cb type
        String doubleS = owner.getBundleString("LABEL_HEADER_TYPE_DOUBLE");
        cbType.addItem(doubleS);
        String stringS = owner.getBundleString("LABEL_HEADER_TYPE_STRING");
        cbType.addItem(stringS);
        cbType.setSelectedIndex(0);
        // resize
        this.labelHeaderName.setSize(MyUtilities.lenghtOfString(this.labelHeaderName.getText(), labelHeaderName.getFontMetrics(this.labelHeaderName.getFont())), this.labelHeaderName.getHeight());
        this.labelUnit.setSize(MyUtilities.lenghtOfString(this.labelUnit.getText(), labelUnit.getFontMetrics(this.labelUnit.getFont())), this.labelUnit.getHeight());
        this.labelDescription.setSize(MyUtilities.lenghtOfString(this.labelDescription.getText(), labelDescription.getFontMetrics(this.labelUnit.getFont())), this.labelDescription.getHeight());
        this.labelType.setSize(MyUtilities.lenghtOfString(this.labelType.getText(), labelType.getFontMetrics(this.labelType.getFont())), this.labelType.getHeight());
        int posx = Math.max(labelHeaderName.getX()+labelHeaderName.getWidth(), labelUnit.getX()+labelUnit.getWidth());
        posx = Math.max(posx, labelDescription.getX()+labelDescription.getWidth());
        posx = Math.max(posx, labelType.getX()+labelType.getWidth());
        posx += 10;
        this.textFieldHeaderName.setBounds(posx, textFieldHeaderName.getY()-4, textFieldHeaderName.getWidth(), textFieldHeaderName.getHeight());
        this.textFieldUnit.setBounds(posx, textFieldUnit.getY()-4, textFieldUnit.getWidth(), textFieldUnit.getHeight());
        this.scrollPaneDescription.setBounds(posx, scrollPaneDescription.getY(), scrollPaneDescription.getWidth(), scrollPaneDescription.getHeight());
        this.cbType.setBounds(posx, cbType.getY(), cbType.getWidth(), cbType.getHeight());
        int width = 300;
        width = Math.max(textFieldHeaderName.getX()+textFieldHeaderName.getWidth(), textFieldUnit.getX()+textFieldUnit.getWidth());
        width = Math.max(width, scrollPaneDescription.getX()+scrollPaneDescription.getWidth());
        width = Math.max(width, cbType.getX()+cbType.getWidth());
        width += 20;
        this.setSize(width, 180);
        this.setPreferredSize(getSize());
        if (right == DataConstants.NONE_RIGHT){
            this.textFieldHeaderName.setEnabled(false);
            this.textFieldUnit.setEnabled(false);
            this.areaDescription.setEnabled(false);
            this.cbType.setEnabled(false);
            this.remove(this.buttonOk);
            buttonOk = null;
            this.buttonCancel.setText(owner.getBundleString("BUTTON_OK"));
            posx =  (width - buttonCancel.getWidth()) /2 ;
            this.buttonCancel.setBounds(posx, this.buttonCancel.getY(), this.buttonCancel.getWidth(), this.buttonCancel.getHeight());
        }
        initHeader();
        repaint();
    }

    private void initHeader(){
        if(header != null){
            this.textFieldHeaderName.setText(header.getValue());
            if(header.getUnit() != null)
                this.textFieldUnit.setText(header.getUnit());
            this.areaDescription.setText(header.getDescription());
            if(header.getType().equalsIgnoreCase(DataConstants.TYPE_DOUBLE)){
                cbType.setSelectedIndex(0);
            }else if (header.getType().equalsIgnoreCase(DataConstants.TYPE_STRING)){
                cbType.setSelectedIndex(1);
            }
        }
    }
    /* mise a jour du type */
    private void updateType(){
        int id = cbType.getSelectedIndex();
        isUnitMode = id==0;
        labelUnit.setVisible(isUnitMode);
        textFieldUnit.setVisible(isUnitMode);
        int posy = labelType.getY()+labelType.getHeight()+10;
        if(isUnitMode){
            posy = labelUnit.getY()+labelUnit.getHeight()+10;
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

    /* validation */
    private void validEditDataHeader(){
        // header name
        String name = textFieldHeaderName.getText();
        if (name.length() > DataConstants.MAX_LENGHT_DATAHEADER_NAME){
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
        if (unit.length() > DataConstants.MAX_LENGHT_UNIT){
            String msg = owner.getBundleString("MSG_LENGHT_MAX");
            msg  = MyUtilities.replace(msg, 0, owner.getBundleString("LABEL_HEADER_UNIT"));
            msg = MyUtilities.replace(msg, 1, ""+DataConstants.MAX_LENGHT_UNIT);
            owner.displayError(new CopexReturn(msg, false), owner.getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }
        // description
        String description = areaDescription.getText();
        if(description.length() > DataConstants.MAX_LENGHT_DATAHEADER_DESCRIPTION){
            String msg = owner.getBundleString("MSG_LENGHT_MAX");
            msg  = MyUtilities.replace(msg, 0, owner.getBundleString("LABEL_HEADER_DESCRIPTION"));
            msg = MyUtilities.replace(msg, 1, ""+DataConstants.MAX_LENGHT_DATAHEADER_DESCRIPTION);
            owner.displayError(new CopexReturn(msg, false), owner.getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }
        // type
        int idT = cbType.getSelectedIndex();
        String type = DataConstants.TYPE_DOUBLE;
        if(idT==1){
            type =  DataConstants.TYPE_STRING;
            unit = null;
        }
        // validation
        boolean isOk = owner.updateDataHeader(dataset, name, unit, noCol, description, type);
        if(isOk)
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
        cbType = new javax.swing.JComboBox();

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
        textFieldHeaderName.setBounds(110, 10, 180, 25);

        labelUnit.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        labelUnit.setText(owner.getBundleString("LABEL_HEADER_UNIT"));
        getContentPane().add(labelUnit);
        labelUnit.setBounds(10, 70, 75, 14);

        textFieldUnit.setPreferredSize(new java.awt.Dimension(6, 22));
        getContentPane().add(textFieldUnit);
        textFieldUnit.setBounds(110, 70, 180, 25);

        buttonCancel.setText(owner.getBundleString("BUTTON_CANCEL"));
        buttonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonCancelActionPerformed(evt);
            }
        });
        getContentPane().add(buttonCancel);
        buttonCancel.setBounds(210, 180, 99, 23);

        buttonOk.setText(owner.getBundleString("BUTTON_OK"));
        buttonOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonOkActionPerformed(evt);
            }
        });
        getContentPane().add(buttonOk);
        buttonOk.setBounds(50, 180, 99, 23);

        labelDescription.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        labelDescription.setText(owner.getBundleString("LABEL_HEADER_DESCRIPTION"));
        getContentPane().add(labelDescription);
        labelDescription.setBounds(10, 110, 75, 14);

        labelType.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        labelType.setText(owner.getBundleString("LABEL_HEADER_TYPE"));
        getContentPane().add(labelType);
        labelType.setBounds(10, 40, 75, 14);

        areaDescription.setColumns(20);
        areaDescription.setLineWrap(true);
        areaDescription.setRows(3);
        areaDescription.setWrapStyleWord(true);
        scrollPaneDescription.setViewportView(areaDescription);

        getContentPane().add(scrollPaneDescription);
        scrollPaneDescription.setBounds(110, 110, 220, 60);

        cbType.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbTypeItemStateChanged(evt);
            }
        });
        getContentPane().add(cbType);
        cbType.setBounds(110, 40, 130, 20);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void buttonOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonOkActionPerformed
        validEditDataHeader();
    }//GEN-LAST:event_buttonOkActionPerformed

    private void buttonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonCancelActionPerformed
        this.dispose();
    }//GEN-LAST:event_buttonCancelActionPerformed

    private void cbTypeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbTypeItemStateChanged
        updateType();
    }//GEN-LAST:event_cbTypeItemStateChanged

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
    private javax.swing.JComboBox cbType;
    private javax.swing.JLabel labelDescription;
    private javax.swing.JLabel labelHeaderName;
    private javax.swing.JLabel labelType;
    private javax.swing.JLabel labelUnit;
    private javax.swing.JScrollPane scrollPaneDescription;
    private javax.swing.JTextField textFieldHeaderName;
    private javax.swing.JTextField textFieldUnit;
    // End of variables declaration//GEN-END:variables

}

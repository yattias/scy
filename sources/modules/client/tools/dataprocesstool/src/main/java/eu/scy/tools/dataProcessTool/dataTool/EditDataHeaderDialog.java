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


    public EditDataHeaderDialog(FitexToolPanel owner, Dataset dataset, DataHeader header, int noCol, char right) {
        super();
        this.owner = owner;
        this.header = header;
        this.right = right;
        this.dataset = dataset;
        this.noCol = noCol;
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
        if(header != null){
            this.textFieldHeaderName.setText(header.getValue());
            this.textFieldUnit.setText(header.getUnit());
        }
        // resize
        this.labelHeaderName.setSize(MyUtilities.lenghtOfString(this.labelHeaderName.getText(), getFontMetrics(this.labelHeaderName.getFont())), this.labelHeaderName.getHeight());
        this.labelUnit.setSize(MyUtilities.lenghtOfString(this.labelUnit.getText(), getFontMetrics(this.labelUnit.getFont())), this.labelUnit.getHeight());
        int posx = Math.max(labelHeaderName.getX()+labelHeaderName.getWidth(), labelUnit.getX()+labelUnit.getWidth());
        posx += 10;
        this.textFieldHeaderName.setBounds(posx, textFieldHeaderName.getY(), textFieldHeaderName.getWidth(), textFieldHeaderName.getHeight());
        this.textFieldUnit.setBounds(posx, textFieldUnit.getY(), textFieldUnit.getWidth(), textFieldUnit.getHeight());
        int width = 300;
        width = Math.max(textFieldHeaderName.getX()+textFieldHeaderName.getWidth(), textFieldUnit.getX()+textFieldUnit.getWidth());
        width += 20;
        this.setSize(width, 180);
        this.setPreferredSize(getSize());
        if (right == DataConstants.NONE_RIGHT){
            this.textFieldHeaderName.setEnabled(false);
            this.textFieldUnit.setEnabled(false);
            this.remove(this.buttonOk);
            buttonOk = null;
            this.buttonCancel.setText(owner.getBundleString("BUTTON_OK"));
            posx =  (width - buttonCancel.getWidth()) /2 ;
            this.buttonCancel.setBounds(posx, this.buttonCancel.getY(), this.buttonCancel.getWidth(), this.buttonCancel.getHeight());
        }
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
        // validation
        boolean isOk = owner.updateDataHeader(dataset, name, unit, noCol);
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

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(owner.getBundleString("TITLE_DIALOG_EDIT_DATAHEADER"));
        setMinimumSize(new java.awt.Dimension(280, 180));
        setModal(true);
        setResizable(false);
        getContentPane().setLayout(null);

        labelHeaderName.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        labelHeaderName.setText(owner.getBundleString("LABEL_HEADER_NAME"));
        getContentPane().add(labelHeaderName);
        labelHeaderName.setBounds(10, 10, 75, 14);
        getContentPane().add(textFieldHeaderName);
        textFieldHeaderName.setBounds(110, 10, 180, 20);

        labelUnit.setFont(new java.awt.Font("Tahoma", 1, 11));
        labelUnit.setText(owner.getBundleString("LABEL_HEADER_UNIT"));
        getContentPane().add(labelUnit);
        labelUnit.setBounds(10, 40, 75, 14);
        getContentPane().add(textFieldUnit);
        textFieldUnit.setBounds(110, 40, 180, 20);

        buttonCancel.setText(owner.getBundleString("BUTTON_CANCEL"));
        buttonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonCancelActionPerformed(evt);
            }
        });
        getContentPane().add(buttonCancel);
        buttonCancel.setBounds(170, 110, 99, 23);

        buttonOk.setText(owner.getBundleString("BUTTON_OK"));
        buttonOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonOkActionPerformed(evt);
            }
        });
        getContentPane().add(buttonOk);
        buttonOk.setBounds(30, 110, 99, 23);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void buttonOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonOkActionPerformed
        validEditDataHeader();
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
    private javax.swing.JButton buttonCancel;
    private javax.swing.JButton buttonOk;
    private javax.swing.JLabel labelHeaderName;
    private javax.swing.JLabel labelUnit;
    private javax.swing.JTextField textFieldHeaderName;
    private javax.swing.JTextField textFieldUnit;
    // End of variables declaration//GEN-END:variables

}

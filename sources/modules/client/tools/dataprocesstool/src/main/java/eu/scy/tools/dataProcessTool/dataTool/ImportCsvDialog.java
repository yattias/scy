/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


package eu.scy.tools.dataProcessTool.dataTool;

import eu.scy.tools.dataProcessTool.utilities.DataConstants;
import eu.scy.tools.dataProcessTool.utilities.MyUtilities;
import java.awt.FontMetrics;

/**
 * importCsvDialog to determine the separator field (; or,) and the separator text (nothing or "")
 * @author Marjolaine
 */
public class ImportCsvDialog extends javax.swing.JDialog {
    private FitexToolPanel owner;

    private String[] listCharEncode = {"UTF-8", "ISO-8859-1", "ISO-8859-2","ISO-8859-4", "ISO-8859-7" };
    private String[] listEncodeName = {"Unicode (UTF8)", "Western Europe", "Western and Central Europe", "Western Europe and Baltic countries", "Greek"};

    public ImportCsvDialog(FitexToolPanel owner) {
        super();
        this.owner = owner;
        initComponents();
        setLocation(owner.getLocationDialog());
        setModal(true);
        setModalityType(DEFAULT_MODALITY_TYPE);
        setResizable(false);
        initGUI();
    }
    

    /** Creates new form ImportCsvDialog */
    public ImportCsvDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    private void initGUI(){
        labelEncode.setSize(MyUtilities.lenghtOfString(this.labelEncode.getText(), getFontMetrics(this.labelEncode.getFont())), labelEncode.getHeight());
        labelSepField.setSize(MyUtilities.lenghtOfString(this.labelSepField.getText(), getFontMetrics(this.labelSepField.getFont())), labelSepField.getHeight());
        labelSepText.setSize(MyUtilities.lenghtOfString(this.labelSepText.getText(), getFontMetrics(this.labelSepText.getFont())), labelSepText.getHeight());
        for(int k=0; k<listEncodeName.length; k++){
            cbEncode.addItem(listEncodeName[k]);
        }
        cbSeparatorField.addItem(DataConstants.CSV_SEPARATOR_COMMA);
        cbSeparatorField.addItem(DataConstants.CSV_SEPARATOR_SEMICOLON);
        cbSeparatorText.addItem(DataConstants.CSV_SEPARATOR_TEXT);
        cbSeparatorText.addItem(DataConstants.CSV_SEPARATOR_TEXT_QUOTATION_MARK);

        int posx = Math.max(labelSepField.getWidth(), labelSepText.getWidth());
        posx = Math.max(labelEncode.getWidth(), posx);
        int max = getMaxWidth(listEncodeName, cbEncode.getFontMetrics(cbEncode.getFont()));
        cbEncode.setSize(max, cbEncode.getHeight());
        cbEncode.setBounds(posx+10, cbEncode.getY(), cbEncode.getWidth(), cbEncode.getHeight());
        cbSeparatorField.setBounds(posx+10, cbSeparatorField.getY(), cbSeparatorField.getWidth(), cbSeparatorField.getHeight());
        cbSeparatorText.setBounds(posx+10, cbSeparatorText.getY(), cbSeparatorText.getWidth(), cbSeparatorText.getHeight());
        int w = cbEncode.getX()+cbEncode.getWidth()+10;
        int h = buttonCancel.getHeight()+buttonCancel.getY()+40;
        setSize(w,h);
        buttonOk.setSize(60+MyUtilities.lenghtOfString(this.buttonOk.getText(), getFontMetrics(this.buttonOk.getFont())), buttonOk.getHeight());
        buttonCancel.setSize(60+MyUtilities.lenghtOfString(this.buttonCancel.getText(), getFontMetrics(this.buttonCancel.getFont())), buttonCancel.getHeight());
    }

    private int getMaxWidth(String[] list, FontMetrics fm){
         int max = 0;
         for(int k=0; k<list.length; k++){
             max = Math.max(max,MyUtilities.lenghtOfString(MyUtilities.getTextWithoutHTML(list[k]), fm));
         }
         return max+40;
     }

    private void updateSeparatorField(){
        String sepField = (String)cbSeparatorField.getSelectedItem();
        boolean displayText = sepField.equals(DataConstants.CSV_SEPARATOR_COMMA);
        labelSepText.setVisible(displayText);
        cbSeparatorText.setVisible(displayText);
        repaint();
    }

    private void doImport(){
        String sepField = (String)cbSeparatorField.getSelectedItem();
        String sepText = null;
        if(sepField.equals(DataConstants.CSV_SEPARATOR_COMMA)){
            sepText = (String)cbSeparatorText.getSelectedItem();
        }
        String charEncode = listCharEncode[cbEncode.getSelectedIndex()];
        owner.importCsvData(sepField, sepText, charEncode);
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

        labelSepField = new javax.swing.JLabel();
        cbSeparatorField = new javax.swing.JComboBox();
        labelSepText = new javax.swing.JLabel();
        cbSeparatorText = new javax.swing.JComboBox();
        buttonOk = new javax.swing.JButton();
        buttonCancel = new javax.swing.JButton();
        labelEncode = new javax.swing.JLabel();
        cbEncode = new javax.swing.JComboBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(owner.getBundleString("TITLE_DIALOG_IMPORT"));
        setModal(true);
        setResizable(false);
        getContentPane().setLayout(null);

        labelSepField.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        labelSepField.setText(owner.getBundleString("LABEL_SEPARATOR_FIELD"));
        getContentPane().add(labelSepField);
        labelSepField.setBounds(10, 50, 75, 14);

        cbSeparatorField.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbSeparatorFieldItemStateChanged(evt);
            }
        });
        getContentPane().add(cbSeparatorField);
        cbSeparatorField.setBounds(103, 47, 60, 20);

        labelSepText.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        labelSepText.setText(owner.getBundleString("LABEL_SEPARATOR_TEXT"));
        getContentPane().add(labelSepText);
        labelSepText.setBounds(10, 88, 75, 14);

        getContentPane().add(cbSeparatorText);
        cbSeparatorText.setBounds(103, 85, 60, 20);

        buttonOk.setText(owner.getBundleString("BUTTON_OK"));
        buttonOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonOkActionPerformed(evt);
            }
        });
        getContentPane().add(buttonOk);
        buttonOk.setBounds(41, 132, 99, 23);

        buttonCancel.setText(owner.getBundleString("BUTTON_CANCEL"));
        buttonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonCancelActionPerformed(evt);
            }
        });
        getContentPane().add(buttonCancel);
        buttonCancel.setBounds(191, 132, 99, 23);

        labelEncode.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        labelEncode.setText(owner.getBundleString("LABEL_CHAR_ENCODE"));
        getContentPane().add(labelEncode);
        labelEncode.setBounds(10, 19, 75, 14);

        cbEncode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbEncodeActionPerformed(evt);
            }
        });
        getContentPane().add(cbEncode);
        cbEncode.setBounds(103, 16, 28, 20);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void buttonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonCancelActionPerformed
        owner.importCsvData(null, null, null);
        this.dispose();
    }//GEN-LAST:event_buttonCancelActionPerformed

    private void buttonOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonOkActionPerformed
        doImport();
    }//GEN-LAST:event_buttonOkActionPerformed

    private void cbSeparatorFieldItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbSeparatorFieldItemStateChanged
        updateSeparatorField();
    }//GEN-LAST:event_cbSeparatorFieldItemStateChanged

    private void cbEncodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbEncodeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbEncodeActionPerformed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                ImportCsvDialog dialog = new ImportCsvDialog(new javax.swing.JFrame(), true);
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
    private javax.swing.JComboBox cbEncode;
    private javax.swing.JComboBox cbSeparatorField;
    private javax.swing.JComboBox cbSeparatorText;
    private javax.swing.JLabel labelEncode;
    private javax.swing.JLabel labelSepField;
    private javax.swing.JLabel labelSepText;
    // End of variables declaration//GEN-END:variables

}

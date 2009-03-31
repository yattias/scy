/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ParamOperationDialog.java
 *
 * Created on 9 févr. 2009, 13:44:10
 */

package eu.scy.tools.dataProcessTool.dataTool;

import eu.scy.tools.dataProcessTool.common.Dataset;
import eu.scy.tools.dataProcessTool.common.TypeOperationParam;
import eu.scy.tools.dataProcessTool.utilities.CopexReturn;
import eu.scy.tools.dataProcessTool.utilities.ScyUtilities;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 * dialog to enter the parameter of an operation
 * @author Marjolaine
 */
public class ParamOperationDialog extends javax.swing.JDialog {

    /* owner */
    private MainDataToolPanel owner ;

    /* operation */
    private TypeOperationParam operation;
    /* largeur max des label */
    private int maxWitdhLabel = 0 ;
    /* liste des textField */
    private JTextField[] fields;

    /* dataset */
    private Dataset ds;
    /* isonCol */
    private boolean isOnCol ;
    /* list No */
    ArrayList<Integer> listNo ;

    // CONSTRUCTOR
    public ParamOperationDialog(MainDataToolPanel owner, TypeOperationParam operation, Dataset ds,  boolean isOnCol, ArrayList<Integer> listNo) {
        super();
        this.owner = owner;
        this.operation = operation;
        this.ds = ds;
        this.isOnCol = isOnCol ;
        this.listNo = listNo;
        initComponents();
        init();
        setLocationRelativeTo(owner);
    }

    /** Creates new form ParamOperationDialog */
    public ParamOperationDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    /* initialisation */
    private void init(){
        String libelle = operation.getLibelle() ;
        int nbParam = operation.getNbParam() ;
        fields = new JTextField[nbParam];
        String s = getLibelle(libelle, 0, nbParam);
        if (s.length() > 0){
            createLabel(s);
        }
        for (int i=0; i<nbParam; i++){
            createTextField(i);
            s = getLibelle(libelle, i+1, nbParam) ;
            if (s.length() > 0)
                createLabel(s);
        }
        int w = maxWitdhLabel+20 ;
        w = Math.max(200,w);
        int h = ((maxWitdhLabel + (nbParam*60)+50)/maxWitdhLabel +1)*30 ;
        panelParam.setSize(w, h);
        panelParam.setPreferredSize(getSize());
        buttonPanel.setSize(w,60);
        buttonPanel.setPreferredSize(getSize());
        setSize(w, panelParam.getHeight()+100);

    }

    /* retourne la partie de libelle */
    private String getLibelle(String libelle, int id, int nbParam){
        int idStart = 0;
        int beginIndex = 0;
        if (id > 0){
            String s = "{"+id+"}";
            idStart = libelle.indexOf(s);
            beginIndex = s.length() + idStart ;
        }
        int idEnd = libelle.length();
        if (id < nbParam){
            idEnd = libelle.indexOf("{"+(id+1)+"}");
        }
        return libelle.substring(beginIndex, idEnd);
    }

    /* creation label */
    private void createLabel(String text){
        JLabel label = new JLabel(text);
        label.setFont(new java.awt.Font("Tahoma", 1, 11) );
        label.setSize(ScyUtilities.lenghtOfString(text, getFontMetrics(label.getFont())), 20);
        maxWitdhLabel = Math.max(maxWitdhLabel, label.getWidth());
        panelParam.add(label);
    }

    /* creation TextField */
    private void createTextField(int id){
        JTextField tf = new JTextField();
        tf.setSize(60, 20);
        tf.setPreferredSize(new Dimension(60,20));
        panelParam.add(tf);
        fields[id] = tf;
    }

    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelParam = new javax.swing.JPanel();
        buttonPanel = new javax.swing.JPanel();
        buttonOk = new javax.swing.JButton();
        buttonCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(owner.getBundleString("TITLE_DIALOG_OP_PARAM"));
        setModal(true);
        setResizable(false);

        panelParam.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
        getContentPane().add(panelParam, java.awt.BorderLayout.CENTER);

        buttonOk.setText(owner.getBundleString("BUTTON_OK"));
        buttonOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonOkActionPerformed(evt);
            }
        });

        buttonCancel.setText(owner.getBundleString("BUTTON_CANCEL"));
        buttonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonCancelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout buttonPanelLayout = new javax.swing.GroupLayout(buttonPanel);
        buttonPanel.setLayout(buttonPanelLayout);
        buttonPanelLayout.setHorizontalGroup(
            buttonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(buttonPanelLayout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addComponent(buttonOk)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 42, Short.MAX_VALUE)
                .addComponent(buttonCancel)
                .addGap(26, 26, 26))
        );
        buttonPanelLayout.setVerticalGroup(
            buttonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, buttonPanelLayout.createSequentialGroup()
                .addContainerGap(89, Short.MAX_VALUE)
                .addGroup(buttonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonOk)
                    .addComponent(buttonCancel))
                .addContainerGap())
        );

        getContentPane().add(buttonPanel, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void buttonOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonOkActionPerformed
        String[] tabVal = new String[operation.getNbParam()];
        // recupere les parametres
        for (int i=0; i<operation.getNbParam(); i++){
            JTextField tf = this.fields[i];
            String s = tf.getText() ;
            double v ;
            if (s != null && s.length() > 0 ){
                try{
                    v = Double.parseDouble(s);
                }catch(NumberFormatException e){
                    owner.displayError(new CopexReturn(owner.getBundleString("MSG_ERROR_DOUBLE_VALUE"), false), owner.getBundleString("TITLE_DIALOG_ERROR"));
                    return;
                }
            }else if (s == null)
                s = "";
            tabVal[i] = s;
        }
        boolean isOk = owner.setParamOperation(ds, operation,isOnCol, listNo, tabVal);
        if (isOk)
            this.dispose();
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
                ParamOperationDialog dialog = new ParamOperationDialog(new javax.swing.JFrame(), true);
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
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JPanel panelParam;
    // End of variables declaration//GEN-END:variables

}

/*
 * CreateDataVisualDialog.java
 *
 * Created on 18 novembre 2008, 13:23
 */

package eu.scy.tools.dataProcessTool.dataTool;

import eu.scy.tools.dataProcessTool.common.DataHeader;
import eu.scy.tools.dataProcessTool.common.TypeVisualization;
import eu.scy.tools.dataProcessTool.utilities.CopexReturn;
import eu.scy.tools.dataProcessTool.utilities.MyUtilities;
import eu.scy.tools.dataProcessTool.utilities.DataConstants;
import java.util.ArrayList;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Dialog which allows to the user to choose between the different types of visual
 * @author  Marjolaine Bodin
 */
public class CreateDataVisualDialog extends javax.swing.JDialog {

    // PROPERTY 
    /* owner */
    private FitexToolPanel owner ;
    /* liste des choix possibles */
    private TypeVisualization[] tabTypes;
    /* liste des colonnes */
    private DataHeader[] listCol;
    /* liste des colonnes pour le deuxieme axe*/
    private DataHeader[] listCol2;

    private JPanel panelData;
    private JLabel labelDataChoice;
    private JLabel labelX;
    private JLabel labelY;
    private JComboBox cbData1;
    private JComboBox cbData2;

    private ArrayList<JPanel> listPanelPlot;

    
    // CONSTRUCTOR
    public CreateDataVisualDialog(FitexToolPanel owner, TypeVisualization[] tabTypes, DataHeader[] listCol) {
        super();
        this.owner = owner;
        this.tabTypes = tabTypes;
        this.listCol = listCol;
        initComponents();
        setLocation(owner.getLocationDialog());
        setModal(true);
        setResizable(false);
        init();
    }
    
    
    
    
    // METHOD 
    private void init(){
        int l = 30;
       int n = this.tabTypes.length;
       for (int i=0; i<n; i++){
           cbType.addItem(this.tabTypes[i].getName());
           int tl = MyUtilities.lenghtOfString(this.tabTypes[i].getName(), getFontMetrics(cbType.getFont()));
           l = Math.max(l, tl);
       }
       
      cbType.setSize(l+25, 20);
      this.add(getPanelData());
      if (n > 0)
        this.cbType.setSelectedIndex(0);
      if (n == 1)
          this.cbType.setEnabled(false);
      selectVisualType();
      setSize(380,200);
      setPreferredSize(getSize());
      repaint();
    }

    private void selectVisualType(){
        if(panelData == null)
            return;
        int id = cbType.getSelectedIndex() ;
        if (id != -1){
            TypeVisualization typeVis = tabTypes[id];
            if (typeVis.getNbColParam() < 2){
                if(cbData2 != null){
                    panelData.remove(this.cbData2);
                    panelData.remove(this.labelX);
                    panelData.remove(labelY);
                    this.labelDataChoice.setText(owner.getBundleString("LABEL_DATA_CHOICE"));
                    this.labelDataChoice.setSize(MyUtilities.lenghtOfString(this.labelDataChoice.getText(), getFontMetrics(this.labelDataChoice.getFont())), this.labelDataChoice.getHeight());
                }
                cbData2 = null;
                labelX = null;
                labelY = null;
                int x = labelDataChoice.getX()+labelDataChoice.getWidth()+5;
                x = Math.max(x, fieldName.getX()-10);
                cbData1.setBounds(x,cbData1.getY(),cbData1.getWidth(),cbData1.getHeight());
            }else{
                if(cbData2 == null)  {
                    panelData.add(getLabelX());
                    panelData.add(getLabelY());
                    panelData.add(getCbData2());
                }
                String s = owner.getBundleString("LABEL_AXIS_CHOICE");
                s  = MyUtilities.replace(s, 0, "0");
                this.labelDataChoice.setText(s);
                this.labelDataChoice.setSize(MyUtilities.lenghtOfString(this.labelDataChoice.getText(), getFontMetrics(this.labelDataChoice.getFont())), this.labelDataChoice.getHeight());
                int x = labelDataChoice.getX()+labelDataChoice.getWidth()+5;
                x = Math.max(x, fieldName.getX()-10);
                labelX.setBounds(x, labelX.getY(), labelX.getWidth(), labelX.getHeight());
                cbData1.setBounds(labelX.getX()+labelX.getWidth()+5,cbData1.getY(),cbData1.getWidth(),cbData1.getHeight());
                labelY.setBounds(cbData1.getX()+cbData1.getWidth()+5, labelY.getY(), labelY.getWidth(), labelY.getHeight());
                cbData2.setBounds(labelY.getX()+labelY.getWidth()+5,cbData2.getY(),cbData2.getWidth(),cbData2.getHeight());
                cbData1.setSelectedIndex(0);
            }
        }
        panelData.revalidate();
        panelData.repaint();
    }

    /* mise a jour des axes */
    private void updateCbData2(){
        if(cbData2 == null)
            return;
        this.listCol2 = new DataHeader[this.listCol.length - 1];
        int id1 = cbData1.getSelectedIndex() ;
        int j=0;
        for (int i=0; i<this.listCol.length; i++){
            if (i != id1){
                this.listCol2[j] = this.listCol[i];
            }
        }
        cbData2.removeAllItems();
        for (int i=0; i<listCol2.length; i++){
            if(listCol2[i] != null){
                cbData2.addItem(listCol2[i].getValue());
            }
        }
        cbData2.setSelectedIndex(0);
        repaint();
    }


    /* creation d'un nouveau type */
    private void createTypeVisual(){
        // recupere le nom
        String name = fieldName.getText() ;
        if (name == null || name.length() == 0){
            String msg = owner.getBundleString("MSG_ERROR_FIELD_NULL") ;
            msg  = MyUtilities.replace(msg, 0, owner.getBundleString("LABEL_NAME"));
            owner.displayError(new CopexReturn(msg, false), owner.getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }
         if (name.length() > DataConstants.MAX_LENGHT_GRAPH_NAME){
            String msg = owner.getBundleString("MSG_LENGHT_MAX");
             msg  = MyUtilities.replace(msg, 0, owner.getBundleString("LABEL_NAME"));
             msg = MyUtilities.replace(msg, 1, ""+DataConstants.MAX_LENGHT_GRAPH_NAME);
            owner.displayError(new CopexReturn(msg, false), owner.getBundleString("TITLE_DIALOG_ERROR"));
            return;
         }
        // recupere le type de vis
        int id = cbType.getSelectedIndex() ;
        TypeVisualization typeVis= tabTypes[id];
        // recupere les axes
        int id1 = cbData1.getSelectedIndex() ;
        DataHeader dataHeader = listCol[id1];
        DataHeader dataHeader2 = null;
        if (typeVis.getNbColParam() > 1){
            int id2 = cbData2.getSelectedIndex() ;
            dataHeader2 = listCol2[id2];
//            if (id1 == id2){
//                owner.displayError(new CopexReturn(owner.getBundleString("MSG_ERROR_AXIS"), false), owner.getBundleString("TITLE_DIALOG_ERROR"));
//                return ;
//            }
        }
        boolean isOk = owner.createVisualization(name, typeVis, dataHeader, dataHeader2);
        if (isOk)
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

        labelType = new javax.swing.JLabel();
        cbType = new javax.swing.JComboBox();
        buttonOk = new javax.swing.JButton();
        buttonCancel = new javax.swing.JButton();
        labelName = new javax.swing.JLabel();
        fieldName = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(this.owner.getBundleString("TITLE_DIALOG_CREATE_VISUAL"));
        setModal(true);
        setResizable(false);
        getContentPane().setLayout(null);

        labelType.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        labelType.setText(this.owner.getBundleString("LABEL_TYPE"));
        getContentPane().add(labelType);
        labelType.setBounds(10, 40, 75, 14);

        cbType.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbTypeItemStateChanged(evt);
            }
        });
        getContentPane().add(cbType);
        cbType.setBounds(90, 40, 120, 20);

        buttonOk.setText(this.owner.getBundleString("BUTTON_OK"));
        buttonOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonOkActionPerformed(evt);
            }
        });
        getContentPane().add(buttonOk);
        buttonOk.setBounds(40, 140, 99, 23);

        buttonCancel.setText(this.owner.getBundleString("BUTTON_CANCEL"));
        buttonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonCancelActionPerformed(evt);
            }
        });
        getContentPane().add(buttonCancel);
        buttonCancel.setBounds(190, 140, 99, 23);

        labelName.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        labelName.setText(this.owner.getBundleString("LABEL_NAME"));
        getContentPane().add(labelName);
        labelName.setBounds(10, 10, 75, 14);
        getContentPane().add(fieldName);
        fieldName.setBounds(90, 10, 230, 20);

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void buttonOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonOkActionPerformed
    createTypeVisual();
}//GEN-LAST:event_buttonOkActionPerformed

private void buttonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonCancelActionPerformed
    this.dispose();
}//GEN-LAST:event_buttonCancelActionPerformed

private void cbTypeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbTypeItemStateChanged
    selectVisualType();
}//GEN-LAST:event_cbTypeItemStateChanged

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonCancel;
    private javax.swing.JButton buttonOk;
    private javax.swing.JComboBox cbType;
    private javax.swing.JTextField fieldName;
    private javax.swing.JLabel labelName;
    private javax.swing.JLabel labelType;
    // End of variables declaration//GEN-END:variables


    private JPanel getPanelData(){
        if(this.panelData == null){
            panelData = new JPanel();
            panelData.setName("panelData");
            panelData.setLayout(null);
            panelData.add(getLabelDataChoice());
            panelData.add(getLabelX());
            panelData.add(getCbData1());
            panelData.add(getLabelY());
            panelData.add(getCbData2());
            int w = cbData2.getX()+cbData2.getWidth()+5;
            panelData.setBounds(10, 70, w,30);
        }
        return panelData;
    }

    private JLabel getLabelDataChoice(){
        if(labelDataChoice == null){
            labelDataChoice = new JLabel();
            labelDataChoice.setName("labelDataChoice");
            String s = owner.getBundleString("LABEL_AXIS_CHOICE");
            s  = MyUtilities.replace(s, 0, "0");
            labelDataChoice.setText(s);
            labelDataChoice.setFont(new java.awt.Font("Tahoma", 1, 11));
            this.labelDataChoice.setSize(MyUtilities.lenghtOfString(this.labelDataChoice.getText(), getFontMetrics(this.labelDataChoice.getFont())), 14);
            labelDataChoice.setBounds(0, 3, labelDataChoice.getWidth(), labelDataChoice.getHeight());
        }
        return labelDataChoice;
    }

    private JLabel getLabelX(){
        if(labelX == null){
            labelX = new JLabel();
            labelX.setName("labelX");
            labelX.setText(owner.getBundleString("LABEL_X"));
            labelX.setFont(new java.awt.Font("Tahoma", 1, 11));
            this.labelX.setSize(MyUtilities.lenghtOfString(this.labelX.getText(), getFontMetrics(this.labelX.getFont())), 14);
            int x = labelDataChoice.getX()+labelDataChoice.getWidth()+5;
            x = Math.max(x, fieldName.getX()-10);
            labelX.setBounds(x, 3, labelX.getWidth(), labelX.getHeight());
        }
        return labelX;
    }
    private JComboBox getCbData1(){
        if(cbData1 == null){
            cbData1 = new JComboBox();
            cbData1.setName("cbData1");
            cbData1.setBounds(labelX.getX()+labelX.getWidth()+5, 0, 80, 20);
            for (int i=0; i<listCol.length; i++){
                if(listCol[i] != null){
                    cbData1.addItem(listCol[i].getValue());
                }
            }
            cbData1.addItemListener(new java.awt.event.ItemListener() {
                public void itemStateChanged(java.awt.event.ItemEvent evt) {
                    updateCbData2();
                }
            });
        }
        return cbData1;
    }


    private JLabel getLabelY(){
        if(labelY == null){
            labelY = new JLabel();
            labelY.setName("labelY");
            labelY.setText(owner.getBundleString("LABEL_Y"));
            labelY.setFont(new java.awt.Font("Tahoma", 1, 11));
            this.labelY.setSize(MyUtilities.lenghtOfString(this.labelY.getText(), getFontMetrics(this.labelY.getFont())), 14);
            labelY.setBounds(cbData1.getX()+cbData1.getWidth()+10, 3, labelY.getWidth(), labelY.getHeight());
        }
        return labelY;
    }
    private JComboBox getCbData2(){
        if(cbData2 == null){
            System.out.println("getCbData2");
            cbData2 = new JComboBox();
            cbData2.setName("cbData2");
            cbData2.setBounds(labelY.getX()+labelY.getWidth()+5, 0, 80, 20);
            updateCbData2();
        }
        return cbData2;
    }
}

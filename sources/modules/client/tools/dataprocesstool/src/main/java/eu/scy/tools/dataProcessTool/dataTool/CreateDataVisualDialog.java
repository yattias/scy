/*
 * CreateDataVisualDialog.java
 *
 * Created on 18 novembre 2008, 13:23
 */

package eu.scy.tools.dataProcessTool.dataTool;

import eu.scy.tools.dataProcessTool.common.DataHeader;
import eu.scy.tools.dataProcessTool.common.PlotXY;
import eu.scy.tools.dataProcessTool.common.TypeVisualization;
import eu.scy.tools.dataProcessTool.utilities.ActionPlot;
import eu.scy.tools.dataProcessTool.utilities.CopexReturn;
import eu.scy.tools.dataProcessTool.utilities.MyUtilities;
import eu.scy.tools.dataProcessTool.utilities.DataConstants;
import java.awt.Font;
import java.util.ArrayList;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Dialog which allows to the user to choose between the different types of visual
 * @author  Marjolaine Bodin
 */
public class CreateDataVisualDialog extends javax.swing.JDialog implements ActionPlot {
/* owner */
    private FitexToolPanel owner ;
    /* liste des choix possibles */
    private TypeVisualization[] tabTypes;
    /* liste des colonnes */
    private DataHeader[] listCol;
    /* liste des colonnes pouvant etre etiquettes */
    private DataHeader[] listColLabelHeader;

    private JPanel panelData;
    private JLabel labelDataChoice = null;
    private JComboBox cbData = null;
    private JLabel labelHeaderLabel = null;
    private JComboBox cbHeaderLabel = null;

    private PlotPanel plotPanel;

    
    // CONSTRUCTOR
    public CreateDataVisualDialog(FitexToolPanel owner, TypeVisualization[] tabTypes, DataHeader[] listCol, DataHeader[] listColLabelHeader) {
        super();
        this.owner = owner;
        this.tabTypes = tabTypes;
        this.listCol = listCol;
        this.listColLabelHeader = listColLabelHeader;
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
      setSize(400,200);
      setPreferredSize(getSize());
      resizePanel();
    }

    private void selectVisualType(){
        if(panelData == null)
            return;
        int id = cbType.getSelectedIndex() ;
        if (id != -1){
            TypeVisualization typeVis = tabTypes[id];
            if (typeVis.getNbColParam() < 2){
                if(plotPanel != null){
                    panelData.remove(plotPanel);
                }
                if(labelDataChoice == null){
                    panelData.add(getLabelDataChoice());
                    panelData.add(getCbData());
                }
                if(labelHeaderLabel == null && isHeaderLabel()){
                    panelData.add(getLabelHeaderLabel());
                    panelData.add(getCbHeaderLabel());
                }
                plotPanel = null;
                int x = labelDataChoice.getX()+labelDataChoice.getWidth()+5;
                x = Math.max(x, fieldName.getX()-10);
                cbData.setBounds(x,cbData.getY(),cbData.getWidth(),cbData.getHeight());
                panelData.setSize(cbData.getX()+cbData.getWidth()+20, 30);
                if(isHeaderLabel()){
                    labelHeaderLabel.setBounds(cbData.getX()+cbData.getWidth()+10, labelHeaderLabel.getY(), labelHeaderLabel.getWidth(), labelHeaderLabel.getHeight());
                    cbHeaderLabel.setBounds(labelHeaderLabel.getX()+labelHeaderLabel.getWidth()+5, cbHeaderLabel.getY(), cbHeaderLabel.getWidth(), cbHeaderLabel.getHeight());
                    panelData.setSize(cbHeaderLabel.getX()+cbHeaderLabel.getWidth()+20,30);
                 }
            }else{
                if(labelDataChoice != null){
                    panelData.remove(labelDataChoice);
                    panelData.remove(cbData);
                    if(labelHeaderLabel != null){
                        panelData.remove(labelHeaderLabel);
                        panelData.remove(cbHeaderLabel);
                    }
                }
                labelDataChoice = null;
                cbData = null;
                labelHeaderLabel = null;
                cbHeaderLabel = null;
                if(plotPanel == null)  {
                    panelData.add(getPlotPanel());
                }
                plotPanel.setBounds(0, plotPanel.getY(), plotPanel.getWidth(), plotPanel.getHeight());
                panelData.setSize(plotPanel.getWidth()+plotPanel.getX()+20, plotPanel.getHeight());
            }
        }
        panelData.revalidate();
        resizePanel();
    }

    private void resizePanel(){
        buttonOk.setBounds(buttonOk.getX(), panelData.getY()+panelData.getHeight()+20, buttonOk.getWidth(), buttonOk.getHeight());
        buttonCancel.setBounds(buttonCancel.getX(), buttonOk.getY(), buttonCancel.getWidth(), buttonCancel.getHeight());
        setSize(getWidth(), buttonOk.getY()+buttonOk.getHeight()+40);
    }

    private PlotPanel getPlotPanel(){
        if(plotPanel == null){
            plotPanel = new PlotPanel(owner, listCol);
            plotPanel.setBounds(0, 0, plotPanel.getWidth(), plotPanel.getHeight());
            plotPanel.addActionPlot(this);
        }
        return plotPanel;
    }
    

    private boolean isHeaderLabel(){
        return listColLabelHeader.length  > 0;
    }
    private JLabel getLabelHeaderLabel(){
        if(labelHeaderLabel == null){
            labelHeaderLabel = new JLabel();
            labelHeaderLabel.setName("labelHeaderLabel");
            labelHeaderLabel.setText(owner.getBundleString("LABEL_HEADER_LABEL"));
            labelHeaderLabel.setFont(new Font("Tahoma",Font.BOLD, 11));
            int w = MyUtilities.lenghtOfString(this.labelHeaderLabel.getText(), labelHeaderLabel.getFontMetrics(this.labelHeaderLabel.getFont()));
            labelHeaderLabel.setSize(w, 14);
            labelHeaderLabel.setBounds(cbData.getX()+cbData.getWidth()+10,labelDataChoice.getY(), labelHeaderLabel.getWidth(), labelHeaderLabel.getHeight());
        }
        return labelHeaderLabel;
    }

    private JComboBox getCbHeaderLabel(){
        if(cbHeaderLabel == null){
            cbHeaderLabel = new JComboBox();
            cbHeaderLabel.setName("cbHeaderLabel");
            cbHeaderLabel.setBounds(labelHeaderLabel.getX()+labelHeaderLabel.getWidth()+5, 0, 120, 20);
            cbHeaderLabel.addItem(owner.getBundleString("DEFAULT_HEADER_LABEL"));
            for (int i=0; i<listColLabelHeader.length; i++){
                if(listColLabelHeader[i] != null){
                    cbHeaderLabel.addItem(listColLabelHeader[i].getValue());
                }
            }
        }
        return cbHeaderLabel;
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
         if (controlLenght() && name.length() > DataConstants.MAX_LENGHT_VISUALIZATION_NAME){
            String msg = owner.getBundleString("MSG_LENGHT_MAX");
             msg  = MyUtilities.replace(msg, 0, owner.getBundleString("LABEL_NAME"));
             msg = MyUtilities.replace(msg, 1, ""+DataConstants.MAX_LENGHT_VISUALIZATION_NAME);
            owner.displayError(new CopexReturn(msg, false), owner.getBundleString("TITLE_DIALOG_ERROR"));
            return;
         }
        // recupere le type de vis
        int id = cbType.getSelectedIndex() ;
        TypeVisualization typeVis= tabTypes[id];
        // recupere les axes
        DataHeader dataHeader = null;
        if(cbData != null){
            int id1 = cbData.getSelectedIndex() ;
            dataHeader = listCol[id1];
        }
        // etiquette
        DataHeader headerLabel = null;
        if(cbHeaderLabel != null){
            int idH = cbHeaderLabel.getSelectedIndex();
            if(idH > 0){
                headerLabel = listColLabelHeader[idH-1];
            }
        }
        ArrayList<PlotXY> listPlot = new ArrayList();
        if(plotPanel != null)
            listPlot = plotPanel.getListPlotXY();
        boolean isOk = owner.createVisualization(name, typeVis, dataHeader, headerLabel, listPlot);
        if (isOk)
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

        labelType.setFont(new java.awt.Font("Tahoma", 1, 11));
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

        labelName.setFont(new java.awt.Font("Tahoma", 1, 11));
        labelName.setText(this.owner.getBundleString("LABEL_NAME"));
        getContentPane().add(labelName);
        labelName.setBounds(10, 10, 75, 14);

        fieldName.setPreferredSize(new java.awt.Dimension(6, 26));
        getContentPane().add(fieldName);
        fieldName.setBounds(90, 10, 230, 26);

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
            panelData.add(getCbData());
            int w = cbData.getX()+cbData.getWidth()+20;
            panelData.setBounds(10, 70, w,30);
        }
        return panelData;
    }

    private JLabel getLabelDataChoice(){
        if(labelDataChoice == null){
            labelDataChoice = new JLabel();
            labelDataChoice.setName("labelDataChoice");
            String s = owner.getBundleString("LABEL_DATA_CHOICE");
            //s  = MyUtilities.replace(s, 0, "0");
            labelDataChoice.setText(s);
            labelDataChoice.setFont(new java.awt.Font("Tahoma", 1, 11));
            this.labelDataChoice.setSize(MyUtilities.lenghtOfString(this.labelDataChoice.getText(), getFontMetrics(this.labelDataChoice.getFont())), 14);
            labelDataChoice.setBounds(0, 3, labelDataChoice.getWidth(), labelDataChoice.getHeight());
        }
        return labelDataChoice;
    }

    
    private JComboBox getCbData(){
        if(cbData == null){
            cbData = new JComboBox();
            cbData.setName("cbData");
            cbData.setBounds(labelDataChoice.getX()+labelDataChoice.getWidth()+5, 0, 100, 20);
            for (int i=0; i<listCol.length; i++){
                if(listCol[i] != null){
                    cbData.addItem(listCol[i].getValue());
                }
            }
        }
        return cbData;
    }

    @Override
    public void updatePlotPanel() {
        plotPanel.setBounds(0, plotPanel.getY(), plotPanel.getWidth(), plotPanel.getHeight());
        panelData.setSize(plotPanel.getWidth()+plotPanel.getX()+20, plotPanel.getHeight());
        resizePanel();
    }


    
}

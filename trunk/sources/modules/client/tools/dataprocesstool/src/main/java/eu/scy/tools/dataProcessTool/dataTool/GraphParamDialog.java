/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * GraphParamDialog.java
 *
 * Created on 8 juin 2009, 12:02:50
 */

package eu.scy.tools.dataProcessTool.dataTool;

import eu.scy.tools.dataProcessTool.common.DataHeader;
import eu.scy.tools.dataProcessTool.common.Graph;
import eu.scy.tools.dataProcessTool.common.ParamGraph;
import eu.scy.tools.dataProcessTool.utilities.CopexReturn;
import eu.scy.tools.dataProcessTool.utilities.DataConstants;
import eu.scy.tools.dataProcessTool.utilities.MyUtilities;
import java.text.DecimalFormat;

/**
 *
 * @author Marjolaine
 */
public class GraphParamDialog extends javax.swing.JDialog {

    /* owner */
    private FitexToolPanel dataToolPanel;
    private Graph graph;
    private DecimalFormat decimalFormat;
    private DataHeader[] listCol;
    /* liste des colonnes pour le deuxieme axe*/
    private DataHeader[] listCol2;

    public GraphParamDialog(FitexToolPanel dataToolPanel, Graph Graph, DataHeader[] listCol) {
        super();
        this.dataToolPanel = dataToolPanel;
        this.graph = Graph;
        this.decimalFormat = new DecimalFormat();
        this.decimalFormat.setDecimalSeparatorAlwaysShown(false);
        this.listCol = listCol;
        initComponents();
        initGUI();
        setLocationRelativeTo(dataToolPanel);
        setModal(true);
        setResizable(false);
    }



    /** Creates new form GraphParamDialog */
    public GraphParamDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }


    private void initGUI(){
        String s = dataToolPanel.getBundleString("LABEL_AXIS_CHOICE");
        s  = MyUtilities.replace(s, 0, "0");
        labelPlot1.setText(s);
        resizeElements();
        for (int i=0; i<listCol.length; i++){
            if(listCol[i] != null){
                cbXAxis.addItem(listCol[i].getValue());
            }
        }
        cbXAxis.setSelectedIndex(0);
        if (graph != null && graph.getParamGraph() != null){
            ParamGraph paramGraph = graph.getParamGraph();
            cbXAxis.setSelectedItem(paramGraph.getHeaderX().getValue());
            textFieldXMin.setText(""+decimalFormat.format(paramGraph.getX_min()));
            textFieldXMax.setText(""+decimalFormat.format(paramGraph.getX_max()));
            textFieldDeltaX.setText(""+decimalFormat.format(paramGraph.getDeltaX()));
            //cbYAxis.setSelectedItem(paramGraph.getY_name());
            textFieldYMin.setText(""+decimalFormat.format(paramGraph.getY_min()));
            textFieldYMax.setText(""+decimalFormat.format(paramGraph.getY_max()));
            textFieldDeltaY.setText(""+decimalFormat.format(paramGraph.getDeltaY()));
        }
    }

    private void resizeElements(){
        this.labelPlot1.setSize(MyUtilities.lenghtOfString(this.labelPlot1.getText(), getFontMetrics(this.labelPlot1.getFont())), this.labelPlot1.getHeight());
       // x
       this.labelXNameAxis.setSize(MyUtilities.lenghtOfString(this.labelXNameAxis.getText(), getFontMetrics(this.labelXNameAxis.getFont())), this.labelXNameAxis.getHeight());
       this.labelXMin.setSize(MyUtilities.lenghtOfString(this.labelXMin.getText(), getFontMetrics(this.labelXMin.getFont())), this.labelXMin.getHeight());
       this.labelXMax.setSize(MyUtilities.lenghtOfString(this.labelXMax.getText(), getFontMetrics(this.labelXMax.getFont())), this.labelXMax.getHeight());
       this.labelDeltaX.setSize(MyUtilities.lenghtOfString(this.labelDeltaX.getText(), getFontMetrics(this.labelDeltaX.getFont())), this.labelDeltaX.getHeight());
       //y
       this.labelYNameAxis.setSize(MyUtilities.lenghtOfString(this.labelYNameAxis.getText(), getFontMetrics(this.labelYNameAxis.getFont())), this.labelYNameAxis.getHeight());
       this.labelYMin.setSize(MyUtilities.lenghtOfString(this.labelYMin.getText(), getFontMetrics(this.labelYMin.getFont())), this.labelYMin.getHeight());
       this.labelYMax.setSize(MyUtilities.lenghtOfString(this.labelYMax.getText(), getFontMetrics(this.labelYMax.getFont())), this.labelYMax.getHeight());
       this.labelDeltaY.setSize(MyUtilities.lenghtOfString(this.labelDeltaY.getText(), getFontMetrics(this.labelDeltaY.getFont())), this.labelDeltaY.getHeight());
       // bouton Ok
       this.buttonOk.setSize(60+MyUtilities.lenghtOfString(this.buttonOk.getText(), getFontMetrics(this.buttonOk.getFont())), this.buttonOk.getHeight());
       // bouton Annuler
       this.buttonCancel.setSize(60+MyUtilities.lenghtOfString(this.buttonCancel.getText(), getFontMetrics(this.buttonCancel.getFont())), this.buttonCancel.getHeight());
   }

    private void validParamGraph(){
        // x
        DataHeader headerX = listCol[cbXAxis.getSelectedIndex()];
        String s = textFieldXMin.getText();
        s = s.replace(",", ".");
        double xMin;
        try{
            xMin = Double.parseDouble(s);
        }catch(NumberFormatException e){
            dataToolPanel.displayError(new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_PARAM_AXIS"), false), dataToolPanel.getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }
        s = textFieldXMax.getText();
        s = s.replace(",", ".");
        double xMax;
        try{
            xMax = Double.parseDouble(s);
        }catch(NumberFormatException e){
            dataToolPanel.displayError(new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_PARAM_AXIS"), false), dataToolPanel.getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }
        s = textFieldDeltaX.getText();
        s = s.replace(",", ".");
        double deltaX;
        try{
            deltaX = Double.parseDouble(s);
        }catch(NumberFormatException e){
            dataToolPanel.displayError(new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_PARAM_AXIS"), false), dataToolPanel.getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }
        //y
        DataHeader headerY = listCol2[cbYAxis.getSelectedIndex()];
        s = textFieldYMin.getText();
        s = s.replace(",", ".");
        double yMin;
        try{
            yMin = Double.parseDouble(s);
        }catch(NumberFormatException e){
            dataToolPanel.displayError(new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_PARAM_AXIS"), false), dataToolPanel.getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }
        s = textFieldYMax.getText();
        s = s.replace(",", ".");
        double yMax;
        try{
            yMax = Double.parseDouble(s);
        }catch(NumberFormatException e){
            dataToolPanel.displayError(new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_PARAM_AXIS"), false), dataToolPanel.getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }
        s = textFieldDeltaY.getText();
        s = s.replace(",", ".");
        double deltaY;
        try{
            deltaY = Double.parseDouble(s);
        }catch(NumberFormatException e){
            dataToolPanel.displayError(new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_PARAM_AXIS"), false), dataToolPanel.getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }
        //controle la coherence des axes
        if (xMin>=xMax || yMin>=yMax) {
             dataToolPanel.displayError(new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_PARAM_AXIS"), false), dataToolPanel.getBundleString("TITLE_DIALOG_ERROR"));
             return;
        }
        //
        ParamGraph paramGraph = graph.getParamGraph();
        ParamGraph newParamGraph = new ParamGraph(headerX, headerY, xMin, xMax, yMin, yMax, deltaX, deltaY, paramGraph == null ? false : paramGraph.isAutoscale());
        boolean isOk = dataToolPanel.updateGraphParam(graph, newParamGraph);
        if(isOk){
            this.dispose();
        }
    }

    private void quitParamGraph(){
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

        labelXMin = new javax.swing.JLabel();
        labelXMax = new javax.swing.JLabel();
        labelDeltaX = new javax.swing.JLabel();
        labelYMin = new javax.swing.JLabel();
        labelYMax = new javax.swing.JLabel();
        labelDeltaY = new javax.swing.JLabel();
        textFieldXMin = new javax.swing.JTextField();
        textFieldXMax = new javax.swing.JTextField();
        textFieldDeltaX = new javax.swing.JTextField();
        textFieldYMin = new javax.swing.JTextField();
        textFieldYMax = new javax.swing.JTextField();
        textFieldDeltaY = new javax.swing.JTextField();
        buttonOk = new javax.swing.JButton();
        buttonCancel = new javax.swing.JButton();
        labelXNameAxis = new javax.swing.JLabel();
        labelYNameAxis = new javax.swing.JLabel();
        cbXAxis = new javax.swing.JComboBox();
        cbYAxis = new javax.swing.JComboBox();
        labelPlot1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(dataToolPanel.getBundleString("TITLE_DIALOG_GRAPH_PARAM"));
        setMinimumSize(new java.awt.Dimension(480, 200));
        setModal(true);
        setResizable(false);
        getContentPane().setLayout(null);

        labelXMin.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        labelXMin.setText(dataToolPanel.getBundleString("LABEL_XMIN"));
        getContentPane().add(labelXMin);
        labelXMin.setBounds(10, 10, 50, 14);

        labelXMax.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        labelXMax.setText(dataToolPanel.getBundleString("LABEL_XMAX"));
        getContentPane().add(labelXMax);
        labelXMax.setBounds(160, 10, 50, 14);

        labelDeltaX.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        labelDeltaX.setText(dataToolPanel.getBundleString("LABEL_DELTAX"));
        getContentPane().add(labelDeltaX);
        labelDeltaX.setBounds(320, 10, 50, 14);

        labelYMin.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        labelYMin.setText(dataToolPanel.getBundleString("LABEL_YMIN"));
        getContentPane().add(labelYMin);
        labelYMin.setBounds(10, 50, 50, 14);

        labelYMax.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        labelYMax.setText(dataToolPanel.getBundleString("LABEL_YMAX"));
        getContentPane().add(labelYMax);
        labelYMax.setBounds(160, 50, 50, 14);

        labelDeltaY.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        labelDeltaY.setText(dataToolPanel.getBundleString("LABEL_DELTAY"));
        getContentPane().add(labelDeltaY);
        labelDeltaY.setBounds(320, 50, 50, 14);
        getContentPane().add(textFieldXMin);
        textFieldXMin.setBounds(70, 7, 80, 20);
        getContentPane().add(textFieldXMax);
        textFieldXMax.setBounds(220, 7, 80, 20);
        getContentPane().add(textFieldDeltaX);
        textFieldDeltaX.setBounds(380, 7, 80, 20);
        getContentPane().add(textFieldYMin);
        textFieldYMin.setBounds(70, 47, 80, 20);
        getContentPane().add(textFieldYMax);
        textFieldYMax.setBounds(220, 47, 80, 20);
        getContentPane().add(textFieldDeltaY);
        textFieldDeltaY.setBounds(380, 47, 80, 20);

        buttonOk.setText(dataToolPanel.getBundleString("BUTTON_OK"));
        buttonOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonOkActionPerformed(evt);
            }
        });
        getContentPane().add(buttonOk);
        buttonOk.setBounds(80, 140, 99, 23);

        buttonCancel.setText(dataToolPanel.getBundleString("BUTTON_CANCEL"));
        buttonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonCancelActionPerformed(evt);
            }
        });
        getContentPane().add(buttonCancel);
        buttonCancel.setBounds(270, 140, 99, 23);

        labelXNameAxis.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        labelXNameAxis.setText(dataToolPanel.getBundleString("LABEL_X"));
        getContentPane().add(labelXNameAxis);
        labelXNameAxis.setBounds(90, 90, 30, 14);

        labelYNameAxis.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        labelYNameAxis.setText(dataToolPanel.getBundleString("LABEL_Y"));
        getContentPane().add(labelYNameAxis);
        labelYNameAxis.setBounds(260, 90, 30, 14);

        cbXAxis.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbXAxisItemStateChanged(evt);
            }
        });
        getContentPane().add(cbXAxis);
        cbXAxis.setBounds(120, 87, 90, 20);

        getContentPane().add(cbYAxis);
        cbYAxis.setBounds(290, 87, 90, 20);

        labelPlot1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        labelPlot1.setText("jLabel1");
        getContentPane().add(labelPlot1);
        labelPlot1.setBounds(20, 90, 60, 14);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void buttonOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonOkActionPerformed
        validParamGraph();
    }//GEN-LAST:event_buttonOkActionPerformed

    private void buttonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonCancelActionPerformed
        quitParamGraph();
    }//GEN-LAST:event_buttonCancelActionPerformed

    private void cbXAxisItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbXAxisItemStateChanged
        cbXAxisChange();
    }//GEN-LAST:event_cbXAxisItemStateChanged

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                GraphParamDialog dialog = new GraphParamDialog(new javax.swing.JFrame(), true);
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
    private javax.swing.JComboBox cbXAxis;
    private javax.swing.JComboBox cbYAxis;
    private javax.swing.JLabel labelDeltaX;
    private javax.swing.JLabel labelDeltaY;
    private javax.swing.JLabel labelPlot1;
    private javax.swing.JLabel labelXMax;
    private javax.swing.JLabel labelXMin;
    private javax.swing.JLabel labelXNameAxis;
    private javax.swing.JLabel labelYMax;
    private javax.swing.JLabel labelYMin;
    private javax.swing.JLabel labelYNameAxis;
    private javax.swing.JTextField textFieldDeltaX;
    private javax.swing.JTextField textFieldDeltaY;
    private javax.swing.JTextField textFieldXMax;
    private javax.swing.JTextField textFieldXMin;
    private javax.swing.JTextField textFieldYMax;
    private javax.swing.JTextField textFieldYMin;
    // End of variables declaration//GEN-END:variables


    private void cbXAxisChange(){
        if(cbYAxis == null)
            return;
        this.listCol2 = new DataHeader[this.listCol.length - 1];
        int id1 = cbXAxis.getSelectedIndex() ;
        int j=0;
        for (int i=0; i<this.listCol.length; i++){
            if (i != id1){
                this.listCol2[j] = this.listCol[i];
            }
        }
        cbYAxis.removeAllItems();
        for (int i=0; i<listCol2.length; i++){
            if(listCol2[i] != null){
                cbYAxis.addItem(listCol2[i].getValue());
            }
        }
        cbYAxis.setSelectedIndex(0);
        if(this.graph != null && this.graph.getParamGraph() != null){
            cbYAxis.setSelectedItem(graph.getParamGraph().getHeaderY().getValue());
        }
        repaint();
    }
}

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
import eu.scy.tools.dataProcessTool.common.PlotXY;
import eu.scy.tools.dataProcessTool.common.Visualization;
import eu.scy.tools.dataProcessTool.controller.FitexNumber;
import eu.scy.tools.dataProcessTool.utilities.ActionPlot;
import eu.scy.tools.dataProcessTool.utilities.CopexReturn;
import eu.scy.tools.dataProcessTool.utilities.DataConstants;
import eu.scy.tools.dataProcessTool.utilities.MyUtilities;
import java.awt.Dimension;
import java.text.NumberFormat;
import java.util.ArrayList;

/**
 *
 * @author Marjolaine
 */
public class GraphParamDialog extends javax.swing.JDialog implements ActionPlot {

    /* owner */
    private FitexToolPanel dataToolPanel;
    private Visualization vis;
    private NumberFormat numberFormat;
    private DataHeader[] listCol;
    private PlotPanel plotPanel;

    public GraphParamDialog(FitexToolPanel dataToolPanel, Visualization vis){
        super();
        this.dataToolPanel = dataToolPanel;
        this.vis = vis;
        this.numberFormat = dataToolPanel.getNumberFormat();
        initComponents();
        initGUI();
        setLocationRelativeTo(dataToolPanel);
        setModal(true);
        setResizable(false);
    }
    public GraphParamDialog(FitexToolPanel dataToolPanel, Visualization vis,  DataHeader[] listCol) {
        super();
        this.dataToolPanel = dataToolPanel;
        this.vis = vis;
        this.numberFormat = dataToolPanel.getNumberFormat();
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

    private boolean isGraph(){
        return vis instanceof Graph;
    }

    private void initGUI(){
        int w = MyUtilities.lenghtOfString(this.cbFixedAutoscale.getText(), cbFixedAutoscale.getFontMetrics(this.cbFixedAutoscale.getFont()));
        cbFixedAutoscale.setSize(w+40, 23);
        // nom de la visualization
        fieldName.setText(this.vis.getName());
        if(isGraph()){
            this.add(getPlotPanel());
            if (vis != null && ((Graph)vis).getParamGraph() != null){
                ParamGraph paramGraph = ((Graph)vis).getParamGraph();
                plotPanel.setSelectedPlots(paramGraph.getPlots());
                textFieldXMin.setText(""+numberFormat.format(paramGraph.getX_min()));
                textFieldXMax.setText(""+numberFormat.format(paramGraph.getX_max()));
                textFieldDeltaX.setText(""+numberFormat.format(paramGraph.getDeltaX()));
                //cbYAxis.setSelectedItem(paramGraph.getY_name());
                textFieldYMin.setText(""+numberFormat.format(paramGraph.getY_min()));
                textFieldYMax.setText(""+numberFormat.format(paramGraph.getY_max()));
                textFieldDeltaY.setText(""+numberFormat.format(paramGraph.getDeltaY()));
//                textFieldXMin.setText(""+paramGraph.getX_min());
//                textFieldXMax.setText(""+paramGraph.getX_max());
//                textFieldDeltaX.setText(""+paramGraph.getDeltaX());
//                textFieldYMin.setText(""+paramGraph.getY_min());
//                textFieldYMax.setText(""+paramGraph.getY_max());
//                textFieldDeltaY.setText(""+paramGraph.getDeltaY());
                cbFixedAutoscale.setSelected(paramGraph.isDeltaFixedAutoscale());
            }
        }else{
            remove(labelXMin);
            remove(textFieldXMin);
            remove(labelXMax);
            remove(textFieldXMax);
            remove(labelDeltaX);
            remove(textFieldDeltaX);
            remove(labelYMin);
            remove(textFieldYMin);
            remove(labelYMax);
            remove(textFieldYMax);
            remove(labelDeltaY);
            remove(textFieldDeltaY);
            remove(cbFixedAutoscale);
//            remove(plotPanel);
        }
        resizeElements();
    }

    private PlotPanel getPlotPanel(){
        if(plotPanel == null){
            plotPanel = new PlotPanel(dataToolPanel, listCol);
            plotPanel.setBounds(10, textFieldYMin.getY()+textFieldYMin.getHeight()+30, plotPanel.getWidth(), plotPanel.getHeight());
            plotPanel.addActionPlot(this);
        }
        return plotPanel;
    }

    private void resizePanel(){
        this.buttonOk.setBounds(this.buttonOk.getX(), this.plotPanel.getY()+this.plotPanel.getHeight()+10, this.buttonOk.getWidth(), this.buttonOk.getHeight());
        this.buttonCancel.setBounds(this.buttonCancel.getX(), this.buttonOk.getY(), this.buttonCancel.getWidth(), this.buttonCancel.getHeight());
        setSize(getWidth(), buttonOk.getY()+buttonOk.getHeight()+40);
    }
    private void resizeElements(){
        this.labelName.setSize(MyUtilities.lenghtOfString(this.labelName.getText(), getFontMetrics(this.labelName.getFont())), this.labelName.getHeight());
        this.fieldName.setBounds(labelName.getX()+labelName.getWidth()+10, fieldName.getY(), fieldName.getWidth(), fieldName.getHeight());
        if(isGraph()){
            // x
            this.labelXMin.setSize(MyUtilities.lenghtOfString(this.labelXMin.getText(), getFontMetrics(this.labelXMin.getFont())), this.labelXMin.getHeight());
            this.labelXMax.setSize(MyUtilities.lenghtOfString(this.labelXMax.getText(), getFontMetrics(this.labelXMax.getFont())), this.labelXMax.getHeight());
            this.labelDeltaX.setSize(MyUtilities.lenghtOfString(this.labelDeltaX.getText(), getFontMetrics(this.labelDeltaX.getFont())), this.labelDeltaX.getHeight());
            //y
            this.labelYMin.setSize(MyUtilities.lenghtOfString(this.labelYMin.getText(), getFontMetrics(this.labelYMin.getFont())), this.labelYMin.getHeight());
            this.labelYMax.setSize(MyUtilities.lenghtOfString(this.labelYMax.getText(), getFontMetrics(this.labelYMax.getFont())), this.labelYMax.getHeight());
            this.labelDeltaY.setSize(MyUtilities.lenghtOfString(this.labelDeltaY.getText(), getFontMetrics(this.labelDeltaY.getFont())), this.labelDeltaY.getHeight());
            int wMax = cbFixedAutoscale.getWidth()+cbFixedAutoscale.getX();
            if(getWidth() < wMax)
                setSize(wMax, getHeight());
        }
       // bouton Ok
       this.buttonOk.setSize(60+MyUtilities.lenghtOfString(this.buttonOk.getText(), getFontMetrics(this.buttonOk.getFont())), this.buttonOk.getHeight());
       // bouton Annuler
       this.buttonCancel.setSize(60+MyUtilities.lenghtOfString(this.buttonCancel.getText(), getFontMetrics(this.buttonCancel.getFont())), this.buttonCancel.getHeight());
       if(!isGraph()){
           int width = fieldName.getX()+fieldName.getWidth()+20;
           this.buttonOk.setBounds(width/5, fieldName.getY()+fieldName.getHeight()+20, buttonOk.getWidth(), buttonOk.getHeight());
           this.buttonCancel.setBounds(width-(width/5)-buttonCancel.getWidth(), buttonOk.getY(), buttonCancel.getWidth(), buttonCancel.getHeight());
           int height = buttonOk.getY()+buttonOk.getHeight()+40;
           Dimension dim = new Dimension(width, height);
           this.setMinimumSize(dim);
           this.setSize(dim);
       }
   }

    private boolean controlLenght(){
        return dataToolPanel.controlLenght();
    }

    private void validParamGraph(){
        // recupere les donnees :
        String name = this.fieldName.getText();
        if (controlLenght() && name.length() > DataConstants.MAX_LENGHT_VISUALIZATION_NAME){
            String msg = dataToolPanel.getBundleString("MSG_LENGHT_MAX");
            msg  = MyUtilities.replace(msg, 0, dataToolPanel.getBundleString("LABEL_NAME_VISUALIZATION"));
            msg = MyUtilities.replace(msg, 1, ""+DataConstants.MAX_LENGHT_DATASET_NAME);
            dataToolPanel.displayError(new CopexReturn(msg, false), dataToolPanel.getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }
        if (name.length() == 0){
            String msg = dataToolPanel.getBundleString("MSG_ERROR_FIELD_NULL");
            msg  = MyUtilities.replace(msg, 0, dataToolPanel.getBundleString("LABEL_NAME_VISUALIZATION"));
            dataToolPanel.displayError(new CopexReturn(msg ,false), dataToolPanel.getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }
        if (!isGraph()){
            if(!name.equals(vis.getName())){
                boolean isOk = dataToolPanel.updateVisualizationName(vis, name);
                if(isOk)
                    this.dispose();
                else return;
            }else{
                this.dispose();
            }
            return;
        }
        // x
        double xMin = FitexNumber.getDoubleValue(textFieldXMin.getText());
        if(Double.isNaN(xMin)){
            dataToolPanel.displayError(new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_PARAM_AXIS"), false), dataToolPanel.getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }
        double xMax = FitexNumber.getDoubleValue(textFieldXMax.getText());
        if(Double.isNaN(xMax)){
            dataToolPanel.displayError(new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_PARAM_AXIS"), false), dataToolPanel.getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }
        double deltaX = FitexNumber.getDoubleValue(textFieldDeltaX.getText());
        if(Double.isNaN(deltaX)){
            dataToolPanel.displayError(new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_PARAM_AXIS"), false), dataToolPanel.getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }
        if(deltaX == 0){
            dataToolPanel.displayError(new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_PARAM_AXIS"), false), dataToolPanel.getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }
        //y
        double yMin = FitexNumber.getDoubleValue(textFieldYMin.getText());
        if(Double.isNaN(yMin)){
            dataToolPanel.displayError(new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_PARAM_AXIS"), false), dataToolPanel.getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }
        double yMax = FitexNumber.getDoubleValue(textFieldYMax.getText());
        if(Double.isNaN(yMax)){
            dataToolPanel.displayError(new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_PARAM_AXIS"), false), dataToolPanel.getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }
        double deltaY = FitexNumber.getDoubleValue(textFieldDeltaY.getText());
        if(Double.isNaN(deltaY)){
            dataToolPanel.displayError(new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_PARAM_AXIS"), false), dataToolPanel.getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }
        if(deltaY == 0){
            dataToolPanel.displayError(new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_PARAM_AXIS"), false), dataToolPanel.getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }
        // delta fixed?
        boolean deltaFixedAutoscale = cbFixedAutoscale.isSelected();
        //controle la coherence des axes
        if (xMin>=xMax || yMin>=yMax) {
             dataToolPanel.displayError(new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_PARAM_AXIS"), false), dataToolPanel.getBundleString("TITLE_DIALOG_ERROR"));
             return;
        }
        ArrayList<PlotXY> plots = plotPanel.getListPlotXY();
        ParamGraph newParamGraph = new ParamGraph(plots, xMin, xMax, yMin, yMax, deltaX, deltaY, deltaFixedAutoscale);
        boolean isOk = dataToolPanel.updateGraphParam(((Graph)vis), name, newParamGraph);
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
        labelName = new javax.swing.JLabel();
        fieldName = new javax.swing.JTextField();
        cbFixedAutoscale = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(dataToolPanel.getBundleString("TITLE_DIALOG_GRAPH_PARAM"));
        setMinimumSize(new java.awt.Dimension(480, 250));
        setModal(true);
        setResizable(false);
        getContentPane().setLayout(null);

        labelXMin.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        labelXMin.setText(dataToolPanel.getBundleString("LABEL_XMIN"));
        getContentPane().add(labelXMin);
        labelXMin.setBounds(10, 70, 50, 14);

        labelXMax.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        labelXMax.setText(dataToolPanel.getBundleString("LABEL_XMAX"));
        getContentPane().add(labelXMax);
        labelXMax.setBounds(160, 70, 50, 14);

        labelDeltaX.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        labelDeltaX.setText(dataToolPanel.getBundleString("LABEL_DELTAX"));
        getContentPane().add(labelDeltaX);
        labelDeltaX.setBounds(320, 70, 50, 14);

        labelYMin.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        labelYMin.setText(dataToolPanel.getBundleString("LABEL_YMIN"));
        getContentPane().add(labelYMin);
        labelYMin.setBounds(10, 100, 50, 14);

        labelYMax.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        labelYMax.setText(dataToolPanel.getBundleString("LABEL_YMAX"));
        getContentPane().add(labelYMax);
        labelYMax.setBounds(160, 100, 50, 14);

        labelDeltaY.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        labelDeltaY.setText(dataToolPanel.getBundleString("LABEL_DELTAY"));
        getContentPane().add(labelDeltaY);
        labelDeltaY.setBounds(320, 100, 50, 14);

        textFieldXMin.setPreferredSize(new java.awt.Dimension(6, 27));
        getContentPane().add(textFieldXMin);
        textFieldXMin.setBounds(70, 60, 80, 27);

        textFieldXMax.setPreferredSize(new java.awt.Dimension(6, 27));
        getContentPane().add(textFieldXMax);
        textFieldXMax.setBounds(230, 60, 80, 27);

        textFieldDeltaX.setPreferredSize(new java.awt.Dimension(6, 27));
        getContentPane().add(textFieldDeltaX);
        textFieldDeltaX.setBounds(380, 60, 80, 27);

        textFieldYMin.setPreferredSize(new java.awt.Dimension(6, 27));
        getContentPane().add(textFieldYMin);
        textFieldYMin.setBounds(70, 90, 80, 27);

        textFieldYMax.setPreferredSize(new java.awt.Dimension(6, 27));
        getContentPane().add(textFieldYMax);
        textFieldYMax.setBounds(230, 90, 80, 27);

        textFieldDeltaY.setPreferredSize(new java.awt.Dimension(6, 27));
        getContentPane().add(textFieldDeltaY);
        textFieldDeltaY.setBounds(380, 90, 80, 27);

        buttonOk.setText(dataToolPanel.getBundleString("BUTTON_OK"));
        buttonOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonOkActionPerformed(evt);
            }
        });
        getContentPane().add(buttonOk);
        buttonOk.setBounds(80, 190, 99, 23);

        buttonCancel.setText(dataToolPanel.getBundleString("BUTTON_CANCEL"));
        buttonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonCancelActionPerformed(evt);
            }
        });
        getContentPane().add(buttonCancel);
        buttonCancel.setBounds(270, 190, 99, 23);

        labelName.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        labelName.setText(dataToolPanel.getBundleString("LABEL_NAME"));
        getContentPane().add(labelName);
        labelName.setBounds(10, 20, 75, 14);

        fieldName.setPreferredSize(new java.awt.Dimension(6, 27));
        getContentPane().add(fieldName);
        fieldName.setBounds(70, 10, 230, 27);

        cbFixedAutoscale.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        cbFixedAutoscale.setSelected(true);
        cbFixedAutoscale.setText(dataToolPanel.getBundleString("LABEL_FIXED_AUTOSCALE"));
        getContentPane().add(cbFixedAutoscale);
        cbFixedAutoscale.setBounds(320, 120, 101, 23);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void buttonOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonOkActionPerformed
        validParamGraph();
    }//GEN-LAST:event_buttonOkActionPerformed

    private void buttonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonCancelActionPerformed
        quitParamGraph();
    }//GEN-LAST:event_buttonCancelActionPerformed

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
    private javax.swing.JCheckBox cbFixedAutoscale;
    private javax.swing.JTextField fieldName;
    private javax.swing.JLabel labelDeltaX;
    private javax.swing.JLabel labelDeltaY;
    private javax.swing.JLabel labelName;
    private javax.swing.JLabel labelXMax;
    private javax.swing.JLabel labelXMin;
    private javax.swing.JLabel labelYMax;
    private javax.swing.JLabel labelYMin;
    private javax.swing.JTextField textFieldDeltaX;
    private javax.swing.JTextField textFieldDeltaY;
    private javax.swing.JTextField textFieldXMax;
    private javax.swing.JTextField textFieldXMin;
    private javax.swing.JTextField textFieldYMax;
    private javax.swing.JTextField textFieldYMin;
    // End of variables declaration//GEN-END:variables


    

    @Override
    public void updatePlotPanel() {
        plotPanel.setBounds(plotPanel.getX(), plotPanel.getY(), plotPanel.getWidth(), plotPanel.getHeight());
        resizePanel();
    }
}

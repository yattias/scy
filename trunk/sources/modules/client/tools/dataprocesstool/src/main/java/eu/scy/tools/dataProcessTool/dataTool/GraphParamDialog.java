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
    private DataProcessToolPanel dataToolPanel;

    private ParamGraph paramGraph;
    private DecimalFormat decimalFormat;

    public GraphParamDialog(DataProcessToolPanel dataToolPanel, ParamGraph paramGraph) {
        super();
        this.dataToolPanel = dataToolPanel;
        this.paramGraph = paramGraph;
        this.decimalFormat = new DecimalFormat();
        this.decimalFormat.setDecimalSeparatorAlwaysShown(false);
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
        resizeElements();
        if (paramGraph != null){
            textFieldXNameAxis.setText(paramGraph.getX_name());
            textFieldXMin.setText(""+decimalFormat.format(paramGraph.getX_min()));
            textFieldXMax.setText(""+decimalFormat.format(paramGraph.getX_max()));
            textFieldDeltaX.setText(""+decimalFormat.format(paramGraph.getDeltaX()));
            textFieldYNameAxis.setText(paramGraph.getY_name());
            textFieldYMin.setText(""+decimalFormat.format(paramGraph.getY_min()));
            textFieldYMax.setText(""+decimalFormat.format(paramGraph.getY_max()));
            textFieldDeltaY.setText(""+decimalFormat.format(paramGraph.getDeltaY()));
        }
    }

    private void resizeElements(){
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
        String xName = textFieldXNameAxis.getText() ;
        if (xName.length() > DataConstants.MAX_LENGHT_AXIS_NAME){
            String msg = dataToolPanel.getBundleString("MSG_LENGHT_MAX");
            msg  = MyUtilities.replace(msg, 0, dataToolPanel.getBundleString("LABEL_X_NAME_AXIS"));
            msg = MyUtilities.replace(msg, 1, ""+DataConstants.MAX_LENGHT_AXIS_NAME);
            dataToolPanel.displayError(new CopexReturn(msg, false), dataToolPanel.getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }
        if (xName.length() == 0){
            String msg = dataToolPanel.getBundleString("MSG_ERROR_FIELD_NULL");
            msg  = MyUtilities.replace(msg, 0, dataToolPanel.getBundleString("LABEL_X_NAME_AXIS"));
            dataToolPanel.displayError(new CopexReturn(msg ,false), dataToolPanel.getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }
        String s = textFieldXMin.getText();
        double xMin;
        try{
            xMin = Double.parseDouble(s);
        }catch(NumberFormatException e){
            dataToolPanel.displayError(new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_PARAM_AXIS"), false), dataToolPanel.getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }
        s = textFieldXMax.getText();
        double xMax;
        try{
            xMax = Double.parseDouble(s);
        }catch(NumberFormatException e){
            dataToolPanel.displayError(new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_PARAM_AXIS"), false), dataToolPanel.getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }
        s = textFieldDeltaX.getText();
        double deltaX;
        try{
            deltaX = Double.parseDouble(s);
        }catch(NumberFormatException e){
            dataToolPanel.displayError(new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_PARAM_AXIS"), false), dataToolPanel.getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }
        //y
        String yName = textFieldYNameAxis.getText() ;
        if (yName.length() > DataConstants.MAX_LENGHT_AXIS_NAME){
            String msg = dataToolPanel.getBundleString("MSG_LENGHT_MAX");
            msg  = MyUtilities.replace(msg, 0, dataToolPanel.getBundleString("LABEL_Y_NAME_AXIS"));
            msg = MyUtilities.replace(msg, 1, ""+DataConstants.MAX_LENGHT_AXIS_NAME);
            dataToolPanel.displayError(new CopexReturn(msg, false), dataToolPanel.getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }
        if (yName.length() == 0){
            String msg = dataToolPanel.getBundleString("MSG_ERROR_FIELD_NULL");
            msg  = MyUtilities.replace(msg, 0, dataToolPanel.getBundleString("LABEL_Y_NAME_AXIS"));
            dataToolPanel.displayError(new CopexReturn(msg ,false), dataToolPanel.getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }
        s = textFieldYMin.getText();
        double yMin;
        try{
            yMin = Double.parseDouble(s);
        }catch(NumberFormatException e){
            dataToolPanel.displayError(new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_PARAM_AXIS"), false), dataToolPanel.getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }
        s = textFieldYMax.getText();
        double yMax;
        try{
            yMax = Double.parseDouble(s);
        }catch(NumberFormatException e){
            dataToolPanel.displayError(new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_PARAM_AXIS"), false), dataToolPanel.getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }
        s = textFieldDeltaY.getText();
        double deltaY;
        try{
            deltaY = Double.parseDouble(s);
        }catch(NumberFormatException e){
            dataToolPanel.displayError(new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_PARAM_AXIS"), false), dataToolPanel.getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }
        //controle la cohérence des axes
        if (xMin>=xMax || yMin>=yMax) {
             dataToolPanel.displayError(new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_PARAM_AXIS"), false), dataToolPanel.getBundleString("TITLE_DIALOG_ERROR"));
             return;
        }
        //
        ParamGraph newParamGraph = new ParamGraph(xName, yName, xMin, xMax, yMin, yMax, deltaX, deltaY, paramGraph == null ? false : paramGraph.isAutoscale());
        boolean isOk = dataToolPanel.updateGraphParam(newParamGraph);
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
        textFieldXNameAxis = new javax.swing.JTextField();
        labelYNameAxis = new javax.swing.JLabel();
        textFieldYNameAxis = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(dataToolPanel.getBundleString("TITLE_DIALOG_GRAPH_PARAM"));
        setModal(true);
        setResizable(false);

        labelXMin.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        labelXMin.setText(dataToolPanel.getBundleString("LABEL_XMIN"));

        labelXMax.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        labelXMax.setText(dataToolPanel.getBundleString("LABEL_XMAX"));

        labelDeltaX.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        labelDeltaX.setText(dataToolPanel.getBundleString("LABEL_DELTAX"));

        labelYMin.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        labelYMin.setText(dataToolPanel.getBundleString("LABEL_YMIN"));

        labelYMax.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        labelYMax.setText(dataToolPanel.getBundleString("LABEL_YMAX"));

        labelDeltaY.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        labelDeltaY.setText(dataToolPanel.getBundleString("LABEL_DELTAY"));

        buttonOk.setText(dataToolPanel.getBundleString("BUTTON_OK"));
        buttonOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonOkActionPerformed(evt);
            }
        });

        buttonCancel.setText(dataToolPanel.getBundleString("BUTTON_CANCEL"));
        buttonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonCancelActionPerformed(evt);
            }
        });

        labelXNameAxis.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        labelXNameAxis.setText(dataToolPanel.getBundleString("LABEL_X_NAME_AXIS"));

        labelYNameAxis.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        labelYNameAxis.setText(dataToolPanel.getBundleString("LABEL_Y_NAME_AXIS"));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(labelXNameAxis)
                                    .addComponent(labelXMax)
                                    .addComponent(labelDeltaX))
                                .addGap(32, 32, 32)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(textFieldXNameAxis)
                                    .addComponent(textFieldXMin)
                                    .addComponent(textFieldXMax)
                                    .addComponent(textFieldDeltaX, javax.swing.GroupLayout.DEFAULT_SIZE, 88, Short.MAX_VALUE)))
                            .addComponent(labelXMin))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(buttonOk)
                        .addGap(50, 50, 50)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labelYNameAxis)
                            .addComponent(labelYMin)
                            .addComponent(labelYMax)
                            .addComponent(labelDeltaY))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(textFieldDeltaY, javax.swing.GroupLayout.DEFAULT_SIZE, 78, Short.MAX_VALUE)
                            .addComponent(textFieldYMax, javax.swing.GroupLayout.DEFAULT_SIZE, 78, Short.MAX_VALUE)
                            .addComponent(textFieldYNameAxis, javax.swing.GroupLayout.DEFAULT_SIZE, 78, Short.MAX_VALUE)
                            .addComponent(textFieldYMin, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 78, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(46, 46, 46)
                        .addComponent(buttonCancel)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(labelXNameAxis)
                            .addComponent(textFieldXNameAxis, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(textFieldYNameAxis, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, 11, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(labelXMin)
                            .addComponent(textFieldXMin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labelYMin)
                            .addComponent(textFieldYMin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(labelXMax)
                            .addComponent(textFieldXMax, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(textFieldYMax, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labelYMax))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(labelDeltaX)
                            .addComponent(textFieldDeltaX, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(textFieldDeltaY, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labelDeltaY))
                        .addGap(47, 47, 47))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addComponent(labelYNameAxis)))
                .addGap(35, 35, 35))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(159, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(buttonCancel, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(buttonOk, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(22, 22, 22))
        );

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
    private javax.swing.JLabel labelDeltaX;
    private javax.swing.JLabel labelDeltaY;
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
    private javax.swing.JTextField textFieldXNameAxis;
    private javax.swing.JTextField textFieldYMax;
    private javax.swing.JTextField textFieldYMin;
    private javax.swing.JTextField textFieldYNameAxis;
    // End of variables declaration//GEN-END:variables

}

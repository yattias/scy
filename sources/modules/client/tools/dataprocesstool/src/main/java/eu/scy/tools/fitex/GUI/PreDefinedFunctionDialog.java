/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * PreDefinedFunctionDialog.java
 *
 * Created on 17 mai 2010, 17:30:35
 */

package eu.scy.tools.fitex.GUI;

import eu.scy.tools.dataProcessTool.common.PreDefinedFunction;
import eu.scy.tools.dataProcessTool.common.PreDefinedFunctionCategory;
import eu.scy.tools.dataProcessTool.dataTool.FitexToolPanel;
import eu.scy.tools.dataProcessTool.utilities.DataConstants;
import eu.scy.tools.dataProcessTool.utilities.MyUtilities;
import java.awt.FontMetrics;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * dialog which allows to the user to choose a predefined function
 * @author Marjolaine
 */
public class PreDefinedFunctionDialog extends javax.swing.JDialog {
    /* owner */
    private FitexToolPanel owner ;
    private FitexPanel fitexPanel;
    /* list functions */
    private ArrayList<PreDefinedFunctionCategory> listFunctions;

    public PreDefinedFunctionDialog(FitexToolPanel owner, FitexPanel fitexPanel,ArrayList<PreDefinedFunctionCategory> listFunctions) {
        super();
        this.owner = owner;
        this.fitexPanel = fitexPanel;
        this.listFunctions = listFunctions;
        initComponents();
        setLocation(owner.getLocationDialog());
        setModal(true);
        setModalityType(DEFAULT_MODALITY_TYPE);
        setResizable(false);
        initGUI();
    }


    /** Creates new form PreDefinedFunctionDialog */
    public PreDefinedFunctionDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
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
        labelFunction = new javax.swing.JLabel();
        labelDescription = new javax.swing.JLabel();
        labelFunctionDescription = new javax.swing.JLabel();
        cbType = new javax.swing.JComboBox();
        cbFunction = new javax.swing.JComboBox();
        buttonOk = new javax.swing.JButton();
        buttonCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(owner.getBundleString("TITLE_DIALOG_PREDEFINED_FUNCTION"));
        setMinimumSize(new java.awt.Dimension(300, 200));
        setModal(true);
        setResizable(false);
        getContentPane().setLayout(null);

        labelType.setFont(new java.awt.Font("Tahoma", 1, 11));
        labelType.setText(owner.getBundleString("LABEL_TYPE"));
        getContentPane().add(labelType);
        labelType.setBounds(10, 10, 80, 14);

        labelFunction.setFont(new java.awt.Font("Tahoma", 1, 11));
        labelFunction.setText(owner.getBundleString("LABEL_FUNCTION_NAME"));
        getContentPane().add(labelFunction);
        labelFunction.setBounds(10, 50, 80, 14);

        labelDescription.setFont(new java.awt.Font("Tahoma", 1, 11));
        labelDescription.setText(owner.getBundleString("LABEL_DESCRIPTION"));
        getContentPane().add(labelDescription);
        labelDescription.setBounds(10, 90, 80, 14);

        labelFunctionDescription.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        labelFunctionDescription.setMinimumSize(new java.awt.Dimension(75, 14));
        labelFunctionDescription.setPreferredSize(new java.awt.Dimension(75, 14));
        getContentPane().add(labelFunctionDescription);
        labelFunctionDescription.setBounds(100, 70, 200, 35);

        cbType.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbTypeItemStateChanged(evt);
            }
        });
        getContentPane().add(cbType);
        cbType.setBounds(100, 10, 70, 20);

        cbFunction.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbFunctionItemStateChanged(evt);
            }
        });
        getContentPane().add(cbFunction);
        cbFunction.setBounds(100, 50, 70, 30);

        buttonOk.setText(owner.getBundleString("BUTTON_OK"));
        buttonOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonOkActionPerformed(evt);
            }
        });
        getContentPane().add(buttonOk);
        buttonOk.setBounds(50, 140, 99, 23);

        buttonCancel.setText(owner.getBundleString("BUTTON_CANCEL"));
        buttonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonCancelActionPerformed(evt);
            }
        });
        getContentPane().add(buttonCancel);
        buttonCancel.setBounds(190, 140, 99, 23);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cbTypeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbTypeItemStateChanged
        changeType();
    }//GEN-LAST:event_cbTypeItemStateChanged

    private void cbFunctionItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbFunctionItemStateChanged
        changeFunction();
    }//GEN-LAST:event_cbFunctionItemStateChanged

    private void buttonOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonOkActionPerformed
        validPreDefinedFunction();
    }//GEN-LAST:event_buttonOkActionPerformed

    private void buttonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonCancelActionPerformed
        this.dispose();
    }//GEN-LAST:event_buttonCancelActionPerformed


    private void initGUI(){
        ArrayList<String> list = new ArrayList();
        for(Iterator<PreDefinedFunctionCategory> cat = listFunctions.iterator();cat.hasNext();){
            String s = "<html>"+cat.next().getName(owner.getLocale())+"</html>";
            cbType.addItem(s);
            list.add(s);
        }
        int max = getMaxWidth(list, cbType.getFontMetrics(cbType.getFont()));
        cbType.setSize(max, cbType.getHeight());
        labelType.setSize(MyUtilities.lenghtOfString(this.labelType.getText(), getFontMetrics(this.labelType.getFont())), labelType.getHeight());
        labelFunction.setSize(MyUtilities.lenghtOfString(this.labelFunction.getText(), getFontMetrics(this.labelFunction.getFont())), labelFunction.getHeight());
        labelDescription.setSize(MyUtilities.lenghtOfString(this.labelDescription.getText(), getFontMetrics(this.labelDescription.getFont())), labelDescription.getHeight());
        int maxL = Math.max(labelType.getWidth(), labelFunction.getWidth());
        maxL = Math.max(maxL, labelDescription.getWidth());
        int x = maxL + 10+ labelType.getX();
        cbType.setBounds(x, cbType.getY(), cbType.getWidth(), cbType.getHeight());
        cbFunction.setBounds(x, cbFunction.getY(), cbFunction.getWidth(), cbFunction.getHeight());
        labelFunctionDescription.setBounds(x, labelFunctionDescription.getY(), labelFunctionDescription.getWidth(), labelFunctionDescription.getHeight());
        buttonOk.setSize(60+MyUtilities.lenghtOfString(this.buttonOk.getText(), getFontMetrics(this.buttonOk.getFont())), buttonOk.getHeight());
        buttonCancel.setSize(60+MyUtilities.lenghtOfString(this.buttonCancel.getText(), getFontMetrics(this.buttonCancel.getFont())), buttonCancel.getHeight());
        changeType();
    }

    private void validPreDefinedFunction(){
        PreDefinedFunction fct = getSelectedFunction();
        fitexPanel.setPredefinedFunction(fct);
        this.dispose();
    }

    private void changeType(){
        cbFunction.removeAllItems();
        PreDefinedFunctionCategory cat = getSelectedCategory();
        ArrayList<String> list= new ArrayList();
        if(cat != null){
            for(Iterator<PreDefinedFunction> f = cat.getListFunctions().iterator();f.hasNext();){
                String s = "<html>"+f.next().getName(owner.getLocale())+"</html>";
                cbFunction.addItem(s);
                list.add(s);
            }
            if(cbFunction.getItemCount() > 0)
                cbFunction.setSelectedIndex(0);
        }
        int max = getMaxWidth(list, cbFunction.getFontMetrics(cbFunction.getFont()));
        cbFunction.setSize(max, cbFunction.getHeight());
        repaint();
    }
    private void changeFunction(){
        PreDefinedFunction fct = getSelectedFunction();
        String txt = "";
        if(fct == null){
            labelFunctionDescription.setText(txt);
        }else{
            txt = "y = ";
            if(fct.getType() == DataConstants.FUNCTION_TYPE_X_FCT_Y)
                txt = "x = ";
            txt += fct.getDescription() ;
            labelFunctionDescription.setText("<html>"+txt+"</html>");
        }
        labelFunctionDescription.setBounds(labelFunctionDescription.getX(), labelFunctionDescription.getY(), MyUtilities.lenghtOfString(MyUtilities.getTextWithoutHTML(labelFunctionDescription.getText()), getFontMetrics(this.labelFunctionDescription.getFont())), labelFunctionDescription.getHeight());
        int m = Math.max(labelFunctionDescription.getWidth(), cbFunction.getWidth());
        setSize(labelFunctionDescription.getX()+m+10, getHeight());
        buttonOk.setBounds(getWidth()/5, buttonOk.getY(), buttonOk.getWidth(), buttonOk.getHeight());
        buttonCancel.setBounds(getWidth()-(getWidth()/5)- buttonCancel.getWidth(), buttonCancel.getY(), buttonCancel.getWidth(), buttonCancel.getHeight());
        repaint();
    }

    private PreDefinedFunctionCategory getSelectedCategory(){
        int id = cbType.getSelectedIndex();
        if(id == -1){
            return null;
        }else{
            return listFunctions.get(id);
        }
    }

     private PreDefinedFunction getSelectedFunction(){
        int id = cbFunction.getSelectedIndex();
        if(id == -1){
            return null;
        }else{
            PreDefinedFunctionCategory cat = getSelectedCategory();
            if(cat == null){
                return null;
            }else{
                return cat.getListFunctions().get(id);
            }
        }
    }
     
     private int getMaxWidth(ArrayList<String> list, FontMetrics fm){
         int max = 0;
         for(Iterator<String> s = list.iterator();s.hasNext();){
             max = Math.max(max,MyUtilities.lenghtOfString(MyUtilities.getTextWithoutHTML(s.next()), fm));
         }
         return max+40;
     }
    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                PreDefinedFunctionDialog dialog = new PreDefinedFunctionDialog(new javax.swing.JFrame(), true);
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
    private javax.swing.JComboBox cbFunction;
    private javax.swing.JComboBox cbType;
    private javax.swing.JLabel labelDescription;
    private javax.swing.JLabel labelFunction;
    private javax.swing.JLabel labelFunctionDescription;
    private javax.swing.JLabel labelType;
    // End of variables declaration//GEN-END:variables

}

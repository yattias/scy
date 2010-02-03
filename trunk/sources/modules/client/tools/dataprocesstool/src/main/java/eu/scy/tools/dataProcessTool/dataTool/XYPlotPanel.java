/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.dataProcessTool.dataTool;

import eu.scy.tools.dataProcessTool.common.DataHeader;
import eu.scy.tools.dataProcessTool.common.PlotXY;
import eu.scy.tools.dataProcessTool.utilities.ActionPlotPanel;
import eu.scy.tools.dataProcessTool.utilities.MyUtilities;
import java.awt.Font;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * one XY plot
 * @author Marjolaine
 */
public class XYPlotPanel extends JPanel {
    public static final int HEIGHT_PANEL_PARAM =30;
    private FitexToolPanel owner;
    private String titlePlot;
    /* liste des colonnes */
    private DataHeader[] listCol;
    /* liste des colonnes pour le deuxieme axe*/
    private DataHeader[] listCol2;

    private JLabel labelPlot;
    private JLabel labelX;
    private JLabel labelY;
    private JComboBox cbAxisX;
    private JComboBox cbAxisY;

    private ActionPlotPanel actionPlotPanel;



    public XYPlotPanel(FitexToolPanel owner, String titlePlot, DataHeader[] listCol) {
        super();
        this.owner = owner;
        this.titlePlot = titlePlot;
        this.listCol = listCol;
        initGUI();
    }

    private void initGUI(){
        setLayout(null);
        this.add(getLabelPlot());
        this.add(getLabelX());
        this.add(getCbAxisX());
        this.add(getLabelY());
        this.add(getCbAxisY());
        cbAxisX.setSelectedIndex(0);
        setSize(cbAxisY.getX()+cbAxisY.getWidth()+10, HEIGHT_PANEL_PARAM);
        setPreferredSize(getSize());
    }

   public void addActionPlotPanel(ActionPlotPanel action){
       this.actionPlotPanel = action;
   }

    private JLabel getLabelPlot(){
        if(labelPlot == null){
            labelPlot = new JLabel();
            labelPlot.setName("labelPlot");
            labelPlot.setText(titlePlot);
            labelPlot.setFont(new Font("Tahoma",Font.BOLD, 11));
            int w = MyUtilities.lenghtOfString(this.labelPlot.getText(), labelPlot.getFontMetrics(this.labelPlot.getFont()));
            labelPlot.setSize(w, 14);
            labelPlot.setBounds(0,3,labelPlot.getWidth(), labelPlot.getHeight());
        }
        return labelPlot;
    }

    private JLabel getLabelX(){
        if(labelX == null){
            labelX = new JLabel();
            labelX.setName("labelX");
            labelX.setText(owner.getBundleString("LABEL_X"));
            labelX.setFont(new java.awt.Font("Tahoma", 1, 11));
            this.labelX.setSize(MyUtilities.lenghtOfString(this.labelX.getText(), labelX.getFontMetrics(this.labelX.getFont())), 14);
            labelX.setBounds(labelPlot.getX()+labelPlot.getWidth()+5, 3, labelX.getWidth(), labelX.getHeight());
        }
        return labelX;
    }

    private JComboBox getCbAxisX(){
        if(cbAxisX == null){
            cbAxisX = new JComboBox();
            cbAxisX.setName("cbAxisX");
            cbAxisX.setBounds(labelX.getX()+labelX.getWidth()+5, 0, 80, 20);
            for (int i=0; i<listCol.length; i++){
                if(listCol[i] != null){
                    cbAxisX.addItem(listCol[i].getValue());
                }
            }
            cbAxisX.addItemListener(new java.awt.event.ItemListener() {
                public void itemStateChanged(java.awt.event.ItemEvent evt) {
                    updateCbAxisY();
                    setCbChange();
                }
            });
        }
        return cbAxisX;
    }

    private JLabel getLabelY(){
        if(labelY == null){
            labelY = new JLabel();
            labelY.setName("labelY");
            labelY.setText(owner.getBundleString("LABEL_Y"));
            labelY.setFont(new java.awt.Font("Tahoma", 1, 11));
            this.labelY.setSize(MyUtilities.lenghtOfString(this.labelY.getText(), getFontMetrics(this.labelY.getFont())), 14);
            labelY.setBounds(cbAxisX.getX()+cbAxisX.getWidth()+10, 3, labelY.getWidth(), labelY.getHeight());
        }
        return labelY;
    }

    private JComboBox getCbAxisY(){
        if(cbAxisY == null){
            cbAxisY = new JComboBox();
            cbAxisY.setName("cbAxisY");
            cbAxisY.setBounds(labelY.getX()+labelY.getWidth()+5, 0, 80, 20);
            cbAxisY.addItemListener(new java.awt.event.ItemListener() {
                public void itemStateChanged(java.awt.event.ItemEvent evt) {
                    setCbChange();
                }
            });
            updateCbAxisY();
        }
        return cbAxisY;
    }

    /* changement dans les cb */
    private void setCbChange(){
        if(actionPlotPanel != null)
            actionPlotPanel.updatePlotValue();
    }
    /* mise a jour des axes */
    private void updateCbAxisY(){
        if(cbAxisY == null)
            return;
        this.listCol2 = new DataHeader[this.listCol.length - 1];
        int id1 = cbAxisX.getSelectedIndex() ;
        int j=0;
        for (int i=0; i<this.listCol.length; i++){
            if (i != id1){
                this.listCol2[j] = this.listCol[i];
                j++;
            }
        }
        cbAxisY.removeAllItems();
        for (int i=0; i<listCol2.length; i++){
            if(listCol2[i] != null){
                cbAxisY.addItem(listCol2[i].getValue());
            }
        }
        cbAxisY.setSelectedIndex(0);
        repaint();
    }

    public PlotXY getPlotXY(){
        int id1 = cbAxisX.getSelectedIndex() ;
        DataHeader dataHeader1 = listCol[id1];
        int id2 = cbAxisY.getSelectedIndex() ;
        if(id2 == -1 || id2 >=listCol2.length)
            return null;
        DataHeader dataHeader2 = listCol2[id2];
        return new PlotXY(dataHeader1, dataHeader2);
    }

    public void setSelectedPlot(PlotXY plot){
        this.cbAxisX.setSelectedItem(plot.getHeaderX().getValue());
        this.cbAxisY.setSelectedItem(plot.getHeaderY().getValue());
    }

    public int getStartX(){
        return this.labelPlot.getX()+this.labelPlot.getWidth()+5;
    }
    public void setStartX(int startX){
        labelX.setBounds(startX, labelX.getY(), labelX.getWidth(), labelX.getHeight());
        cbAxisX.setBounds(labelX.getX()+labelX.getWidth()+5, cbAxisX.getY(), cbAxisX.getWidth(), cbAxisX.getHeight());
        labelY.setBounds(cbAxisX.getX()+cbAxisX.getWidth()+10, labelY.getY(), labelY.getWidth(), labelY.getHeight());
        cbAxisY.setBounds(labelY.getX()+labelY.getWidth()+5, cbAxisY.getY(), cbAxisY.getWidth(), cbAxisY.getHeight());
        setSize(cbAxisY.getX()+cbAxisY.getWidth()+10, HEIGHT_PANEL_PARAM);
        setPreferredSize(getSize());
        revalidate();
        repaint();
    }
}

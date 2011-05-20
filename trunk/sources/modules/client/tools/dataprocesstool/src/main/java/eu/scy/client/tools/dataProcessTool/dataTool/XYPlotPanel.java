/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.dataProcessTool.dataTool;

import eu.scy.client.tools.dataProcessTool.common.DataHeader;
import eu.scy.client.tools.dataProcessTool.common.PlotXY;
import eu.scy.client.tools.dataProcessTool.utilities.ActionCopexButton;
import eu.scy.client.tools.dataProcessTool.utilities.ActionPlotPanel;
import eu.scy.client.tools.dataProcessTool.utilities.CopexButtonPanel;
import eu.scy.client.tools.dataProcessTool.utilities.DataConstants;
import eu.scy.client.tools.dataProcessTool.utilities.MyUtilities;
import java.awt.Color;
import java.awt.Font;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * one XY plot
 * @author Marjolaine
 */
public class XYPlotPanel extends JPanel implements ActionCopexButton{
    public static final int HEIGHT_PANEL_PARAM =30;
    private FitexToolPanel owner;
    private String titlePlot;
    private Color plotColor;
    /* columns list */
    private DataHeader[] listCol;
    /* columns list for the 2nd axe*/
    private DataHeader[] listCol2;

    private JLabel labelPlot;
    private JLabel labelX;
    private JLabel labelY;
    private JComboBox cbAxisX;
    private JComboBox cbAxisY;
    private CopexButtonPanel buttonRemove;

    private ActionPlotPanel actionPlotPanel;

    /* Images */
    private ImageIcon imgRemove;
    private ImageIcon imgRemoveClic;
    private ImageIcon imgRemoveSurvol;
    private ImageIcon imgRemoveGris;

    public XYPlotPanel(FitexToolPanel owner, Color plotColor, DataHeader[] listCol) {
        super();
        this.owner = owner;
        this.plotColor = plotColor;
        this.listCol = listCol;
        initGUI();
    }

    private void initGUI(){
        setLayout(null);
        imgRemove = owner.getCopexImage("Bouton-AdT-28_delete.png");
        imgRemoveClic = owner.getCopexImage("Bouton-AdT-28_delete_cli.png");
        imgRemoveSurvol = owner.getCopexImage("Bouton-AdT-28_delete_sur.png");
        imgRemoveGris = owner.getCopexImage("Bouton-AdT-28_delete_gri.png");
        setTitlePlot();
        this.add(getButtonRemove());
        this.add(getLabelPlot());
        this.add(getLabelX());
        this.add(getCbAxisX());
        this.add(getLabelY());
        this.add(getCbAxisY());
        cbAxisX.setSelectedIndex(0);
        setSize(cbAxisY.getX()+cbAxisY.getWidth()+10, HEIGHT_PANEL_PARAM);
        setPreferredSize(getSize());
    }

    private void setTitlePlot(){
        if(plotColor.equals(DataConstants.SCATTER_PLOT_COLOR_1))
            titlePlot = owner.getBundleString("LABEL_AXIS_RED");
        else if( plotColor.equals(DataConstants.SCATTER_PLOT_COLOR_2))
            titlePlot = owner.getBundleString("LABEL_AXIS_PURPLE");
        else if( plotColor.equals(DataConstants.SCATTER_PLOT_COLOR_3))
            titlePlot = owner.getBundleString("LABEL_AXIS_ORANGE");
        else if( plotColor.equals(DataConstants.SCATTER_PLOT_COLOR_4))
            titlePlot = owner.getBundleString("LABEL_AXIS_BROWN");
    }
   public void addActionPlotPanel(ActionPlotPanel action){
       this.actionPlotPanel = action;
   }

   public Color getPlotColor(){
       return this.plotColor;
   }
   private CopexButtonPanel getButtonRemove(){
        if(buttonRemove == null){
            buttonRemove = new CopexButtonPanel(imgRemove.getImage(),imgRemoveSurvol.getImage(), imgRemoveClic.getImage(), imgRemoveGris.getImage() );
            buttonRemove.setBounds(0,5, buttonRemove.getWidth(), buttonRemove.getHeight());
            buttonRemove.addActionCopexButton(this);
            buttonRemove.setToolTipText(owner.getBundleString("TOOLTIPTEXT_REMOVE_PLOT"));
        }
        return buttonRemove;
    }

    private JLabel getLabelPlot(){
        if(labelPlot == null){
            labelPlot = new JLabel();
            labelPlot.setName("labelPlot");
            labelPlot.setText(titlePlot);
            labelPlot.setFont(new Font("Tahoma",Font.BOLD, 11));
            int w = MyUtilities.lenghtOfString(this.labelPlot.getText(), labelPlot.getFontMetrics(this.labelPlot.getFont()));
            labelPlot.setSize(w, 14);
            labelPlot.setBounds(buttonRemove.getX()+buttonRemove.getWidth()+5,3,labelPlot.getWidth(), labelPlot.getHeight());
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
            cbAxisX.setBounds(labelX.getX()+labelX.getWidth()+5, 0, 120, 20);
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
            cbAxisY.setBounds(labelY.getX()+labelY.getWidth()+5, 0, 120, 20);
            cbAxisY.addItemListener(new java.awt.event.ItemListener() {
                public void itemStateChanged(java.awt.event.ItemEvent evt) {
                    setCbChange();
                }
            });
            updateCbAxisY();
        }
        return cbAxisY;
    }

    /*change in the combobox*/
    private void setCbChange(){
        if(actionPlotPanel != null)
            actionPlotPanel.updatePlotValue();
    }

    /*update axis */
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
        return new PlotXY(-1, dataHeader1, dataHeader2, plotColor);
    }

    public void setSelectedPlot(PlotXY plot){
        plotColor = plot.getPlotColor();
        this.setTitlePlot();
        labelPlot.setText(titlePlot);
        int w = MyUtilities.lenghtOfString(this.labelPlot.getText(), labelPlot.getFontMetrics(this.labelPlot.getFont()));
        labelPlot.setBounds(labelPlot.getX(), labelPlot.getY(), w, labelPlot.getHeight());
        labelPlot.revalidate();
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

    @Override
    public void actionCopexButtonClic(CopexButtonPanel button) {
        if(actionPlotPanel != null)
            actionPlotPanel.removePlot(this);
    }

    public void setRemoveEnabled(boolean enabled){
        this.buttonRemove.setEnabled(enabled);
    }
}

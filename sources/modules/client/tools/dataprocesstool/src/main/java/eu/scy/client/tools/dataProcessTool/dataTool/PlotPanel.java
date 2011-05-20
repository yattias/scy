/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.dataProcessTool.dataTool;

import eu.scy.client.tools.dataProcessTool.common.DataHeader;
import eu.scy.client.tools.dataProcessTool.common.PlotXY;
import eu.scy.client.tools.dataProcessTool.utilities.ActionPlot;
import eu.scy.client.tools.dataProcessTool.utilities.ActionPlotPanel;
import eu.scy.client.tools.dataProcessTool.utilities.DataConstants;
import eu.scy.client.tools.dataProcessTool.utilities.MyUtilities;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * plot panel : list of xyplotpanel
 * @author Marjolaine
 */
public class PlotPanel extends JPanel implements  ActionPlotPanel {
    private FitexToolPanel owner;
    /* columns list */
    private DataHeader[] listCol;
    private Color[] tabColor = null;
    private ActionPlot actionPlot;

    private JPanel xyPlotPanel;
    private ArrayList<XYPlotPanel> listPlotPanel;
    private JButton buttonAddPlot;

    public PlotPanel(FitexToolPanel owner, DataHeader[] listCol) {
        super();
        this.owner = owner;
        this.listCol = listCol;
        tabColor = new Color[DataConstants.MAX_PLOT];
        tabColor[0] = DataConstants.SCATTER_PLOT_COLOR_1;
        tabColor[1] = DataConstants.SCATTER_PLOT_COLOR_2;
        tabColor[2] = DataConstants.SCATTER_PLOT_COLOR_3;
        tabColor[3] = DataConstants.SCATTER_PLOT_COLOR_4;
        listPlotPanel = new ArrayList();
        initGUI();
    }



    public void addActionPlot(ActionPlot actionPlot){
        this.actionPlot = actionPlot;
    }
    private void initGUI(){
        this.setLayout(null);
        this.add(getXYPlotPanel());
        this.add(getButtonAddPlot());
        addPlot();
    }

    private JPanel getXYPlotPanel(){
        if(xyPlotPanel == null){
            xyPlotPanel = new JPanel();
            xyPlotPanel.setName("xyPlotPanel");
            xyPlotPanel.setLayout(null);
        }
        return xyPlotPanel;
    }
    private XYPlotPanel getXYPlotPanel(int id){
        int idColor = getIdColor();
        XYPlotPanel aPlot = new XYPlotPanel(owner, tabColor[idColor], listCol);
        aPlot.addActionPlotPanel(this);
        listPlotPanel.add(aPlot);
        xyPlotPanel.add(aPlot);
        aPlot.setBounds(0, id*XYPlotPanel.HEIGHT_PANEL_PARAM , aPlot.getWidth(), XYPlotPanel.HEIGHT_PANEL_PARAM);
        resizePanel();
        return aPlot;
    }

    private int getIdColor(){
        for (int i=0; i<tabColor.length; i++){
            XYPlotPanel panel = getXYPlotPanelWithColor(tabColor[i]);
            if(panel == null)
                return i;
        }
        return 0;
    }

    private XYPlotPanel getXYPlotPanelWithColor(Color color){
        for (Iterator<XYPlotPanel> panel = listPlotPanel.iterator();panel.hasNext();){
            XYPlotPanel p = panel.next();
            if(p.getPlotColor().equals(color))
                return p;
        }
        return null;
    }

    private JButton getButtonAddPlot(){
        if(this.buttonAddPlot == null){
            buttonAddPlot = new JButton();
            buttonAddPlot.setName("buttonAddPlot");
            buttonAddPlot.setText(owner.getBundleString("BUTTON_ADD_PLOT"));
            buttonAddPlot.setSize(60+MyUtilities.lenghtOfString(buttonAddPlot.getText(), buttonAddPlot.getFontMetrics(buttonAddPlot.getFont())), 23);
            buttonAddPlot.setToolTipText(owner.getBundleString("TOOLTIPTEXT_ADD_PLOT"));
            buttonAddPlot.setBounds(10, 0, buttonAddPlot.getWidth(), buttonAddPlot.getHeight());
            buttonAddPlot.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    addPlot();
                }
            });
        }
        return buttonAddPlot ;
    }

    
    
    private void updateButtons(){
        if(buttonAddPlot== null)
            return;
        int nb = listPlotPanel.size();
        boolean removeEnabled = nb > 1;
        for (Iterator<XYPlotPanel> p = listPlotPanel.iterator();p.hasNext();){
            p.next().setRemoveEnabled(removeEnabled);
        }
        //buttonAddPlot.setEnabled(nb < MyConstants.MAX_PLOT && nb < listCol.length && controlUnitCoherence());
        buttonAddPlot.setEnabled(nb < DataConstants.MAX_PLOT && nb < listCol.length );
    }
    
    private void addPlot(){
        xyPlotPanel.add(getXYPlotPanel(listPlotPanel.size()));
        xyPlotPanel.revalidate();
        xyPlotPanel.repaint();
        updateButtons();
    }

   
    private void resizePanel(){
        int nb = listPlotPanel.size();
        int width = 0;
        int startX = 0;
        int nbP = listPlotPanel.size();
        for (int i=0; i<nbP; i++){
            listPlotPanel.get(i).setBounds(listPlotPanel.get(i).getX(), i*XYPlotPanel.HEIGHT_PANEL_PARAM, listPlotPanel.get(i).getWidth(), listPlotPanel.get(i).getHeight());
        }
        for(Iterator<XYPlotPanel> p = listPlotPanel.iterator();p.hasNext();){
            XYPlotPanel plotPanel = p.next();
            //width = Math.max(width, plotPanel.getWidth());
            startX = Math.max(startX,plotPanel.getStartX());
        }
        for(Iterator<XYPlotPanel> p = listPlotPanel.iterator();p.hasNext();){
            XYPlotPanel plotPanel = p.next();
            plotPanel.setStartX(startX);
            width = Math.max(width, plotPanel.getWidth());
        }
        int height = XYPlotPanel.HEIGHT_PANEL_PARAM*nb;
        xyPlotPanel.setSize(width, height);
        xyPlotPanel.setBounds(0,0,xyPlotPanel.getWidth(), xyPlotPanel.getHeight());
        buttonAddPlot.setBounds(buttonAddPlot.getX(), xyPlotPanel.getY()+xyPlotPanel.getHeight(), buttonAddPlot.getWidth(), buttonAddPlot.getHeight());
        setSize(width, buttonAddPlot.getY()+buttonAddPlot.getHeight()+20);
        revalidate();
        repaint();
        if(actionPlot != null)
            actionPlot.updatePlotPanel();
    }

    public ArrayList<PlotXY> getListPlotXY(){
        ArrayList<PlotXY> list = new ArrayList();
        for (Iterator<XYPlotPanel> p = listPlotPanel.iterator();p.hasNext();){
            PlotXY plot = p.next().getPlotXY();
            if(plot != null)
                list.add(plot);
        }
        return list;
    }

    public void setSelectedPlots(ArrayList<PlotXY> listPlots){
        int nb = listPlots.size();
        listPlotPanel.get(0).setSelectedPlot(listPlots.get(0));
        for(int i=1; i<nb; i++){
            addPlot();
            listPlotPanel.get(i).setSelectedPlot(listPlots.get(i));
        }
        resizePanel();
    }

    // control if  consistency unit
    private boolean controlUnitCoherence(){
        ArrayList<PlotXY> listPlot = getListPlotXY();
        ArrayList<DataHeader> listHeaderRest = getListRestHeader(listPlot);
        for(Iterator<DataHeader> h = listHeaderRest.iterator();h.hasNext();){
            if(canPlotWith(h.next(),listPlot))
                return true;
        }
        return false;
    }

    /* returns the list of headers remaining */
    private ArrayList<DataHeader> getListRestHeader(ArrayList<PlotXY> listPlot){
        ArrayList<DataHeader> list = new ArrayList();
        for(int j=0; j<listCol.length; j++){
            if(!isPlot(listCol[j], listPlot)){
                list.add(listCol[j]);
            }
        }
        return list;
    }

    private boolean isPlot(DataHeader header, ArrayList<PlotXY> listPlot){
        for(Iterator<PlotXY> p = listPlot.iterator();p.hasNext();){
            PlotXY plot = p.next();
            if(plot.isOnNo(header.getNoCol()))
                return true;
        }
        return false;
    }

    private boolean canPlotWith(DataHeader header, ArrayList<PlotXY> listPlot){
        for(Iterator<PlotXY> p = listPlot.iterator();p.hasNext();){
            PlotXY plot = p.next();
            if((plot.getHeaderX() == null && header.getUnit() == null) || (plot.getHeaderY() == null && header.getUnit() == null))
                return true;
            if(header.getUnit()!= null && ( (plot.getHeaderX().getUnit() != null && plot.getHeaderX().getUnit().equals(header.getUnit())) || (plot.getHeaderY().getUnit() != null && plot.getHeaderY().getUnit().equals(header.getUnit()))  ))
                return true;
        }
        return false;
    }

    @Override
    public void updatePlotValue() {
        updateButtons();
    }

    @Override
    public void removePlot(XYPlotPanel plotPanel) {
        int id = listPlotPanel.indexOf(plotPanel);
        if(id != -1){
            xyPlotPanel.remove(listPlotPanel.get(id));
            listPlotPanel.remove(id);
            xyPlotPanel.revalidate();
            xyPlotPanel.repaint();
            resizePanel();
            updateButtons();
        }
    }
}

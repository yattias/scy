/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.dataProcessTool.dataTool;

import eu.scy.tools.dataProcessTool.common.DataHeader;
import eu.scy.tools.dataProcessTool.common.PlotXY;
import eu.scy.tools.dataProcessTool.utilities.ActionCopexButton;
import eu.scy.tools.dataProcessTool.utilities.ActionPlot;
import eu.scy.tools.dataProcessTool.utilities.ActionPlotPanel;
import eu.scy.tools.dataProcessTool.utilities.CopexButtonPanel;
import eu.scy.tools.dataProcessTool.utilities.MyConstants;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 * plot panel : list of xyplotpanel
 * @author Marjolaine
 */
public class PlotPanel extends JPanel implements ActionCopexButton, ActionPlotPanel {
    private FitexToolPanel owner;
    /* liste des colonnes */
    private DataHeader[] listCol;
    private String[] tabPlot  = null;
    private ActionPlot actionPlot;

    private JPanel xyPlotPanel;
    private ArrayList<XYPlotPanel> listPlotPanel;
    private CopexButtonPanel buttonAddPlot;
    private CopexButtonPanel buttonRemovePlot;

    public PlotPanel(FitexToolPanel owner, DataHeader[] listCol) {
        super();
        this.owner = owner;
        this.listCol = listCol;
        tabPlot = new String[MyConstants.MAX_PLOT];
        tabPlot[0] = (owner.getBundleString("LABEL_AXIS_RED"));
        tabPlot[1] = (owner.getBundleString("LABEL_AXIS_PURPLE"));
        tabPlot[2] = (owner.getBundleString("LABEL_AXIS_ORANGE"));
        tabPlot[3] = (owner.getBundleString("LABEL_AXIS_BROWN"));
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
        this.add(getButtonRemoveParam());
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
        XYPlotPanel aPlot = new XYPlotPanel(owner, tabPlot[id], listCol);
        aPlot.addActionPlotPanel(this);
        listPlotPanel.add(aPlot);
        xyPlotPanel.add(aPlot);
        aPlot.setBounds(0, id*XYPlotPanel.HEIGHT_PANEL_PARAM , aPlot.getWidth(), XYPlotPanel.HEIGHT_PANEL_PARAM);
        resizePanel();
        return aPlot;
    }

    
    private CopexButtonPanel getButtonAddPlot(){
        if(this.buttonAddPlot == null){
            ImageIcon buttonAdd = owner.getCopexImage("Bouton-onglet_ouverture.png");
            ImageIcon buttonAddSurvol = owner.getCopexImage("Bouton-onglet_ouverture_sur.png");
            ImageIcon buttonAddClic = owner.getCopexImage("Bouton-onglet_ouverture_cli.png");
            ImageIcon buttonAddDisabled = owner.getCopexImage("Bouton-onglet_ouverture_grise.png");
            buttonAddPlot = new CopexButtonPanel(28, buttonAdd.getImage(), buttonAddSurvol.getImage(), buttonAddClic.getImage(), buttonAddDisabled.getImage());
            buttonAddPlot.addActionCopexButton(this);
            buttonAddPlot.setToolTipText(owner.getBundleString("TOOLTIPTEXT_ADD_PLOT"));
            buttonAddPlot.setBounds(10, 0, 28, 20);
        }
        return buttonAddPlot ;
    }

    private CopexButtonPanel getButtonRemoveParam(){
        if(this.buttonRemovePlot == null){
            ImageIcon buttonRemove = owner.getCopexImage("Bouton-onglet_moins.png");
            ImageIcon buttonRemoveSurvol = owner.getCopexImage("Bouton-onglet_moins_sur.png");
            ImageIcon buttonRemoveClic = owner.getCopexImage("Bouton-onglet_moins_cli.png");
            ImageIcon buttonRemoveDisabled = owner.getCopexImage("Bouton-onglet_moins_grise.png");
            buttonRemovePlot = new CopexButtonPanel(28, buttonRemove.getImage(), buttonRemoveSurvol.getImage(), buttonRemoveClic.getImage(), buttonRemoveDisabled.getImage());
            buttonRemovePlot.addActionCopexButton(this);
            buttonRemovePlot.setToolTipText(owner.getBundleString("TOOLTIPTEXT_REMOVE_PLOT"));
            buttonRemovePlot.setBounds(buttonAddPlot.getX()+buttonAddPlot.getWidth()+5, buttonAddPlot.getY(), 28,20);
        }
        return buttonRemovePlot ;
    }

    @Override
    public void actionCopexButtonClic(CopexButtonPanel button) {
        if(button.equals(this.buttonAddPlot)){
            addPlot();
        }else{
            removePlot();
        }
    }

    
    private void updateButtons(){
        if(buttonAddPlot== null || buttonRemovePlot ==null)
            return;
        int nb = listPlotPanel.size();
        buttonRemovePlot.setEnabled(nb > 1);
        //buttonAddPlot.setEnabled(nb < MyConstants.MAX_PLOT && nb < listCol.length && controlUnitCoherence());
        buttonAddPlot.setEnabled(nb < MyConstants.MAX_PLOT && nb < listCol.length );
    }
    
    private void addPlot(){
        xyPlotPanel.add(getXYPlotPanel(listPlotPanel.size()));
        xyPlotPanel.revalidate();
        xyPlotPanel.repaint();
        updateButtons();
    }

    private void removePlot(){
        int id = listPlotPanel.size()-1;
        xyPlotPanel.remove(listPlotPanel.get(id));
        listPlotPanel.remove(id);
        xyPlotPanel.revalidate();
        xyPlotPanel.repaint();
        resizePanel();
        updateButtons();
    }

    private void resizePanel(){
        int nb = listPlotPanel.size();
        int width = 0;
        int startX = 0;
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
        buttonAddPlot.setBounds(buttonAddPlot.getX(), xyPlotPanel.getY()+xyPlotPanel.getHeight()+10, buttonAddPlot.getWidth(), buttonAddPlot.getHeight());
        buttonRemovePlot.setBounds(buttonRemovePlot.getX(), buttonAddPlot.getY(), buttonRemovePlot.getWidth(), buttonRemovePlot.getHeight());
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
    }

    // controle s'il reste des donnees a plotter encore coherente avec celles deja demandees
    private boolean controlUnitCoherence(){
        ArrayList<PlotXY> listPlot = getListPlotXY();
        ArrayList<DataHeader> listHeaderRest = getListRestHeader(listPlot);
        for(Iterator<DataHeader> h = listHeaderRest.iterator();h.hasNext();){
            if(canPlotWith(h.next(),listPlot))
                return true;
        }
        return false;
    }

    /* retourne la liste des header restants */
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
            if(plot.getHeaderX().getUnit().equals(header.getUnit()) || plot.getHeaderY().getUnit().equals(header.getUnit()))
                return true;
        }
        return false;
    }

    @Override
    public void updatePlotValue() {
        updateButtons();
    }
}

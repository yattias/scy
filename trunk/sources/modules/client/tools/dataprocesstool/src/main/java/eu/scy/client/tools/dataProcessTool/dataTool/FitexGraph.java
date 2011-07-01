/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.dataProcessTool.dataTool;

import eu.scy.client.tools.dataProcessTool.common.FunctionParam;
import eu.scy.client.tools.dataProcessTool.common.Graph;
import eu.scy.client.tools.dataProcessTool.common.ParamGraph;
import eu.scy.client.tools.dataProcessTool.common.PlotXY;
import eu.scy.client.tools.dataProcessTool.common.Visualization;
import eu.scy.client.tools.fitex.GUI.ActionFitex;
import eu.scy.client.tools.fitex.GUI.FitexPanel;
import java.awt.Color;
import java.util.ArrayList;

/**
 *
 * @author Marjolaine
 */
public class FitexGraph extends CopexGraph implements ActionFitex {
    private FitexPanel fitexPanel;

    public FitexGraph(FitexToolPanel owner,long dbKeyDs, Visualization vis, FitexPanel fitexPanel) {
        super(owner,dbKeyDs, vis, fitexPanel);
        this.fitexPanel = fitexPanel;
        fitexPanel.addActionFitex(this);
    }

    public FitexPanel getFitexPanel() {
        return fitexPanel;
    }

    @Override
    public void openDatas() {

    }

    @Override
    public void setParam(ArrayList<PlotXY> plots, double xmin, double xmax, double deltaX, double ymin, double ymax, double deltaY, boolean deltaFixedAutoscale) {
        owner.updateGraphParam((Graph)vis, vis.getName(), new ParamGraph(plots,  xmin, xmax, ymin, ymax, deltaX, deltaY, deltaFixedAutoscale), true);
    }

//    @Override
//    public void setAutoScale(boolean autoScale) {
//        owner.setAutoScale(dbKeyDs, vis.getDbKey(), autoScale);
//    }

    @Override
    public void setFunctionModel(String function, char type, Color color, ArrayList<FunctionParam> listParam, String idPredefFunction) {
        owner.setFunctionModel((Graph)vis, function, type, color, listParam, idPredefFunction, true);
    }

    @Override
    public void setPreviousZoom() {
        owner.setPreviousZoom((Graph)vis);
    }

}

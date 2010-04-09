/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.dataProcessTool.dataTool;

import eu.scy.tools.dataProcessTool.common.FunctionParam;
import eu.scy.tools.dataProcessTool.common.Graph;
import eu.scy.tools.dataProcessTool.common.ParamGraph;
import eu.scy.tools.dataProcessTool.common.PlotXY;
import eu.scy.tools.dataProcessTool.common.Visualization;
import eu.scy.tools.fitex.GUI.ActionFitex;
import eu.scy.tools.fitex.GUI.FitexPanel;
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
        owner.updateGraphParam((Graph)vis, vis.getName(), new ParamGraph(plots,  xmin, xmax, ymin, ymax, deltaX, deltaY, deltaFixedAutoscale));
    }

//    @Override
//    public void setAutoScale(boolean autoScale) {
//        owner.setAutoScale(dbKeyDs, vis.getDbKey(), autoScale);
//    }

    @Override
    public void setFunctionModel(String function, Color color, ArrayList<FunctionParam> listParam) {
        owner.setFunctionModel((Graph)vis, function, color, listParam);
    }

    @Override
    public void setPreviousZoom() {
        owner.setPreviousZoom((Graph)vis);
    }

}

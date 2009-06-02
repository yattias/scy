/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.dataProcessTool.dataTool;

import eu.scy.tools.dataProcessTool.common.Visualization;
import eu.scy.tools.fitex.GUI.ActionFitex;
import eu.scy.tools.fitex.GUI.FitexPanel;
import java.awt.Color;

/**
 *
 * @author Marjolaine
 */
public class FitexGraph extends CopexGraph implements ActionFitex {

    public FitexGraph(DataProcessToolPanel owner,long dbKeyDs, Visualization vis, FitexPanel fitexPanel) {
        super(owner,dbKeyDs, vis, fitexPanel);
        fitexPanel.addActionFitex(this);
    }

    @Override
    public void openDatas() {

    }

    @Override
    public void setParam(boolean autoScale, double xmin, double xmax, double deltaX, double ymin, double ymax, double deltaY) {
        owner.setParamGraph(dbKeyDs, vis.getDbKey(), autoScale, xmin, xmax, deltaX, ymin, ymax, deltaY);
    }

    @Override
    public void setAutoScale(boolean autoScale) {
        owner.setAutoScale(dbKeyDs, vis.getDbKey(), autoScale);
    }

    @Override
    public void setFunctionModel(String function, Color color) {
        owner.setFunctionModel(function, color);
    }

}

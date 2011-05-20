package eu.scy.client.tools.fitex.GUI;

import eu.scy.client.tools.dataProcessTool.common.FunctionParam;
import eu.scy.client.tools.dataProcessTool.common.PlotXY;
import java.awt.Color;
import java.util.ArrayList;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * interface fitexPanel
 * @author Marjolaine
 */
public interface ActionFitex {
    /* data button */
    public void openDatas();

    /* update parameter */
    public void setParam(ArrayList<PlotXY> plots,double xmin, double xmax, double deltaX, double ymin, double ymax, double deltaY, boolean deltaFixedAutoscale);

    
    /*update function model */
    public void setFunctionModel(String function, char type, Color color, ArrayList<FunctionParam> listParam, String idPredefFunction);

    /* zoom */
    public void setPreviousZoom();
}

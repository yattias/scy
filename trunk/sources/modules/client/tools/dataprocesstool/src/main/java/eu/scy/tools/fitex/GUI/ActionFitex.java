package eu.scy.tools.fitex.GUI;

import eu.scy.tools.dataProcessTool.common.FunctionParam;
import eu.scy.tools.dataProcessTool.common.PlotXY;
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
    /* bouton Donnees */
    public void openDatas();

    /* mise a jour des parametres */
    public void setParam(ArrayList<PlotXY> plots,double xmin, double xmax, double deltaX, double ymin, double ymax, double deltaY, boolean deltaFixedAutoscale);

    
    /*mise a jour d'une fonction modele */
    public void setFunctionModel(String function, char type, Color color, ArrayList<FunctionParam> listParam, String idPredefFunction);

    /* zoom */
    public void setPreviousZoom();
}

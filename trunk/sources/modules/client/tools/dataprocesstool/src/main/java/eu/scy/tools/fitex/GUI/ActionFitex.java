package eu.scy.tools.fitex.GUI;

import eu.scy.tools.dataProcessTool.common.DataHeader;
import eu.scy.tools.dataProcessTool.common.FunctionParam;
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
    public void setParam(DataHeader headerX, DataHeader headerY, boolean autoScale, double xmin, double xmax, double deltaX, double ymin, double ymax, double deltaY);

    /* mise a jour auto scale */
    public void setAutoScale(boolean autoScale);

    /*mise a jour d'une fonction modele */
    public void setFunctionModel(String function, Color color, ArrayList<FunctionParam> listParam);

}

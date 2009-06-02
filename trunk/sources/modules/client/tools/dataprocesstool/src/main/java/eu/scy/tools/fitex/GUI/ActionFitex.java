package eu.scy.tools.fitex.GUI;

import java.awt.Color;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * interface fitexPanel
 * @author Marjolaine
 */
public interface ActionFitex {
    /* bouton Données */
    public void openDatas();

    /* mise à jour des paramètres */
    public void setParam(boolean autoScale, double xmin, double xmax, double deltaX, double ymin, double ymax, double deltaY);

    /* mise à jour auto scale */
    public void setAutoScale(boolean autoScale);

    /*mise à jour d'une fonction modele */
    public void setFunctionModel(String function, Color color);

}

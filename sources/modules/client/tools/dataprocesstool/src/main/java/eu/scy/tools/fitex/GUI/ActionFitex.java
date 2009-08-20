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
    /* bouton Donnees */
    public void openDatas();

    /* mise a  jour des parametres */
    public void setParam(boolean autoScale, double xmin, double xmax, double deltaX, double ymin, double ymax, double deltaY);

    /* mise a  jour auto scale */
    public void setAutoScale(boolean autoScale);

    /*mise a  jour d'une fonction modele */
    public void setFunctionModel(String function, Color color);

}

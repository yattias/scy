/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.utilities;

import eu.scy.tools.copex.common.Material;
import eu.scy.tools.copex.common.QData;


/**
 *
 * @author Marjolaine
 */
public interface ActionSelectParamTaskRepeat {
    /* ajout d'un nouveau materiel */
    public void addOutputMaterial(int index, int noRepeat,Material material);
    /* ajout d'un nouveau data */
    public void addOutputData(int index, int noRepeat,QData data);
}

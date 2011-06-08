/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.utilities;

import eu.scy.client.tools.copex.common.Material;
import eu.scy.client.tools.copex.common.QData;


/**
 *
 * @author Marjolaine
 */
public interface ActionSelectParamTaskRepeat {
    /* add new material */
    public void addOutputMaterial(int index, int noRepeat,Material material);
    /* add new data */
    public void addOutputData(int index, int noRepeat,QData data);
}

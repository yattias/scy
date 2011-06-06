/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.utilities;

import eu.scy.client.tools.copex.common.MaterialUsed;


/**
 *
 * @author Marjolaine
 */
public interface ActionMaterialDetail {
    public void actionResize();
    public void actionRemoveMaterial(MaterialUsed mUsed);
    public void saveData(MaterialUsed mUsed);
}

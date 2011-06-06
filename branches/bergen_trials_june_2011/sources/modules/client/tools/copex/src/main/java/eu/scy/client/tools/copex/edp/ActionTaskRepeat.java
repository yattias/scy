/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.edp;

import eu.scy.client.tools.copex.common.InitialAcquisitionOutput;
import eu.scy.client.tools.copex.common.InitialManipulationOutput;
import eu.scy.client.tools.copex.common.InitialParamData;
import eu.scy.client.tools.copex.common.InitialParamMaterial;
import eu.scy.client.tools.copex.common.InitialParamQuantity;
import eu.scy.client.tools.copex.common.InitialTreatmentOutput;

/**
 * action task repeat
 * @author Marjolaine
 */
public interface ActionTaskRepeat {
    /* reisze du panel */
    public void actionResize();
    /* modification du nombre d erepetition */
    public void actionUpdateNbRepeat(int nbRepeat);
    /* selection de parametre */
    public void actionSetSelected(Object oldSel, Object newSel);

}

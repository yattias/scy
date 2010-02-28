/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.edp;

import eu.scy.tools.copex.common.InitialAcquisitionOutput;
import eu.scy.tools.copex.common.InitialManipulationOutput;
import eu.scy.tools.copex.common.InitialParamData;
import eu.scy.tools.copex.common.InitialParamMaterial;
import eu.scy.tools.copex.common.InitialParamQuantity;
import eu.scy.tools.copex.common.InitialTreatmentOutput;

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

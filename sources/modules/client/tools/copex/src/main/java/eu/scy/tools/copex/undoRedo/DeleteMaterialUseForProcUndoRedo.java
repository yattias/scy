/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.undoRedo;

import eu.scy.tools.copex.common.ExperimentalProcedure;
import eu.scy.tools.copex.common.Material;
import eu.scy.tools.copex.controller.ControllerInterface;
import eu.scy.tools.copex.edp.CopexTree;
import eu.scy.tools.copex.edp.EdPPanel;
import eu.scy.tools.copex.utilities.MyConstants;



/**
 * suppression d'un materiel utlise pour un proc
 * @author MBO
 */
public class DeleteMaterialUseForProcUndoRedo extends CopexUndoRedo {
    // ATTRIBUTS
    /*  Proc */
    private ExperimentalProcedure proc;
    /* Mat */
    private Material material;
    /*justification*/
    private String justification;

    // CONSTRUCTEURS
    public DeleteMaterialUseForProcUndoRedo(EdPPanel edP, ControllerInterface controller, CopexTree tree, ExperimentalProcedure proc, Material material, String justification) {
        super(edP, controller, tree);
        this.proc = proc;
        this.material = material;
        this.justification = justification;
    }

    // OVERRIDE
    @Override
    public void redo() {
        super.redo();
        edP.removeMaterialUse(material, MyConstants.REDO);
    }

    @Override
    public void undo() {
        super.undo();
        edP.addMaterialUseForProc(material, justification, MyConstants.UNDO);
    }
}

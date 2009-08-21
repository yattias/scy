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
 * mise a jour du materiel utlise pour un proc
 * @author MBO 
 */
public class UpdateMaterialUseForProcUndoRedo extends CopexUndoRedo {
    // ATTRIBUTS
    /*  Proc */
    private ExperimentalProcedure proc;
    /* Mat */
    private Material material;
    /*justification*/
    private String oldJustification;
    private String newJustification;

    // CONSTRUCTEURS
    public UpdateMaterialUseForProcUndoRedo(EdPPanel edP, ControllerInterface controller, CopexTree tree, ExperimentalProcedure proc, Material material, String oldJustification, String newJustification) {
        super(edP, controller, tree);
        this.proc = proc;
        this.material = material;
        this.oldJustification = oldJustification;
        this.newJustification = newJustification;
    }

    // OVERRIDE
    @Override
    public void redo() {
        super.redo();
        edP.updateMaterialUseForProc(material, newJustification, MyConstants.REDO);
    }

    @Override
    public void undo() {
        super.undo();
        edP.updateMaterialUseForProc(material, oldJustification, MyConstants.UNDO);
    }
    
}

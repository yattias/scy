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
 * undo / redo : ajout d'un materiel utilise pour proc
 * @author MBO
 */
public class AddMaterialUseForProcUndoRedo extends CopexUndoRedo{
    // ATTRIBUTS
    /*  Proc */
    private ExperimentalProcedure proc;
    /* Mat */
    private Material material;
    /*justification*/
    private String justification;

    // CONSTRUCTEURS
    public AddMaterialUseForProcUndoRedo(EdPPanel edP, ControllerInterface controller, CopexTree tree, ExperimentalProcedure proc, Material material, String justification) {
        super(edP, controller, tree);
        this.proc = proc;
        this.material = material;
        this.justification = justification;
    }

    // OVERRIDE
    @Override
    public void redo() {
        super.redo();
        edP.addMaterialUseForProc(material, justification, MyConstants.REDO);
    }

    @Override
    public void undo() {
        super.undo();
        edP.removeMaterialUse(material, MyConstants.UNDO);
    }
    
    
    
}

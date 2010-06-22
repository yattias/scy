/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.undoRedo;

import eu.scy.client.tools.copex.common.ExperimentalProcedure;
import eu.scy.client.tools.copex.controller.ControllerInterface;
import eu.scy.client.tools.copex.edp.CopexTree;
import eu.scy.client.tools.copex.edp.EdPPanel;
import eu.scy.client.tools.copex.utilities.CopexReturn;
import eu.scy.client.tools.copex.utilities.MyConstants;


/**
 * renommer un protocole : gestion du undo redo
 * @author Marjolaine
 */
public class UpdateProcNameUndoRedo extends CopexUndoRedo {
    /* protocole */
    private ExperimentalProcedure proc;
    /* ancien nom */
    private String oldName;
    /* nouveau nom */
    private String newName;

  
    // CONSTRUCTEURS
     public UpdateProcNameUndoRedo(EdPPanel edP, ControllerInterface controller, CopexTree tree, ExperimentalProcedure proc, String oldName, String newName) {
        super(edP, controller, tree);
        this.proc = proc;
        this.oldName = oldName;
        this.newName = newName;
    }
    // METHODES
    /* undo */
    @Override
    public void undo(){
        super.undo();
        CopexReturn cr = this.controller.updateProcName(proc, oldName, MyConstants.UNDO);
        if (cr.isError()){
            edP.displayError(cr, edP.getBundleString("TITLE_DIALOG_ERROR"));
        }
    }
    
    
    /* redo */
    @Override
    public void redo(){
        super.redo();
        CopexReturn cr = this.controller.updateProcName(proc, newName, MyConstants.REDO);
        if (cr.isError()){
            edP.displayError(cr, edP.getBundleString("TITLE_DIALOG_ERROR"));
        }
    }
}

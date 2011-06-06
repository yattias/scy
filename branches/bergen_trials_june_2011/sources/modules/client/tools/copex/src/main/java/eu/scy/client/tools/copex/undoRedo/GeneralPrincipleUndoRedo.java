/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.undoRedo;

import eu.scy.client.tools.copex.common.GeneralPrinciple;
import eu.scy.client.tools.copex.common.ExperimentalProcedure;
import eu.scy.client.tools.copex.controller.ControllerInterface;
import eu.scy.client.tools.copex.edp.CopexTree;
import eu.scy.client.tools.copex.edp.EdPPanel;
import eu.scy.client.tools.copex.utilities.CopexReturn;
import java.util.ArrayList;

/**
 * general principle undo redo
 * @author Marjolaine
 */
public class GeneralPrincipleUndoRedo extends CopexUndoRedo {
    private ExperimentalProcedure proc;
    private GeneralPrinciple oldPrinciple;
    private GeneralPrinciple newPrinciple;

    public GeneralPrincipleUndoRedo(EdPPanel edP, ControllerInterface controller, CopexTree tree, ExperimentalProcedure proc, GeneralPrinciple oldPrinciple, GeneralPrinciple newPrinciple) {
        super(edP, controller, tree);
        this.proc = proc;
        this.oldPrinciple = oldPrinciple;
        this.newPrinciple = newPrinciple;
        if(oldPrinciple != null)
            this.oldPrinciple = (GeneralPrinciple)oldPrinciple.clone();
        if(newPrinciple != null)
            this.newPrinciple = (GeneralPrinciple)newPrinciple.clone();
    }

    /* undo */
    @Override
    public void undo(){
        super.undo();
        ArrayList v = new ArrayList();
        CopexReturn cr = this.controller.setGeneralPrinciple(proc, oldPrinciple, v);
        if (cr.isError()){
            edP.displayError(cr, edP.getBundleString("TITLE_DIALOG_ERROR"));
        }
        GeneralPrinciple principle = null;
        if(v.size() > 0)
            principle = (GeneralPrinciple)v.get(0);
        proc.setGeneralPrinciple(principle);
        edP.setGeneralPrinciple(principle);
    }

    /* redo */
    @Override
    public void redo(){
        super.redo();
        ArrayList v = new ArrayList();
        CopexReturn cr = this.controller.setGeneralPrinciple(proc, newPrinciple, v);
        if (cr.isError()){
            edP.displayError(cr, edP.getBundleString("TITLE_DIALOG_ERROR"));
        }
        GeneralPrinciple principle = null;
        if(v.size() > 0)
            principle = (GeneralPrinciple)v.get(0);
        proc.setGeneralPrinciple(principle);
        edP.setGeneralPrinciple(principle);
    }

}

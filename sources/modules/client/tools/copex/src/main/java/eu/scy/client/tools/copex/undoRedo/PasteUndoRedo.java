/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.undoRedo;

import eu.scy.client.tools.copex.common.ExperimentalProcedure;
import eu.scy.client.tools.copex.controller.ControllerInterface;
import eu.scy.client.tools.copex.dnd.SubTree;
import eu.scy.client.tools.copex.edp.CopexTree;
import eu.scy.client.tools.copex.edp.EdPPanel;
import eu.scy.client.tools.copex.edp.TaskSelected;
import eu.scy.client.tools.copex.utilities.CopexReturn;
import eu.scy.client.tools.copex.utilities.MyConstants;
import java.util.ArrayList;

/**
 * undo redo : paste
 * @author Marjolaine
 */
public class PasteUndoRedo extends CopexUndoRedo{
    /* sub tree */
    private SubTree subTree;
    /* selected task to paste  */
    private TaskSelected ts;
    /* list of selected task */
    private ArrayList<TaskSelected> listTask;

    
    public PasteUndoRedo(EdPPanel edP, ControllerInterface controller, CopexTree tree, SubTree subTree, TaskSelected ts, ArrayList<TaskSelected> listTask) {
        super(edP, controller, tree);
        this.subTree = subTree;
        this.ts = ts;
        this.listTask = listTask;
    }
    
    
    /* undo */
    @Override
    public void undo(){
        super.undo();
        ArrayList v = new ArrayList();
        CopexReturn cr = this.controller.suppr(listTask, v, true, MyConstants.UNDO);
        if (cr.isError()){
            edP.displayError(cr, edP.getBundleString("TITLE_DIALOG_ERROR"));
        }
        ExperimentalProcedure newProc = (ExperimentalProcedure)v.get(0);
        tree.suppr(listTask);
        edP.updateProc(newProc);
        edP.updateMenu();
    }
    
    /* redo */
    @Override
    public void redo(){
       super.redo();
       edP.setSubTree(subTree);
       ArrayList v = new ArrayList();
       CopexReturn cr = this.controller.paste(getProc(), subTree, ts, MyConstants.REDO, v);
       if (cr.isError()){
            edP.displayError(cr, edP.getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }
       
    }
}

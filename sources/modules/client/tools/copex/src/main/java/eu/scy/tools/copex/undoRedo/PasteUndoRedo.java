/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.undoRedo;

import eu.scy.tools.copex.common.LearnerProcedure;
import eu.scy.tools.copex.controller.ControllerInterface;
import eu.scy.tools.copex.dnd.SubTree;
import eu.scy.tools.copex.edp.CopexTree;
import eu.scy.tools.copex.edp.EdPPanel;
import eu.scy.tools.copex.edp.TaskSelected;
import eu.scy.tools.copex.utilities.CopexReturn;
import eu.scy.tools.copex.utilities.MyConstants;
import java.util.ArrayList;

/**
 * undo redo : coller
 * @author MBO
 */
public class PasteUndoRedo extends CopexUndoRedo{
    // ATTRIBUTS
    /* sous arbre */
    private SubTree subTree;
    /* tache selectionne pour coller */
    private TaskSelected ts;
    /* liste des ts */
    private ArrayList<TaskSelected> listTask;

    
    // CONSTRUCTEURS
    public PasteUndoRedo(EdPPanel edP, ControllerInterface controller, CopexTree tree, SubTree subTree, TaskSelected ts, ArrayList<TaskSelected> listTask) {
        super(edP, controller, tree);
        this.subTree = subTree;
        this.ts = ts;
        this.listTask = listTask;
    }
    
    
    //METHODES
    /* undo */
    @Override
    public void undo(){
        super.undo();
        ArrayList v = new ArrayList();
        CopexReturn cr = this.controller.suppr(listTask, v, true, MyConstants.UNDO);
        if (cr.isError()){
            edP.displayError(cr, edP.getBundleString("TITLE_DIALOG_ERROR"));
        }
        LearnerProcedure newProc = (LearnerProcedure)v.get(0);
        edP.updateProc(newProc);
        tree.suppr(listTask);
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

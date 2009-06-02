/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.undoRedo;


import eu.scy.tools.copex.common.LearnerProcedure;
import eu.scy.tools.copex.controller.ControllerInterface;
import eu.scy.tools.copex.edp.CopexTree;
import eu.scy.tools.copex.edp.EdPPanel;
import eu.scy.tools.copex.edp.TaskSelected;
import eu.scy.tools.copex.utilities.CopexReturn;
import eu.scy.tools.copex.utilities.MyConstants;
import java.util.ArrayList;

/**
 * undo redo : ajout d'une tache
 * @author MBO
 */
public class AddTaskUndoRedo extends CopexUndoRedo {
    //ATTRIBUTS
    /* tache */
    private TaskSelected task;
    /* positionnement dans l'arbre : tache sel lors de l'ajout */
    private TaskSelected ts ;

    // CONSTRUCTEURS
    public AddTaskUndoRedo(EdPPanel edP, ControllerInterface controller, CopexTree tree, TaskSelected task, TaskSelected ts) {
        super(edP, controller, tree);
        this.task = task;
        this.ts = ts;
    }
    
    
    //METHODES
    /* undo */
    @Override
    public void undo(){
        super.undo();
        ArrayList<TaskSelected> listTs = new ArrayList();
        listTs.add(task);
        ArrayList v = new ArrayList();
        CopexReturn cr = this.controller.suppr(listTs, v, true, MyConstants.UNDO);
        if (cr.isError()){
            edP.displayError(cr, edP.getBundleString("TITLE_DIALOG_ERROR"));
        }
        LearnerProcedure newProc = (LearnerProcedure)v.get(0);
        edP.updateProc(newProc);
        tree.suppr(listTs);
    }
    
    
    /* redo */
    @Override
    public void redo(){
        super.redo();
        ArrayList v = new ArrayList();
        CopexReturn cr = this.controller.addTask(task.getSelectedTask(), tree.getProc(), ts.getTaskBrother(),ts.getTaskParent(),   v, MyConstants.REDO, false);
        if (cr.isError()){
            edP.displayError(cr, edP.getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }
        LearnerProcedure newProc = (LearnerProcedure)v.get(0);
        edP.updateProc(newProc);
        tree.addTask(task.getSelectedTask(),ts);
        task.setSelectedNode(tree.getNode(task.getSelectedTask()));
    }
    
    
}

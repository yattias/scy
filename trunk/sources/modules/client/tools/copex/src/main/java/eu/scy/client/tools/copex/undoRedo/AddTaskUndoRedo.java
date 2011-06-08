/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.undoRedo;


import eu.scy.client.tools.copex.common.ExperimentalProcedure;
import eu.scy.client.tools.copex.controller.ControllerInterface;
import eu.scy.client.tools.copex.edp.CopexTree;
import eu.scy.client.tools.copex.edp.EdPPanel;
import eu.scy.client.tools.copex.edp.TaskSelected;
import eu.scy.client.tools.copex.utilities.CopexReturn;
import eu.scy.client.tools.copex.utilities.MyConstants;
import java.util.ArrayList;

/**
 * undo redo : add a task
 * @author Marjolaine
 */
public class AddTaskUndoRedo extends CopexUndoRedo {
    /* task */
    private TaskSelected task;
    /* position in the tree: selected task while adding */
    private TaskSelected ts ;

    public AddTaskUndoRedo(EdPPanel edP, ControllerInterface controller, CopexTree tree, TaskSelected task, TaskSelected ts) {
        super(edP, controller, tree);
        this.task = task;
        this.ts = ts;
    }
    
    
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
        ExperimentalProcedure newProc = (ExperimentalProcedure)v.get(0);
        tree.suppr(listTs);
        edP.updateProc(newProc);
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
        ExperimentalProcedure newProc = (ExperimentalProcedure)v.get(0);
        tree.addTask(task.getSelectedTask(),ts);
        task.setSelectedNode(tree.getNode(task.getSelectedTask()));
        edP.updateProc(newProc);
    }
    
    
}

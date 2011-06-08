/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.undoRedo;

import eu.scy.client.tools.copex.common.CopexTask;
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
 * undo redo : cut
 * @author Marjolaine
 */
public class CutUndoRedo extends CopexUndoRedo {
    /* list of tasks deleted  */
    private ArrayList<TaskSelected> listTask;
    /* for each task, position in the tree */
    private ArrayList<TaskSelected> listTs;
    /* sub tree */
    private SubTree subTree;

    
    public CutUndoRedo(EdPPanel edP, ControllerInterface controller, CopexTree tree, ArrayList<TaskSelected> listTask, ArrayList<TaskSelected> listTs, SubTree subTree) {
        super(edP, controller, tree);
        this.listTask = listTask;
        this.listTs = listTs;
        this.subTree = subTree;
    }
    
    
    /* undo */
    @Override
    public void undo(){
        super.undo();
        int nbT = listTask.size();
        for (int i=0; i<nbT; i++){
            TaskSelected task = listTask.get(i);
            TaskSelected ts = listTs.get(i);
            ArrayList v = new ArrayList();
            CopexTask taskBrother = null;
            CopexTask taskParent = null;
            if (task.attachLikeBrother() )
                taskBrother = ts.getSelectedTask();
            else 
                taskParent  = ts.getSelectedTask();
            CopexReturn cr = this.controller.addTask(task.getSelectedTask(), tree.getProc(), taskBrother, taskParent,   v, MyConstants.UNDO, true);
            if (cr.isError()){
                edP.displayError(cr, edP.getBundleString("TITLE_DIALOG_ERROR"));
                return;
            }
            ExperimentalProcedure newProc = (ExperimentalProcedure)v.get(0);
            tree.addTask(task.getSelectedTask(),tree.getTaskSelected(ts.getSelectedTask()), task.attachLikeBrother());
            task.setSelectedNode(tree.getNode(task.getSelectedTask()));
            edP.setSubTree(null);
            edP.updateProc(newProc);
            edP.updateMenu();
        }
    }
    
    
    /* redo */
    @Override
    public void redo(){
        super.redo();
        ArrayList v = new ArrayList();
        CopexReturn cr = this.controller.cut(listTask, subTree, v, MyConstants.REDO);
        if (cr.isError()){
            edP.displayError(cr, edP.getBundleString("TITLE_DIALOG_ERROR"));
        }
        ExperimentalProcedure newProc = (ExperimentalProcedure)v.get(0);
        tree.suppr(listTask);
        edP.setSubTree(subTree);
        edP.updateProc(newProc);
        edP.updateMenu();
    }
}

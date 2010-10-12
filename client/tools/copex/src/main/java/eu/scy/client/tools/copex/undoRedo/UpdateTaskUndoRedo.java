/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.undoRedo;


import eu.scy.client.tools.copex.common.CopexTask;
import eu.scy.client.tools.copex.common.ExperimentalProcedure;
import eu.scy.client.tools.copex.controller.ControllerInterface;
import eu.scy.client.tools.copex.edp.CopexTree;
import eu.scy.client.tools.copex.edp.CopexNode;
import eu.scy.client.tools.copex.edp.EdPPanel;
import eu.scy.client.tools.copex.utilities.CopexReturn;
import eu.scy.client.tools.copex.utilities.MyConstants;
import java.util.ArrayList;

/**
 * undo redo de la modification d'une tache
 * @author Marjolaine
 */
public class UpdateTaskUndoRedo extends CopexUndoRedo{
    /* ancienne tache */
    private CopexTask oldTask;
    /*noeud */
    private CopexNode node;
    /* nouvelle tache */
    private CopexTask newTask;

    public UpdateTaskUndoRedo(EdPPanel edP, ControllerInterface controller, CopexTree tree, CopexTask oldTask, CopexNode node, CopexTask newTask) {
        super(edP, controller, tree);
        this.oldTask = (CopexTask)oldTask.clone();
        this.node = node;
        this.newTask = (CopexTask)newTask.clone();
    }
    
    /* undo */
    @Override
    public void undo(){
        super.undo();
        ArrayList v = new ArrayList();
        CopexReturn cr = this.controller.updateTask(oldTask, getProc(), newTask, MyConstants.UNDO,  v);
        if (cr.isError()){
            edP.displayError(cr, edP.getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }
        ExperimentalProcedure newproc = (ExperimentalProcedure)v.get(0);
        edP.updateProc(newproc);
        tree.updateTask(oldTask, node);
    }
    
    
    /* redo */
    @Override
    public void redo(){
        super.redo();
        ArrayList v = new ArrayList();
        CopexReturn cr = this.controller.updateTask(newTask, getProc(), oldTask, MyConstants.REDO,  v);
        if (cr.isError()){
            edP.displayError(cr, edP.getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }
        ExperimentalProcedure newproc = (ExperimentalProcedure)v.get(0);
        edP.updateProc(newproc);
        tree.updateTask(newTask, node);
    }
    
    
}

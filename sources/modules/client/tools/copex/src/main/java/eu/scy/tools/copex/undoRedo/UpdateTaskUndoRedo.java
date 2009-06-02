/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.undoRedo;


import eu.scy.tools.copex.common.CopexTask;
import eu.scy.tools.copex.common.LearnerProcedure;
import eu.scy.tools.copex.controller.ControllerInterface;
import eu.scy.tools.copex.edp.CopexTree;
import eu.scy.tools.copex.edp.CopexTreeNode;
import eu.scy.tools.copex.edp.EdPPanel;
import eu.scy.tools.copex.utilities.CopexReturn;
import eu.scy.tools.copex.utilities.MyConstants;
import java.util.ArrayList;

/**
 * undo redo de la modification d'une tache
 * @author Admin2
 */
public class UpdateTaskUndoRedo extends CopexUndoRedo{
    // ATTRIBUTS
    /* ancienne tache */
    private CopexTask oldTask;
    /*noeud */
    private CopexTreeNode node;
    /* nouvelle tache */
    private CopexTask newTask;

    // CONSTRUCTEURS
    public UpdateTaskUndoRedo(EdPPanel edP, ControllerInterface controller, CopexTree tree, CopexTask oldTask, CopexTreeNode node, CopexTask newTask) {
        super(edP, controller, tree);
        this.oldTask = (CopexTask)oldTask.clone();
        this.node = node;
        this.newTask = (CopexTask)newTask.clone();
    }
    
    // METHODES
    /* undo */
    @Override
    public void undo(){
        super.undo();
        ArrayList v = new ArrayList();
        CopexReturn cr = this.controller.updateTask(oldTask, getProc(), newTask, MyConstants.UNDO,  v);
        if (cr.isError()){
            edP.displayError(cr, edP.getBundleString("TITLE_DIALOG_ERROR"));
        }
        LearnerProcedure newproc = (LearnerProcedure)v.get(0);
        edP.updateProc(newproc);
        System.out.println("MODIFICATION DE LA TACHE : "+newTask.getDescription() +" => "+oldTask.getDescription());
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
        }
        LearnerProcedure newproc = (LearnerProcedure)v.get(0);
        edP.updateProc(newproc);
        System.out.println("MODIFICATION DE LA TACHE : "+oldTask.getDescription() +" => "+newTask.getDescription());
        tree.updateTask(newTask, node);
    }
    
    
}

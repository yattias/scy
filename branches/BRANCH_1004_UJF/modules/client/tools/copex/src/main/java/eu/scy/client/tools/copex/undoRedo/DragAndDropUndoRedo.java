/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.undoRedo;

import eu.scy.client.tools.copex.controller.ControllerInterface;
import eu.scy.client.tools.copex.dnd.SubTree;
import eu.scy.client.tools.copex.edp.CopexTree;
import eu.scy.client.tools.copex.edp.EdPPanel;
import eu.scy.client.tools.copex.edp.TaskSelected;
import eu.scy.client.tools.copex.utilities.CopexReturn;
import eu.scy.client.tools.copex.utilities.MyConstants;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * undo redo : drag and drop 
 * @author MBO
 */
public class DragAndDropUndoRedo extends CopexUndoRedo {
    //ATTRIBUTS
    /*sous arbre */
    private SubTree subTree;
    /*tache insertion */
    private TaskSelected ts;
    /*ancinene tache insertion */
    private TaskSelected oldTs;

     // CONSTRUCTEURS
    public DragAndDropUndoRedo(EdPPanel edP, ControllerInterface controller, CopexTree tree, SubTree subTree, TaskSelected ts, TaskSelected oldTs) {
        super(edP, controller, tree);
        this.subTree = subTree;
        this.ts = ts;
        this.oldTs = oldTs;
    }
    
   
    
    //METHODES
    /* undo */
    @Override
    public void undo(){
        super.undo();
        CopexReturn cr = this.controller.move(oldTs, subTree, MyConstants.UNDO);
         if (cr.isError()){
            edP.displayError(cr, edP.getBundleString("TITLE_DIALOG_ERROR"));
            return;
         }
         tree.addTasks(subTree.getListTask(), subTree, oldTs);
         tree.removeTask(subTree, false);
         cr = this.controller.finalizeDragAndDrop(getProc());
         if (cr.isError()){
            edP.displayError(cr, edP.getBundleString("TITLE_DIALOG_ERROR"));
            return;
         }
         
    }
    
    /* redo*/
     @Override
    public void redo(){
         super.redo();
        CopexReturn cr = this.controller.move(ts, subTree, MyConstants.REDO);
         if (cr.isError()){
            edP.displayError(cr, edP.getBundleString("TITLE_DIALOG_ERROR"));
            return;
         }
         tree.addTasks(subTree.getListTask(), subTree, ts);
         tree.removeTask(subTree, false);
         cr = this.controller.finalizeDragAndDrop(getProc());
         if (cr.isError()){
            edP.displayError(cr, edP.getBundleString("TITLE_DIALOG_ERROR"));
            return;
         }
     }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.undoRedo;


import eu.scy.client.tools.copex.common.ExperimentalProcedure;
import eu.scy.client.tools.copex.controller.ControllerInterface;
import eu.scy.client.tools.copex.edp.CopexTree;
import eu.scy.client.tools.copex.edp.EdPPanel;
import javax.swing.undo.*; 

/**
 * undo redo in copex for an experimental procedure
 * @author Marjolaine
 */
public class CopexUndoRedo extends AbstractUndoableEdit{
    /* owner*/
    protected EdPPanel edP;
    /* controller */
    protected ControllerInterface controller;
    /* tree */
    protected CopexTree tree;
    
    public CopexUndoRedo(EdPPanel edP, ControllerInterface controller, CopexTree tree) {
        this.edP = edP;
        this.controller = controller;
        this.tree = tree;
    }
    protected ExperimentalProcedure getProc(){
        return this.tree.getProc();
    }


}

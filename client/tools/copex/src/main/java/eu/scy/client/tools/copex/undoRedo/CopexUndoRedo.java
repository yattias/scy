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
 * classe mere permettant de gerer le undo /redo dans l'edp.
 * Il n'y a pas de undo/redo sur les actions ajouter / supprimer un protocole
 * car ces actions ne sont pas liees a un arbre en particulier mais a l'editeur en general
 * @author Marjolaine
 */
public class CopexUndoRedo extends AbstractUndoableEdit{
    /* editeur de proc */
    protected EdPPanel edP;
    /* controller */
    protected ControllerInterface controller;
    /* arbre */
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

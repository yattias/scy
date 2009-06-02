/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.undoRedo;


import eu.scy.tools.copex.common.LearnerProcedure;
import eu.scy.tools.copex.controller.ControllerInterface;
import eu.scy.tools.copex.edp.CopexTree;
import eu.scy.tools.copex.edp.EdPPanel;
import javax.swing.undo.*; 

/**
 * classe mere permettant de gérer le undo /redo dans l'edp.
 * Il n'y a pas de undo/redo sur les actions ajouter / supprimer un protocole
 * car ces actions ne sont pas liées à un arbre en particulier mais à l'éditeur en général
 * @author MBO
 */
public class CopexUndoRedo extends AbstractUndoableEdit{
    // ATTRIBUTS
    /* editeur de proc */
    protected EdPPanel edP;
    /* controller */
    protected ControllerInterface controller;
    /* arbre */
    protected CopexTree tree;
    
    // CONSTRUCTEURS
    public CopexUndoRedo(EdPPanel edP, ControllerInterface controller, CopexTree tree) {
        this.edP = edP;
        this.controller = controller;
        this.tree = tree;
    }
    
    //METHODES
    protected LearnerProcedure getProc(){
        return this.tree.getProc();
    }


}

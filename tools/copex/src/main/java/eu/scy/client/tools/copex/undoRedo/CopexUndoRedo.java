/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.undoRedo;


import eu.scy.client.tools.copex.common.LearnerProcedure;
import eu.scy.client.tools.copex.controller.ControllerInterface;
import eu.scy.client.tools.copex.edp.CopexTree;
import eu.scy.client.tools.copex.edp.EdPPanel;
import javax.swing.undo.*; 

/**
 * classe mere permettant de gerer le undo /redo dans l'edp.
 * Il n'y a pas de undo/redo sur les actions ajouter / supprimer un protocole
 * car ces actions ne sont pas liees a un arbre en particulier mais a l'editeur en general
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

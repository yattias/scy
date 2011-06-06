/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.undoRedo;

import eu.scy.client.tools.copex.edp.CopexTree;
import javax.swing.undo.UndoManager;

/**
 * manager permettant de gerer la liste des undo redo
 * @author MBO
 */
public class CopexUndoManager extends UndoManager{
    // CONSTANTES
    public final static int MAX_UNDO_REDO = 10;
    // ATTRIBUTS
    private CopexTree tree;

    //CONSTRUCTEURS
    public CopexUndoManager(CopexTree tree) {
        super();
        this.tree = tree;
        setLimit(MAX_UNDO_REDO);
    }

    
    
}

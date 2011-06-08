/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.undoRedo;

import eu.scy.client.tools.copex.edp.CopexTree;
import javax.swing.undo.UndoManager;

/**
 * management of the undo redo
 * @author Marjolaine
 */
public class CopexUndoManager extends UndoManager{
    public final static int MAX_UNDO_REDO = 10;

    private CopexTree tree;

    public CopexUndoManager(CopexTree tree) {
        super();
        this.tree = tree;
        setLimit(MAX_UNDO_REDO);
    }

    
    
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.dataProcessTool.undoRedo;

import eu.scy.tools.dataProcessTool.dataTool.DataTable;
import javax.swing.undo.UndoManager;

/**
 * manager permettant de gerer les undo/redo
 * @author Marjolaine
 */
public class DataUndoManager extends UndoManager {
    // CONSTANTES
    public final static int MAX_UNDO_REDO = 10;

    private DataTable table;

    public DataUndoManager(DataTable table) {
        super();
        this.table = table;
        setLimit(MAX_UNDO_REDO);
    }

}

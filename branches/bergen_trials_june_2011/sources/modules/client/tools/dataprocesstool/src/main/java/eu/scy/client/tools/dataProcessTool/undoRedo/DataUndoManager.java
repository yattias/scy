/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.dataProcessTool.undoRedo;

import eu.scy.client.tools.dataProcessTool.dataTool.DataTable;
import javax.swing.undo.UndoManager;

/**
 * management undo/redo
 * @author Marjolaine
 */
public class DataUndoManager extends UndoManager {
    public final static int MAX_UNDO_REDO = 10;

    private DataTable table;

    public DataUndoManager(DataTable table) {
        super();
        this.table = table;
        setLimit(MAX_UNDO_REDO);
    }

}

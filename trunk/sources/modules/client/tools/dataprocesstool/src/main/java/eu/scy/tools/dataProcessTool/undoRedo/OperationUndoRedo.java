/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.dataProcessTool.undoRedo;

import eu.scy.tools.dataProcessTool.common.Data;
import eu.scy.tools.dataProcessTool.common.DataOperation;
import eu.scy.tools.dataProcessTool.common.Dataset;
import eu.scy.tools.dataProcessTool.controller.ControllerInterface;
import eu.scy.tools.dataProcessTool.dataTool.DataProcessToolPanel;
import eu.scy.tools.dataProcessTool.dataTool.DataTable;
import eu.scy.tools.dataProcessTool.utilities.CopexReturn;
import java.util.ArrayList;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

/**
 * create an operation undo/redo => delete operation
 * @author Marjolaine
 */
public class OperationUndoRedo extends DataUndoRedo {
    private DataOperation operation;

    public OperationUndoRedo(DataTable table, DataProcessToolPanel dataToolPanel, ControllerInterface controller, DataOperation operation) {
        super(table, dataToolPanel, controller);
        this.operation = operation;
    }

    @Override
    public void redo() throws CannotRedoException {
        super.redo();
        ArrayList v = new ArrayList();
        CopexReturn cr = this.controller.createOperation(getDataset(), operation.getTypeOperation().getType(), operation.isOnCol(), operation.getListNo(), v);
        if (cr.isError()){
            dataToolPanel.displayError(cr, dataToolPanel.getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }
        Dataset nds = (Dataset)v.get(0);
        DataOperation op= (DataOperation)v.get(1);
        this.operation = op;
        dataToolPanel.updateDataset(nds) ;
    }

    @Override
    public void undo() throws CannotUndoException {
        super.undo();
        ArrayList<DataOperation> listOp = new ArrayList();
        listOp.add(operation);
        ArrayList<Integer>[] tabSel  = new ArrayList[2];
        tabSel[0] = new ArrayList();
        tabSel[1] = new ArrayList();
        CopexReturn cr = this.controller.deleteData(false, getDataset(), new ArrayList(), listOp, tabSel);
        if(cr.isError()){
            dataToolPanel.displayError(cr, dataToolPanel.getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }
    }


}

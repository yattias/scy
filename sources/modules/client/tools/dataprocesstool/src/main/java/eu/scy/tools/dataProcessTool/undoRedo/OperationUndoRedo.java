/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.dataProcessTool.undoRedo;

import eu.scy.tools.dataProcessTool.common.DataOperation;
import eu.scy.tools.dataProcessTool.common.Dataset;
import eu.scy.tools.dataProcessTool.controller.ControllerInterface;
import eu.scy.tools.dataProcessTool.dataTool.DataTable;
import eu.scy.tools.dataProcessTool.dataTool.FitexToolPanel;
import eu.scy.tools.dataProcessTool.utilities.CopexReturn;
import java.util.ArrayList;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import org.jdom.Element;

/**
 * create an operation undo/redo => delete operation
 * @author Marjolaine
 */
public class OperationUndoRedo extends DataUndoRedo {
    private DataOperation operation;

    public OperationUndoRedo(DataTable table, FitexToolPanel dataToolPanel, ControllerInterface controller, DataOperation operation) {
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
        this.dataToolPanel.logRedo(this);
        cr = this.controller.exportHTML();
        if (cr.isError()){
            dataToolPanel.displayError(cr, dataToolPanel.getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }
    }

    @Override
    public void undo() throws CannotUndoException {
        super.undo();
        ArrayList<DataOperation> listOp = new ArrayList();
        listOp.add(operation);
        ArrayList v = new ArrayList();
        CopexReturn cr = this.controller.deleteData(false, getDataset(), new ArrayList(), new ArrayList(), new ArrayList(), listOp,  v);
        if(cr.isError()){
            dataToolPanel.displayError(cr, dataToolPanel.getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }
        if(v.size()> 0){
            Dataset ds = (Dataset)v.get(0);
            dataToolPanel.updateDataset(ds);
        }
        this.dataToolPanel.logUndo(this);
        cr = this.controller.exportHTML();
        if (cr.isError()){
            dataToolPanel.displayError(cr, dataToolPanel.getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }
    }

    @Override
    public Element toXMLLog(){
        Element element = new Element("undoRedoOperation");
        element.addContent(operation.toXMLLog());
        return element;
    }

}

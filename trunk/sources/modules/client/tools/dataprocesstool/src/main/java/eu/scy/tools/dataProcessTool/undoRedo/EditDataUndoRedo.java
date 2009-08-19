/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.dataProcessTool.undoRedo;

import eu.scy.tools.dataProcessTool.common.Dataset;
import eu.scy.tools.dataProcessTool.controller.ControllerInterface;
import eu.scy.tools.dataProcessTool.dataTool.DataProcessToolPanel;
import eu.scy.tools.dataProcessTool.dataTool.DataTable;
import eu.scy.tools.dataProcessTool.utilities.CopexReturn;
import java.util.ArrayList;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;


/**
 * edit data : undo / redo
 * @author Marjolaine
 */
public class EditDataUndoRedo extends DataUndoRedo{
    private Double oldValue;
    private Double newValue;
    private int idR;
    private int idC;

    public EditDataUndoRedo(DataTable table, DataProcessToolPanel dataToolPanel, ControllerInterface controller, Double oldValue, Double newValue, int idR, int idC) {
        super(table, dataToolPanel, controller);
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.idR = idR;
        this.idC = idC;
    }



    @Override
    public void redo() throws CannotRedoException {
        super.redo();
        ArrayList v = new ArrayList();
        CopexReturn cr = this.controller.updateData(getDataset(), idR, idC, newValue, v);
        if (cr.isError()){
            dataToolPanel.displayError(cr, dataToolPanel.getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }
        Dataset nds = (Dataset)v.get(0);
        this.dataToolPanel.updateDataset(nds);
    }

    @Override
    public void undo() throws CannotUndoException {
        super.undo();
        ArrayList v = new ArrayList();
        CopexReturn cr = this.controller.updateData(getDataset(), idR, idC, oldValue, v);
        if (cr.isError()){
            dataToolPanel.displayError(cr, dataToolPanel.getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }
        Dataset nds = (Dataset)v.get(0);
        this.dataToolPanel.updateDataset(nds);
    }



}

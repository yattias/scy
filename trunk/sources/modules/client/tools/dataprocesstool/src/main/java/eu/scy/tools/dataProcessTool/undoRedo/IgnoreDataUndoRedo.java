/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.dataProcessTool.undoRedo;

import eu.scy.tools.dataProcessTool.common.Data;
import eu.scy.tools.dataProcessTool.common.Dataset;
import eu.scy.tools.dataProcessTool.controller.ControllerInterface;
import eu.scy.tools.dataProcessTool.dataTool.DataProcessToolPanel;
import eu.scy.tools.dataProcessTool.dataTool.DataTable;
import eu.scy.tools.dataProcessTool.utilities.CopexReturn;
import java.util.ArrayList;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

/**
 * ignore or not some data undo redo
 * @author Marjolaine
 */
public class IgnoreDataUndoRedo extends DataUndoRedo{
    private boolean isIgnored;
    private ArrayList<Data> listData;

    public IgnoreDataUndoRedo(DataTable table, DataProcessToolPanel dataToolPanel, ControllerInterface controller, boolean isIgnored, ArrayList<Data> listData) {
        super(table, dataToolPanel, controller);
        this.isIgnored = isIgnored;
        this.listData = listData;
    }

    @Override
    public void redo() throws CannotRedoException {
        super.redo();
        ArrayList v = new ArrayList();
        CopexReturn cr = this.controller.setDataIgnored(getDataset(), !isIgnored, listData, v);
        if (cr.isError()){
            dataToolPanel.displayError(cr, dataToolPanel.getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }
        Dataset nds = (Dataset)v.get(0);
        dataToolPanel.updateDataset(nds);
        isIgnored = !isIgnored ;
    }

    @Override
    public void undo() throws CannotUndoException {
        super.undo();
        ArrayList v = new ArrayList();
        CopexReturn cr = this.controller.setDataIgnored(getDataset(), !isIgnored, listData, v);
        if (cr.isError()){
            dataToolPanel.displayError(cr, dataToolPanel.getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }
        Dataset nds = (Dataset)v.get(0);
        dataToolPanel.updateDataset(nds);
        isIgnored = !isIgnored ;
    }


}

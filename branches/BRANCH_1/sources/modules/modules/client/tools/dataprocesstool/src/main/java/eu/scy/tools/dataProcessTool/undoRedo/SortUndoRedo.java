/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.dataProcessTool.undoRedo;

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
 * sort undo redo
 * @author Marjolaine
 */
public class SortUndoRedo extends DataUndoRedo{

    private Dataset oldDataset;
    private Dataset newDataset;

    public SortUndoRedo(DataTable table, FitexToolPanel dataToolPanel, ControllerInterface controller, Dataset oldDataset, Dataset newDataset) {
        super(table, dataToolPanel, controller);
        this.oldDataset = oldDataset;
        this.newDataset = newDataset;
    }

    @Override
    public void redo() throws CannotRedoException {
        super.redo();
        ArrayList v = new ArrayList();
        CopexReturn cr = this.controller.updateDataset(newDataset, v);
        if (cr.isError()){
             dataToolPanel.displayError(cr,  dataToolPanel.getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }
        Dataset nds = (Dataset)v.get(0);
        dataToolPanel.updateDataset(nds);
        this.dataToolPanel.logRedo(this);
    }

    @Override
    public void undo() throws CannotUndoException {
        super.undo();
        ArrayList v = new ArrayList();
        CopexReturn cr = this.controller.updateDataset(oldDataset, v);
        if (cr.isError()){
             dataToolPanel.displayError(cr,  dataToolPanel.getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }
        Dataset nds = (Dataset)v.get(0);
        dataToolPanel.updateDataset(nds);
        this.dataToolPanel.logUndo(this);
    }

    @Override
    public Element toXMLLog(){
        Element element = new Element("undoRedoSort");
        return element;
    }
}

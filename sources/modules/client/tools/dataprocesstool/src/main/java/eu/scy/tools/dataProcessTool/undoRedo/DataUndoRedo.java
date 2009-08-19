/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.dataProcessTool.undoRedo;

import eu.scy.tools.dataProcessTool.common.Dataset;
import eu.scy.tools.dataProcessTool.controller.ControllerInterface;
import eu.scy.tools.dataProcessTool.dataTool.DataProcessToolPanel;
import eu.scy.tools.dataProcessTool.dataTool.DataTable;
import javax.swing.undo.AbstractUndoableEdit;

/**
 * action de undo/redo sur une table
 * @author Marjolaine
 */
public class DataUndoRedo extends AbstractUndoableEdit{
    /* table sur laquelle s'effectur le undo/redo */
    protected DataTable table;
    /* data tool panel */
    protected DataProcessToolPanel dataToolPanel;
    /* controller */
    protected ControllerInterface controller;

    public DataUndoRedo(DataTable table, DataProcessToolPanel dataToolPanel, ControllerInterface controller) {
        this.table = table;
        this.dataToolPanel = dataToolPanel;
        this.controller = controller;
    }

    protected Dataset getDataset(){
        return this.table.getDataset() ;
    }
}

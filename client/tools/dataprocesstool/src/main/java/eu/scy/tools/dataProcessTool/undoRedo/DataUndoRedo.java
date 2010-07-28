/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.dataProcessTool.undoRedo;

import eu.scy.tools.dataProcessTool.common.Dataset;
import eu.scy.tools.dataProcessTool.controller.ControllerInterface;
import eu.scy.tools.dataProcessTool.dataTool.DataTable;
import eu.scy.tools.dataProcessTool.dataTool.FitexToolPanel;
import javax.swing.undo.AbstractUndoableEdit;
import org.jdom.Element;

/**
 * action de undo/redo sur une table
 * @author Marjolaine
 */
public abstract class DataUndoRedo extends AbstractUndoableEdit{
    /* table sur laquelle s'effectur le undo/redo */
    protected DataTable table;
    /* data tool panel */
    protected FitexToolPanel dataToolPanel;
    /* controller */
    protected ControllerInterface controller;

    public DataUndoRedo(DataTable table, FitexToolPanel dataToolPanel, ControllerInterface controller) {
        this.table = table;
        this.dataToolPanel = dataToolPanel;
        this.controller = controller;
    }

    protected Dataset getDataset(){
        return this.table.getDataset() ;
    }

    public Element toXMLLog(){
        return null;
    }
}

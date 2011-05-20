/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.dataProcessTool.undoRedo;

import eu.scy.client.tools.dataProcessTool.common.Dataset;
import eu.scy.client.tools.dataProcessTool.controller.ControllerInterface;
import eu.scy.client.tools.dataProcessTool.dataTool.DataTable;
import eu.scy.client.tools.dataProcessTool.dataTool.FitexToolPanel;
import javax.swing.undo.AbstractUndoableEdit;
import org.jdom.Element;

/**
 * undo/redo action on the table
 * @author Marjolaine
 */
public abstract class DataUndoRedo extends AbstractUndoableEdit{
    /* table  */
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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.dataProcessTool.undoRedo;

import eu.scy.client.tools.dataProcessTool.common.Data;
import eu.scy.client.tools.dataProcessTool.common.Dataset;
import eu.scy.client.tools.dataProcessTool.controller.ControllerInterface;
import eu.scy.client.tools.dataProcessTool.dataTool.DataTable;
import eu.scy.client.tools.dataProcessTool.dataTool.FitexToolPanel;
import eu.scy.client.tools.dataProcessTool.utilities.CopexReturn;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import org.jdom.Element;

/**
 * ignore or not some data undo redo
 * @author Marjolaine
 */
public class IgnoreDataUndoRedo extends DataUndoRedo{
    private boolean isIgnored;
    private ArrayList<Data> listData;

    public IgnoreDataUndoRedo(DataTable table, FitexToolPanel dataToolPanel, ControllerInterface controller, boolean isIgnored, ArrayList<Data> listData) {
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
        ArrayList v = new ArrayList();
        CopexReturn cr = this.controller.setDataIgnored(getDataset(), !isIgnored, listData, v);
        if (cr.isError()){
            dataToolPanel.displayError(cr, dataToolPanel.getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }
        Dataset nds = (Dataset)v.get(0);
        dataToolPanel.updateDataset(nds);
        isIgnored = !isIgnored ;
        this.dataToolPanel.logUndo(this);
        cr = this.controller.exportHTML();
        if (cr.isError()){
            dataToolPanel.displayError(cr, dataToolPanel.getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }
    }


    @Override
    public Element toXMLLog(){
        Element element = new Element("undoRedoIgnore");
        element.addContent(new Element("is_ignored").setText(isIgnored? "true":"false"));
        for(Iterator<Data> d= listData.iterator();d.hasNext();){
            element.addContent(d.next().toXMLLog());
        }
        return element;
    }
}

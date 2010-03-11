/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.dataProcessTool.undoRedo;

import eu.scy.tools.dataProcessTool.common.Data;
import eu.scy.tools.dataProcessTool.common.DataHeader;
import eu.scy.tools.dataProcessTool.common.Dataset;
import eu.scy.tools.dataProcessTool.controller.ControllerInterface;
import eu.scy.tools.dataProcessTool.dataTool.FitexToolPanel;
import eu.scy.tools.dataProcessTool.dataTool.DataTable;
import eu.scy.tools.dataProcessTool.utilities.CopexReturn;
import eu.scy.tools.dataProcessTool.utilities.MyConstants;
import java.util.ArrayList;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import org.jdom.Element;

/**
 * paste : undo/redo
 * @author Marjolaine
 */
public class PasteUndoRedo extends DataUndoRedo {
    private Dataset subData;
    private int[] selCell;

    private ArrayList<Data[]> listData;
    private ArrayList<DataHeader[]> listHeader;
    private ArrayList<Integer>[] listRowAndCol;

    public PasteUndoRedo(DataTable table, FitexToolPanel dataToolPanel, ControllerInterface controller, Dataset subData, int[] selCell, ArrayList<Data[]> listData, ArrayList<DataHeader[]> listHeader, ArrayList<Integer>[] listRowAndCol) {
        super(table, dataToolPanel, controller);
        this.subData = subData;
        this.selCell = selCell;
        this.listData = listData;
        this.listHeader = listHeader;
        this.listRowAndCol = listRowAndCol;
    }

    

    @Override
    public void redo() throws CannotRedoException {
        super.redo();
        ArrayList v = new ArrayList();
        CopexReturn cr = this.controller.paste(getDataset().getDbKey(),subData, selCell, v);
        if(cr.isError()){
            dataToolPanel.displayError(cr, dataToolPanel.getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }
        Dataset nds = (Dataset)v.get(0);
        dataToolPanel.updateDataset(nds);
        this.dataToolPanel.logRedo(this);
    }

    @Override
    public void undo() throws CannotUndoException {
        super.undo();
        //Dataset nds = getDataset() ;
        // suppression header
        int nbH = this.listHeader.size();
        for (int i=0; i<nbH; i++){
            ArrayList v = new ArrayList();
            DataHeader oldH = this.listHeader.get(i)[0];
            DataHeader newH = this.listHeader.get(i)[1];
            String title = oldH == null ? "" : oldH.getValue() ;
            String unit = oldH == null ? "" : oldH.getUnit() ;
            String description = oldH == null ? "" : oldH.getDescription();
            String type = oldH == null ?MyConstants.DEFAULT_TYPE_COLUMN : oldH.getType();
            CopexReturn cr = this.controller.updateDataHeader(getDataset(),true, newH.getNoCol(), title,unit,description, type,  v );
            if(cr.isError()){
                dataToolPanel.displayError(cr, dataToolPanel.getBundleString("TITLE_DIALOG_ERROR"));
                return;
            }
            //nds = (Dataset)v.get(0);
        }
        // suppression donnees
        int nbD = this.listData.size();
        for (int i=0; i<nbD; i++){
            ArrayList v = new ArrayList();
            Data oldD = this.listData.get(i)[0];
            Data newD = this.listData.get(i)[1];
            String value = oldD == null ? null : oldD.getValue() ;
            CopexReturn cr = this.controller.updateData(getDataset(),newD.getNoRow(), newD.getNoCol(), value, v );
            if(cr.isError()){
                dataToolPanel.displayError(cr, dataToolPanel.getBundleString("TITLE_DIALOG_ERROR"));
                return;
            }
            //nds = (Dataset)v.get(0);
        }
        // suppression des eventuelles lignes/colonnes
        ArrayList v = new ArrayList();
        CopexReturn cr = this.controller.deleteData(true, getDataset(), new ArrayList(), listRowAndCol[0], listRowAndCol[1], new ArrayList(),  v);
        if(cr.isError()){
            dataToolPanel.displayError(cr, dataToolPanel.getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }
        if(v.size()> 0){
            Dataset ds = (Dataset)v.get(0);
            dataToolPanel.updateDataset(ds);
        }
        //dataToolPanel.updateDataset(nds);
        this.dataToolPanel.logUndo(this);
    }

    @Override
    public Element toXMLLog(){
        Element element = new Element("undoRedoPaste");
        element.addContent(subData.toXMLLog());
        Element e = new Element("sel_cell");
        e.addContent(new Element("id_row").setText(Integer.toString(selCell[0])));
        e.addContent(new Element("id_col").setText(Integer.toString(selCell[1])));
        element.addContent(e);
        return element;
    }


}

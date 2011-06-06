/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.dataProcessTool.undoRedo;

import eu.scy.client.tools.dataProcessTool.common.CopyDataset;
import eu.scy.client.tools.dataProcessTool.common.Data;
import eu.scy.client.tools.dataProcessTool.common.DataHeader;
import eu.scy.client.tools.dataProcessTool.common.Dataset;
import eu.scy.client.tools.dataProcessTool.controller.ControllerInterface;
import eu.scy.client.tools.dataProcessTool.dataTool.FitexToolPanel;
import eu.scy.client.tools.dataProcessTool.dataTool.DataTable;
import eu.scy.client.tools.dataProcessTool.utilities.CopexReturn;
import eu.scy.client.tools.dataProcessTool.utilities.DataConstants;
import java.util.ArrayList;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import org.jdom.Element;

/**
 * paste : undo/redo
 * @author Marjolaine
 */
public class PasteUndoRedo extends DataUndoRedo {
    private CopyDataset copyDs;
    private int[] selCell;

    private ArrayList<Data[]> listData;
    private ArrayList<DataHeader[]> listHeader;
    private ArrayList<Integer>[] listRowAndCol;

    public PasteUndoRedo(DataTable table, FitexToolPanel dataToolPanel, ControllerInterface controller, CopyDataset copyDs, int[] selCell, ArrayList<Data[]> listData, ArrayList<DataHeader[]> listHeader, ArrayList<Integer>[] listRowAndCol) {
        super(table, dataToolPanel, controller);
        this.copyDs = copyDs;
        this.selCell = selCell;
        this.listData = listData;
        this.listHeader = listHeader;
        this.listRowAndCol = listRowAndCol;
    }

    

    @Override
    public void redo() throws CannotRedoException {
        super.redo();
        ArrayList v = new ArrayList();
        CopexReturn cr = this.controller.paste(getDataset().getDbKey(),copyDs, selCell, v);
        if(cr.isError()){
            dataToolPanel.displayError(cr, dataToolPanel.getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }
        Dataset nds = (Dataset)v.get(0);
        dataToolPanel.updateDataset(nds);
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
        //Dataset nds = getDataset() ;
        // remove header
        int nbH = this.listHeader.size();
        for (int i=0; i<nbH; i++){
            ArrayList v = new ArrayList();
            DataHeader oldH = this.listHeader.get(i)[0];
            DataHeader newH = this.listHeader.get(i)[1];
            String title = oldH == null ? "" : oldH.getValue() ;
            String unit = oldH == null ? "" : (oldH.getUnit() == null ?"" : oldH.getUnit()) ;
            String description = oldH == null ? "" : oldH.getDescription();
            String type = oldH == null ?DataConstants.DEFAULT_TYPE_COLUMN : oldH.getType();
            String formula = oldH == null ? null : oldH.getFormulaValue() ;
            boolean scientificNotation = oldH == null ? false : oldH.isScientificNotation();
            int nbShDec = oldH == null ? DataConstants.NB_DECIMAL_UNDEFINED : oldH.getNbShownDecimals();
            int nbSigDig = oldH == null? DataConstants.NB_SIGNIFICANT_DIGITS_UNDEFINED : oldH.getNbSignificantDigits();
            CopexReturn cr = this.controller.updateDataHeader(getDataset(),true, newH.getNoCol(), title,unit,description, type,  formula,dataToolPanel.getFunction(formula),scientificNotation, nbShDec, nbSigDig,  v );
            if(cr.isError()){
                dataToolPanel.displayError(cr, dataToolPanel.getBundleString("TITLE_DIALOG_ERROR"));
                return;
            }
            //nds = (Dataset)v.get(0);
        }
        // remove data
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
        // remove rows/columns if necessary
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
        cr = this.controller.exportHTML();
        if (cr.isError()){
            dataToolPanel.displayError(cr, dataToolPanel.getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }
    }

    @Override
    public Element toXMLLog(){
        Element element = new Element("undoRedoPaste");
        element.addContent(copyDs.toXMLLog());
        Element e = new Element("sel_cell");
        e.addContent(new Element("id_row").setText(Integer.toString(selCell[0])));
        e.addContent(new Element("id_col").setText(Integer.toString(selCell[1])));
        element.addContent(e);
        return element;
    }


}

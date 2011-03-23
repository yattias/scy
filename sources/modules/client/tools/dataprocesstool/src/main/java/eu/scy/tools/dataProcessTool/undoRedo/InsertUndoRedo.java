/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.dataProcessTool.undoRedo;

import eu.scy.tools.dataProcessTool.common.Data;
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
 * insert row / col undo redo
 * @author Marjolaine
 */
public class InsertUndoRedo extends DataUndoRedo {
    private boolean isOnCol;
    private int nbInsert;
    private int idBefore;

    public InsertUndoRedo(DataTable table, FitexToolPanel dataToolPanel, ControllerInterface controller, boolean isOnCol, int nbInsert, int idBefore) {
        super(table, dataToolPanel, controller);
        this.isOnCol = isOnCol;
        this.nbInsert = nbInsert;
        this.idBefore = idBefore;
    }

    @Override
    public void redo() throws CannotRedoException {
        super.redo();
        ArrayList v = new ArrayList();
        CopexReturn cr = this.controller.insertData(getDataset(), isOnCol, nbInsert, idBefore, v);
        if (cr.isError()){
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
        ArrayList<Data> listData = new ArrayList();
        ArrayList[] listRowAndCol = new ArrayList[2];
        listRowAndCol[0] = new ArrayList();
        listRowAndCol[1] = new ArrayList();
        Dataset ds = getDataset();
        int nbR = ds.getNbRows();
        int nbC = ds.getNbCol() ;
        if (isOnCol){
            for (int j=0; j<nbInsert; j++){
                listRowAndCol[1].add(j+idBefore);
                for (int i=0; i<nbR; i++){
                    if (ds.getData(i, j+idBefore) != null){
                        listData.add(ds.getData(i, j+idBefore));
                    }
                }
            }
        }else{
            // en lignes
            for (int i=0; i<nbInsert; i++){
                listRowAndCol[0].add(i+idBefore);
                for (int j=0 ; j<nbC; j++){
                    if (ds.getData(i+idBefore, j) != null){
                        listData.add(ds.getData(i+idBefore, j));
                    }
                }
            }
        }
        ArrayList v = new ArrayList();
        CopexReturn cr = this.controller.deleteData(true, getDataset(), listData, listRowAndCol[0], listRowAndCol[1],new ArrayList(),v);
        if (cr.isError()){
            dataToolPanel.displayError(cr, dataToolPanel.getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }
        if(v.size()> 0){
            Dataset newDs = (Dataset)v.get(0);
            dataToolPanel.updateDataset(newDs);
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
        Element element = new Element("undoRedoInsert");
        element.addContent(new Element("is_on_col").setText(isOnCol ?"true":"false"));
        element.addContent(new Element("idBefore").setText(Integer.toString(idBefore)));
        element.addContent(new Element("nbInsert").setText(Integer.toString(nbInsert)));
        return element;
    }

}

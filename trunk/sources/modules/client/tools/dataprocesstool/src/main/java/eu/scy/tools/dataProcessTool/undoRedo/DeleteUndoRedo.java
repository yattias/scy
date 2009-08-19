/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.dataProcessTool.undoRedo;

import eu.scy.tools.dataProcessTool.common.Data;
import eu.scy.tools.dataProcessTool.common.DataHeader;
import eu.scy.tools.dataProcessTool.common.DataOperation;
import eu.scy.tools.dataProcessTool.common.Dataset;
import eu.scy.tools.dataProcessTool.controller.ControllerInterface;
import eu.scy.tools.dataProcessTool.dataTool.DataProcessToolPanel;
import eu.scy.tools.dataProcessTool.dataTool.DataTable;
import eu.scy.tools.dataProcessTool.utilities.CopexReturn;
import java.util.ArrayList;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

/**
 * delete datas undo redo
 * @author Marjolaine
 */
public class DeleteUndoRedo extends DataUndoRedo {
    private ArrayList<Data> listData;
    private ArrayList<DataHeader> listHeader;
    private ArrayList<Integer>[] listNoRowCol;
    private ArrayList<DataOperation> listOperations;

    public DeleteUndoRedo(DataTable table, DataProcessToolPanel dataToolPanel, ControllerInterface controller, ArrayList<Data> listData, ArrayList<DataHeader> listHeader, ArrayList[] listNoRowCol, ArrayList<DataOperation> listOperations) {
        super(table, dataToolPanel, controller);
        this.listData = listData;
        this.listHeader = listHeader ;
        this.listNoRowCol = listNoRowCol;
        this.listOperations = listOperations;
    }

    @Override
    public void redo() throws CannotRedoException {
        super.redo();
        CopexReturn cr = this.controller.deleteData(true, getDataset(), listData, listOperations, listNoRowCol);
        if (cr.isError()){
            dataToolPanel.displayError(cr, dataToolPanel.getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }
    }

    @Override
    public void undo() throws CannotUndoException {
        super.undo();
        int nbRS = listNoRowCol[0].size();
        int nbCS = listNoRowCol[1].size();
        Dataset nds = getDataset() ;
        if(nbRS > 0 || nbCS > 0){
            // reinsertion de lignes /colonnes
            ArrayList v = new ArrayList();
            boolean isOnCol = nbCS > 0;
            int nbInsert = 1;
            int nb = isOnCol ? nbCS : nbRS;
            for (int i=0; i<nb; i++){
                int idBefore = isOnCol ? (listNoRowCol[1].get(i)) : (listNoRowCol[0].get(i));
                CopexReturn cr = this.controller.insertData(getDataset(), isOnCol, nbInsert, idBefore, v);
                if (cr.isError()){
                    dataToolPanel.displayError(cr, dataToolPanel.getBundleString("TITLE_DIALOG_ERROR"));
                    return;
                }
                nds = (Dataset)v.get(0);
            }
        }
        // mise à jour des données
        int nbData = listData.size();
        for (int i=0; i<nbData; i++){
            Data d= listData.get(i);
            ArrayList v = new ArrayList();
            CopexReturn cr = this.controller.updateData(getDataset(), d.getNoRow(), d.getNoCol(), d.getValue(), v);
            if (cr.isError()){
               dataToolPanel.displayError(cr, dataToolPanel.getBundleString("TITLE_DIALOG_ERROR"));
               return;
            }
            nds = (Dataset)v.get(0);
        }
        int nbH = listHeader.size();
        for (int i=0; i<nbH; i++){
            DataHeader h = listHeader.get(i);
            ArrayList v = new ArrayList();
            CopexReturn cr = this.controller.updateDataHeader(getDataset(), h.getNoCol(), h.getValue(), h.getUnit(), v);
            if (cr.isError()){
               dataToolPanel.displayError(cr, dataToolPanel.getBundleString("TITLE_DIALOG_ERROR"));
               return;
            }
            nds = (Dataset)v.get(0);
        }
        // ajout des operations
        int nbO= this.listOperations.size();
        if (nbO > 0){
            for (int i=0; i<nbO; i++){
                ArrayList v = new ArrayList();
                DataOperation operation = listOperations.get(i);
                CopexReturn cr = this.controller.createOperation(getDataset(), operation.getTypeOperation().getType(), operation.isOnCol(), operation.getListNo(), v);
                if (cr.isError()){
                    dataToolPanel.displayError(cr, dataToolPanel.getBundleString("TITLE_DIALOG_ERROR"));
                    return;
                }
                nds = (Dataset)v.get(0);
            }
        }
        // mise à jour
        dataToolPanel.updateDataset(nds) ;
    }



}

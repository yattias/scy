/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.dataProcessTool.undoRedo;

import eu.scy.client.tools.dataProcessTool.common.Data;
import eu.scy.client.tools.dataProcessTool.common.DataHeader;
import eu.scy.client.tools.dataProcessTool.common.DataOperation;
import eu.scy.client.tools.dataProcessTool.common.Dataset;
import eu.scy.client.tools.dataProcessTool.common.Graph;
import eu.scy.client.tools.dataProcessTool.common.Visualization;
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
 * delete datas undo redo
 * @author Marjolaine
 */
public class DeleteUndoRedo extends DataUndoRedo {
    private ArrayList<Data> listData;
    private ArrayList<Data> oldListData;
    private ArrayList<DataHeader> listHeader;
    private ArrayList<Integer>[] listNoRowCol;
    private ArrayList<DataOperation> listOperations;
    private ArrayList<DataOperation> listOperationToUpdate;
    private ArrayList<Visualization> listVisualizationToUpdate;
    private ArrayList<DataOperation> listOperationToDel;
    private ArrayList<Visualization> listVisualizationToDel;

    public DeleteUndoRedo(DataTable table, FitexToolPanel dataToolPanel, ControllerInterface controller, ArrayList<Data> oldListData,ArrayList<Data> listData, ArrayList<DataHeader> listHeader, ArrayList[] listNoRowCol, ArrayList<DataOperation> listOperations,  ArrayList<DataOperation> listOperationToUpdate,ArrayList<DataOperation> listOperationToDel, ArrayList<Visualization> listVisualizationToUpdate,ArrayList<Visualization> listVisualizationToDel) {
        super(table, dataToolPanel, controller);
        this.oldListData =oldListData;
        this.listData = listData;
        this.listHeader = listHeader ;
        this.listNoRowCol = listNoRowCol;
        this.listOperations = listOperations;
        this.listOperationToUpdate = listOperationToUpdate;
        this.listOperationToDel = listOperationToDel;
        this.listVisualizationToUpdate = listVisualizationToUpdate;
        this.listVisualizationToDel = listVisualizationToDel;
    }

    @Override
    public void redo() throws CannotRedoException {
        super.redo();
        ArrayList v = new ArrayList();
        CopexReturn cr = this.controller.deleteData(true, getDataset(), listData, listNoRowCol[0], listNoRowCol[1],listOperations,  v);
        if (cr.isError()){
            dataToolPanel.displayError(cr, dataToolPanel.getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }
        if(v.size()> 0){
            Dataset ds = (Dataset)v.get(0);
            dataToolPanel.updateDataset(ds);
            ArrayList[] tabDel = (ArrayList[])v.get(1);
            listOperationToUpdate =  (ArrayList<DataOperation>)tabDel[0];
            listOperationToDel = (ArrayList<DataOperation>)tabDel[1];
            listVisualizationToUpdate = (ArrayList<Visualization>)tabDel[2];
            listVisualizationToDel = (ArrayList<Visualization>)tabDel[3];
        }
        dataToolPanel.logUndo(this);
        cr = this.controller.exportHTML();
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
            // insert rows/columns
            ArrayList v = new ArrayList();
            boolean isOnCol = nbCS > 0;
            int nbInsert = 1;
            int nb = isOnCol ? nbCS : nbRS;
            for (int i=nb-1; i>=0; i--){
                int idBefore = isOnCol ? (listNoRowCol[1].get(i)) : (listNoRowCol[0].get(i));
                CopexReturn cr = this.controller.insertData(getDataset(), isOnCol, nbInsert, idBefore, v);
                if (cr.isError()){
                    dataToolPanel.displayError(cr, dataToolPanel.getBundleString("TITLE_DIALOG_ERROR"));
                    return;
                }
                nds = (Dataset)v.get(0);
            }
        }
        //udpate data
        int nbData = oldListData.size();
        for (int i=0; i<nbData; i++){
            Data d= oldListData.get(i);
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
            CopexReturn cr = this.controller.updateDataHeader(getDataset(), true, h.getNoCol(), h.getValue(), h.getUnit(),h.getDescription(), h.getType(), h.getFormulaValue(), dataToolPanel.getFunction(h.getFormulaValue()), h.isScientificNotation(), h.getNbShownDecimals(), h.getNbSignificantDigits(), v);
            if (cr.isError()){
               dataToolPanel.displayError(cr, dataToolPanel.getBundleString("TITLE_DIALOG_ERROR"));
               return;
            }
            nds = (Dataset)v.get(0);
        }
        // add operations
        int nbO= this.listOperations.size();
        for (int i=0; i<nbO; i++){
            ArrayList v = new ArrayList();
            DataOperation operation = listOperations.get(i);
            CopexReturn cr = this.controller.createOperation(getDataset(), operation.getTypeOperation().getType(), operation.isOnCol(), operation.getListNo(), v);
            if (cr.isError()){
                dataToolPanel.displayError(cr, dataToolPanel.getBundleString("TITLE_DIALOG_ERROR"));
                return;
            }
            nds = (Dataset)v.get(0);
            DataOperation op  = (DataOperation)v.get(1);
            listOperations.set(i, op);

        }
        // add operations which were removed because we remove data
        nbO= this.listOperationToDel.size();
        for (int i=0; i<nbO; i++){
            ArrayList v= new ArrayList();
            DataOperation operation = listOperationToDel.get(i);
            CopexReturn cr = this.controller.createOperation(getDataset(), operation.getTypeOperation().getType(), operation.isOnCol(), operation.getListNo(), v);
            if(cr.isError()){
                dataToolPanel.displayError(cr, dataToolPanel.getBundleString("TITLE_DIALOG_ERROR"));
                return;
            }
            nds = (Dataset)v.get(0);
            DataOperation ope  = (DataOperation)v.get(1);
            listOperationToDel.set(i, ope);
        }
        // update operations/data
        for(Iterator<DataOperation> o = listOperationToUpdate.iterator();o.hasNext();){
            ArrayList v= new ArrayList();
            DataOperation op = o.next();
            CopexReturn cr = this.controller.updateOperation(getDataset(), op, v);
            if(cr.isError()){
                dataToolPanel.displayError(cr, dataToolPanel.getBundleString("TITLE_DIALOG_ERROR"));
                return;
            }
            nds = (Dataset)v.get(0);
        }
        // add visualization
        for(Iterator<Visualization> vis = listVisualizationToDel.iterator();vis.hasNext();){
            ArrayList v= new ArrayList();
            CopexReturn cr = this.controller.createVisualization(getDataset(), vis.next(), true, v);
            if(cr.isError()){
                dataToolPanel.displayError(cr, dataToolPanel.getBundleString("TITLE_DIALOG_ERROR"));
                return;
            }
            nds = (Dataset)v.get(0);
        }
        // update visualizations
        for(Iterator<Visualization> vis = listVisualizationToUpdate.iterator(); vis.hasNext();){
            ArrayList v= new ArrayList();
            Visualization visu = vis.next();
            if(visu instanceof Graph){
                CopexReturn cr = this.controller.setParamGraph(getDataset().getDbKey(), visu.getDbKey(), ((Graph)visu).getParamGraph(), v);
                if(cr.isError()){
                    dataToolPanel.displayError(cr, dataToolPanel.getBundleString("TITLE_DIALOG_ERROR"));
                    return;
                }
                 Visualization newvis = (Visualization)v.get(0);
                int idVis = nds.getIdVisualization(newvis.getDbKey());
                if (idVis != -1)
                    nds.getListVisualization().set(idVis, newvis);
            }
        }
        //System.out.println("dataset apres undo : "+nds.toString());
        // update
        dataToolPanel.updateDataset(nds) ;
        dataToolPanel.logRedo(this);
        CopexReturn cr = this.controller.exportHTML();
        if (cr.isError()){
            dataToolPanel.displayError(cr, dataToolPanel.getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }
    }


    @Override
    public Element toXMLLog(){
        Element element = new Element("undoRedoDelete");
        return element;
    }

}

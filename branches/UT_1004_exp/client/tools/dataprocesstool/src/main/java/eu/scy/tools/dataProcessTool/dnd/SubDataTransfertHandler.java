/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.dataProcessTool.dnd;

import eu.scy.tools.dataProcessTool.dataTool.DataTable;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.TransferHandler;

/**
 * transfert de donnees
 * @author Marjolaine
 */
public class SubDataTransfertHandler extends TransferHandler {
    /* type d'actions supportees */
    @Override
    public int getSourceActions(JComponent c) {
        return COPY_OR_MOVE;
    }

    /* creation d'un objet transferable */
    @Override
    protected Transferable createTransferable(JComponent c) {
        if (c instanceof DataTable && ((DataTable)c).selIsSubData()){
            //SubDataTransferable subData = new SubDataTransferable(((DataTable)c).getSubDataCopy());
            //return subData;
            return null;
        }else
            return null;
    }

    /* methode invoquee apres le transfert */
    @Override
    protected void exportDone(JComponent source, Transferable data, int action) {
        if (action == MOVE && data instanceof SubDataTransferable){
            SubData subData = ((SubDataTransferable)data).getSubData();
            DataTable table = subData.getTable();
            //table.removeData(subData);
        }
    }

    /* methode appelee lors du geste drag and drop savoir si le transfert est accepte */
    @Override
    public boolean canImport(TransferSupport support) {
        if (support.isDataFlavorSupported(SubDataTransferable.subDataFlavor)){
            JTable.DropLocation dropLocation  = (JTable.DropLocation)support.getDropLocation();
            int row = dropLocation.getRow();
            int col = dropLocation.getColumn();

            try{
                Object o = support.getTransferable().getTransferData(SubDataTransferable.subDataFlavor);
                if (o instanceof SubDataTransferable){
                        SubData subDataToMove = ((SubDataTransferable)o).getSubData();
                        

            }
            }catch(UnsupportedFlavorException e1){
                System.out.println("ERROR DRAG AND DROP :"+e1);
                return false;
            }catch(IOException e2){
                System.out.println("ERROR DRAG AND DROP :"+e2);
                return false;
            }
            return true;
        }else {
            System.out.println("non transferable ");
            return false;
        }
    }


     @Override
    public boolean importData(TransferSupport support) {
       if (!canImport(support))
            return false;
        JTable.DropLocation dropLocation  = (JTable.DropLocation)support.getDropLocation();
        try{
            Object o = support.getTransferable().getTransferData(SubDataTransferable.subDataFlavor);
            if (o instanceof SubDataTransferable){
               SubData subDataToMove = ((SubDataTransferable)o).getSubData();
                DataTable table = subDataToMove.getTable();
               int dropNoCol = getNodeNoColInsert(dropLocation, table);
                //return table.moveSubData(subDataToMove, dropNoCol);
               return false;
            }
            return true;
        }catch(UnsupportedFlavorException e1){
            System.out.println("ERROR DRAG AND DROP :"+e1);
            return false;
        }catch(IOException e2){
            System.out.println("ERROR DRAG AND DROP :"+e2);
            return false;
        }
    }

     public int getNodeNoColInsert(JTable.DropLocation dropLocation, DataTable table){
         return 0;
     }

}

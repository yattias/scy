/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.dataProcessTool.dnd;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

/**
 * sous donnees pouvant etre deplacer /coller
 * @author Marjolaine
 */
public class SubDataTransferable implements Transferable {
    // ATTRIBUTS
    /* sous table */
    private SubData subData;

    /* flavor supporte*/
    private static String mimeType = DataFlavor.javaJVMLocalObjectMimeType + ";class="+ SubDataTransferable.class.getName();
    public final static  DataFlavor subDataFlavor = new DataFlavor(mimeType, "SubDataTransferable");
   /* flavors */
   private DataFlavor[] flavors = {subDataFlavor};

    // CONSTRUCTEURS
    public SubDataTransferable(SubData subData) {
        this.subData = subData;
    }

    public SubData getSubData() {
        return subData;
    }


    @Override
    public DataFlavor[] getTransferDataFlavors() {
       return flavors;
    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return flavor.equals(subDataFlavor);
    }

    @Override
    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
        if (flavor == subDataFlavor) {
           return this;
        }else
           throw new UnsupportedFlavorException(flavor);
    }
}

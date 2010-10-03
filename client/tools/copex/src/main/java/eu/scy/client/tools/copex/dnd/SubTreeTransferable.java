/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.dnd;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

/**
 * sous arbre pouvant etre drag and drop
 * @author MBO 
 */
public class SubTreeTransferable implements Transferable {
    // ATTRIBUTS
    /* sous arbre */
    private SubTree subTree;
    
    /* flavor supporte*/
    private static String mimeType = DataFlavor.javaJVMLocalObjectMimeType + ";class="+ SubTreeTransferable.class.getName();
    public final static  DataFlavor subTreeFlavor = new DataFlavor(mimeType, "SubTreeTransferable");
   /* flavors */
   private DataFlavor[] flavors = {subTreeFlavor};

    // CONSTRUCTEURS
    public SubTreeTransferable(SubTree subTree) {
        this.subTree = subTree;
    }

    public SubTree getSubTree() {
        return subTree;
    }
    
    
    @Override
    public DataFlavor[] getTransferDataFlavors() {
       return flavors;
    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return flavor.equals(subTreeFlavor);
    }

    @Override
    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
        if (flavor == subTreeFlavor) {
           return this;
        }else
           throw new UnsupportedFlavorException(flavor);
    }

}

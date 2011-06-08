/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.dnd;

import eu.scy.client.tools.copex.edp.CopexNode;
import eu.scy.client.tools.copex.edp.CopexTree;
import eu.scy.client.tools.copex.utilities.MyConstants;
import java.awt.Point;
import java.awt.datatransfer.Transferable;
import java.io.IOException;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.util.Vector;
import javax.swing.*;
import javax.swing.tree.TreePath;

/**
 * data transfer for drag and drop
 * @author Marjolaine
 */
public class TreeTransferHandler extends TransferHandler {
    private boolean debug = false;


    /* action type supported */
    @Override
    public int getSourceActions(JComponent c) {
        return MOVE;
    }

    /* creation of a transferable object  */
    @Override
    protected Transferable createTransferable(JComponent c) {
        if (c instanceof CopexTree && ((CopexTree)c).selIsSubTree() && ((CopexTree)c).selCanBeMove()){
            SubTreeTransferable subTree = new SubTreeTransferable(((CopexTree)c).getSubTreeCopy(true));
            return subTree;
        }else
            return null;
    }

    
    /* method called after the transfer */
    @Override
    protected void exportDone(JComponent source, Transferable data, int action) {
        if (action == MOVE && data instanceof SubTreeTransferable){
            SubTree subTree = ((SubTreeTransferable)data).getSubTree();
            CopexTree tree = subTree.getOwner();
            tree.removeTask(subTree, true);
        }
    }
    
    

    /* methode called while drag and drop to know if the transfer is accepted */
    @Override
    public boolean canImport(TransferSupport support) {
        if (support.isDataFlavorSupported(SubTreeTransferable.subTreeFlavor)){
            JTree.DropLocation dropLocation  = (JTree.DropLocation)support.getDropLocation();
            TreePath tp = dropLocation.getPath();
            if (tp == null){
                return false;
            }
            try{
                Object o = support.getTransferable().getTransferData(SubTreeTransferable.subTreeFlavor);
                if (o instanceof SubTreeTransferable){
                        SubTree subTreeToMove = ((SubTreeTransferable)o).getSubTree();
                        Vector v = new Vector();
                        CopexNode insertNode = getNodeInsert(dropLocation, subTreeToMove.getOwner(), v);
                        boolean brother = (Boolean )v.get(0);
                        boolean markNode = (Boolean)v.get(1);
                        // if node is not in the tree task => denied
                        if(insertNode.isQuestion() || insertNode.isHypothesis() || insertNode.isGeneralPrinciple() || insertNode.isMaterial() ||insertNode.isDatasheet() || insertNode.isEvaluation())
                            return false;
                        // if insert node is in the sub tree => denied dnd
                        if (subTreeToMove.containNode(insertNode)){
                            return false;
                        }
                        if (subTreeToMove.isQuestion() && !insertNode.isQuestion()){
                            return false;
                        }
                        boolean nodeCanBeParent = (insertNode.isManipulation() &&subTreeToMove.getProc().getQuestion().getParentRight() == MyConstants.EXECUTE_RIGHT  ) ||
                                (!insertNode.isManipulation() && insertNode.canBeParent());
                        boolean parentNodeCanBeParent = insertNode.getParent() != null &&
                            ( ((CopexNode)insertNode.getParent()).isManipulation() && subTreeToMove.getProc().getQuestion().getParentRight() == MyConstants.EXECUTE_RIGHT)
                            || (! ((CopexNode)insertNode.getParent()).isManipulation() && ((CopexNode)insertNode.getParent()).canBeParent());
                        // if insert node cans not have children => dnd denied
                        //if ((! brother && !insertNode.canBeParent() ) || (brother && insertNode.getParent() != null && !((CopexNode)insertNode.getParent()).canBeParent()))
                        if((!brother && !nodeCanBeParent) || (brother && !parentNodeCanBeParent)){
                            return false;
                        }
                        // if we move tasks that contain materials prod. after => denied
                        if (isProblemWithMaterialProd(insertNode, subTreeToMove)){
                            return false;
                        }
                        // and if we move takss that contain data prod. after => denied
                        if (isProblemWithDataProd(insertNode, subTreeToMove)){
                            return false;
                        }
                        // parent node is surrounded, only in the case we move the task in the parent
                        subTreeToMove.getOwner().refreshMouseOver();
                        /*if(brother){
                            if (insertNode.getParent() != null){
                                ((CopexTreeNode)insertNode.getParent()).setMouseover(true);
                            }
                        }else{
                            insertNode.setMouseover(true);
                        }*/
                        if( (!insertNode.isAction() && !brother && insertNode.getChildCount() == 0)){
                                insertNode.setMouseover(true);
                        }
                         if ((insertNode.isAction() && insertNode.getParent() != null && ((CopexNode)insertNode.getParent()).getIndex(insertNode) == ((CopexNode)insertNode.getParent()).getChildCount()-1 )){
                             ((CopexNode)insertNode.getParent()).setMouseover(true);
                        }
                        subTreeToMove.getOwner().revalidate();
                        subTreeToMove.getOwner().repaint();

            }
            }catch(UnsupportedFlavorException e1){
                // System.out.println("ERROR DRAG AND DROP :"+e1);
                return false;
            }catch(IOException e2){
                // System.out.println("ERROR DRAG AND DROP :"+e2);
                return false;
            }
            return true;
        }else {
            // System.out.println("non transferable ");
            return false;
        }
    }

    /* returns true if the drag of the subtree is a problem from a material prod. point of view  */
    private boolean isProblemWithMaterialProd(CopexNode insertNode, SubTree subTreeToMove){
        return subTreeToMove.getOwner().isProblemWithMaterialProd(insertNode, subTreeToMove);
    }

    /* returns true if the drag of the subtree is a problem from a data prod. point of view  */
    private boolean isProblemWithDataProd(CopexNode insertNode, SubTree subTreeToMove){
        return subTreeToMove.getOwner().isProblemWithDataProd(insertNode, subTreeToMove);
    }


    /*returns insert node */
    private CopexNode getNodeInsert(JTree.DropLocation dropLocation, CopexTree tree , Vector v){
        boolean brother = false;
        CopexNode insertNode = null;
        TreePath tp = dropLocation.getPath();
        Point point = dropLocation.getDropPoint();
        // tp is the parent where we have to insert => search ondex where insert
        int id = dropLocation.getChildIndex();
        // Be careful, id =0 can mean that we insert as brother or first child 
        CopexNode node = (CopexNode)tp.getLastPathComponent();
        insertNode = node;
        boolean markNode = false;
        if(id==-1 || id > insertNode.getChildCount()){
            id = insertNode.getChildCount();
            if(id==0){
                markNode = true;
            }else{
                insertNode = (CopexNode)node.getChildAt(id-1);
                brother = true;
            }

        }else if(id == 0){
            brother = false;
        }else{
            insertNode = (CopexNode)node.getChildAt(id-1);
            brother = true;
        }
        if((insertNode.isManipulation() || insertNode.isQuestion()) && brother){
            if(insertNode.getChildCount() == 0){
                brother = false;
            }else{
                insertNode = (CopexNode)insertNode.getChildAt(insertNode.getChildCount()-1);
            }
        }

        v.add(brother);
        v.add(markNode);
        return insertNode;

    }
    
    @Override
    public boolean importData(TransferSupport support) {
       if (!canImport(support))
            return false;
        JTree.DropLocation dropLocation  = (JTree.DropLocation)support.getDropLocation();
        try{
            Object o = support.getTransferable().getTransferData(SubTreeTransferable.subTreeFlavor);
            if (o instanceof SubTreeTransferable){
               SubTree subTreeToMove = ((SubTreeTransferable)o).getSubTree();
                CopexTree tree = subTreeToMove.getOwner();
                Vector v = new Vector();
                CopexNode dropNode = getNodeInsert(dropLocation, tree, v);
                boolean brother = (Boolean )v.get(0);
                boolean isOk =  tree.moveSubTree(subTreeToMove, dropNode, brother);
                tree.refreshMouseOver();
                return isOk;
            }
            return true;
        }catch(UnsupportedFlavorException e1){
            // System.out.println("ERROR DRAG AND DROP :"+e1);
            return false;
        }catch(IOException e2){
            // System.out.println("ERROR DRAG AND DROP :"+e2);
            return false;
        }
    }
}

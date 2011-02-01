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
import java.util.Locale;
import java.util.Vector;
import javax.swing.*;
import javax.swing.tree.TreePath;

/**
 * transfert de donnees pour le drag and drop
 * @author MBO
 */
public class TreeTransferHandler extends TransferHandler {
    private boolean debug = false;


    /* type d'actions supportees */
    @Override
    public int getSourceActions(JComponent c) {
        return MOVE;
    }

    /* creation d'un objet transferable */
    @Override
    protected Transferable createTransferable(JComponent c) {
        if (c instanceof CopexTree && ((CopexTree)c).selIsSubTree() && ((CopexTree)c).selCanBeMove()){
            SubTreeTransferable subTree = new SubTreeTransferable(((CopexTree)c).getSubTreeCopy(true));
            return subTree;
        }else
            return null;
    }

    
    /* methode invoquee apres le transfert */
    @Override
    protected void exportDone(JComponent source, Transferable data, int action) {
        if (action == MOVE && data instanceof SubTreeTransferable){
            SubTree subTree = ((SubTreeTransferable)data).getSubTree();
            CopexTree tree = subTree.getOwner();
            tree.removeTask(subTree, true);
        }
    }
    
    

    /* methode appelee lors du geste drag and drop savoir si le transfert est accepte */
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
                        // si le noeud n'est pas de l'arbre des taches => on refuse
                        if(insertNode.isQuestion() || insertNode.isHypothesis() || insertNode.isGeneralPrinciple() || insertNode.isMaterial() ||insertNode.isDatasheet() || insertNode.isEvaluation())
                            return false;
                        // si le noeud insertion appartient au sous arbre => on refuse le drag and drop
                        if (subTreeToMove.containNode(insertNode)){
                            // if(debug)
                                // System.out.println("containNode");
                            return false;
                        }
                        if (subTreeToMove.isQuestion() && !insertNode.isQuestion()){
                            // if(debug)
                                // System.out.println("is question");
                            return false;
                        }
                        boolean nodeCanBeParent = (insertNode.isManipulation() &&subTreeToMove.getProc().getQuestion().getParentRight() == MyConstants.EXECUTE_RIGHT  ) ||
                                (!insertNode.isManipulation() && insertNode.canBeParent());
                       // if(debug)
                            // System.out.println("nodeCanBeParent : "+nodeCanBeParent);
                        boolean parentNodeCanBeParent = insertNode.getParent() != null &&
                            ( ((CopexNode)insertNode.getParent()).isManipulation() && subTreeToMove.getProc().getQuestion().getParentRight() == MyConstants.EXECUTE_RIGHT)
                            || (! ((CopexNode)insertNode.getParent()).isManipulation() && ((CopexNode)insertNode.getParent()).canBeParent());
                        // if(debug)
                            // System.out.println("parentNodeCanBeParent : "+parentNodeCanBeParent);
                        // si le noeud d'insertion ne peut avoir d'enfants => on refuse
                        //if ((! brother && !insertNode.canBeParent() ) || (brother && insertNode.getParent() != null && !((CopexNode)insertNode.getParent()).canBeParent()))
                        if((!brother && !nodeCanBeParent) || (brother && !parentNodeCanBeParent)){
                            // if(debug)
                                // System.out.println("pbl parent");
                            return false;
                        }
                        // si on deplace des taches qui contiennent des materials produits apres => on refuse
                        if (isProblemWithMaterialProd(insertNode, subTreeToMove)){
                            // if(debug)
                                // System.out.println("pbl with material");
                            return false;
                        }
                        // si on deplace des taches qui contiennent des data produits apres => on refuse
                        if (isProblemWithDataProd(insertNode, subTreeToMove)){
                            // if(debug)
                                // System.out.println("pbl with data");
                            return false;
                        }
                        // on entoure le noeud parent, uniquement lorsqu'on met la tache dans une action
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

    /* retourne vrai si le deplacement du sous arbre pose probleme du point de vue du material produit */
    private boolean isProblemWithMaterialProd(CopexNode insertNode, SubTree subTreeToMove){
        return subTreeToMove.getOwner().isProblemWithMaterialProd(insertNode, subTreeToMove);
    }

    /* retourne vrai si le deplacement du sous arbre pose probleme du point de vue du data produit */
    private boolean isProblemWithDataProd(CopexNode insertNode, SubTree subTreeToMove){
        return subTreeToMove.getOwner().isProblemWithDataProd(insertNode, subTreeToMove);
    }


    /* retourne le noeud d'insertion */
    private CopexNode getNodeInsert(JTree.DropLocation dropLocation, CopexTree tree , Vector v){
        boolean brother = false;
        CopexNode insertNode = null;
        TreePath tp = dropLocation.getPath();
        Point point = dropLocation.getDropPoint();
        // tp est en fait le parent ou il faut inserer => recherche index ou inserer
        int id = dropLocation.getChildIndex();
        // Attention id =0 peut signifier qu'on insere en tant que frere ou en tant que premier enfant
        CopexNode node = (CopexNode)tp.getLastPathComponent();
        insertNode = node;
//        if (id  > 0)
//            insertNode = (CopexNode)node.getChildAt(id-1);
//        if(debug)
//            // System.out.println("*****insertNode : "+insertNode.getDebug(tree.getLocale()));
//        TreePath path = tree.getPathForLocation((int)point.getX(), (int)point.getY());
//        boolean markNode = false;
////        if (path != null){
////            CopexTreeNode realNode = (CopexTreeNode)path.getLastPathComponent();
////            // System.out.println("realNode : "+realNode.getTask().getDescription());
////            // cas d'ajout en fin d'une etape / ss question
////            if (!insertNode.isAction() && insertNode.getChildCount() > 0 && insertNode.getChildAt(insertNode.getChildCount() -1).equals(realNode)){
////                insertNode = (CopexTreeNode)insertNode.getChildAt(insertNode.getChildCount() -1);
////                brother = true;
////            }
////            //cas drag and drop sur dossier => ajoute a la fin
////            if (!insertNode.isAction() && realNode.equals(insertNode) && insertNode.getChildCount() > 0){
////                insertNode = (CopexTreeNode)insertNode.getChildAt(insertNode.getChildCount() -1);
////                brother = true;
////                markNode = true;
////            }
////            if(!insertNode.isAction() && !realNode.isAction() && insertNode.getTask().getDbKeyBrother() == realNode.getTask().getDbKey() && realNode.getChildCount() == 0){
////                insertNode = realNode;
////                brother = false;
////            }
////            // cas de l'insertion entre 2 freres etapes
////            CopexTreeNode parent = (CopexTreeNode)insertNode.getParent();
////            if (!insertNode.isAction() && parent != null && parent.getChildAfter(insertNode) != null && parent.getChildAfter(insertNode).equals(realNode))
////                brother = true;
////
////        }else{
////                //// System.out.println("path null");
////                if (!insertNode.isAction())
////                    brother = true;
////
////        }
//         if(insertNode.isAction()){
//             brother = true;
//         }else{
//            if (path == null){
//                brother = true;
//            }else{
//                CopexNode realNode = (CopexNode)path.getLastPathComponent();
//                if(debug)
//                    // System.out.println("realNode : "+realNode.getDebug(tree.getLocale()));
//                // si node =realNode => sur dossier => a la fin de node
//                if(realNode.equals(insertNode)){
//                    if(insertNode.getChildCount() != 0){
//                        insertNode = (CopexNode)insertNode.getChildAt(insertNode.getChildCount() -1);
//                        brother = true;
//                        markNode = true;
//                    }
//                }
//                // si realNode = 1° enfant de node => position 0
//                else if (insertNode.getChildCount() > 0 && insertNode.getChildAt(0).equals(realNode)){
//
//                }
//                // si realnode = dernier enfant => frere de node
//                else if (insertNode.getChildCount() > 0 && insertNode.getChildAt(insertNode.getChildCount()-1).equals(realNode)){
//                    brother = true;
//                }
//                // si realNode = frere de node => frere de node
//                else if (insertNode.getTask() != null && realNode.getTask() != null && insertNode.getTask().getDbKeyBrother() == realNode.getTask().getDbKey()){
//                    brother = true;
//                }
//            }
//         }
//          //// System.out.println("brother : "+brother);
//          v.add(brother);
//          v.add(markNode);
//          if(debug)
//            // System.out.println("INSERTION EN "+brother+" au noeud "+insertNode.getDebug(tree.getLocale()));
//          return insertNode;
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
        //// System.out.println("INSERTION EN "+brother+" au noeud "+insertNode.getDebug(tree.getLocale()));
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

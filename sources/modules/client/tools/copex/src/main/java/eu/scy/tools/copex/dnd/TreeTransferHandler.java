/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.dnd;

import eu.scy.tools.copex.edp.CopexTree;
import eu.scy.tools.copex.edp.CopexTreeNode;
import java.awt.Point;
import java.awt.datatransfer.Transferable;
import java.io.IOException;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.util.Vector;
import javax.swing.*;
import javax.swing.tree.TreePath;

/**
 * transfert de donnees pour le drag and drop
 * @author MBO
 */
public class TreeTransferHandler extends TransferHandler {

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
                        CopexTreeNode insertNode = getNodeInsert(dropLocation, subTreeToMove.getOwner(), v);
                        boolean brother = (Boolean )v.get(0);
                        boolean markNode = (Boolean)v.get(1);
                        
                        // si le noeud insertion appartient au sous arbre => on refuse le drag and drop
                        if (subTreeToMove.containNode(insertNode)){
                            return false;
                        }
                        if (subTreeToMove.isQuestion() && !insertNode.isQuestion()){
                            return false;
                        }
                        // si le noeud d'insertion ne peut avoir d'enfants => on refuse
                        if ((! brother && !insertNode.canBeParent()) || (brother && insertNode.getParent() != null && !((CopexTreeNode)insertNode.getParent()).canBeParent()) )
                            return false;
                        // si on deplace des taches qui contiennent des materials produits apres => on refuse
                        if (isProblemWithMaterialProd(insertNode, subTreeToMove))
                            return false;
                        // si on deplace des taches qui contiennent des data produits apres => on refuse
                        if (isProblemWithDataProd(insertNode, subTreeToMove))
                            return false;
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
                        
                         if ((insertNode.isAction() && insertNode.getParent() != null && ((CopexTreeNode)insertNode.getParent()).getIndex(insertNode) == ((CopexTreeNode)insertNode.getParent()).getChildCount()-1 )){
                             ((CopexTreeNode)insertNode.getParent()).setMouseover(true);
                        }
                        subTreeToMove.getOwner().revalidate();
                        subTreeToMove.getOwner().repaint();

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

    /* retourne vrai si le deplacement du sous arbre pose probleme du point de vue du material produit */
    private boolean isProblemWithMaterialProd(CopexTreeNode insertNode, SubTree subTreeToMove){
        return subTreeToMove.getOwner().isProblemWithMaterialProd(insertNode, subTreeToMove);
    }

    /* retourne vrai si le deplacement du sous arbre pose probleme du point de vue du data produit */
    private boolean isProblemWithDataProd(CopexTreeNode insertNode, SubTree subTreeToMove){
        return subTreeToMove.getOwner().isProblemWithDataProd(insertNode, subTreeToMove);
    }


    /* retourne le noeud d'insertion */
    private CopexTreeNode getNodeInsert(JTree.DropLocation dropLocation, CopexTree tree , Vector v){
        boolean brother = false;
        CopexTreeNode insertNode = null;
        TreePath tp = dropLocation.getPath();
        Point point = dropLocation.getDropPoint();
        // tp est en fait le parent ou il faut inserer => recherche index ou inserer 
        int id = dropLocation.getChildIndex();
        // Attention id =0 peut signifier qu'on insere en tant que frere ou en tant que premier enfant
        CopexTreeNode node = (CopexTreeNode)tp.getLastPathComponent();
        insertNode = node;
        if (id  > 0)
            insertNode = (CopexTreeNode)node.getChildAt(id-1);
        //System.out.println("*****insertNode : "+insertNode.getTask().getDescription());
        TreePath path = tree.getPathForLocation((int)point.getX(), (int)point.getY());
        boolean markNode = false;
//        if (path != null){
//            CopexTreeNode realNode = (CopexTreeNode)path.getLastPathComponent();
//            System.out.println("realNode : "+realNode.getTask().getDescription());
//            // cas d'ajout en fin d'une etape / ss question
//            if (!insertNode.isAction() && insertNode.getChildCount() > 0 && insertNode.getChildAt(insertNode.getChildCount() -1).equals(realNode)){
//                insertNode = (CopexTreeNode)insertNode.getChildAt(insertNode.getChildCount() -1);
//                brother = true;
//            }
//            //cas drag and drop sur dossier => ajoute a la fin
//            if (!insertNode.isAction() && realNode.equals(insertNode) && insertNode.getChildCount() > 0){
//                insertNode = (CopexTreeNode)insertNode.getChildAt(insertNode.getChildCount() -1);
//                brother = true;
//                markNode = true;
//            }
//            if(!insertNode.isAction() && !realNode.isAction() && insertNode.getTask().getDbKeyBrother() == realNode.getTask().getDbKey() && realNode.getChildCount() == 0){
//                insertNode = realNode;
//                brother = false;
//            }
//            // cas de l'insertion entre 2 freres etapes
//            CopexTreeNode parent = (CopexTreeNode)insertNode.getParent();
//            if (!insertNode.isAction() && parent != null && parent.getChildAfter(insertNode) != null && parent.getChildAfter(insertNode).equals(realNode))
//                brother = true;
//
//        }else{
//                //System.out.println("path null");
//                if (!insertNode.isAction())
//                    brother = true;
//
//        }
         if(insertNode.isAction()){
             brother = true;
         }else{
            if (path == null){
                brother = true;
            }else{
                CopexTreeNode realNode = (CopexTreeNode)path.getLastPathComponent();
                //System.out.println("realNode : "+realNode.getTask().getDescription());
                // si node =realNode => sur dossier => a la fin de node
                if(realNode.equals(insertNode)){
                    if(insertNode.getChildCount() != 0){
                        insertNode = (CopexTreeNode)insertNode.getChildAt(insertNode.getChildCount() -1);
                        brother = true;
                        markNode = true;
                    }
                }
                // si realNode = 1° enfant de node => position 0
                else if (insertNode.getChildCount() > 0 && insertNode.getChildAt(0).equals(realNode)){

                }
                // si realnode = dernier enfant => frere de node
                else if (insertNode.getChildCount() > 0 && insertNode.getChildAt(insertNode.getChildCount()-1).equals(realNode)){
                    brother = true;
                }
                // si realNode = frere de node => frere de node
                else if (insertNode.getTask().getDbKeyBrother() == realNode.getTask().getDbKey()){
                    brother = true;
                }
            }
         }
          //System.out.println("brother : "+brother);
          v.add(brother);
          v.add(markNode);
          //System.out.println("INSERTION EN "+brother+" au noeud "+insertNode.getTask().getDescription());
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
                CopexTreeNode dropNode = getNodeInsert(dropLocation, tree, v);
                boolean brother = (Boolean )v.get(0);
                boolean isOk =  tree.moveSubTree(subTreeToMove, dropNode, brother);
                tree.refreshMouseOver();
                return isOk;
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
}

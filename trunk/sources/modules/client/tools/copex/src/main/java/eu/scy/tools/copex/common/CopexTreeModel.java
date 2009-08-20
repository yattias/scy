/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.common;

import eu.scy.tools.copex.edp.CopexTreeNode;
import java.util.ArrayList;
import javax.swing.tree.*;

/**
 * represente le modele de donnees sous forme d'arbres
 * @author MBO
 */
public class CopexTreeModel extends DefaultTreeModel {

    // ATTRIBUTS
    /* protocole */
    private ExperimentalProcedure proc;
    /* liste des taches */
    private ArrayList<CopexTask> listTask;
    private int listTaskSize;
    

    
    // CONSTRUCTEURS 
    public CopexTreeModel(ExperimentalProcedure proc, ArrayList<CopexTask> listTask){
        super(new CopexTreeNode(proc.getQuestion()));
        this.proc = proc;
        this.listTask = listTask;
        this.listTaskSize = listTask.size();
        buildTree((CopexTreeNode)root, this.listTask);
        
    }

    // constructeur d'un sous arbre
    public CopexTreeModel(ExperimentalProcedure proc, ArrayList<CopexTask> listTask, CopexTask fictivTask){
        super(new CopexTreeNode(fictivTask));
        this.proc = proc;
        this.listTask = listTask;
        this.listTaskSize = listTask.size();
        buildTree((CopexTreeNode)root, this.listTask);
        
    }
    
    /* construction de l'arbre */
    private void buildTree(CopexTreeNode node, ArrayList<CopexTask> listT){
       CopexTask[] tabTask = getBrotherAndChild(node.getTask(), listT);
       CopexTask tB = tabTask[0];
       CopexTask tC = tabTask[1];
       if (tC != null){
           CopexTreeNode newChild = new CopexTreeNode(tC);
           insertNodeInto(newChild, node , node.getChildCount());
           buildTree(newChild, listT);
       }
       if (tB != null){
           CopexTreeNode newBrother = new CopexTreeNode(tB);
           insertNodeInto(newBrother, (CopexTreeNode)node.getParent() , node.getParent().getChildCount());
           buildTree(newBrother, listT);
       }
    }
    
    /* renvoit la tache frere et enfant , null sinon */
    private CopexTask[] getBrotherAndChild(CopexTask task, ArrayList<CopexTask> listT){
        int nbT = listT.size();
        CopexTask[] tabTask = new CopexTask[2];
        long dbKeyB = task.getDbKeyBrother();
        long dbKeyC = task.getDbKeyChild();
        CopexTask tB = null;
        CopexTask tC = null;
        if (dbKeyB != -1 || dbKeyC != -1){
            for (int i=0;i<nbT;i++){
                CopexTask t = listT.get(i);
                if (t.getDbKey() == dbKeyB)
                    tB = t;
                else if (t.getDbKey() == dbKeyC)
                    tC = t;
            }
        }
        tabTask[0] = tB;
        tabTask[1] = tC;
        return tabTask;
    }
    
    /* ajout de noeuds a  partir d'un noeud donne*/
    public void addNodes(CopexTreeNode node, ArrayList<CopexTask> listTask){
        buildTree(node, listTask);
    }
    
    public void updateProc(ExperimentalProcedure newProc){
        this.proc = newProc;
        this.listTask = newProc.getListTask();
        this.listTaskSize = listTask.size();
        updateNode((CopexTreeNode)root);
    }
    
    private void updateNode(CopexTreeNode node){
        int nbC = node.getChildCount();
        // recherche de la tache 
        CopexTask task = getTask(node);
        if (task != null){
            node.getTask().setDbKeyBrother(task.getDbKeyBrother());
            node.getTask().setDbKeyChild(task.getDbKeyChild());
        }
        for (int k=0; k<nbC; k++){
            updateNode((CopexTreeNode)node.getChildAt(k));
        }
    }
    
    private CopexTask getTask(CopexTreeNode node){
        for (int i=0; i<listTaskSize; i++){
            CopexTask t = listTask.get(i);
            if (t.getDbKey() == node.getTask().getDbKey())
                return t;
        }
        return null;
    }
}

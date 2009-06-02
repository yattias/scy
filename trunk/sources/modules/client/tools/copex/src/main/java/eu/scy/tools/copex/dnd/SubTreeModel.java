/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.dnd;

import eu.scy.tools.copex.common.*;
import eu.scy.tools.copex.edp.CopexTreeNode;
import java.util.ArrayList;
import javax.swing.tree.DefaultTreeModel;

/**
 * modele de données pour une selection dans un arbre
 * @author MBO
 */
public class SubTreeModel extends DefaultTreeModel {

    // ATTRIBUTS 
    /* liste des taches */
    private ArrayList<CopexTask> listTask;
    
    

    public SubTreeModel(ArrayList<CopexTask> listTask) {
        super(new CopexTreeNode(listTask.get(0)));
        this.listTask = listTask;
        buildTree((CopexTreeNode)root);
    }
    
    
    /* construction de l'arbre */
    private void buildTree(CopexTreeNode node){
       CopexTask[] tabTask = getBrotherAndChild(node.getTask());
       CopexTask tB = tabTask[0];
       CopexTask tC = tabTask[1];
       if (tC != null){
           CopexTreeNode newChild = new CopexTreeNode(tC);
           insertNodeInto(newChild, node , node.getChildCount());
           buildTree(newChild);
       }
       if (tB != null){
           CopexTreeNode newBrother = new CopexTreeNode(tB);
           insertNodeInto(newBrother, (CopexTreeNode)node.getParent() , node.getParent().getChildCount());
           buildTree(newBrother);
       }
    }
    
    /* renvoit la tache frere et enfant , null sinon */
    private CopexTask[] getBrotherAndChild(CopexTask task){
        CopexTask[] tabTask = new CopexTask[2];
        long dbKeyB = task.getDbKeyBrother();
        long dbKeyC = task.getDbKeyChild();
        CopexTask tB = null;
        CopexTask tC = null;
        if (dbKeyB != -1 || dbKeyC != -1){
            int listTaskSize = this.listTask.size();
            for (int i=0;i<listTaskSize;i++){
                CopexTask t = listTask.get(i);
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
    
}

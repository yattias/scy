/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.dnd;

import eu.scy.client.tools.copex.common.*;
import eu.scy.client.tools.copex.edp.TaskTreeNode;
import java.util.ArrayList;
import javax.swing.tree.DefaultTreeModel;

/**
 * modele de donnees pour une selection dans un arbre
 * @author MBO
 */
public class SubTreeModel extends DefaultTreeModel {

    // ATTRIBUTS 
    /* liste des taches */
    private ArrayList<CopexTask> listTask;
    
    

    public SubTreeModel(ArrayList<CopexTask> listTask) {
        super(new TaskTreeNode(listTask.get(0)));
        this.listTask = listTask;
        buildTree((TaskTreeNode)root);
    }
    
    
    /* construction de l'arbre */
    private void buildTree(TaskTreeNode node){
       CopexTask[] tabTask = getBrotherAndChild(node.getTask());
       CopexTask tB = tabTask[0];
       CopexTask tC = tabTask[1];
       if (tC != null){
           TaskTreeNode newChild = new TaskTreeNode(tC);
           insertNodeInto(newChild, node , node.getChildCount());
           buildTree(newChild);
       }
       if (tB != null){
           TaskTreeNode newBrother = new TaskTreeNode(tB);
           insertNodeInto(newBrother, (TaskTreeNode)node.getParent() , node.getParent().getChildCount());
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

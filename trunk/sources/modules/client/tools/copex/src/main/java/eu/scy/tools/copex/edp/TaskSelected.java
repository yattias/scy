/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.edp;

import eu.scy.tools.copex.common.*;
import java.util.ArrayList;

/**
 * represente une selection simple
 * @author MBO
 */
public class TaskSelected {

    // ATTRIBUTS
    /* protocole */
    private LearnerProcedure proc;
    /* tache selectionnée */
    private CopexTask taskSelected;
    /* tache frere */
    private CopexTask taskBrother;
    /* tache parent */
    private CopexTask taskParent;
    /* tache grand frere */
    private CopexTask taskOldBrother;
    /* liste de tous les enfants - sur tous les niveaux */
    private ArrayList<CopexTask> listAllChildren;
    /* tache parent */
    private CopexTask parentTask;
    // en graphique 
    /* noeud selectionne */
    private CopexTreeNode selectedNode;
    /* noeud frere */
    private CopexTreeNode brotherNode;
    /* noeud parent */
    private CopexTreeNode parentNode;
    /* noeud grand frere */
    private CopexTreeNode oldBrotherNode;
    /* dernier noeud frere */
    private CopexTreeNode lastBrotherNode;
    /*grand frere eventuel */
    private CopexTask oldBrother ;

    // CONSTRUCTEURS 
    public TaskSelected(LearnerProcedure proc, CopexTask selectedTask, CopexTask taskBrother, CopexTask taskParent, CopexTask taskOldBrother, CopexTreeNode selectedNode, CopexTreeNode brotherNode, CopexTreeNode parentNode, CopexTreeNode oldBrotherNode, ArrayList<CopexTask> listAllChildren, CopexTask parentTask, CopexTreeNode lastBrotherNode, CopexTask oldBrother) {
        this.proc = proc;
        this.taskSelected = selectedTask;
        this.taskBrother = taskBrother;
        this.taskParent = taskParent;
        this.taskOldBrother = taskOldBrother;
        this.selectedNode = selectedNode;
        this.brotherNode = brotherNode;
        this.parentNode = parentNode;
        this.oldBrotherNode = oldBrotherNode;
        this.listAllChildren = listAllChildren;
        this.parentTask = parentTask;
        this.lastBrotherNode = lastBrotherNode;
        this.oldBrother = oldBrother ;
    }

    public void setSelectedNode(CopexTreeNode selectedNode) {
        this.selectedNode = selectedNode;
    }
    
    // METHODES
    
    /* retourne la  tache selectionnee si selection simple, null sinon*/
     public CopexTask getSelectedTask(){
         return this.taskSelected;
     }

    public CopexTreeNode getLastBrotherNode() {
        return lastBrotherNode;
    }
     
     /* retourne la  noeud selectionne si selection simple, null sinon*/
     public CopexTreeNode getSelectedNode(){
         return this.selectedNode;
     }

    
     // GETTER AND SETTER
    public LearnerProcedure getProc() {
        return proc;
    }
    /* retourne la tache frere */ 
     public CopexTask getTaskBrother(){
         return this.taskBrother;
     }
     /* retourne la tache parent */ 
     public CopexTask getTaskParent(){
         return this.taskParent;
     }

    public CopexTreeNode getBrotherNode() {
        return brotherNode;
    }

    public CopexTreeNode getParentNode() {
        return parentNode;
    }

    public CopexTask getTaskOldBrother() {
        return taskOldBrother;
    }

    public ArrayList<CopexTask> getListAllChildren() {
        return listAllChildren;
    }

    public CopexTask getParentTask() {
        return parentTask;
    }
    
    // retourne la tache à laquelle   on s'attache
    public CopexTask getTaskToAttach(){
       return oldBrother == null ? parentTask : oldBrother;
    }
    
    // retourne vrai si on s'attaache en frere
    public boolean attachLikeBrother(){
        
        return oldBrother != null;
    }

    @Override
    public String toString() {
        String s = "tache sel : "+this.taskSelected.getDescription()+"("+this.taskSelected.getDbKey()+")\n";
        s += "tache parent : "+parentTask.getDescription()+" ("+parentTask.getDbKey()+")\n";
        s += "tache frere : ";
        if (this.taskBrother == null)
            s += " null \n";
        else
            s += taskBrother.getDescription()+" ("+taskBrother.getDbKey()+")\n";
        return s;
        
    }
    
}

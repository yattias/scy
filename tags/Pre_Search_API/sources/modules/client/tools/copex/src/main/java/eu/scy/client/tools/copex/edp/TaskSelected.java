/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.edp;

import eu.scy.client.tools.copex.common.*;
import java.util.ArrayList;

/**
 * represente une selection simple
 * @author Marjolaine
 */
public class TaskSelected {

    /* protocole */
    private ExperimentalProcedure proc;
    /* tache selectionnee */
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
    private CopexNode selectedNode;
    /* noeud frere */
    private CopexNode brotherNode;
    /* noeud parent */
    private CopexNode parentNode;
    /* noeud grand frere */
    private CopexNode oldBrotherNode;
    /* dernier noeud frere */
    private CopexNode lastBrotherNode;
    /*grand frere eventuel */
    private CopexTask oldBrother ;

    // CONSTRUCTEURS 
    public TaskSelected(ExperimentalProcedure proc, CopexTask selectedTask, CopexTask taskBrother, CopexTask taskParent, CopexTask taskOldBrother, CopexNode selectedNode, CopexNode brotherNode, CopexNode parentNode, CopexNode oldBrotherNode, ArrayList<CopexTask> listAllChildren, CopexTask parentTask, CopexNode lastBrotherNode, CopexTask oldBrother) {
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

    public void setSelectedNode(CopexNode selectedNode) {
        this.selectedNode = selectedNode;
    }
    
    
    /* retourne la  tache selectionnee si selection simple, null sinon*/
     public CopexTask getSelectedTask(){
         return this.taskSelected;
     }

    public CopexNode getLastBrotherNode() {
        return lastBrotherNode;
    }
     
     /* retourne la  noeud selectionne si selection simple, null sinon*/
     public CopexNode getSelectedNode(){
         return this.selectedNode;
     }

    
    public ExperimentalProcedure getProc() {
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

    public CopexNode getBrotherNode() {
        return brotherNode;
    }

    public CopexNode getParentNode() {
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
    
    // retourne la tache a laquelle   on s'attache
    public CopexTask getTaskToAttach(){
       return oldBrother == null ? parentTask : oldBrother;
    }
    
    // retourne vrai si on s'attaache en frere
    public boolean attachLikeBrother(){
        return oldBrother != null;
    }

    public String toString(EdPPanel edP) {
        String s = "tache sel : "+this.taskSelected.getDescription(edP.getLocale())+"("+this.taskSelected.getDbKey()+")\n";
        s += "tache parent : "+parentTask.getDescription(edP.getLocale())+" ("+parentTask.getDbKey()+")\n";
        s += "tache frere : ";
        if (this.taskBrother == null)
            s += " null \n";
        else
            s += taskBrother.getDescription(edP.getLocale())+" ("+taskBrother.getDbKey()+")\n";
        return s;
        
    }
    
}

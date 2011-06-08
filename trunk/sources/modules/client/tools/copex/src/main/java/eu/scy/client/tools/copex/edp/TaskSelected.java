/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.edp;

import eu.scy.client.tools.copex.common.*;
import java.util.ArrayList;

/**
 * selected task
 * @author Marjolaine
 */
public class TaskSelected {

    /* proc */
    private ExperimentalProcedure proc;
    /* selected task */
    private CopexTask taskSelected;
    /* brother task */
    private CopexTask taskBrother;
    /* parent task */
    private CopexTask taskParent;
    /* old brother task */
    private CopexTask taskOldBrother;
    /* list of all childs - all levels  */
    private ArrayList<CopexTask> listAllChildren;
    /* parent task */
    private CopexTask parentTask;
    /* selected node */
    private CopexNode selectedNode;
    /* brother node */
    private CopexNode brotherNode;
    /* parent node  */
    private CopexNode parentNode;
    /* old brother node */
    private CopexNode oldBrotherNode;
    /* last brother node */
    private CopexNode lastBrotherNode;
    /* old brother node, if exists */
    private CopexTask oldBrother ;

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
    
    
    /* returns the selected task, if simple selection, null otherwise*/
     public CopexTask getSelectedTask(){
         return this.taskSelected;
     }

    public CopexNode getLastBrotherNode() {
        return lastBrotherNode;
    }
     
     /* returns the selected node, if simple selection, null otherwise*/
     public CopexNode getSelectedNode(){
         return this.selectedNode;
     }

    
    public ExperimentalProcedure getProc() {
        return proc;
    }

    /* returns the brother task*/
     public CopexTask getTaskBrother(){
         return this.taskBrother;
     }

     /*returns the parent task */
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
    
    // returns the task to attach
    public CopexTask getTaskToAttach(){
       return oldBrother == null ? parentTask : oldBrother;
    }
    
    // returns true if connect as brother
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

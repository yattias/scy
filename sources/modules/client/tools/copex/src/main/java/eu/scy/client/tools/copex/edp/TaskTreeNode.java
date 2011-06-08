/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.edp;



import eu.scy.client.tools.copex.common.CopexAction;
import eu.scy.client.tools.copex.common.CopexTask;
import eu.scy.client.tools.copex.common.Question;
import eu.scy.client.tools.copex.common.Step;
import eu.scy.client.tools.copex.utilities.MyConstants;

/**
 * represents a node in the tree, could be :
 * - question o
 * - step
 * - action
 * @author Marjolaine
 */
public class TaskTreeNode extends CopexNode {
    /*task*/
    private CopexTask task;
    /*indicates the mouseover of the mouse :)during the drag and drop  */
    private boolean mouseover;

    public TaskTreeNode(CopexTask task) {
        super(task);
        this.task = task;
        this.mouseover = false;
    }

    @Override
    public CopexTask getTask() {
        return task;
    }

    public void setTask(CopexTask task) {
        this.task = task;
    }

    public boolean isMouseover() {
        return mouseover;
    }

    @Override
    public void setMouseover(boolean mouseover) {
        if(!getTask().isAction())
            this.mouseover = mouseover;
    }

    

    /* returns true if readonly */
    @Override
    public boolean  isDisabled(){
        return !canEdit() || !canDelete() || !canCopy() || !canMove() || !canBeParent() ;
    }
    
    /* returns true if the node can be edited*/
    @Override
    public boolean canEdit(){
        return this.getTask().getEditRight() == MyConstants.EXECUTE_RIGHT;
    }

     /* returns true if the node can be deleted */
    @Override
    public boolean canDelete(){
        return this.getTask().getDeleteRight() == MyConstants.EXECUTE_RIGHT;
    }

     /* returns true if the node can be copeid  */
    @Override
    public boolean canCopy(){
        return this.getTask().getCopyRight() == MyConstants.EXECUTE_RIGHT;
    }

     /* returns true if the node can be mode */
    @Override
    public boolean canMove(){
        return this.getTask().getMoveRight() == MyConstants.EXECUTE_RIGHT;
    }

     /* returns true if the node can be parent*/
    @Override
    public boolean canBeParent(){
        return this.getTask().getParentRight() == MyConstants.EXECUTE_RIGHT;
    }

    @Override
    public boolean isQuestion(){
        return (this.getTask() instanceof Question);
    }

     public boolean isStep(){
        return (this.getTask() instanceof Step);
    }

     /* returns true if the step has no child */
     public boolean isStepAlone(){
        return (this.getTask() instanceof Step) && this.getChildCount() == 0 ;
    }
     
     @Override
     public boolean isAction(){
        return (this.getTask() instanceof CopexAction);
    }
}

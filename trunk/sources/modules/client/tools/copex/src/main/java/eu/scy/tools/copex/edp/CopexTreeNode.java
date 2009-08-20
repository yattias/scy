/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.edp;



import eu.scy.tools.copex.common.CopexAction;
import eu.scy.tools.copex.common.CopexTask;
import eu.scy.tools.copex.common.Question;
import eu.scy.tools.copex.common.Step;
import eu.scy.tools.copex.utilities.MyConstants;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * represente un noeud dans l'arbre : elle peut etre de nature diffenrente : 
 * - question ou sous question
 * - etape 
 * - action
 * @author MBO
 */
public class CopexTreeNode extends DefaultMutableTreeNode {
    // PROPERTY
    /*tache associee */
    private CopexTask task;
    /*boolean qui indique passage souris lors du drag and drop */
    private boolean mouseover;

    // CONSTRUCTOR
    public CopexTreeNode(CopexTask task) {
        super(task);
        this.task = task;
        this.mouseover = false;
    }

    // GETTER AND SETTER
    public CopexTask getTask() {
        return task;
    }

    public void setTask(CopexTask task) {
        this.task = task;
    }

    public boolean isMouseover() {
        return mouseover;
    }

    public void setMouseover(boolean mouseover) {
        if(!getTask().isAction())
            this.mouseover = mouseover;
    }

    

    // METHOD
    /* retourne vrai si en lecture seule */
    public boolean  isDisabled(){
        return !canEdit() || !canDelete() || !canCopy() || !canMove() || !canBeParent() ;
    }
    
    /* retourne vrai si peut etre edite */
    public boolean canEdit(){
        return this.getTask().getEditRight() == MyConstants.EXECUTE_RIGHT;
    }
     /* retourne vrai si peut etre supprime */
    public boolean canDelete(){
        return this.getTask().getDeleteRight() == MyConstants.EXECUTE_RIGHT;
    }
     /* retourne vrai si peut etre copiee */
    public boolean canCopy(){
        return this.getTask().getCopyRight() == MyConstants.EXECUTE_RIGHT;
    }
     /* retourne vrai si peut etre move */
    public boolean canMove(){
        return this.getTask().getMoveRight() == MyConstants.EXECUTE_RIGHT;
    }
     /* retourne vrai si peut etre parent */
    public boolean canBeParent(){
        return this.getTask().getParentRight() == MyConstants.EXECUTE_RIGHT;
    }
    public boolean isQuestion(){
        return (this.getTask() instanceof Question);
    }
     public boolean isStep(){
        return (this.getTask() instanceof Step);
    }
     /* retourne vrai si c'est une etape vide */
     public boolean isStepAlone(){
        return (this.getTask() instanceof Step) && this.getChildCount() == 0 ;
    }
     public boolean isAction(){
        return (this.getTask() instanceof CopexAction);
    }
}

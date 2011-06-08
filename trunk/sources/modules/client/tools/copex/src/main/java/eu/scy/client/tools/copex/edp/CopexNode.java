/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.edp;

import eu.scy.client.tools.copex.common.CopexTask;
import eu.scy.client.tools.copex.common.DataSheet;
import eu.scy.client.tools.copex.common.Evaluation;
import eu.scy.client.tools.copex.common.GeneralPrinciple;
import eu.scy.client.tools.copex.common.Hypothesis;
import eu.scy.client.tools.copex.common.Manipulation;
import eu.scy.client.tools.copex.common.MaterialProc;
import eu.scy.client.tools.copex.common.Question;
import eu.scy.client.tools.copex.utilities.MyConstants;
import java.util.Locale;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * node of the main tree (copex tree)
 * @author Marjolaine
 */
public class CopexNode extends DefaultMutableTreeNode {
    private Object node;

    public CopexNode(Object node) {
        this.node = node;
    }

    public Object getNode() {
        return node;
    }

    public void setNode(Object node) {
        this.node = node;
    }

    public boolean isQuestion(){
        return node instanceof Question;
    }

    public boolean isHypothesis(){
        return node instanceof Hypothesis;
    }

    public boolean isGeneralPrinciple(){
        return node instanceof GeneralPrinciple;
    }

    public boolean isMaterial(){
        return node instanceof MaterialProc;
    }
    public boolean isManipulation(){
        return node instanceof Manipulation;
    }

    public boolean isDatasheet(){
        return node instanceof DataSheet;
    }
    public boolean isEvaluation(){
        return node instanceof Evaluation;
    }

    public CopexTask getTask(){
        if(isQuestion())
            return (Question)getNode();
        else if (this instanceof TaskTreeNode){
            ((TaskTreeNode)this).getTask();
        }
        return null;
    }

    public boolean canMove(){
        if(this instanceof TaskTreeNode)
            return ((TaskTreeNode)this).canMove();
        else
            return false;
    }

    public boolean canCopy(){
        return false;
    }

    public boolean canDelete(){
        return false;
    }

    public boolean isDisabled(){
        return true;
    }

    public boolean isAction(){
        return false;
    }
    public void setMouseover(boolean mouseover) {
    }

    /* returns true if it can be parent */
    public boolean canBeParent(){
        CopexTask task = getTask();
        if(task == null)
            return false;
        else
            return task.getParentRight() == MyConstants.EXECUTE_RIGHT;
    }

    /* returns true if the node can be edited */
    public boolean canEdit(){
        CopexTask task = getTask();
        if(task == null)
            return false;
        else
            return task.getEditRight() == MyConstants.EXECUTE_RIGHT;
    }

    public String getDebug(Locale locale){
        //locale = new Locale("en", "GB");
        if(isQuestion())
            return "question "+getTask().getDescription(locale);
        else if(isHypothesis())
            return "hypothesis";
        else if (isGeneralPrinciple())
            return "principe";
        else if(isMaterial())
            return "material";
        else if(isManipulation())
            return "manipulation";
        else if(isDatasheet())
            return "datasheet";
        else if (isEvaluation())
            return "evaluation";
        else return getTask().getDescription(locale);
    }
}

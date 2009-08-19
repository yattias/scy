/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.common;

/**
 *
 * @author Marjolaine
 */
public class TaskRepeatValueParamQuantity extends TaskRepeatValue{
    /* action param quantity */
    private ActionParamQuantity actionParamQuantity;

    public TaskRepeatValueParamQuantity(long dbKey, int noRepeat, ActionParamQuantity actionParamQuantity) {
        super(dbKey, noRepeat);
        this.actionParamQuantity = actionParamQuantity;
    }

    public ActionParamQuantity getActionParamQuantity() {
        return actionParamQuantity;
    }

    public void setActionParamQuantity(ActionParamQuantity actionParamQuantity) {
        this.actionParamQuantity = actionParamQuantity;
    }
    // OVERRIDE
    @Override
    public Object clone() {
        TaskRepeatValueParamQuantity v = (TaskRepeatValueParamQuantity) super.clone() ;

        v.setActionParamQuantity((ActionParamQuantity)this.actionParamQuantity.clone());
        return v;
    }
}

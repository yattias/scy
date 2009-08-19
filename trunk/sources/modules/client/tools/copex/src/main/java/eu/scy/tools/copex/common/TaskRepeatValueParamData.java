/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.common;

/**
 *
 * @author Marjolaine
 */
public class TaskRepeatValueParamData extends TaskRepeatValue{
    /* action param data */
    private ActionParamData actionParamData;

    public TaskRepeatValueParamData(long dbKey, int noRepeat, ActionParamData actionParamData) {
        super(dbKey, noRepeat);
        this.actionParamData = actionParamData;
    }

    public ActionParamData getActionParamData() {
        return actionParamData;
    }

    public void setActionParamData(ActionParamData actionParamData) {
        this.actionParamData = actionParamData;
    }
    // OVERRIDE
    @Override
    public Object clone() {
        TaskRepeatValueParamData v = (TaskRepeatValueParamData) super.clone() ;

        v.setActionParamData((ActionParamData)this.actionParamData.clone());
        return v;
    }
}

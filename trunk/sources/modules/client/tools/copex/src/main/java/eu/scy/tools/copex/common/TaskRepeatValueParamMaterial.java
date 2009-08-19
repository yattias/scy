/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.common;

/**
 *
 * @author Marjolaine
 */
public class TaskRepeatValueParamMaterial extends TaskRepeatValue{
    /* action param material */
    private ActionParamMaterial actionParamMaterial;

    public TaskRepeatValueParamMaterial(long dbKey, int noRepeat, ActionParamMaterial actionParamMaterial) {
        super(dbKey, noRepeat);
        this.actionParamMaterial = actionParamMaterial;
    }

    public ActionParamMaterial getActionParamMaterial() {
        return actionParamMaterial;
    }

    public void setActionParamMaterial(ActionParamMaterial actionParamMaterial) {
        this.actionParamMaterial = actionParamMaterial;
    }
    // OVERRIDE
    @Override
    public Object clone() {
        TaskRepeatValueParamMaterial v = (TaskRepeatValueParamMaterial) super.clone() ;

        v.setActionParamMaterial((ActionParamMaterial)this.actionParamMaterial.clone());
        return v;
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.common;

import java.util.ArrayList;

/**
 *
 * @author Marjolaine
 */
public class TaskRepeatParamMaterial extends TaskRepeatParam{
    /* initial param material */
    private InitialParamMaterial initialParamMaterial;
    private ArrayList<TaskRepeatValueParamMaterial> listValue;

    public TaskRepeatParamMaterial(long dbKey, InitialParamMaterial initialParamMaterial, ArrayList<TaskRepeatValueParamMaterial> listValue) {
        super(dbKey);
        this.initialParamMaterial = initialParamMaterial;
        this.listValue = listValue ;
    }

    public InitialParamMaterial getInitialParamMaterial() {
        return initialParamMaterial;
    }

    public void setInitialParamMaterial(InitialParamMaterial initialParamMaterial) {
        this.initialParamMaterial = initialParamMaterial;
    }

    public ArrayList<TaskRepeatValueParamMaterial> getListValue() {
        return listValue;
    }

    public void setListValue(ArrayList<TaskRepeatValueParamMaterial> listValue) {
        this.listValue = listValue;
    }

    // OVERRIDE
    @Override
    public Object clone() {
        TaskRepeatParamMaterial p = (TaskRepeatParamMaterial) super.clone() ;

        p.setInitialParamMaterial((InitialParamMaterial)initialParamMaterial.clone());
        ArrayList<TaskRepeatValueParamMaterial> list = new ArrayList();
        for (int i=0; i<this.listValue.size(); i++){
            list.add((TaskRepeatValueParamMaterial)this.listValue.get(i).clone());
        }
        p.setListValue(list);
        return p;
    }

}

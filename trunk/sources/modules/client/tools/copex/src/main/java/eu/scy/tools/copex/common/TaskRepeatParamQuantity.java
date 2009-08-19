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
public class TaskRepeatParamQuantity extends TaskRepeatParam {
    /* param quantity */
    private InitialParamQuantity initialParamQuantity;
    private ArrayList<TaskRepeatValueParamQuantity> listValue;

    public TaskRepeatParamQuantity(long dbKey, InitialParamQuantity initialParamQuantity, ArrayList<TaskRepeatValueParamQuantity> listValue) {
        super(dbKey);
        this.initialParamQuantity = initialParamQuantity;
        this.listValue = listValue;
    }

    public InitialParamQuantity getInitialParamQuantity() {
        return initialParamQuantity;
    }

    public void setInitialParamQuantity(InitialParamQuantity initialParamQuantity) {
        this.initialParamQuantity = initialParamQuantity;
    }

    public ArrayList<TaskRepeatValueParamQuantity> getListValue() {
        return listValue;
    }

    public void setListValue(ArrayList<TaskRepeatValueParamQuantity> listValue) {
        this.listValue = listValue;
    }

    // OVERRIDE
    @Override
    public Object clone() {
        TaskRepeatParamQuantity p = (TaskRepeatParamQuantity) super.clone() ;

        p.setInitialParamQuantity((InitialParamQuantity)initialParamQuantity.clone());
        ArrayList<TaskRepeatValueParamQuantity> list = new ArrayList();
        for (int i=0; i<this.listValue.size(); i++){
            list.add((TaskRepeatValueParamQuantity)this.listValue.get(i).clone());
        }
        p.setListValue(list);
        return p;
    }

}

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
public class TaskRepeatParamData extends TaskRepeatParam{
    /*initial param data */
    private InitialParamData initialParamData;
    /* liste des valeurs */
    private ArrayList<TaskRepeatValueParamData> listValue;

    public TaskRepeatParamData(long dbKey, InitialParamData initialParamData, ArrayList<TaskRepeatValueParamData> listValue) {
        super(dbKey);
        this.initialParamData = initialParamData;
        this.listValue = listValue ;
    }

    public InitialParamData getInitialParamData() {
        return initialParamData;
    }

    public void setInitialParamData(InitialParamData initialParamData) {
        this.initialParamData = initialParamData;
    }

    public ArrayList<TaskRepeatValueParamData> getListValue() {
        return listValue;
    }

    public void setListValue(ArrayList<TaskRepeatValueParamData> listValue) {
        this.listValue = listValue;
    }

    // OVERRIDE
    @Override
    public Object clone() {
        TaskRepeatParamData p = (TaskRepeatParamData) super.clone() ;

        p.setInitialParamData((InitialParamData)this.initialParamData.clone());
        ArrayList<TaskRepeatValueParamData> list = new ArrayList();
        for (int i=0; i<this.listValue.size(); i++){
            list.add((TaskRepeatValueParamData)this.listValue.get(i).clone());
        }
        p.setListValue(list);
        return p;
    }

}

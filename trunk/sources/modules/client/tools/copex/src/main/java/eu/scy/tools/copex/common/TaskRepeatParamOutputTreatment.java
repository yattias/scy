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
public class TaskRepeatParamOutputTreatment extends TaskRepeatParam {
    /* initial output tretment */
    private InitialTreatmentOutput output ;
    private ArrayList<TaskRepeatValueDataProd> listValue;

    public TaskRepeatParamOutputTreatment(long dbKey, InitialTreatmentOutput output, ArrayList<TaskRepeatValueDataProd> listValue) {
        super(dbKey);
        this.output = output;
        this.listValue = listValue ;
    }

    public InitialTreatmentOutput getOutput() {
        return output;
    }

    public void setOutput(InitialTreatmentOutput output) {
        this.output = output;
    }

    public ArrayList<TaskRepeatValueDataProd> getListValue() {
        return listValue;
    }

    public void setListValue(ArrayList<TaskRepeatValueDataProd> listValue) {
        this.listValue = listValue;
    }

    // OVERRIDE
    @Override
    public Object clone() {
        TaskRepeatParamOutputTreatment p = (TaskRepeatParamOutputTreatment) super.clone() ;

        p.setOutput((InitialTreatmentOutput)output.clone());
        ArrayList<TaskRepeatValueDataProd> list = new ArrayList();
        for (int i=0; i<this.listValue.size(); i++){
            list.add((TaskRepeatValueDataProd)this.listValue.get(i).clone());
        }
        p.setListValue(list);
        return p;
    }
}

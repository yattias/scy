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
public class TaskRepeatParamOutputAcquisition extends TaskRepeatParam {
    /* output acquisition*/
    private InitialAcquisitionOutput output;
    private ArrayList<TaskRepeatValueDataProd> listValue;

    public TaskRepeatParamOutputAcquisition(long dbKey, InitialAcquisitionOutput output, ArrayList<TaskRepeatValueDataProd> listValue) {
        super(dbKey);
        this.output = output;
        this.listValue = listValue ;
    }

    public InitialAcquisitionOutput getOutput() {
        return output;
    }

    public void setOutput(InitialAcquisitionOutput output) {
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
        TaskRepeatParamOutputAcquisition p = (TaskRepeatParamOutputAcquisition) super.clone() ;

        p.setOutput((InitialAcquisitionOutput)output.clone());
        ArrayList<TaskRepeatValueDataProd> list = new ArrayList();
        for (int i=0; i<this.listValue.size(); i++){
            list.add((TaskRepeatValueDataProd)this.listValue.get(i).clone());
        }
        p.setListValue(list);
        return p;
    }
}

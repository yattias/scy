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
public class TaskRepeatParamOutputManipulation extends TaskRepeatParam {
    /* output manipulation */
    private InitialManipulationOutput output;
    private ArrayList<TaskRepeatValueMaterialProd> listValue;

    public TaskRepeatParamOutputManipulation(long dbKey, InitialManipulationOutput output, ArrayList<TaskRepeatValueMaterialProd> listValue) {
        super(dbKey);
        this.output = output;
        this.listValue = listValue ;
    }

    public InitialManipulationOutput getOutput() {
        return output;
    }

    public void setOutput(InitialManipulationOutput output) {
        this.output = output;
    }

    public ArrayList<TaskRepeatValueMaterialProd> getListValue() {
        return listValue;
    }

    public void setListValue(ArrayList<TaskRepeatValueMaterialProd> listValue) {
        this.listValue = listValue;
    }

    // OVERRIDE
    @Override
    public Object clone() {
        TaskRepeatParamOutputManipulation p = (TaskRepeatParamOutputManipulation) super.clone() ;

        p.setOutput((InitialManipulationOutput)output.clone());
        ArrayList<TaskRepeatValueMaterialProd> list = new ArrayList();
        for (int i=0; i<this.listValue.size(); i++){
            list.add((TaskRepeatValueMaterialProd)this.listValue.get(i).clone());
        }
        p.setListValue(list);
        return p;
    }

}

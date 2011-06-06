/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.common;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.jdom.Element;
import org.jdom.JDOMException;

/**
 *
 * @author Marjolaine
 */
public class TaskRepeatParamOutputTreatment extends TaskRepeatParam {
    public final static String TAG_TASK_REPEAT_PARAM_OUTPUT_TREATMENT = "task_repeat_param_output_treatment";
    /* initial output tretment */
    private InitialTreatmentOutput output ;
    private ArrayList<TaskRepeatValueDataProd> listValue;

    public TaskRepeatParamOutputTreatment(long dbKey, long dbKeyActionParam, InitialTreatmentOutput output, ArrayList<TaskRepeatValueDataProd> listValue) {
        super(dbKey, dbKeyActionParam);
        this.output = output;
        this.listValue = listValue ;
    }

    public TaskRepeatParamOutputTreatment(Element xmlElem, long idParam, List<InitialTreatmentOutput> listInitialTreatmentOutput, long idValue,  long idQuantity, List<PhysicalQuantity> listPhysicalQuantity) throws JDOMException {
        super(xmlElem);
        if (xmlElem.getName().equals(TAG_TASK_REPEAT_PARAM_OUTPUT_TREATMENT)) {
            dbKey = idParam++;
            dbKeyActionParam = -1;
            if(xmlElem.getChild(TaskRepeatParam.TAG_TASK_REPEAT_PARAM_ACTION) != null){
                try{
                    dbKeyActionParam = Long.parseLong(xmlElem.getChild(TaskRepeatParam.TAG_TASK_REPEAT_PARAM_ACTION).getText());
                }catch (NumberFormatException e){

                }
            }
            output = new InitialTreatmentOutput(xmlElem.getChild(InitialTreatmentOutput.TAG_INITIAL_OUTPUT_TREATMENT_REF), listInitialTreatmentOutput);
            listValue = new ArrayList();
            for(Iterator<Element> variableElem = xmlElem.getChildren(TaskRepeatValueDataProd.TAG_TASK_REPEAT_VALUE_DATA_PROD).iterator(); variableElem.hasNext();){
                Element e = variableElem.next();
                TaskRepeatValueDataProd v = new TaskRepeatValueDataProd(e, idValue++,  idQuantity++, listPhysicalQuantity);
                listValue.add(v);
            }
		} else {
			throw(new JDOMException("Task repeat  param output treatment  expects <"+TAG_TASK_REPEAT_PARAM_OUTPUT_TREATMENT+"> as root element, but found <"+xmlElem.getName()+">."));
		}
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

    @Override
    public Element toXML(){
        Element element = new Element(TAG_TASK_REPEAT_PARAM_OUTPUT_TREATMENT);
        element.addContent(output.toXMLRef());
        if(dbKeyActionParam != -1){
            element.addContent(new Element(TaskRepeatParam.TAG_TASK_REPEAT_PARAM_ACTION).setText(""+dbKeyActionParam));
        }
        for(Iterator<TaskRepeatValueDataProd> v = listValue.iterator();v.hasNext();){
            element.addContent(v.next().toXML());
        }
        return element;
    }
}

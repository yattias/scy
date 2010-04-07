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
public class TaskRepeatParamOutputAcquisition extends TaskRepeatParam {
    public final static String TAG_TASK_REPEAT_PARAM_OUTPUT_ACQUISITION = "task_repeat_param_output_acquisition";
    /* output acquisition*/
    private InitialAcquisitionOutput output;
    private ArrayList<TaskRepeatValueDataProd> listValue;

    public TaskRepeatParamOutputAcquisition(long dbKey, InitialAcquisitionOutput output, ArrayList<TaskRepeatValueDataProd> listValue) {
        super(dbKey);
        this.output = output;
        this.listValue = listValue ;
    }

    public TaskRepeatParamOutputAcquisition(Element xmlElem, long idParam, List<InitialAcquisitionOutput> listInitialAcquisitionOutput, long idValue,  long idQuantity, List<PhysicalQuantity> listPhysicalQuantity) throws JDOMException {
        super(xmlElem);
        if (xmlElem.getName().equals(TAG_TASK_REPEAT_PARAM_OUTPUT_ACQUISITION)) {
            dbKey = idParam++;
            output = new InitialAcquisitionOutput(xmlElem.getChild(InitialAcquisitionOutput.TAG_INITIAL_OUTPUT_ACQUISITION_REF), listInitialAcquisitionOutput);
            listValue = new ArrayList();
            for(Iterator<Element> variableElem = xmlElem.getChildren(TaskRepeatValueDataProd.TAG_TASK_REPEAT_VALUE_DATA_PROD).iterator(); variableElem.hasNext();){
                Element e = variableElem.next();
                TaskRepeatValueDataProd v = new TaskRepeatValueDataProd(e, idValue++,  idQuantity++, listPhysicalQuantity);
                listValue.add(v);
            }
		} else {
			throw(new JDOMException("Task repeat  param output acquisition expects <"+TAG_TASK_REPEAT_PARAM_OUTPUT_ACQUISITION+"> as root element, but found <"+xmlElem.getName()+">."));
		}
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

    @Override
    public Element toXML(){
        Element element = new Element(TAG_TASK_REPEAT_PARAM_OUTPUT_ACQUISITION);
        element.addContent(output.toXMLRef());
        for(Iterator<TaskRepeatValueDataProd> v = listValue.iterator();v.hasNext();){
            element.addContent(v.next().toXML());
        }
        return element;
    }
}

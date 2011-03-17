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
public class TaskRepeatParamOutputManipulation extends TaskRepeatParam {
    public final static String TAG_TASK_REPEAT_PARAM_OUTPUT_MANIPULATION = "task_repeat_param_output_manipulation";
    /* output manipulation */
    private InitialManipulationOutput output;
    private ArrayList<TaskRepeatValueMaterialProd> listValue;

    public TaskRepeatParamOutputManipulation(long dbKey, long dbKeyActionParam, InitialManipulationOutput output, ArrayList<TaskRepeatValueMaterialProd> listValue) {
        super(dbKey, dbKeyActionParam);
        this.output = output;
        this.listValue = listValue ;
    }

    public TaskRepeatParamOutputManipulation(Element xmlElem, long idParam, List<InitialManipulationOutput> listInitialManipulationOutput, long idValue,  long idQuantity, long idMaterial,  List<PhysicalQuantity> listPhysicalQuantity, List<TypeMaterial> listTypeMaterial) throws JDOMException {
        super(xmlElem);
        if (xmlElem.getName().equals(TAG_TASK_REPEAT_PARAM_OUTPUT_MANIPULATION)) {
            dbKey = idParam++;
            dbKeyActionParam = -1;
            if(xmlElem.getChild(TaskRepeatParam.TAG_TASK_REPEAT_PARAM_ACTION) != null){
                try{
                    dbKeyActionParam = Long.parseLong(xmlElem.getChild(TaskRepeatParam.TAG_TASK_REPEAT_PARAM_ACTION).getText());
                }catch (NumberFormatException e){

                }
            }
            output = new InitialManipulationOutput(xmlElem.getChild(InitialManipulationOutput.TAG_INITIAL_OUTPUT_MANIPULATION_REF), listInitialManipulationOutput);
            listValue = new ArrayList();
            for(Iterator<Element> variableElem = xmlElem.getChildren(TaskRepeatValueMaterialProd.TAG_TASK_REPEAT_VALUE_MATERIAL_PROD).iterator(); variableElem.hasNext();){
                Element e = variableElem.next();
                TaskRepeatValueMaterialProd v = new TaskRepeatValueMaterialProd(e, idValue++,  idMaterial++, idQuantity++, listTypeMaterial, listPhysicalQuantity);
                listValue.add(v);
            }
		} else {
			throw(new JDOMException("Task repeat  param output manipulation expects <"+TAG_TASK_REPEAT_PARAM_OUTPUT_MANIPULATION+"> as root element, but found <"+xmlElem.getName()+">."));
		}
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

    @Override
    public Element toXML(){
        Element element = new Element(TAG_TASK_REPEAT_PARAM_OUTPUT_MANIPULATION);
        element.addContent(output.toXMLRef());
        if(dbKeyActionParam != -1){
            element.addContent(new Element(TaskRepeatParam.TAG_TASK_REPEAT_PARAM_ACTION).setText(""+dbKeyActionParam));
        }
        for(Iterator<TaskRepeatValueMaterialProd> v = listValue.iterator();v.hasNext();){
            element.addContent(v.next().toXML());
        }
        return element;
    }
}

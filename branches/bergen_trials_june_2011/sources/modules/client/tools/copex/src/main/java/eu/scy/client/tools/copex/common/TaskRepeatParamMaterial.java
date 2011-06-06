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
public class TaskRepeatParamMaterial extends TaskRepeatParam{
    public final static String TAG_TASK_REPEAT_PARAM_MATERIAL = "task_repeat_param_material";
    /* initial param material */
    private InitialParamMaterial initialParamMaterial;
    private ArrayList<TaskRepeatValueParamMaterial> listValue;

    public TaskRepeatParamMaterial(long dbKey, long dbKeyActionParam,InitialParamMaterial initialParamMaterial, ArrayList<TaskRepeatValueParamMaterial> listValue) {
        super(dbKey,  dbKeyActionParam);
        this.initialParamMaterial = initialParamMaterial;
        this.listValue = listValue ;
    }

    public TaskRepeatParamMaterial(Element xmlElem, long idParam, List<InitialParamMaterial> listInitialParamMaterial, long idValue, long idActionParam, List<Material> listMaterial,  List<PhysicalQuantity> listPhysicalQuantity, List<ActionParamQuantity> listActionParamQuantity ) throws JDOMException {
        super(xmlElem);
        if (xmlElem.getName().equals(TAG_TASK_REPEAT_PARAM_MATERIAL)) {
            dbKey = idParam++;
            dbKeyActionParam = -1;
            if(xmlElem.getChild(TaskRepeatParam.TAG_TASK_REPEAT_PARAM_ACTION) != null){
                try{
                    dbKeyActionParam = Long.parseLong(xmlElem.getChild(TaskRepeatParam.TAG_TASK_REPEAT_PARAM_ACTION).getText());
                }catch (NumberFormatException e){

                }
            }
            initialParamMaterial = new InitialParamMaterial(xmlElem.getChild(InitialParamMaterial.TAG_INITIAL_PARAM_MATERIAL_REF), listInitialParamMaterial);
            listValue = new ArrayList();
            for(Iterator<Element> variableElem = xmlElem.getChildren(TaskRepeatValueParamMaterial.TAG_TASK_REPEAT_VALUE_PARAM_MATERIAL).iterator(); variableElem.hasNext();){
                Element e = variableElem.next();
                TaskRepeatValueParamMaterial v = new TaskRepeatValueParamMaterial(e, idValue++, idActionParam++, listPhysicalQuantity, listInitialParamMaterial, listMaterial, listActionParamQuantity);
                listValue.add(v);
            }
		} else {
			throw(new JDOMException("Task repeat  param material expects <"+TAG_TASK_REPEAT_PARAM_MATERIAL+"> as root element, but found <"+xmlElem.getName()+">."));
		}
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

    @Override
    public Element toXML(){
        Element element = new Element(TAG_TASK_REPEAT_PARAM_MATERIAL);
        element.addContent(initialParamMaterial.toXMLRef());
        if(dbKeyActionParam != -1){
            element.addContent(new Element(TaskRepeatParam.TAG_TASK_REPEAT_PARAM_ACTION).setText(""+dbKeyActionParam));
        }
        for(Iterator<TaskRepeatValueParamMaterial> v = listValue.iterator();v.hasNext();){
            element.addContent(v.next().toXML());
        }
        return element;
    }

}

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
public class TaskRepeatParamData extends TaskRepeatParam{
    public final static String TAG_TASK_REPEAT_PARAM_DATA = "task_repeat_param_data";
    /*initial param data */
    private InitialParamData initialParamData;
    /* values list */
    private ArrayList<TaskRepeatValueParamData> listValue;

    public TaskRepeatParamData(long dbKey, long dbKeyActionParam, InitialParamData initialParamData, ArrayList<TaskRepeatValueParamData> listValue) {
        super(dbKey, dbKeyActionParam);
        this.initialParamData = initialParamData;
        this.listValue = listValue ;
    }

    public TaskRepeatParamData(Element xmlElem, long idParam, List<InitialParamData> listInitialParamData, long idValue, long idActionParam, long idQuantity, List<PhysicalQuantity> listPhysicalQuantity) throws JDOMException {
        super(xmlElem);
        if (xmlElem.getName().equals(TAG_TASK_REPEAT_PARAM_DATA)) {
            dbKey = idParam++;
            dbKeyActionParam = -1;
            if(xmlElem.getChild(TaskRepeatParam.TAG_TASK_REPEAT_PARAM_ACTION) != null){
                try{
                    dbKeyActionParam = Long.parseLong(xmlElem.getChild(TaskRepeatParam.TAG_TASK_REPEAT_PARAM_ACTION).getText());
                }catch (NumberFormatException e){

                }
            }
            initialParamData = new InitialParamData(xmlElem.getChild(InitialParamData.TAG_INITIAL_PARAM_DATA_REF), listInitialParamData);
            listValue = new ArrayList();
            for(Iterator<Element> variableElem = xmlElem.getChildren(TaskRepeatValueParamData.TAG_TASK_REPEAT_VALUE_PARAM_DATA).iterator(); variableElem.hasNext();){
                Element e = variableElem.next();
                TaskRepeatValueParamData v = new TaskRepeatValueParamData(e, idValue++, idActionParam++, idQuantity++, listPhysicalQuantity, listInitialParamData);
                listValue.add(v);
            }
        } else {
            throw(new JDOMException("Task repeat  param data expects <"+TAG_TASK_REPEAT_PARAM_DATA+"> as root element, but found <"+xmlElem.getName()+">."));
	}
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

    @Override
    public Element toXML(){
        Element element = new Element(TAG_TASK_REPEAT_PARAM_DATA);
        element.addContent(initialParamData.toXMLRef());
        if(dbKeyActionParam != -1){
            element.addContent(new Element(TaskRepeatParam.TAG_TASK_REPEAT_PARAM_ACTION).setText(""+dbKeyActionParam));
        }
        for(Iterator<TaskRepeatValueParamData> v = listValue.iterator();v.hasNext();){
            element.addContent(v.next().toXML());
        }
        return element;
    }

}

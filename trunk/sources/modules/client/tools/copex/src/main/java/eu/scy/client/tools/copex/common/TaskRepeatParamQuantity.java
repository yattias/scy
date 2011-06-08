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
public class TaskRepeatParamQuantity extends TaskRepeatParam {
    public final static String TAG_TASK_REPEAT_PARAM_QUANTITY = "task_repeat_param_quantity";
    /* param quantity */
    private InitialParamQuantity initialParamQuantity;
    private ArrayList<TaskRepeatValueParamQuantity> listValue;

    public TaskRepeatParamQuantity(long dbKey,long dbKeyActionParam, InitialParamQuantity initialParamQuantity, ArrayList<TaskRepeatValueParamQuantity> listValue) {
        super(dbKey, dbKeyActionParam);
        this.initialParamQuantity = initialParamQuantity;
        this.listValue = listValue;
    }

    public TaskRepeatParamQuantity(Element xmlElem, long idParam, List<InitialParamQuantity> listInitialParamQuantity, long idValue, long idActionParam, long idQuantity, List<PhysicalQuantity> listPhysicalQuantity) throws JDOMException {
        super(xmlElem);
        if (xmlElem.getName().equals(TAG_TASK_REPEAT_PARAM_QUANTITY)) {
            dbKey = idParam++;
            dbKeyActionParam = -1;
            if(xmlElem.getChild(TaskRepeatParam.TAG_TASK_REPEAT_PARAM_ACTION) != null){
                try{
                    dbKeyActionParam = Long.parseLong(xmlElem.getChild(TaskRepeatParam.TAG_TASK_REPEAT_PARAM_ACTION).getText());
                }catch (NumberFormatException e){

                }
            }
            initialParamQuantity = new InitialParamQuantity(xmlElem.getChild(InitialParamQuantity.TAG_INITIAL_PARAM_QUANTITY_REF), listInitialParamQuantity);
            listValue = new ArrayList();
            for(Iterator<Element> variableElem = xmlElem.getChildren(TaskRepeatValueParamQuantity.TAG_TASK_REPEAT_VALUE_PARAM_QUANTITY).iterator(); variableElem.hasNext();){
                Element e = variableElem.next();
                TaskRepeatValueParamQuantity v = new TaskRepeatValueParamQuantity(e, idValue++, idActionParam++, idQuantity++, listPhysicalQuantity, listInitialParamQuantity);
                listValue.add(v);
            }
	} else {
            throw(new JDOMException("Task repeat  param quantity expects <"+TAG_TASK_REPEAT_PARAM_QUANTITY+"> as root element, but found <"+xmlElem.getName()+">."));
	}
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

    @Override
    public Element toXML(){
        Element element = new Element(TAG_TASK_REPEAT_PARAM_QUANTITY);
        element.addContent(initialParamQuantity.toXMLRef());
        if(dbKeyActionParam != -1){
            element.addContent(new Element(TaskRepeatParam.TAG_TASK_REPEAT_PARAM_ACTION).setText(""+dbKeyActionParam));
        }
        for(Iterator<TaskRepeatValueParamQuantity> v = listValue.iterator();v.hasNext();){
            element.addContent(v.next().toXML());
        }
        return element;
    }

}

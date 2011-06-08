/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.common;

import java.util.List;
import org.jdom.Element;
import org.jdom.JDOMException;

/**
 *
 * @author Marjolaine
 */
public class TaskRepeatValueParamData extends TaskRepeatValue{
    public final static String TAG_TASK_REPEAT_VALUE_PARAM_DATA = "task_repeat_value_param_data";
    /* action param data */
    private ActionParamData actionParamData;

    public TaskRepeatValueParamData(long dbKey, int noRepeat, ActionParamData actionParamData) {
        super(dbKey, noRepeat);
        this.actionParamData = actionParamData;
    }

    public TaskRepeatValueParamData(Element xmlElem, long idValue, long idActionParam,long idQuantity, List<PhysicalQuantity> listPhysicalQuantity, List<InitialParamData> listInitialParamData ) throws JDOMException {
        super(xmlElem);
        if (xmlElem.getName().equals(TAG_TASK_REPEAT_VALUE_PARAM_DATA)) {
            dbKey = idValue++;
            noRepeat = Integer.parseInt(xmlElem.getChild(TAG_TASK_REPEAT_NO_REPEAT).getText());
            actionParamData = new ActionParamData(xmlElem.getChild(ActionParamData.TAG_ACTION_PARAM_DATA), idActionParam++, listInitialParamData, idQuantity++, listPhysicalQuantity);
	} else {
            throw(new JDOMException("Task repeat value param data expects <"+TAG_TASK_REPEAT_VALUE_PARAM_DATA+"> as root element, but found <"+xmlElem.getName()+">."));
	}
    }

    public ActionParamData getActionParamData() {
        return actionParamData;
    }

    public void setActionParamData(ActionParamData actionParamData) {
        this.actionParamData = actionParamData;
    }
    
    @Override
    public Object clone() {
        TaskRepeatValueParamData v = (TaskRepeatValueParamData) super.clone() ;

        v.setActionParamData((ActionParamData)this.actionParamData.clone());
        return v;
    }

    @Override
    public Element toXML(){
        Element element = new Element(TAG_TASK_REPEAT_VALUE_PARAM_DATA);
        element.addContent(new Element(TAG_TASK_REPEAT_NO_REPEAT).setText(Integer.toString(noRepeat)));
        element.addContent(actionParamData.toXML());
        return element;
    }
}

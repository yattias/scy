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
public class TaskRepeatValueParamQuantity extends TaskRepeatValue{
    public final static String TAG_TASK_REPEAT_VALUE_PARAM_QUANTITY = "task_repeat_value_param_quantity";
    /* action param quantity */
    private ActionParamQuantity actionParamQuantity;

    public TaskRepeatValueParamQuantity(long dbKey, int noRepeat, ActionParamQuantity actionParamQuantity) {
        super(dbKey, noRepeat);
        this.actionParamQuantity = actionParamQuantity;
    }

    public TaskRepeatValueParamQuantity(Element xmlElem, long idValue, long idActionParam, long idQuantity, List<PhysicalQuantity> listPhysicalQuantity, List<InitialParamQuantity> listInitialParamQuantity) throws JDOMException {
        super(xmlElem);
        if (xmlElem.getName().equals(TAG_TASK_REPEAT_VALUE_PARAM_QUANTITY)) {
            dbKey = idValue++;
            noRepeat = Integer.parseInt(xmlElem.getChild(TAG_TASK_REPEAT_NO_REPEAT).getText());
            actionParamQuantity = new ActionParamQuantity(xmlElem.getChild(ActionParamQuantity.TAG_ACTION_PARAM_QUANTITY), idActionParam++, listInitialParamQuantity, idQuantity++, listPhysicalQuantity);
	} else {
            throw(new JDOMException("Task repeat value param quantity expects <"+TAG_TASK_REPEAT_VALUE_PARAM_QUANTITY+"> as root element, but found <"+xmlElem.getName()+">."));
	}
    }

    public ActionParamQuantity getActionParamQuantity() {
        return actionParamQuantity;
    }

    public void setActionParamQuantity(ActionParamQuantity actionParamQuantity) {
        this.actionParamQuantity = actionParamQuantity;
    }
    
    @Override
    public Object clone() {
        TaskRepeatValueParamQuantity v = (TaskRepeatValueParamQuantity) super.clone() ;

        v.setActionParamQuantity((ActionParamQuantity)this.actionParamQuantity.clone());
        return v;
    }

    @Override
    public Element toXML(){
        Element element = new Element(TAG_TASK_REPEAT_VALUE_PARAM_QUANTITY);
        element.addContent(new Element(TAG_TASK_REPEAT_NO_REPEAT).setText(Integer.toString(noRepeat)));
        element.addContent(actionParamQuantity.toXML());
        return element;
    }
}

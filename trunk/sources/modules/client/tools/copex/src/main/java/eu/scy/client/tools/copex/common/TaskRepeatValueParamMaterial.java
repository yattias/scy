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
public class TaskRepeatValueParamMaterial extends TaskRepeatValue{
    public final static String TAG_TASK_REPEAT_VALUE_PARAM_MATERIAL = "task_repeat_value_param_material";
    /* action param material */
    private ActionParamMaterial actionParamMaterial;

    public TaskRepeatValueParamMaterial(long dbKey, int noRepeat, ActionParamMaterial actionParamMaterial) {
        super(dbKey, noRepeat);
        this.actionParamMaterial = actionParamMaterial;
    }

    public TaskRepeatValueParamMaterial(Element xmlElem, long idValue, long idActionParam, List<PhysicalQuantity> listPhysicalQuantity, List<InitialParamMaterial> listInitialParamMaterial, List<Material> listMaterial, List<ActionParamQuantity> listActionParamQuantity ) throws JDOMException {
        super(xmlElem);
        if (xmlElem.getName().equals(TAG_TASK_REPEAT_VALUE_PARAM_MATERIAL)) {
            dbKey = idValue++;
            noRepeat = Integer.parseInt(xmlElem.getChild(TAG_TASK_REPEAT_NO_REPEAT).getText());
            actionParamMaterial = new ActionParamMaterial(xmlElem.getChild(ActionParamMaterial.TAG_ACTION_PARAM_MATERIAL), idActionParam++, listInitialParamMaterial, listMaterial, listActionParamQuantity);
	} else {
            throw(new JDOMException("Task repeat value param material expects <"+TAG_TASK_REPEAT_VALUE_PARAM_MATERIAL+"> as root element, but found <"+xmlElem.getName()+">."));
	}
    }

    public ActionParamMaterial getActionParamMaterial() {
        return actionParamMaterial;
    }

    public void setActionParamMaterial(ActionParamMaterial actionParamMaterial) {
        this.actionParamMaterial = actionParamMaterial;
    }
    
    @Override
    public Object clone() {
        TaskRepeatValueParamMaterial v = (TaskRepeatValueParamMaterial) super.clone() ;

        v.setActionParamMaterial((ActionParamMaterial)this.actionParamMaterial.clone());
        return v;
    }

    @Override
    public Element toXML(){
        Element element = new Element(TAG_TASK_REPEAT_VALUE_PARAM_MATERIAL);
        element.addContent(new Element(TAG_TASK_REPEAT_NO_REPEAT).setText(Integer.toString(noRepeat)));
        element.addContent(actionParamMaterial.toXML());
        return element;
    }
}

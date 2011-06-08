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
public class TaskRepeatValueDataProd extends TaskRepeatValue{
    public final static String TAG_TASK_REPEAT_VALUE_DATA_PROD = "task_repeat_value_data_prod";
    /* data prod */
    private QData data;

    public TaskRepeatValueDataProd(long dbKey, int noRepeat, QData data) {
        super(dbKey, noRepeat);
        this.data = data;
    }

    public TaskRepeatValueDataProd(Element xmlElem, long idValue, long idQuantity, List<PhysicalQuantity> listPhysicalQuantity) throws JDOMException {
        super(xmlElem);
        if (xmlElem.getName().equals(TAG_TASK_REPEAT_VALUE_DATA_PROD)) {
            dbKey = idValue++;
            noRepeat = Integer.parseInt(xmlElem.getChild(TAG_TASK_REPEAT_NO_REPEAT).getText());
            data = new QData(xmlElem.getChild(Quantity.TAG_PARAMETER), idQuantity++, listPhysicalQuantity);
	} else {
            throw(new JDOMException("Task repeat value data prod expects <"+TAG_TASK_REPEAT_VALUE_DATA_PROD+"> as root element, but found <"+xmlElem.getName()+">."));
	}
    }

    public QData getData() {
        return data;
    }

    public void setData(QData data) {
        this.data = data;
    }
    
    @Override
    public Object clone() {
        TaskRepeatValueDataProd v = (TaskRepeatValueDataProd) super.clone() ;

        v.setData((QData)this.data.clone());
        return v;
    }

    @Override
    public Element toXML(){
        Element element = new Element(TAG_TASK_REPEAT_VALUE_DATA_PROD);
        element.addContent(new Element(TAG_TASK_REPEAT_NO_REPEAT).setText(Integer.toString(noRepeat)));
        element.addContent(data.toXML());
        return element;
    }
}

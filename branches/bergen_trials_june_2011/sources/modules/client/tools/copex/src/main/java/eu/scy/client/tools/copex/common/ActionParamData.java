/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.common;

import java.util.List;
import java.util.Locale;
import org.jdom.Element;
import org.jdom.JDOMException;

/**
 * parametre d'une action de type data
 * @author Marjolaine
 */
public class ActionParamData extends ActionParam implements Cloneable{
    public final static String TAG_ACTION_PARAM_DATA = "action_param_data";
    /*parametre/quantite lie */
    private QData data;

    // CONSTRUCTOR
    public ActionParamData(long dbKey, InitialActionParam initialParam, QData data) {
        super(dbKey, initialParam);
        this.data = data;
    }

    public ActionParamData(Element xmlElem, long idActionParam, List<InitialParamData> listInitialParamData, long idQuantity, List<PhysicalQuantity> listPhysicalQuantity) throws JDOMException {
        super(xmlElem);
        if (xmlElem.getName().equals(TAG_ACTION_PARAM_DATA)) {
			dbKey = idActionParam;
            initialParam = new InitialParamData(xmlElem.getChild(InitialParamData.TAG_INITIAL_PARAM_DATA_REF), listInitialParamData);
            data = new QData(xmlElem.getChild(QData.TAG_PARAMETER), idQuantity++, listPhysicalQuantity);
        }
		else {
			throw(new JDOMException("Action Param Data expects <"+TAG_ACTION_PARAM_DATA+"> as root element, but found <"+xmlElem.getName()+">."));
		}
        
    }


    // GETTER AND SETTER
    public QData getData() {
        return data;
    }

    public void setData(QData data) {
        this.data = data;
    }

    // OVERRIDE
    @Override
    public Object clone() {
        ActionParamData p = (ActionParamData) super.clone() ;

        p.setData((QData)this.data.clone());
        return p;
    }

    /* description dans l'arbre*/
    @Override
    public String toDescription(Locale locale){
        return data.getName(locale);
    }

    @Override
    public Element toXML(){
        Element element = new Element(TAG_ACTION_PARAM_DATA);
        element.addContent(initialParam.toXMLRef());
        element.addContent(data.toXML());
		return element;
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.common;

import eu.scy.client.tools.copex.utilities.CopexUtilities;
import java.text.NumberFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import org.jdom.Element;
import org.jdom.JDOMException;


/**
 * parametre d'une action de type quantite
 * @author Marjolaine
 */
public class ActionParamQuantity extends ActionParam{
    public final static String TAG_ACTION_PARAM_QUANTITY = "action_param_quantity";
    public final static String TAG_ACTION_PARAM_QUANTITY_REF = "action_param_quantity_ref";
    public final static String TAG_ACTION_PARAM_QUANTITY_CODE = "code";
    /*parametre/quantite lie */
    private Parameter parameter;
    private String code;

    // CONSTRUCTOR
    public ActionParamQuantity(long dbKey, InitialActionParam initialParam, Parameter parameter) {
        super(dbKey, initialParam);
        this.parameter = parameter;
    }


    public ActionParamQuantity(Element xmlElem, long idActionParam, List<InitialParamQuantity> listInitialParamQuantity, long idQuantity, List<PhysicalQuantity> listPhysicalQuantity) throws JDOMException {
        super(xmlElem);
        if (xmlElem.getName().equals(TAG_ACTION_PARAM_QUANTITY)) {
			dbKey = idActionParam;
            code =xmlElem.getChild(TAG_ACTION_PARAM_QUANTITY_CODE).getText();
            initialParam = new InitialParamQuantity(xmlElem.getChild(InitialParamQuantity.TAG_INITIAL_PARAM_QUANTITY_REF), listInitialParamQuantity);
            parameter = new Parameter(xmlElem.getChild(Quantity.TAG_PARAMETER), idQuantity++, listPhysicalQuantity);
        }
		else {
			throw(new JDOMException("Action Param Quantity expects <"+TAG_ACTION_PARAM_QUANTITY+"> as root element, but found <"+xmlElem.getName()+">."));
		}

    }

    public ActionParamQuantity(Element xmlElem,List<ActionParamQuantity> list)throws JDOMException {
        super(xmlElem);
        if (xmlElem.getName().equals(TAG_ACTION_PARAM_QUANTITY_REF)) {
            code =xmlElem.getChild(TAG_ACTION_PARAM_QUANTITY_CODE).getText();
            for(Iterator<ActionParamQuantity> q = list.iterator();q.hasNext();){
                ActionParamQuantity p = q.next();
                if(p.getCode().equals(code)){
                    this.dbKey = p.getDbKey();
                    this.parameter = p.getParameter();
                    this.initialParam = p.getInitialParam();
                }
            }
        }
		else {
			throw(new JDOMException("Action Param Quantity expects <"+TAG_ACTION_PARAM_QUANTITY_REF+"> as root element, but found <"+xmlElem.getName()+">."));
		}
    }

    // GETTER AND SETTER
    public Parameter getParameter() {
        return parameter;
    }

    public void setParameter(Parameter parameter) {
        this.parameter = parameter;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    // OVERRIDE
    @Override
    public Object clone() {
        ActionParamQuantity p = (ActionParamQuantity) super.clone() ;

        p.setParameter((Parameter)this.parameter.clone());
        return p;
    }

    /* description dans l'arbre*/
    @Override
    public String toDescription(Locale locale){
        NumberFormat numberFormat = NumberFormat.getNumberInstance(locale);
        numberFormat.setMaximumFractionDigits(Integer.MAX_VALUE);
        numberFormat.setGroupingUsed(false);
        String txt = CopexUtilities.getText(parameter.getUnit().getListSymbol(), locale);
        return numberFormat.format(parameter.getValue())+" "+txt;
    }

    @Override
    public Element toXML(){
        Element element = new Element(TAG_ACTION_PARAM_QUANTITY);
        element.addContent(new Element(TAG_ACTION_PARAM_QUANTITY_CODE).setText(code));
        element.addContent(initialParam.toXMLRef());
        element.addContent(parameter.toXML());
		return element;
    }

    public Element toXMLRef(){
        Element element = new Element(TAG_ACTION_PARAM_QUANTITY_REF);
        element.addContent(new Element(TAG_ACTION_PARAM_QUANTITY_CODE).setText(code));
        return element;
    }

}

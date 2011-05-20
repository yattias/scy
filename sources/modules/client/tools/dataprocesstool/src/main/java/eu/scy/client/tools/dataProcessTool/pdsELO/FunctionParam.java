/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.dataProcessTool.pdsELO;

import org.jdom.Element;
import org.jdom.JDOMException;
import roolo.elo.JDomStringConversion;

/**
 *
 * @author Marjolaine
 */
public class FunctionParam {
    /* TAG NAMES */
    public final static String TAG_VISUALIZATION_FUNCTION_PARAM = "function_param";
    public final static String TAG_VISUALIZATION_FUNCTION_PARAM_NAME="param";
    public final static String TAG_VISUALIZATION_FUNCTION_PARAM_VALUE="value";


    private String param;
    private double value;

    public FunctionParam(String param, double value) {
        this.param = param;
        this.value = value;
    }

    public FunctionParam(Element xmlElem) throws JDOMException {
        if (xmlElem.getName().equals(TAG_VISUALIZATION_FUNCTION_PARAM)) {
            this.param =xmlElem.getChild(TAG_VISUALIZATION_FUNCTION_PARAM_NAME).getText() ;
            try{
                this.value = Double.parseDouble(xmlElem.getChild(TAG_VISUALIZATION_FUNCTION_PARAM_VALUE).getText());
            }catch(NumberFormatException e){
                throw(new JDOMException("Function param expects VALUE as double"));
            }
        } else {
            throw(new JDOMException("Operation expects <"+TAG_VISUALIZATION_FUNCTION_PARAM+"> as root element, but found <"+xmlElem.getName()+">."));
	}
    }

    public FunctionParam(String xmlString) throws JDOMException {
        this(new JDomStringConversion().stringToXml(xmlString));
    }

    public String getParam() {
        return param;
    }


    public double getValue() {
        return value;
    }


    // toXML
    public Element toXML(){
        Element element = new Element(TAG_VISUALIZATION_FUNCTION_PARAM);
        element.addContent(new Element(TAG_VISUALIZATION_FUNCTION_PARAM_NAME).setText(this.param));
        element.addContent(new Element(TAG_VISUALIZATION_FUNCTION_PARAM_VALUE).setText(Double.toString(this.value)));
	return element;
    }

}

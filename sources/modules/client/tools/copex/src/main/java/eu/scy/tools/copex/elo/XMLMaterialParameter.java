/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.elo;

import org.jdom.Element;
import org.jdom.JDOMException;

/**
 * parameter of a material
 * @author Marjolaine
 */
public class XMLMaterialParameter {
    /* tag names */
    public final static String TAG_PARAMETER = "parameter";
    private final static String TAG_PARAMETER_ID = "id_param";
    private final static String TAG_PARAMETER_NAME = "param_name";
    private final static String TAG_PARAMETER_TYPE = "param_type";
    private final static String TAG_PARAMETER_VALUE = "param_value";
    private final static String TAG_PARAMETER_UNCERTAINTY = "param_uncertainty";
    private final static String TAG_PARAMETER_UNIT = "unit";

    /*id quantity */
    private String idParam;
    /* quantity name */
    private String paramName;
    /* type */
    private String paramType;
    /*value */
    private String paramValue;
    /* uncertainty */
    private String paramUncertainty;
    /* unit */
    private String paramUnit;

    public XMLMaterialParameter(String idParam, String paramName, String paramType, String paramValue, String paramUncertainty, String paramUnit) {
        this.idParam = idParam;
        this.paramName = paramName;
        this.paramType = paramType;
        this.paramValue = paramValue;
        this.paramUncertainty = paramUncertainty;
        this.paramUnit = paramUnit;
    }

    public XMLMaterialParameter(Element xmlElem) throws JDOMException {
		if (xmlElem.getName().equals(TAG_PARAMETER)) {
			idParam = xmlElem.getChild(TAG_PARAMETER_ID).getText();
            paramName = xmlElem.getChildText(TAG_PARAMETER_NAME);
            paramType = xmlElem.getChildText(TAG_PARAMETER_TYPE);
            paramValue = xmlElem.getChildText(TAG_PARAMETER_VALUE);
            paramUncertainty = xmlElem.getChildText(TAG_PARAMETER_UNCERTAINTY);
            paramUnit = xmlElem.getChildText(TAG_PARAMETER_UNIT);

		} else {
			throw(new JDOMException("Material Parameter expects <"+TAG_PARAMETER+"> as root element, but found <"+xmlElem.getName()+">."));
		}
	}

    public String getIdParam() {
        return idParam;
    }

    public String getParamName() {
        return paramName;
    }

    public String getParamType() {
        return paramType;
    }

    public String getParamUncertainty() {
        return paramUncertainty;
    }

    public String getParamUnit() {
        return paramUnit;
    }

    public String getParamValue() {
        return paramValue;
    }

    // toXML
    public Element toXML(){
        Element element = new Element(TAG_PARAMETER);
		element.addContent(new Element(TAG_PARAMETER_ID).setText(idParam));
        if (paramName != null)
            element.addContent(new Element(TAG_PARAMETER_NAME).setText(paramName));
        if (paramType != null)
            element.addContent(new Element(TAG_PARAMETER_TYPE).setText(paramType));
        if (paramValue != null)
            element.addContent(new Element(TAG_PARAMETER_VALUE).setText(paramValue));
        if (paramUncertainty != null)
            element.addContent(new Element(TAG_PARAMETER_UNCERTAINTY).setText(paramUncertainty));
        if (paramUnit != null)
            element.addContent(new Element(TAG_PARAMETER_UNIT).setText(paramUnit));

		return element;
    }


}

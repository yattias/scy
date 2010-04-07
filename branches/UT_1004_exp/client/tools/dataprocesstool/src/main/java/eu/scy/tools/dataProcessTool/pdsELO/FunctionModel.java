/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.dataProcessTool.pdsELO;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.jdom.Element;
import org.jdom.JDOMException;
import roolo.elo.JDomStringConversion;

/**
 * function model : description / color
 * @author Marjolaine
 */
public class FunctionModel {

    /* TAG NAMES */
    public final static String TAG_VISUALIZATION_FUNCTION_MODEL = "function_model";
    public final static String TAG_VISUALIZATION_FUNCTION_MODEL_DESCRIPTION="description";
    public final static String TAG_VISUALIZATION_FUNCTION_MODEL_COLOR_R="color_r";
    public final static String TAG_VISUALIZATION_FUNCTION_MODEL_COLOR_G="color_g";
    public final static String TAG_VISUALIZATION_FUNCTION_MODEL_COLOR_B="color_b";

    /* description */
    private String description;
    /* color */
    private int colorR;
    private int colorG;
    private int colorB;
    /* eventuellement param */
    private List<FunctionParam> listParam;

    public FunctionModel(String description, int colorR, int colorG, int colorB, List<FunctionParam> listParam) {
        this.description = description;
        this.colorR = colorR;
        this.colorG = colorG;
        this.colorB = colorB;
        this.listParam = listParam;
    }

    public FunctionModel(Element xmlElem) throws JDOMException {
		if (xmlElem.getName().equals(TAG_VISUALIZATION_FUNCTION_MODEL)) {
			this.description =xmlElem.getChild(TAG_VISUALIZATION_FUNCTION_MODEL_DESCRIPTION).getText() ;
            try{
                this.colorR = Integer.parseInt(xmlElem.getChild(TAG_VISUALIZATION_FUNCTION_MODEL_COLOR_R).getText());
                this.colorG = Integer.parseInt(xmlElem.getChild(TAG_VISUALIZATION_FUNCTION_MODEL_COLOR_G).getText());
                this.colorB = Integer.parseInt(xmlElem.getChild(TAG_VISUALIZATION_FUNCTION_MODEL_COLOR_B).getText());
            }catch(NumberFormatException e){
                throw(new JDOMException("Function model expects COLOR as integer"));
            }
            listParam = new LinkedList<FunctionParam>();
            for (Iterator<Element> variableElem = xmlElem.getChildren(FunctionParam.TAG_VISUALIZATION_FUNCTION_PARAM).iterator(); variableElem.hasNext();) {
                listParam.add(new FunctionParam(variableElem.next()));
            }
		} else {
			throw(new JDOMException("Operation expects <"+TAG_VISUALIZATION_FUNCTION_MODEL+"> as root element, but found <"+xmlElem.getName()+">."));
		}
	}

	public FunctionModel(String xmlString) throws JDOMException {
		this(new JDomStringConversion().stringToXml(xmlString));
	}



    public int getColorB() {
        return colorB;
    }

    public int getColorG() {
        return colorG;
    }

    public int getColorR() {
        return colorR;
    }

    public String getDescription() {
        return description;
    }

    public List<FunctionParam> getListParam() {
        return listParam;
    }


    // toXML
    public Element toXML(){
        Element element = new Element(TAG_VISUALIZATION_FUNCTION_MODEL);
        element.addContent(new Element(TAG_VISUALIZATION_FUNCTION_MODEL_DESCRIPTION).setText(this.description));
        element.addContent(new Element(TAG_VISUALIZATION_FUNCTION_MODEL_COLOR_R).setText(Integer.toString(this.colorR)));
        element.addContent(new Element(TAG_VISUALIZATION_FUNCTION_MODEL_COLOR_G).setText(Integer.toString(this.colorG)));
        element.addContent(new Element(TAG_VISUALIZATION_FUNCTION_MODEL_COLOR_B).setText(Integer.toString(this.colorB)));
        if (listParam != null && listParam.size() > 0){
            for (Iterator<FunctionParam> param = listParam.iterator(); param.hasNext();) {
                    element.addContent(param.next().toXML());
            }
        }
		return element;
    }
}

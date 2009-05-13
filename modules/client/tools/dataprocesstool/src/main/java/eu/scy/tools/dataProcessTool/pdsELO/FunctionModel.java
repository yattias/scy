/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.dataProcessTool.pdsELO;

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

    public FunctionModel(String description, int colorR, int colorG, int colorB) {
        this.description = description;
        this.colorR = colorR;
        this.colorG = colorG;
        this.colorB = colorB;
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


    // toXML
    public Element toXML(){
        Element element = new Element(TAG_VISUALIZATION_FUNCTION_MODEL);
        element.addContent(new Element(TAG_VISUALIZATION_FUNCTION_MODEL_DESCRIPTION).setText(this.description));
        element.addContent(new Element(TAG_VISUALIZATION_FUNCTION_MODEL_COLOR_R).setText(Integer.toString(this.colorR)));
        element.addContent(new Element(TAG_VISUALIZATION_FUNCTION_MODEL_COLOR_G).setText(Integer.toString(this.colorG)));
        element.addContent(new Element(TAG_VISUALIZATION_FUNCTION_MODEL_COLOR_B).setText(Integer.toString(this.colorB)));

		return element;
    }
}

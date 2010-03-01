/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.dataProcessTool.pdsELO;

import org.jdom.Element;
import org.jdom.JDOMException;
import roolo.elo.JDomStringConversion;

/**
 * visualization type bar chart
 * @author Marjolaine
 */
public class BarVisualization extends Visualization {
    /* id row/col */
    private int id;

    public BarVisualization(String type, String name,  int id) {
        super(type, name);
        this.id = id;
    }

    public BarVisualization(Element xmlElem) throws JDOMException {
		super(xmlElem);
        if (xmlElem.getName().equals(TAG_VISUALIZATION)) {
            this.type =xmlElem.getChild(TAG_VISUALIZATION_TYPE).getText() ;
            Element elDef = xmlElem.getChild(TAG_VISUALIZATION_DEFINITION);
            this.name = elDef.getChild(TAG_VISUALIZATION_DEF_NAME).getText() ;
            Element el = elDef.getChild(TAG_VISUALIZATION_DEF_ID_COL) ;
            try{
                this.id = Integer.parseInt(el.getText());
            } catch(NumberFormatException e){
                throw(new JDOMException("Pie operation expects id as Integer ."));
            }

		} else {
			throw(new JDOMException("Operation expects <"+TAG_VISUALIZATION+"> as root element, but found <"+xmlElem.getName()+">."));
		}
	}


    public BarVisualization(String xmlString) throws JDOMException {
		this(new JDomStringConversion().stringToXml(xmlString));
	}

    public int getId() {
        return id;
    }

    // toXML
    @Override
    public Element toXML(){
        Element element = new Element(TAG_VISUALIZATION);
        element.addContent(new Element(TAG_VISUALIZATION_TYPE).setText(this.type));
        Element elDef = new Element(TAG_VISUALIZATION_DEFINITION);
        elDef.addContent(new Element(TAG_VISUALIZATION_DEF_NAME).setText(this.name));
        elDef.addContent(new Element(TAG_VISUALIZATION_DEF_ID_COL).setText(Integer.toString(this.id)));
        element.addContent(elDef);

		return element;
    }

}

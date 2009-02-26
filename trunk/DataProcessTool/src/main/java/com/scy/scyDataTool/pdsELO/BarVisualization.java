/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.scy.scyDataTool.pdsELO;

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

    public BarVisualization(String type, String name, boolean isOnCol, int id) {
        super(type, name, isOnCol);
        this.id = id;
    }

    public BarVisualization(Element xmlElem) throws JDOMException {
		super(xmlElem);
        if (xmlElem.getName().equals(TAG_VISUALIZATION)) {
            this.type =xmlElem.getChild(TAG_VISUALIZATION_TYPE).getText() ;
            Element elDef = xmlElem.getChild(TAG_VISUALIZATION_DEFINITION);
            this.name = elDef.getChild(TAG_VISUALIZATION_DEF_NAME).getText() ;
            Element el = elDef.getChild(TAG_VISUALIZATION_DEF_ID_COL) ;
			this.isOnCol = el !=null;
            if (el == null)
                el = elDef.getChild(TAG_VISUALIZATION_DEF_ID_ROW);
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
        Element elId = new Element(TAG_VISUALIZATION_DEF_ID_ROW);
        if (this.isOnCol)
            elId = new Element(TAG_VISUALIZATION_DEF_ID_COL);
        elDef.addContent(elId.setText(Integer.toString(this.id)));
        element.addContent(elDef);

		return element;
    }

}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.saveProcXml;

import org.jdom.Element;
import org.jdom.JDOMException;

/**
 * column datasheet
 * @author Marjolaine
 */
public class Column {
    /* tag names */
    public static final String TAG_COLUMN = "column";

    /* value */
    private String value;

    public Column(String value) {
        this.value = value;
    }



    public Column(Element xmlElem) throws JDOMException {
		if (xmlElem.getName().equals(TAG_COLUMN)) {
            value = xmlElem.getText();
		} else {
			throw(new JDOMException("Column expects <"+TAG_COLUMN+"> as root element, but found <"+xmlElem.getName()+">."));
		}
	}

    public String getValue() {
        return value;
    }


    // toXML
    public Element toXML(){
        Element element = new Element(TAG_COLUMN);
        element.setText(value);
		return element;
    }
}

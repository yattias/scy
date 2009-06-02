/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.elo;

import org.jdom.Element;
import org.jdom.JDOMException;

/**
 * type of a material
 * @author Marjolaine
 */
public class XMLMaterialType {
    /* tag names */
    public final static String TAG_TYPE = "type";
    private final static String TAG_TYPE_ID = "id_type";
    private final static String TAG_TYPE_NAME = "type_name";

    /* id type */
    private String idType;
    /* type name */
    private String typeName;

    public XMLMaterialType(String idType, String typeName) {
        this.idType = idType;
        this.typeName = typeName;
    }

    public XMLMaterialType(Element xmlElem) throws JDOMException {
		if (xmlElem.getName().equals(TAG_TYPE)) {
			idType = xmlElem.getChild(TAG_TYPE_ID).getText();
            typeName = xmlElem.getChild(TAG_TYPE_NAME).getText();
		} else {
			throw(new JDOMException("Type Material expects <"+TAG_TYPE+"> as root element, but found <"+xmlElem.getName()+">."));
		}
	}

    public String getIdType() {
        return idType;
    }

    public String getTypeName() {
        return typeName;
    }

    // toXML
    public Element toXML(){
        Element element = new Element(TAG_TYPE);
		element.addContent(new Element(TAG_TYPE_ID).setText(idType));
        element.addContent(new Element(TAG_TYPE_NAME).setText(typeName));
		return element;
    }


}

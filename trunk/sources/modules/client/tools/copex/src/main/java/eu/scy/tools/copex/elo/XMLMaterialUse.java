/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.elo;

import eu.scy.tools.copex.utilities.CopexUtilities;
import org.jdom.Element;
import org.jdom.JDOMException;

/**
 * material use during the proc
 * @author Marjolaine
 */
public class XMLMaterialUse {
    /* tag names */
    public static final String TAG_MATERIAL = "material";
    public static final String TAG_MATERIAL_ID = "id";
    public static final String TAG_MATERIAL_NAME = "name";
    public static final String TAG_MATERIAL_JUSTIFICATION = "justification";

    /*id*/
    private String idMaterial;
    /*name */
    private String materialName;
    /* justification */
    private String justification;

    public XMLMaterialUse(String idMaterial, String materialName, String justification) {
        this.idMaterial = idMaterial;
        this.materialName = materialName;
        this.justification = justification;
    }

     public XMLMaterialUse(Element xmlElem) throws JDOMException {
		if (xmlElem.getName().equals(TAG_MATERIAL)) {
			idMaterial = xmlElem.getChild(TAG_MATERIAL_ID).getText();
            materialName = xmlElem.getChild(TAG_MATERIAL_NAME).getText();
            if(xmlElem.getChild(TAG_MATERIAL_JUSTIFICATION) != null)
                justification = xmlElem.getChild(TAG_MATERIAL_JUSTIFICATION).getText();
		} else {
			throw(new JDOMException("MaterialUse expects <"+TAG_MATERIAL+"> as root element, but found <"+xmlElem.getName()+">."));
		}
	}


    public String getIdMaterial() {
        return idMaterial;
    }

    public String getJustification() {
        return justification;
    }

    public String getMaterialName() {
        return materialName;
    }
     // toXML
    public Element toXML(){
        Element element = new Element(TAG_MATERIAL);
		element.addContent(new Element(TAG_MATERIAL_ID).setText(idMaterial));
        element.addContent(new Element(TAG_MATERIAL_NAME).setText(materialName));
        if(justification != null && justification.length() > 0){
            justification = CopexUtilities.getUTF8String(justification);
                element.addContent(new Element(TAG_MATERIAL_JUSTIFICATION).setText(justification));
        }
        return element;
    }

}

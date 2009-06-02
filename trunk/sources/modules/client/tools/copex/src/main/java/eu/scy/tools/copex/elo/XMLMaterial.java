/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.elo;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.jdom.Element;
import org.jdom.JDOMException;

/**
 * material 
 * @author Marjolaine
 */
public class XMLMaterial {
    /*tag names */
    public final static String TAG_MATERIAL = "material";
    private final static String TAG_MATERIAL_ID = "id_mat";
    private final static String TAG_MATERIAL_NAME = "mat_name";
    private final static String TAG_MATERIAL_DESCRIPTION = "mat_description";

    /* id material */
    private String idMaterial;
    /* material name */
    private String materialName;
    /* material description */
    private String materialDescription;
    /* liste des types du material */
    private List<XMLMaterialType> listType;
    /* liste des parameters */
    private List<XMLMaterialParameter> listParameters;

    public XMLMaterial(String idMaterial, String materialName, String materialDescription, List<XMLMaterialType> listType, List<XMLMaterialParameter> listParameters) {
        this.idMaterial = idMaterial;
        this.materialName = materialName;
        this.materialDescription = materialDescription;
        this.listType = listType;
        this.listParameters = listParameters;
    }

    public XMLMaterial(Element xmlElem) throws JDOMException {
		if (xmlElem.getName().equals(TAG_MATERIAL)) {
			idMaterial = xmlElem.getChild(TAG_MATERIAL_ID).getText();
            materialName = xmlElem.getChild(TAG_MATERIAL_NAME).getText();
            materialDescription = xmlElem.getChildText(TAG_MATERIAL_DESCRIPTION);
            listType = new LinkedList<XMLMaterialType>();
			for (Iterator<Element> variableElem = xmlElem.getChildren(XMLMaterialType.TAG_TYPE).iterator(); variableElem.hasNext();) {
				listType.add(new XMLMaterialType(variableElem.next()));
			}
            listParameters = new LinkedList<XMLMaterialParameter>();
			for (Iterator<Element> variableElem = xmlElem.getChildren(XMLMaterialParameter.TAG_PARAMETER).iterator(); variableElem.hasNext();) {
				listParameters.add(new XMLMaterialParameter(variableElem.next()));
			}
		} else {
			throw(new JDOMException("Material expects <"+TAG_MATERIAL+"> as root element, but found <"+xmlElem.getName()+">."));
		}
	}


    public String getIdMaterial() {
        return idMaterial;
    }

    public List<XMLMaterialType> getListType() {
        return listType;
    }

    public List<XMLMaterialParameter> getListParameters() {
        return listParameters;
    }

    public String getMaterialDescription() {
        return materialDescription;
    }

    public String getMaterialName() {
        return materialName;
    }

    // toXML
    public Element toXML(){
        Element element = new Element(TAG_MATERIAL);
		element.addContent(new Element(TAG_MATERIAL_ID).setText(idMaterial));
        element.addContent(new Element(TAG_MATERIAL_NAME).setText(materialName));
        if (materialDescription != null)
            element.addContent(new Element(TAG_MATERIAL_DESCRIPTION).setText(materialDescription));
        for (Iterator<XMLMaterialType> type = listType.iterator(); type.hasNext();) {
				element.addContent(type.next().toXML());
		}
        if (listParameters != null){
            for (Iterator<XMLMaterialParameter> p = listParameters.iterator(); p.hasNext();) {
				element.addContent(p.next().toXML());
            }
        }
		return element;
    }
    

}

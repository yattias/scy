/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.data;

import com.lowagie.text.ElementTags;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.jdom.Element;
import org.jdom.JDOMException;


/**
 *
 * @author Marjolaine
 */
public class XMLGeneral implements Cloneable{
    /*tag name  */
    public static final String TAG_COPEX = "copex" ;
    public static final String TAG_COPEX_QUANTITIES = "quantities" ;
    public static final String TAG_COPEX_MATERIALS = "materials" ;
    public static final String TAG_COPEX_ACTIONS_PRESTRUCTURED = "actions_prestructured" ;
    public static final String TAG_COPEX_MATERIAL_STRATEGY = "strategy_material";

    private List<XMLQuantity> listQuantities;
    private List<XMLMaterial> listMaterials;
    private List<XMLActionPreStructured> listActionsPreStructured;
    private List<XMLStrategy> listStrategy;

    public XMLGeneral(List<XMLQuantity> listQuantities, List<XMLMaterial> listMaterials, List<XMLActionPreStructured> listActionsPreStructured, List<XMLStrategy> listStrategy) {
        this.listQuantities = listQuantities;
        this.listMaterials = listMaterials;
        this.listActionsPreStructured = listActionsPreStructured;
        this.listStrategy = listStrategy;
    }


    public XMLGeneral(Element xmlElem) throws JDOMException {
		if (xmlElem.getName().equals(TAG_COPEX)) {
            if (xmlElem.getChild(TAG_COPEX_QUANTITIES) != null){
                listQuantities = new LinkedList<XMLQuantity>();
                for (Iterator<Element> variableElem = xmlElem.getChildren(XMLQuantity.TAG_QUANTITY).iterator(); variableElem.hasNext();) {
                    listQuantities.add(new XMLQuantity(variableElem.next()));
                }
            }
            if (xmlElem.getChild(TAG_COPEX_MATERIALS) != null){
                listMaterials = new LinkedList<XMLMaterial>();
                for (Iterator<Element> variableElem = xmlElem.getChildren(XMLMaterial.TAG_MATERIAL).iterator(); variableElem.hasNext();) {
                    listMaterials.add(new XMLMaterial(variableElem.next()));
                }
            }
            if (xmlElem.getChild(TAG_COPEX_ACTIONS_PRESTRUCTURED) != null){
                listActionsPreStructured = new LinkedList<XMLActionPreStructured>();
                for (Iterator<Element> variableElem = xmlElem.getChildren(XMLActionPreStructured.TAG_ACTION).iterator(); variableElem.hasNext();) {
                    listActionsPreStructured.add(new XMLActionPreStructured(variableElem.next()));
                }
            }
            if(xmlElem.getChild(TAG_COPEX_MATERIAL_STRATEGY) != null){
                listStrategy = new LinkedList<XMLStrategy>();
                for (Iterator<Element> variableElem = xmlElem.getChildren(XMLStrategy.TAG_STRATEGY).iterator();variableElem.hasNext();){
                    listStrategy.add(new XMLStrategy(variableElem.next()));
                }
            }
		} else {
			throw(new JDOMException("XMLGeneral expects <"+TAG_COPEX+"> as root element, but found <"+xmlElem.getName()+">."));
		}
	}

    public List<XMLActionPreStructured> getListActionsPreStructured() {
        return listActionsPreStructured;
    }

    public List<XMLMaterial> getListMaterials() {
        return listMaterials;
    }

    public List<XMLQuantity> getListQuantities() {
        return listQuantities;
    }

    public List<XMLStrategy> getListStrategy() {
        return listStrategy;
    }

    public void setListStrategy(List<XMLStrategy> listStrategy) {
        this.listStrategy = listStrategy;
    }

     // toXML
    public Element toXML(){
        Element element = new Element(TAG_COPEX);
        if(listQuantities != null && listQuantities.size() > 0){
            Element listQtt = new Element(TAG_COPEX_QUANTITIES);
            for (Iterator<XMLQuantity> q = listQuantities.iterator(); q.hasNext();) {
                    listQtt.addContent(q.next().toXML());
            }
            element.addContent(listQtt);
        }
        if(listMaterials != null && listMaterials.size() > 0){
            Element listMat = new Element(TAG_COPEX_MATERIALS);
            for (Iterator<XMLMaterial> m = listMaterials.iterator(); m.hasNext();) {
                    listMat.addContent(m.next().toXML());
            }
            element.addContent(listMat);
        }
        if(listActionsPreStructured != null && listActionsPreStructured.size() > 0){
            Element listAct = new Element(TAG_COPEX_ACTIONS_PRESTRUCTURED);
            for (Iterator<XMLActionPreStructured> a = listActionsPreStructured.iterator(); a.hasNext();) {
                    listAct.addContent(a.next().toXML());
            }
            element.addContent(listAct);
        }
        if( listStrategy != null && listStrategy.size() > 0){
            Element listS = new Element(TAG_COPEX_MATERIAL_STRATEGY);
            for (Iterator<XMLStrategy> s = listStrategy.iterator();s.hasNext();){
                listS.addContent(s.next().toXML());
            }
            element.addContent(listS);
        }
		return element;
    }

}

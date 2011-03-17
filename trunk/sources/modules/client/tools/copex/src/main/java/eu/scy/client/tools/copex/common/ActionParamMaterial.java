/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.common;

import java.util.List;
import java.util.Locale;
import org.jdom.Element;
import org.jdom.JDOMException;


/**
 * parametre de l'action de type materiel
 * @author Marjolaine
 */
public class ActionParamMaterial extends ActionParam{
    public final static String TAG_ACTION_PARAM_MATERIAL = "action_param_material";
    /* material associe */
    private Material material ;
    /* eventuellement quantite associee */
    private ActionParamQuantity quantity ;

    // CONSTRUCTOR
    public ActionParamMaterial(long dbKey, InitialActionParam initialParam, Material material, ActionParamQuantity quantity) {
        super(dbKey, initialParam);
        this.material = material;
        this.quantity = quantity;
    }

    public ActionParamMaterial(Element xmlElem, long idActionParam, List<InitialParamMaterial> listInitialParamMaterial, List<Material> listMaterial, List<ActionParamQuantity> listActionParamQuantity) throws JDOMException {
        super(xmlElem);
        if (xmlElem.getName().equals(TAG_ACTION_PARAM_MATERIAL)) {
			dbKey = idActionParam;
            initialParam = new InitialParamMaterial(xmlElem.getChild(InitialParamMaterial.TAG_INITIAL_PARAM_MATERIAL_REF), listInitialParamMaterial);
            if(xmlElem.getChild(Material.TAG_MATERIAL_REF) != null)
                material = new Material(xmlElem.getChild(Material.TAG_MATERIAL_REF), listMaterial);
            if(xmlElem.getChild(ActionParamQuantity.TAG_ACTION_PARAM_QUANTITY_REF) != null)
                quantity = new ActionParamQuantity(xmlElem.getChild(ActionParamQuantity.TAG_ACTION_PARAM_QUANTITY_REF), listActionParamQuantity);
        }
		else {
			throw(new JDOMException("Action Param Material expects <"+TAG_ACTION_PARAM_MATERIAL+"> as root element, but found <"+xmlElem.getName()+">."));
		}
        
    }


    // GETTER AND SETTER
    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public ActionParamQuantity getQuantity() {
        return quantity;
    }

    public void setQuantity(ActionParamQuantity quantity) {
        this.quantity = quantity;
    }

    // OVERRIDE
    @Override
    public Object clone() {
        ActionParamMaterial p = (ActionParamMaterial) super.clone() ;

        p.setMaterial((Material)this.material.clone());
        ActionParamQuantity q = null;
        if (this.quantity != null){
            q = (ActionParamQuantity)this.quantity.clone();
        }
        p.setQuantity(q);
        return p;
    }

    /* description dans l'arbre*/
    @Override
    public String toDescription(Locale locale){
        return material.getName(locale);
    }

    @Override
    public Element toXML(){
        Element element = new Element(TAG_ACTION_PARAM_MATERIAL);
        element.addContent(initialParam.toXMLRef());
        element.addContent(material.toXMLRef());
        if(this.quantity != null)
            element.addContent(quantity.toXML());
	return element;
    }

}

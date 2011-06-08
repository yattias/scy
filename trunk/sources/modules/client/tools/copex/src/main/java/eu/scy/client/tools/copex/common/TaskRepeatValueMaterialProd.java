/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.common;

import java.util.List;
import org.jdom.Element;
import org.jdom.JDOMException;

/**
 *
 * @author Marjolaine
 */
public class TaskRepeatValueMaterialProd extends TaskRepeatValue{
    public final static String TAG_TASK_REPEAT_VALUE_MATERIAL_PROD = "task_repeat_value_material_prod";
    /* material prod */
    private Material material;

    public TaskRepeatValueMaterialProd(long dbKey, int noRepeat, Material material) {
        super(dbKey, noRepeat);
        this.material = material;
    }

    public TaskRepeatValueMaterialProd(Element xmlElem, long idValue, long idMaterial, long idQuantity, List<TypeMaterial> listTypeMaterial, List<PhysicalQuantity> listPhysicalQuantity) throws JDOMException {
        super(xmlElem);
        if (xmlElem.getName().equals(TAG_TASK_REPEAT_VALUE_MATERIAL_PROD)) {
            dbKey = idValue++;
            noRepeat = Integer.parseInt(xmlElem.getChild(TAG_TASK_REPEAT_NO_REPEAT).getText());
            material = new Material(xmlElem.getChild(Material.TAG_MATERIAL), idMaterial++, listTypeMaterial, listPhysicalQuantity, idQuantity);
	} else {
            throw(new JDOMException("Task repeat value material prod expects <"+TAG_TASK_REPEAT_VALUE_MATERIAL_PROD+"> as root element, but found <"+xmlElem.getName()+">."));
	}
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }
    
    @Override
    public Object clone() {
        TaskRepeatValueMaterialProd v = (TaskRepeatValueMaterialProd) super.clone() ;

        v.setMaterial((Material)this.material.clone());
        return v;
    }

    @Override
    public Element toXML(){
        Element element = new Element(TAG_TASK_REPEAT_VALUE_MATERIAL_PROD);
        element.addContent(new Element(TAG_TASK_REPEAT_NO_REPEAT).setText(Integer.toString(noRepeat)));
        element.addContent(material.toXML());
        return element;
    }

}

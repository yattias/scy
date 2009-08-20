/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.common;


/**
 * parametre de l'action de type materiel
 * @author Marjolaine
 */
public class ActionParamMaterial extends ActionParam{

    // PROPERTY
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

}

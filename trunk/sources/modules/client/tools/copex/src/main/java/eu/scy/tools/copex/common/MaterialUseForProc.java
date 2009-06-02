/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.common;

/**
 * materiel utilise pour un protocole
 * materiel + justification eventuelle
 * @author MBO
 */
public class MaterialUseForProc implements Cloneable {
    // ATTRIBUTS
    /* material */
    private Material material;
    /* justification */
    private String justification;

    // CONSTRUCTEURS
    public MaterialUseForProc(Material material, String justification) {
        this.material = material;
        this.justification = justification;
    }

    public MaterialUseForProc(Material material) {
        this.material = material;
        this.justification = null;
    }
    
    // GETTER AND SETTER
    public String getJustification() {
        return justification;
    }

    public void setJustification(String justification) {
        this.justification = justification;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    @Override
    public Object clone() {
        try {
            MaterialUseForProc materialUseForProc = (MaterialUseForProc) super.clone() ;
            Material m = (Material)this.material.clone() ;
            String j = null;
            if (this.justification != null)
                j = new String(justification);
            
            materialUseForProc.setMaterial(m);
            materialUseForProc.setJustification(j);
            
            return materialUseForProc;
        } catch (CloneNotSupportedException e) { 
	    // this shouldn't happen, since we are Cloneable
	    throw new InternalError();
	}
    }
    
    
}

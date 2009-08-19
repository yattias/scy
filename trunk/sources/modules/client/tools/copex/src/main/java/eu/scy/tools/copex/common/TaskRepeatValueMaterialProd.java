/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.common;

/**
 *
 * @author Marjolaine
 */
public class TaskRepeatValueMaterialProd extends TaskRepeatValue{
    /* material prod */
    private Material material;

    public TaskRepeatValueMaterialProd(long dbKey, int noRepeat, Material material) {
        super(dbKey, noRepeat);
        this.material = material;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }
    // OVERRIDE
    @Override
    public Object clone() {
        TaskRepeatValueMaterialProd v = (TaskRepeatValueMaterialProd) super.clone() ;

        v.setMaterial((Material)this.material.clone());
        return v;
    }

}

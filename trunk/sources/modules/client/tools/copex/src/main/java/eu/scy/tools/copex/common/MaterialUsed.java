/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.common;

/**
 * material used or not in a procedure by a learner
 * @author Marjolaine
 */
public class MaterialUsed implements Cloneable{
    /* material */
    private Material material;
    /* comments */
    private String comments;
    /* used or not */
    private boolean used;
    /* provient de l'utilisateur */
    private boolean userMaterial;

    // CONSTRUCTEURS
    public MaterialUsed(Material material, String comments, boolean used, boolean userMaterial) {
        this.material = material;
        this.comments = comments;
        this.used = used;
        this.userMaterial = userMaterial;
    }

    public MaterialUsed(Material material) {
        this.material = material;
        this.comments = null;
        this.used = true;
    }

    // GETTER AND SETTER
    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public boolean isUserMaterial() {
        return userMaterial;
    }

    public void setUserMaterial(boolean userMaterial) {
        this.userMaterial = userMaterial;
    }

    @Override
    public Object clone() {
        try {
            MaterialUsed mUsed = (MaterialUsed) super.clone() ;
            Material m = (Material)this.material.clone() ;
            String c = null;
            if (this.comments != null)
                c = new String(comments);

            mUsed.setMaterial(m);
            mUsed.setComments(c);
            mUsed.setUsed(new Boolean(used));
            mUsed.setUserMaterial(new Boolean(userMaterial));

            return mUsed;
        } catch (CloneNotSupportedException e) {
            // this shouldn't happen, since we are Cloneable
            throw new InternalError();
        }
    }
}

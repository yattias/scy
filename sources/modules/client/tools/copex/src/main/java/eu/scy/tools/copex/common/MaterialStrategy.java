/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.common;

/**
 * strategy material :
 * S0 : no material
 * S1 : list of materials to use
 * S2 : material available
 * S3 : material proposed
 * S4 : material created by user
 * @author Marjolaine
 */
public class MaterialStrategy implements Cloneable{
    private long dbKey ;
    private String code;
    private boolean isItem;
    private String itemLibelle;
    private boolean addMaterial;
    private boolean chooseMaterial;
    private boolean commentsMaterial;

    public MaterialStrategy(long dbKey, String code, boolean isItem, String itemLibelle, boolean addMaterial, boolean chooseMaterial, boolean commentsMaterial) {
        this.dbKey = dbKey;
        this.code = code;
        this.isItem = isItem;
        this.itemLibelle = itemLibelle;
        this.addMaterial = addMaterial;
        this.chooseMaterial = chooseMaterial;
        this.commentsMaterial = commentsMaterial;
    }

    public boolean canAddMaterial() {
        return addMaterial;
    }

    public void setAddMaterial(boolean addMaterial) {
        this.addMaterial = addMaterial;
    }

    public boolean canChooseMaterial() {
        return chooseMaterial;
    }

    public void setChooseMaterial(boolean chooseMaterial) {
        this.chooseMaterial = chooseMaterial;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean canCommentsMaterial() {
        return commentsMaterial;
    }

    public void setCommentsMaterial(boolean commentsMaterial) {
        this.commentsMaterial = commentsMaterial;
    }

    public long getDbKey() {
        return dbKey;
    }

    public void setDbKey(long dbKey) {
        this.dbKey = dbKey;
    }

    public boolean isItem() {
        return isItem;
    }

    public void setItem(boolean isItem) {
        this.isItem = isItem;
    }

    public String getItemLibelle() {
        return itemLibelle;
    }

    public void setItemLibelle(String itemLibelle) {
        this.itemLibelle = itemLibelle;
    }

    @Override
    public Object clone()  {
        try {
            MaterialStrategy s = (MaterialStrategy) super.clone() ;
            s.setDbKey(new Long(this.dbKey));
            s.setCode(new String (this.code));
            s.setItemLibelle(new String(this.itemLibelle));
            s.setItem(new Boolean(this.isItem));
            s.setAddMaterial(new Boolean(this.addMaterial));
            s.setChooseMaterial(new Boolean(this.chooseMaterial));
            s.setCommentsMaterial(new Boolean(this.commentsMaterial));
            return s;
        } catch (CloneNotSupportedException e) {
            // this shouldn't happen, since we are Cloneable
            throw new InternalError();
        }
    }


}

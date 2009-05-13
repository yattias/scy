/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.dataProcessTool.common;

import java.awt.Color;

/**
 * modele de fonctions
 * @author Marjolaine
 */
public class FunctionModel implements Cloneable{
    //PROPERTY
    /* identifiant */
    private long dbKey ;
    /* description, suit f(x) = */
    private String description;
    /* couleur */
    private Color color ;

    // CONSTRUCTOR
     public FunctionModel(long dbKey, String description, Color color) {
        this.dbKey = dbKey ;
        this.description = description;
        this.color = color;
    }

    public long getDbKey() {
        return dbKey;
    }

    public void setDbKey(long dbKey) {
        this.dbKey = dbKey;
    }

    // GETTER AND SETTER

     public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // CLONE
    @Override
    public Object clone()  {
        try {
            FunctionModel fm = (FunctionModel)super.clone();
            String descriptionC = new String(this.description);
            Color colorC  = this.color ;
            fm.setDbKey(this.dbKey);
            fm.setDescription(descriptionC);
            fm.setColor(colorC);

            return fm;
        } catch (CloneNotSupportedException e) {
            // this shouldn't happen, since we are Cloneable
            throw new InternalError();
        }
    }


}

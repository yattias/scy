/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.common;

import java.util.ArrayList;


/**
 * parametre initial action nommee de type type de materiel
 * @author Marjolaine
 */
public class InitialParamMaterial extends InitialActionParam {

    // PROPERTY
    /* type de materiel */
    private TypeMaterial typeMaterial;
    /* eventuellementparam quantity dependant*/
    private InitialParamQuantity paramQuantity ;
    /* type de materiel 2 eventuellement */
    private TypeMaterial typeMaterial2;
    /*indique si exclusif ou non */
    private boolean andTypes;
    /* boolean indiquant s'il s'agit de tous les types de materiel du proc */
    private boolean allType;

    // CONSTRUCTOR
    public InitialParamMaterial(long dbKey, String paramName, TypeMaterial typeMaterial, TypeMaterial typeMaterial2, boolean andTypes, InitialParamQuantity paramQuantity, boolean allType) {
        super(dbKey, paramName);
        this.typeMaterial = typeMaterial;
        this.typeMaterial2 = typeMaterial2;
        this.andTypes =andTypes ;
        this.paramQuantity = paramQuantity ;
        this.allType = allType ;
    }

    // GETTER AND SETTER
    public TypeMaterial getTypeMaterial() {
        return typeMaterial;
    }

    public void setTypeMaterial(TypeMaterial typeMaterial) {
        this.typeMaterial = typeMaterial;
    }

    public InitialParamQuantity getParamQuantity() {
        return paramQuantity;
    }

    public void setParamQuantity(InitialParamQuantity paramQuantity) {
        this.paramQuantity = paramQuantity;
    }

    public TypeMaterial getTypeMaterial2() {
        return typeMaterial2;
    }

    public void setTypeMaterial2(TypeMaterial typeMaterial2) {
        this.typeMaterial2 = typeMaterial2;
    }

    public boolean isAndTypes() {
        return andTypes;
    }

    public void setAndTypes(boolean andTypes) {
        this.andTypes = andTypes;
    }

    public boolean isAllType() {
        return allType;
    }

    public void setAllType(boolean allType) {
        this.allType = allType;
    }

  
    public boolean canAccept(Material m){
        if(allType)
            return true;
        boolean controlType2 = typeMaterial2 != null;
        long dbKeyType2 = -1;
        if (controlType2)
            dbKeyType2 = typeMaterial2.getDbKey();
        if (m.isType(typeMaterial.getDbKey())){
                if ((controlType2 && m.isType(dbKeyType2)&& andTypes) || !controlType2 || (controlType2 && !andTypes) )
                    return true;
         }else  if(controlType2 && !andTypes && m.isType(dbKeyType2)){
                return true;
         }
        return false;
    }

    // OVERRIDE
    @Override
    public Object clone() {
        InitialParamMaterial p = (InitialParamMaterial) super.clone() ;

        if(typeMaterial == null)
            p.setTypeMaterial(null);
        else
            p.setTypeMaterial((TypeMaterial)this.getTypeMaterial().clone());
        if (paramQuantity == null)
            p.setParamQuantity(null);
        else
            p.setParamQuantity((InitialParamQuantity)this.paramQuantity.clone());
        if (this.typeMaterial2 == null)
            p.setTypeMaterial2(null);
        else
            p.setTypeMaterial2((TypeMaterial)this.getTypeMaterial2().clone());
        p.setAndTypes(this.andTypes);
        return p;
    }

}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.common;


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

    // CONSTRUCTOR
    public InitialParamMaterial(long dbKey, String paramName, TypeMaterial typeMaterial, TypeMaterial typeMaterial2, boolean andTypes, InitialParamQuantity paramQuantity) {
        super(dbKey, paramName);
        this.typeMaterial = typeMaterial;
        this.typeMaterial2 = typeMaterial2;
        this.andTypes =andTypes ;
        this.paramQuantity = paramQuantity ;
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

  
    

    // OVERRIDE
    @Override
    public Object clone() {
        InitialParamMaterial p = (InitialParamMaterial) super.clone() ;

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

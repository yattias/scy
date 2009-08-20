/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.common;

import java.util.ArrayList;

/**
 * Parametres initial de type quantite
 * @author Marjolaine
 */
public class InitialParamQuantity extends InitialActionParam {

    // PROPERTY
    /* valeur - fait reference a  une grandeur physique */
    private PhysicalQuantity physicalQuantity ;
    /* nom de la quantite */
    private String quantityName;

    // CONSTRUCTOR
    public InitialParamQuantity(long dbKey, String paramName, PhysicalQuantity physicalQuantity, String quantityName) {
        super(dbKey, paramName);
        this.physicalQuantity = physicalQuantity;
        this.quantityName = quantityName;
    }

    // GETTER AND SETTER
    public PhysicalQuantity getPhysicalQuantity() {
        return physicalQuantity;
    }

    public void setPhysicalQuantity(PhysicalQuantity physicalQuantity) {
        this.physicalQuantity = physicalQuantity;
    }

    public String getQuantityName() {
        return quantityName;
    }

    public void setQuantityName(String quantityName) {
        this.quantityName = quantityName;
    }

    // OVERRIDE
    @Override
    public Object clone() {
        InitialParamQuantity p = (InitialParamQuantity) super.clone() ;

        p.setPhysicalQuantity((PhysicalQuantity)this.physicalQuantity.clone());
        p.setQuantityName(new String(this.quantityName));
        return p;
    }

    // METHOD
    /* retourne la liste des unites possibles pour cette grandeur */
    public ArrayList<CopexUnit> getListUnit(){
        return physicalQuantity.getListUnit() ;
    }
}

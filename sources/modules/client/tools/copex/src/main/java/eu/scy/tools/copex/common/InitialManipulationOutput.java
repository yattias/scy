/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.common;

import java.util.ArrayList;

/**
 * output d'une action manipulation
 * @author Marjolaine
 */
public class InitialManipulationOutput extends InitialOutput implements Cloneable{
    
    /* liste des types de material attendu */
    private ArrayList<TypeMaterial> typeMaterialProd ;

    public InitialManipulationOutput(long dbKey, String textProd, String name, ArrayList<TypeMaterial> typeMaterialProd) {
        super(dbKey, textProd, name);
        this.typeMaterialProd = typeMaterialProd;
    }


    public ArrayList<TypeMaterial> getTypeMaterialProd() {
        return typeMaterialProd;
    }

    public void setTypeMaterialProd(ArrayList<TypeMaterial> typeMaterialProd) {
        this.typeMaterialProd = typeMaterialProd;
    }

    @Override
    protected Object clone(){
        InitialManipulationOutput a = (InitialManipulationOutput) super.clone() ;

        ArrayList<TypeMaterial> typeMaterialProdC = new ArrayList();
        for (int i=0; i<typeMaterialProd.size(); i++){
            typeMaterialProdC.add((TypeMaterial)this.typeMaterialProd.get(i).clone());
        }
        a.setTypeMaterialProd(typeMaterialProdC);

        return a;
    }


}

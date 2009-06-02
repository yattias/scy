/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.common;

import java.util.ArrayList;

/**
 * action de type manipulation
 * uses material
 * produces material
 * @author Marjolaine
 */
public class InitialActionManipulation extends InitialNamedAction implements Cloneable{
    // PROPERTY
    /* nb material prod*/
    private int nbMaterialProd;
    /* type de material attendu */
    private ArrayList<TypeMaterial> typeMaterialProd;
    /* texte prod */
    private String textProd;

    // CONSTRUCTOR
    public InitialActionManipulation(long dbKey, String code, String libelle, boolean isSetting, InitialActionVariable variable, boolean draw,  boolean repeat, int nbMaterialProd, ArrayList<TypeMaterial> typeMaterialProd, String textProd) {
        super(dbKey, code, libelle, isSetting, variable, draw, repeat);
        this.nbMaterialProd = nbMaterialProd;
        this.typeMaterialProd = typeMaterialProd ;
        this.textProd = textProd ;
    }

    // GETTER AND SETTER
    public int getNbMaterialProd() {
        return nbMaterialProd;
    }

    public void setNbMaterialProd(int nbMaterialProd) {
        this.nbMaterialProd = nbMaterialProd;
    }

    public ArrayList<TypeMaterial> getTypeMaterialProd() {
        return typeMaterialProd;
    }

    public void setTypeMaterialProd(ArrayList<TypeMaterial> typeMaterialProd) {
        this.typeMaterialProd = typeMaterialProd;
    }

    public String getTextProd() {
        return textProd;
    }

    public void setTextProd(String textProd) {
        this.textProd = textProd;
    }

    // OVERRIDE
    @Override
    public Object clone() {
        InitialActionManipulation a = (InitialActionManipulation) super.clone() ;

        a.setNbMaterialProd(this.nbMaterialProd);
        ArrayList<TypeMaterial> typeMaterialProdC = new ArrayList();
        for (int i=0; i<typeMaterialProd.size(); i++){
            typeMaterialProdC.add((TypeMaterial)this.typeMaterialProd.get(i).clone());
        }
        a.setTypeMaterialProd(typeMaterialProdC);
        a.setTextProd(new String (this.textProd));

        return a;
    }

}

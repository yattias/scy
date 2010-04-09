/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.dataProcessTool.common;

import java.awt.Color;

/**
 * type operation parametree
 * @author Marjolaine
 */
public class TypeOperationParam extends TypeOperation implements Cloneable{
    //PROPERTY
    /* nombre de parametres */
    private int nbParam;
    /* libelle */
    private String libelle;

    // CONSTRUCTOR
    public TypeOperationParam(long dbKey, int type, String codeName, Color color, int nbParam, String libelle) {
        super(dbKey, type, codeName, color);
        this.nbParam = nbParam;
        this.libelle = libelle;
    }

    // GETTER AND SETTER
    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public int getNbParam() {
        return nbParam;
    }

    public void setNbParam(int nbParam) {
        this.nbParam = nbParam;
    }

    @Override
    public Object clone()  {
        TypeOperationParam typeOpParam = (TypeOperationParam) super.clone() ;
        typeOpParam.setNbParam(this.nbParam);
        typeOpParam.setLibelle(new String(this.libelle));
        return typeOpParam;
    }


}

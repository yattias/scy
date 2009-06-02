/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.common;

/**
 * action de type treatment
 * uses data
 * produces data
 * @author Marjolaine
 */
public class InitialActionTreatment extends InitialNamedAction implements Cloneable {

    // PROPERTY
    /* nb data prod */
    private int nbDataProd ;
    /* unites des data prod */
    CopexUnit[] unitDataProd;
    /* texte prod */
    private String textProd;

    // CONSTRUCTOR
    public InitialActionTreatment(long dbKey, String code, String libelle, boolean isSetting, InitialActionVariable variable, boolean draw, boolean repeat, int nbDataProd, CopexUnit[] unitDataProd, String textProd) {
        super(dbKey, code, libelle, isSetting, variable, draw, repeat);
        this.nbDataProd = nbDataProd;
        this.unitDataProd = unitDataProd ;
        this.textProd = textProd;
    }

    // GETTER AND SETTER
    public int getNbDataProd() {
        return nbDataProd;
    }

    public void setNbDataProd(int nbDataProd) {
        this.nbDataProd = nbDataProd;
    }

    public CopexUnit[] getUnitDataProd() {
        return unitDataProd;
    }

    public void setUnitDataProd(CopexUnit[] unitDataProd) {
        this.unitDataProd = unitDataProd;
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
        InitialActionTreatment a = (InitialActionTreatment) super.clone() ;

        CopexUnit[] unitDataProdC = new CopexUnit[this.unitDataProd.length];
        for (int i=0; i<this.unitDataProd.length; i++){
            unitDataProdC[i] = (CopexUnit)this.unitDataProd[i].clone();
        }
        a.setUnitDataProd(unitDataProdC);
        a.setNbDataProd(this.nbDataProd);
        a.setTextProd(new String (this.textProd));
        return a;
    }

}

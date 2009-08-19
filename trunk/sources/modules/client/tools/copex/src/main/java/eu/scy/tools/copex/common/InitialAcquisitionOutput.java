/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.common;


/**
 * output d'une action acquisition
 * @author Marjolaine
 */
public class InitialAcquisitionOutput extends InitialOutput implements Cloneable {
    /* unites des data produites */
    private CopexUnit[] unitDataProd;

    public InitialAcquisitionOutput(long dbKey, String textProd, String name, CopexUnit[] unitDataProd) {
        super(dbKey, textProd, name);
        this.unitDataProd = unitDataProd ;
    }

    
    public CopexUnit[] getUnitDataProd() {
        return unitDataProd;
    }

    public void setUnitDataProd(CopexUnit[] unitDataProd) {
        this.unitDataProd = unitDataProd;
    }



    @Override
    protected Object clone(){
        InitialAcquisitionOutput a = (InitialAcquisitionOutput) super.clone() ;

        CopexUnit[] unitDataProdC = new CopexUnit[this.unitDataProd.length];
        for (int i=0; i<this.unitDataProd.length; i++){
            unitDataProdC[i] = (CopexUnit)this.unitDataProd[i].clone();
        }
        a.setUnitDataProd(unitDataProdC);
        return a;
    }

}

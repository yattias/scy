/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.common;

import java.util.ArrayList;

/**
 * action de type acquisition
 * uses Material
 * produces Data
 * @author Marjolaine
 */
public class InitialActionAcquisition extends InitialNamedAction implements Cloneable {

    // PROPERTY
    /* nb data prod*/
    private int nbDataProd;
    /* liste des output*/
    private ArrayList<InitialAcquisitionOutput> listOutput;

    // CONSTRUCTOR
    public InitialActionAcquisition(long dbKey, String code, String libelle, boolean isSetting, InitialActionVariable variable, boolean draw,  boolean repeat, int nbDataProd, ArrayList<InitialAcquisitionOutput> listOutput) {
        super(dbKey, code, libelle, isSetting, variable, draw, repeat);
        this.nbDataProd = nbDataProd;
        this.listOutput = listOutput ;
    }

    // GETTER AND SETTER
    public int getNbDataProd() {
        return nbDataProd;
    }

    public void setNbDataProd(int nbDataProd) {
        this.nbDataProd = nbDataProd;
    }

    public ArrayList<InitialAcquisitionOutput> getListOutput() {
        return listOutput;
    }

    public void setListOutput(ArrayList<InitialAcquisitionOutput> listOutput) {
        this.listOutput = listOutput;
    }

    
    // OVERRIDE
    @Override
    public Object clone() {
        InitialActionAcquisition a = (InitialActionAcquisition) super.clone() ;

        a.setNbDataProd(this.nbDataProd);
        ArrayList<InitialAcquisitionOutput> list = new ArrayList();
        for (int i=0; i<listOutput.size(); i++){
            list.add((InitialAcquisitionOutput)this.listOutput.get(i).clone());
        }
        a.setListOutput(list);
        return a;
    }


}

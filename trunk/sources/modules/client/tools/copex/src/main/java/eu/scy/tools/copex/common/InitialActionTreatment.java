/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.common;

import java.util.ArrayList;

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
    /* liste des output*/
    private ArrayList<InitialTreatmentOutput> listOutput;

    // CONSTRUCTOR
    public InitialActionTreatment(long dbKey, String code, String libelle, boolean isSetting, InitialActionVariable variable, boolean draw, boolean repeat, int nbDataProd, ArrayList<InitialTreatmentOutput> listOutput) {
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

    public ArrayList<InitialTreatmentOutput> getListOutput() {
        return listOutput;
    }

    public void setListOutput(ArrayList<InitialTreatmentOutput> listOutput) {
        this.listOutput = listOutput;
    }

    

    // OVERRIDE
    @Override
    public Object clone() {
        InitialActionTreatment a = (InitialActionTreatment) super.clone() ;

        ArrayList<InitialTreatmentOutput> list = new ArrayList();
        for (int i=0; i<listOutput.size(); i++){
            list.add((InitialTreatmentOutput)this.listOutput.get(i).clone());
        }
        a.setListOutput(list);
        a.setNbDataProd(this.nbDataProd);
        return a;
    }

}

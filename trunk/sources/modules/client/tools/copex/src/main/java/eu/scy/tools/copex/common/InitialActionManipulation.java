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
    /* liste des output*/
    private ArrayList<InitialManipulationOutput> listOutput;

    // CONSTRUCTOR
    public InitialActionManipulation(long dbKey, String code, String libelle, boolean isSetting, InitialActionVariable variable, boolean draw,  boolean repeat, int nbMaterialProd, ArrayList<InitialManipulationOutput> listOutput) {
        super(dbKey, code, libelle, isSetting, variable, draw, repeat);
        this.nbMaterialProd = nbMaterialProd;
        this.listOutput = listOutput ;
    }

    // GETTER AND SETTER
    public int getNbMaterialProd() {
        return nbMaterialProd;
    }

    public void setNbMaterialProd(int nbMaterialProd) {
        this.nbMaterialProd = nbMaterialProd;
    }

    public ArrayList<InitialManipulationOutput> getListOutput() {
        return listOutput;
    }

    public void setListOutput(ArrayList<InitialManipulationOutput> listOutput) {
        this.listOutput = listOutput;
    }

    

    // OVERRIDE
    @Override
    public Object clone() {
        InitialActionManipulation a = (InitialActionManipulation) super.clone() ;

        a.setNbMaterialProd(this.nbMaterialProd);
        ArrayList<InitialManipulationOutput> list = new ArrayList();
        for (int i=0; i<listOutput.size(); i++){
            list.add((InitialManipulationOutput)this.listOutput.get(i).clone());
        }
        a.setListOutput(list);

        return a;
    }

}

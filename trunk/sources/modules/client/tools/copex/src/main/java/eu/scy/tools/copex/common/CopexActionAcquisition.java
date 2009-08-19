/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.common;

import eu.scy.tools.copex.edp.EdPPanel;
import java.util.ArrayList;
import org.jdom.Element;

/**
 * action de type acquisition
 * @author Marjolaine
 */
public class CopexActionAcquisition extends CopexActionParam{
    // PROPERTY
    /* liste des data prod*/
    private ArrayList<QData> listDataProd;

    // CONSTRUCTOR
    public CopexActionAcquisition(String description, String comments, InitialNamedAction namedAction, ActionParam[] tabParam, ArrayList<QData> listDataProd) {
        super(description, comments, namedAction, tabParam);
        this.listDataProd = listDataProd;
    }

    public CopexActionAcquisition(long dbKey, String name, String description, String comments, String taskImage, Element draw, boolean isVisible, TaskRight taskRight, long dbkeyBrother, long dbKeyChild, InitialNamedAction namedAction, ActionParam[] tabParam, ArrayList<QData> listDataProd, TaskRepeat taskRepeat) {
        super(dbKey, name, description, comments, taskImage, draw, isVisible, taskRight, dbkeyBrother, dbKeyChild, namedAction, tabParam, taskRepeat);
        this.listDataProd = listDataProd;
    }

    public CopexActionAcquisition(long dbKey, String name, String description, String comments, String taskImage, Element draw, boolean isVisible, TaskRight taskRight, InitialNamedAction namedAction, ActionParam[] tabParam, ArrayList<QData> listDataProd, TaskRepeat taskRepeat) {
        super(dbKey, name, description, comments, taskImage,draw,  isVisible, taskRight, namedAction, tabParam, taskRepeat);
        this.listDataProd = listDataProd;
    }


    // GETTER AND SETTER
    public ArrayList<QData> getListDataProd() {
        return listDataProd;
    }

    public void setListDataProd(ArrayList<QData> listDataProd) {
        this.listDataProd = listDataProd;
    }
    // OVERRIDE
    @Override
    public Object clone() {
        CopexActionAcquisition a = (CopexActionAcquisition) super.clone() ;
        ArrayList<QData> listDataProdC = new ArrayList();
        int nbD = this.listDataProd.size() ;
        for (int i=0; i<nbD; i++){
            listDataProdC.add((QData)this.listDataProd.get(i).clone());
        }
        a.setListDataProd(listDataProdC);
        return a;
    }

    @Override
    public String toDescription(EdPPanel edP) {
        String s = super.toDescription(edP);
        int nbDataProd = listDataProd.size();
        for (int i=0; i<nbDataProd; i++){
            s += "\n "+((InitialActionAcquisition)this.namedAction).getListOutput().get(i).getTextProd()+" : "+listDataProd.get(i).getName();
        }
        return s;
    }
}

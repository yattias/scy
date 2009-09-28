/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.common;

import eu.scy.tools.copex.edp.CopexPanel;
import java.util.ArrayList;
import org.jdom.Element;

/**
 * action de type acquisition
 * @author Marjolaine
 */
public class CopexActionTreatment extends CopexActionParam{
    // PROPERTY
    /* liste des data prod ou de liste de data prod */
    private ArrayList<Object> listDataProd;


    //CONSTRUCTOR
    public CopexActionTreatment(String description, String comments, InitialNamedAction namedAction, Object[] tabParam, ArrayList<Object> listDataProd) {
        super(description, comments, namedAction, tabParam);
        this.listDataProd = listDataProd;
    }

    public CopexActionTreatment(long dbKey, String name, String description, String comments, String taskImage, Element draw, boolean isVisible, TaskRight taskRight, long dbkeyBrother, long dbKeyChild, InitialNamedAction namedAction, Object[] tabParam, ArrayList<Object> listDataProd, TaskRepeat taskRepeat) {
        super(dbKey, name, description, comments, taskImage, draw, isVisible, taskRight, dbkeyBrother, dbKeyChild, namedAction, tabParam, taskRepeat);
        this.listDataProd = listDataProd;
    }

    public CopexActionTreatment(long dbKey, String name, String description, String comments, String taskImage, Element draw, boolean isVisible, TaskRight taskRight, InitialNamedAction namedAction, Object[] tabParam, ArrayList<Object> listDataProd, TaskRepeat taskRepeat) {
        super(dbKey, name, description, comments, taskImage, draw, isVisible, taskRight, namedAction, tabParam, taskRepeat);
        this.listDataProd = listDataProd;
    }

    // GETTER AND SETTER
    public ArrayList<Object> getListDataProd() {
        return listDataProd;
    }

    public void setListDataProd(ArrayList<Object> listDataProd) {
        this.listDataProd = listDataProd;
    }

    // OVERRIDE
    @Override
    public Object clone() {
        CopexActionTreatment a = (CopexActionTreatment) super.clone() ;
        ArrayList<Object> listDataProdC = new ArrayList();
        int nbD = this.listDataProd.size() ;
        for (int i=0; i<nbD; i++){
            if(this.listDataProd.get(i) instanceof QData){
                listDataProdC.add((QData)((QData)this.listDataProd.get(i)).clone());
            }else if (this.listDataProd.get(i) instanceof ArrayList){
                int n = ((ArrayList)this.listDataProd.get(i)).size();
                ArrayList<QData> l = new ArrayList();
                for (int j=0; j<n;j++){
                    l.add((QData)((ArrayList<QData>)this.listDataProd.get(i)).get(j).clone());
                }
                listDataProdC.add(l);
            }
        }
        a.setListDataProd(listDataProdC);
        return a;
    }

    @Override
    public String toDescription(CopexPanel edP) {
        String s = super.toDescription(edP);
        int nbDataProd = listDataProd.size();
        for (int i=0; i<nbDataProd; i++){
            if (this.listDataProd.get(i) instanceof QData){
                s += "\n"+((InitialActionTreatment)this.namedAction).getListOutput().get(i).getTextProd()+" : "+((QData)listDataProd.get(i)).getName();
            }else if (this.listDataProd.get(i) instanceof ArrayList){
                int n = ((ArrayList)this.listDataProd.get(i)).size();
                s += "\n"+((InitialActionTreatment)this.namedAction).getListOutput().get(i).getTextProd()+" : ";
                for (int j=0; j<n; j++){
                    s += ((ArrayList<QData>)this.listDataProd.get(i)).get(j).getName()+" | ";
                }
            }
        }
        return s;
    }

}

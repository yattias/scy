/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.common;

import java.sql.Date;
import java.util.ArrayList;

/**
 * protocole initial de la mission
 * @author MBO
 */
public class InitialProcedure extends ExperimentalProcedure {

    // PROPERTY
    /*code */
    private String code;
    /* liste action nommees */
    private ArrayList<InitialNamedAction> listNamedAction;
    /* action libre autorisee */
    private boolean isFreeAction;
     /* liste du materiel disponible associe */
    private ArrayList<Material> listMaterial ;

    // CONSTRUCTOR
    public InitialProcedure(long dbKey, String name, Date dateLastModification, boolean isActiv, char right, String code, boolean isFreeAction, ArrayList<InitialNamedAction> listNamedAction) {
        super(dbKey, name, dateLastModification, isActiv, right);
        this.isFreeAction = isFreeAction ;
        this.code = code;
        this.listNamedAction = listNamedAction ;
    }

     //GETTER AND SETTER
    public boolean isFreeAction() {
        return isFreeAction;
    }

    public void setFreeAction(boolean isFreeAction) {
        this.isFreeAction = isFreeAction;
    }

    public ArrayList<InitialNamedAction> getListNamedAction() {
        return listNamedAction;
    }

    public void setListNamedAction(ArrayList<InitialNamedAction> listNamedAction) {
        this.listNamedAction = listNamedAction;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public ArrayList<Material> getListMaterial() {
        return listMaterial;
    }

    public void setListMaterial(ArrayList<Material> listMaterial) {
        this.listMaterial = listMaterial;
    }

    @Override
    public Object clone()  {
         InitialProcedure p = (InitialProcedure) super.clone() ;
            p.setFreeAction(this.isFreeAction);
            p.setCode(new String(this.code));
            ArrayList<InitialNamedAction> l = null;
            if (this.listNamedAction != null){
                l = new ArrayList();
                int nb = this.listNamedAction.size() ;
                for (int i=0; i<nb; i++){
                    l.add((InitialNamedAction)this.listNamedAction.get(i).clone());
                }
            }
            p.setListNamedAction(l);
            if (listMaterial != null){
                ArrayList<Material> listMC = new ArrayList();
                int nb = listMaterial.size();
                for (int i=0; i<nb; i++){
                    listMC.add((Material)listMaterial.get(i).clone());
                }
                p.setListMaterial(listMC);
            }
            return p;
        
    }

}

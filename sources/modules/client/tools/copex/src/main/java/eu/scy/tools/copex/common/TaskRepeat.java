/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.common;

import java.util.ArrayList;


/**
 * repeat of an action or a step (group of task)
 * @author Marjolaine
 */
public class TaskRepeat implements Cloneable {
    // ATTRIBUTS
    /* identifiant de la répétition */
    private long dbKey;
    /*nombre de repetition */
    private int nbRepeat;
    /*liste des parametres à faire varier */
    private ArrayList<InitialActionParam> listParam;
    /* liste des valeurs de ces paramètres pour chaque itération */
    private ArrayList<ActionParam[]> listParamValue;

    // CONSTRUCTOR
    public TaskRepeat(long dbKey, int nbRepeat, ArrayList<InitialActionParam> listParam, ArrayList<ActionParam[]> listParamValue) {
        this.dbKey = dbKey;
        this.nbRepeat = nbRepeat;
        this.listParam = listParam;
        this.listParamValue = listParamValue;
    }

    public TaskRepeat(long dbKey, int nbRepeat,  ArrayList<ActionParam> listParam) {
        this.dbKey = dbKey;
        this.nbRepeat = nbRepeat;
        setParams(listParam);
    }

    

   
    // GETTER AND SETTER
    public long getDbKey() {
        return dbKey;
    }

    public void setDbKey(long dbKey) {
        this.dbKey = dbKey;
    }

    public ArrayList<InitialActionParam> getListParam() {
        return listParam;
    }

    public void setListParam(ArrayList<InitialActionParam> listParam) {
        this.listParam = listParam;
    }

    public ArrayList<ActionParam[]> getListParamValue() {
        return listParamValue;
    }

    public void setListParamValue(ArrayList<ActionParam[]> listParamValue) {
        this.listParamValue = listParamValue;
    }

    public int getNbRepeat() {
        return nbRepeat;
    }

    public void setNbRepeat(int nbRepeat) {
        this.nbRepeat = nbRepeat;
    }

    
   

     // OVERRIDE
    @Override
    protected Object clone() throws CloneNotSupportedException {
       try {
            TaskRepeat tr = (TaskRepeat) super.clone() ;

            tr.setDbKey(this.dbKey);
            tr.setNbRepeat(this.nbRepeat);
            if(listParam == null){
                tr.setListParam(null);
            }else{
                ArrayList<InitialActionParam> listP = new ArrayList();
                int nb = this.listParam.size();
                for (int i=0; i<nb; i++){
                    listP.add((InitialActionParam)this.listParam.get(i).clone());
                }
            }
            if(this.listParamValue == null){
                tr.setListParamValue(null);
            }else{
                ArrayList<ActionParam[]> listV = new ArrayList();
                int nb = this.listParamValue.size();
                for (int i=0; i<nb; i++){
                    int nbP = this.listParamValue.get(i).length;
                    ActionParam[] p = new ActionParam[nbP];
                    for (int j=0; j<nbP; j++){
                        p[j] = (ActionParam)this.listParamValue.get(i)[j].clone();
                    }
                    listV.add(p);
                }
                tr.setListParamValue(listV);
            }
            
            return tr;
        } catch (CloneNotSupportedException e) {
            // this shouldn't happen, since we are Cloneable
            throw new InternalError();
        }
    }

    /* construis la lsite des parametres*/
    private void setParams(ArrayList<ActionParam> listActionParams){
        int nb = listActionParams.size();
        this.listParam = new ArrayList();
        this.listParamValue = new ArrayList();
        for (int i=0; i<nb; i++){
            InitialActionParam ip = listActionParams.get(i).getInitialParam() ;
            int id = getIndexOfInitialParam(ip.getDbKey());
            if(id == -1){
                this.listParam.add(ip);
                ActionParam[] tabP = new ActionParam[nbRepeat];
                tabP[0] = listActionParams.get(i);
                this.listParamValue.add(tabP);
            }else{
                ActionParam[] tabP = this.listParamValue.get(id);
                for (int j=0; j<tabP.length; j++){
                    if (tabP[j] == null){
                        tabP[j] = listActionParams.get(i);
                    }
                }
            }
        }
    }

    /*indice du parametre initial, -1 sinon */
    private int getIndexOfInitialParam(long dbkey){
        int nb = this.listParam.size();
        for (int i=0; i<nb; i++){
            if (this.listParam.get(i).getDbKey() == dbkey)
                return i;
        }
        return -1;
    }

}

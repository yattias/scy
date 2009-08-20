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
    /* identifiant de la repetition */
    private long dbKey;
    /*nombre de repetition */
    private int nbRepeat;
    /* liste des parametres a modifier : de type InitialActionParam ou InitialOutput- si null et nbrepeat > 1 => aucun */
    private ArrayList<TaskRepeatParam> listParam;
   

    // CONSTRUCTOR
    public TaskRepeat(long dbKey, int nbRepeat) {
        this.dbKey = dbKey;
        this.nbRepeat = nbRepeat;
        this.listParam = new ArrayList();
    }

    public TaskRepeat(long dbKey, int nbRepeat, ArrayList<TaskRepeatParam> listParam) {
        this.dbKey = dbKey;
        this.nbRepeat = nbRepeat;
        this.listParam = listParam;
    }

   

   
    // GETTER AND SETTER
    public long getDbKey() {
        return dbKey;
    }

    public void setDbKey(long dbKey) {
        this.dbKey = dbKey;
    }

   
    public int getNbRepeat() {
        return nbRepeat;
    }

    public void setNbRepeat(int nbRepeat) {
        this.nbRepeat = nbRepeat;
    }

    public ArrayList<TaskRepeatParam> getListParam() {
        return listParam;
    }

    public void setListParam(ArrayList<TaskRepeatParam> listParam) {
        this.listParam = listParam;
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
                ArrayList<TaskRepeatParam> list = new ArrayList();
                for (int i=0; i<this.listParam.size(); i++){
                    list.add((TaskRepeatParam)this.listParam.get(i).clone());
                }
                tr.setListParam(list);
            }
            
            return tr;
        } catch (CloneNotSupportedException e) {
            // this shouldn't happen, since we are Cloneable
            throw new InternalError();
        }
    }

    
}

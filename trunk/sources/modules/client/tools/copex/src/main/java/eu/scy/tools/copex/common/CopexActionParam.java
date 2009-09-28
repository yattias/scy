/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.common;

import eu.scy.tools.copex.edp.CopexPanel;
import java.util.ArrayList;
import org.jdom.Element;


/**
 * action parametree
 * @author Marjolaine
 */
public class CopexActionParam extends CopexActionNamed implements Cloneable{
    // PROPERTY
    /* liste des parametres s'il s'agit d'une action parametree, si repetition => tableau de parametres */
    protected Object[] tabParam;

    // CONSTRUCTOR
    public CopexActionParam(long dbKey, String name, String description, String comments, String taskImage,Element draw,  boolean isVisible, TaskRight taskRight, InitialNamedAction namedAction, Object[] tabParam, TaskRepeat taskRepeat) {
        super(dbKey, name, description, comments, taskImage, draw, isVisible, taskRight, namedAction, taskRepeat);
        this.tabParam = tabParam;
    }

    public CopexActionParam(long dbKey, String name, String description, String comments, String taskImage,Element draw, boolean isVisible, TaskRight taskRight, long dbkeyBrother, long dbKeyChild, InitialNamedAction namedAction, Object[] tabParam, TaskRepeat taskRepeat) {
        super(dbKey, name, description, comments, taskImage, draw, isVisible, taskRight, dbkeyBrother, dbKeyChild, namedAction, taskRepeat);
        this.tabParam = tabParam;
    }

    public CopexActionParam(String description, String comments, InitialNamedAction namedAction, Object[] tabParam) {
        super(description, comments, namedAction);
        this.tabParam = tabParam;
    }

   // GETTER AND SETTER
    public Object[] getTabParam() {
        return tabParam;
    }

    public void setTabParam(Object[] tabParam) {
        this.tabParam = tabParam;
    }



    //METHOD
    /* retourne le nombre de param */
    public int getNbParam(){
        return this.tabParam.length ;
    }

    
    @Override
     public String toDescription(CopexPanel edP){
         String s = "";
         int nbParam = tabParam.length ;
         boolean repeatParam = false;
         for (int i=0; i<nbParam; i++){
            String t = namedAction.getVariable().getTextLibelle(i);
            s += t;
            if(tabParam[i] instanceof ActionParam){
                ActionParam param = (ActionParam)tabParam[i] ;
                if (param instanceof ActionParamQuantity){
                    s += " " +Double.toString(((ActionParamQuantity)param).getParameter().getValue())+" "+((ActionParamQuantity)param).getParameter().getUnit().getSymbol()+" ";
                }else if (param instanceof ActionParamMaterial){ // material
                    s += " "+((ActionParamMaterial)param).getMaterial().getName()+" ";
                }else if (param instanceof ActionParamData){
                    s += " "+((ActionParamData)param).getData().getName() +" ";
                }
            }else if (tabParam[i] instanceof ArrayList){
                // nom du parametres
                repeatParam = true;
                s += " ("+this.namedAction.getVariable().getTabParam()[i].getParamName()+") ";
            }else{
                if(tabParam[i] == null)
                    System.out.println("toDescription, null");
                else
                    System.out.println("toDescription : "+tabParam[i].getClass());
            }
         }
         s += " "+ namedAction.getVariable().getTextLibelle(-1);
         if(repeatParam){
             s+= "\n"+edP.getBundleString("LABEL_REPEAT_MODIFY_PARAM_TREE")+" ";
             for (int i=0; i<nbParam; i++){
                 if(tabParam[i] instanceof ArrayList){
                     s += "\n"+this.namedAction.getVariable().getTabParam()[i].getParamName()+" = ";
                     int nb = ((ArrayList)tabParam[i]).size();
                     for (int j=0; j<nb; j++){
                         s+= ((ArrayList<ActionParam>)tabParam[i]).get(j).toDescription()+" | ";
                     }
                 }
             }
         }
         return s;
     }

    // OVERRIDE
    @Override
    public Object clone() {
        CopexActionParam a = (CopexActionParam) super.clone() ;
        Object[] tabParamC = new Object[this.tabParam.length];
        for (int i=0; i<tabParam.length; i++){
            if(tabParam[i] instanceof ActionParam)
                tabParamC[i] = (ActionParam)((ActionParam)tabParam[i]).clone();
            else if(tabParam[i] instanceof ArrayList){
                ArrayList<ActionParam> t = (ArrayList<ActionParam>)tabParam[i];
                ArrayList<ActionParam> t2 = new ArrayList();
                int n = t.size();
                for (int j=0; j<n; j++){
                    t2.add((ActionParam)t.get(j).clone());
                }
                tabParamC[i] = t2;
            }
        }
        a.setTabParam(tabParamC);
        return a;
    }
    
    @Override
    protected void updateParameter(){
        super.updateParameter();
        if(getTaskRepeat() != null){
            ArrayList<ActionParam> l = null;
            for (int i=0; i<tabParam.length; i++){
                InitialActionParam initActionParam =  namedAction.getVariable().getTabParam()[i] ;
                l = getTaskRepeat().getListActionParam(initActionParam);
                if(l != null && l.size() > 0){
                    tabParam[i] = l;
                }
            }
        }
    }

     
}

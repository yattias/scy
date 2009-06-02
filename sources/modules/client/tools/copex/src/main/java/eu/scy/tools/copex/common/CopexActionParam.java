/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.common;

import eu.scy.tools.copex.edp.EdPPanel;
import org.jdom.Element;


/**
 * action parametree
 * @author Marjolaine
 */
public class CopexActionParam extends CopexActionNamed implements Cloneable{
    // PROPERTY
    /* liste des parametres s'il s'agit d'une action parametree */
    protected ActionParam[] tabParam;

    // CONSTRUCTOR
    public CopexActionParam(long dbKey, String name, String description, String comments, String taskImage,Element draw,  boolean isVisible, TaskRight taskRight, InitialNamedAction namedAction, ActionParam[] tabParam, TaskRepeat taskRepeat) {
        super(dbKey, name, description, comments, taskImage, draw, isVisible, taskRight, namedAction, taskRepeat);
        this.tabParam = tabParam;
    }

    public CopexActionParam(long dbKey, String name, String description, String comments, String taskImage,Element draw, boolean isVisible, TaskRight taskRight, long dbkeyBrother, long dbKeyChild, InitialNamedAction namedAction, ActionParam[] tabParam, TaskRepeat taskRepeat) {
        super(dbKey, name, description, comments, taskImage, draw, isVisible, taskRight, dbkeyBrother, dbKeyChild, namedAction, taskRepeat);
        this.tabParam = tabParam;
    }

    public CopexActionParam(String description, String comments, InitialNamedAction namedAction, ActionParam[] tabParam) {
        super(description, comments, namedAction);
        this.tabParam = tabParam;
    }

   // GETTER AND SETTER
    public ActionParam[] getTabParam() {
        return tabParam;
    }

    public void setTabParam(ActionParam[] tabParam) {
        this.tabParam = tabParam;
    }



    //METHOD
    /* retourne le nombre de param */
    public int getNbParam(){
        return this.tabParam.length ;
    }

    
    @Override
     public String toDescription(EdPPanel edP){
         String s = "";
         int nbParam = tabParam.length ;
         for (int i=0; i<nbParam; i++){
            String t = namedAction.getVariable().getTextLibelle(i);
            s += t;
            ActionParam param = tabParam[i] ;
            if (param instanceof ActionParamQuantity){
                s += " " +((ActionParamQuantity)param).getParameter().getValue()+" "+((ActionParamQuantity)param).getParameter().getUnit().getSymbol()+" ";
            }else if (param instanceof ActionParamMaterial){ // material
                s += " "+((ActionParamMaterial)param).getMaterial().getName()+" ";
            }else if (param instanceof ActionParamData){
                s += " "+((ActionParamData)param).getData().getName() +" ";
            }
         }
         s += " "+ namedAction.getVariable().getTextLibelle(-1);
         return s;
     }

    // OVERRIDE
    @Override
    public Object clone() {
        CopexActionParam a = (CopexActionParam) super.clone() ;
        ActionParam[] tabParamC = new ActionParam[this.tabParam.length];
        for (int i=0; i<tabParam.length; i++){
            tabParamC[i] = (ActionParam)tabParam[i].clone();
        }
        a.setTabParam(tabParamC);
        return a;
    }
}

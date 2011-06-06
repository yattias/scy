/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.common;

import eu.scy.client.tools.copex.edp.CopexPanel;
import eu.scy.client.tools.copex.utilities.CopexUtilities;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdom.Element;
import org.jdom.JDOMException;


/**
 * action parametree
 * @author Marjolaine
 */
public abstract class CopexActionParam extends CopexActionNamed implements Cloneable{
    public final static String TAG_ACTION_PARAM_LIST ="action_param_list";

    /* liste des parametres s'il s'agit d'une action parametree, si repetition => tableau de parametres */
    protected Object[] tabParam;
    private final Logger logger = Logger.getLogger(CopexActionParam.class.getName());

    // CONSTRUCTOR
    public CopexActionParam(long dbKey, List<LocalText> listName, List<LocalText> listDescription, List<LocalText> listComments, String taskImage,Element draw,  boolean isVisible, TaskRight taskRight, InitialNamedAction namedAction, Object[] tabParam, TaskRepeat taskRepeat) {
        super(dbKey, listName, listDescription, listComments, taskImage, draw, isVisible, taskRight, namedAction, taskRepeat);
        this.tabParam = tabParam;
    }

    public CopexActionParam(long dbKey,Locale locale, String name,String description, String comment, String taskImage,Element draw,  boolean isVisible, TaskRight taskRight, InitialNamedAction namedAction, Object[] tabParam, TaskRepeat taskRepeat) {
        super(dbKey, locale, name, description, comment, taskImage, draw, isVisible, taskRight, namedAction, taskRepeat);
        this.tabParam = tabParam;
    }

    public CopexActionParam(long dbKey, List<LocalText> listName, List<LocalText> listDescription, List<LocalText> listComments, String taskImage,Element draw, boolean isVisible, TaskRight taskRight, long dbkeyBrother, long dbKeyChild, InitialNamedAction namedAction, Object[] tabParam, TaskRepeat taskRepeat) {
        super(dbKey, listName, listDescription, listComments, taskImage, draw, isVisible, taskRight, dbkeyBrother, dbKeyChild, namedAction, taskRepeat);
        this.tabParam = tabParam;
    }

    public CopexActionParam(List<LocalText> listDescription, List<LocalText> listComments, InitialNamedAction namedAction, Object[] tabParam) {
        super(listDescription, listComments, namedAction);
        this.tabParam = tabParam;
    }

    public CopexActionParam(Element xmlElem) throws JDOMException {
        super(xmlElem);
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
        return toDescription(edP.getLocale());
//        NumberFormat numberFormat = NumberFormat.getNumberInstance(edP.getLocale());
//        numberFormat.setMaximumFractionDigits(Integer.MAX_VALUE);
//        numberFormat.setGroupingUsed(false);
//        Locale locale = edP.getLocale();
//         String s = "";
//         int nbParam = tabParam.length ;
//         boolean repeatParam = false;
//         for (int i=0; i<nbParam; i++){
//            String t = namedAction.getVariable().getTextLibelle(locale, i);
//            s += t;
//            if(tabParam[i] instanceof ActionParam){
//                ActionParam param = (ActionParam)tabParam[i] ;
//                if (param instanceof ActionParamQuantity){
//                    String txt = CopexUtilities.getText(((ActionParamQuantity)param).getParameter().getUnit().getListSymbol(), locale);
//                    s += " " +numberFormat.format(((ActionParamQuantity)param).getParameter().getValue())+" "+txt+" ";
//                }else if (param instanceof ActionParamMaterial){ // material
//                    s += " "+CopexUtilities.getText(((ActionParamMaterial)param).getMaterial().getListName(), locale)+" ";
//                }else if (param instanceof ActionParamData){
//                    s += " "+((ActionParamData)param).getData().getName(locale) +" ";
//                }
//            }else if (tabParam[i] instanceof ArrayList){
//                // nom du parametres
//                repeatParam = true;
//                s += " ("+this.namedAction.getVariable().getTabParam()[i].getParamName(locale)+") ";
//            }else{
//                if(tabParam[i] == null) {
//                    logger.log(Level.SEVERE, "CopexActionParam.toDescription, null param");
//		} else {
//                    logger.log(Level.SEVERE, "CopexActionParam.toDescription, param "+tabParam[i].getClass());
//		}
//            }
//         }
//         s += " "+ namedAction.getVariable().getTextLibelle(edP.getLocale(), -1);
//         if(repeatParam){
//             s+= "\n"+edP.getBundleString("LABEL_REPEAT_MODIFY_PARAM_TREE")+" ";
//             for (int i=0; i<nbParam; i++){
//                 if(tabParam[i] instanceof ArrayList){
//                     s += "\n"+this.namedAction.getVariable().getTabParam()[i].getParamName(edP.getLocale())+" = ";
//                     int nb = ((ArrayList)tabParam[i]).size();
//                     for (int j=0; j<nb; j++){
//                         s+= ((ArrayList<ActionParam>)tabParam[i]).get(j).toDescription(edP.getLocale())+" | ";
//                     }
//                 }
//             }
//         }
//         return s;
     }

    @Override
    public String toDescription(Locale locale){
        NumberFormat numberFormat = NumberFormat.getNumberInstance(locale);
        numberFormat.setMaximumFractionDigits(Integer.MAX_VALUE);
        numberFormat.setGroupingUsed(false);
         String s = "";
         int nbParam = tabParam.length ;
         boolean repeatParam = false;
         for (int i=0; i<nbParam; i++){
            String t = namedAction.getVariable().getTextLibelle(locale, i);
            s += t;
            if(tabParam[i] instanceof ActionParam){
                ActionParam param = (ActionParam)tabParam[i] ;
                if (param instanceof ActionParamQuantity){
                    String txt = CopexUtilities.getText(((ActionParamQuantity)param).getParameter().getUnit().getListSymbol(), locale);
                    s += " " +numberFormat.format(((ActionParamQuantity)param).getParameter().getValue())+" "+txt+" ";
                }else if (param instanceof ActionParamMaterial){ // material
                    s += " "+CopexUtilities.getText(((ActionParamMaterial)param).getMaterial().getListName(), locale)+" ";
                }else if (param instanceof ActionParamData){
                    s += " "+((ActionParamData)param).getData().getName(locale) +" ";
                }
            }else if (tabParam[i] instanceof ArrayList){
                // nom du parametres
                repeatParam = true;
                //s += " ("+this.namedAction.getVariable().getTabParam()[i].getParamName(locale)+") ";
                s += " | ";
                int nb = ((ArrayList)tabParam[i]).size();
                for (int j=0; j<nb; j++){
                    s+= ((ArrayList<ActionParam>)tabParam[i]).get(j).toDescription(locale)+" | ";
                }
            }else{
                if(tabParam[i] == null) {
                    // System.out.println("toDescription, null");
                    logger.log(Level.SEVERE, "CopexActionParam.toDescription, null param");
                } else {
                    // System.out.println("toDescription : "+tabParam[i].getClass());
                    logger.log(Level.SEVERE, "CopexActionParam.toDescription, param "+tabParam[i].getClass());
		}
            }
         }
         s += " "+ namedAction.getVariable().getTextLibelle(locale, -1);
//         if(repeatParam){
//             //s+= "\n"+edP.getBundleString("LABEL_REPEAT_MODIFY_PARAM_TREE")+" ";
//             s+= "\n"+" ";
//             for (int i=0; i<nbParam; i++){
//                 if(tabParam[i] instanceof ArrayList){
//                     s += "\n"+this.namedAction.getVariable().getTabParam()[i].getParamName(locale)+" = ";
//                     int nb = ((ArrayList)tabParam[i]).size();
//                     for (int j=0; j<nb; j++){
//                         s+= ((ArrayList<ActionParam>)tabParam[i]).get(j).toDescription(locale)+" | ";
//                     }
//                 }
//             }
//         }
         return s;
    }

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

    @Override
    public Element toXML(){
        Element e = super.toXML();
        return getXML(e);
    }
    @Override
    public Element toXML(Element e){
        return getXML(super.toXML(e));
    }

    
    protected Element getXML(Element element){
         for (int i=0; i<tabParam.length; i++){
            if(tabParam[i] instanceof ActionParam)
                element.addContent(((ActionParam)tabParam[i]).toXML());
            else if(tabParam[i] instanceof ArrayList){
                Element e = new Element(TAG_ACTION_PARAM_LIST);
                int nb = ((ArrayList<ActionParam>)tabParam[i]).size();
                for (int k=0; k<nb; k++){
                    e.addContent(((ArrayList<ActionParam>)tabParam[i]).get(k).toXML());
                }
                element.addContent(e);
            }
        }
        return element;
    }

    @Override
    public List<Material> getMaterialUsed(){
        List<Material> listM = new LinkedList();
        for (int i=0; i<tabParam.length; i++){
            if(tabParam[i] instanceof ActionParamMaterial){
                listM.add(((ActionParamMaterial)tabParam[i]).getMaterial());
            } else if(tabParam[i] instanceof ArrayList){
                int nb = ((ArrayList<ActionParam>)tabParam[i]).size();
                for (int k=0; k<nb; k++){
                    if(((ArrayList<ActionParam>)tabParam[i]).get(k) instanceof ActionParamMaterial){
                        listM.add(((ActionParamMaterial)((ArrayList<ActionParam>)tabParam[i]).get(k)).getMaterial());
                    }
                }
            }
        }
        return listM;
    }
     
}

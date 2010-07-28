/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.common;

import java.util.Locale;
import org.jdom.Element;
import org.jdom.JDOMException;


/**
 * parametres d'une action :
 * - de type quantite
 * - de type material
 * - de type data
 * @author Marjolaine
 */
public class ActionParam implements Cloneable {
    public final static String TAG_ACTION_PARAM_ID= "id";

    /* identifiant */
    protected long dbKey;
    /* parametre initial auquel il est lie */
    protected InitialActionParam initialParam;

    // CONSTRUCTOR
    public ActionParam(long dbKey, InitialActionParam initialParam) {
        this.dbKey = dbKey;
        this.initialParam = initialParam;
    }

    public ActionParam(Element xmlElem) throws JDOMException {
		
    }

    // GETTER AND SETTER
    public long getDbKey() {
        return dbKey;
    }

    public void setDbKey(long dbKey) {
        this.dbKey = dbKey;
    }

    public InitialActionParam getInitialParam() {
        return initialParam;
    }

    public void setInitialParam(InitialActionParam initialParam) {
        this.initialParam = initialParam;
    }

    @Override
    public Object clone()  {
        try {
            ActionParam p = (ActionParam) super.clone() ;
            p.setDbKey(this.dbKey);
            p.setInitialParam((InitialActionParam)initialParam.clone());
            return p;
        } catch (CloneNotSupportedException e) {
	    // this shouldn't happen, since we are Cloneable
	    throw new InternalError();
        }
    }

    /* description dans l'arbre*/
    public String toDescription(Locale locale){
        return "";
    }

    // toXML
    public Element toXML(){
        return null;
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.common;

import java.util.Locale;
import org.jdom.Element;
import org.jdom.JDOMException;


/**
 * parameters of an action, could be
 * - quantity
 * - material
 * - data
 * @author Marjolaine
 */
public class ActionParam implements Cloneable {
    public final static String TAG_ACTION_PARAM_ID= "id";

    /* db identifier */
    protected long dbKey;
    /* initial parameter which it is linked */
    protected InitialActionParam initialParam;

    public ActionParam(long dbKey, InitialActionParam initialParam) {
        this.dbKey = dbKey;
        this.initialParam = initialParam;
    }

    public ActionParam(Element xmlElem) throws JDOMException {
		
    }

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

    /* description for the tree*/
    public String toDescription(Locale locale){
        return "";
    }

    // toXML
    public Element toXML(){
        return null;
    }
}

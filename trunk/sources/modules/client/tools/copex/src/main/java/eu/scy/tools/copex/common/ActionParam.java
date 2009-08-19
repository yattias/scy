/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.common;


/**
 * paramètres d'une action :
 * - de type quantité
 * - de type material
 * @author Marjolaine
 */
public class ActionParam implements Cloneable {
    // PROPERTY
    /* identifiant */
    protected long dbKey;
    /* parametre initial auquel il est li� */
    protected InitialActionParam initialParam;

    // CONSTRUCTOR
    public ActionParam(long dbKey, InitialActionParam initialParam) {
        this.dbKey = dbKey;
        this.initialParam = initialParam;
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
}

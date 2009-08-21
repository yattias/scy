/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.common;

/**
 * parametres d'une action initiale nommee
 *  peut etre soit de type quantite soit de type type de materiel
 * @author Marjolaine
 */
public class InitialActionParam implements Cloneable {

    // PROPERTY
    /* identifiant */
    protected long dbKey;
    /* nom du parametre */
    private String paramName;

    // CONSTRUCTOR
    public InitialActionParam(long dbKey, String paramName) {
        this.dbKey = dbKey;
        this.paramName =  paramName;
    }

    // GETTER AND SETTER
    public long getDbKey() {
        return dbKey;
    }

    public void setDbKey(long dbKey) {
        this.dbKey = dbKey;
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    @Override
    public Object clone()  {
        try {
            InitialActionParam p = (InitialActionParam) super.clone() ;
            p.setDbKey(this.dbKey);
            if(this.paramName != null)
                p.setParamName(new String(this.paramName));
            return p;
        } catch (CloneNotSupportedException e) {
            // this shouldn't happen, since we are Cloneable
            throw new InternalError();
        }
    }

    @Override
    public String toString() {
        return "("+dbKey+") "+paramName;
    }

}

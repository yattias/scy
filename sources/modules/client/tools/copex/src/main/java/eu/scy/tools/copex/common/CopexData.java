/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.common;

/**
 * represente une donnee du tableau de la feuille de donnees
 * @author MBO
 */
public class CopexData implements Cloneable{

    // ATTRIBUTS
    /* identifiant bd */
    private long dbKey;
    /* donnees */
    private String data;

    // CONSTRUCTEURS
    public CopexData(long dbKey, String data) {
        this.dbKey = dbKey;
        this.data = data;
    }

    
    // GETTER AND SETTER
    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public long getDbKey() {
        return dbKey;
    }

    public void setDbKey(long dbKey) {
        this.dbKey = dbKey;
    }

    @Override
    public Object clone() {
        try {
            CopexData ds = (CopexData) super.clone() ;
            ds.setDbKey(new Long(this.dbKey));
            ds.setData(new String(this.data));
            
            return ds;
        } catch (CloneNotSupportedException e) { 
	    // this shouldn't happen, since we are Cloneable
	    throw new InternalError();
	}
    }
    
    
}

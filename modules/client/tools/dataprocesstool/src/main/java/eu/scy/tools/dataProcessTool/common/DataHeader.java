/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.dataProcessTool.common;

/**
 * En tête du tableau 
 * @author Marjolaine   
 */
public class DataHeader implements Cloneable {
    // PROPERTY 
    /* identifiant */
    private long dbKey;
    /* valeur */
    private String value;
    /* noCol */
    private int noCol;

    // CONSTRUCTEURS
    public DataHeader(long dbKey, String value, int noCol) {
        this.dbKey = dbKey;
        this.value = value;
        this.noCol = noCol;
    }

    // GETTER AND SETTER
    public long getDbKey() {
        return dbKey;
    }

    public void setDbKey(long dbKey) {
        this.dbKey = dbKey;
    }

    public int getNoCol() {
        return noCol;
    }

    public void setNoCol(int noCol) {
        this.noCol = noCol;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    
    // CLONE
    @Override
    public Object clone()  {
        try {
            DataHeader dataheader = (DataHeader) super.clone() ;
            long dbKeyC = this.dbKey;
            String valueC = new String(this.value);
            int noColC = new Integer(this.noCol);
            
            dataheader.setDbKey(dbKeyC);
            dataheader.setValue(valueC);
            dataheader.setNoCol(noColC);
            
            return dataheader;
        } catch (CloneNotSupportedException e) { 
	    // this shouldn't happen, since we are Cloneable
	    throw new InternalError();
	}
    }

}

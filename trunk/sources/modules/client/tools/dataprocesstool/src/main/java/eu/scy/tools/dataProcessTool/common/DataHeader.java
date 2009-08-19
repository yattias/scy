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
    /* unit */
    private String unit;

    // CONSTRUCTEURS
    public DataHeader(long dbKey, String value, String unit, int noCol) {
        this.dbKey = dbKey;
        this.value = value;
        this.unit = unit;
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

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
    
    // CLONE
    @Override
    public Object clone()  {
        try {
            DataHeader dataheader = (DataHeader) super.clone() ;
            long dbKeyC = this.dbKey;
            String valueC = new String(this.value);
            int noColC = new Integer(this.noCol);
            String unitC = new String(this.unit);
            
            dataheader.setDbKey(dbKeyC);
            dataheader.setValue(valueC);
            dataheader.setUnit(unitC);
            dataheader.setNoCol(noColC);
            
            return dataheader;
        } catch (CloneNotSupportedException e) { 
	    // this shouldn't happen, since we are Cloneable
	    throw new InternalError();
	}
    }

}

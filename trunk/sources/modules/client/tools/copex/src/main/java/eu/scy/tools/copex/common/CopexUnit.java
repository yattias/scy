/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.common;

/**
 * unite de mesure
 * @author Marjolaine
 */
public class CopexUnit implements Cloneable{

    // PROPERTY
    /* identifiant  */
    private long dbKey;
    /* nom */
    private String name;
    /* symbol */
    private String symbol ;

    // CONSTRUCTOR
    public CopexUnit(long dbKey, String name, String symbol) {
        this.dbKey = dbKey;
        this.name = name;
        this.symbol = symbol;
    }

    // GETTER AND SETTER
    public long getDbKey() {
        return dbKey;
    }

    public void setDbKey(long dbKey) {
        this.dbKey = dbKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    // OVERRIDE
    @Override
    public Object clone()  {
       try {
            CopexUnit unit = (CopexUnit) super.clone() ;

            unit.setDbKey(this.dbKey);
            unit.setName(new String(this.name));
            unit.setSymbol(new String (this.symbol));
            return unit;
        } catch (CloneNotSupportedException e) {
	    // this shouldn't happen, since we are Cloneable
	    throw new InternalError();
        }
    }

}

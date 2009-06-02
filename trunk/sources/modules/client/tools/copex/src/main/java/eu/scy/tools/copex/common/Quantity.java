/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.common;

/**
 * represente une quantite
 * @author MBO
 */
public class Quantity implements Cloneable{
    // ATTRIBUTS
    /* cle primaire bd */
    private long dbKey;
    /* nom */
    private String name;
    /*type  */
    private String type;
    /* valeur */
    private double value;
    /* incertitude */
    private String uncertainty;
    /* unite */
    private CopexUnit unit;

    // CONSTRUCTEURS
    public Quantity(long dbKey, String name, String type, double value, String uncertainty, CopexUnit unit) {
        this.dbKey = dbKey;
        this.name = name;
        this.type = type;
        this.value = value;
        this.uncertainty = uncertainty;
        this.unit = unit;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getUncertainty() {
        return uncertainty;
    }

    public CopexUnit getUnit() {
        return unit;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setUncertainty(String uncertainty) {
        this.uncertainty = uncertainty;
    }

    public void setUnit(CopexUnit unit) {
        this.unit = unit;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    public long getDbKey() {
        return dbKey;
    }

    public void setDbKey(long dbKey) {
        this.dbKey = dbKey;
    }
    
     @Override
    public Object clone()  {
        try {
            Quantity quantity = (Quantity) super.clone() ;
            long dbKeyC = this.dbKey;
            String nameC = null;
            if (this.name != null)
                    nameC = new String(this.name);
            String typeC = null;
            if (this.type != null)
                    typeC = new String(this.type);
            double valueC = new Double(this.value);
            String uncertaintyC = null;
            if (this.uncertainty != null)
                    uncertaintyC = new String(this.uncertainty);
            CopexUnit unitC = null;
            if (this.unit != null)
                    unitC = (CopexUnit)this.unit.clone();
            
            quantity.setDbKey(dbKeyC);
            quantity.setName(nameC);
            quantity.setType(typeC);
            quantity.setValue(valueC);
            quantity.setUncertainty(uncertaintyC);
            quantity.setUnit(unitC);
            
            return quantity;
        } catch (CloneNotSupportedException e) { 
	    // this shouldn't happen, since we are Cloneable
	    throw new InternalError();
	}
    }
}

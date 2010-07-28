/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.dataProcessTool.common;

import eu.scy.tools.dataProcessTool.utilities.DataConstants;
import org.jdom.Element;

/**
 * En tete du tableau 
 * @author Marjolaine   
 */
public class DataHeader implements Cloneable {
    public final static String TAG_HEADER = "header";
    private final static String TAG_HEADER_UNIT = "unit";
    private final static String TAG_HEADER_NO =  "no";
    private final static String TAG_HEADER_TYPE = "type";
    private final static String TAG_HEADER_DESCRIPTION = "description";
    private final static String TAG_HEADER_FORMULA="formula";
    
    /* identifiant */
    private long dbKey;
    /* valeur */
    private String value;
    /* noCol */
    private int noCol;
    /* unit - null si de type autre que double */
    private String unit;
    /* type */
    private String type;
    /* description */
    private String description;
    /* si formule, description de la formule */
    private String formulaValue;

    // CONSTRUCTEURS
    public DataHeader(long dbKey, String value, String unit, int noCol, String type, String description, String formulaValue) {
        this.dbKey = dbKey;
        this.value = value;
        this.unit = unit;
        this.noCol = noCol;
        this.type = type;
        this.description = description;
        this.formulaValue = formulaValue;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isDouble(){
        return getType().equals(DataConstants.TYPE_DOUBLE);
    }

    public boolean isUnitNull(){
        return this.unit == null || this.unit.equals("");
    }

    public String getFormulaValue() {
        return formulaValue;
    }

    public void setFormulaValue(String formulaValue) {
        this.formulaValue = formulaValue;
    }

    public boolean isFormula() {
        return this.isDouble() && this.formulaValue != null;
    }

    // CLONE
    @Override
    public Object clone()  {
        try {
            DataHeader dataheader = (DataHeader) super.clone() ;
            long dbKeyC = this.dbKey;
            String valueC = new String(this.value);
            int noColC = new Integer(this.noCol);
            String unitC = null;
            if(unit != null)
                unitC = new String(this.unit);
            String formulaValueC = null;
            if(this.formulaValue != null){
                formulaValueC = new String(this.formulaValue);
            }
            
            dataheader.setDbKey(dbKeyC);
            dataheader.setValue(valueC);
            dataheader.setUnit(unitC);
            dataheader.setNoCol(noColC);
            dataheader.setType(new String(this.type));
            dataheader.setDescription(new String(this.description));
            dataheader.setFormulaValue(formulaValueC);
            
            return dataheader;
        } catch (CloneNotSupportedException e) { 
	    // this shouldn't happen, since we are Cloneable
	    throw new InternalError();
	}
    }

    public Element toXMLLog(){
         Element e = new Element(TAG_HEADER);
         e.addContent(new Element(TAG_HEADER_NO).setText(Integer.toString(noCol)));
         e.addContent(new Element(TAG_HEADER_UNIT).setText(unit));
         e.addContent(new Element(TAG_HEADER_TYPE).setText(type));
         e.addContent(new Element(TAG_HEADER_DESCRIPTION).setText(description));
         if(isFormula())
            e.addContent(new Element(TAG_HEADER_FORMULA).setText(formulaValue));
         return e;

     }

}

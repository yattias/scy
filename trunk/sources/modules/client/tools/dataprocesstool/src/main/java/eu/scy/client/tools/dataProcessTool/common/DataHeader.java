/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.dataProcessTool.common;

import eu.scy.client.tools.dataProcessTool.utilities.DataConstants;
import javax.swing.SwingConstants;
import org.jdom.Element;
import org.jdom.JDOMException;

/**
 * header of the dataset
 * @author Marjolaine   
 */
public class DataHeader implements Cloneable {
    public final static String TAG_HEADER = "header";
    public final static String TAG_HEADER_ID = "header_id";
    private final static String TAG_HEADER_VALUE = "value";
    private final static String TAG_HEADER_UNIT = "unit";
    private final static String TAG_HEADER_NO =  "no";
    private final static String TAG_HEADER_TYPE = "type";
    private final static String TAG_HEADER_DESCRIPTION = "description";
    private final static String TAG_HEADER_FORMULA="formula";
    private final static String TAG_HEADER_SCIENTIFIC_NOTATION = "scientific_notation";
    private final static String TAG_HEADER_NB_SHOWN_DECIMAL = "nb_shown_decimal";
    private final static String TAG_HEADER_SIGNIFICANT_DIGITS = "significant_digits";
    private final static String TAG_HEADER_DATA_ALIGNMENT = "data_alignment";
    
    /* db identifier */
    private long dbKey;
    /* value, title of the header */
    private String value;
    /* no col index */
    private int noCol;
    /* unit - null if the type is not double (String) */
    private String unit;
    /* type */
    private String type;
    /* description */
    private String description;
    /* if defined by a formula, description of the formula */
    private String formulaValue;
    /* scientific notation */
    private boolean scientificNotation;
    private int nbShownDecimals;
    private int nbSignificantDigits;
    /* alignment (left, center,right)*/
    private int dataAlignment;


    public DataHeader(long dbKey, String value, String unit, int noCol, String type, String description, String formulaValue, boolean scientificNotation, int nbShownDecimals, int nbSignificantDigits, int dataAlignment) {
        this.dbKey = dbKey;
        this.value = value;
        this.unit = unit;
        this.noCol = noCol;
        this.type = type;
        this.description = description;
        this.formulaValue = formulaValue;
        this.scientificNotation = scientificNotation;
        this.nbShownDecimals = nbShownDecimals;
        this.nbSignificantDigits = nbSignificantDigits;
        this.dataAlignment = dataAlignment;
    }

    public DataHeader(Element xmlElem) throws JDOMException {
        if (xmlElem.getName().equals(TAG_HEADER)) {
            dbKey = -1;
            try{
                dbKey = Long.parseLong(xmlElem.getChild(TAG_HEADER_ID).getText());
            }catch(NumberFormatException e){
                
            }
            this.value = xmlElem.getChild(TAG_HEADER_VALUE).getText();
            if(xmlElem.getChild(TAG_HEADER_UNIT) != null){
                unit = xmlElem.getChild(TAG_HEADER_UNIT).getText();
            }
            this.type = xmlElem.getChild(TAG_HEADER_TYPE).getText();
            this.description = xmlElem.getChild(TAG_HEADER_DESCRIPTION).getText();
            try{
                noCol = Integer.parseInt(xmlElem.getChildText(TAG_HEADER_NO));
            }catch(NumberFormatException ex){
                throw(new JDOMException("DataHeader noCol expected integer"));
            }
            formulaValue = null;
            if(xmlElem.getChild(TAG_HEADER_FORMULA) != null){
                formulaValue = xmlElem.getChild(TAG_HEADER_FORMULA).getText();
            }
            scientificNotation = false;
            if(xmlElem.getChild(TAG_HEADER_SCIENTIFIC_NOTATION) != null){
                scientificNotation = xmlElem.getChild(TAG_HEADER_SCIENTIFIC_NOTATION).getText().equals("true") ? true : false;
            }
            nbShownDecimals = DataConstants.NB_DECIMAL_UNDEFINED;
            if(xmlElem.getChild(TAG_HEADER_NB_SHOWN_DECIMAL) != null){
                try{
                    nbShownDecimals = Integer.parseInt(xmlElem.getChild(TAG_HEADER_NB_SHOWN_DECIMAL).getText());
                }catch(NumberFormatException e){
                }
            }
            nbSignificantDigits = DataConstants.NB_SIGNIFICANT_DIGITS_UNDEFINED;
            if(xmlElem.getChild(TAG_HEADER_SIGNIFICANT_DIGITS) != null){
                try{
                    nbSignificantDigits = Integer.parseInt(xmlElem.getChild(TAG_HEADER_SIGNIFICANT_DIGITS).getText());
                }catch(NumberFormatException e){
                }
            }
            this.dataAlignment = DataConstants.DEFAULT_DATASET_ALIGNMENT;
            if(xmlElem.getChild(TAG_HEADER_DATA_ALIGNMENT) != null){
                try{
                    String s = xmlElem.getChild(TAG_HEADER_DATA_ALIGNMENT).getText();
                    int f = Integer.parseInt(s);
                    if(f == SwingConstants.LEFT || f == SwingConstants.CENTER || f == SwingConstants.RIGHT)
                        this.dataAlignment = f;
                }catch(NumberFormatException e){

                }
            }
        }else {
            throw(new JDOMException("DataHeader expects <"+TAG_HEADER+"> as root element, but found <"+xmlElem.getName()+">."));
	}
    }

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

    public int getNbShownDecimals() {
        return nbShownDecimals;
    }

    public void setNbShownDecimals(int nbShownDecimals) {
        this.nbShownDecimals = nbShownDecimals;
    }

    public int getNbSignificantDigits() {
        return nbSignificantDigits;
    }

    public void setNbSignificantDigits(int nbSignificantDigits) {
        this.nbSignificantDigits = nbSignificantDigits;
    }

    public boolean isScientificNotation() {
        return scientificNotation;
    }

    public void setScientificNotation(boolean scientificNotation) {
        this.scientificNotation = scientificNotation;
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

    public int getDataAlignment() {
        return dataAlignment;
    }

    public void setDataAlignment(int dataAlignment) {
        this.dataAlignment = dataAlignment;
    }

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
            dataheader.setScientificNotation(new Boolean(scientificNotation));
            dataheader.setNbShownDecimals(new Integer(nbShownDecimals));
            dataheader.setNbSignificantDigits(new Integer(nbSignificantDigits));
            dataheader.setDataAlignment(dataAlignment);
            
            return dataheader;
        } catch (CloneNotSupportedException e) { 
	    // this shouldn't happen, since we are Cloneable
	    throw new InternalError();
	}
    }

    public Element toXMLLog(){
         Element e = new Element(TAG_HEADER);
         e.addContent(new Element(TAG_HEADER_VALUE).setText(value));
         e.addContent(new Element(TAG_HEADER_ID).setText(Long.toString(dbKey)));
         e.addContent(new Element(TAG_HEADER_NO).setText(Integer.toString(noCol)));
         if(isDouble()){
             e.addContent(new Element(TAG_HEADER_UNIT).setText(unit));
             if(isFormula())
                e.addContent(new Element(TAG_HEADER_FORMULA).setText(formulaValue));
             e.addContent(new Element(TAG_HEADER_SCIENTIFIC_NOTATION).setText(scientificNotation?"true":"false"));
             if(this.getNbShownDecimals() != DataConstants.NB_DECIMAL_UNDEFINED)
                e.addContent(new Element(TAG_HEADER_NB_SHOWN_DECIMAL).setText(Integer.toString(nbShownDecimals)));
             if(this.getNbSignificantDigits() != DataConstants.NB_SIGNIFICANT_DIGITS_UNDEFINED)
                 e.addContent(new Element(TAG_HEADER_SIGNIFICANT_DIGITS).setText(Integer.toString(nbSignificantDigits)));
         }
         e.addContent(new Element(TAG_HEADER_TYPE).setText(type));
         e.addContent(new Element(TAG_HEADER_DESCRIPTION).setText(description));
         e.addContent(new Element(TAG_HEADER_DATA_ALIGNMENT).setText(Integer.toString(dataAlignment)));
         return e;

     }

    // if the description is not empty, the symbol is "symbol: description"
    public static String computeHeaderValue(String symbol, String description){
        if(description == null || description.trim().length() == 0)
            return symbol;
        else{
            return symbol+": "+description;
        }
    }

}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.dataProcessTool.pdsELO;

import eu.scy.tools.dataProcessTool.common.DataHeader;
import eu.scy.tools.dataProcessTool.utilities.DataConstants;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import org.jdom.Element;
import org.jdom.JDOMException;
import roolo.elo.JDomStringConversion;

/**
 *
 * @author Marjolaine
 */
public class ProcessedHeader {
    /*tag name  */
    public static final String TAG_PROCESSED_HEADER = "processed_hedaer" ;
    private static final String TAG_PROCESSED_HEADER_COLUMN = "column";
    public static final String TAG_PROCESSED_HEADER_FORMULA = "formula";
    public static final String TAG_PROCESSED_HEADER_SCIENTIFIC_NOTATION = "scientific_notation";
    public static final String TAG_PROCESSED_HEADER_NB_SHOWN_DECIMALS = "nb_shown_decimals";
    public static final String TAG_PROCESSED_HEADER_NB_SIGNIFICANT_DIGITS = "nb_significant_digits";
    public static final String TAG_PROCESSED_HEADER_DATA_ALIGNMENT = "data_alignment";

    /* column id */
    private String columnId;
    /* formula */
    private String formula;
    private boolean scientificNotation;
    private int nbShownDecimals;
    private int nbSignificantDigits;
    private int dataAlignment;

    public ProcessedHeader(String columnId, String formula, boolean scientificNotation, int nbShownDecimals, int nbSignificantDigits, int dataAlignment) {
        this.columnId = columnId;
        this.formula = formula;
        this.scientificNotation = scientificNotation;
        this.nbShownDecimals = nbShownDecimals;
        this.nbSignificantDigits = nbSignificantDigits;
        this.dataAlignment = dataAlignment;
    }
    public ProcessedHeader(Element xmlElem) throws JDOMException {
        if (xmlElem.getName().equals(TAG_PROCESSED_HEADER)) {
            columnId = xmlElem.getChildText(TAG_PROCESSED_HEADER_COLUMN);
            formula = null;
            if(xmlElem.getChild(TAG_PROCESSED_HEADER_FORMULA) != null){
                formula = xmlElem.getChild(TAG_PROCESSED_HEADER_FORMULA).getText();
            }
            scientificNotation = false;
            if(xmlElem.getChild(TAG_PROCESSED_HEADER_SCIENTIFIC_NOTATION) != null){
                scientificNotation = xmlElem.getChild(TAG_PROCESSED_HEADER_SCIENTIFIC_NOTATION).getText().equals("true") ? true : false;
            }
            nbShownDecimals = DataConstants.NB_DECIMAL_UNDEFINED;
            if(xmlElem.getChild(TAG_PROCESSED_HEADER_NB_SHOWN_DECIMALS) != null){
                try{
                    nbShownDecimals = Integer.parseInt(xmlElem.getChild(TAG_PROCESSED_HEADER_NB_SHOWN_DECIMALS).getText());
                }catch(NumberFormatException e){
                }
            }
            nbSignificantDigits = DataConstants.NB_SIGNIFICANT_DIGITS_UNDEFINED;
            if(xmlElem.getChild(TAG_PROCESSED_HEADER_NB_SIGNIFICANT_DIGITS) != null){
                try{
                    nbSignificantDigits = Integer.parseInt(xmlElem.getChild(TAG_PROCESSED_HEADER_NB_SIGNIFICANT_DIGITS).getText());
                }catch(NumberFormatException e){
                }
            }
            this.dataAlignment = DataConstants.DEFAULT_DATASET_ALIGNMENT;
            if(xmlElem.getChild(TAG_PROCESSED_HEADER_DATA_ALIGNMENT) != null){
                try{
                    String s = xmlElem.getChild(TAG_PROCESSED_HEADER_DATA_ALIGNMENT).getText();
                    int f = Integer.parseInt(s);
                    if(f == SwingConstants.LEFT || f == SwingConstants.CENTER || f == SwingConstants.RIGHT)
                        this.dataAlignment = f;
                }catch(NumberFormatException e){
                    
                }
            }
        }else {
            throw(new JDOMException("ProcessedHeader expects <"+TAG_PROCESSED_HEADER+"> as root element, but found <"+xmlElem.getName()+">."));
	}
    }

    public ProcessedHeader(String xmlString) throws JDOMException {
        this(new JDomStringConversion().stringToXml(xmlString));
    }

    public String getColumnId() {
        return columnId;
    }

    public String getFormula() {
        return formula;
    }

    public int getNbShownDecimals() {
        return nbShownDecimals;
    }

    public int getNbSignificantDigits() {
        return nbSignificantDigits;
    }

    public boolean isScientificNotation() {
        return scientificNotation;
    }

    public int getDataAlignment() {
        return dataAlignment;
    }

    public void setDataAlignment(int dataAlignment) {
        this.dataAlignment = dataAlignment;
    }

    // toXML
    public Element toXML(){
        Element element = new Element(TAG_PROCESSED_HEADER);
        element.addContent(new Element(TAG_PROCESSED_HEADER_COLUMN).setText(this.columnId));
        element.addContent(new Element(TAG_PROCESSED_HEADER_DATA_ALIGNMENT).setText(Integer.toString(dataAlignment)));
	if(formula != null && !formula.equals("")){
            element.addContent(new Element(TAG_PROCESSED_HEADER_FORMULA).setText(this.formula));
        }
        element.addContent(new Element(TAG_PROCESSED_HEADER_SCIENTIFIC_NOTATION).setText(scientificNotation ? "true" : "false"));
        if(nbShownDecimals != DataConstants.NB_DECIMAL_UNDEFINED)
            element.addContent(new Element(TAG_PROCESSED_HEADER_NB_SHOWN_DECIMALS).setText(Integer.toString(nbShownDecimals)));
        if(nbSignificantDigits != DataConstants.NB_SIGNIFICANT_DIGITS_UNDEFINED)
            element.addContent(new Element(TAG_PROCESSED_HEADER_NB_SIGNIFICANT_DIGITS).setText(Integer.toString(nbSignificantDigits)));
	return element;
    }
}

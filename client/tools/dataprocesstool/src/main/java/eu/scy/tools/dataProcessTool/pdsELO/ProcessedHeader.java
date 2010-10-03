/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.dataProcessTool.pdsELO;

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

    /* column id */
    private String columnId;
    /* formula */
    private String formula;

    public ProcessedHeader(String columnId, String formula) {
        this.columnId = columnId;
        this.formula = formula;
    }
    public ProcessedHeader(Element xmlElem) throws JDOMException {
        if (xmlElem.getName().equals(TAG_PROCESSED_HEADER)) {
            columnId = xmlElem.getChildText(TAG_PROCESSED_HEADER_COLUMN);
            formula = null;
            if(xmlElem.getChild(TAG_PROCESSED_HEADER_FORMULA) != null){
                formula = xmlElem.getChild(TAG_PROCESSED_HEADER_FORMULA).getText();
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

    // toXML
    public Element toXML(){
        Element element = new Element(TAG_PROCESSED_HEADER);
        element.addContent(new Element(TAG_PROCESSED_HEADER_COLUMN).setText(this.columnId));
	if(formula != null || !formula.equals("")){
            element.addContent(new Element(TAG_PROCESSED_HEADER_FORMULA).setText(this.formula));
        }
	return element;
    }
}

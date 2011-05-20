/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.dataProcessTool.pdsELO;

import org.jdom.Element;
import org.jdom.JDOMException;
import roolo.elo.JDomStringConversion;

/**
 * data is represented by its row number and column number
 * @author Marjolaine
 */
public class Data {
    /* tag name */
    public static final String TAG_DATA = "idData";
    private static final String TAG_DATA_ROW = "row";
    private static final String TAG_DATA_COLUMN = "column";

    /* row id */
    private String rowId;
    /* column id */
    private String columnId;

    public Data(String rowId, String columnId) {
        this.rowId = rowId;
        this.columnId = columnId;
    }

     public Data(Element xmlElem) throws JDOMException {
        if (xmlElem.getName().equals(TAG_DATA)) {
            rowId = xmlElem.getChild(TAG_DATA_ROW).getText();
            columnId = xmlElem.getChild(TAG_DATA_COLUMN).getText();
	} else {
            throw(new JDOMException("Data expects <"+TAG_DATA+"> as root element, but found <"+xmlElem.getName()+">."));
	}
    }

    public Data(String xmlString) throws JDOMException {
        this(new JDomStringConversion().stringToXml(xmlString));
    }

    public String getColumnId() {
        return columnId;
    }

    public String getRowId() {
        return rowId;
    }

    // toXML
    public Element toXML(){
        Element element  = new Element(TAG_DATA);
		element.addContent(new Element(TAG_DATA_ROW).setText(rowId));
        element.addContent(new Element(TAG_DATA_COLUMN).setText(columnId));
		return element;

    }


}

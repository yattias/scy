/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.dataProcessTool.gmbl;

import eu.scy.client.tools.dataProcessTool.utilities.DataConstants;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import org.jdom.Element;
import org.jdom.JDOMException;
import roolo.elo.JDomStringConversion;

/**
 *
 * @author Marjolaine
 */
public class GmblColumn {
    private String columnTitle;

    private String unit;
    private String type;

    private List<String> cellValues;

    public GmblColumn(String columnTitle,String unit, String type, List<String> cellValues) {
        this.columnTitle = columnTitle;
        this.unit = unit;
        this.type = type;
        this.cellValues = cellValues;
    }
    
    
    public GmblColumn(Element xmlElem) throws JDOMException {
        if (xmlElem.getName().equals(GmblDataset.TAG_GMBL_DATACOLUMN)) {
            columnTitle = "";
            if(xmlElem.getChild(GmblDataset.TAG_GMBL_COLUMN_NAME) != null){
                columnTitle = xmlElem.getChild(GmblDataset.TAG_GMBL_COLUMN_NAME).getText();
            }
            unit = "";
            if(xmlElem.getChild(GmblDataset.TAG_GMBL_COLUMN_UNIT) != null){
                unit = xmlElem.getChild(GmblDataset.TAG_GMBL_COLUMN_UNIT).getText();
            }
            type = DataConstants.DEFAULT_TYPE_COLUMN;
            if(xmlElem.getChild(GmblDataset.TAG_GMBL_COLUMN_TEXT) != null){
                type = DataConstants.DEFAULT_TYPE_COLUMN;
                String s = xmlElem.getChild(GmblDataset.TAG_GMBL_COLUMN_TEXT).getText();
                if(s != null && s.equals("0")){
                    s = DataConstants.TYPE_DOUBLE;
                }else{
                    s = DataConstants.TYPE_STRING;
                }
            }
            cellValues = new LinkedList<String>();
            if(xmlElem.getChild(GmblDataset.TAG_GMBL_COLUMN_CELL) != null){
                String columnsCells = xmlElem.getChild(GmblDataset.TAG_GMBL_COLUMN_CELL).getText();
                String[] tokens = columnsCells.trim().split("\n");
                cellValues.addAll(Arrays.asList(tokens));
            }
        }else {
            throw(new JDOMException("GmblColumn expects <"+GmblDataset.TAG_GMBL_DATACOLUMN+"> as root element, but found <"+xmlElem.getName()+">."));
	}
    }

    public GmblColumn(String xmlString) throws JDOMException {
        this(new JDomStringConversion().stringToXml(xmlString));
    }

    public List<String> getCellValues() {
        return cellValues;
    }

    public String getColumnTitle() {
        return columnTitle;
    }

    public String getType() {
        return type;
    }

    public String getUnit() {
        return unit;
    }

    public void setDsName(String dsName){
        if(!dsName.equals("")){
           columnTitle = dsName+": "+columnTitle;
        }
    }
}

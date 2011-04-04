/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.dataProcessTool.gmbl;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.jdom.Element;
import org.jdom.JDOMException;
import roolo.elo.JDomStringConversion;

/**
 * gmbl file dataset
 * @author Marjolaine
 */
public class GmblDataset {
    /*tag name  */
    public static final String TAG_GMBL_DATASET = "DataSet";
    public static final String TAG_GMBL_DATASET_NAME = "DataSetName";
    public static final String TAG_GMBL_DATACOLUMN = "DataColumn";
    public static final String TAG_GMBL_COLUMN_NAME = "DataObjectName";
    public static final String TAG_GMBL_COLUMN_UNIT = "ColumnUnits";
    public static final String TAG_GMBL_COLUMN_TEXT = "ColumnTreatAsText";
    public static final String TAG_GMBL_COLUMN_TYPE = "ColumnDataType";
    public static final String TAG_GMBL_COLUMN_CELL = "ColumnCells";

    private String datasetName;
    private List<GmblColumn> columns;

    public GmblDataset(String datasetName, List<GmblColumn> columns) {
        this.datasetName = datasetName;
        this.columns = columns;
    }

    public GmblDataset(Element xmlElem) throws JDOMException {
        if (xmlElem.getName().equals(TAG_GMBL_DATASET)) {
            datasetName = "";
            if(xmlElem.getChild(TAG_GMBL_DATASET_NAME) != null){
                datasetName = xmlElem.getChild(TAG_GMBL_DATASET_NAME).getText();
            }
            columns = new LinkedList<GmblColumn>();
            for (Iterator<Element> variableElem = xmlElem.getChildren(TAG_GMBL_DATACOLUMN).iterator(); variableElem.hasNext();) {
                GmblColumn c = new GmblColumn(variableElem.next());
                c.setDsName(datasetName);
                columns.add(c);
            }
        }else {
            throw(new JDOMException("GmblDataset expects <"+GmblDataset.TAG_GMBL_DATASET+"> as root element, but found <"+xmlElem.getName()+">."));
	}
    }

    public GmblDataset(String xmlString) throws JDOMException {
        this(new JDomStringConversion().stringToXml(xmlString));
    }

    public List<GmblColumn> getColumns() {
        return columns;
    }

    public String getDatasetName() {
        return datasetName;
    }

}

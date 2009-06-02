/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.saveProcXml;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.jdom.Element;
import org.jdom.JDOMException;

/**
 *
 * @author Marjolaine
 */
public class DataSheet {
    /* tag names */
    public static final String TAG_DATASHEET = "datasheet";
    public static final String TAG_DATASHEET_ID = "id";
    public static final String TAG_DATASHEET_NB_ROWS = "nbRow" ;
    public static final String TAG_DATASHEET_NB_COLS = "nbCol" ;

    /*id*/
    private String idDataSheet;
    /* nb de lignes */
    private String nbRows;
    /* nb de  colonnes */
    private String nbCols;
    /*list row */
    private List<Row> listRow;

    public DataSheet(String idDataSheet, String nbRows, String nbCols, List listRow) {
        this.idDataSheet = idDataSheet;
        this.nbCols = nbCols ;
        this.nbRows = nbRows ;
        this.listRow = listRow;
    }



     public DataSheet(Element xmlElem) throws JDOMException {
		if (xmlElem.getName().equals(TAG_DATASHEET)) {
			idDataSheet = xmlElem.getChild(TAG_DATASHEET_ID).getText();
            nbRows = xmlElem.getChild(TAG_DATASHEET_NB_ROWS).getText();
            nbCols = xmlElem.getChild(TAG_DATASHEET_NB_COLS).getText();
            listRow = new LinkedList<Row>();
			for (Iterator<Element> variableElem = xmlElem.getChildren(Row.TAG_ROW).iterator(); variableElem.hasNext();) {
				listRow.add(new Row(variableElem.next()));
			}
		} else {
			throw(new JDOMException("DataSheet expects <"+TAG_DATASHEET+"> as root element, but found <"+xmlElem.getName()+">."));
		}
	}

    public String getIdDataSheet() {
        return idDataSheet;
    }

    public List<Row> getListRow() {
        return listRow;
    }

    public String getNbCols() {
        return nbCols;
    }

    public String getNbRows() {
        return nbRows;
    }


    
     // toXML
    public Element toXML(){
        Element element = new Element(TAG_DATASHEET);
		element.addContent(new Element(TAG_DATASHEET_ID).setText(idDataSheet));
        element.addContent(new Element(TAG_DATASHEET_NB_ROWS).setText(nbRows));
        element.addContent(new Element(TAG_DATASHEET_NB_COLS).setText(nbCols));
        for (Iterator<Row> row = listRow.iterator(); row.hasNext();) {
				element.addContent(row.next().toXML());
		}
		return element;
    }

}

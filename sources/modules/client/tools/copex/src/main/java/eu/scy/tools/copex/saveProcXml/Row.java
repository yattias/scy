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
public class Row {
    /* tag names */
    public static final String TAG_ROW = "row";

    /* liste columns*/
    private List<Column> listColumns;

    public Row(List listColumns) {
        this.listColumns = listColumns;
    }

    public Row(Element xmlElem) throws JDOMException {
		if (xmlElem.getName().equals(TAG_ROW)) {
            listColumns = new LinkedList<Column>();
			for (Iterator<Element> variableElem = xmlElem.getChildren(Column.TAG_COLUMN).iterator(); variableElem.hasNext();) {
				listColumns.add(new Column(variableElem.next()));
			}
		} else {
			throw(new JDOMException("Row expects <"+TAG_ROW+"> as root element, but found <"+xmlElem.getName()+">."));
		}
	}

    public List getListColumns() {
        return listColumns;
    }
    // toXML
    public Element toXML(){
        Element element = new Element(TAG_ROW);
        for (Iterator<Column> col = listColumns.iterator(); col.hasNext();) {
				element.addContent(col.next().toXML());
		}
		return element;
    }
}

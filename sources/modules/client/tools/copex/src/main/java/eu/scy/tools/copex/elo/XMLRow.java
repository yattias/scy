/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.elo;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.jdom.Element;
import org.jdom.JDOMException;

/**
 *
 * @author Marjolaine
 */
public class XMLRow {
    /* tag names */
    public static final String TAG_ROW = "row";

    /* liste columns*/
    private List<XMLColumn> listColumns;

    public XMLRow(List listColumns) {
        this.listColumns = listColumns;
    }

    public XMLRow(Element xmlElem) throws JDOMException {
		if (xmlElem.getName().equals(TAG_ROW)) {
            listColumns = new LinkedList<XMLColumn>();
			for (Iterator<Element> variableElem = xmlElem.getChildren(XMLColumn.TAG_COLUMN).iterator(); variableElem.hasNext();) {
				listColumns.add(new XMLColumn(variableElem.next()));
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
        for (Iterator<XMLColumn> col = listColumns.iterator(); col.hasNext();) {
				element.addContent(col.next().toXML());
		}
		return element;
    }
}

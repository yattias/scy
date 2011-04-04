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
 *
 * @author Marjolaine
 */
public class GmblDocument {
    /*tag name  */
    public static final String TAG_GMBL_DOCUMENT = "Document" ;

    private List<GmblDataset> datasets;

    public GmblDocument(List<GmblDataset> datasets) {
        this.datasets = datasets;
    }

    public GmblDocument(Element xmlElem) throws JDOMException {
        if (xmlElem.getName().equals(TAG_GMBL_DOCUMENT)) {
            datasets = new LinkedList<GmblDataset>();
            for (Iterator<Element> variableElem = xmlElem.getChildren(GmblDataset.TAG_GMBL_DATASET).iterator(); variableElem.hasNext();) {
                datasets.add(new GmblDataset(variableElem.next()));
            }
        }else {
            throw(new JDOMException("GmblDocument expects <"+TAG_GMBL_DOCUMENT+"> as root element, but found <"+xmlElem.getName()+">."));
	}
    }

    public GmblDocument(String xmlString) throws JDOMException {
        this(new JDomStringConversion().stringToXml(xmlString));
    }

    public List<GmblDataset> getColumns() {
        return datasets;
    }
}

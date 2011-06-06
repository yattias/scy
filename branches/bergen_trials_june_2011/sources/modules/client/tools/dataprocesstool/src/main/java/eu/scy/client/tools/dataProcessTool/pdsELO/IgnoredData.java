/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.dataProcessTool.pdsELO;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.jdom.Element;
import org.jdom.JDOMException;
import roolo.elo.JDomStringConversion;

/**
 * ignored data; a data is represented by its row number / column number
 * @author Marjolaine
 */
public class IgnoredData {
    /* tag name */
    public final static String TAG_IGNORED_DATA = "ignoredData" ;

    /* list data */
    private List<Data> listIgnoredData;

    public IgnoredData(List<Data> listIgnoredData) {
        this.listIgnoredData = listIgnoredData;
    }

     public IgnoredData(Element xmlElem) throws JDOMException {
         if (xmlElem != null){
                if (xmlElem.getName().equals(TAG_IGNORED_DATA)) {
                listIgnoredData = new LinkedList<Data>();
                for (Iterator<Element> variableElem = xmlElem.getChildren(Data.TAG_DATA).iterator(); variableElem.hasNext();) {
                    listIgnoredData.add(new Data(variableElem.next()));
                }
            } else {
                throw(new JDOMException("IgnoredData expects <"+TAG_IGNORED_DATA+"> as root element, but found <"+xmlElem.getName()+">."));
            }
         }
     }

    public IgnoredData(String xmlString) throws JDOMException {
        this(new JDomStringConversion().stringToXml(xmlString));
    }

    public List<Data> getListIgnoredData() {
        return listIgnoredData;
    }

    // toXML
    public Element toXML(){
        if (listIgnoredData != null && listIgnoredData.size() > 0){
            Element element = new Element(TAG_IGNORED_DATA);
            for (Iterator<Data> data = listIgnoredData.iterator(); data.hasNext();) {
                    element.addContent(data.next().toXML());
            }
            return element;
        }
		return null;
    }

}

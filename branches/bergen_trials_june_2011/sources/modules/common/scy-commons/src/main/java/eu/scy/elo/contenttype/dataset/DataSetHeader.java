package eu.scy.elo.contenttype.dataset;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.jdom.Element;
import org.jdom.JDOMException;

import roolo.elo.JDomStringConversion;
//import roolo.helper.SerializationHelper;

public class DataSetHeader implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 7291313935532912820L;
	private String name = "DataSetHeader"; // name of the node set by Jcrom
	private String path; // mandatory attribute -- requested by Jcrom

    private List<DataSetColumn> variables;
    private Locale locale;

    private Element element;
    private String elementStr;

    // This constructor is only used by jcrom for persistance purposes
    public DataSetHeader(){
    	//this.elementStr = SerializationHelper.nullSerialization;
    }

    public DataSetHeader(List<DataSetColumn> variables, Locale locale) {
        this.variables = variables;
        this.locale = locale;
       // this.elementStr = SerializationHelper.nullSerialization;
    }

    public DataSetHeader(Element xmlElem) throws JDOMException {
        if (xmlElem.getName().equals("header")) {
            variables = new LinkedList<DataSetColumn>();
            locale = new Locale(xmlElem.getAttribute("language").getValue());
            for (Iterator<Element> variableElem = xmlElem.getChildren("column").iterator(); variableElem
            .hasNext();) {
                variables.add(new DataSetColumn(variableElem.next()));
            }
        } else {
            throw(new JDOMException("DataSetHeader expects <header> as root element, but found <"+xmlElem.getName()+">."));
        }
    }

    public DataSetHeader(String xmlString) throws JDOMException {
        this(new JDomStringConversion().stringToXml(xmlString));
    }

    public Element toXML() {
    	loadIfNecessary();

        if (element == null) {
            element = new Element("header");
            element.setAttribute("language", locale.getLanguage().toString());
            for (Iterator<DataSetColumn> variable = variables.iterator(); variable.hasNext();) {
                element.addContent(variable.next().toXML());
            }
        }

        //elementStr = SerializationHelper.serializeValue(element);
        return element;
    }

    public List<DataSetColumn> getColumns() {
        return variables;
    }

    public int getColumnCount() {
        return variables.size();
    }

    public Locale getLocale() {
        return locale;
    }

    private void loadIfNecessary() {
//		if (element == null && !elementStr.equals(SerializationHelper.nullSerialization)){
//			element = (Element)SerializationHelper.unSerializeValue(elementStr);
//		}
	}
}

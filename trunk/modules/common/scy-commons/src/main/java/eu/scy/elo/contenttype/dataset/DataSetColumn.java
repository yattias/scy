package eu.scy.elo.contenttype.dataset;

import java.io.Serializable;

import org.jcrom.annotations.JcrName;
import org.jcrom.annotations.JcrPath;
import org.jcrom.annotations.JcrProperty;
import org.jdom.Element;
import org.jdom.JDOMException;

import roolo.elo.JDomStringConversion;

//TODO: implement language dependent variable descriptions
public class DataSetColumn implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3529182804495379273L;
	@JcrName private String name = "DataSetColumn"; // name of the node set by Jcrom
	@JcrPath private String path; // mandatory attribute -- requested by Jcrom	
	
    @JcrProperty private String symbol;
    @JcrProperty private String description;
    @JcrProperty private String type;
    
    public DataSetColumn(){
    	
    }
    
    public DataSetColumn(String symbol, String description, String type) {
        this.symbol = symbol;
        this.description = description;
        this.type = type;
    }
    
    public DataSetColumn(Element xmlElem) throws JDOMException {
        if (xmlElem.getName().equals("column")) {
            symbol = xmlElem.getChild("symbol").getText();
            description = xmlElem.getChild("description").getText();
            type = xmlElem.getChild("type").getText();
        } else {
            throw(new JDOMException("DataSetColumn expects <column> as root element, but found <"+xmlElem.getName()+">."));
        }
    }
    
    public DataSetColumn(String xmlString) throws JDOMException {
        this(new JDomStringConversion().stringToXml(xmlString));
    }
    
    public String getSymbol() {
        return symbol;
    }
    
    public String getDescription() {
        return description;
    }
    
    public String getType() {
        return type;
    }
    
    public Element toXML() {
        Element elem = new Element("column");
        elem.addContent(new Element("symbol").setText(symbol));
        elem.addContent(new Element("description").setText(description));
        elem.addContent(new Element("type").setText(type));
        return elem;
    }
    
}

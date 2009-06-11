package eu.scy.actionlogging.api;

import java.io.IOException;
import java.util.Properties;

import org.jdom.Content;
import org.jdom.Element;
import org.jdom.JDOMException;

public interface IAction
{
    
    /**
     * This interface describes how Actions should be logged.
     * The idea is to storage them into a Properties object (easy XML serialization)
     */
	
	/**
     * adds a new 'context property'
     * these properties are available and relevant
     * for all kinds of actions, like "tool", "mission", etc.
     * @param key	->hashmap key
     * @param value	-> hashmap value
     */
    public void addContext(String key, String value);
    
    /**
     * returns the value of 'key'
     * @param key
     * @return	value of 'key'
     */
    public String getContext(String key);
    
    public void addAttribute(String key, String value);
    
    public void addAttribute(String name, String value, String subElement);
    
    public void addAttribute(String name, String value, Content subElement);
        
    public String getAttribute(String key);
    
    /**
     * returns the XML serialized version of the property object
     * @return	XML serialized object
     */
    public String getXMLString();
    
    /**
     * returns the XML serialized version of the property object
     * @return	XML serialized object
     */
    public Element getXML();
    
    /**
     * loads properties from XML
     * @param xml
     * @throws IOException 
     * @throws JDOMException 
     */
    public void setFromXML(String xml) throws JDOMException, IOException;
    
	public void setFromXML(Element element) throws JDOMException;
}

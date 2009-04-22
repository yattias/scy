package eu.scy.actionlogging.api;

import java.util.Properties;

public interface IAction
{
    
    /**
     * This interface describes how Actions should be logged.
     * The idea is to storage them into a Properties object (easy XML serialization)
     */
    
    /**
     * adds a new 'property'
     * @param key	->hashmap key
     * @param value	-> hashmap value
     */
    public void addProperty(String key, String value);
    
    /**
     * returns the value of 'key'
     * @param key
     * @return	value of 'key'
     */
    public String getProperty(String key);
    
    /**
     * returns the properties object used to storage the data
     * @return	the properties object
     */
    public Properties getProperties();
    
    /**
     * returns the XML serialized version of the property object
     * @return	XML serialized object
     */
    public String getXML();
    
    /**
     * loads properties from XML
     * @param xml
     */
    public void setPropertiesFromXML(String xml);
}

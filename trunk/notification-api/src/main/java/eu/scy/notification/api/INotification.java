package eu.scy.notification.api;

import java.util.Properties;

public interface INotification
{
	/**
	 * the information is stored in a Properties object
	 */
	
	/**
	 * reads the XML-Serialized object and instantiates the Properties object
	 * @param xml	
	 */
	public void setPropertiesFromXML(String xml);
	
	/**
	 * returns the whole Properties object
	 * @return	whole Properties object
	 */
	public Properties getProperties();
	
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
	 * @return properties as XML
	 */
	public String getXML();
}

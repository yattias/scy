package eu.scy.notification;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Properties;

import eu.scy.notification.api.INotification;


public class Notification implements INotification
{
	private Properties properties = new Properties();
	
	
	public Notification() {
	}
	
	/**
	 * creates a new notification from XML
	 * @param xml	
	 */
	public Notification(String xml) {
		setPropertiesFromXML(xml);
	}
	
	/**
	 * returns the properties object
	 */
	public Properties getProperties()
	{
		return properties;
	}

	/**
	 * sets properties from XML,
	 * used in the cunstructor
	 * @param xml
	 */
	public void setPropertiesFromXML(String xml)
	{
		InputStream in = new ByteArrayInputStream(xml.getBytes());
		try {
			properties.loadFromXML(in);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * returns XML
	 */
	public String getXML()
	{
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try {
			properties.storeToXML(os, null);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return os.toString();
	}

	/**
	 * adds a key<>value pair
	 * @param key
	 * @param value
	 */
	public void addProperty(String key, String value) {
		properties.setProperty(key, value);
		
	}

	/**
	 * @param key
	 * @return value of 'key'
	 */
	public String getProperty(String key) {
		return properties.getProperty(key);
	}

}

package eu.scy.common.datasync;

import java.util.Map;

import org.dom4j.Element;

public interface ISyncObject {

	/**
	 * returns the id of this ISyncObject
	 * 
	 * @return the unique id of this ISyncObject
	 */
	public String getID();
	
	/**
	 * sets the id of this ISyncObject
	 * 
	 * @param id
	 */
	public void setID(String id);
	
	/**
	 * @return the ISyncObject creation time in milliseconds-since-1970
	 */
	public long getObjectCreatedTime();

	/**
	 * sets the creation time of the ISyncObject
	 * typically set automatically in the constructor
	 * 
	 * @param timestamp
	 */
	public void setObjectCreatedTime(long timestamp);
	
	/**
	 * @return the timestamp (in milliseconds) of the last modification of this ISyncObject
	 */
	public long getLastChangeTime();
	
	/**
	 * sets the time of the last modification
	 * this is typically done internally
	 * 
	 * @param timestamp
	 */
	public void setLastChangeTime(long timestamp);
	
	/**
	 * @return
	 */
	public String getToolname();

	/**
	 * @param toolname
	 */
	public void setToolname(String toolname);
	
	/**
	 * @return
	 */
	public String getUserId();

	/**
	 * @param userId
	 */
	public void setUserId(String userId);
	
	/**
	 * @param key
	 * @param value
	 */
	public void setProperty(String key, String value);
	
	/**
	 * @param key
	 * @return
	 */
	public String getProperty(String key);
	
	/**
	 * @return
	 */
	public Map<String,String> getProperties();

	/**
	 * @return
	 */
	public Element toXMLElement();
	
	/**
	 * @return
	 */
	public String toXMLString();
	
}

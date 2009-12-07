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
	public long getCreationTime();

	/**
	 * sets the creation time of the ISyncObject
	 * typically set automatically in the constructor
	 * 
	 * @param timestamp
	 */
	public void setCreationTime(long timestamp);
	
	/**
	 * @return the timestamp (in milliseconds) of the last modification of this ISyncObject
	 */
	public long getLastModificationTime();
	
	/**
	 * sets the time of the last modification
	 * this is typically done internally
	 * 
	 * @param timestamp
	 */
	public void setLastModificationTime(long timestamp);
	
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
	public String getCreator();

	/**
	 * @param userId
	 */
	public void setCreator(String userId);
	
	/**
	 * @return
	 */
	public String getLastModificator();
	
	/**
	 * @param userId
	 */
	public void setLastModificator(String userId);
	
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

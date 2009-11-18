package eu.scy.common.datasync;

import java.util.Map;

import org.dom4j.Element;

public interface ISyncObject {

	public String getID();
	
	public void setID(String id);
	
	public long getObjectCreatedTime();

	public void setObjectCreatedTime(long timestamp);
	
	public long getLastChangeTime();
	
	public void setLastChangeTime(long timestamp);
	
	public String getToolname();

	public void setToolname(String toolname);
	
	public String getUserId();

	public void setUserId(String userId);
	
	public void setProperty(String key, String value);
	
	public String getProperty(String key);
	
	public Map<String,String> getProperties();

	public Element toXMLElement();
	
	public String toXMLString();
	
}

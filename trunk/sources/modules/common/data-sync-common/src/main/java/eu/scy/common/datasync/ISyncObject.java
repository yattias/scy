package eu.scy.common.datasync;

import java.util.Map;

import org.dom4j.Element;

public interface ISyncObject {

	public String getID();
	
	public long getObjectCreatedTime();
	
	public long getLastChangeTime();
	
	public String getToolname();
	
	public String getUsername();
	
	public void setProperty(String key, String value);
	
	public String getProperty(String key);
	
	public Map<String,String> getProperties();

	public Element toXMLElement();
	
	public String toXMLString();
}

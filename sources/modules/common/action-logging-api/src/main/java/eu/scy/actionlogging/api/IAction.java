package eu.scy.actionlogging.api;

import java.util.Map;


public interface IAction {

    String NOT_AVAILABLE = "n/a";

    /**
     * This interface describes how Actions should be logged.
     */

	public String getId();
	
	/**
     * adds a new 'context property'
     * these properties are available and relevant
     * for all kinds of actions, like "tool", "mission", etc.
     * @param key	->property key
     * @param value	-> property value
     * @throws IllegalArgumentException if the value is null
     */
    public void addContext(ContextConstants constant, String value);
    
    /**
     * returns the value of 'key'
     * @param key
     * @return	value of 'key'
     */
    public String getContext(ContextConstants constant);
    
    /**
     * 
     * @param key
     * @param value
     * 
     * @throws IllegalArgumentException if the value is null
     */
    public void addAttribute(String key, String value);
    
    public String getAttribute(String key);
    
    public Map<String, String> getAttributes();
    
    /**
     * 
     * @param context
     * @throws IllegalArgumentException if one of the values inside the context is null
     */
    public void setContext(IContext context);
    
    public IContext getContext();
    
    public void setUser(String user);
    
    public String getUser();
    
    /*@Deprecated
    public void setTime(String time);
    @Deprecated
    public String getTime();
    */
    public void setTimeInMillis(long time);
    
    public long getTimeInMillis();
    
    public void setType(String type);
    
    public String getType();
    
    /*@Deprecated
    public void setDataType(String dataType);
    
    @Deprecated
    public String getDataType();
    
    @Deprecated
    public void setData(String data);
    
    @Deprecated
    public String getData();
    */
}

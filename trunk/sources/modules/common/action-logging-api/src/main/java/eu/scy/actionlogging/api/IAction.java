package eu.scy.actionlogging.api;


public interface IAction
{
    
    /**
     * This interface describes how Actions should be logged.
     */
	
	/**
     * adds a new 'context property'
     * these properties are available and relevant
     * for all kinds of actions, like "tool", "mission", etc.
     * @param key	->property key
     * @param value	-> property value
     */
    public void addContext(ContextConstants constant, String value);
    
    /**
     * returns the value of 'key'
     * @param key
     * @return	value of 'key'
     */
    public String getContext(ContextConstants constant);
    
    public void addAttribute(String key, String value);
    
    public String getAttribute(String key);
    
}

package eu.scy.actionlogging.api;

public interface IContext {

	/**
	 * Returns the context value for the given constant 
	 * 
	 * @param constant
	 * @return context value as String
	 */
	public String get(ContextConstants constant);
	
	/**
	 * Sets the context value for the given constant
	 * 
	 * @param constant
	 * @param value
	 */
	public void set(ContextConstants constant, String value);

}

package eu.scy.agents.api;

public interface IParameter {

	/**
	 * Set a parameter
	 * 
	 * @param <T>
	 * @param name
	 *            The name of the parameter.
	 * @param object
	 *            The value of the parameter
	 */
	public <T> void set(String name, T object);

	/**
	 * Read an object back from the ADB
	 * 
	 * @param <T>
	 * @param key
	 *            The key which identifies the object.
	 * @return The object identified by <code>key</code> or null if not
	 *         existent.
	 */
	public <T> T get(String key);
}

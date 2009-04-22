package eu.scy.agents.api;

/**
 * An interface to allow access to the Agent Database (ADB).
 * 
 * @author fschulz
 */
public interface IPersistentStorage {

	/**
	 * Write a key value pair into the persistent Storage
	 * 
	 * @param <T>
	 * @param key The key to identify the object written to the ADB
	 * @param object The value to write to the ADB.
	 */
	public <T> void put(String key, T object);

	/**
	 * Read an object back from the ADB
	 * 
	 * @param <T>
	 * @param key The key which identifies the object.
	 * @return The object identified by <code>key</code> or null if not existent.
	 */
	public <T> T get(String key);

}

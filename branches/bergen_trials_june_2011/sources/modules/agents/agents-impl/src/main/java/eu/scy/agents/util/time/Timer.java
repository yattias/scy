package eu.scy.agents.util.time;

/**
 * Abstraction for System.currentTimeMillis. Mainly done so unit tests can use predefined times.
 * @author fschulz
 *
 */
public interface Timer {

	/**
	 * Returns the current time in millis
	 * @return
	 */
	public long currentTimeMillis();

}

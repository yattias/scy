package eu.scy.agents.api;

/**
 * Exception that flags that something went wrong with the agent lifecycle.
 * 
 * @author Florian Schulz
 * 
 */
public class AgentLifecycleException extends Exception {

	private static final long serialVersionUID = -3129535954611820111L;

	/**
	 * Create a new exception depicting <code>message</code> as error.
	 * 
	 * @param message
	 *            The error message.
	 */
	public AgentLifecycleException(String message) {
		super(message);
	}

	/**
	 * Create a new exception depicting <code>message</code> and
	 * <code>cause</code> as error.
	 * 
	 * @param message
	 *            The error message.
	 * @param cause
	 *            The underlying exception that is re-thrown.
	 */
	public AgentLifecycleException(String message, Throwable cause) {
		super(message, cause);
	}

}

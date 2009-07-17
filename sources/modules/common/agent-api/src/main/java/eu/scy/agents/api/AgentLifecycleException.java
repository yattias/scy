package eu.scy.agents.api;

/**
 * Exception that flags that something went wrong with the agent lifecycle.
 * 
 * @author Florian Schulz
 * 
 */
public class AgentLifecycleException extends Exception {

	private static final long serialVersionUID = -3129535954611820111L;

	public AgentLifecycleException(String message) {
		super(message);
	}
	
        public AgentLifecycleException(String message, Throwable cause) {
            super(message, cause);
        }

}

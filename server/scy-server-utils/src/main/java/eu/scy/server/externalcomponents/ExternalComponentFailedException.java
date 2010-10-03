package eu.scy.server.externalcomponents;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 05.nov.2009
 * Time: 13:32:24
 * To change this template use File | Settings | File Templates.
 */
public class ExternalComponentFailedException extends Exception {

	/**
	 * 
	 */
	public ExternalComponentFailedException() {
		super();
	}

	/**
	 * @param message
	 * @param cause
	 */
	public ExternalComponentFailedException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public ExternalComponentFailedException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public ExternalComponentFailedException(Throwable cause) {
		super(cause);
	}
}

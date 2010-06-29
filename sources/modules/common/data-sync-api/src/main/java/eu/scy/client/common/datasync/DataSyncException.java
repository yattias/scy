/**
 * 
 */
package eu.scy.client.common.datasync;

/**
 * @author giemza
 *
 */
public class DataSyncException extends Exception {

	/**
	 * Default constructor
	 */
	public DataSyncException() {
		super();
	}

	/**
	 * @param message
	 */
	public DataSyncException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public DataSyncException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public DataSyncException(String message, Throwable cause) {
		super(message, cause);
	}

}

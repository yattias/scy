/*
 * Created on 01.okt.2004
 *
 * 
 */
package eu.scy.colemo.server.exceptions;

/**
 *
 * 
 */
public class ClassNameAlreadyExistException extends Exception {

	/**
	 * 
	 */
	public ClassNameAlreadyExistException() {
		super();
		
	}

	/**
	 * @param arg0
	 */
	public ClassNameAlreadyExistException(String arg0) {
		super(arg0);
		
	}

	/**
	 * @param arg0
	 */
	public ClassNameAlreadyExistException(Throwable arg0) {
		super(arg0);
		
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public ClassNameAlreadyExistException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		
	}

}

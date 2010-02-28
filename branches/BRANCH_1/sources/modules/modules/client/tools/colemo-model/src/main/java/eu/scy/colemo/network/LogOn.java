/*
 * Created on 07.okt.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package eu.scy.colemo.network;

import java.io.Serializable;


/**
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class LogOn implements Serializable{
	private Person person;
	//private InetAddress ip;
	/**
	 * 
	 */
	public LogOn(Person person) {
		
		this.person=person;
		//this.ip=ip;
	}

	/**
	 * @return Returns the person.
	 */
	public Person getPerson() {
		return person;
	}
	/**
	public InetAddress getIp() {
	    return ip;
	}*/
}

/*
 * Created on 07.okt.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package eu.scy.colemo.network;

import java.io.Serializable;
import java.net.InetAddress;

/**
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class NetworkMessage implements Serializable{

	private InetAddress ip;
	private Object object;
	private String action;
	private Person person;
	
	/**
	 * 
	 */
	public NetworkMessage(InetAddress ip,Person person, Object object, String action) {
		this.ip = ip;
		this.object = object;
		this.person = person;
		this.action = action;
	}
	public InetAddress getIp() {
		return ip;
	}
	
	public Object getObject() {
		return object;
	}
	public String getAction() {
		return action;
	}
	
	public Person getPerson() {
		return person;
	}
}

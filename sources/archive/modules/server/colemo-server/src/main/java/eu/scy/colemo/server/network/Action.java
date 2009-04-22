package eu.scy.colemo.server.network;

import eu.scy.colemo.network.Person;

import java.net.InetAddress;


/**
 * @author Øystein
 *
 * 
 */
public class Action {

	private Person person;
	private long time;
	private String action;
	private InetAddress ip;
	
	public Action(InetAddress ip,Person person,long time, String action) {
		this.ip=ip;
		this.person=person;
		this.time=time;
		this.action=action;
	}
	public String toString() {
		return new String("IP: "+ip+", User: "+person.getUserName()+", Action: "+action+", Time: "+time);
	}

	/**
	 * @return Returns the person.
	 */
	public Person getPerson() {
		return person;
	}
	
	/**
	 * @return Returns the ip.
	 */
	public InetAddress getIp() {
		return ip;
	}
	/**
	 * @return Returns the action.
	 */
	public String getAction() {
		return action;
	}
	/**
	 * @return Returns the time.
	 */
	public long getTime() {
		return time;
	}
}

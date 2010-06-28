/*
 * Created on 09.nov.2004
 *
 */
package eu.scy.colemo.contributions;

import eu.scy.colemo.network.Person;

import java.io.Serializable;
import java.net.InetAddress;

/**
 *
 */
public class AddLink extends BaseConceptMapNode implements Serializable, Contribution {

	private String from;
	private String to;
	private String user;
	private InetAddress ip;
	private Person person;
	
	public AddLink(String from, String to,String user,InetAddress ip, Person person) {
		this.from=from;
		this.to=to;
		this.user=user;
		this.person=person;
		this.ip=ip;
	}

	
	/**
	 * @return Returns the from.
	 */
	public String getFrom() {
		return from;
	}
	/**
	 * @return Returns the to.
	 */
	public String getTo() {
		return to;
	}
	/**
	 * @return Returns the user.
	 */
	public String getUser() {
		return user;
	}
	/* (non-Javadoc)
	 * @see eu.scy.colemo.contributions.Contribution#getIp()
	 */
	public InetAddress getIp() {
		return ip;
	}

	/* (non-Javadoc)
	 * @see eu.scy.colemo.contributions.Contribution#getPerson()
	 */
	public Person getPerson() {
		return person;
	}
}

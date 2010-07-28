/*
 * Created on 13.nov.2004
 *
 */
package eu.scy.colemo.contributions;

import eu.scy.colemo.network.Person;

import java.io.Serializable;
import java.net.InetAddress;

/**
 *
 */
public class Chat implements Serializable, Contribution{
	
	private String input;
	private InetAddress ip;
	private Person person;
	
	public Chat(String input, InetAddress ip, Person person) {
		this.input=input;
		this.ip=ip;
		this.person=person;
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

	/**
	 * @return Returns the input.
	 */
	public String getInput() {
		return input;
	}
}

/*
 * Created on 10.nov.2004
 *
 * 
 */
package eu.scy.colemo.contributions;

import java.io.Serializable;
import java.net.InetAddress;

import eu.scy.colemo.server.uml.UmlLink;
import eu.scy.colemo.network.Person;

/**
  *
 * 
 */
public class DeleteLink implements Serializable,Contribution{

	private UmlLink umlLink;
	private InetAddress ip;
	private Person person;
	
	public DeleteLink(UmlLink umlLink,InetAddress ip, Person person) {
		this.umlLink=umlLink;
		this.person=person;
		this.person=person;
		this.ip=ip;
	}

	/**
	 * @return Returns the umlLink.
	 */
	public UmlLink getUmlLink() {
		return umlLink;
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
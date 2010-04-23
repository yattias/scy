/*
 * Created on 10.nov.2004
 *
 * 
 */
package eu.scy.colemo.contributions;

import java.io.Serializable;
import java.net.InetAddress;

import eu.scy.colemo.server.uml.UmlAssociation;
import eu.scy.colemo.network.Person;

/**
 *
 * 
 */
public class DeleteAssociation implements Serializable,Contribution{

	private UmlAssociation umlAssociation;
	private InetAddress ip;
	private Person person;
	
	public DeleteAssociation(UmlAssociation umlAssociation,InetAddress ip,Person person) {
		this.umlAssociation=umlAssociation;
		this.person=person;
		this.person=person;
		this.ip=ip;
	}

	/**
	 * @return Returns the umlLink.
	 */
	public UmlAssociation getUmlAssociation() {
		return umlAssociation;
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
/*
 * Created on 10.nov.2004
 *
 */
package eu.scy.colemo.contributions;

import java.io.Serializable;
import java.net.InetAddress;

import eu.scy.colemo.server.uml.UmlClass;
import eu.scy.colemo.network.Person;

/**
  *
 * 
 */
public class DeleteClass implements Serializable,Contribution{

	private UmlClass umlClass;
	private InetAddress ip;
	private Person person;
	
	public DeleteClass(UmlClass umlClass,InetAddress ip,Person person) {
		this.umlClass=umlClass;
		this.person=person;
		this.ip=ip;
	}

	/**
	 * @return Returns the umlClass.
	 */
	public UmlClass getUmlClass() {
		return umlClass;
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

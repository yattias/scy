/*
 * Created on 09.nov.2004
 *
 */
package eu.scy.colemo.contributions;

import java.io.Serializable;
import java.net.InetAddress;

import eu.scy.colemo.server.uml.UmlClass;
import eu.scy.colemo.network.Person;

/**
 *
 */
public class AddMethod implements Serializable, Contribution{

	private UmlClass umlClass;
	private String method;
	private InetAddress ip;
	private Person person;
	
	public AddMethod(UmlClass umlClass,String method,InetAddress ip,Person person) {
		this.umlClass=umlClass;
		this.method=method;
		this.person=person;
		this.ip=ip;
	}

	
	/**
	 * @return Returns the umlClass.
	 */
	public UmlClass getUmlClass() {
		return umlClass;
	}
	/**
	 * @return Returns the method.
	 */
	public String getMethod() {
		return method;
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


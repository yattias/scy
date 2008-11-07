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
 * @author �ystein
 *
 */
public class AddField implements Serializable, Contribution {

	private UmlClass umlClass;
	private String field;
	private InetAddress ip;
	private Person person;
	
	public AddField(UmlClass umlClass,String field,InetAddress ip,Person person) {
		this.umlClass=umlClass;
		this.field=field;
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
	 * @return Returns the field.
	 */
	public String getField() {
		return field;
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


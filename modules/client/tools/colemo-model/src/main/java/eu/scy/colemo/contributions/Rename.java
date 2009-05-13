/*
 * Created on 15.nov.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package eu.scy.colemo.contributions;

import java.io.Serializable;
import java.net.InetAddress;

import eu.scy.colemo.server.uml.UmlClass;
import eu.scy.colemo.network.Person;

/**
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Rename implements Serializable, Contribution{

	private UmlClass umlClass;
	private InetAddress ip;
	private Person person;
	private String newName;
	private String type;
	private String oldName;
	
	public Rename(UmlClass umlClass,String type,String newName,String oldName,InetAddress ip, Person person) {
		this.umlClass=umlClass;
		this.ip=ip;
		this.person=person;
		this.newName=newName;
		this.type=type;
		this.oldName=oldName;
		
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
	 * @return Returns the newName.
	 */
	public String getNewName() {
		return newName;
	}
	/**
	 * @return Returns the umlClass.
	 */
	public UmlClass getUmlClass() {
		return umlClass;
	}
	/**
	 * @return Returns the type.
	 */
	public String getType() {
		return type;
	}
	/**
	 * @return Returns the oldName.
	 */
	public String getOldName() {
		return oldName;
	}
}

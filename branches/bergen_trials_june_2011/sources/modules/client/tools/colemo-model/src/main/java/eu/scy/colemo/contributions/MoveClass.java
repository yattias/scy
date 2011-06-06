/*
 * Created on 10.nov.2004
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
public class MoveClass extends BaseConceptMapNode implements Serializable {

	private UmlClass umlClass;
	private InetAddress ip;
	private Person person;
	
	public MoveClass(UmlClass umlClass,InetAddress ip,Person person) {
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

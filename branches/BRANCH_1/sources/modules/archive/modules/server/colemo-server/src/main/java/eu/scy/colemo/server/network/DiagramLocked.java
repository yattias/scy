/*
 * Created on 25.okt.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package eu.scy.colemo.server.network;

import eu.scy.colemo.network.Person;

import java.io.Serializable;

/**
 * @author Øystein
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DiagramLocked implements Serializable {

	private Person person;
	private long time;
	private boolean locked;
	
	public DiagramLocked(Person person, long time,boolean locked) {
		this.person=person;
		this.time=time;
		this.locked=locked;
	}

	/**
	 * @return Returns the person.
	 */
	public Person getPerson() {
		return person;
	}
	/**
	 * @return Returns the time.
	 */
	public long getTime() {
		return time;
	}
	/**
	 * @return Returns the locked.
	 */
	public boolean isLocked() {
		return locked;
	}
}

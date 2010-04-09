/*
 * Created on 06.okt.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package eu.scy.colemo.network;

import eu.scy.core.model.impl.SCYUserImpl;

import java.io.Serializable;

/**
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Person extends SCYUserImpl implements Serializable{

	private boolean typing = false;
	
	public Person(String userName) {
		//getUserDetails().setUsername(userName);
	}

	/**
	 * @return Returns the typing.
	 */
	public boolean isTyping() {
		return typing;
	}
	/**
	 * @param typing The typing to set.
	 */
	public void setTyping(boolean typing) {
		this.typing = typing;
	}
}

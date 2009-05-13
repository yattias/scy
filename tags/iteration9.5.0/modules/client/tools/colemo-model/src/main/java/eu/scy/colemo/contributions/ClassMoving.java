/*
 * Created on 23.nov.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package eu.scy.colemo.contributions;

import java.io.Serializable;
import java.net.InetAddress;

import eu.scy.colemo.server.uml.UmlClass;

/**
 *
 * 
 */
public class ClassMoving implements Serializable {

	private UmlClass umlClass;
	private InetAddress ip;
	
	public ClassMoving(UmlClass umlClass,InetAddress ip) {
		this.umlClass=umlClass;
		this.ip=ip;
	}

	/**
	 * @return Returns the umlClass.
	 */
	public UmlClass getUmlClass() {
		return umlClass;
	}
	/**
	 * @return Returns the ip.
	 */
	public InetAddress getIp() {
		return ip;
	}
}

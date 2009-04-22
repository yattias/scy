/*
 * Created on 19.okt.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package eu.scy.colemo.server.agent;

import java.io.Serializable;
import java.net.InetAddress;

/**
 * @author Øystein
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class StartVote implements Serializable{
	private InetAddress ip;
	private String user;
	private String clas;
	
	public StartVote(InetAddress ip,String user, String clas) {
		this.ip=ip;
		this.user=user;
		this.clas=clas;
		
	}

	/**
	 * @return Returns the clas.
	 */
	public String getClas() {
		return clas;
	}
	/**
	 * @return Returns the ip.
	 */
	public InetAddress getIp() {
		return ip;
	}
	/**
	 * @return Returns the user.
	 */
	public String getUser() {
		return user;
	}
}

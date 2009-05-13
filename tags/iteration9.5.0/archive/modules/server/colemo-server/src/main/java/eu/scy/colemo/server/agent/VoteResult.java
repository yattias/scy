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
public class VoteResult implements Serializable{

	private int yes=0;
	private int no=0;
	private int max=0;
	private InetAddress ip;
	private String user;
	private String clas;
	
	public VoteResult(InetAddress ip,String user,String clas) {
		this.ip=ip;
		this.clas=clas;
		this.user=user;
	}
	
	
	public void add(int a) {
		if(a==-1){
			max--;
		}
		else if (a==0){
			yes++;
		}
		else no++;
	}
	/**
	 * @param max The max to set.
	 */
	public void setMax(int max) {
		this.max = max;
	}
	
	/**
	 * @return Returns the no.
	 */
	public int getNo() {
		return no;
	}
	/**
	 * @return Returns the yes.
	 */
	public int getYes() {
		return yes;
	}
	/**
	 * @return Returns the max.
	 */
	public int getMax() {
		return max;
	}
	/**
	 * @return Returns the ip.
	 */
	public InetAddress getIp() {
		return ip;
	}
	/**
	 * @return Returns the clas.
	 */
	public String getClas() {
		return clas;
	}
	/**
	 * @return Returns the user.
	 */
	public String getUser() {
		return user;
	}
}

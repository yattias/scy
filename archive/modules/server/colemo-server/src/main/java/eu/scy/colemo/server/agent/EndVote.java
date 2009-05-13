/*
 * Created on 20.okt.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package eu.scy.colemo.server.agent;

import java.io.Serializable;

/**
 * @author Øystein
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class EndVote implements Serializable{

	private String clas;
	private boolean delete;
	public EndVote(String clas, boolean delete) {
		this.clas=clas;
		this.delete=delete;
	}

	/**
	 * @return Returns the clas.
	 */
	public String getClas() {
		return clas;
	}
	/**
	 * @return Returns the delete.
	 */
	public boolean isDelete() {
		return delete;
	}
}

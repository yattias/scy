/*
 * Created on 16.nov.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package eu.scy.colemo.contributions;

import java.io.Serializable;

/**
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class UserTyping implements Serializable{

	private String user;
	private boolean typing;
	public UserTyping(String user,boolean typing) {
		this.user=user;
		this.typing=typing;
	}

	/**
	 * @return Returns the user.
	 */
	public String getUser() {
		return user;
	}
	/**
	 * @return Returns the typing.
	 */
	public boolean isTyping() {
		return typing;
	}
}

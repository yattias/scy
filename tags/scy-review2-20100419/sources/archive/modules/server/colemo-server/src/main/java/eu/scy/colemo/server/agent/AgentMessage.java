/*
 * Created on 14.okt.2004
 *
 */
package eu.scy.colemo.server.agent;

import java.io.Serializable;

/**
 * @author Øystein
 *
 */
public class AgentMessage implements Serializable{
	
	private String message;
	private boolean dialogMode;
	
	public AgentMessage(String message, boolean dialogMode) {
		this.message=message;
		this.dialogMode=dialogMode;
	}

	/**
	 * @return Returns the dialogMode.
	 */
	public boolean isDialogMode() {
		return dialogMode;
	}
	/**
	 * @return Returns the message.
	 */
	public String getMessage() {
		return message;
	}
}

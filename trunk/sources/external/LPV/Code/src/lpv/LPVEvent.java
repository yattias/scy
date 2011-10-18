package lpv;

import java.util.EventObject;

public class LPVEvent extends EventObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8860290180139301428L;

	private boolean bNewUser;
	private String newUserName;

	private int newUserIndex;
	
	public LPVEvent(Object arg0) {
		super(arg0);
		
	}

	protected void setbNewUser(boolean bNewUser) {
		this.bNewUser = bNewUser;
	}

	public boolean isNewUser() {
		return bNewUser;
	}

	protected void setNewUserName(String newUserName) {
		this.newUserName = newUserName;
		setbNewUser(true);
	}

	public String getNewUserName() {
		return newUserName;
	}

	public int getNewUserIndex() {
		return newUserIndex;
	}
	
	protected void setNewUserIndex(int index) {
		newUserIndex = index;
		setbNewUser(true);
		
	}
	

}

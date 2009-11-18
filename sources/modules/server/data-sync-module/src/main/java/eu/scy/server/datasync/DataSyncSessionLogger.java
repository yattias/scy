package eu.scy.server.datasync;

import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;
import info.collide.sqlspaces.commons.User;

import java.util.ArrayList;
import java.util.List;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.muc.MultiUserChat;

import eu.scy.common.datasync.SyncAction;

/**
 * This class logs all actions from a synchronized session into the tuplespace.
 * 
 * @author giemza
 */
public class DataSyncSessionLogger {

	private String id;
	private String toolId;
	private List<String> users;
	
	private TupleSpace loggingSpace;

	public DataSyncSessionLogger(String id) {
		this.id = id;
		users = new ArrayList<String>();
	}

	public void connect(XMPPConnection connection) throws Exception {
		MultiUserChat muc = new MultiUserChat(connection, id);
		muc.create("datasynclistener");
		Form form = new Form(Form.TYPE_SUBMIT);
		muc.sendConfigurationForm(form);
		
		loggingSpace = new TupleSpace(new User("SyncSessionLogger@" + id), "scy.collide.info", 2525, id);
	}
	
	public void log(SyncAction action) {
		// now we map the syncaction to the syncactiontuple (sat)
		Tuple sat = new Tuple();
		sat.add("syncaction");
		sat.add(action.getId());
		sat.add(action.getUserId());
		sat.add(action.getSessionId());
		sat.add(action.getType());
		sat.add(action.getTimestamp());
		try {
			loggingSpace.write(sat);
		} catch (TupleSpaceException e) {
			e.printStackTrace();
		}
	}

	public void setToolId(String toolName) {
		this.toolId = toolName;
	}

	public String getToolId() {
		return this.toolId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setUsers(List<String> users) {
		this.users = users;
	}

	public void addUser(String userName) {
		if (userName != null && !this.users.contains(userName)) {
			users.add(userName);
		}
	}

	public List<String> getUsers() {
		return this.users;
	}

	public void removeUser(String userName) {
		if (userName != null && this.users.contains(userName)) {
			this.users.remove(userName);
		}
	}
}

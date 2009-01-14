package eu.scy.actionlogging.logger;

import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Field;
import info.collide.sqlspaces.commons.Tuple;
import eu.scy.actionlogging.api.IAction;
import eu.scy.actionlogging.api.IActionLogger;

public class ActionLogger implements IActionLogger {
	
	//default settings:
	private String host = "127.0.0.1";
	private int port = 2525;
	private String space = "SCYSpace-actionlogging";
	//
	private TupleSpace ts = null;
	private Field user = null;

	/**
	 * simple constructor for an actionlogger
	 * @param user	user throwing actions (NOT! the tool)
	 */
	public ActionLogger(String user) {
		this.user = new Field(user);
		connect();
	}
	
	/**
	 * extended constructor
	 * @param host	SQLSpaces host
	 * @param port	SQLSpaces port	
	 * @param space	SQLSPace 
	 * @param user	user throwing actions (NOT! the tool)
	 */
	public ActionLogger(String host, int port, String space, String user) {
		this.user = new Field(user);
		this.host = host;
		this.port = port;
		this.space = space;
		connect();
	}

	/**
	 * creates a connection to the SQLSPaces server
	 */
	private void connect() {
		try {
			ts = new TupleSpace(host, port, space);
			System.out.println("actionlogger verbunden");
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * logs an action
	 * @param tool	the tool throwing the action
	 * @param action IAction thrown
	 */
	public void log(String tool, IAction action) {
		Field content = new Field(action.getXML());
		Field timestamp = new Field(new java.sql.Timestamp(new java.util.Date().getTime()).toString());
		Tuple message = new Tuple(user, tool, timestamp, content);
		try {
			ts.write(message);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

}

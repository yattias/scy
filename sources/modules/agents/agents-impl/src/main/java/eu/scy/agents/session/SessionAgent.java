package eu.scy.agents.session;

import info.collide.sqlspaces.commons.Field;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;

import java.rmi.dgc.VMID;
import java.util.Map;

import org.apache.log4j.Logger;

import eu.scy.actionlogging.ActionTupleTransformer;
import eu.scy.actionlogging.api.ContextConstants;
import eu.scy.actionlogging.api.IAction;
import eu.scy.agents.api.AgentLifecycleException;
import eu.scy.agents.impl.AbstractRequestAgent;
import eu.scy.agents.impl.ActionConstants;
import eu.scy.agents.impl.AgentProtocol;

/**
 * An agent that tracks the locations of users. Which mission and which las they
 * are in.
 * 
 * The agent can be queried for:
 * <ul>
 * <li>Which LAS a user is in: ("userInfoRequest","query", <QueryId>:String,
 * <Mission>:String, "lasOfUser", <UserName>:String) -> ("userInfoRequest",
 * "response", <QueryId>:String, <Las>:String)</li>
 * <li>What users are in a LAS: ("userInfoRequest","query", <QueryId>:String,
 * <Mission>:String, "userInLas", <LASId>:String) -> ("userInfoRequest",
 * "response", <QueryId>:String, <User>:String*)</li>
 * <li>What users are in a Mission: ("userInfoRequest","query",
 * <QueryId>:String, <Mission>:String, "userInMission") -> ("userInfoRequest",
 * "response", <QueryId>:String, <User>:String*)</li>
 * </ul>
 * 
 * @author fschulz
 * 
 */
public class SessionAgent extends AbstractRequestAgent {

	static final String LANGUAGE = "language";

	private static final String LAS = "las";

	private static final String MISSION = "mission";

	public static final String METHOD_USERS_IN_LAS = "userInLas";
	public static final String METHOD_USERS_IN_MISSION = "userInMission";
	public static final String METHOD_GET_LAS = "lasOfUser";
	public static final String USER_INFO_REQUEST = "userInfoRequest";

	public static final String NAME = SessionAgent.class.getName();

	private static final Logger LOGGER = Logger.getLogger(SessionAgent.class);

	private int actionTupleListenerId;
	private int requestListenerId;

	public SessionAgent(Map<String, Object> params) {
		super(NAME, params);
		if (params.containsKey(AgentProtocol.TS_HOST)) {
			host = (String) params.get(AgentProtocol.TS_HOST);
		}
		if (params.containsKey(AgentProtocol.TS_PORT)) {
			port = (Integer) params.get(AgentProtocol.TS_PORT);
		}
		try {
			actionTupleListenerId = getActionSpace().eventRegister(
					Command.WRITE, getActivationTuple(), this, true);

			requestListenerId = getCommandSpace().eventRegister(Command.WRITE,
					getCommandTuple(), this, true);
		} catch (TupleSpaceException e) {
			e.printStackTrace();
		}
	}

	private Tuple getCommandTuple() {
		// ("userInfoRequest","query", <QueryId>:String, <Mission>:String,
		// <Method>:String, <Parameter>:String)
		return new Tuple(SessionAgent.USER_INFO_REQUEST, AgentProtocol.QUERY,
				String.class, String.class, String.class,
				Field.createWildCardField());
	}

	private Tuple getActivationTuple() {
		return new Tuple(ActionConstants.ACTION, String.class, Long.class,
				String.class, String.class, String.class, String.class,
				String.class, String.class, Field.createWildCardField());
	}

	@Override
	protected void doRun() throws TupleSpaceException, AgentLifecycleException,
			InterruptedException {
		while (status == Status.Running) {
			sendAliveUpdate();
			try {
				Thread.sleep(AgentProtocol.ALIVE_INTERVAL / 3);
			} catch (InterruptedException e) {
				throw new AgentLifecycleException(e.getMessage(), e);
			}
		}
	}

	@Override
	protected void doStop() throws AgentLifecycleException {
		status = Status.Stopping;
	}

	@Override
	protected Tuple getIdentifyTuple(String queryId) {
		return null;
	}

	@Override
	public boolean isStopped() {
		return status == Status.Stopping;
	}

	@Override
	public void call(Command command, int seq, Tuple activationTuple,
			Tuple beforeTuple) {
		if (seq == actionTupleListenerId) {
			IAction action = ActionTupleTransformer
					.getActionFromTuple(activationTuple);
			String type = action.getType();
			if (type.equals(ActionConstants.ACTION_TOOL_OPENED)) {
				handleToolOpened(action);
			} else if (type.equals(ActionConstants.ACTION_TOOL_CLOSED)) {
				handleToolClosed(action);
			} else if (type.equals(ActionConstants.ACTION_LAS_CHANGED)) {
				handleLasChanged(action);
			} else if (type.equals(ActionConstants.ACTION_LOG_IN)) {
				handleLogin(action);
			} else if (type.equals(ActionConstants.ACTION_LOG_OUT)) {
				handleLogout(action);
			}
		} else if (requestListenerId == seq) {
			if (activationTuple.getField(0).getValue()
					.equals(USER_INFO_REQUEST)) {
				// ("userInfoRequest","query", <QueryId>:String,
				// <Mission>:String,
				// <Method>:String, <Parameter>:String)
				String queryId = (String) activationTuple.getField(2)
						.getValue();
				String mission = (String) activationTuple.getField(3)
						.getValue();
				String method = (String) activationTuple.getField(4).getValue();
				String parameter = "";
				if (activationTuple.numberOfFields() > 5) {
					parameter = (String) activationTuple.getField(5).getValue();
				}
				if (METHOD_USERS_IN_LAS.equals(method)) {
					handleCommandUsersInLas(queryId, mission, parameter);
				} else if (METHOD_GET_LAS.equals(method)) {
					handleCommandLasForUser(queryId, mission, parameter);
				} else if (METHOD_USERS_IN_MISSION.equals(method)) {
					handleCommandUsersInMission(queryId, mission, parameter);
				} else {
					LOGGER.debug("requested not existing method");
				}
			} else {
				LOGGER.debug("got wrong tuple: " + beforeTuple);
			}

		} else {
			logger.debug("Callback passed to Superclass.");
			super.call(command, seq, activationTuple, beforeTuple);
			return;
		}
	}

	private void handleCommandUsersInMission(String queryId, String mission,
			String parameter) {
		try {
			Tuple[] tuples = getSessionSpace().readAll(
					new Tuple(MISSION, String.class, mission));
			Tuple response = new Tuple(USER_INFO_REQUEST,
					AgentProtocol.RESPONSE, queryId);
			for (Tuple t : tuples) {
				String user = (String) t.getField(1).getValue();
				response.add(user);
			}
			sendResponse(response);
		} catch (TupleSpaceException e) {
			LOGGER.warn("", e);
		}
	}

	private void handleCommandLasForUser(String queryId, String mission,
			String user) {
		try {
			Tuple lasTuple = getSessionSpace().read(
					new Tuple(LAS, user, String.class));

			String lasForUser = (String) lasTuple.getField(2).getValue();
			if (lasForUser == null) {
				return;
			}
			Tuple response = new Tuple(USER_INFO_REQUEST,
					AgentProtocol.RESPONSE, queryId, lasForUser);
			sendResponse(response);
		} catch (TupleSpaceException e) {
			LOGGER.warn("", e);
		}
	}

	private void handleCommandUsersInLas(String queryId, String mission,
			String las) {

		try {
			Tuple[] lasTuples = getSessionSpace().readAll(
					new Tuple(LAS, String.class, las));
			Tuple response = new Tuple(USER_INFO_REQUEST,
					AgentProtocol.RESPONSE, queryId);
			for (Tuple t : lasTuples) {
				String user = (String) t.getField(2).getValue();
				response.add(user);
			}
			sendResponse(response);
		} catch (TupleSpaceException e) {
			LOGGER.warn("", e);
		}
	}

	private void sendResponse(Tuple response) throws TupleSpaceException {
		getCommandSpace().write(response);
	}

	private void handleLogout(IAction action) {
		try {
			getSessionSpace().deleteAll(
					new Tuple(String.class, action.getUser(), String.class));
		} catch (TupleSpaceException e) {
			LOGGER.warn("", e);
		}
	}

	private void handleLogin(IAction action) {
		String user = action.getUser();

		try {
			getSessionSpace()
.write(
					new Tuple(LANGUAGE, user, action
							.getAttribute(LANGUAGE)));
			getSessionSpace().write(
					new Tuple(MISSION, user, action
							.getAttribute("missionSpecification")));
		} catch (TupleSpaceException e) {
			LOGGER.warn("", e);
		}
	}

	private void handleLasChanged(IAction action) {
		try {
			getSessionSpace().write(
					new Tuple(LAS, action.getUser(), action
							.getAttribute(ActionConstants.LAS)));
		} catch (TupleSpaceException e) {
			LOGGER.warn("", e);
		}
		sendNotification(action);
	}

	private void sendNotification(IAction action) {
		Tuple notification = new Tuple();
		notification.add(AgentProtocol.NOTIFICATION);
		notification.add(new VMID().toString());
		notification.add(action.getUser());
		notification.add(action.getContext(ContextConstants.eloURI));
		notification.add(NAME);
		notification.add(action.getContext(ContextConstants.mission));
		notification.add(action.getContext(ContextConstants.session));

		notification.add("type=authoring_notification");
		notification.add("oldLas="
				+ action.getAttribute(ActionConstants.OLD_LAS));
		notification.add("newLas=" + action.getAttribute(ActionConstants.LAS));
		try {
			getCommandSpace().write(notification);
		} catch (TupleSpaceException e) {
			e.printStackTrace();
		}
	}

	private void handleToolClosed(IAction action) {
		try {
			getSessionSpace().delete(
					new Tuple("tool", action.getUser(), action
							.getContext(ContextConstants.tool)));
		} catch (TupleSpaceException e) {
			LOGGER.warn("", e);
		}
	}

	private void handleToolOpened(IAction action) {
		try {
			getSessionSpace().write(
					new Tuple("tool", action.getUser(), action
							.getContext(ContextConstants.tool)));
		} catch (TupleSpaceException e) {
			LOGGER.warn("", e);
		}
	}

}

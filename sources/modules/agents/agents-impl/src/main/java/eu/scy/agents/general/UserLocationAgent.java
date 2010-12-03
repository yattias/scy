package eu.scy.agents.general;

import info.collide.sqlspaces.commons.Field;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;

import eu.scy.actionlogging.ActionTupleTransformer;
import eu.scy.actionlogging.api.ContextConstants;
import eu.scy.actionlogging.api.IAction;
import eu.scy.agents.api.AgentLifecycleException;
import eu.scy.agents.impl.AbstractRequestAgent;
import eu.scy.agents.impl.AgentProtocol;

public class UserLocationAgent extends AbstractRequestAgent {

	public static final String METHOD_GET_LAS = "lasOfUser";
	public static final String USER_INFO_REQUEST = "userInfoRequest";
	public static final String NAME = UserLocationAgent.class.getName();

	private static final Logger LOGGER = Logger
			.getLogger(UserLocationAgent.class);

	private static final String LAS = "las";

	private int actionTupleListenerId;
	private int requestListenerId;

	private static ConcurrentHashMap<String, UserLocationInfoMap> missionInfoMap;
	private Lock lock;

	public UserLocationAgent(Map<String, Object> params) {
		super(NAME, params);
		if (params.containsKey(AgentProtocol.TS_HOST)) {
			host = (String) params.get(AgentProtocol.TS_HOST);
		}
		if (params.containsKey(AgentProtocol.TS_PORT)) {
			port = (Integer) params.get(AgentProtocol.TS_PORT);
		}
		lock = new ReentrantLock();
		missionInfoMap = new ConcurrentHashMap<String, UserLocationInfoMap>();
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
		return new Tuple(UserLocationAgent.USER_INFO_REQUEST,
				AgentProtocol.QUERY, String.class, String.class, String.class,
				String.class);
	}

	private Tuple getActivationTuple() {
		return new Tuple(AgentProtocol.ACTION, String.class, Long.class,
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
				throw new AgentLifecycleException(e.getMessage());
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
			if (type.equals(AgentProtocol.ACTION_TOOL_OPENED)) {
				handleToolOpened(action);
			} else if (type.equals(AgentProtocol.ACTION_TOOL_CLOSED)) {
				handleToolClosed(action);
			} else if (type.equals(AgentProtocol.ACTION_LAS_CHANGED)) {
				handleLasChanged(action);
			} else if (type.equals(AgentProtocol.ACTION_LOG_IN)) {
				handleLogin(action);
			} else if (type.equals(AgentProtocol.ACTION_LOG_OUT)) {
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
				String parameter = (String) activationTuple.getField(5)
						.getValue();
				if ("userInLas".equals(method)) {
					List<String> usersInLas = getUsersInLas(mission, parameter);
					if (usersInLas == null) {
						return;
					}
					// ("userInfoRequest", "response" <QueryId>:String,
					// <User>:String*)
					Tuple response = new Tuple(USER_INFO_REQUEST,
							AgentProtocol.RESPONSE, queryId);
					for (String user : usersInLas) {
						response.add(user);
					}
					sendResponse(response);
				} else if (METHOD_GET_LAS.equals(method)) {
					String lasForUser = getLasForUser(mission, parameter);
					if (lasForUser == null) {
						return;
					}
					// ("userInfoRequest", "response" <QueryId>:String,
					// <Las>:String)
					Tuple response = new Tuple(USER_INFO_REQUEST,
							AgentProtocol.RESPONSE, queryId, lasForUser);
					sendResponse(response);
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

	private void sendResponse(Tuple response) {
		try {
			getCommandSpace().write(response);
		} catch (TupleSpaceException e) {
			LOGGER.warn("", e);
		}
	}

	private String getLasForUser(String mission, String user) {
		UserLocationInfoMap userLocationInfoMap = getUserLocationInfoMap(mission);
		if (userLocationInfoMap == null) {
			LOGGER
					.debug("did not find any information for mission: "
							+ mission);
			return null;
		}
		UserLocationInfo userInfo = userLocationInfoMap.getUserInfo(user);
		if (userInfo == null) {
			LOGGER.debug("did not find any information for user " + user
					+ " in mission: " + mission);
			return null;

		}
		return userInfo.getLas();
	}

	private List<String> getUsersInLas(String mission, String las) {
		UserLocationInfoMap userLocationInfoMap = getUserLocationInfoMap(mission);
		if (userLocationInfoMap == null) {
			LOGGER
					.debug("did not find any information for mission: "
							+ mission);
			return null;
		}
		return userLocationInfoMap.getUserInLas(las);
	}

	private void handleLogout(IAction action) {
		String mission = action.getContext(ContextConstants.mission);
		UserLocationInfoMap userLocationInfoMap = getUserLocationInfoMap(mission);
		userLocationInfoMap.remove(action.getUser());
	}

	private void handleLogin(IAction action) {
		String mission = action.getContext(ContextConstants.mission);
		UserLocationInfoMap userLocationInfoMap = getUserLocationInfoMap(mission);
		userLocationInfoMap.add(action.getUser(), new UserLocationInfo());
	}

	private void handleLasChanged(IAction action) {
		String mission = action.getContext(ContextConstants.mission);
		UserLocationInfoMap userLocationInfoMap = getUserLocationInfoMap(mission);
		UserLocationInfo userInfo = getUserInfo(action, userLocationInfoMap);

		userInfo.setLas(action.getAttribute(LAS));
	}

	private void handleToolClosed(IAction action) {
		String mission = action.getContext(ContextConstants.mission);
		UserLocationInfoMap userLocationInfoMap = getUserLocationInfoMap(mission);
		UserLocationInfo userInfo = getUserInfo(action, userLocationInfoMap);

		userInfo.removeTool(action.getContext(ContextConstants.tool));
		userInfo.removeELO(action.getContext(ContextConstants.eloURI));
	}

	private void handleToolOpened(IAction action) {
		String mission = action.getContext(ContextConstants.mission);
		UserLocationInfoMap userLocationInfoMap = getUserLocationInfoMap(mission);
		UserLocationInfo userInfo = getUserInfo(action, userLocationInfoMap);

		userInfo.addTool(action.getContext(ContextConstants.tool));
		userInfo.addELO(action.getContext(ContextConstants.eloURI));
	}

	private UserLocationInfo getUserInfo(IAction action,
			UserLocationInfoMap userLocationInfoMap) {

		UserLocationInfo userInfo = null;
		lock.lock();
		try {
			userInfo = userLocationInfoMap.getUserInfo(action.getUser());
			if (userInfo == null) {
				userInfo = new UserLocationInfo();
				userLocationInfoMap.add(action.getUser(), userInfo);
			}
		} finally {
			lock.unlock();
		}

		return userInfo;
	}

	private UserLocationInfoMap getUserLocationInfoMap(String mission) {
		UserLocationInfoMap userLocationInfoMap = null;
		lock.lock();
		try {
			userLocationInfoMap = missionInfoMap.get(mission);
			if (userLocationInfoMap == null) {
				userLocationInfoMap = new UserLocationInfoMap();
				missionInfoMap.put(mission, userLocationInfoMap);
			}
		} finally {
			lock.unlock();
		}
		return userLocationInfoMap;
	}
}

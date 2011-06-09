package eu.scy.agents.groupformation;

import eu.scy.common.mission.StrategyType;
import info.collide.sqlspaces.commons.Field;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;

import java.net.URI;
import java.net.URISyntaxException;
import java.rmi.dgc.VMID;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import roolo.api.IRepository;
import roolo.elo.api.IELO;
import roolo.elo.api.IMetadataTypeManager;
import eu.scy.actionlogging.Action;
import eu.scy.actionlogging.ActionTupleTransformer;
import eu.scy.actionlogging.Context;
import eu.scy.actionlogging.api.ContextConstants;
import eu.scy.actionlogging.api.IAction;
import eu.scy.agents.Mission;
import eu.scy.agents.api.AgentLifecycleException;
import eu.scy.agents.api.IRepositoryAgent;
import eu.scy.agents.api.parameter.AgentParameter;
import eu.scy.agents.groupformation.cache.MissionGroupCache;
import eu.scy.agents.impl.AbstractRequestAgent;
import eu.scy.agents.impl.ActionConstants;
import eu.scy.agents.impl.AgentProtocol;
import eu.scy.agents.session.Session;

public class GroupFormationAgent extends AbstractRequestAgent implements
		IRepositoryAgent {

	private static final Logger LOGGER = Logger
			.getLogger(GroupFormationAgent.class);

	private static final String LAS = "newLasId";
	private static final String OLD_LAS = "oldLasId";
	private static final String STRATEGY = "strategy";
	private static final String FORM_GROUP = "form_group";
	static final String NAME = GroupFormationAgent.class.getName();
	private static final String SCOPE = "scope";

	private static final String MIN_GROUP_SIZE_PARAMETER = "MinGroupSize";
	private static final String MAX_GROUP_SIZE_PARAMETER = "MaxGroupSize";

	private int listenerId;
	private IRepository repository;
	private GroupFormationStrategyFactory factory;

	private final Object lock;

	private MissionGroupCache missionGroupsCache;

	public GroupFormationAgent(Map<String, Object> params) {
		super(NAME, params);
		lock = new Object();
		if (params.containsKey(AgentProtocol.TS_HOST)) {
			host = (String) params.get(AgentProtocol.TS_HOST);
		}
		if (params.containsKey(AgentProtocol.TS_PORT)) {
			port = (Integer) params.get(AgentProtocol.TS_PORT);
		}
		params.put(MIN_GROUP_SIZE_PARAMETER, 2);
		params.put(MAX_GROUP_SIZE_PARAMETER, 10);
		configuration.addAllParameter(params);
		factory = new GroupFormationStrategyFactory();
		try {
			listenerId = getActionSpace().eventRegister(Command.WRITE,
					getActivationTuple(), this, true);
		} catch (TupleSpaceException e) {
			e.printStackTrace();
		}
		missionGroupsCache = new MissionGroupCache();
	}

	/* activated by action log */
	private Tuple getActivationTuple() {
		/*
		 * return new Tuple(AgentProtocol.ACTION, String.class, Long.class,
		 * FORM_GROUP, Field.createWildCardField());
		 */
		return new Tuple(ActionConstants.ACTION, String.class, Long.class,
				String.class, Field.createWildCardField());
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
	protected Tuple getListParameterTuple(String queryId) {
		return AgentProtocol.getListParametersTupleResponse(NAME, queryId,
				Arrays.asList(MIN_GROUP_SIZE_PARAMETER,
						MAX_GROUP_SIZE_PARAMETER));
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
	public void call(Command command, int seq, Tuple afterTuple,
			Tuple beforeTuple) {
		if (listenerId != seq) {
			logger.debug("Callback passed to Superclass.");
			super.call(command, seq, afterTuple, beforeTuple);
			return;
		} else {
			IAction action = ActionTupleTransformer
					.getActionFromTuple(afterTuple);
			String type = action.getType();
			String oldLas = action.getAttribute(OLD_LAS);
			String las = action.getAttribute(LAS);
			if (!correctLasEntry(oldLas, las)) {
				return;
			}
			if (type.equals(ActionConstants.ACTION_LOG_OUT)) {
				synchronized (lock) {
					missionGroupsCache.removeUser(action.getUser());
					return;
				}
			}
			if (!type.equals(ActionConstants.ACTION_LAS_CHANGED)) {
				return;
			}
			if ("conceptualisatsionConceptMap".equals(oldLas)) {
				synchronized (lock) {
					removeUserFromCache(
							action,
							(Integer) configuration.getParameter(new AgentParameter(
									getSession().getMission(action.getUser())
											.getName(),
									MIN_GROUP_SIZE_PARAMETER)), oldLas);
				}
			}
			if ("conceptualisatsionConceptMap".equals(las)) {
				try {
					runGroupFormation(action);
				} catch (TupleSpaceException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private boolean correctLasEntry(String oldLas, String newLas) {
		if (oldLas == null) {
			return false;
		}
		if (oldLas.equals(newLas)) {
			return false;
		}
		return true;
	}

	private void removeUserFromCache(IAction action, int minGroupSize,
			String las) {
		Mission mission = getSession().getMission(action.getUser());
		missionGroupsCache.removeFromCache(mission, las, action.getUser(),
				minGroupSize);
	}

	private void runGroupFormation(IAction action) throws TupleSpaceException {
		Mission mission = getSession().getMission(action.getUser());
		String las = action.getAttribute(LAS);

		int minGroupSize = (Integer) configuration
				.getParameter(new AgentParameter(mission.getName(),
						MIN_GROUP_SIZE_PARAMETER));
		int maxGroupSize = (Integer) configuration
				.getParameter(new AgentParameter(mission.getName(),
						MAX_GROUP_SIZE_PARAMETER));

		String eloUri = action.getContext(ContextConstants.eloURI);
		IELO elo = getElo(eloUri);
		StrategyType strategy = StrategyType.DUMMY;// action.getAttribute(STRATEGY);
		GroupFormationScope scope = GroupFormationScope.LAS;// valueOf(action.getAttribute(SCOPE));

		Set<String> availableUsers = getAvailableUsers(scope, mission, las,
				action.getUser());
		if (availableUsers.size() < minGroupSize) {
			if (!missionGroupsCache.contains(mission, las, action.getUser())) {
				sendWaitNotification(action);
			}
			return;
		}

		GroupFormationStrategy groupFormationStrategy = factory
				.getStrategy(strategy);
		groupFormationStrategy.setGroupFormationCache(missionGroupsCache.get(
				mission, las));
		groupFormationStrategy.setScope(scope);
		groupFormationStrategy.setLas(las);
		groupFormationStrategy.setMission(mission.getName());
		groupFormationStrategy.setMinimumGroupSize(minGroupSize);
		groupFormationStrategy.setMaximumGroupSize(maxGroupSize);
		groupFormationStrategy.setAvailableUsers(availableUsers);

		Collection<Set<String>> formedGroups = groupFormationStrategy
				.formGroup(elo);

		// if (groupsAreOk(formedGroup, minGroupSize, maxGroupSize)) {
		missionGroupsCache.addGroups(mission, las, formedGroups);
		// }
		try {
			synchronized (lock) {
				sendGroupNotification(action, formedGroups);
			}
		} catch (TupleSpaceException e) {
			LOGGER.error("Could not write into Tuplespace", e);
		}

	}

	private Set<String> getAvailableUsers(GroupFormationScope scope,
			Mission mission, String las, String thisUser)
			throws TupleSpaceException {
		Set<String> availableUsers = new HashSet<String>();
		availableUsers.add(thisUser);
		switch (scope) {
		case LAS:
			Tuple[] allUsersInLas = getSessionSpace()
					.readAll(
							new Tuple(Session.LAS, String.class, mission
									.getName(), las));
			for (Tuple t : allUsersInLas) {
				availableUsers.add((String) t.getField(1).getValue());
			}
			break;
		case MISSION:
			Tuple[] allUsersInMission = getSessionSpace().readAll(
					new Tuple(Session.MISSION, String.class, mission,
							String.class));
			for (Tuple t : allUsersInMission) {
				availableUsers.add((String) t.getField(1).getValue());
			}
			break;
		}
		Collection<Set<String>> groups = missionGroupsCache.getGroups(mission,
				las);
		for (Set<String> group : groups) {
			availableUsers.removeAll(group);
		}
		return availableUsers;
	}

	// private boolean groupsAreOk(Collection<Set<String>> formedGroup,
	// int minGroupSize, int maxGroupSize) {
	// for (Set<String> group : formedGroup) {
	// if (group.size() < minGroupSize || group.size() > maxGroupSize) {
	// return false;
	// }
	// }
	// return true;
	// }

	private void sendGroupNotification(IAction action,
			Collection<Set<String>> formedGroups) throws TupleSpaceException {

		for (Set<String> group : formedGroups) {

			for (String user : group) {
				String notificationId = createId();
				Tuple removeAllBuddiesTuple = createRemoveAllBuddiesNotification(
						action, notificationId, user);
				getCommandSpace().write(removeAllBuddiesTuple);

				waitForNotificationProcessedAction(notificationId,
						"remove all buddies for " + user
								+ " notification was not processed");
			}

			for (String user : group) {
				StringBuilder message = new StringBuilder();
				message.append("Please consider collaboration with these students:\n");

				String userListString = createUserListString(user, group);
				message.append(userListString);

				String messageNotificationId = createId();
				Tuple messageNotificationTuple = createMessageNotificationTuple(
						action, messageNotificationId, message.toString(), user);
				logGroupFormation(action, userListString, user);

				for (String userToBuddify : group) {
					if (!user.equals(userToBuddify)) {
						String buddifyNotificationId = messageNotificationId;
						Tuple buddifyNotification = createBuddifyNotificationTuple(
								action, buddifyNotificationId, user,
								userToBuddify);
						getCommandSpace().write(buddifyNotification);

						waitForNotificationProcessedAction(
								buddifyNotificationId, "Buddify " + user
										+ " -> " + userToBuddify
										+ " notification was not processed");
					}
				}
				getCommandSpace().write(messageNotificationTuple);
				waitForNotificationProcessedAction(messageNotificationId,
						"Message about group notification was not processed");
			}
		}
	}

	private void waitForNotificationProcessedAction(String notificationId,
			String message) throws TupleSpaceException {
		Tuple notificationProcessedTuple = getActionSpace().waitToRead(
				new Tuple(ActionConstants.ACTION, notificationId, Long.class,
						String.class, Field.createWildCardField()),
				AgentProtocol.MILLI_SECOND * 50);
		if (notificationProcessedTuple == null) {
			logger.warn(message);
		}
	}

	private String createId() {
		return new VMID().toString();
	}

	private Tuple createRemoveAllBuddiesNotification(IAction action,
			String notificationId, String user) {
		Tuple notificationTuple = new Tuple();
		notificationTuple.add(AgentProtocol.NOTIFICATION);
		notificationTuple.add(notificationId);
		notificationTuple.add(user);
		notificationTuple.add("no specific elo");
		notificationTuple.add(NAME);
		notificationTuple.add(action.getContext(ContextConstants.mission));
		notificationTuple.add(action.getContext(ContextConstants.session));
		notificationTuple.add("type=remove_all_buddies");
		notificationTuple.add("user=" + user);
		return notificationTuple;
	}

	private Tuple createBuddifyNotificationTuple(IAction action,
			String notificationId, String user, String userToBuddify) {
		Tuple notificationTuple = new Tuple();
		notificationTuple.add(AgentProtocol.NOTIFICATION);
		notificationTuple.add(notificationId);
		notificationTuple.add(user);
		notificationTuple.add("no specific elo");
		notificationTuple.add(NAME);
		notificationTuple.add(action.getContext(ContextConstants.mission));
		notificationTuple.add(action.getContext(ContextConstants.session));
		notificationTuple.add("type=add_buddy");
		notificationTuple.add("user=" + userToBuddify);
		return notificationTuple;
	}

	private void logGroupFormation(IAction action, String userListString,
			String user) {
		Action groupFormationAction = new Action();
		groupFormationAction.setContext(new Context(NAME, action
				.getContext(ContextConstants.mission), action
				.getContext(ContextConstants.session), action
				.getContext(ContextConstants.eloURI)));
		groupFormationAction.setId(createId());
		groupFormationAction.setTimeInMillis(System.currentTimeMillis());
		groupFormationAction.setUser(action.getUser());
		groupFormationAction.setType(FORM_GROUP);
		groupFormationAction.addAttribute("user", user);
		groupFormationAction.addAttribute("group", userListString);
		Tuple actionAsTuple = ActionTupleTransformer
				.getActionAsTuple(groupFormationAction);
		try {
			getActionSpace().write(actionAsTuple);
		} catch (TupleSpaceException e) {
			LOGGER.error("Could not write action into Tuplespace", e);
		}
	}

	private Tuple createMessageNotificationTuple(IAction action,
			String notificationId, String message, String user) {
		Tuple notificationTuple = new Tuple();
		notificationTuple.add(AgentProtocol.NOTIFICATION);
		notificationTuple.add(notificationId);
		notificationTuple.add(user);
		notificationTuple.add("no specific elo");
		notificationTuple.add(NAME);
		notificationTuple.add(action.getContext(ContextConstants.mission));
		notificationTuple.add(action.getContext(ContextConstants.session));
		notificationTuple.add("text=" + message.toString());
		notificationTuple.add("title=Groupformation");
		notificationTuple.add("type=message_dialog_show");
		notificationTuple.add("modal=false");
		notificationTuple.add("dialogType=OK_DIALOG");
		return notificationTuple;
	}

	String createUserListString(String userToNotify, Set<String> group) {
		StringBuilder message = new StringBuilder();
		int i = 0;
		for (String user : group) {
			if (user.equals(userToNotify)) {
				i++;
				continue;
			}
			message.append(sanitizeName(user));
			if (i != group.size() - 1) {
				message.append("; ");
			}
			i++;
		}
		return message.toString();
	}

	private String sanitizeName(String user) {
		int indexOf = user.indexOf("@");
		if (indexOf != -1) {
			return user.substring(0, indexOf);
		}
		return user;
	}

	private IELO getElo(String eloUri) {
		try {
			return repository.retrieveELO(new URI(eloUri));
		} catch (URISyntaxException e) {
			e.printStackTrace();
			return null;
		}
	}

	private void sendWaitNotification(IAction action) {
		Tuple notificationTuple = createMessageNotificationTuple(action,
				createId(), "please wait for other users to be available",
				action.getUser());
		try {
			getCommandSpace().write(notificationTuple);
		} catch (TupleSpaceException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void setMetadataTypeManager(IMetadataTypeManager manager) {
		// metadataTypeManager = manager;
	}

	@Override
	public void setRepository(IRepository rep) {
		repository = rep;
	}

	public GroupFormationStrategyFactory getFactory() {
		return factory;
	}

	public void setFactory(GroupFormationStrategyFactory factory) {
		this.factory = factory;
	}
}

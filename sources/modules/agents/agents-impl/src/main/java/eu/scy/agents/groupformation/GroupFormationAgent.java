package eu.scy.agents.groupformation;

import info.collide.sqlspaces.commons.Field;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;

import java.net.URI;
import java.net.URISyntaxException;
import java.rmi.dgc.VMID;
import java.util.Arrays;
import java.util.Collection;
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
import eu.scy.agents.api.AgentLifecycleException;
import eu.scy.agents.api.IRepositoryAgent;
import eu.scy.agents.api.parameter.AgentParameter;
import eu.scy.agents.impl.AbstractRequestAgent;
import eu.scy.agents.impl.AgentProtocol;

public class GroupFormationAgent extends AbstractRequestAgent implements
		IRepositoryAgent {

	private static final Logger LOGGER = Logger
			.getLogger(GroupFormationAgent.class);

	private static final String SCY_LAB = "scy-lab";
	private static final String STRATEGY = "strategy";
	private static final String FORM_GROUP = "form_group";
	private static final String NAME = GroupFormationAgent.class.getName();
	private static final String SCOPE = "scope";

	private static final String MIN_GROUP_SIZE_PARAMETER = "MinGroupSize";
	private static final String MAX_GROUP_SIZE_PARAMETER = "MaxGroupSize";

	private int listenerId;
	private IRepository repository;
	private GroupFormationStrategyFactory factory;

	private Map<String, GroupFormationCache> missionGroupsCache;

	public GroupFormationAgent(Map<String, Object> params) {
		super(NAME, params);
		if (params.containsKey(AgentProtocol.TS_HOST)) {
			host = (String) params.get(AgentProtocol.TS_HOST);
		}
		if (params.containsKey(AgentProtocol.TS_PORT)) {
			port = (Integer) params.get(AgentProtocol.TS_PORT);
		}
		factory = new GroupFormationStrategyFactory();
		try {
			listenerId = getActionSpace().eventRegister(Command.WRITE,
					getActivationTuple(), this, true);
		} catch (TupleSpaceException e) {
			e.printStackTrace();
		}
	}

	/* activated by action log */
	private Tuple getActivationTuple() {
		return new Tuple(AgentProtocol.ACTION, String.class, Long.class,
				FORM_GROUP, String.class, String.class, String.class,
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
			String mission = action.getContext(ContextConstants.mission);

			int minGroupSize = configuration.getParameter(new AgentParameter(
					mission, MIN_GROUP_SIZE_PARAMETER));
			int maxGroupSize = configuration.getParameter(new AgentParameter(
					mission, MAX_GROUP_SIZE_PARAMETER));

			String eloUri = action.getContext(ContextConstants.eloURI);
			IELO elo = getElo(eloUri);
			if (elo == null) {
				// TODO handle?
				return;
			}

			GroupFormationCache groupFormationCache = missionGroupsCache
					.get(mission);

			String strategy = action.getAttribute(STRATEGY);
			String scope = action.getAttribute(SCOPE);

			GroupFormationStrategy groupFormationStrategy = factory
					.getStrategy(strategy);
			groupFormationStrategy.setGroupFormationCache(groupFormationCache);
			groupFormationStrategy.setScope(scope);
			groupFormationStrategy.setMission(mission);
			groupFormationStrategy.setMinimumGroupSize(minGroupSize);
			groupFormationStrategy.setMaximumGroupSize(maxGroupSize);
			Collection<Set<String>> formedGroup = groupFormationStrategy
					.formGroup(elo);

			groupFormationCache.addGroups(formedGroup);
			// if (groupsAreOk(formedGroup, minGroupSize, maxGroupSize)) {
			missionGroupsCache.put(mission, groupFormationCache);
			// }

			sendCollaborationNotification(action, groupFormationCache);
		}
	}

	private boolean groupsAreOk(Collection<Set<String>> formedGroup,
			int minGroupSize, int maxGroupSize) {
		for (Set<String> group : formedGroup) {
			if (group.size() < minGroupSize || group.size() > maxGroupSize) {
				return false;
			}
		}
		return true;
	}

	private void sendCollaborationNotification(IAction action,
			GroupFormationCache cache) {

		for (Set<String> group : cache.getGroups()) {
			StringBuilder message = new StringBuilder();
			message
					.append("textmessage=Please consider collaboration with these students: ");

			String userListString = createUserListString(group);
			message.append(userListString);

			for (String user : group) {
				Tuple notificationTuple = createNotificationTuple(action,
						message, user);

				logGroupFormation(action, userListString, user);

				try {
					getCommandSpace().write(notificationTuple);
				} catch (TupleSpaceException e) {
					LOGGER.error("Could not write into Tuplespace", e);
				}
			}
		}
	}

	private void logGroupFormation(IAction action, String userListString,
			String user) {
		Action groupFormationAction = new Action();
		groupFormationAction.setContext(new Context(NAME, action
				.getContext(ContextConstants.mission), action
				.getContext(ContextConstants.session), action
				.getContext(ContextConstants.eloURI)));
		groupFormationAction.setId(new VMID().toString());
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

	private Tuple createNotificationTuple(IAction action,
			StringBuilder message, String user) {
		Tuple notificationTuple = new Tuple();
		notificationTuple.add(AgentProtocol.NOTIFICATION);
		notificationTuple.add(new VMID().toString());
		notificationTuple.add(user);
		notificationTuple.add(SCY_LAB);
		notificationTuple.add(NAME);
		notificationTuple.add(action.getContext(ContextConstants.mission));
		notificationTuple.add(action.getContext(ContextConstants.session));
		notificationTuple.add(message.toString());
		return notificationTuple;
	}

	private String createUserListString(Set<String> group) {
		StringBuilder message = new StringBuilder();
		int i = 0;
		for (String user : group) {
			message.append(user);
			if (i == group.size() - 2) {
				message.append(", ");
			}
			i++;
		}
		return message.toString();
	}

	private IELO getElo(String eloUri) {
		try {
			return repository.retrieveELO(new URI(eloUri));
		} catch (URISyntaxException e) {
			e.printStackTrace();
			return null;
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

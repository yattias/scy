package eu.scy.agents.groupformation;

import info.collide.sqlspaces.commons.Field;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

import roolo.api.IRepository;
import roolo.elo.api.IELO;
import roolo.elo.api.IMetadataTypeManager;
import eu.scy.actionlogging.ActionTupleTransformer;
import eu.scy.actionlogging.api.ContextConstants;
import eu.scy.actionlogging.api.IAction;
import eu.scy.agents.api.AgentLifecycleException;
import eu.scy.agents.api.IRepositoryAgent;
import eu.scy.agents.impl.AbstractRequestAgent;
import eu.scy.agents.impl.AgentProtocol;

public class GroupFormationAgent extends AbstractRequestAgent implements
		IRepositoryAgent {

	private static final String STRATEGY = "strategy";
	private static final String FORM_GROUP = "form_group";
	private static final String NAME = GroupFormationAgent.class.getName();

	private int listenerId;
	// private PersistentStorage storage = null;
	private IMetadataTypeManager metadataTypeManager;
	private IRepository repository;
	private GroupFormationStrategyFactory factory;

	public GroupFormationAgent(Map<String, Object> params) {
		super(NAME, params);
		if (params.containsKey(AgentProtocol.TS_HOST)) {
			host = (String) params.get(AgentProtocol.TS_HOST);
		}
		if (params.containsKey(AgentProtocol.TS_PORT)) {
			port = (Integer) params.get(AgentProtocol.TS_PORT);
		}
		// storage = new PersistentStorage(host, port);
		factory = new GroupFormationStrategyFactory();
		try {
			listenerId = getActionSpace().eventRegister(Command.WRITE,
					getActivationTuple(), this, true);
		} catch (TupleSpaceException e) {
			e.printStackTrace();
		}
	}

	private Tuple getActivationTuple() {
		// ("form_group",<QueryID>:String, <User>:String, <ELOUri>:String.class,
		// <Strategy>:String)
		// return new Tuple("form_group", AgentProtocol.QUERY, String.class,
		// String.class, String.class);
		//		
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

			String eloUri = action.getContext(ContextConstants.eloURI);
			IELO elo = getElo(eloUri);
			if (elo == null) {
				// TODO handle?
				return;
			}

			String user = getUser(elo);

			String strategy = action.getAttribute(STRATEGY);

			GroupFormationStrategy groupFormationStrategy = factory
					.getStrategy(strategy);

			List<String> formedGroup = groupFormationStrategy.formGroup(elo,
					user);

			activateCollaboration(action, formedGroup);
		}
	}

	private void activateCollaboration(IAction action, List<String> group) {
		StringBuilder propsedUser = new StringBuilder();
		for (String user : group) {
			propsedUser.append(user);
			propsedUser.append(",");
		}
		String eloUri = action.getContext(ContextConstants.eloURI);
		String mission = action.getContext(ContextConstants.mission);
		String session = action.getContext(ContextConstants.session);

		try {
			// TODO what is something???
			getCommandSpace().write(
					new Tuple("something???", "collaboration",
							"collaboration_request", propsedUser.toString(),
							eloUri, mission, session));
		} catch (TupleSpaceException e) {
			e.printStackTrace();
		}
	}

	private String getUser(IELO elo) {
		// TODO which user to get
		return null;
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
		metadataTypeManager = manager;
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

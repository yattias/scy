package eu.scy.agents.impl.manager;

import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Callback;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;

import java.util.TreeMap;

import eu.scy.agents.api.IAgentFactory;
import eu.scy.agents.impl.AgentProtocol;
import eu.scy.agents.impl.Parameter;

public class AgentManager implements Callback {

	private static AgentManager me = null;
	private TupleSpace tupleSpace;

	public static AgentManager getInstance() {
		if (me == null) {
			try {
				me = new AgentManager();
			} catch (TupleSpaceException e) {
				throw new RuntimeException(e);
			}
		}
		return me;
	}

	private TreeMap<String, IAgentFactory> agentFactories;
	private TreeMap<String, Long> agentAlive;

	private AgentManager() throws TupleSpaceException {
		agentFactories = new TreeMap<String, IAgentFactory>();
		agentAlive = new TreeMap<String, Long>();
		initTupleSpace();
		tupleSpace.eventRegister(Command.WRITE, AgentProtocol
				.getAliveTupleTemplate(), this, true);
	}

	private void initTupleSpace() {
		try {
			tupleSpace = new TupleSpace();
		} catch (TupleSpaceException e) {
			throw new RuntimeException(e);
		}
	}

	public void setAgentFactory(IAgentFactory factory) {
		agentFactories.put(factory.getAgentName(), factory);
	}

	public void removeAgentFactory(IAgentFactory factory) {
		agentFactories.remove(factory.getAgentName());
	}

	public void stopAgent(String name) {
		try {
			tupleSpace.write(AgentProtocol.getStopTuple(name));
		} catch (TupleSpaceException e) {
			throw new RuntimeException(e);
		}
	}

	public void startAgent(String name) {
		agentFactories.get(name).create(new Parameter());
		try {
			tupleSpace.write(AgentProtocol.getStartTuple(name));
		} catch (TupleSpaceException e) {
			throw new RuntimeException(e);
		}
	}

	public void suspendAgent(String name) {
		try {
			tupleSpace.write(AgentProtocol.getSuspendTuple(name));
		} catch (TupleSpaceException e) {
			throw new RuntimeException(e);
		}
	}

	public void resumeAgent(String name) {
		try {
			tupleSpace.write(AgentProtocol.getResumeTuple(name));
		} catch (TupleSpaceException e) {
			throw new RuntimeException(e);
		}
	}

	void dispose() {
		me = null;
	}

	@Override
	public void call(Command command, int seq, Tuple afterTuple,
			Tuple beforeTuple) {
		if (AgentProtocol.getAliveTupleTemplate().matches(afterTuple)) {
			String agentName = (String) afterTuple.getField(2).getValue();
			// Status status = Status.valueOf((String) afterTuple.getField(3)
			// .getValue());
			long aliveTS = (Long) afterTuple.getField(4).getValue();
			// long lastAliveTS = aliveTS -
			agentAlive.put(agentName, aliveTS);
		}
	}
}

package eu.scy.agents.impl.manager;

import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Callback;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;

import java.util.HashMap;
import java.util.TreeMap;

import eu.scy.agents.api.AgentLifecycleException;
import eu.scy.agents.api.IAgentFactory;
import eu.scy.agents.api.IParameter;
import eu.scy.agents.api.IThreadedAgent;
import eu.scy.agents.impl.AgentProtocol;
import eu.scy.agents.impl.Parameter;

public class AgentManager implements Callback {

	// private static AgentManager me = null;
	private TupleSpace tupleSpace;
	private IParameter parameters;

	private HashMap<String, IThreadedAgent> agents;

	// public static AgentManager getInstance() {
	// if (me == null) {
	// try {
	// me = new AgentManager();
	// } catch (TupleSpaceException e) {
	// throw new RuntimeException(e);
	// }
	// }
	// return me;
	// }

	private TreeMap<String, IAgentFactory> agentFactories;
	private TreeMap<String, Long> agentAlive;

	public AgentManager() {
		agentFactories = new TreeMap<String, IAgentFactory>();
		agentAlive = new TreeMap<String, Long>();
		agents = new HashMap<String, IThreadedAgent>();
		try {
			tupleSpace = new TupleSpace();
			tupleSpace.eventRegister(Command.WRITE,
					AgentProtocol.ALIVE_TUPLE_TEMPLATE, this, true);
		} catch (TupleSpaceException e) {
			throw new RuntimeException(
					"TupleSpace could not be accessed. Agent manager won't work",
					e);
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
		IThreadedAgent agent = agentFactories.get(name).create(new Parameter());
		agents.put(name, agent);
		try {
			agent.start();
		} catch (AgentLifecycleException e) {
			e.printStackTrace();
		}
	}

	// public void suspendAgent(String name) {
	// try {
	// tupleSpace.write(AgentProtocol.getSuspendTuple(name));
	// } catch (TupleSpaceException e) {
	// throw new RuntimeException(e);
	// }
	// }
	//
	// public void resumeAgent(String name) {
	// try {
	// tupleSpace.write(AgentProtocol.getResumeTuple(name));
	// } catch (TupleSpaceException e) {
	// throw new RuntimeException(e);
	// }
	// }

	public void killAgent(String name) {
		try {
			tupleSpace.write(AgentProtocol.getKillTuple(name));
		} catch (TupleSpaceException e) {
			throw new RuntimeException(e);
		}
	}

	void dispose() {
		try {
			tupleSpace.disconnect();
		} catch (TupleSpaceException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void call(Command command, int seq, Tuple afterTuple,
			Tuple beforeTuple) {
		if (AgentProtocol.ALIVE_TUPLE_TEMPLATE.matches(afterTuple)) {
			if ((Command.WRITE == command) || (Command.UPDATE == command)) {
				String agentName = (String) afterTuple.getField(2).getValue();
				// Status status = Status.valueOf((String)
				// afterTuple.getField(3)
				// .getValue());
				long aliveTS = afterTuple.getLastModificationTimestamp();
				agentAlive.put(agentName, aliveTS);
			} else if (Command.DELETE == command) {
				String agentId = (String) beforeTuple.getField(2).getValue();
				startAgent(agentId);
			}
		}
	}

	public void setParameter(String name, Object parameter) {
		parameters.set(name, parameter);
	}
}

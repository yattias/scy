package eu.scy.agents.impl.manager;

import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Callback;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;
import info.collide.sqlspaces.commons.User;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.rmi.dgc.VMID;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import roolo.api.IRepository;
import roolo.elo.api.IMetadataTypeManager;
import eu.scy.agents.api.AgentLifecycleException;
import eu.scy.agents.api.IRepositoryAgent;
import eu.scy.agents.api.IThreadedAgent;
import eu.scy.agents.impl.AgentProtocol;

/**
 * This class provides a facility to manage the agents inside SCY.<br>
 * It is used to start/stop/kill single or multiple agents of the same or different kind.<br>
 * <br>
 * There are some conventions regarding the agents which can be managed using this manager: - All manageable agents must
 * be an implementation of {@link IThreadedAgent}.<br>
 * <br>
 * - All manageable agents must implement a constructor like {@code MyAgent(Map<String, Object> map)} This maps is
 * usually used to set some properties. If you do not need properties, you can ignore the map exept for the entry
 * <i>"id"</i> which contains the uniqueID ({@link VMID} as String) of the single instances.<br>
 * - The whole inter-agent-communication is handled using the SQLSpaces
 * (http://weinbrenner.collide.info/sqlspaces-site/) and follows following convention:<br/> {@code
 * ("query"|"response"|"agentCommand"), queryID [String], agentID [String], agentName [String], serviceName [String],
 * ....(Payload)}
 * 
 * @author Florian Schulz
 * @author Jan Engler
 * @author Stefan Weinbrenner
 */
public class AgentManager implements Callback {

	private TupleSpace tupleSpace;

	private Map<String, IThreadedAgent> agentIdMap;

	private Map<String, IThreadedAgent> oldAgents;

	private Map<String, Long> agentAlive;

	private Map<String, Class<?>> agentClasses;

	private static final Logger logger = Logger.getLogger(AgentManager.class.getName());

	private static final Level LOGLEVEL = Level.OFF;

	private Map<String, Map<String, Object>> startParameters;

	// TODO: inject via spring magic (no magic without setter)
	private IRepository repository;

	// TODO: inject via spring magic (no magic without setter)
	private IMetadataTypeManager manager;

	/**
	 * This constructor is used to start an agent manager. Please make sure that only one instance is running on one
	 * SQLSpace. We intentionally used no Singleton Pattern or Lazy creation to provide the possibility to have several
	 * managers running parallel, but you have to know what you are doing.
	 * 
	 * @param host The host of the SQLSpaces server (e.g. localhost)
	 * @param port The port of the SQLSpaces server (e.g. 2525)
	 */
	public AgentManager(String host, int port) {
		initLogger();
		agentAlive = new HashMap<String, Long>();
		agentIdMap = new HashMap<String, IThreadedAgent>();
		oldAgents = new HashMap<String, IThreadedAgent>();
		startParameters = new HashMap<String, Map<String, Object>>();
		agentClasses = new HashMap<String, Class<?>>();
		try {

			// TODO COmmand.all
			tupleSpace = new TupleSpace(new User("AgentManager"), host, port, AgentProtocol.COMMAND_SPACE_NAME);
			tupleSpace.eventRegister(Command.WRITE, AgentProtocol.ALIVE_TUPLE_TEMPLATE, this, true);
			tupleSpace.eventRegister(Command.UPDATE, AgentProtocol.ALIVE_TUPLE_TEMPLATE, this, true);
			tupleSpace.eventRegister(Command.DELETE, AgentProtocol.ALIVE_TUPLE_TEMPLATE, this, true);
		} catch (TupleSpaceException e) {
			throw new RuntimeException("TupleSpace could not be accessed. Agent manager won't work", e);
		}
		Runnable r = new Runnable() {

			@Override
			public void run() {
				cleanUp();
			}
		};

		Runtime.getRuntime().addShutdownHook(new Thread(r));
	}

	private void initLogger() {
		ConsoleHandler cH = new ConsoleHandler();
		SimpleFormatter sF = new SimpleFormatter();
		cH.setFormatter(sF);
		cH.setLevel(LOGLEVEL);
		logger.setLevel(LOGLEVEL);
		logger.addHandler(cH);
	}

	/**
	 * This method kills all agents and tidy up the hole manager.
	 */
	public synchronized void cleanUp() {
	        if (agentIdMap.isEmpty() && oldAgents.isEmpty() && startParameters.isEmpty() && !tupleSpace.isConnected()) {
	            return;
	        }
		for (IThreadedAgent agent : agentIdMap.values()) {
			try {
				agent.kill();
			} catch (AgentLifecycleException e) {
				e.printStackTrace();
			}
		}
		for (IThreadedAgent agent : oldAgents.values()) {
			try {
				agent.kill();
			} catch (AgentLifecycleException e) {
				e.printStackTrace();
			}
		}
		try {
			tupleSpace.disconnect();
		} catch (TupleSpaceException e) {
			logger.info("Already disconnected");
		}
		agentIdMap.clear();
		oldAgents.clear();
		startParameters.clear();
	}

	/**
	 * This methods sends a stop-Tuple to the passed {@link IThreadedAgent}.<br>
	 * The agent itself have to make sure that it is stopped correctly. After sent the tuple, the agent is taken from
	 * the list of actual agents ( {@code agentIdMap}) and a reference is stored in the {@code oldAgents} -Map.<br>
	 * This is to make sure that if an alive-tuple with the id of an already killed agent is received (via callback)
	 * this agent can be referenced and killed the <i>"hard way"</i> (via {@code Thread.stop()})
	 * 
	 * @param agent The agent to send the stop-tuple
	 * @return The name of the agent you have forced to stop.
	 */
	public String stopAgent(IThreadedAgent agent) {
		try {
			tupleSpace.write(AgentProtocol.getStopTuple(agent.getId(), agent.getName(), new VMID()));
			oldAgents.put(agent.getId(), agent);
			agentIdMap.remove(agent.getId());
			return agent.getName();
		} catch (TupleSpaceException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * This methods sends a stop-Tuple to the passed agentId({@link VMID} as String). The agent itself have to make sure
	 * that it is stopped correctly. After sent the tuple, the agent is taken from the list of actual agents ( {@code
	 * agentIdMap} and a reference is stored in the {@code oldAgents}-Map This is to make sure that if an alive-tuple
	 * with the id of an already killed agent is received (via callback) this agent can be referenced and killed the
	 * "hard way" ({@code Thread.stop()})
	 * 
	 * @param agentId The agent's id to send the stop-tuple
	 * @return The name of the agent you have forced to stop.
	 */
	public String stopAgent(String agentId) {
		return stopAgent(agentIdMap.get(agentId));
	}

	/**
	 * This method is used to start an agent using this manager. To start this {@link IThreadedAgent} you have to pass
	 * the (fully qualified) name (e.g. eu.scy.agents.impl.ThreadedAgentMock) which can be accessed using the classpath.
	 * You can pass a {@link Map} with properties to the agent. If you pass {@code null} instead, a new HashMap with
	 * only the instance ID of this agent will be created.
	 * 
	 * @param name The (fully qualified, classpath accessible) Name of the new Agent
	 * @param params A {@link Map} with properties. Can be {@code null}
	 * @return The new (just started) {@link IThreadedAgent}
	 * @throws AgentLifecycleException If something went wrong during the creation of the new agent (e.g. in the process
	 *             of reflection) this Exception is thrown
	 */
	public IThreadedAgent startAgent(String name, Map<String, Object> params) throws AgentLifecycleException {
		try {
		        Class<?> c = agentClasses.get(name);
		        if (c == null) {
		            c = Class.forName(name);
		            agentClasses.put(name, c);
		        }
			Constructor<?> con = c.getConstructor(Map.class);
			String agentId = new VMID().toString();
			// if the params are null a new HashMap have to be created
			if (params == null) {
				params = new HashMap<String, Object>();
			}
			params.put(AgentProtocol.PARAM_AGENT_ID, agentId);
			IThreadedAgent agent = (IThreadedAgent) con.newInstance(params);
			agentIdMap.put(agentId, agent);
			startParameters.put(agentId, params);
			// There are special kinds of agents which are treated from here
			// For instance a IRepositoryAgent needs references to a
			// repository and a MetaDataTypeManager
			if (agent instanceof IRepositoryAgent) {
				IRepositoryAgent ira = (IRepositoryAgent) agent;
				ira.setMetadataTypeManager(manager);
				ira.setRepository(repository);
			}
			// The agent is started
			agent.start();
			logger.log(Level.INFO, "Agent started: " + agent.getName() + ", id: " + agent.getId());
			return agent;
		} catch (ClassNotFoundException e) {
			throw new AgentLifecycleException("Class for agent " + name + " not found! Could not be started!", e);
		} catch (SecurityException e) {
			throw new AgentLifecycleException(
					"Error during instantiation of agent " + name + "! Could not be started!", e);
		} catch (NoSuchMethodException e) {
			throw new AgentLifecycleException("Agent " + name
					+ " has no constructor as expected! Could not be started!", e);
		} catch (IllegalArgumentException e) {
			throw new AgentLifecycleException(
					"Error during instantiation of agent " + name + "! Could not be started!", e);
		} catch (InstantiationException e) {
			throw new AgentLifecycleException(
					"Error during instantiation of agent " + name + "! Could not be started!", e);
		} catch (IllegalAccessException e) {
			throw new AgentLifecycleException(
					"Error during instantiation of agent " + name + "! Could not be started!", e);
		} catch (InvocationTargetException e) {
			throw new AgentLifecycleException(
					"Error during instantiation of agent " + name + "! Could not be started!", e);
		}
	}

	/**
	 * This Method uses the "hard-way" to stop an agent. If an agent is still alive although it has been forced to stop
	 * the Thread in which this agent lives will be stopped by Thread.stop()
	 * 
	 * @param id The uniqueID ({@link VMID} as String) of the agent
	 * @throws AgentLifecycleException If something went wrong during the killing of this agent this exception is
	 *             thrown.
	 */
	public void killAgent(String id) throws AgentLifecycleException {
		IThreadedAgent agentToKill = agentIdMap.get(id);
		killAgent(agentToKill);
	}

	@Override
	public void call(Command command, int seq, Tuple afterTuple, Tuple beforeTuple) {
		if ((Command.WRITE == command) || (Command.UPDATE == command)) {
			if (AgentProtocol.ALIVE_TUPLE_TEMPLATE.matches(afterTuple)) {
				String agentId = (String) afterTuple.getField(2).getValue();
				long aliveTS = afterTuple.getLastModificationTimestamp();
				if (oldAgents.containsKey(agentId)) {
					logger.log(Level.WARNING, "Agent " + agentId
							+ " should be stopped, but is still running. Trying to kill it the hard way ...");
					IThreadedAgent agent = oldAgents.get(agentId);
					try {
						killAgent(agent);
					} catch (AgentLifecycleException e) {
						e.printStackTrace();
					}
				} else if (agentIdMap.containsKey(agentId)) {
					agentAlive.put(agentId, aliveTS);
				}
			}
		} else if (Command.DELETE == command) {
			String agentId = (String) beforeTuple.getField(2).getValue();
			if (oldAgents.containsKey(agentId)) {
				oldAgents.remove(agentId);
				startParameters.remove(agentId);
			} else if (agentIdMap.containsKey(agentId)) {
				try {
					restartAgent(agentId);
				} catch (AgentLifecycleException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void killAgent(IThreadedAgent agent) throws AgentLifecycleException {
		agent.kill();
		agentIdMap.remove(agent.getId());
	}

	/*
	 * This Method is used internally to restart an agent which should be alive but isn't. We use the kill method to
	 * stop this agent the "hard way" to make sure that (a potential) zomie-instance is shut down. After that we start
	 * an agent using his parameter.
	 */
	private void restartAgent(String agentId) throws AgentLifecycleException {
		logger.log(Level.FINE, "restartAgent with ID: " + agentId);
		IThreadedAgent agentToRestart = agentIdMap.get(agentId);
		try {
		    agentToRestart.kill();
		} catch (AgentLifecycleException e) {
		    // okay, probably the agent is already dead -> don't care
		    e.printStackTrace();
		}
		Map<String, Object> params = startParameters.get(agentId);
		startAgent(agentToRestart.getName(), params);
	}

	/**
	 * This method is just for testing purposes. In a later version this methods will be deleted.
	 * 
	 * @return A {@link Map} with the old agents in it.
	 */
	@Deprecated
	public Map<String, IThreadedAgent> getOldAgentsMap() {
		return oldAgents;
	}

	/**
	 * This method is just for testing purposes. In a later version this methods will be deleted.
	 * 
	 * @return A {@link Map} with the actual agents in it.
	 */
	@Deprecated
	public Map<String, IThreadedAgent> getAgentsIdMap() {
		return agentIdMap;
	}

	/**
	 * Set the repository.
	 * 
	 * @param repository
	 */
	public void setRepository(IRepository repository) {
		this.repository = repository;
	}

	/**
	 * Set the metadatatypemanager.
	 * 
	 * @param manager
	 */
	public void setMetadataTypeManager(IMetadataTypeManager manager) {
		this.manager = manager;
	}

}

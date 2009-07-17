package eu.scy.agents.impl.manager;

import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Callback;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import roolo.api.IRepository;
import roolo.elo.api.IELO;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.IMetadataTypeManager;

import eu.scy.agents.api.AgentLifecycleException;
import eu.scy.agents.api.IRepositoryAgent;
import eu.scy.agents.api.IThreadedAgent;
import eu.scy.agents.impl.AgentProtocol;

public class AgentManager implements Callback {

    private TupleSpace tupleSpace;

    private Map<String, IThreadedAgent> agents;

    private Map<String, IThreadedAgent> oldAgents;

    private Map<String, Long> agentAlive;

    private Map<String, Map<String, Object>> startParameters;

    // TODO: inject via spring magic
    private IRepository<IELO<IMetadataKey>, IMetadataKey> repository;

    // TODO: inject via spring magic
    private IMetadataTypeManager<IMetadataKey> manager;

    public AgentManager(String host, int port) {
        agentAlive = new HashMap<String, Long>();
        agents = new HashMap<String, IThreadedAgent>();
        oldAgents = new HashMap<String, IThreadedAgent>();
        startParameters = new HashMap<String, Map<String, Object>>();
        try {
            tupleSpace = new TupleSpace(host, port, AgentProtocol.COMMAND_SPACE_NAME);
            tupleSpace.eventRegister(Command.WRITE, AgentProtocol.ALIVE_TUPLE_TEMPLATE, this, true);
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

    protected void cleanUp() {
        for (String agentName : agents.keySet()) {
            killAgent(agentName);
        }
        for (String agentName : oldAgents.keySet()) {
            killAgent(agentName);
        }
    }

    public void stopAgent(String name) {
        try {
            tupleSpace.write(AgentProtocol.getStopTuple(name));
        } catch (TupleSpaceException e) {
            throw new RuntimeException(e);
        }
    }

    public void startAgent(String name, Map<String, Object> params) throws AgentLifecycleException {
        try {
            Class<?> c = Class.forName(name);
            Constructor<?> con = c.getConstructor(Map.class);
            if (params == null) {
                params = new HashMap<String, Object>();
            }
            IThreadedAgent agent = (IThreadedAgent) con.newInstance(params);
            agents.put(name, agent);
            startParameters.put(name, params);
            if (agent instanceof IRepositoryAgent) {
                IRepositoryAgent ira = (IRepositoryAgent) agent;
                ira.setMetadataTypeManager(manager);
                ira.setRepository(repository);
            }
            agent.start();
        } catch (ClassNotFoundException e) {
            throw new AgentLifecycleException("Class for agent " + name + " not found! Could not be started!", e);
        } catch (SecurityException e) {
            throw new AgentLifecycleException("Error during instantiation of agent " + name + "! Could not be started!", e);
        } catch (NoSuchMethodException e) {
            throw new AgentLifecycleException("Agent " + name + " has no constructor as expected! Could not be started!", e);
        } catch (IllegalArgumentException e) {
            throw new AgentLifecycleException("Error during instantiation of agent " + name + "! Could not be started!", e);
        } catch (InstantiationException e) {
            throw new AgentLifecycleException("Error during instantiation of agent " + name + "! Could not be started!", e);
        } catch (IllegalAccessException e) {
            throw new AgentLifecycleException("Error during instantiation of agent " + name + "! Could not be started!", e);
        } catch (InvocationTargetException e) {
            throw new AgentLifecycleException("Error during instantiation of agent " + name + "! Could not be started!", e);
        }
    }

    public void killAgent(String name) {
        try {
            tupleSpace.write(AgentProtocol.getKillTuple(name));
            IThreadedAgent agent = agents.remove(name);
            oldAgents.put(name, agent);
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
    public void call(Command command, int seq, Tuple afterTuple, Tuple beforeTuple) {
        if (AgentProtocol.ALIVE_TUPLE_TEMPLATE.matches(afterTuple)) {
            if ((Command.WRITE == command) || (Command.UPDATE == command)) {
                String agentName = (String) afterTuple.getField(2).getValue();
                long aliveTS = afterTuple.getLastModificationTimestamp();
                if (oldAgents.containsKey(agentName)) {
                    System.err.println("Agent " + agentName + " should be stopped, but is still running. Trying to kill it the hard way ...");
                    IThreadedAgent agent = agents.get(agentName);
                    try {
                        agent.stop();
                    } catch (AgentLifecycleException e) {
                        e.printStackTrace();
                    }
                } else if (agents.containsKey(agentName)) {
                    agentAlive.put(agentName, aliveTS);
                    System.out.println(agentName + " still alive!");
                }
            } else if (Command.DELETE == command) {
                String agentId = (String) beforeTuple.getField(2).getValue();
                if (oldAgents.containsKey(agentId)) {
                    oldAgents.remove(agentId);
                } else {
                    restartAgent(agentId);
                }
            }
        }
    }

    private void restartAgent(String agentId) {
        killAgent(agentId);
        Map<String, Object> params = startParameters.get(agentId);
        try {
            startAgent(agentId, params);
        } catch (AgentLifecycleException e) {
            e.printStackTrace();
        }
    }

}

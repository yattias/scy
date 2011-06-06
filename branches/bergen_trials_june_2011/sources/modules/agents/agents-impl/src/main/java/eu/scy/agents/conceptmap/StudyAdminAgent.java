package eu.scy.agents.conceptmap;

import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Callback;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;
import info.collide.sqlspaces.commons.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import eu.scy.agents.api.AgentLifecycleException;
import eu.scy.agents.conceptmap.enrich.CMEnricherAgent;
import eu.scy.agents.conceptmap.model.UserConceptMapAgent;
import eu.scy.agents.conceptmap.proposer.CMProposerAgent;
import eu.scy.agents.impl.AbstractThreadedAgent;
import eu.scy.agents.impl.AgentProtocol;

public class StudyAdminAgent implements Callback {

    public enum StudyAgent {
        PROPOSER,
        MODELLER,
        ENRICHER;

        public static List<String> agentNames = new ArrayList<String>();

        static {
            agentNames.addAll(Arrays.asList(new String[] { "Sofie", "Marie", "Anne", "Emma", "Lena", "Hanna", "Leonie", "Johanna", "Mia", "Frieda", "Lea", "Luisa", "Maria", "Lisa", "Alina", "Laura", "Amelie", "Lilly", "Mareen", "Merle", "Klara", "Charlotte", "Aurelie", "Celine", "Pauline", "Lorena", "Mariella", "Julia", "Zoe", "Marlene", "Sara", "Fiona", "Maja", "Helen", "Mara", "Jamila", "Maret", "Eva", "Doreen", "Melina", "Emilie", "Janine", "Madita", "Naomi", "Nele", "Mala", "Jette", "Livia", "Olivia", "Paula", "Samira", "Phoebe", "Chloe", "Lana", "Tabea", "Rahel", "Theresa", "Stella", "Samina", "Xenia", "Ann", "May", "Viviana", "Malaika", "Laila", "Viktoria", "Greta", "Liselotte", "Martha", "Annabell", "Magdalena", "Henriette", "Emina", "Ivette", "Samanta", "Thea", "Sabina", "Leonore", "Aileen", "Eliana", "Tialda", "Eve", "Bea", "Clementine", "Tina", "Gisella", "Juliette", "Talida", "Grete", "Heidi", "Leni", "Hermine", "Violett", "Ariane", "Susanne", "Dorit", "Maileen", "Felicitas", "Nina", "Karina" }));
        }

        public static void freeName(String name) {
            agentNames.add(name);
        }

        public AbstractThreadedAgent start(Map<String,Object> params) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            String name = agentNames.remove((int) (Math.random() * agentNames.size()));
            map.put(AgentProtocol.PARAM_AGENT_ID, name);
            map.put(AgentProtocol.TS_HOST, "localhost");
            map.put(AgentProtocol.TS_PORT, 2525);
            AbstractThreadedAgent agent = null;
            switch (this) {
                case ENRICHER:
                    agent = new CMEnricherAgent(map);
                    break;
                case MODELLER:
                    agent = new UserConceptMapAgent(map);
                    break;
                case PROPOSER:
                    if (params!=null && params.get("observer")!=null){
                        map.put("observer", params.get("observer"));
                    }
                    agent = new CMProposerAgent(map);
                    break;
            }
            try {
                agent.start();
                return agent;
            } catch (AgentLifecycleException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    private TupleSpace commandSpace;

    private Map<String, AbstractThreadedAgent> agents;

    public StudyAdminAgent() {
        try {
            commandSpace = new TupleSpace(new User("AdminAgent"),"localhost", 2525, "command");
            commandSpace.eventRegister(Command.WRITE, new Tuple("study", String.class, String.class), this, true);
            agents = new HashMap<String, AbstractThreadedAgent>();
            //startAgent(StudyAgent.ENRICHER);
           // startAgent(StudyAgent.MODELLER);
          //  startAgent(StudyAgent.PROPOSER);
        } catch (TupleSpaceException e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void call(Command cmd, int seqnum, Tuple afterTuple, Tuple beforeTuple) {
        String command = afterTuple.getField(1).getValue().toString();
        String param = afterTuple.getField(2).getValue().toString();
        if ("start".equals(command)) {
            StudyAgent agentType = StudyAgent.valueOf(param.toUpperCase());
            if (agentType == StudyAgent.ENRICHER) {
                for (Entry<String, AbstractThreadedAgent> e : agents.entrySet()) {
                    if (e.getValue() instanceof CMEnricherAgent) {
                        stopAgent(e.getKey());
                        System.err.println("OMG, " + e.getKey() + " was just killed!");
                        break;
                    }
                }
            }
            startAgent(agentType,null);
        } else if ("stop".equals(command)) {
            stopAgent(param);
        }
        try {
            commandSpace.delete(afterTuple);
        } catch (TupleSpaceException e) {
            e.printStackTrace();
        }
    }

    public void stopAgent(String agentName) {
        if (agents.containsKey(agentName)) {
            try {
                AbstractThreadedAgent agent = agents.get(agentName);
                agent.kill();
                commandSpace.delete(new Tuple("study admin", String.class, agent.getId()));
                agents.remove(agentName);
                StudyAgent.freeName(agentName);
            } catch (AgentLifecycleException e) {
                e.printStackTrace();
            } catch (TupleSpaceException e) {
                e.printStackTrace();
            }
            // System.out.println(agentName + " stopped");
        } else {
            System.err.println("Agent named '" + agentName + "' not found!");
        }
    }

    public String startAgent(StudyAgent agentType, Map<String,Object> params) {
        AbstractThreadedAgent agent = agentType.start(params);
        agents.put(agent.getId(), agent);
        try {
            commandSpace.write(new Tuple("study admin", agentType.name(), agent.getId()));
        } catch (TupleSpaceException e) {
            e.printStackTrace();
        }
        // System.out.println(agent.getId() + " (" + agentType.name().toLowerCase() + ") started");
        return agent.getId();
    }

}

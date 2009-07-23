package eu.scy.agents.supervisor;

import info.collide.sqlspaces.commons.Callback;
import info.collide.sqlspaces.commons.Field;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.rmi.dgc.VMID;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import eu.scy.agents.api.AgentLifecycleException;
import eu.scy.agents.impl.AbstractThreadedAgent;
import eu.scy.agents.impl.AgentProtocol;

public class SupervisingAgent extends AbstractThreadedAgent implements Callback {

    private static final Tuple alive_template = new Tuple(AgentProtocol.COMMAND_LINE, String.class, String.class, String.class, AgentProtocol.ALIVE);

    private static final Logger logger = Logger.getLogger(SupervisingAgent.class.getName());

    private static final String name = "supervising agent";

    private static final Tuple query_template = new Tuple("query", Field.createWildCardField());

    private static final Tuple response_template = new Tuple("response", Field.createWildCardField());

    private static final long ident_timeout = 5 * 1000;

    private boolean debug = true;

    private boolean isStopped;

    private List<String> knownAgents;

    private Map<String, List<Tuple>> agentInDoc;

    private Map<String, List<Tuple>> agentOutDoc;

    private Map<String, Set<String>> agentInstances;

    private Map<Integer, CallbackSequence> cbMap;

    public enum CallbackSequence {
        ALIVE_WRITTEN,
        ALIVE_UPDATED,
        ALIVE_DELETED,
        QUERY_WRITTEN,
        RESPONSE_WRITTEN;
    }

    public SupervisingAgent(Map<String, Object> map) {
        super(name, (String) map.get("id"));
        knownAgents = new ArrayList<String>();
        agentInDoc = new HashMap<String, List<Tuple>>();
        agentOutDoc = new HashMap<String, List<Tuple>>();
        agentInstances = new HashMap<String, Set<String>>();
        cbMap = new HashMap<Integer, CallbackSequence>();
        initLogger();
        logger.log(Level.FINEST, name + " up and running...");
    }

    @Override
    public void call(Command command, int seq, Tuple afterTuple, Tuple beforeTuple) {
        super.call(command, seq, afterTuple, beforeTuple);
        CallbackSequence cbseq = cbMap.get(seq);
        switch (cbseq) {
            case ALIVE_WRITTEN:
                if (afterTuple.getFields().length >= 4) {
                    logger.log(Level.FINEST, "Alive written");
                    aliveWritten(afterTuple);
                } else {
                    logger.log(Level.WARNING, "This is strange! There is a Tuple with only " + afterTuple.getFields().length + " fields. Have you implemented the TupleDoc document in the right manner? Hu?  ");
                }
                break;
            case ALIVE_UPDATED:
                if (afterTuple.getFields().length >= 4) {
                    logger.log(Level.FINEST, "Alive updated");
                    aliveUpdated(afterTuple);
                } else {
                    logger.log(Level.WARNING, "This is strange! There is a Tuple with only " + afterTuple.getFields().length + " fields. Have you implemented the TupleDoc document in the right manner? Hu?  ");
                }
                break;
            case ALIVE_DELETED:
                if (beforeTuple.getFields().length >= 4) {
                    logger.log(Level.FINEST, "Alive deleted");
                    aliveDeleted(beforeTuple);
                } else {
                    logger.log(Level.WARNING, "This is strange! There is a Tuple with only " + afterTuple.getFields().length + " fields. Have you implemented the TupleDoc document in the right manner? Hu?  ");
                }
                break;
            case QUERY_WRITTEN:
                if (afterTuple.getFields().length >= 4) {
                    logger.log(Level.FINEST, "Query written");
                    queryWritten(afterTuple);
                } else {
                    logger.log(Level.WARNING, "This is strange! There is a Tuple with only " + afterTuple.getFields().length + " fields. Have you implemented the TupleDoc document in the right manner? Hu?  ");
                }
                break;
            case RESPONSE_WRITTEN:
                if (afterTuple.getFields().length >= 4) {
                    logger.log(Level.FINEST, "Response written");
                    responseWritten(afterTuple);
                } else {
                    logger.log(Level.WARNING, "This is strange! There is a Tuple with only " + afterTuple.getFields().length + " fields. Have you implemented the TupleDoc document in the right manner? Hu?  ");
                }
                break;
            default:
                logger.log(Level.WARNING, "Hmmmmm....I got a callback i didn't registered....WTF?");
                break;
        }
    }

    private void responseWritten(Tuple afterTuple) {
        String agentName = afterTuple.getField(3).getValue().toString();
        if (knownAgents.contains(agentName)) {

            List<Tuple> tupleDoc = agentOutDoc.get(agentName);
            for (Tuple tuple : tupleDoc) {
                if (tuple.matches(afterTuple)) {
                    System.out.println("Hey Mister " + agentName + ". Your answer" + afterTuple + " fulfilles the TupleDoc conventions.");
                    break;
                }
            }
        } else {
            logger.log(Level.WARNING, "There is no agent : " + agentName + ". Who the hell are you?");
        }
    }

    private void queryWritten(Tuple afterTuple) {
        String agentName = afterTuple.getField(3).getValue().toString();
        if (knownAgents.contains(agentName)) {

            List<Tuple> tupleDoc = agentInDoc.get(agentName);
            for (Tuple tuple : tupleDoc) {
                if (tuple.matches(afterTuple)) {
                    System.out.println("YES! The agent " + agentName + " can interpret your query :" + afterTuple);
                    break;
                }
            }
        } else {
            logger.log(Level.WARNING, "There is no agent : " + agentName);
        }
    }

    private void aliveDeleted(Tuple beforeTuple) {
        String agentId = beforeTuple.getField(2).getValue().toString();
        String agentName = beforeTuple.getField(3).getValue().toString();
        if (agentInstances.containsKey(agentName)) {
            Set<String> agentIds = agentInstances.get(agentName);
            boolean deleted = agentIds.remove(agentId);

            if (deleted) {
                if (agentIds.isEmpty()) {
                    knownAgents.remove(agentName);
                    agentInDoc.remove(agentName);
                    agentOutDoc.remove(agentName);
                }
            } else {
                logger.log(Level.WARNING, "Agent " + agentName + " with id=" + agentId + " could not be deleted...");
            }
        } else {
            logger.log(Level.WARNING, "An agent disappeared, but wasn't registered here....");
        }
    }

    private void aliveUpdated(Tuple afterTuple) {
    // Nothing to do yet
    }

    private void aliveWritten(Tuple afterTuple) {
        String agentId = afterTuple.getField(3).getValue().toString();
        String agentName = afterTuple.getField(2).getValue().toString();

        if (!knownAgents.contains(agentName)) {
            VMID queryId = new VMID();
            Tuple identifyQuery = AgentProtocol.getIdentifyTuple(agentId, agentName, queryId);
            String id = identifyQuery.getField(1).getValue().toString();
            try {
                getTupleSpace().write(identifyQuery);
                Tuple identifyResponse = getTupleSpace().waitToTake(new Tuple(AgentProtocol.COMMAND_LINE, queryId.toString(), id, name, Field.createWildCardField()), ident_timeout);
                if (identifyResponse == null) {
                    logger.log(Level.WARNING, "QID: "+queryId.toString()+"Agent " + agentName + " with ID=" + agentId + " does not respond on identify query...");
                } else {
                    ArrayList<String> tupleDocEntries = new ArrayList<String>();
                    for (int i = 3; i < identifyResponse.getFields().length; i++) {

                        String entry = identifyResponse.getField(i).getValue().toString();

                        tupleDocEntries.add(entry);
                    }
                    knownAgents.add(agentName);
                    ArrayList<Tuple> in = new ArrayList<Tuple>();
                    ArrayList<Tuple> out = new ArrayList<Tuple>();
                    for (String tupleDoc : tupleDocEntries) {
                        try {
                            in.add(convertStringToTuple(tupleDoc, true));
                            out.add(convertStringToTuple(tupleDoc, false));
                        } catch (Exception e) {
                            logger.log(Level.SEVERE, "Something very bad happened, but: Stand back, I know RegEx!");
                        }
                    }
                    agentInDoc.put(agentName, in);
                    agentOutDoc.put(agentName, out);
                    HashSet<String> al = new HashSet<String>();
                    al.add(agentId);
                    agentInstances.put(agentName, al);
                    logger.log(Level.FINEST, "Coool, a new Agent appeared. Her sweet little name is " + agentName + ". Her ID is " + agentId + ". Gratz to the parents: She is healthy and has all TupleDoc a small Agent needs.");
                }
            } catch (TupleSpaceException e) {
                logger.log(Level.SEVERE, e.getMessage());
            }
        } else {
            if (agentInstances.containsKey(agentName)) {
                Set<String> agentIds = agentInstances.get(agentName);
                agentIds.add(agentId);
            } else {
                logger.log(Level.SEVERE, "Something went bitterly wrong......");
            }
        }
    }

    @Override
    protected void doRun() throws TupleSpaceException, AgentLifecycleException {
        sendAliveUpdate();
        int[] cbSeq = new int[5];
        // register callbacks
        cbSeq[0] = getTupleSpace().eventRegister(Command.WRITE, alive_template, this, true);
        cbSeq[1] = getTupleSpace().eventRegister(Command.UPDATE, alive_template, this, true);
        cbSeq[2] = getTupleSpace().eventRegister(Command.DELETE, alive_template, this, true);
        cbSeq[3] = getTupleSpace().eventRegister(Command.WRITE, query_template, this, true);
        cbSeq[4] = getTupleSpace().eventRegister(Command.WRITE, response_template, this, true);
        cbMap.put(cbSeq[0], CallbackSequence.ALIVE_WRITTEN);
        cbMap.put(cbSeq[1], CallbackSequence.ALIVE_UPDATED);
        cbMap.put(cbSeq[2], CallbackSequence.ALIVE_DELETED);
        cbMap.put(cbSeq[3], CallbackSequence.QUERY_WRITTEN);
        cbMap.put(cbSeq[4], CallbackSequence.RESPONSE_WRITTEN);
        logger.log(Level.FINEST, cbSeq.length + " callbacks registered");
    }

    @Override
    protected void doStop() {
        // deregister all callbacks
        for (Integer seq : cbMap.keySet()) {
            if (seq != 0) {
                try {
                    getTupleSpace().eventDeRegister(seq);
                    getTupleSpace().disconnect();
                } catch (TupleSpaceException e) {
                    logger.log(Level.SEVERE, e.getMessage());
                }
            }
        }
        isStopped = true;
        logger.log(Level.FINEST, "Supervisor stopped");

    }

    private void initLogger() {
        ConsoleHandler cH = new ConsoleHandler();
        SimpleFormatter sF = new SimpleFormatter();
        cH.setFormatter(sF);
        if (debug) {
            cH.setLevel(Level.ALL);
            logger.setLevel(Level.ALL);
        } else {
            // TODO is Level.fine the right choice?
            cH.setLevel(Level.FINE);
            logger.setLevel(Level.FINE);
        }
        logger.addHandler(cH);
    }

    @Override
    public boolean isStopped() {
        return isStopped;
    }

    public Set<String> getAllAgentIds(String agentName) {
        return agentInstances.get(agentName);

    }

    private Tuple convertStringToTuple(String tupleDoc, boolean intuple) throws ClassNotFoundException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException, SecurityException, NoSuchMethodException {
        Tuple t = new Tuple();
        String s = tupleDoc.split("->")[intuple ? 0 : 1];
        Pattern p = Pattern.compile("(\"[^\"]+\"|<[^>]+>):\\w+");
        Matcher m = p.matcher(s);
        Class<?> c = null;
        while (m.find()) {
            String fieldName = m.group();
            if (fieldName.split(":")[1].toLowerCase().equals("xml")) {
                c = org.w3c.dom.Document.class;
            } else if (fieldName.split(":")[1].toLowerCase().equals("binary")) {
                c = byte[].class;
            } else {
                c = Class.forName("java.lang." + fieldName.split(":")[1]);
            }
            Field f;
            if (fieldName.startsWith("\"")) {
                Constructor<?> con = c.getConstructor(String.class);
                Object o = con.newInstance(fieldName.split(":")[0]);
                f = new Field(o);
            } else {
                f = new Field(c);
            }
            t.add(f);
        }
        return t;
    }

    public static void main(String[] args) {
        String agentId = new VMID().toString();
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("id", agentId);
        Thread t = new Thread(new SupervisingAgent(map));
        t.start();
    }

    @Override
    protected Tuple getIdentifyTuple(String queryId) {
        return null;
    }

}

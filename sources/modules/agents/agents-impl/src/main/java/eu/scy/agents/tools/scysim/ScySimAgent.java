package eu.scy.agents.tools.scysim;

import info.collide.sqlspaces.commons.Callback;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;
import info.collide.sqlspaces.commons.util.XMLUtils;

import java.io.IOException;
import java.rmi.dgc.VMID;
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.jdom.JDOMException;
import org.w3c.dom.Document;

import eu.scy.actionlogging.api.IAction;
import eu.scy.agents.api.AgentLifecycleException;
import eu.scy.agents.impl.AbstractThreadedAgent;
import eu.scy.agents.impl.AgentProtocol;

public class ScySimAgent extends AbstractThreadedAgent implements Callback {

    private static final String name = "eu.scy.agents.tools.scysim.ScySimAgent";

    private final Tuple scySimTemplate = new Tuple(String.class, String.class, String.class, String.class, "scysimulator", String.class, Document.class);

    private static final Logger logger = Logger.getLogger(ScySimAgent.class.getName());

    private static final Level DEBUGLEVEL = Level.ALL;

    private int cbSeq;

    private ArrayList<Double> diff;

    private enum Type {
        VARS_SELECTED {

            public String toString() {
                return "variables_selected";
            }
        },

        VALUE_CHANGED {

            public String toString() {
                return "value_changed";
            }
        }
    }

    // TODO fill in TupleDoc
    private String tupleDoc = "To be filled....";

    private double tolerance = 10;

    private String examinedVarName;

    public ScySimAgent(Map<String, Object> map) {
        super(name, (String) map.get("id"));
        examinedVarName = "Mtot";
        initLogger();
        try {
            cbSeq = getTupleSpace().eventRegister(Command.WRITE, scySimTemplate, this, true);
            logger.log(Level.FINEST, "Callback registered");
        } catch (TupleSpaceException e) {
            e.printStackTrace();
        }
        diff = new ArrayList<Double>();
        logger.log(Level.FINEST, name + " initiated");
    }

    @Override
    protected void doRun() throws TupleSpaceException, AgentLifecycleException {
        while (status == Status.Running) {
            sendAliveUpdate();
            try {
                Thread.sleep(AgentProtocol.ALIVE_INTERVAL - 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void doStop() {
        status = Status.Stopping;
        if (cbSeq != 0) {
            try {
                getTupleSpace().eventDeRegister(cbSeq);
            } catch (TupleSpaceException e) {
                logger.log(Level.SEVERE, e.getMessage());
            }
        }
        logger.log(Level.FINE, name + " stopped");
    }

    @Override
    protected Tuple getIdentifyTuple(String queryId) {
        Tuple t = new Tuple(AgentProtocol.RESPONSE, queryId, this.getId(), this.getName(), AgentProtocol.MESSAGE_IDENTIFY, tupleDoc);
        return t;
    }

    @Override
    public boolean isStopped() {
        return false;
    }

    @Override
    public void call(Command command, int seq, Tuple afterTuple, Tuple beforeTuple) {
        logger.log(Level.FINEST, "Callback arrived");
        if (cbSeq != seq) {
            // If a callback arrives here that wasn't registered from this class it is passed to the AbstractThreadedAgent.
            logger.log(Level.FINEST, "Callback passed to Superclass.");
            super.call(command, seq, afterTuple, beforeTuple);
            return;
        }

        try {
            IAction action = new eu.scy.actionlogging.logger.Action(XMLUtils.transformDocumentToString((Document) afterTuple.getField(6).getValue()));
            // If selected vars changed
            String actionID = afterTuple.getField(0).getValue().toString().trim();
            String time = afterTuple.getField(1).getValue().toString().trim();
            String type = afterTuple.getField(2).getValue().toString().trim();
            ;
            String user = afterTuple.getField(3).getValue().toString().trim();
            // String tool = action.getContext("tool");
            // String mission = action.getContext("mission");
            String selection = "";
            String variableName = "";
            String oldValue = "";
            String newValue = "";

            if (type.trim().equals(Type.VARS_SELECTED.toString())) {
                selection = action.getAttribute("selected_variables");
                logger.log(Level.FINEST, "Selected Vars (User:" + user + "): " + selection);

                // if a value of a selected variable changed
            } else if (type.equals(Type.VALUE_CHANGED.toString())) {
                variableName = action.getAttribute("name");
                oldValue = action.getAttribute("oldValue");
                newValue = action.getAttribute("newValue");
                logger.log(Level.FINEST, "Value changed (User: " + user + ", Variable: " + variableName + "): From " + oldValue + " to " + newValue);
                if (variableName.trim().equals(examinedVarName.trim())) {
                    if (Math.abs(Double.parseDouble(newValue)) <= tolerance) {
                        diff.clear();
                    }
                    if (diff.size() >= 10 && diff.size() < 20) {
                        VMID qid = new VMID();
                        // TODO retrieve user experience from another agent
                        Tuple scaffoldQuery = new Tuple(AgentProtocol.QUERY, qid, String.class, "simquest actuator", "first level scaffold", 50);
                        Tuple scaffoldResponse = new Tuple(AgentProtocol.RESPONSE, qid, String.class, AgentProtocol.ResonseType.OK);
                        try {
                            getTupleSpace().write(scaffoldQuery);
                            getTupleSpace().waitToTake(scaffoldResponse, 1000);
                        } catch (TupleSpaceException e) {
                            e.printStackTrace();
                        }
                        logger.log(Level.FINEST, "First scaffold");
                    } else if (diff.size() >= 20 && diff.size() < 30) {
                        VMID qid = new VMID();
                        Tuple scaffoldQuery = new Tuple(AgentProtocol.QUERY, qid, String.class, "simquest actuator", "second level scaffold", 50);
                        Tuple scaffoldResponse = new Tuple(AgentProtocol.RESPONSE, qid, String.class, AgentProtocol.ResonseType.OK);
                        try {
                            getTupleSpace().write(scaffoldQuery);
                            getTupleSpace().waitToTake(scaffoldResponse, 1000);
                        } catch (TupleSpaceException e) {
                            e.printStackTrace();
                        }
                        logger.log(Level.FINEST, "Second scaffold");
                    } else if (diff.size() >= 30) {
                        VMID qid = new VMID();
                        Tuple scaffoldQuery = new Tuple(AgentProtocol.QUERY, qid, String.class, "simquest actuator", "second level scaffold", 50);
                        Tuple scaffoldResponse = new Tuple(AgentProtocol.RESPONSE, qid, String.class, AgentProtocol.ResonseType.OK);
                        try {
                            getTupleSpace().write(scaffoldQuery);
                            getTupleSpace().waitToTake(scaffoldResponse, 1000);
                        } catch (TupleSpaceException e) {
                            e.printStackTrace();
                        }
                        logger.log(Level.FINEST, "Final scaffold");
                    }

                    diff.add(Math.abs(Double.parseDouble(newValue)));
                }
            }
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void initLogger() {
        ConsoleHandler cH = new ConsoleHandler();
        SimpleFormatter sF = new SimpleFormatter();
        cH.setFormatter(sF);
        cH.setLevel(DEBUGLEVEL);
        logger.setLevel(DEBUGLEVEL);
        logger.addHandler(cH);
    }

}

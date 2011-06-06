package eu.scy.agents.tools.scysim;

import info.collide.sqlspaces.commons.Callback;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;

import java.rmi.dgc.VMID;
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.dom4j.Element;
import org.w3c.dom.Document;

import eu.scy.actionlogging.ActionXMLTransformer;
import eu.scy.actionlogging.api.IAction;
import eu.scy.agents.api.AgentLifecycleException;
import eu.scy.agents.impl.AbstractThreadedAgent;
import eu.scy.agents.impl.AgentProtocol;

public class ScySimAgent extends AbstractThreadedAgent implements Callback {

	private static final String NAME = ScySimAgent.class.getName();

	private final Tuple scySimTemplate = new Tuple(String.class, String.class,
			String.class, String.class, "scysimulator", String.class,
			Document.class);

	private static final Logger LOGGER = Logger.getLogger(ScySimAgent.class
			.getName());

	private static final Level DEBUGLEVEL = Level.ALL;

	private int cbSeq;

	private ArrayList<Double> diff;

	private enum Type {
		VARS_SELECTED {

			@Override
			public String toString() {
				return "variables_selected";
			}
		},

		VALUE_CHANGED {

			@Override
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
		super(NAME, (String) map.get(AgentProtocol.PARAM_AGENT_ID));
		examinedVarName = "Mtot";
		initLogger();
		try {
			cbSeq = getCommandSpace().eventRegister(Command.WRITE,
					scySimTemplate, this, true);
			LOGGER.log(Level.FINEST, "Callback registered");
		} catch (TupleSpaceException e) {
			e.printStackTrace();
		}
		diff = new ArrayList<Double>();
		LOGGER.log(Level.FINEST, NAME + " initiated");
	}

	@Override
	protected void doRun() throws TupleSpaceException, AgentLifecycleException,
			InterruptedException {
		while (status == Status.Running) {
			sendAliveUpdate();
			Thread.sleep(AgentProtocol.COMMAND_EXPIRATION);
		}
	}

	@Override
	protected void doStop() {
		status = Status.Stopping;
		if (cbSeq != 0) {
			try {
				getCommandSpace().eventDeRegister(cbSeq);
			} catch (TupleSpaceException e) {
				e.printStackTrace();
				LOGGER.log(Level.SEVERE, e.getMessage());
			}
		}
		LOGGER.log(Level.FINE, NAME + " stopped");
	}

	@Override
	protected Tuple getIdentifyTuple(String queryId) {
		Tuple t = new Tuple(AgentProtocol.RESPONSE, queryId, getId(),
				getName(), AgentProtocol.MESSAGE_IDENTIFY, tupleDoc);
		return t;
	}

	@Override
	public boolean isStopped() {
		return false;
	}

	@Override
	public void call(Command command, int seq, Tuple afterTuple,
			Tuple beforeTuple) {
		LOGGER.log(Level.FINEST, "Callback arrived");
		if (cbSeq != seq) {
			// If a callback arrives here that wasn't registered from this class
			// it is passed to the
			// AbstractThreadedAgent.
			LOGGER.log(Level.FINEST, "Callback passed to Superclass.");
			super.call(command, seq, afterTuple, beforeTuple);
			return;
		}
		// TODO Look up if this is working.....
		IAction action = new ActionXMLTransformer((Element) afterTuple
				.getField(7).getValue()).getActionAsPojo();
		// If selected vars changed
		String actionID = afterTuple.getField(0).getValue().toString().trim();
		String time = afterTuple.getField(1).getValue().toString().trim();
		String type = afterTuple.getField(2).getValue().toString().trim();
		String user = afterTuple.getField(3).getValue().toString().trim();
		// String tool = action.getContext("tool");
		// String mission = action.getContext("mission");
		String selection = "";
		String variableName = "";
		String oldValue = "";
		String newValue = "";

		if (type.trim().equals(Type.VARS_SELECTED.toString())) {
			selection = action.getAttribute("selected_variables");
			LOGGER.log(Level.FINEST, "Selected Vars (User:" + user + "): "
					+ selection);

			// if a value of a selected variable changed
		} else if (type.equals(Type.VALUE_CHANGED.toString())) {
			variableName = action.getAttribute("name");
			oldValue = action.getAttribute("oldValue");
			newValue = action.getAttribute("newValue");
			LOGGER.log(Level.FINEST, "Value changed (User: " + user
					+ ", Variable: " + variableName + "): From " + oldValue
					+ " to " + newValue);
			if (variableName.trim().equals(examinedVarName.trim())) {
				if (Math.abs(Double.parseDouble(newValue)) <= tolerance) {
					diff.clear();
				}
				if (diff.size() >= 10 && diff.size() < 20) {
					VMID qid = new VMID();
					// TODO retrieve user experience from another agent
					Tuple scaffoldQuery = new Tuple(AgentProtocol.QUERY, qid,
							String.class, "simquest actuator",
							"first level scaffold", 50);
					Tuple scaffoldResponse = new Tuple(AgentProtocol.RESPONSE,
							qid, String.class, AgentProtocol.ResonseType.OK);
					try {
						getCommandSpace().write(scaffoldQuery);
						getCommandSpace().waitToTake(scaffoldResponse, 1000);
					} catch (TupleSpaceException e) {
						e.printStackTrace();
					}
					LOGGER.log(Level.FINEST, "First scaffold");
				} else if (diff.size() >= 20 && diff.size() < 30) {
					VMID qid = new VMID();
					Tuple scaffoldQuery = new Tuple(AgentProtocol.QUERY, qid,
							String.class, "simquest actuator",
							"second level scaffold", 50);
					Tuple scaffoldResponse = new Tuple(AgentProtocol.RESPONSE,
							qid, String.class, AgentProtocol.ResonseType.OK);
					try {
						getCommandSpace().write(scaffoldQuery);
						getCommandSpace().waitToTake(scaffoldResponse, 1000);
					} catch (TupleSpaceException e) {
						e.printStackTrace();
					}
					LOGGER.log(Level.FINEST, "Second scaffold");
				} else if (diff.size() >= 30) {
					VMID qid = new VMID();
					Tuple scaffoldQuery = new Tuple(AgentProtocol.QUERY, qid,
							String.class, "simquest actuator",
							"second level scaffold", 50);
					Tuple scaffoldResponse = new Tuple(AgentProtocol.RESPONSE,
							qid, String.class, AgentProtocol.ResonseType.OK);
					try {
						getCommandSpace().write(scaffoldQuery);
						getCommandSpace().waitToTake(scaffoldResponse, 1000);
					} catch (TupleSpaceException e) {
						e.printStackTrace();
					}
					LOGGER.log(Level.FINEST, "Final scaffold");
				}

				diff.add(Math.abs(Double.parseDouble(newValue)));
			}
		}
	}

	private void initLogger() {
		ConsoleHandler cH = new ConsoleHandler();
		SimpleFormatter sF = new SimpleFormatter();
		cH.setFormatter(sF);
		cH.setLevel(DEBUGLEVEL);
		LOGGER.setLevel(DEBUGLEVEL);
		LOGGER.addHandler(cH);
	}

}

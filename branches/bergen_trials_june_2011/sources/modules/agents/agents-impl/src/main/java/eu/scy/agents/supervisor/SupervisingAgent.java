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

/**
 * The Supervising agent provides functionality to check if new agents are
 * available and identifies them with their tupledoc
 * 
 * @author Jan Engler
 */
public class SupervisingAgent extends AbstractThreadedAgent implements Callback {

	public static final String NAME = SupervisingAgent.class.getName();

	private static final Tuple alive_template = new Tuple(
			AgentProtocol.COMMAND_LINE, String.class, String.class,
			String.class, AgentProtocol.ALIVE);

	private static final Logger logger = Logger
			.getLogger(SupervisingAgent.class.getName());

	private static final Tuple query_template = new Tuple(AgentProtocol.QUERY,
			Field.createWildCardField());

	private static final Tuple response_template = new Tuple(
			AgentProtocol.RESPONSE, Field.createWildCardField());

	private static final long ident_timeout = 5 * 1000;

	private static final Level DEBUGLEVEL = Level.FINE;

	private boolean isStopped;

	private List<String> knownAgents;

	private Map<String, List<Tuple>> agentInDoc;

	private Map<String, List<Tuple>> agentOutDoc;

	private Map<String, Set<String>> agentInstances;

	private Map<Integer, CallbackSequence> cbMap;

	public enum CallbackSequence {
		ALIVE_ALL, QUERY_WRITTEN, RESPONSE_WRITTEN;
	}

	public SupervisingAgent(Map<String, Object> map) {
		super(NAME, (String) map.get(AgentProtocol.PARAM_AGENT_ID));
		knownAgents = new ArrayList<String>();
		agentInDoc = new HashMap<String, List<Tuple>>();
		agentOutDoc = new HashMap<String, List<Tuple>>();
		agentInstances = new HashMap<String, Set<String>>();
		cbMap = new HashMap<Integer, CallbackSequence>();
		initLogger();
		logger.log(Level.FINE, NAME + " up and running...");
		int[] cbSeq = new int[3];
		// register callbacks
		try {
			// Does that transaction work?
			getCommandSpace().beginTransaction();
			cbSeq[0] = getCommandSpace().eventRegister(Command.ALL,
					alive_template, this, true);
			cbSeq[1] = getCommandSpace().eventRegister(Command.WRITE,
					query_template, this, true);
			cbSeq[2] = getCommandSpace().eventRegister(Command.WRITE,
					response_template, this, true);
			getCommandSpace().commitTransaction();
		} catch (TupleSpaceException e) {
			e.printStackTrace();
		}
		cbMap.put(cbSeq[0], CallbackSequence.ALIVE_ALL);
		cbMap.put(cbSeq[1], CallbackSequence.QUERY_WRITTEN);
		cbMap.put(cbSeq[2], CallbackSequence.RESPONSE_WRITTEN);
		logger.log(Level.FINEST, cbSeq.length + " callbacks registered");
	}

	@Override
	public void call(Command command, int seq, Tuple afterTuple,
			Tuple beforeTuple) {
		logger.log(Level.FINEST, "Callback arrived");
		if (cbMap.get(seq) == null) {
			// If a callback arrives here that wasn't registered from this class
			// it is passed to the AbstractThreadedAgent.
			logger.log(Level.FINEST, "Callback passed to Superclass.");
			super.call(command, seq, afterTuple, beforeTuple);
			return;
		}
		String agentName = "";

		// get the name of the agent
		if (beforeTuple != null && beforeTuple.getFields().length >= 3) {
			agentName = beforeTuple.getField(3).getValue().toString();
		}
		if (afterTuple != null && afterTuple.getFields().length >= 3) {
			agentName = afterTuple.getField(3).getValue().toString();
		}
		// if the call was initiated by myself, i don't care
		if (!agentName.trim().equals(SupervisingAgent.NAME)) {
			CallbackSequence cbseq = cbMap.get(seq);
			switch (cbseq) {
			case ALIVE_ALL:
				switch (command) {
				case WRITE:
					if (afterTuple.getFields().length >= 4) {
						logger.log(Level.FINEST, "Alive written");
						aliveWritten(afterTuple);
					} else {
						logger.log(
								Level.WARNING,
								"This is strange! There is a Tuple with only "
										+ afterTuple.getFields().length
										+ " fields. Have you implemented the TupleDoc document in the right manner? ");
					}
					break;
				case UPDATE:
					if (afterTuple.getFields().length >= 4) {
						logger.log(Level.FINEST, "Alive updated");
						aliveUpdated(afterTuple);
					} else {
						logger.log(
								Level.WARNING,
								"This is strange! There is a Tuple with only "
										+ afterTuple.getFields().length
										+ " fields. Have you implemented the TupleDoc document in the right manner?");
					}
					break;
				case DELETE:
					if (beforeTuple.getFields().length >= 4) {
						logger.log(Level.FINEST, "Alive deleted");
						aliveDeleted(beforeTuple);
					} else {
						logger.log(
								Level.WARNING,
								"This is strange! There is a Tuple with only "
										+ afterTuple.getFields().length
										+ " fields. Have you implemented the TupleDoc document in the right manner? ");
					}
					break;
				default:
					break;
				}
				break;
			case QUERY_WRITTEN:
				if (afterTuple.getFields().length >= 4) {
					logger.log(Level.FINEST, "Query written");
					queryWritten(afterTuple);
				} else {
					logger.log(
							Level.WARNING,
							"This is strange! There is a Tuple with only "
									+ afterTuple.getFields().length
									+ " fields. Have you implemented the TupleDoc document in the right manner? Hu?  ");
				}
				break;
			case RESPONSE_WRITTEN:
				if (afterTuple.getFields().length >= 4) {
					logger.log(Level.FINEST, "Response written");
					responseWritten(afterTuple);
				} else {
					logger.log(
							Level.WARNING,
							"This is strange! There is a Tuple with only "
									+ afterTuple.getFields().length
									+ " fields. Have you implemented the TupleDoc document in the right manner? Hu?  ");
				}
				break;
			default:
				logger.log(Level.WARNING,
						"There was a callback i didn't registered.");
				break;
			}
		}
	}

	private void responseWritten(Tuple afterTuple) {
		String agentName = afterTuple.getField(3).getValue().toString();
		if (!afterTuple.getField(4).getValue().toString().trim()
				.equals(AgentProtocol.MESSAGE_IDENTIFY)) {
			if (knownAgents.contains(agentName)) {
				List<Tuple> tupleDoc = agentOutDoc.get(agentName);
				for (Tuple tuple : tupleDoc) {
					if (tuple.matches(afterTuple)) {
						logger.log(Level.FINE, "Response matches TupleDOC");
						break;
					}
				}
			} else {
				logger.log(Level.WARNING, "There is no agent : " + agentName
						+ ".");
			}
		} else {
			logger.log(Level.FINEST, "Idenfify responses are not handled here.");
		}
	}

	private void queryWritten(Tuple afterTuple) {
		String agentId = afterTuple.getField(2).getValue().toString();
		String agentName = afterTuple.getField(3).getValue().toString();
		if (!afterTuple.getField(4).getValue().toString().trim()
				.equals(AgentProtocol.MESSAGE_IDENTIFY)) {
			if (knownAgents.contains(agentId)) {
				List<Tuple> tupleDoc = agentInDoc.get(agentId);
				for (Tuple tuple : tupleDoc) {
					if (tuple.matches(afterTuple)) {
						logger.log(Level.FINE, "Query matches TupleDOC");
						break;
					}
				}
			} else {
				logger.log(Level.WARNING, "There is no agent : " + agentName);
			}
		} else {
			logger.log(Level.FINEST, "Idenfify queries are not handled here.");
		}
	}

	private void aliveDeleted(Tuple beforeTuple) {
		String agentId = beforeTuple.getField(2).getValue().toString();
		String agentName = beforeTuple.getField(3).getValue().toString();
		if (agentInstances.containsKey(agentName)) {
			Set<String> agentIds = agentInstances.get(agentName);
			boolean deleted = agentIds.remove(agentId);
			if (deleted) {
				logger.log(Level.FINE, "Agent " + agentName + " with id="
						+ agentId + " was deleted...");
				if (agentIds.isEmpty()) {
					knownAgents.remove(agentName);
					agentInDoc.remove(agentName);
					agentOutDoc.remove(agentName);
					logger.log(Level.FINE, "No more Instances of  " + agentName
							+ " available.");
				}
			} else {
				logger.log(Level.WARNING, "Agent " + agentName + " with id="
						+ agentId + " could not be deleted...");
			}
		} else {
			logger.log(Level.WARNING,
					"An agent disappeared, but wasn't registered here....");
		}
	}

	private void aliveUpdated(Tuple afterTuple) {
		aliveWritten(afterTuple);
	}

	private void aliveWritten(Tuple afterTuple) {
		String agentId = afterTuple.getField(2).getValue().toString();
		String agentName = afterTuple.getField(3).getValue().toString();
		if (!knownAgents.contains(agentName)) {
			VMID queryId = new VMID();
			final Tuple identifyQuery = AgentProtocol.getIdentifyTuple(agentId,
					agentName, queryId);
			String id = identifyQuery.getField(2).getValue().toString();
			try {
				getCommandSpace().write(identifyQuery);
				Tuple t = new Tuple(AgentProtocol.RESPONSE, queryId.toString(),
						id, agentName, Field.createWildCardField());
				Tuple identifyResponse = getCommandSpace().waitToTake(t,
						ident_timeout);
				if (identifyResponse == null) {
					logger.log(Level.WARNING, "QID: " + queryId.toString()
							+ "Agent " + agentName + " with ID=" + agentId
							+ " does not respond on identify query...");
				} else {
					ArrayList<String> tupleDocEntries = new ArrayList<String>();
					for (int i = 5; i < identifyResponse.getFields().length; i++) {
						String entry = identifyResponse.getField(i).getValue()
								.toString();
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
							e.printStackTrace();
							logger.log(Level.SEVERE, e.getMessage());
						}
					}
					agentInDoc.put(agentName, in);
					agentOutDoc.put(agentName, out);
					HashSet<String> al = new HashSet<String>();
					al.add(agentId);
					agentInstances.put(agentName, al);
					logger.log(Level.FINE, "New Agent(" + agentName
							+ ") registered!");
				}
			} catch (TupleSpaceException e) {
				e.printStackTrace();
				logger.log(Level.SEVERE, e.getMessage());
			}
		} else {
			if (agentInstances.containsKey(agentName)) {
				Set<String> agentIds = agentInstances.get(agentName);
				if (!agentIds.contains(agentId)) {
					agentIds.add(agentId);
					logger.log(Level.FINE, "New Instance of " + agentName
							+ " registered");
				} else {
					logger.log(Level.FINEST, "This instance of " + agentName
							+ " is already registered");
				}
			} else {
				logger.log(Level.SEVERE, "Known Agent without Instance found!");
			}
		}
	}

	@Override
	protected void doRun() throws TupleSpaceException, AgentLifecycleException,
			InterruptedException {
		while (status == Status.Running) {
			sendAliveUpdate();
			Thread.sleep(5000);
		}
	}

	@Override
	protected void doStop() {
		// deregister all callbacks
		status = Status.Stopping;
		for (Integer seq : cbMap.keySet()) {
			if (seq != 0) {
				try {
					getCommandSpace().eventDeRegister(seq);
				} catch (TupleSpaceException e) {
					e.printStackTrace();
					logger.log(Level.SEVERE, e.getMessage());
				}
			}
		}
		logger.log(Level.FINE, "Supervisor stopped");

	}

	private void initLogger() {
		ConsoleHandler cH = new ConsoleHandler();
		SimpleFormatter sF = new SimpleFormatter();
		cH.setFormatter(sF);
		cH.setLevel(DEBUGLEVEL);
		logger.setLevel(DEBUGLEVEL);
		logger.addHandler(cH);
	}

	@Override
	public boolean isStopped() {
		return isStopped;
	}

	public Set<String> getAllAgentIds(String agentName) {
		return agentInstances.get(agentName);

	}

	private Tuple convertStringToTuple(String tupleDoc, boolean intuple)
			throws ClassNotFoundException, IllegalArgumentException,
			InstantiationException, IllegalAccessException,
			InvocationTargetException, SecurityException, NoSuchMethodException {
		Tuple t = new Tuple();
		if (tupleDoc.contains("->")) {
			String s = tupleDoc.split("->")[intuple ? 0 : 1];
			Pattern p = Pattern.compile("(\"[^\"]+\"|<[^>]+>):\\w+");
			Matcher m = p.matcher(s);
			Class<?> c = null;
			while (m.find()) {
				String fieldName = m.group();
				if (fieldName.split(":")[1].toLowerCase().equals("xml")) {
					c = org.w3c.dom.Document.class;
				} else if (fieldName.split(":")[1].toLowerCase().equals(
						"binary")) {
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

		} else {
			logger.log(Level.WARNING, "The String \"" + tupleDoc
					+ "\" is no valid tupleDoc!");
		}
		return t;
	}

	@Override
	protected Tuple getIdentifyTuple(String queryId) {
		return null;
	}
}

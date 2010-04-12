package eu.scy.agents.keywords;

import java.rmi.dgc.VMID;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;
import eu.scy.agents.api.AgentLifecycleException;
import eu.scy.agents.impl.AbstractRequestAgent;
import eu.scy.agents.impl.AgentProtocol;

public class OntologyKeywordsAgent extends AbstractRequestAgent {

	private static final Logger logger = Logger.getLogger(OntologyKeywordsAgent.class.getName());

	public static final String EXTRACT_ONTOLOGY_KEYWORDS = "ExtractOntologyKeywords";

	private int listenerId;
	private Tuple activationTuple;

	public OntologyKeywordsAgent(Map<String, Object> params) {
		super(OntologyKeywordsAgent.class.getName(), params);
		if (params.containsKey(AgentProtocol.TS_HOST)) {
			host = (String) params.get(AgentProtocol.TS_HOST);
		}
		if (params.containsKey(AgentProtocol.TS_PORT)) {
			port = (Integer) params.get(AgentProtocol.TS_PORT);
		}
		activationTuple = new Tuple(EXTRACT_ONTOLOGY_KEYWORDS, AgentProtocol.QUERY, String.class, String.class);
		try {
			listenerId = getCommandSpace().eventRegister(Command.WRITE, activationTuple, this, true);
		} catch (TupleSpaceException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void call(Command command, int seq, Tuple afterTuple, Tuple beforeTuple) {
		if (listenerId != seq) {
			logger.debug("Callback passed to Superclass.");
			super.call(command, seq, afterTuple, beforeTuple);
			return;
		} else {
			String queryId = (String) afterTuple.getField(2).getValue();
			String text = (String) afterTuple.getField(3).getValue();

			Set<String> keywords = getKeywords(text);

			try {
				getCommandSpace().write(createResponseTuple(keywords, queryId));
			} catch (TupleSpaceException e) {
				e.printStackTrace();
			}
		}
	}

	private Tuple createResponseTuple(Set<String> keywords, String queryId) {
		StringBuffer keywordBuffer = new StringBuffer();
		for (String keyword : keywords) {
			keywordBuffer.append(keyword);
			keywordBuffer.append(";");
		}
		return new Tuple(EXTRACT_ONTOLOGY_KEYWORDS, AgentProtocol.RESPONSE, queryId, keywordBuffer.toString().trim());
	}

	private Set<String> getKeywords(String text) {
		Set<String> tokens = preprocessText(text);

		Set<String> ontologyKeywords = getOntologyKeywords();

		Set<String> result = merge(ontologyKeywords, tokens);

		return result;
	}

	private Set<String> merge(Set<String> ontologyKeywords, Set<String> tokens) {
		tokens.retainAll(ontologyKeywords);
		return tokens;
	}

	private Set<String> getOntologyKeywords() {
		Set<String> ontologyKeywords = new HashSet<String>();
		try {
			VMID queryId = new VMID();
			getCommandSpace()
					.write(new Tuple(queryId.toString(), "onto", "entities", "http://www.scy.eu/scyontology#"));

			Tuple response = getCommandSpace().waitToTake(new Tuple(queryId.toString(), String.class),
					AgentProtocol.ALIVE_INTERVAL * 3);
			if (response == null) {
				return ontologyKeywords;
			}
			String keywordString = (String) response.getField(3).getValue();
			StringTokenizer tokenizer = new StringTokenizer(keywordString, ",");

			while (tokenizer.hasMoreTokens()) {
				ontologyKeywords.add(tokenizer.nextToken());
			}
		} catch (TupleSpaceException e) {
			logger.warn("No ontology keywords found: " + e.getMessage());
		}
		return ontologyKeywords;
	}

	private Set<String> preprocessText(String text) {
		Set<String> tokens = new HashSet<String>();
		text = text.replaceAll("<.*?\\s*?.*?>", " ");
		text = text.replaceAll("[\\)\\(;,\\.\\?!:]", " ");
		text = text.replaceAll("\\s+", " ");
		StringTokenizer tokenizer = new StringTokenizer(text);
		while (tokenizer.hasMoreTokens()) {
			tokens.add(tokenizer.nextToken().toLowerCase());
		}
		return tokens;
	}

	@Override
	protected void doRun() throws TupleSpaceException, AgentLifecycleException, InterruptedException {
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
		try {
			getCommandSpace().eventDeRegister(listenerId);
			listenerId = -1;
		} catch (TupleSpaceException e) {
			throw new AgentLifecycleException("Could not deregister listener", e);
		}
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

}

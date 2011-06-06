package eu.scy.agents.keywords;

import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;

import java.rmi.dgc.VMID;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import eu.scy.agents.Mission;
import eu.scy.agents.api.AgentLifecycleException;
import eu.scy.agents.impl.AbstractRequestAgent;
import eu.scy.agents.impl.AgentProtocol;

public class OntologyKeywordsAgent extends AbstractRequestAgent {

	private static final Logger logger = Logger
			.getLogger(OntologyKeywordsAgent.class.getName());

	public static final String EXTRACT_ONTOLOGY_KEYWORDS = "ExtractOntologyKeywords";

	public static final String NAME = OntologyKeywordsAgent.class.getName();

	private int listenerId;

	private Tuple activationTuple;

	private Pattern p;

	public OntologyKeywordsAgent(Map<String, Object> params) {
		super(NAME, params);
		if (params.containsKey(AgentProtocol.TS_HOST)) {
			this.host = (String) params.get(AgentProtocol.TS_HOST);
		}
		if (params.containsKey(AgentProtocol.TS_PORT)) {
			this.port = (Integer) params.get(AgentProtocol.TS_PORT);
		}
		this.activationTuple = new Tuple(EXTRACT_ONTOLOGY_KEYWORDS,
				AgentProtocol.QUERY, String.class, String.class, String.class,
				String.class);
		try {
			this.listenerId = this.getCommandSpace().eventRegister(
					Command.WRITE, this.activationTuple, this, true);
		} catch (TupleSpaceException e) {
			e.printStackTrace();
		}
		this.p = Pattern.compile("\\p{Lu}");
	}

	@Override
	public void call(Command command, int seq, Tuple afterTuple,
			Tuple beforeTuple) {
		if (this.listenerId != seq) {
			logger.debug("Callback passed to Superclass.");
			super.call(command, seq, afterTuple, beforeTuple);
			return;
		} else {
			String queryId = (String) afterTuple.getField(2).getValue();
			String text = (String) afterTuple.getField(3).getValue();
			String missionString = (String) afterTuple.getField(4).getValue();
			Mission mission = Mission.getForName(missionString);
			String language = (String) afterTuple.getField(5).getValue();
			
			Set<String> keywords = this.getKeywords(text, mission, language);

			try {
				this.getCommandSpace().write(
						this.createResponseTuple(keywords, queryId));
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
		return new Tuple(EXTRACT_ONTOLOGY_KEYWORDS, AgentProtocol.RESPONSE,
				queryId, keywordBuffer.toString().trim());
	}

	private Set<String> getKeywords(String text, Mission mission, String language) {
		Set<String> tokens = this.preprocessText(text);

		Set<String> ontologyKeywords = this.getOntologyKeywords(mission, language);

		Set<String> result = this.merge(ontologyKeywords, tokens);

		return result;
	}

	private Set<String> merge(Set<String> ontologyKeywords, Set<String> tokens) {
		tokens.retainAll(ontologyKeywords);
		logger.info("Keywords that match on the ontology: " + tokens);
		return tokens;
	}

	private Set<String> getOntologyKeywords(Mission mission, String language) {
		Set<String> ontologyKeywords = new HashSet<String>();
		try {
			VMID queryId = new VMID();
			this.getCommandSpace().write(
					new Tuple(queryId.toString(), "onto", "labels",
							mission.getNamespace(), language));

			Tuple response = this.getCommandSpace().waitToTake(
					new Tuple(queryId.toString(), AgentProtocol.RESPONSE,
							String.class), AgentProtocol.ALIVE_INTERVAL * 3);
			if (response == null) {
				return ontologyKeywords;
			}
			String keywordString = (String) response.getField(2).getValue();
			StringTokenizer tokenizer = new StringTokenizer(keywordString, ",");

			while (tokenizer.hasMoreTokens()) {
				ontologyKeywords.add(this.unCamelize(tokenizer.nextToken()));
			}
			logger.info("found " + ontologyKeywords + " in the ontology");
		} catch (TupleSpaceException e) {
			logger.warn("No ontology keywords found: " + e.getMessage(), e);
		}
		return ontologyKeywords;
	}

	private String unCamelize(String nextToken) {
		Matcher m = this.p.matcher(nextToken);
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			m.appendReplacement(sb, " " + m.group());
		}
		m.appendTail(sb);
		return sb.toString().trim().toLowerCase();
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
	protected void doRun() throws TupleSpaceException, AgentLifecycleException,
			InterruptedException {
		while (this.status == Status.Running) {
			this.sendAliveUpdate();
			try {
				Thread.sleep(AgentProtocol.ALIVE_INTERVAL / 3);
			} catch (InterruptedException e) {
				throw new AgentLifecycleException(e.getMessage(), e);
			}
		}
	}

	@Override
	protected void doStop() throws AgentLifecycleException {
		try {
			this.getCommandSpace().eventDeRegister(this.listenerId);
			this.listenerId = -1;
		} catch (TupleSpaceException e) {
			throw new AgentLifecycleException("Could not deregister listener",
					e);
		}
		this.status = Status.Stopping;

	}

	@Override
	protected Tuple getIdentifyTuple(String queryId) {
		return null;
	}

	@Override
	public boolean isStopped() {
		return this.status == Status.Stopping;
	}

}

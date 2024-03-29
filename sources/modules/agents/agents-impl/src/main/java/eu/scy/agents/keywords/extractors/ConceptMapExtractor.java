package eu.scy.agents.keywords.extractors;

import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Field;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;

import java.rmi.dgc.VMID;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import roolo.elo.api.IELO;
import eu.scy.agents.Mission;
import eu.scy.agents.impl.AgentProtocol;
import eu.scy.agents.keywords.ExtractKeywordsAgent;
import eu.scy.agents.util.Utilities;

public class ConceptMapExtractor implements KeywordExtractor {

	private final static Logger logger = Logger
			.getLogger(KeywordExtractor.class);

	private TupleSpace tupleSpace;

    private String language;
	private Mission mission;

    public static String NODEPATH = "//nodes/eu.scy.scymapper.impl.model.NodeModel/label";
    public static String LINKPATH = "//links/eu.scy.scymapper.impl.model.NodeLinkModel/myLabel";

    public ConceptMapExtractor() {
	}

	@Override
	public List<String> getKeywords(IELO elo) {
		String text = getText(elo);
		if (!"".equals(text)) {
			return getKeywords(text);
		} else {
			return new ArrayList<String>();
		}
	}

	private String getText(IELO elo) {
		String text = Utilities.getEloText(elo, NODEPATH, logger);
		text = text + " " + Utilities.getEloText(elo, LINKPATH, logger);
		return text;
	}

	private List<String> getKeywords(String text) {
		try {
			String queryId = new VMID().toString();
			Tuple extractKeywordsTriggerTuple = new Tuple(
					ExtractKeywordsAgent.EXTRACT_KEYWORDS, AgentProtocol.QUERY,
					queryId, text, mission.getName(), language);
			extractKeywordsTriggerTuple.setExpiration(7200000);
			Tuple responseTuple = null;
			if (this.tupleSpace.isConnected()) {
				this.tupleSpace.write(extractKeywordsTriggerTuple);
				responseTuple = this.tupleSpace.waitToTake(new Tuple(
						ExtractKeywordsAgent.EXTRACT_KEYWORDS,
						AgentProtocol.RESPONSE, queryId, Field
								.createWildCardField()));
			}
			if (responseTuple != null) {
				ArrayList<String> keywords = new ArrayList<String>();
				for (int i = 3; i < responseTuple.getNumberOfFields(); i++) {
					String keyword = (String) responseTuple.getField(i)
							.getValue();
					keywords.add(keyword);
				}
				return keywords;
			}
		} catch (TupleSpaceException e) {
			e.printStackTrace();
		}
		return new ArrayList<String>();
	}

	@Override
	public TupleSpace getTupleSpace() {
		return tupleSpace;
	}

	@Override
	public void setTupleSpace(TupleSpace tupleSpace) {
		this.tupleSpace = tupleSpace;
	}

	@Override
	public void setMission(Mission mission) {
		this.mission = mission;
	}

    @Override
    public void setLanguage(String language) {
        this.language = language;
    }
}

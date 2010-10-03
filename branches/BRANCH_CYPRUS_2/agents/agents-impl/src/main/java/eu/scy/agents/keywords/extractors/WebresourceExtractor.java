package eu.scy.agents.keywords.extractors;

import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Field;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;

import java.rmi.dgc.VMID;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import roolo.elo.api.IContent;
import roolo.elo.api.IELO;
import eu.scy.agents.impl.AgentProtocol;
import eu.scy.agents.keywords.ExtractKeywordsAgent;

public class WebresourceExtractor implements KeywordExtractor {

	private final static Logger logger = Logger.getLogger(TextExtractor.class);

	private static final String ANNOTATIONS_START = "<annotations>";
	private static final String ANNOTATIONS_END = "</annotations>";

	private TupleSpace tupleSpace;

	@Override
	public List<String> getKeywords(IELO elo) {
		String text = getEloText(elo);
		if (!"".equals(text)) {
			return getKeywords(text);
		} else {
			return new ArrayList<String>();
		}
	}

	@Override
	public TupleSpace getTupleSpace() {
		return tupleSpace;
	}

	@Override
	public void setTupleSpace(TupleSpace tupleSpace) {
		this.tupleSpace = tupleSpace;
	}

	private String getEloText(IELO elo) {
		IContent content = elo.getContent();
		if (content == null) {
			logger.fatal("Content of elo is null");
			return "";
		}
		String text = content.getXml();
		text = text.substring(text.indexOf(ANNOTATIONS_START), text
				.lastIndexOf(ANNOTATIONS_END)
				+ ANNOTATIONS_END.length());
		logger.debug("Got text " + text);
		return text;
	}

	private List<String> getKeywords(String text) {
		try {
			String queryId = new VMID().toString();
			Tuple extractKeywordsTriggerTuple = new Tuple(
					ExtractKeywordsAgent.EXTRACT_KEYWORDS, AgentProtocol.QUERY,
					queryId, text);
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

}

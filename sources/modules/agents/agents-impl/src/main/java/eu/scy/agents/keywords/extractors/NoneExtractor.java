package eu.scy.agents.keywords.extractors;

import info.collide.sqlspaces.client.TupleSpace;

import java.util.ArrayList;
import java.util.List;

import roolo.elo.api.IELO;
import eu.scy.agents.Mission;

/**
 * Always returns an empty list of keywords. It is used as default extractor
 * when the {@link KeywordExtractorFactory} doesn't know how to handle a
 * specific elo type.
 * 
 * @author fschulz
 * 
 */
public class NoneExtractor implements KeywordExtractor {

	@Override
	public List<String> getKeywords(IELO elo) {
		return new ArrayList<String>();
	}

	@Override
	public TupleSpace getTupleSpace() {
		return null;
	}

	@Override
	public void setTupleSpace(TupleSpace tupleSpace) {
	}

	@Override
	public void setMission(Mission mission) {
	}
}

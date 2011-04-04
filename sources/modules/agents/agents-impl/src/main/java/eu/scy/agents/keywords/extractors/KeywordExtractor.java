package eu.scy.agents.keywords.extractors;

import info.collide.sqlspaces.client.TupleSpace;

import java.util.List;

import eu.scy.agents.Mission;

import roolo.elo.api.IELO;

/**
 * Abstract interface to extract keywords from an ELO. Subclasses should handle
 * a specific ELO type.
 * 
 * @author fschulz
 * 
 */
public interface KeywordExtractor {

	/**
	 * Extract keywords from an elo.
	 * 
	 * @param elo
	 *            The elo from which the keywords should be extracted. May not
	 *            be null.
	 * @return A list of keywords if there were some found or a empty list.
	 */
	public List<String> getKeywords(IELO elo);

	public TupleSpace getTupleSpace();

	public void setTupleSpace(TupleSpace tupleSpace);

	public void setMission(Mission mission);

}

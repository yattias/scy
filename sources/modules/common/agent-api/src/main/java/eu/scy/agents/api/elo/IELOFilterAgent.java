package eu.scy.agents.api.elo;

import eu.scy.agents.api.IAgent;
import roolo.elo.api.IELO;
import roolo.elo.api.IMetadataKey;

/**
 * An interface to provide common functionality for a filtering agent that
 * processes ELOs. These type of agents are registered to the
 * {@link IELOAgentDispatcher} and are activated on whenever an ELO is saved,
 * updated or retrieved. They can either edit the metadata of the processed ELO
 * or notify another agent about its result. They should not be calculation
 * intensive as it would slow down the whole roolo.
 * 
 * @author Florian Schulz
 * 
 * @param <T>
 * @param <K>
 */
public abstract interface IELOFilterAgent<T extends IELO<K>, K extends IMetadataKey>
		extends IAgent {

	/**
	 * Processes an elo. It either edits the metadata or sends a notification to
	 * another agent. Depending on the type of filtering agent (before, after,
	 * notification) certain metadata is not present in the ELO.
	 * 
	 * @param elo
	 *            The ELO to process.
	 */
	public void processElo(T elo);

}

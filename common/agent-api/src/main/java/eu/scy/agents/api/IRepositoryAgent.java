package eu.scy.agents.api;

import roolo.api.IRepository;
import roolo.elo.api.IMetadataTypeManager;

/**
 * Marker interface to flag that an agent needs access to the RoOLo repository.
 * It is the job of the programmer to make sure, that the agent which implements
 * this interface does not get a reference to a {@link IELOAgentDispatcher}.
 * This could leed to a infinity loop of calling this agent.
 * 
 * @author Stefan Weinbrenner
 * 
 */
public interface IRepositoryAgent {

	/**
	 * Set the needed reference to the repository.
	 * 
	 * @param rep
	 *            The repository reference.
	 */
	public void setRepository(IRepository rep);

	/**
	 * Set the {@link IMetadataTypeManager} needed additionally to the
	 * repository.
	 * 
	 * @param manager
	 *            The IMetadataTypeManager reference.
	 */
	public void setMetadataTypeManager(IMetadataTypeManager manager);

}

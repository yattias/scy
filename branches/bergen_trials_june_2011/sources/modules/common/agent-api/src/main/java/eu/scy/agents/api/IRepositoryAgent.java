package eu.scy.agents.api;

import roolo.api.IRepository;
import roolo.elo.api.IMetadataTypeManager;

/**
 * Marker interface to flag that an agent needs access to the RoOLo repository.
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

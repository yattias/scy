package eu.scy.agents;

import java.rmi.dgc.VMID;

import roolo.api.IRepository;
import roolo.elo.api.IMetadataTypeManager;
import eu.scy.agents.api.IThreadedAgent;

/**
 * This class provides a facility to manage the agents inside SCY.<br>
 * It is used to start/stop/kill single or multiple agents of the same or different kind.<br>
 * <br>
 * There are some conventions regarding the agents which can be managed using this manager: - All
 * manageable agents must be an implementation of {@link IThreadedAgent}.<br>
 * <br>
 * - All manageable agents must implement a constructor like
 * {@code MyAgent(Map<String, Object> map)} This maps is usually used to set some properties. If you
 * do not need properties, you can ignore the map exept for the entry <i>"id"</i> which contains the
 * uniqueID ({@link VMID} as String) of the single instances.<br>
 * - The whole inter-agent-communication is handled using the SQLSpaces
 * (http://weinbrenner.collide.info/sqlspaces-site/) and follows following convention:<br/>
 * {@code
 * ("query"|"response"|"agentCommand"), queryID [String], agentID [String], agentName [String], serviceName [String],
 * ....(Payload)}
 * 
 * @author Florian Schulz
 * @author Jan Engler
 * @author Stefan Weinbrenner
 */
public class SCYAgentManager extends AgentManager {

    private IRepository repository;

    private IMetadataTypeManager manager;

    /**
     * This constructor is used to start an agent manager. Please make sure that only one instance
     * is running on one SQLSpace. We intentionally used no Singleton Pattern or Lazy creation to
     * provide the possibility to have several managers running parallel, but you have to know what
     * you are doing.
     * 
     * @param host
     *            The host of the SQLSpaces server (e.g. localhost)
     * @param port
     *            The port of the SQLSpaces server (e.g. 2525)
     */
    public SCYAgentManager(String host, int port) {
        super(host, port);
    }

    @Override
    protected void doInjections(IThreadedAgent agent) {
        // For instance a IRepositoryAgent needs references to a
        // repository and a MetaDataTypeManager
        if (agent instanceof IRepositoryAgent) {
            IRepositoryAgent ira = (IRepositoryAgent) agent;
            ira.setMetadataTypeManager(manager);
            ira.setRepository(repository);
        }
    }

    /**
     * Set the repository.
     * 
     * @param repository
     */
    public void setRepository(IRepository repository) {
        this.repository = repository;
    }

    /**
     * Set the metadatatypemanager.
     * 
     * @param manager
     */
    public void setMetadataTypeManager(IMetadataTypeManager manager) {
        this.manager = manager;
    }

}

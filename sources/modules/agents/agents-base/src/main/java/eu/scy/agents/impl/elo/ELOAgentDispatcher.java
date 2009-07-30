package eu.scy.agents.impl.elo;

import java.net.URI;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import roolo.api.IRepository;
import roolo.api.search.IQuery;
import roolo.api.search.ISearchResult;
import roolo.elo.api.IELO;
import roolo.elo.api.IMetadata;
import eu.scy.agents.api.elo.IELOAgentDispatcher;
import eu.scy.agents.api.elo.IELOFilterAgent;

/**
 * The basic implementation of the RooloAgentDispatcher. Following calls are
 * intercepted:
 * <ul>
 * <li>
 * {@link IRepository#addELO(IELO)} - the before agents are called before the
 * save happens. Every other filtering agent type is called after the actual
 * save.</li>
 * <li>
 * {@link IRepository#retrieveELO(URI)} - the after agents are called before the
 * retrieved ELO is handed back to the client.</li>
 * <li>
 * {@link IRepository#updateELO(IELO)} - see addElo(IELO).</li>
 * <li>
 * every other call is redirected to the real repository.</li>
 * </ul>
 * 
 * @author Florian Schulz
 * 
 */
public class ELOAgentDispatcher implements IELOAgentDispatcher {

	private IRepository repository;

	private Queue<IELOFilterAgent> beforeQueue;
	private Queue<IELOFilterAgent> afterQueue;
	private List<IELOFilterAgent> notificationAgents;

	/**
	 * Create a new EloAgentDispatcher.
	 */
	public ELOAgentDispatcher() {
		beforeQueue = new LinkedList<IELOFilterAgent>();
		afterQueue = new LinkedList<IELOFilterAgent>();
		notificationAgents = new LinkedList<IELOFilterAgent>();
	}

	@Override
	public IMetadata addNewELO(IELO elo) {
		processBefore(elo);

		IMetadata metadata = repository.addNewELO(elo);

		notifyAgents(elo);

		processAfter(elo);
		return metadata;
	}

	private void notifyAgents(final IELO elo) {
		for (final IELOFilterAgent agent : notificationAgents) {
			Thread t = new Thread(new Runnable() {

				@Override
				public void run() {
					agent.processElo(elo);

				}
			});
			t.start();
		}
	}

	@Override
	public void archiveELO(URI id) {
		repository.archiveELO(id);
	}

	@Override
	public void deleteELO(URI id) {
		repository.deleteELO(id);
	}

	@Override
	public IELO retrieveELO(URI id) {
		IELO elo = repository.retrieveELO(id);
		processAfter(elo);
		return elo;
	}

	@Override
	public void addMetadata(URI id, IMetadata metadata) {
		repository.addMetadata(id, metadata);
	}

	@Override
	public IMetadata retrieveMetadata(URI id) {
		return repository.retrieveMetadata(id);
	}

	@Override
	public List<ISearchResult> search(IQuery query) {
		return repository.search(query);
	}

	@Override
	public void unarchiveELO(URI id) {
		repository.unarchiveELO(id);
	}

	@Override
	public IMetadata updateELO(IELO elo) {
		processBefore(elo);

		IMetadata metadata = repository.updateELO(elo);

		notifyAgents(elo);

		processAfter(elo);
		return metadata;
	}

	private void processAfter(IELO elo) {
		System.out.println("starting after save processing");
		for (IELOFilterAgent agent : afterQueue) {
			agent.processElo(elo);
		}
		System.out.println("ended after save processing");
	}

	private void processBefore(IELO elo) {
		System.out.println("starting before save processing");
		for (IELOFilterAgent agent : beforeQueue) {
			agent.processElo(elo);
		}
		System.out.println("ended before save processing");
	}

	/**
	 * Set the real repository the calls are redirected to.
	 * 
	 * @param repo
	 *            The repository the calls are redirected to.
	 */
	public void setRepository(IRepository repo) {
		this.repository = repo;
	}

	/**
	 * Get the repository the calls are redirected to.
	 * 
	 * @return the real repository.
	 */
	public IRepository getRepository() {
		return repository;
	}

	@Override
	public void setBeforeAgents(List<IELOFilterAgent> agents) {
		beforeQueue.addAll(agents);
	}

	@Override
	public void setAfterAgents(List<IELOFilterAgent> agents) {
		afterQueue.addAll(agents);
	}

	@Override
	public void setNotificationAgents(List<IELOFilterAgent> agents) {
		notificationAgents.addAll(agents);
	}

	@Override
	public void addAfterAgent(IELOFilterAgent agent) {
		afterQueue.add(agent);
	}

	@Override
	public void addBeforeAgent(IELOFilterAgent agent) {
		beforeQueue.add(agent);
	}

	@Override
	public void addNotificationAgent(IELOFilterAgent agent) {
		notificationAgents.add(agent);
	}

	@Override
	public IMetadata addForkedELO(IELO elo) {
		// TODO Need agents to run on this elo.
		return repository.addForkedELO(elo);
	}

	@Override
	public IELO retrieveFirstVersionELO(URI uri) {
		// TODO agents.
		return repository.retrieveFirstVersionELO(uri);
	}

	@Override
	public IELO retrieveLastVersionELO(URI uri) {
		// TODO Agents?
		return repository.retrieveLastVersionELO(uri);
	}

	@Override
	public IMetadata retrieveMetadataFirstVersion(URI uri) {
		return repository.retrieveMetadataFirstVersion(uri);
	}

	@Override
	public IMetadata retrieveMetadataLastVersion(URI uri) {
		// TODO Agents
		return repository.retrieveMetadataLastVersion(uri);
	}

}

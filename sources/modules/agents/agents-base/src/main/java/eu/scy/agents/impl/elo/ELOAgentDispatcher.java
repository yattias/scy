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
import roolo.elo.api.IMetadataKey;
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
 * everyother call is redirected to the real repository.</li>
 * </ul>
 * 
 * @author Florian Schulz
 * 
 * @param <T>
 * @param <K>
 */
public class ELOAgentDispatcher<T extends IELO<K>, K extends IMetadataKey>
		implements IELOAgentDispatcher<T, K> {

	private IRepository<T, K> repository;

	private Queue<IELOFilterAgent<T, K>> beforeQueue;
	private Queue<IELOFilterAgent<T, K>> afterQueue;
	private List<IELOFilterAgent<T, K>> notificationAgents;

	/**
	 * Create a new EloAgentDispatcher.
	 */
	public ELOAgentDispatcher() {
		beforeQueue = new LinkedList<IELOFilterAgent<T, K>>();
		afterQueue = new LinkedList<IELOFilterAgent<T, K>>();
		notificationAgents = new LinkedList<IELOFilterAgent<T, K>>();
	}

	@Override
	public IMetadata<K> addELO(T elo) {
		processBefore(elo);

		IMetadata<K> metadata = repository.addELO(elo);

		notifyAgents(elo);

		processAfter(elo);
		return metadata;
	}

	private void notifyAgents(final T elo) {
		for (final IELOFilterAgent<T, K> agent : notificationAgents) {
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
	public T retrieveELO(URI id) {
		T elo = repository.retrieveELO(id);
		processAfter(elo);
		return elo;
	}

	@Override
	public void addMetadata(URI id, IMetadata<K> metadata) {
		repository.addMetadata(id, metadata);
	}

	@Override
	public IMetadata<K> retrieveMetadata(URI id) {
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
	public IMetadata<K> updateELO(T elo) {
		processBefore(elo);

		IMetadata<K> metadata = repository.updateELO(elo);

		notifyAgents(elo);

		processAfter(elo);
		return metadata;
	}

	private void processAfter(T elo) {
		System.out.println("starting after save processing");
		for (IELOFilterAgent<T, K> agent : afterQueue) {
			agent.processElo(elo);
		}
		System.out.println("ended after save processing");
	}

	private void processBefore(T elo) {
		System.out.println("starting before save processing");
		for (IELOFilterAgent<T, K> agent : beforeQueue) {
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
	public void setRepository(IRepository<T, K> repo) {
		this.repository = repo;
	}

	/**
	 * Get the repository the calls are redirected to.
	 * 
	 * @return the real repository.
	 */
	public IRepository<T, K> getRepository() {
		return repository;
	}

	@Override
	public void setBeforeAgents(List<IELOFilterAgent<T, K>> agents) {
		beforeQueue.addAll(agents);
	}

	@Override
	public void setAfterAgents(List<IELOFilterAgent<T, K>> agents) {
		afterQueue.addAll(agents);
	}

	@Override
	public void setNotificationAgents(List<IELOFilterAgent<T, K>> agents) {
		notificationAgents.addAll(agents);
	}

	@Override
	public void addAfterAgent(IELOFilterAgent<T, K> agent) {
		afterQueue.add(agent);
	}

	@Override
	public void addBeforeAgent(IELOFilterAgent<T, K> agent) {
		beforeQueue.add(agent);
	}

	@Override
	public void addNotificationAgent(IELOFilterAgent<T, K> agent) {
		notificationAgents.add(agent);
	}

}

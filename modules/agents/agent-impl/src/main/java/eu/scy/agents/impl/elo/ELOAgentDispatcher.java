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

public class ELOAgentDispatcher<T extends IELO<K>, K extends IMetadataKey>
		implements IELOAgentDispatcher<T, K> {

	private IRepository<T, K> repository;

	private Queue<IELOFilterAgent<T, K>> beforeQueue;
	private Queue<IELOFilterAgent<T, K>> afterQueue;
	private List<IELOFilterAgent<T, K>> notificationAgents;

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.scy.agents.roolo.dispatcher.proposal.IRooloAgentDispatcher#setRepository
	 * (roolo.api.IRepository)
	 */
	public void setRepository(IRepository<T, K> repository) {
		this.repository = repository;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.scy.agents.roolo.dispatcher.proposal.IRooloAgentDispatcher#getRepository
	 * ()
	 */
	public IRepository<T, K> getRepository() {
		return repository;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.scy.agents.roolo.dispatcher.proposal.IRooloAgentDispatcher#setBeforeAgents
	 * (java.util.List)
	 */
	public void setBeforeAgents(List<IELOFilterAgent<T, K>> agents) {
		beforeQueue.addAll(agents);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.scy.agents.roolo.dispatcher.proposal.IRooloAgentDispatcher#setAfterAgents
	 * (java.util.List)
	 */
	public void setAfterAgents(List<IELOFilterAgent<T, K>> agents) {
		afterQueue.addAll(agents);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeeu.scy.agents.roolo.dispatcher.proposal.IRooloAgentDispatcher#
	 * setNotificationAgents(java.util.List)
	 */
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

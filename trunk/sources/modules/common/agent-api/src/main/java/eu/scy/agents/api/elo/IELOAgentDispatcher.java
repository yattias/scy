package eu.scy.agents.api.elo;

import java.util.List;

import roolo.api.IRepository;
import roolo.elo.api.IELO;
import roolo.elo.api.IMetadataKey;

public interface IELOAgentDispatcher<T extends IELO<K>, K extends IMetadataKey>
		extends IRepository<T, K> {

	void setRepository(IRepository<T, K> repository);

	IRepository<T, K> getRepository();

	void setBeforeAgents(List<IELOFilterAgent<T, K>> agents);

	void addBeforeAgent(IELOFilterAgent<T, K> agent);

	void setAfterAgents(List<IELOFilterAgent<T, K>> agents);

	void addAfterAgent(IELOFilterAgent<T, K> agent);

	void setNotificationAgents(List<IELOFilterAgent<T, K>> agents);

	void addNotificationAgent(IELOFilterAgent<T, K> agent);

}
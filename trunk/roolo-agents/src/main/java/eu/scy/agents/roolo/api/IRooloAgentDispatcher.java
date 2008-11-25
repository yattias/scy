package eu.scy.agents.roolo.api;

import java.util.List;



import roolo.api.IRepository;
import roolo.elo.api.IELO;
import roolo.elo.api.IMetadataKey;

public interface IRooloAgentDispatcher<T extends IELO<K>, K extends IMetadataKey> extends IRepository<T, K> {
    
    void setRepository(IRepository<T, K> repository);
    
    IRepository<T, K> getRepository();
    
    void setBeforeAgents(List<IRooloAgent<T, K>> agents);
    
    void addBeforeAgent(IRooloAgent<T, K> agent);
    
    void setAfterAgents(List<IRooloAgent<T, K>> agents);
    
    void addAfterAgent(IRooloAgent<T, K> agent);
    
    void setNotificationAgents(List<IRooloAgent<T, K>> agents);
    
    void addNotificationAgent(IRooloAgent<T, K> agent);
    
}
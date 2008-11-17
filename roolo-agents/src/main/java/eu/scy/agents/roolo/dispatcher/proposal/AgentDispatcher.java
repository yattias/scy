package eu.scy.agents.roolo.dispatcher.proposal;

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

public class AgentDispatcher<T extends IELO<K>, K extends IMetadataKey> implements IRepository<T, K> {
    
    private IRepository<T, K> repository;
    
    private Queue<IRooloAgent<T, K>> beforeQueue;
    private Queue<IRooloAgent<T, K>> afterQueue;
    private List<IRooloAgent<T, K>> notificationAgents;
    
    public AgentDispatcher() {
        beforeQueue = new LinkedList<IRooloAgent<T, K>>();
        afterQueue = new LinkedList<IRooloAgent<T, K>>();
        notificationAgents = new LinkedList<IRooloAgent<T, K>>();
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
        for (final IRooloAgent<T, K> agent : notificationAgents) {
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
        return repository.retrieveELO(id);
    }
    
    @Override
    public T retrieveFirst(URI id) {
        return repository.retrieveFirst(id);
    }
    
    @Override
    public IMetadata<K> retrieveMetadata(URI id) {
        return repository.retrieveMetadata(id);
    }
    
    @Override
    public List<T> retrieveVersions(List<Float> versions, URI id) {
        return repository.retrieveVersions(versions, id);
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
        
        IMetadata<K> metadata = repository.addELO(elo);
        
        notifyAgents(elo);
        
        processAfter(elo);
        return metadata;
    }
    
    private void processAfter(T elo) {
        System.out.println("starting after save processing");
        for (IRooloAgent<T, K> agent : afterQueue) {
            agent.processElo(elo);
        }
        System.out.println("ended after save processing");
    }
    
    private void processBefore(T elo) {
        System.out.println("starting before save processing");
        for (IRooloAgent<T, K> agent : beforeQueue) {
            agent.processElo(elo);
        }
        System.out.println("ended before save processing");
    }
    
    @Override
    public void updateMetadata(URI id, IMetadata<K> metadata) {
        repository.updateMetadata(id, metadata);
    }
    
    public void setRepository(IRepository<T, K> repository) {
        this.repository = repository;
    }
    
    public IRepository<T, K> getRepository() {
        return repository;
    }
    
    public void setBeforeAgents(List<IRooloAgent<T, K>> agents) {
        beforeQueue.addAll(agents);
    }
    
    public void setAfterAgents(List<IRooloAgent<T, K>> agents) {
        afterQueue.addAll(agents);
    }
    
    public void setNotificationAgents(List<IRooloAgent<T, K>> agents) {
        notificationAgents.addAll(agents);
    }
}

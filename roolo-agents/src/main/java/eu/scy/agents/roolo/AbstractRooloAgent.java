package eu.scy.agents.roolo;

import java.net.URI;
import java.util.List;

import roolo.api.IRepository;
import roolo.api.search.IQuery;
import roolo.api.search.ISearchResult;
import roolo.elo.api.IELO;
import roolo.elo.api.IMetadata;
import roolo.elo.api.IMetadataKey;

public class AbstractRooloAgent<T extends IELO<K>, K extends IMetadataKey> implements
IRepository<T, K>
{
    private IRepository<T, K> repository;
    
    //@Override
    public IMetadata<K> addELO(T elo)
    {
        return repository.addELO(elo);
    }
    
    //@Override
    public void archiveELO(URI id)
    {
        repository.archiveELO(id);
    }
    
    //@Override
    public void deleteELO(URI id)
    {
        repository.deleteELO(id);
    }
    
    //@Override
    public T retrieveELO(URI id)
    {
        return repository.retrieveELO(id);
    }
    
    //@Override
    public T retrieveFirst(URI id)
    {
        return repository.retrieveFirst(id);
    }
    
    //@Override
    public List<T> retrieveVersions(List<Float> versions, URI id)
    {
        return repository.retrieveVersions(versions, id);
    }
    
    //@Override
    public IMetadata<K> retrieveMetadata(URI id)
    {
        return repository.retrieveMetadata(id);
    }
    
    //@Override
    public List<ISearchResult> search(IQuery query)
    {
        return repository.search(query);
    }
    
    //@Override
    public void unarchiveELO(URI id)
    {
        repository.unarchiveELO(id);
    }
    
    //@Override
    public IMetadata<K> updateELO(T elo)
    {
        return repository.updateELO(elo);
    }
    
    //@Override
    public void updateMetadata(URI id, IMetadata<K> metadata)
    {
        repository.updateMetadata(id, metadata);
    }
    
    public void setRepository(IRepository<T, K> repository)
    {
        this.repository = repository;
    }
    
    public IRepository<T, K> getRepository()
    {
        return repository;
    }
    
}

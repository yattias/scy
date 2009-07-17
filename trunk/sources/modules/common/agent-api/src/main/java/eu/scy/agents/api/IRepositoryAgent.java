package eu.scy.agents.api;

import roolo.api.IRepository;
import roolo.elo.api.IELO;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.IMetadataTypeManager;

public interface IRepositoryAgent {

    public void setRepository(IRepository<IELO<IMetadataKey>, IMetadataKey> rep);

    public void setMetadataTypeManager(IMetadataTypeManager<IMetadataKey> manager);

}

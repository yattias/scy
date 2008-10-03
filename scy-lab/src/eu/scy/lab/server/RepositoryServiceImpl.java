package eu.scy.lab.server;

import org.springframework.beans.factory.InitializingBean;

import roolo.api.IELO;
import roolo.api.IMetadataKey;
import roolo.api.IRepository;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import eu.scy.lab.client.repository.RepositoryService;

public class RepositoryServiceImpl extends RemoteServiceServlet implements RepositoryService, InitializingBean {
    
    private IRepository<IELO<IMetadataKey>, IMetadataKey> repository;
    
    public String addELO(String elo) {
        // TODO Auto-generated method stub
        return null;
    }
    
    public String getELO(String id) {
        // TODO Auto-generated method stub
        return null;
    }
    
    public String searchELO(String search) {
        // TODO Auto-generated method stub
        return null;
    }
    
    /**
     * Here Spring injects the configured IRepository implementation.
     * 
     * @param repository
     */
    public void setMockRepository(IRepository<IELO<IMetadataKey>, IMetadataKey> repository) {
        this.repository = repository;
    }
    
    public void afterPropertiesSet() throws Exception {
        System.out.println("****************");
        System.out.println("Repository Service Impl afterPropertiesSet");
        System.out.println("Repository: " + repository.toString());
        System.out.println("****************");
    }
    
}

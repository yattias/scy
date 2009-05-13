/**
 * 
 */
package eu.scy.lab.client.repository;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;


/**
 * @author Giemza
 */
@RemoteServiceRelativePath("repositoryService")
public interface RepositoryService extends RemoteService {
    
    public static class Util {
        
        public static RepositoryServiceAsync getInstance() {
            
            return GWT.create(RepositoryService.class);
        }
    }
    
    public String[] addELO(String elo);
    
    public String[] getELO(String id);
    
    public String[][] searchELO(String search);
    
}

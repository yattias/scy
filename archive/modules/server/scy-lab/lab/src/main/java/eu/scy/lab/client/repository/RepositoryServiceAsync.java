/**
 * 
 */
package eu.scy.lab.client.repository;

import com.google.gwt.user.client.rpc.AsyncCallback;


/**
 * @author Giemza
 */
public interface RepositoryServiceAsync {
    
    public void addELO(String elo, AsyncCallback<String[]> callback);
    
    public void getELO(String id, AsyncCallback<String[]> callback);
    
    public void searchELO(String search, AsyncCallback<String[][]> callback);
    
}

/**
 * 
 */
package eu.scy.lab.client;

import com.google.gwt.user.client.rpc.AsyncCallback;


/**
 * @author Giemza
 *
 */
public interface UserManagementAsync {
    
    public void login(String username, String password, AsyncCallback<Boolean> callback);
    
    public void register(String username, String password, String title, String firstname, String lastname, String birthdate, String email, AsyncCallback<Boolean> callback);
    
    public void getBuddies(String username, AsyncCallback<String[][]> callback);
}

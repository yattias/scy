/**
 * 
 */
package eu.scy.lab.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;


/**
 * @author Giemza
 *
 */
@RemoteServiceRelativePath("usermanagement")
public interface UserManagement extends RemoteService {
    
    public static class Util {
        
        public static UserManagementAsync getInstance() {
            
            return GWT.create(UserManagement.class);
        }
    }
    
    public boolean login(String username, String password);
    
    public boolean register(String username, String password, String title, String firstname, String lastname, String birthdate, String email);
    
    public String[][] getBuddies(String username);
}

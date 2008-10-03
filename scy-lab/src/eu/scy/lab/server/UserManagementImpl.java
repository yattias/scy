package eu.scy.lab.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import eu.scy.lab.client.UserManagement;

public class UserManagementImpl extends RemoteServiceServlet implements UserManagement {
    
    // Inject UserDAOHibernate
    
    public boolean login(String username, String password) {
        return UserDB.authenticateUser(username, password);
    }
    
    public boolean register(String username, String password, String title, String firstname, String lastname, String birthdate, String email) {
        return UserDB.addUser(birthdate, email, firstname, lastname, password, title, username);
    }
    
    
}

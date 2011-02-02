package eu.scy.lab.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import eu.scy.lab.client.UserManagement;

public class UserManagementImpl extends RemoteServiceServlet implements UserManagement {
    
    private static int Counter = 0;
    
    private static String[][] users = new String[][] { { "Stefan" }, { "Adam" }, { "Lars" }, { "Henrik" } };
    
    public boolean login(String username, String password) {
        return UserDB.authenticateUser(username, password);
    }
    
    public boolean register(String username, String password, String title, String firstname, String lastname, String birthdate, String email) {
        return UserDB.addUser(birthdate, email, firstname, lastname, password, title, username);
    }
    
    public String[][] getBuddies(String username) {
        if ((Counter >= 0) && (Counter <= 3)) {
            Counter++;
        }
        if (Counter == 4) {
            Counter = 0;
        }
        String[][] result = new String[Counter][];
        for (int i = 0; i < Counter; i++) {
            result[i] = users[i];
        }
        return result;
    }
}

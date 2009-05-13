/**
 * 
 */
package eu.scy.lab.client.usermanagement;


/**
 * @author Giemza
 *
 */
public class User {
    
    public static User instance;
    
    private String username;
    
    private User() {}
    
    public static User getInstance() {
        if (instance == null) {
            instance = new User();
        }
        return instance;
    }
    
    /**
     * @param username
     *            the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }
    
    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }
}

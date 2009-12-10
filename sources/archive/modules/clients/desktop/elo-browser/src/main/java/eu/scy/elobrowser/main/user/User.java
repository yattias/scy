package eu.scy.elobrowser.main.user;

/**
 *  Singleton class for storing information about the user that is currently logged in.
 *
 *  Note that enum is the only real singleton in Java --> ask Josh Bloch ;)
 *
 * @author weinbrenner
 */
public enum User {

    instance;
    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

package eu.scy.core.model;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 16.okt.2008
 * Time: 06:12:04
 * A session is a unit of work from a user first logs in to the system until the user logs out or is logged out
 */
public interface UserSession extends ScyBase{

    public long getSessionStarted();

    public void setSessionStarted(long sessionStarted);

    public User getUser();

    public void setUser(User user);

    public long getSessionEnded();

    public void setSessionEnded(long sessionEnded);

    public String getSessionId();

    public void setSessionId(String sessionId);

    /**
     * Retuns true if the session is active now. This means that the user is actively logged in and using the system
     * @return
     */
    public Boolean getSessionActive();

    public void setSessionActive(Boolean sessionActive);
}

package eu.scy.core.model;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 16.okt.2008
 * Time: 06:12:04
 * A session is a unit of work from a user first logs in to the system until the user logs out or is logged out
 */
public interface UserSession extends ScyBase{
    long getSessionStarted();

    void setSessionStarted(long sessionStarted);

    User getUser();

    void setUser(User user);

    long getSessionEnded();

    void setSessionEnded(long sessionEnded);

    String getSessionId();

    void setSessionId(String sessionId);
}

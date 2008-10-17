package eu.scy.core.model;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 16.okt.2008
 * Time: 06:12:04
 * To change this template use File | Settings | File Templates.
 */
public interface UserSession extends ScyBase{
    long getSessionStarted();

    void setSessionStarted(long sessionStarted);

    User getUser();

    void setUser(User user);
}

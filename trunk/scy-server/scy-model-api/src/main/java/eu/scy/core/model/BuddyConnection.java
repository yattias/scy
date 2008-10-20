package eu.scy.core.model;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 16.okt.2008
 * Time: 06:26:30
 */
public interface BuddyConnection extends ScyBase{

    User getMyself();

    void setMyself(User myself);

    User getBuddy();

    void setBuddy(User buddy);
}

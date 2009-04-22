package eu.scy.core.model;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 16.okt.2008
 * Time: 06:26:30
 * A many to many connection between users
 */
public interface BuddyConnection extends ScyBase{

    public User getMyself();

    public void setMyself(User myself);

    public User getBuddy();

    public void setBuddy(User buddy);
}

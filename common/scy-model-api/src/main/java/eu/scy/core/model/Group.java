package eu.scy.core.model;

import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 16.okt.2008
 * Time: 06:00:03
 * A group in the SCY system. Groups can can contain users
 */
public interface Group extends ScyBase{

    void addMember(User member);

    Set<User> getMembers();

    void setMembers(Set<User> members);
}
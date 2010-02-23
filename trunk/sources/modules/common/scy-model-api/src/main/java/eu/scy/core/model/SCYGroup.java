package eu.scy.core.model;

import java.util.List;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 16.okt.2008
 * Time: 06:00:03
 * A group in the SCY system. Groups can be arranged hierarchically within projects and can contain users
 */
public interface SCYGroup extends ScyBase{


    void addMember(User member);

    Set<User> getMembers();

    void setMembers(Set<User> members);

    SCYGroup getParent();

    void setParent(SCYGroup parent);
}

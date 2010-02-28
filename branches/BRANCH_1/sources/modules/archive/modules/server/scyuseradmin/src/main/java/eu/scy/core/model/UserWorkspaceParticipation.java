package eu.scy.core.model;

import javax.persistence.*;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 03.sep.2008
 * Time: 14:15:40
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "userWorkspaceParticipation")
public class UserWorkspaceParticipation extends SCYBaseObject{



    private User user;
    private Workspace workspace;

    @ManyToOne(targetEntity = User.class, cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    @JoinColumn (name = "user_primKey")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @ManyToOne(targetEntity = Workspace.class, cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    @JoinColumn (name = "user_primKey")
    public Workspace getWorkspace() {
        return workspace;
    }

    public void setWorkspace(Workspace workspace) {
        this.workspace = workspace;
    }
}

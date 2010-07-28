package eu.scy.core.model.impl.runtime;

import eu.scy.core.model.User;
import eu.scy.core.model.impl.SCYUserImpl;
import eu.scy.core.model.impl.pedagogicalplan.BaseObjectImpl;
import eu.scy.core.model.runtime.AbstractRuntimeAction;

import javax.persistence.*;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 22.apr.2010
 * Time: 06:34:02
 * To change this template use File | Settings | File Templates.
 */

@Entity
@Table(name="runtimeaction")
@Inheritance( strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "runtimeactiontype")
public class AbstractRuntimeActionImpl extends BaseObjectImpl implements AbstractRuntimeAction {

    private String actionType;
    private String actionId;
    private User user;
    private long timeInMillis;
    private String session;
    private String mission;

    @Override
    public String getActionType() {
        return actionType;
    }

    @Override
    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    @Override
    public String getActionId() {
        return actionId;
    }

    @Override
    public void setActionId(String actionId) {
        this.actionId = actionId;
    }

    @Override
    @ManyToOne(targetEntity = SCYUserImpl.class, cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_primKey")
    public User getUser() {
        return user;
    }

    @Override
    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public long getTimeInMillis() {
        return timeInMillis;
    }

    @Override
    public void setTimeInMillis(long timeInMillis) {
        this.timeInMillis = timeInMillis;
    }

    @Override
    public String getSession() {
        return session;
    }

    @Override
    public void setSession(String session) {
        this.session = session;
    }

    @Override
    public String getMission() {
        return mission;
    }

    @Override
    public void setMission(String mission) {
        this.mission = mission;
    }
}

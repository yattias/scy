package eu.scy.core.model.runtime;

import eu.scy.core.model.ScyBase;
import eu.scy.core.model.User;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 22.apr.2010
 * Time: 06:32:40
 * To change this template use File | Settings | File Templates.
 */
public interface AbstractRuntimeAction extends ScyBase {


    String getActionType();

    void setActionType(String actionType);

    String getActionId();

    void setActionId(String actionId);

    User getUser();

    void setUser(User user);

    long getTimeInMillis();

    void setTimeInMillis(long timeInMillis);

    String getSession();

    void setSession(String session);

    String getMission();

    void setMission(String mission);
}

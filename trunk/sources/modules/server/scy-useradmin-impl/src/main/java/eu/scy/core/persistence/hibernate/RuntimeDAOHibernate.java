package eu.scy.core.persistence.hibernate;

import eu.scy.core.model.User;
import eu.scy.core.model.impl.runtime.EloRuntimeActionImpl;
import eu.scy.core.model.impl.runtime.ToolRuntimeActionImpl;
import eu.scy.core.model.runtime.AbstractRuntimeAction;
import eu.scy.core.model.runtime.EloRuntimeAction;
import eu.scy.core.model.runtime.ToolRuntimeAction;
import eu.scy.core.persistence.RuntimeDAO;
import eu.scy.core.persistence.UserDAO;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 22.apr.2010
 * Time: 06:14:02
 * To change this template use File | Settings | File Templates.
 */
public class RuntimeDAOHibernate extends ScyBaseDAOHibernate implements RuntimeDAO {

    private UserDAO userDAO;

    @Override
    public List getActions(User user) {
        return getSession().createQuery("from AbstractRuntimeActionImpl where user = :user order by timeCreated")
                .setEntity("user", user)
                .list();
    }

    @Override
    public void storeAction(String type, String id, long timeInMillis, String tool, String mission, String session, String eloUri, String userName) {
        logger.debug("STORING ACTION: " + type + " " + id + " " + timeInMillis + " " + tool + " " + mission + " " + session + " " + eloUri);
        userName = userName.substring(0, userName.indexOf("@"));
        logger.debug("Loading user: " + userName);
        User user = getUserDAO().getUserByUsername(userName);
        user = (User) getHibernateTemplate().merge(user);
        if (user != null) {
            logger.debug("ACTION PERFORMED BY: " + user.getUserDetails().getUsername());
            AbstractRuntimeAction runtimeAction = createRuntimeAction(type);
            if (runtimeAction != null) {
                if (runtimeAction instanceof ToolRuntimeAction) {
                    ((ToolRuntimeAction) runtimeAction).setTool(tool);
                } else if(runtimeAction instanceof EloRuntimeAction) {

                }

                runtimeAction.setUser(user);
                runtimeAction.setActionId(id);
                runtimeAction.setActionType(type);
                runtimeAction.setTimeInMillis(timeInMillis);


                save(runtimeAction);


            } else {
                logger.warn("UNRECOGNIZED ACTION " + type + " - GIVING MAJOR SHIT!");
            }


        }
    }

    private AbstractRuntimeAction createRuntimeAction(String type) {
        if (type.contains("tool")) return new ToolRuntimeActionImpl();
        else if(type.contains("elo")) return new EloRuntimeActionImpl();
        return null;
    }

    public UserDAO getUserDAO() {
        return userDAO;
    }

    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }
}

package eu.scy.core.persistence.hibernate;

import eu.scy.core.ToolService;
import eu.scy.core.model.User;
import eu.scy.core.model.impl.runtime.EloRuntimeActionImpl;
import eu.scy.core.model.impl.runtime.LASRuntimeActionImpl;
import eu.scy.core.model.impl.runtime.ToolRuntimeActionImpl;
import eu.scy.core.model.pedagogicalplan.Tool;
import eu.scy.core.model.runtime.AbstractRuntimeAction;
import eu.scy.core.model.runtime.EloRuntimeAction;
import eu.scy.core.model.runtime.LASRuntimeAction;
import eu.scy.core.model.runtime.ToolRuntimeAction;
import eu.scy.core.persistence.RuntimeDAO;
import eu.scy.core.persistence.UserDAO;
import roolo.api.IRepository;
import roolo.elo.api.IELO;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
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
    private static final List CURRENT_TOOL_TRIGGERS = new LinkedList();
    private IRepository repository;
    private ToolService toolService;



    @Override
    public List getActions(User user) {
        return getSession().createQuery("from AbstractRuntimeActionImpl where user = :user order by timeCreated")
                .setEntity("user", user)
                .list();
    }

    @Override
    public AbstractRuntimeAction getLatestInterestingAction(User user) {
         return (AbstractRuntimeAction) getSession().createQuery("from AbstractRuntimeActionImpl where user = :user order by timeCreated desc")
                .setEntity("user", user)
                 .setMaxResults(1)
                .uniqueResult();
    }

    @Override
    public String getCurrentTool(User user) {
         ToolRuntimeAction toolAction = (ToolRuntimeAction) getSession().createQuery("from ToolRuntimeActionImpl where user = :user and actionType in (:actionTypes) order by timeCreated desc ")
                 .setEntity("user", user)
                 .setParameterList("actionTypes", getToolTriggers())
                 .setMaxResults(1)
                 .uniqueResult();
        if(toolAction != null) {

            Tool tool = getToolService().getToolByToolId(toolAction.getTool());
            if(tool == null) {
                getToolService().registerTool(toolAction.getTool());
                tool = getToolService().getToolByToolId(toolAction.getTool());
            }

            if(tool != null) return tool.getName();
            else return toolAction.getTool();
        }
        return "";
    }

    @Override
    public String getCurrentELO(User user) {
        EloRuntimeAction eloRuntimeAction = (EloRuntimeAction) getSession().createQuery("from EloRuntimeActionImpl where user = :user order by timeCreated desc")
                .setEntity("user", user)
                .setMaxResults(1)
                .uniqueResult();
        if(eloRuntimeAction != null) {
            if(getRepository() != null) {
                try {
                    long start = System.currentTimeMillis();
                    URI eloUri = new URI(eloRuntimeAction.getEloUri());
                    logger.debug("Trying to load elo: '" + eloUri.toString() +  "'");
                    IELO elo = getRepository().retrieveELOLastVersion(eloUri);
                    logger.debug("ELO: " + elo);
                    if(elo != null) {
                        logger.debug("used  "+ (System.currentTimeMillis() - start) + " millis to load ELO from ROOLO");
                        return elo.getXml();
                    } else {
                        return eloUri.toString();
                    }

                } catch (URISyntaxException e) {
                    logger.error(e);
                    throw new RuntimeException(e);
                }
            }
        } else {
            logger.warn("REPOSITORY IS NULL!!");
        }
        return "";
    }

    @Override
    public String getCurrentLAS(User user) {
                 LASRuntimeAction runtimeAction = (LASRuntimeAction) getSession().createQuery("from LASRuntimeActionImpl where user = :user order by timeCreated desc ")
                 .setEntity("user", user)
                 .setMaxResults(1)
                 .uniqueResult();
        if(runtimeAction != null) {
            return runtimeAction.getNewLASId();
        }

        return null;
    }

    @Override
    public List getLastELOs(User user) {
        return getSession().createQuery("from EloRuntimeActionImpl where user = :user order by timeCreated desc")
                .setEntity("user", user)
                .setMaxResults(5)
                .list();
    }

    @Override
    public List getUsersCurrentlyinLAS(String lasId) {

        List users = getUserDAO().getUsers();
        LinkedList returnList = new LinkedList();
        for (int i = 0; i < users.size(); i++) {
            User user = (User) users.get(i);
            String las = getCurrentLAS(user);
            if(las != null && las.equals(lasId)) returnList.add(user);
        }

        return returnList;
    }


    @Override
    public void storeAction(String type, String id, long timeInMillis, String tool, String mission, String session, String eloUri, String userName, String newLASId, String oldLASId) {
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
                     Tool aTool = getToolService().getToolByToolId(tool);
                     if(aTool == null) getToolService().registerTool(tool);
                } else if(runtimeAction instanceof EloRuntimeAction) {
                    ((EloRuntimeAction) runtimeAction).setEloUri(eloUri);
                } else if(runtimeAction instanceof LASRuntimeAction) {
                    ((LASRuntimeAction) runtimeAction).setNewLASId(newLASId);
                    ((LASRuntimeAction) runtimeAction).setOldLASId(oldLASId);
                }

                logger.debug("ACTION IS OF TYPE: " + runtimeAction.getClass().getName());

                runtimeAction.setUser(user);
                runtimeAction.setActionId(id);
                runtimeAction.setActionType(type);
                runtimeAction.setTimeInMillis(timeInMillis);
                runtimeAction.setMission(mission);
                runtimeAction.setSession(session);
               
                //save(runtimeAction);


            } else {
                //logger.warn("UNRECOGNIZED ACTION " + type + " - GIVING MAJOR SHIT!");
            }


        }
    }

    private AbstractRuntimeAction createRuntimeAction(String type) {
        if (type.contains("tool")) return new ToolRuntimeActionImpl();
        else if(type.contains("elo")) return new EloRuntimeActionImpl();
        else if(type.contains("las")) return new LASRuntimeActionImpl();
        return null;
    }

    public UserDAO getUserDAO() {
        return userDAO;
    }

    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    private List getToolTriggers() {
        List returnList = new LinkedList();
        returnList.add("tool_start");
        returnList.add("tool_gotfocus");
        returnList.add("tool_opened");
        return returnList;
    }

    public IRepository getRepository() {
        return repository;
    }

    public void setRepository(IRepository repository) {
        this.repository = repository;
    }

    public ToolService getToolService() {
        return toolService;
    }

    public void setToolService(ToolService toolService) {
        this.toolService = toolService;
    }
}

package eu.scy.agents.processguidanceservice;

import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Callback;
import info.collide.sqlspaces.commons.Field;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;
import info.collide.sqlspaces.commons.User;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

import org.apache.log4j.Logger;

import eu.scy.actionlogging.Action;
import eu.scy.actionlogging.ActionTupleTransformer;
import eu.scy.actionlogging.api.ContextConstants;
import eu.scy.agents.impl.ActionConstants;
import eu.scy.agents.impl.AgentProtocol;

public class ProcessGuidanceAgent implements Callback {

    public static final String GUIDANCE_AGENT_ID = "process_guidance_agent";

    public static final String NAME = ProcessGuidanceAgent.class.getName();

    public static final Logger logger = Logger.getLogger(NAME);

    private static TupleSpace guidanceSpace;

    private static TupleSpace actionSpace;

    private static TupleSpace commandSpace;

    private MissionModel[] missionModels;

    private HashMap<String, RunUser> users;

    public ProcessGuidanceAgent() {
        try {
            System.out.println("ProcessGuidanceAgent starts to work");
            init();
            getActionSpace().eventRegister(Command.WRITE, new Tuple("action", Field.createWildCardField()), this, false);
            System.out.println("starts to listening");

            // the following line is used only in the development phase
            getGuidanceSpace().takeAll(new Tuple(Field.createWildCardField()));

        } catch (TupleSpaceException e) {
            e.printStackTrace();
        }
    }

    public static TupleSpace getGuidanceSpace() {
        if (guidanceSpace == null) {
            try {
                guidanceSpace = new TupleSpace(User.getDefaultUser(), "scy.collide.info", 2525, false, false, "guidance");
                ;
            } catch (TupleSpaceException e) {
                e.printStackTrace();
            }
        }
        return guidanceSpace;
    }

    public static TupleSpace getActionSpace() {
        if (actionSpace == null) {
            try {
                actionSpace = new TupleSpace(User.getDefaultUser(), "scy.collide.info", 2525, false, false, AgentProtocol.ACTION_SPACE_NAME);
                ;
            } catch (TupleSpaceException e) {
                e.printStackTrace();
            }
        }
        return actionSpace;
    }

    public static TupleSpace getCommandSpace() {
        if (commandSpace == null) {
            try {
                commandSpace = new TupleSpace(User.getDefaultUser(), "scy.collide.info", 2525, false, false, AgentProtocol.COMMAND_SPACE_NAME);
                ;
            } catch (TupleSpaceException e) {
                e.printStackTrace();
            }
        }
        return commandSpace;
    }

    private void init() {
        users = new HashMap<String, RunUser>();
        missionModels = new MissionModel[0];

        // used in the development phase
        addPizzaMission();
        // addECOMission();
        // addHouseMission();
    }

    private void addPizzaMission() {
        MissionModel[] newMissionModels = (MissionModel[]) Arrays.copyOf(missionModels, missionModels.length + 1);
        MissionModel aModel = new MissionModel("roolo://scy.collide.info/scy-collide-server/261.261#0", "pizzaMission");
        aModel.buildPizzaMission();
        newMissionModels[newMissionModels.length - 1] = aModel;
        missionModels = newMissionModels;
    }

    private void addECOMission() {
        MissionModel[] newMissionModels = (MissionModel[]) Arrays.copyOf(missionModels, missionModels.length + 1);
        MissionModel aModel = new MissionModel("roolo://scy.collide.info/scy-collide-server/199.199#0", "ecoMission");
        aModel.buildECOMission();
        newMissionModels[newMissionModels.length - 1] = aModel;
        missionModels = newMissionModels;
    }

    private void addHouseMission() {
        MissionModel[] newMissionModels = (MissionModel[]) Arrays.copyOf(missionModels, missionModels.length + 1);
        MissionModel aModel = new MissionModel("roolo://scy.collide.info/scy-collide-server/135.135#0", "co2MissionRedesign");
        aModel.buildHouseMission();
        newMissionModels[newMissionModels.length - 1] = aModel;
        missionModels = newMissionModels;
    }

    @Override
    public void call(Command cmd, int seqnum, Tuple afterTuple, Tuple beforeTuple) {
        if (afterTuple == null)
            return;

        Action action = (Action) ActionTupleTransformer.getActionFromTuple(afterTuple);
        String userID = action.getUser();

        if (!userID.equalsIgnoreCase("studentm@scy.collide.info/Smack"))
            return;
        System.out.println(action);

        String actionType = action.getType();
        if (actionType.equals("text_inserted")) {
            handleTextInserted(action);
        } else if (actionType.equals("text_deleted")) {
            handleTextDeleted(action);
        } else if (actionType.equals("tool_got_focus")) {
            handleToolGotFocus(action);
        } else if (actionType.equals("tool_lost_focus")) {
            handleToolLostFocus(action);
        } else if (actionType.equals(ActionConstants.ACTION_ELO_LOADED)) {
            handleELOLoaded(action);
        } else if (actionType.equals(ActionConstants.ACTION_ELO_SAVED)) {
            handleELOSaved(action);
        } else if (actionType.equals(ActionConstants.ACTION_TOOL_STARTED)) {
            handleToolStarted(action);
        } else if (actionType.equals("tool_quit")) {
            handleToolQuit(action);
        } else if (actionType.equals("elo_finished")) {
            handleToolComplete(action);
        } else if (actionType.equals(ActionConstants.ACTION_LOG_IN)) {
            handleLogin(action);
        } else if (actionType.equals(ActionConstants.ACTION_LOG_OUT)) {
            handleLogout(action);
        } else if (actionType.equals(ActionConstants.ACTION_LAS_CHANGED)) {
            handleLasChanged(action);
        } else if (actionType.equals(ActionConstants.ACTION_TOOL_OPENED)) {
            handleToolOpened(action);
        } else if (actionType.equals(ActionConstants.ACTION_TOOL_CLOSED)) {
            handleToolClosed(action);
        } else {
            System.out.println("unhandling the action=" + actionType);
        }
    }

    private void handleTextInserted(Action action) {
        RunUser aRunUser = users.get(action.getUser());
        String elo_uri = action.getContext(ContextConstants.eloURI);
        ELORun aELORun = aRunUser.getMissionRun().findELORunByURI(elo_uri);
        if (aELORun != null) {
            aELORun.handleContentIncreased(aRunUser, action.getAttribute("text").length(), action.getTimeInMillis());
            aRunUser.getActionHistory().addAction(new UserAction(UserAction.ACTION_TEXT_INSERTED, aELORun, action.getTimeInMillis()));
        }
    }

    private void handleTextDeleted(Action action) {
        RunUser aRunUser = users.get(action.getUser());
        String elo_uri = action.getContext(ContextConstants.eloURI);
        ELORun aELORun = aRunUser.getMissionRun().findELORunByURI(elo_uri);
        if (aELORun != null) {
            aELORun.handleContentDecreased(aRunUser, action.getAttribute("text").length(), action.getTimeInMillis());
            aRunUser.getActionHistory().addAction(new UserAction(UserAction.ACTION_TEXT_DELETED, aELORun, action.getTimeInMillis()));
        }
    }

    private void handleLogin(Action action) {
        String missionSpecificationURI = action.getAttribute("missionSpecification");
        MissionModel aMissionModel = null;
        for (int i = 0; i < missionModels.length; i++) {
            if (missionSpecificationURI.equalsIgnoreCase(missionModels[i].getId())) {
                aMissionModel = missionModels[i];
            }
        }
        if (aMissionModel == null) {
            // new mission model
            aMissionModel = new MissionModel(missionSpecificationURI, action.getAttribute("missionName"));
            aMissionModel.buildMissionModel();
            MissionModel[] newMissionModels = (MissionModel[]) Arrays.copyOf(missionModels, missionModels.length + 1);
            newMissionModels[newMissionModels.length - 1] = aMissionModel;
            missionModels = newMissionModels;
        }

        String userID = action.getUser();
        RunUser aRunUser = new RunUser(userID);
        aRunUser.buildRunUser();
        users.put(userID, aRunUser);

        String missionRunID = action.getContext().get(ContextConstants.mission);
        MissionRun aMissionRun = new MissionRun(missionRunID, aRunUser, aMissionModel);
        aMissionRun.buildMissionRun();
        aRunUser.setMissionRun(aMissionRun);
    }

    private void handleLasChanged(Action action) {
        RunUser aRunUser = users.get(action.getUser());
        // aRunUser.setCurrentLASName(action.getAttribute(ActionConstants.LAS));
    }

    private void handleToolStarted(Action action) {
        RunUser aRunUser = users.get(action.getUser());
        String elo_uri = action.getContext(ContextConstants.eloURI);
        ELORun aELORun = aRunUser.getMissionRun().findELORunByURI(elo_uri);
        if (aELORun == null) {
            aELORun = new ELORun(elo_uri);
            aELORun.buildELORun(aRunUser, elo_uri);

            if (aELORun.getELOModel() != null) {
                // a planned elo
                aRunUser.getMissionRun().addELORun(aELORun);
                aRunUser.getActionHistory().addAction(new UserAction(UserAction.ACTION_TOOL_STARTED, aELORun, action.getTimeInMillis()));
            } else {
                // an unplanned elo, ignore
            }
        } else {
            // the user starts a closed, used tool in the same session, nothing happens
        }
    }

    private void handleToolOpened(Action action) {
        RunUser aRunUser = users.get(action.getUser());
        String elo_uri = action.getContext(ContextConstants.eloURI);
        ELORun aELORun = aRunUser.getMissionRun().findELORunByURI(elo_uri);
        if (aELORun != null) {
            aRunUser.addOpenedELORun(aELORun);
            aRunUser.getActionHistory().addAction(new UserAction(UserAction.ACTION_TOOL_OPENED, aELORun, action.getTimeInMillis()));
        } else {
            // it is an unplanned elo, ignore
        }
    }

    private void handleToolGotFocus(Action action) {
        RunUser aRunUser = users.get(action.getUser());
        String elo_uri = action.getContext(ContextConstants.eloURI);
        ELORun aELORun = aRunUser.findOpenedELORunByURI(elo_uri);
        if (aELORun != null) {
            aRunUser.setFocusedELORun(aELORun);
            aRunUser.getActionHistory().addAction(new UserAction(UserAction.ACTION_TOOL_GOT_FOCUS, aELORun, action.getTimeInMillis()));
        }
    }

    private void handleELOLoaded(Action action) {

    }

    private void handleELOSaved(Action action) {
        RunUser aRunUser = users.get(action.getUser());
        String elo_uri = action.getContext(ContextConstants.eloURI);
        String old_uri = action.getAttribute("old_uri");

        String code1 = elo_uri.substring(elo_uri.lastIndexOf("/") + 1, elo_uri.lastIndexOf("."));
        String code2 = old_uri.substring(old_uri.lastIndexOf("/") + 1, old_uri.lastIndexOf("."));

        ELORun aELORun = aRunUser.getMissionRun().findELORunByURI(old_uri);
        if (aELORun != null) {
            aELORun.setId(elo_uri);
            if (!code1.equalsIgnoreCase(code2)) {
                // save as a fork
                aELORun.handleCompleteEvent(aRunUser, action.getTimeInMillis());
                aELORun.renameELORun(elo_uri);
            }
            aELORun.updateELOTuple(aRunUser);
            aRunUser.getActionHistory().addAction(new UserAction(UserAction.ACTION_ELO_SAVED, aELORun, action.getTimeInMillis()));
        }
    }

    private void handleToolComplete(Action action) {
        RunUser aRunUser = users.get(action.getUser());
        String elo_uri = action.getContext(ContextConstants.eloURI);
        ELORun aELORun = aRunUser.getMissionRun().findELORunByURI(elo_uri);
        if (aELORun != null) { // a planned elo
            aELORun.handleCompleteEvent(aRunUser, action.getTimeInMillis());
            aRunUser.getActionHistory().addAction(new UserAction(UserAction.ACTION_TOOL_COMPLETE, aELORun, action.getTimeInMillis()));
        }
    }

    private void handleToolLostFocus(Action action) {
        RunUser aRunUser = users.get(action.getUser());
        String elo_uri = action.getContext(ContextConstants.eloURI);
        ELORun aELORun = aRunUser.findOpenedELORunByURI(elo_uri);
        if ((aELORun != null) && (elo_uri.equalsIgnoreCase(aRunUser.getFocusedELORun().getId()))) {
            aRunUser.setFocusedELORun(null);
            aRunUser.getActionHistory().addAction(new UserAction(UserAction.ACTION_TOOL_LOST_FOCUS, aELORun, action.getTimeInMillis()));
        }
    }

    private void handleToolClosed(Action action) {
        RunUser aRunUser = users.get(action.getUser());
        String elo_uri = action.getContext(ContextConstants.eloURI);
        ELORun aELORun = aRunUser.findOpenedELORunByURI(elo_uri);
        aRunUser.removeOpenedELORunById(elo_uri);
        aRunUser.getActionHistory().addAction(new UserAction(UserAction.ACTION_TOOL_CLOSED, aELORun, action.getTimeInMillis()));
    }

    private void handleToolQuit(Action action) {
        RunUser aRunUser = users.get(action.getUser());
        String elo_uri = action.getContext(ContextConstants.eloURI);
        ELORun aELORun = aRunUser.getMissionRun().findELORunByURI(elo_uri);
        aELORun.setLastAccessTime(new Date().getTime());
        aRunUser.getActionHistory().addAction(new UserAction(UserAction.ACTION_TOOL_QUIT, aELORun, action.getTimeInMillis()));
    }

    private void handleLogout(Action action) {
        String userID = action.getUser();
        RunUser aRunUser = users.get(userID);
        // I don't know why the last logout action will be received when login again
        if (aRunUser == null)
            return;
        aRunUser.updateUserTuple(); // change the last access time
        aRunUser.getMissionRun().updateMissionRunTuple(); // change the last access time
        users.remove(userID);
    }

    public static void main(String[] args) {
        new ProcessGuidanceAgent();
    }
}

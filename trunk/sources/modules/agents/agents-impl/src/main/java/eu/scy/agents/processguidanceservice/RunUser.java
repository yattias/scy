package eu.scy.agents.processguidanceservice;

import info.collide.sqlspaces.commons.Field;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;

import java.io.IOException;
import java.rmi.dgc.VMID;
import java.util.ArrayList;

import eu.scy.agents.impl.AgentProtocol;

public class RunUser extends AbstractRun {

    private MissionRun currentMissionRun;

    private String currentLASName;

    private ArrayList<ELORun> openedELORuns = new ArrayList<ELORun>();

    private ELORun focusedELORun;

    private ActionList actionHistory = new ActionList();

    private String generalGuidanceLevel;

    public RunUser(String aUserID) {
        id = aUserID;
    }

    public String getUserName() {
        int i = id.indexOf("@");
        return id.substring(0, i);
    }

    public MissionRun getMissionRun() {
        return currentMissionRun;
    }

    public void setMissionRun(MissionRun aMissionRun) {
        currentMissionRun = aMissionRun;
    }

    public String getCurrentLASName() {
        return currentLASName;
    }

    public void setCurrentLASName(String aName) {
        currentLASName = aName;
    }

    public ELORun getFocusedELORun() {
        return focusedELORun;
    }

    public void setFocusedELORun(ELORun aELORun) {
        focusedELORun = aELORun;
    }

    public ArrayList<ELORun> getOpenedELORun() {
        return openedELORuns;
    }

    public void addOpenedELORun(ELORun aELORun) {
        if ((aELORun != null) && (!openedELORuns.contains(aELORun)))
            openedELORuns.add(aELORun);
    }

    public void removeOpenedELORun(ELORun aELORun) {
        openedELORuns.remove(aELORun);
    }

    public ELORun findOpenedELORunByURI(String aURI) {
        for (int i = 0; i < openedELORuns.size(); i++) {
            if (aURI.equalsIgnoreCase(openedELORuns.get(i).getId())) {
                return (ELORun) openedELORuns.get(i);
            }
        }
        return null;
    }

    public void addOpenedELORunWithURI(String aURI) {
        ELORun aELORun = currentMissionRun.findELORunByURI(aURI);
        addOpenedELORun(aELORun);
    }

    public void removeOpenedELORunById(String aURI) {
        for (int i = 0; i < openedELORuns.size(); i++) {
            if (aURI.equalsIgnoreCase(openedELORuns.get(i).getId())) {
                openedELORuns.remove(openedELORuns.get(i));
                return;
            }
        }
    }

    public ActionList getActionHistory() {
        return actionHistory;
    }

    public String getGeneralGuidanceLevel() {
        return generalGuidanceLevel;
    }

    public void setGeneralGuidanceLevel(String aLevel) {
        generalGuidanceLevel = aLevel;
    }

    public String increaseGuidanceLevel() {
        if ((generalGuidanceLevel.equalsIgnoreCase(MissionRun.MEDIUM)) || (generalGuidanceLevel.equalsIgnoreCase(MissionRun.HIGH))) {
            generalGuidanceLevel = MissionRun.HIGH;
        } else if (generalGuidanceLevel.equalsIgnoreCase(MissionRun.LOW)) {
            generalGuidanceLevel = MissionRun.MEDIUM;
        } else if (generalGuidanceLevel.equalsIgnoreCase(MissionRun.NO)) {
            generalGuidanceLevel = MissionRun.LOW;
        }
        return generalGuidanceLevel;
    }

    public String decreaseGuidanceLevel() {
        if ((generalGuidanceLevel.equalsIgnoreCase(MissionRun.NO)) || (generalGuidanceLevel.equalsIgnoreCase(MissionRun.LOW))) {
            generalGuidanceLevel = MissionRun.NO;
        } else if (generalGuidanceLevel.equalsIgnoreCase(MissionRun.MEDIUM)) {
            generalGuidanceLevel = MissionRun.LOW;
        } else if (generalGuidanceLevel.equalsIgnoreCase(MissionRun.HIGH)) {
            generalGuidanceLevel = MissionRun.MEDIUM;
        }
        return generalGuidanceLevel;
    }

    @Override
    public String toString() {
        String result = new String("user=" + getUserName() + ", missionRunID=" + currentMissionRun.getCode() + ", missionModelID=" + currentMissionRun.getMissionModel().getCode() + ", mission name=" + currentMissionRun.getMissionModel().getName() + ", focusedELO=" + ((focusedELORun == null) ? "no" : focusedELORun.getTitle()) + ", guidanceLevel=" + generalGuidanceLevel + ", openedELORuns: ");
        for (int i = 0; i < openedELORuns.size(); i++) {
            result += openedELORuns.get(i).getCode() + new String("/");
        }
        result += new String("/ ");
        return result;
    }

    public void updateUserTuple() {
        try {
            ProcessGuidanceAgent.getGuidanceSpace().update(myTupleID, new Tuple("user", getId(), generalGuidanceLevel));
        } catch (TupleSpaceException e) {
            ProcessGuidanceAgent.logger.info("Error in guidance TupleSpace while read user info");
        }
    }

    public void buildRunUser() {
        try {
            // check whether it is a new user
            Tuple aUserTuple = ProcessGuidanceAgent.getGuidanceSpace().read(new Tuple("user", getId(), Field.createWildCardField()));
            if (aUserTuple == null) {
                // a new user
                generalGuidanceLevel = MissionRun.HIGH; // "high" as default value
                myTupleID = ProcessGuidanceAgent.getGuidanceSpace().write(new Tuple("user", getId(), generalGuidanceLevel));
            } else {
                // an already registered user
                myTupleID = aUserTuple.getTupleID();
                generalGuidanceLevel = (String) aUserTuple.getField(2).getValue();
            }
        } catch (TupleSpaceException e) {
            ProcessGuidanceAgent.logger.info("Error in guidance TupleSpace while read user info");
        }
    }

    private Tuple createAgendaNotification(String notificationId, String user, String message, String status, String time, String elo_uri) {
        Tuple notificationTuple = new Tuple();
        notificationTuple.add(AgentProtocol.NOTIFICATION); // 1
        notificationTuple.add(notificationId); // 2
        notificationTuple.add(user); // 3
        notificationTuple.add("scylab"); // 4
        notificationTuple.add("process guidance agent"); // 5
        notificationTuple.add("mission"); // 6
        notificationTuple.add("session"); // 7
        notificationTuple.add("type=agenda_notify"); // 8
        notificationTuple.add("text=" + message); // 9
        notificationTuple.add("timestamp=" + time); // 10
        notificationTuple.add("state=" + status); // 11
        notificationTuple.add("elouri=" + elo_uri); // 12
        System.out.println("Status=" + status + " Activity=" + message + " Time=" + time);
        return notificationTuple;
    }

    private Tuple createMessageNotification(String notificationId, String user, String message, String time) {
        Tuple notificationTuple = new Tuple();
        notificationTuple.add(AgentProtocol.NOTIFICATION); // 1
        notificationTuple.add(notificationId); // 2
        notificationTuple.add(user); // 3
        notificationTuple.add("scylab"); // 4
        notificationTuple.add("process guidance agent"); // 5
        notificationTuple.add("mission"); // 6
        notificationTuple.add("session"); // 7
        notificationTuple.add("type=agenda_notify"); // 8
        notificationTuple.add("text=" + message); // 9
        notificationTuple.add("timestamp=" + time); // 10
        System.out.println("[" + time + "]: " + message);
        return notificationTuple;
    }

    public void sendNotification(String aMessage) {
        System.out.println("Send a notification to the user: \"" + getUserName() + "\", " + aMessage);
    }

    public boolean sendConfirmation(String aQuestion) {
        System.out.println("Send a notification to the user: \"" + getUserName() + "\", " + aQuestion);
        try {
            String question = new String("Please input: Y(es) or N(o)?");
            while (true) {
                System.out.println(question);
                char c = (char) System.in.read();
                if ((c == 'Y') || (c == 'y')) {
                    return true;
                } else if ((c == 'N') || (c == 'n')) {
                    return false;
                }
                question = new String("You did not answer correctly. Please input: Y(es) or N(o)?");
            }
        } catch (IOException e) {
            System.out.println(e);
        }
        return false;
    }

    public void provideGuidanceAfterComplete() {

        try {
            String completeNotificationId = createId();
            Tuple completeNotificationTuple = createAgendaNotification(completeNotificationId, this.getId(), getFocusedELORun().getTitle(), getFocusedELORun().getActivityStatus(), String.valueOf(getFocusedELORun().getFinishedTime()), getFocusedELORun().getId());
            ProcessGuidanceAgent.getCommandSpace().write(completeNotificationTuple);

            String aTitle = focusedELORun.getELOModel().getAnUnfinishedDescendant(this);
            String messageNotificationId = createId();
            Tuple messageNotificationTuple;
            if (aTitle != null) {
                messageNotificationTuple = createMessageNotification(messageNotificationId, this.getId(), "You have completed the activity: \"" + getFocusedELORun().getTitle() + "\". The recommanded next activity is \"" + aTitle + "\".", String.valueOf(getFocusedELORun().getFinishedTime()));
            } else {
                messageNotificationTuple = createMessageNotification(messageNotificationId, this.getId(), "You have completed all activities.", String.valueOf(getFocusedELORun().getFinishedTime()));
            }
            ProcessGuidanceAgent.getCommandSpace().write(messageNotificationTuple);

        } catch (TupleSpaceException e) {
            ProcessGuidanceAgent.logger.info("Error in TupleSpace while load an object in roolo");
        }
    }

    public void provideGuidance() {

        if (getMissionRun().getGuidanceLevel().equalsIgnoreCase(MissionRun.HIGH)) {
            provideGuidanceAfterComplete();
        } else if (getMissionRun().getGuidanceLevel().equalsIgnoreCase(MissionRun.MEDIUM)) {

        } else if (getMissionRun().getGuidanceLevel().equalsIgnoreCase(MissionRun.LOW)) {

        } else {

        }

    }

    private String createId() {
        return new VMID().toString();
    }

}
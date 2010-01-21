package eu.scy.agents.sensors.behaviourclassifier;

import java.rmi.dgc.VMID;

import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;

public class BehavioralModel {

    public enum Notification {
        LEVEL1,
        LEVEL2,
        LEVEL3;
    }

    private int userExp;

    private int votat;

    private int canonical;

    private String tool;

    private String name;

    private TupleSpace commandSpace;

    private String session;

    private String mission;

    public BehavioralModel(String name, String tool, String mission, String session, int canonical, int votat, int userExp, TupleSpace commandSpace) {
        this.name = name;
        this.tool = tool;
        this.canonical = canonical;
        this.votat = votat;
        this.userExp = userExp;
        this.commandSpace = commandSpace;
        this.mission = mission;
        this.session = session;
    }

    public int getUserExp() {
        return userExp;
    }

    public int getVotat() {
        return votat;
    }

    public int getCanonical() {
        return canonical;
    }

    public String getTool() {
        return tool;
    }

    public String getName() {
        return name;
    }

    public void updateCanonical(int newCanonical) {
        this.canonical = newCanonical;
        checkForSystematicBehaviour();
    }

    public void updateUserExp(int newUserExp) {
        this.userExp = newUserExp;
        checkForSystematicBehaviour();
    }

    public void updateVotat(int newVotat) {
        this.votat = newVotat;
        checkForSystematicBehaviour();
    }

    private void checkForSystematicBehaviour() {
        // Simple ruleset :externalize
        if (votat < 30 && canonical < 30 && userExp > 60) {
            sendNotification(Notification.LEVEL1);
        } else if (votat < 20 && canonical < 20 && userExp > 60) {
            sendNotification(Notification.LEVEL2);
        } else if (votat < 10 && canonical < 10 && userExp > 60) {
            sendNotification(Notification.LEVEL3);
        }
    }

    private void sendNotification(Notification level) {
        Tuple notificationTuple = new Tuple("notification", new VMID().toString(), name, tool, "ScySimBehaviorClassifier", mission, session, "type=scaffold","level=" + level.name());
        try {
            commandSpace.write(notificationTuple);
        } catch (TupleSpaceException e) {
            e.printStackTrace();
        }
    }
}

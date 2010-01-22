package eu.scy.agents.sensors.behaviourclassifier;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.dgc.VMID;
import java.util.List;
import java.util.Vector;

import javax.swing.Timer;

import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;

public class BehavioralModel {

    private static final int NOTIFICATION_DELAY = 10 * 1000;

    public enum SCAFFOLD {
        VOTAT,
        INC_CHANGE,
        EXTREME_VALUES,
        CONFIRM_HYPO,
        IDENT_HYPO,
        SHOWBUTTON;
    }

    private int userExp;

    private int votat;

    private int canonical;

    private String tool;

    private String name;

    private TupleSpace commandSpace;

    private String session;

    private String mission;

    private boolean showButtonSent = false;

    private List<SCAFFOLD> sentScaffolds;

    private Timer timer;

    public BehavioralModel(String name, String tool, String mission, String session, int canonical, int votat, int userExp, TupleSpace commandSpace) {
        this.name = name;
        this.tool = tool;
        this.canonical = canonical;
        this.votat = votat;
        this.userExp = userExp;
        this.commandSpace = commandSpace;
        this.mission = mission;
        this.session = session;
        sentScaffolds = new Vector<SCAFFOLD>();
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
        if (userExp >= 20){
            sendNotification(SCAFFOLD.VOTAT);
            showButtonSent = true;
        }
//        if (votat < 30 && canonical < 30 && userExp > 30) {
//            sendNotification(SCAFFOLD.VOTAT);
//        } else if (votat < 20 && canonical < 20 && userExp > 30) {
//            sendNotification(SCAFFOLD.INC_CHANGE);
//        } else if (votat < 10 && canonical < 10 && userExp > 30) {
//            sendNotification(SCAFFOLD.EXTREME_VALUES);
//        }
    }

    private void sendNotification(final SCAFFOLD level) {
        if (!sentScaffolds.contains(level)) {
            sentScaffolds.add(level);
            timer = new Timer(NOTIFICATION_DELAY, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    sentScaffolds.remove(level);
                }
            });
            timer.setRepeats(false);
            timer.start();
            Tuple notificationTuple = new Tuple("notification", new VMID().toString(), name, tool, "ScySimBehaviorClassifier", mission, session, "type=scaffold", "level=" + level.name());
            try {
                commandSpace.write(notificationTuple);
            } catch (TupleSpaceException e) {
                e.printStackTrace();
            }
        }
    }
}

package eu.scy.agents.sensors.behaviourclassifier;

import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.dgc.VMID;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.Timer;

import eu.scy.agents.impl.AgentProtocol;

public class BehavioralModel {

    private static final int NORMALIZED_MAX_VALUE = 100;

    private static final int NORMALIZED_MIN_VALUE = 0;

    private static final Logger logger = Logger.getLogger(BehavioralModel.class.getSimpleName());

    private static final int NOTIFICATION_DELAY = 30 * 1000;

    // TODO This enum has to be externalized, cause the client (ScySim) is
    // using it, too. Be aware of this. If you change anything here, it has to be
    // changed on client-side, too.
    public enum Scaffold {
        VOTAT,
        INC_CHANGE,
        EXTREME_VALUES,
        CONFIRM_HYPO,
        IDENT_HYPO,
        SHOWBUTTON;
    }

    private volatile int userExp;

    private volatile int votat = 1;

    private volatile int canonical = 1;

    private String eloUri;

    private String name;

    private TupleSpace commandSpace;

    private List<Scaffold> sentScaffolds;

    private Timer timer;

    private boolean expPhaseStartet = false;

    private int level;

    /**
     * This Model provides information over the aggregated output of three agents:<br/>
     * <ul>
     * <li><i>VOTAT</i> (Vary only one thing at a time) - <b>Prolog</b></li>
     * <li>Canonical/equal increment - <b>Prolog</b></li>
     * <li>UserToolExperience - <b>Java</b></li>
     * </ul>
     * <br/>
     * After every callback from the {@link TupleSpace} this agent will check, if the users<br/>
     * behaviour is systematic or not. If not, it will provide feedback (see the SCAFFOLD enum) <br/>
     * which is sent to the user using the notification mechanism.
     * 
     * @param name
     *            The name of the user as <b>XMPP JID</b> (e.g. name@hub/smack)
     * @param eloUri
     *            The elouri this model is for
     * @param canonical
     *            The actual value for canonical (0-100 - normalized)
     * @param votat
     *            The actual value for VOTAT (0-100 - normalized)
     * @param userExp
     *            The user experience for that tool/user/mission/session combination (0 - 100 - normalized)
     * @param commandSpace
     *            The {@link TupleSpace} where the notification will be delivered
     */
    public BehavioralModel(String name, String eloUri, int canonical, int votat, int userExp, TupleSpace commandSpace) {
        this.name = name;
        this.eloUri = eloUri;
        this.canonical = canonical;
        this.votat = votat;
        this.userExp = userExp;
        this.commandSpace = commandSpace;
        sentScaffolds = new Vector<Scaffold>();
    }

    public void setScaffoldingLevel(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    /**
     * This method returns the current user experience. It returns a value between 0 - 100.
     * 
     * @return A value between 0-100 describing the current user experience
     */
    public int getUserExp() {
        return userExp;
    }

    /**
     * This method returns the current VOTAT value. It returns a value between 0 - 100.
     * 
     * @return A value between 0-100 describing the current VOTAT measurement.
     */
    public int getVotat() {
        return votat;
    }

    /**
     * This method returns the current canonical value. It returns a value between 0 - 100.
     * 
     * @return A value between 0-100 describing the current canonical/equal increment measurement.
     */
    public int getCanonical() {
        return canonical;
    }

    /**
     * Retuns the elouri of this model.
     * 
     * @return The elouri of this model.
     */
    public String getEloUri() {
        return eloUri;
    }

    /**
     * Retuns the username (should be a XMPP JID) of this model.
     * 
     * @return The (XMPP JID) name of the user of this model.
     */
    public String getName() {
        return name;
    }

    /**
     * This method updated the value of "equal increment" in this model with the given new value. Be sure that this value should not be above 100.
     * 
     * @param newCanonical
     *            The new value for equal increment / canonical for this model.
     */
    public void updateCanonical(int newCanonical) {
        if (checkForCorrectValue(newCanonical)) {
            this.canonical = newCanonical;
            checkForSystematicBehaviour();
        } else {
            logger.log(Level.WARNING, "The new value for canonical/equal increment is " + newCanonical + ". It should be a normalized value between 0 and 100. The old value will be preserved!");
        }
    }

    /**
     * This method updated the value of the user experience in this model with the given new value. Be sure that this value should not be above 100.
     * 
     * @param newUserExp
     *            The new value for the user experience for this model.
     */
    public void updateUserExp(int newUserExp) {
        if (newUserExp == 0) {

            // System.out.println("Problem! USerExp=0");
        } else {
            // System.out.println(tool+"/"+name+" set to "+newUserExp);
        }
        this.userExp = newUserExp;
        // Fix to prevent too many notifications
        // checkForSystematicBehaviour();
    }

    /**
     * This method updated the value of VOTAT in this model with the given new value. Be sure that this value should not be above 100.
     * 
     * @param newVotat
     *            The new value for VOTAT for this model.
     */
    public void updateVotat(int newVotat) {
        this.votat = newVotat;
        checkForSystematicBehaviour();
    }

    public void setExpPhaseStarted() {
        this.expPhaseStartet = true;
    }

    /*
     * This method checks if the current model is acting systematical based on the measures of the agents.
     */
    private void checkForSystematicBehaviour() {
        // Simple ruleset :externalize
        if (getLevel() > 1) {

            if (!expPhaseStartet || userExp < 2) {
                return;
            } else {
                if (votat < 1) {
                    sendNotification(Scaffold.VOTAT);
                } else if (canonical < 1) {
                    sendNotification(Scaffold.INC_CHANGE);
                } else {
                    sendNotification(Scaffold.SHOWBUTTON);
                }
            }
        }
    }

    /*
     * Checks if the value of the param value is between the min and the max value of the normalized output of the agents.
     */
    private boolean checkForCorrectValue(int value) {
        if (value >= NORMALIZED_MIN_VALUE && value <= NORMALIZED_MAX_VALUE) {
            return true;
        }
        return false;
    }

    /*
     * If this model recognizes unsystematic behaviour, a notification is sent to the user. The timer makes sure that a message will only be repeated after a delay
     */
    private void sendNotification(final Scaffold level) {
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

            try {
                commandSpace.write(createMessageNotificationTuple(new VMID().toString(), getNotificationTexts(level)[1],getNotificationTexts(level)[0], name));
            } catch (TupleSpaceException e1) {
                e1.printStackTrace();
            }
        }

    }

    public Tuple createMessageNotificationTuple(String notificationId, String message,String title, String user) {
        Tuple notificationTuple = new Tuple();
        notificationTuple.add(AgentProtocol.NOTIFICATION);
        notificationTuple.add(notificationId);
        notificationTuple.add(user);
        notificationTuple.add("no specific elo");
        notificationTuple.add(BehavioralModel.class.getSimpleName());
        notificationTuple.add("n/a");
        notificationTuple.add("n/a");
        notificationTuple.add("text=" + message);
        notificationTuple.add("title="+title);
        notificationTuple.add("type=message_dialog_show");
        notificationTuple.add("modal=true");
        notificationTuple.add("dialogType=OK_DIALOG");
        return notificationTuple;
    }

    private String[] getNotificationTexts(Scaffold level) {
        if (level == Scaffold.VOTAT) {

            String votat = "If a variable is not relevant for the hypothesis under, \ntest then hold that variable constant, or vary one thing at a time (VOTAT), or If not varying a variable, then pick the same value as used in the previous experiment";
            String title = "Vary only one thing at a time.";
            return new String[]{title,votat};
        } else if (level == Scaffold.CONFIRM_HYPO) {
            String confirmHypothesis = "Generate several additional cases in an attempt \nto either confirm or disconfirm the hypothesized relation";
            String title = "Try to confirm your hypothesis.";
            return new String[]{title,confirmHypothesis};
        } else if (level == Scaffold.EXTREME_VALUES) {
            String extemevalues = "Try some extreme values to see if there are limits on the proposed relationship";
            String title = "Try some extreme values.";
            return new String[]{title,extemevalues};
        } else if (level == Scaffold.IDENT_HYPO) {
            String identifyHypothesis = "Generate a small amount of data and examine for a candidate rule or relation";
            String title = "Try to identify a hypothesis.";
            return new String[]{title,identifyHypothesis};
        } else if (level == Scaffold.INC_CHANGE) {
            String equalIncrement = "If choosing a third value for a variable, \nthen choose an equal increment as between first and second values. Or if manipulating a variable, then choose simple, canonical manipulations ";
            String title = "Use equal increments when varying variables.";
            return new String[]{title,equalIncrement};
        }
        return null;
    }
}

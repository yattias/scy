package eu.scy.agents.sensors.behaviourclassifier;

import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Callback;
import info.collide.sqlspaces.commons.Field;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;
import info.collide.sqlspaces.commons.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import eu.scy.agents.api.AgentLifecycleException;
import eu.scy.agents.impl.AbstractThreadedAgent;
import eu.scy.agents.impl.AgentProtocol;

public class ScySimBehaviourClassifier extends AbstractThreadedAgent {

    private static final String ACTION = "action";

    private TupleSpace commandSpace;

    private static final Level DEBUGLEVEL = Level.FINE;

    private static final Logger logger = Logger.getLogger(ScySimBehaviourClassifier.class.getName());

    private boolean isStopped;

    private List<Integer> callbacks;

    private static final Tuple USER_EXP_TEMPLATE = new Tuple("user_exp", Field.createWildCardField());

    private static final Tuple VOTAT_TEMPLATE = new Tuple("votat", Field.createWildCardField());

    private static final Tuple CANONICAL_TEMPLATE = new Tuple("inc_change", Field.createWildCardField());

    private Map<String, BehavioralModel> userModels;

    private int userExpSeq;

    private int votatSeq;

    private int canoSeq;
    
    private static final double MAX_EXP_TIME = 1 * 60 * 60 * 1000;


    protected ScySimBehaviourClassifier(Map<String, Object> map) {
        super("eu.scy.agents.serviceprovider.userexperience.ScySimBehaviourClassifier", (String) map.get("id"), "scy.collide.info", 2525);
        try {
            commandSpace = new TupleSpace(new User(getSimpleName()), host, port, false, false, AgentProtocol.COMMAND_SPACE_NAME);
            SessionCallback cb = new SessionCallback();
            userExpSeq = commandSpace.eventRegister(Command.WRITE, USER_EXP_TEMPLATE, cb, false);
            votatSeq = commandSpace.eventRegister(Command.WRITE, VOTAT_TEMPLATE, cb, false);
            canoSeq = commandSpace.eventRegister(Command.WRITE, CANONICAL_TEMPLATE, cb, false);
            userModels = new HashMap<String, BehavioralModel>();
            initLogger();
        } catch (TupleSpaceException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doRun() throws TupleSpaceException, AgentLifecycleException, InterruptedException {
        while (status == Status.Running) {
            sendAliveUpdate();
            Thread.sleep(5000);
        }

    }

    @Override
    protected void doStop() {
        status = Status.Stopping;
        for (Integer cbSeq : callbacks) {
            try {
                commandSpace.eventDeRegister(cbSeq);
                commandSpace.disconnect();
            } catch (TupleSpaceException e) {
                e.printStackTrace();
            }
        }
        isStopped = true;

    }

    @Override
    protected Tuple getIdentifyTuple(String queryId) {
        // TODO This agent is just a sensor and therefore it don't need to identify....
        return null;
    }

    @Override
    public boolean isStopped() {
        return isStopped;
    }

    class SessionCallback implements Callback {

        @Override
        public void call(Command cmd, int seqnum, Tuple afterTuple, Tuple beforeTuple) {
            String user = null;
            String tool = null;
            String mission = null;
            String session = null;
            BehavioralModel model = null;
            if (seqnum == votatSeq) {
                int newVotat = ((Double) afterTuple.getField(4).getValue()).intValue();
                model = getModel(user, tool, mission, session);
                model.updateVotat(newVotat);

            } else if (seqnum == userExpSeq) {
                int newUserExp = ((Double) afterTuple.getField(4).getValue()).intValue();
                int l =(int) (newUserExp / MAX_EXP_TIME * 100);
                model = getModel(user, tool, mission, session);
                model.updateUserExp(l);

            } else if (seqnum == canoSeq) {
                int newCanonical = ((Double) afterTuple.getField(4).getValue()).intValue();
                model = getModel(user, tool, mission, session);
                model.updateCanonical(newCanonical);

            } else {
                System.err.println("Callback without registered seqnum arrived!");
            }
        }
    }

    private void initLogger() {
        ConsoleHandler cH = new ConsoleHandler();
        SimpleFormatter sF = new SimpleFormatter();
        cH.setFormatter(sF);
        cH.setLevel(DEBUGLEVEL);
        logger.setLevel(DEBUGLEVEL);
        logger.addHandler(cH);
    }

    public BehavioralModel getModel(String user, String tool, String session, String mission) {
        BehavioralModel model = userModels.get(user + "/" + tool + "/" + mission + "/" + session);
        if (model == null) {
            model = new BehavioralModel(name, tool, mission, session, 0, 0, 0, commandSpace);
            logger.log(Level.FINE, "New Model for " + name + " with tool " + tool + " created...");
        }
        return model;

    }

}

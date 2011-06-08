package eu.scy.agents.sensors.behaviourclassifier;

import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Callback;
import info.collide.sqlspaces.commons.Field;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;
import info.collide.sqlspaces.commons.User;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import edu.emory.mathcs.backport.java.util.concurrent.locks.ReentrantLock;
import eu.scy.agents.api.AgentLifecycleException;
import eu.scy.agents.impl.AbstractThreadedAgent;
import eu.scy.agents.impl.AgentProtocol;

public class ScySimBehaviourClassifier extends AbstractThreadedAgent implements Callback {

    private TupleSpace commandSpace;

    private static final Level DEBUGLEVEL = Level.FINE;

    private static final Logger logger = Logger.getLogger(ScySimBehaviourClassifier.class.getName());

    private boolean isStopped;
    
    private ReentrantLock l = new ReentrantLock();

    private static final Tuple USER_EXP_TEMPLATE = new Tuple("user_exp", Field.createWildCardField());

    private static final Tuple VOTAT_TEMPLATE = new Tuple("votat", Field.createWildCardField());

    private static final Tuple CANONICAL_TEMPLATE = new Tuple("inc_change", Field.createWildCardField());

    private static final Tuple EXP_PHASE_STARTET_TEMPLATE = new Tuple("exp_phase_started", Field.createWildCardField());

    private Map<String, BehavioralModel> userModels;

    private int userExpSeq;

    private int votatSeq;

    private int canoSeq;

    private static final double MAX_EXP_TIME = 1 * 60 * 60 * 1000;

//    private int lastVotat;
//
//    private int lastCanonical;
//
//    private int lastUserExp;

    private int expPhaseSeq;

    public ScySimBehaviourClassifier(Map<String, Object> map) {
        super(ScySimBehaviourClassifier.class.getName(), (String) map.get(AgentProtocol.PARAM_AGENT_ID), (String) map.get(AgentProtocol.TS_HOST), (Integer) map.get(AgentProtocol.TS_PORT));
        try {
            commandSpace = new TupleSpace(new User(getSimpleName()), host, port, false, false, AgentProtocol.COMMAND_SPACE_NAME);
            userExpSeq = commandSpace.eventRegister(Command.ALL, USER_EXP_TEMPLATE, this, true);
            votatSeq = commandSpace.eventRegister(Command.ALL, VOTAT_TEMPLATE, this, true);
            canoSeq = commandSpace.eventRegister(Command.ALL, CANONICAL_TEMPLATE, this, true);
            expPhaseSeq = commandSpace.eventRegister(Command.ALL, EXP_PHASE_STARTET_TEMPLATE, this, true);
            userModels = new ConcurrentHashMap<String, BehavioralModel>();
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
        try {
            commandSpace.disconnect();
        } catch (TupleSpaceException e) {
            e.printStackTrace();
        }
        isStopped = true;
    }

    @Override
    protected Tuple getIdentifyTuple(String queryId) {
        // This agent is just a sensor and therefore it don't need to identify....
        return null;
    }

    @Override
    public boolean isStopped() {
        return isStopped;
    }

    private void initLogger() {
        ConsoleHandler cH = new ConsoleHandler();
        SimpleFormatter sF = new SimpleFormatter();
        cH.setFormatter(sF);
        cH.setLevel(DEBUGLEVEL);
        logger.setLevel(DEBUGLEVEL);
        logger.addHandler(cH);
    }

    public  BehavioralModel getModel(String user, String eloUri) {
       l.lock();
        BehavioralModel model = userModels.get(user + "/" + eloUri);
        if (model == null) {
            model = new BehavioralModel(user, eloUri, 1, 1, 0, commandSpace);
            userModels.put(user + "/" + eloUri, model);
            logger.log(Level.FINE, "New Model for " + user + " with tool " + eloUri  + " created...");
        }
        l.unlock();
        return model;

    }

    @Override
    public void call(Command cmd, int seqnum, Tuple afterTuple, Tuple beforeTuple) {
        String user = afterTuple.getField(1).getValue().toString();
        String eloUri = afterTuple.getField(9).getValue().toString();

        BehavioralModel model = null;
        if (seqnum == votatSeq) {
            int newVotat = ((Double) afterTuple.getField(7).getValue()).intValue();
            // if (newVotat != lastVotat) {
            model = getModel(user, eloUri);
            model.updateVotat(newVotat);
            // }
           // lastVotat = newVotat;

        } else if (seqnum == userExpSeq) {
            int newUserExp = ((Long) afterTuple.getField(6).getValue()).intValue();
            // if (newUserExp != lastUserExp) {
            int l = (int) (newUserExp / MAX_EXP_TIME * 100);
            l = Math.min(l, 100);
            model = getModel(user, eloUri);
            model.updateUserExp(l);
            // }
          //  lastUserExp = newUserExp;

        } else if (seqnum == canoSeq) {
            int newCanonical = ((Double) afterTuple.getField(7).getValue()).intValue();
            // if (newCanonical != lastCanonical) {
            model = getModel(user, eloUri);
            model.updateCanonical(newCanonical);
            // }
            //lastCanonical = newCanonical;

        } else if (seqnum == expPhaseSeq) {
            model = getModel(user, eloUri);
            model.setExpPhaseStarted();

        } else {
            System.err.println("Callback without registered seqnum arrived!");
        }
    }

}

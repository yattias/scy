package eu.scy.agents.sensors.userexperience;

import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Callback;
import info.collide.sqlspaces.commons.Field;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;
import info.collide.sqlspaces.commons.User;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.Map.Entry;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.swing.Timer;

import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import eu.scy.actionlogging.Action;
import eu.scy.actionlogging.ActionTupleTransformer;
import eu.scy.actionlogging.ActionXMLTransformer;
import eu.scy.actionlogging.api.ContextConstants;
import eu.scy.agents.api.AgentLifecycleException;
import eu.scy.agents.impl.AbstractThreadedAgent;
import eu.scy.agents.supervisor.SupervisingAgent;

public class ToolExperienceSensor extends AbstractThreadedAgent implements ActionListener {

    // TODO gather that info from tbi?
    private static final String ACTION_SPACE = "actionSpace";

    private static final String SENSOR_SPACE = "sensorSpace";

    private static final String TOOL_ALIVE_SPACE = "toolAliveSpace";

    private static final int UPDATE_INTERVAL = 5 * 1000;

    private TupleSpace actionSpace;

    private TupleSpace sensorSpace;

    private HashMap<String, UserToolExperienceModel> userModels;

    private List<Integer> callbacks;

    private static final Level DEBUGLEVEL = Level.FINE;

    private static final Logger logger = Logger.getLogger(SupervisingAgent.class.getName());

    private Timer timer;

    private boolean initializing;

    private boolean isStopped;

    private List<Action> actionQueue;

    private TupleSpace toolAliveSpace;

    public ToolExperienceSensor(Map<String, Object> map) {
        super("eu.scy.agents.serviceprovider.userexperience.ToolExperienceSensor", (String) map.get("id"), "localhost", 2525);
        try {
            sensorSpace = new TupleSpace(new User(getName()), host, port, false, false, SENSOR_SPACE);
            actionSpace = new TupleSpace(new User(getName()), host, port, false, false, ACTION_SPACE);
            toolAliveSpace = new TupleSpace(new User(getName()), host, port, false, false, TOOL_ALIVE_SPACE);
            init();
        } catch (TupleSpaceException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void init() throws TupleSpaceException, ParseException, DocumentException, IOException {
        initLogger();
        actionQueue = new Vector<Action>();
        userModels = new HashMap<String, UserToolExperienceModel>();
        callbacks = new ArrayList<Integer>();
        startedInitialization();
        SessionCallback cb = new SessionCallback();
        Tuple tupleTemplate = new Tuple("action", Field.createWildCardField());
        callbacks.add(actionSpace.eventRegister(Command.WRITE, tupleTemplate, cb, false));
        rebuildFromSpace();
        finishedInitialization();
        timer = new Timer(UPDATE_INTERVAL, this);
        timer.setRepeats(true);
        timer.start();
    }

    private synchronized void rebuildFromSpace() throws TupleSpaceException, DocumentException, ParseException, IOException {

        Tuple[] userExpTuples = sensorSpace.readAll(new Tuple("user_exp", Field.createWildCardField()));
        long lastActionTime = 0L;
        for (Tuple tuple : userExpTuples) {
            String userName = (String) tuple.getField(1).getValue();
            String tool = (String) tuple.getField(3).getValue();
            long expTime = (Long) tuple.getField(4).getValue();
            int starts = (Integer) tuple.getField(5).getValue();
            int stops = (Integer) tuple.getField(6).getValue();

            if (starts > stops) {
                logger.log(Level.WARNING, "It seemed that the tool " + tool + " of the user " + userName + " crashed the last time...try to fix that!");
                stops = starts;
            }

            long lastModificationTimestamp = tuple.getLastModificationTimestamp();
            if (lastModificationTimestamp > lastActionTime) {
                lastActionTime = lastModificationTimestamp;
            }
            UserToolExperienceModel model = userModels.get(userName);
            if (model == null) {
                model = new UserToolExperienceModel(userName, sensorSpace, toolAliveSpace, starts, stops);
                userModels.put(userName, model);
            }
            model.setToolTime(tool, expTime);
            model.setToolTID(tool, tuple.getTupleID());
        }

        Field timeStampField = new Field(long.class);
        timeStampField.setLowerBound(lastActionTime + 1l);
        Tuple tupleTemplate = new Tuple(new Field(String.class), timeStampField, new Field(String.class), new Field(String.class), new Field(String.class), new Field(String.class), new Field(String.class), new Field(String.class));
        Tuple[] actionTuples = actionSpace.readAll(tupleTemplate);
        logger.log(Level.FINE, "Have to process: " + actionTuples.length + " tuples.");
        Arrays.sort(actionTuples, new Comparator<Tuple>() {

            @Override
            public int compare(Tuple o1, Tuple o2) {
                if (o1.getCreationTimestamp() > o2.getCreationTimestamp()) {
                    return 1;
                } else if (o1.getCreationTimestamp() < o2.getCreationTimestamp()) {
                    return -1;
                }
                return 0;
            }
        });

        for (Tuple tuple : actionTuples) {
            Element actionElement = DocumentHelper.parseText((String) tuple.getField(7).getValue()).getRootElement();
            Action action = (Action) new ActionXMLTransformer(actionElement).getActionAsPojo();
            processAction(action, false);
        }
        for (UserToolExperienceModel m : userModels.values()) {
            logger.log(Level.FINE, "Now after rebuilding:");
            Set<String> toolsList = m.getToolsList();
            for (String string : toolsList) {

                logger.log(Level.FINE, m.getUserName() + " has exp in Tool: " + string + " of " + m.getExperience(string));
            }

        }

    }

    private void startedInitialization() {
        initializing = true;
    }

    private void finishedInitialization() throws ParseException, TupleSpaceException, IOException {
        while (!actionQueue.isEmpty()) {
            processAction(actionQueue.remove(0), false);
        }
        initializing = false;
    }

    private synchronized void processAction(Action a, boolean queued) throws ParseException, TupleSpaceException, IOException {
        logger.log(Level.FINE, "Action to process occured, queued-flag is set to " + queued);
        if (!initializing && queued) {
            actionQueue.add(a);
            return;
        }
        if (a.getType().equals("tool started")) {
            String sessionid = a.getContext(ContextConstants.session);
            UserToolExperienceModel userModel = userModels.get(a.getUser());
            if (userModel == null) {
                userModel = new UserToolExperienceModel(a.getUser(), sensorSpace, toolAliveSpace, 1, 0);
                userModels.put(a.getUser(), userModel);
                logger.log(Level.FINE, "new usermodel for " + a.getUser() + " created");
            } else {
                userModel.setStarts(userModel.getStarts() + 1);
            }
            if (userModel.getStarts() - 1 > userModel.getStops()) {
                logger.log(Level.WARNING, "[processAction]It seemed that the tool " + a.getContext(ContextConstants.tool) + " of the user " + a.getUser() + " crashed the last time...try to fix that!");
                userModel.setStarts(userModel.getStops());
            }
            logger.log(Level.FINE, "Tool started with user: " + a.getUser() + " and SessionID: " + sessionid);
        } else if (a.getType().equals("tool stopped")) {
            String sessionid = a.getContext(ContextConstants.session);
            UserToolExperienceModel exp = userModels.get(a.getUser());
            exp.setToolInactive(a.getContext(ContextConstants.tool), a.getTimeInMillis(), true);
            logger.log(Level.FINE, "Tool stopped with user: " + a.getUser() + " and SessionID: " + sessionid);

        } else if (a.getType().equals("focus gained")) {
            String sessionid = a.getContext(ContextConstants.session);
            long focusTime = a.getTimeInMillis();
            UserToolExperienceModel exp = userModels.get(a.getUser());
            exp.setActiveTool(a.getContext(ContextConstants.tool), focusTime, false);
            logger.log(Level.FINE, "Focus gained with user: " + a.getUser() + " and SessionID: " + sessionid);
        } else if (a.getType().equals("focus lost")) {
            String sessionid = a.getContext(ContextConstants.session);
            long focusEndTime = a.getTimeInMillis();
            UserToolExperienceModel exp = userModels.get(a.getUser());
            exp.setToolInactive(a.getContext(ContextConstants.tool), focusEndTime, false);
            logger.log(Level.FINE, "Focus lost with user: " + a.getUser() + " and SessionID: " + sessionid);
        }
    }

    @Override
    protected void doRun() throws TupleSpaceException, AgentLifecycleException {

        while (status == Status.Running) {
            sendAliveUpdate();
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    protected void doStop() {
        status = Status.Stopping;
        for (Integer cbSeq : callbacks) {
            try {
                actionSpace.eventDeRegister(cbSeq);
                actionSpace.disconnect();
                sensorSpace.disconnect();
            } catch (TupleSpaceException e) {
                e.printStackTrace();
            }

        }

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
            Element element;

            Action a = (Action) new ActionTupleTransformer(afterTuple).getActionAsPojo();
            try {
                processAction(a, false);
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (TupleSpaceException e) {
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        for (Entry<String, UserToolExperienceModel> userModel : userModels.entrySet()) {
            UserToolExperienceModel user = userModel.getValue();
            user.updateActiveToolExperience(UPDATE_INTERVAL, System.currentTimeMillis());
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

}

package eu.scy.agents.serviceprovider.userexperience;

import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Callback;
import info.collide.sqlspaces.commons.Field;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;
import info.collide.sqlspaces.commons.User;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import eu.scy.actionlogging.ActionXMLTransformer;
import eu.scy.actionlogging.TimeFormatHelper;
import eu.scy.actionlogging.api.ContextConstants;
import eu.scy.agents.api.AgentLifecycleException;
import eu.scy.agents.impl.AbstractThreadedAgent;
import eu.scy.agents.supervisor.SupervisingAgent;

public class ToolExperienceSensor extends AbstractThreadedAgent implements ActionListener {

    private static final String ACTION_SPACE = "actionSpace";

    private static final String SENSOR_SPACE = "sensorSpace";

    private static final int UPDATE_INTERVAL = 5 * 1000;

    private TupleSpace actionSpace;

    private TupleSpace sensorSpace;

    private HashMap<String, UserToolExperienceModel> userModels;

    private HashMap<String, Long> userOnlineTimeMap;

    private HashMap<String, String> sessionUserMap;

    private HashMap<String, Long> sessionWithFocusMap;

    private List<Integer> callbacks;

    private static final Level DEBUGLEVEL = Level.FINE;

    private static final Logger logger = Logger.getLogger(SupervisingAgent.class.getName());

    private Timer timer;

    private boolean initializing;

    private boolean isStopped;

    private List<Action> actionQueue;

    private long processActionAttempts;


    public ToolExperienceSensor(Map<String, Object> map) {
        super("eu.scy.agents.serviceprovider.userexperience.ToolExperienceServiceProvider", (String) map.get("id"), "localhost", 2525);
        processActionAttempts = 0L;
        try {
            sensorSpace = new TupleSpace(new User(getName()), host, port, false, false, SENSOR_SPACE);
            actionSpace = new TupleSpace(new User(getName()), host, port, false, false, ACTION_SPACE);
            init();
        } catch (TupleSpaceException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }

    }

    private void init() throws TupleSpaceException, ParseException, DocumentException {
        initLogger();
        actionQueue = new Vector<Action>();
        userModels = new HashMap<String, UserToolExperienceModel>();
        userOnlineTimeMap = new HashMap<String, Long>();
        sessionUserMap = new HashMap<String, String>();
        sessionWithFocusMap = new HashMap<String, Long>();
        callbacks = new ArrayList<Integer>();
        startedInitialization();
        SessionCallback cb = new SessionCallback();
        Tuple tupleTemplate = new Tuple(String.class, long.class, String.class, String.class, String.class,String.class, String.class, String.class);
        callbacks.add(actionSpace.eventRegister(Command.WRITE, tupleTemplate, cb, false));
        rebuildFromSpace();
        finishedInitialization();
        timer = new Timer(UPDATE_INTERVAL, this);
        timer.setRepeats(true);
        timer.start();
    }

    private synchronized void rebuildFromSpace() throws TupleSpaceException, DocumentException, ParseException {

        Tuple[] userExpTuples = sensorSpace.readAll(new Tuple("user_exp", Field.createWildCardField()));
        long lastActionTime = 0L;
        for (Tuple tuple : userExpTuples) {
            String userName = (String) tuple.getField(1).getValue();
            // long lastAction = (Long) tuple.getField(2).getValue();
            String tool = (String) tuple.getField(3).getValue();
            long expTime = (Long) tuple.getField(4).getValue();
            long lastModificationTimestamp = tuple.getLastModificationTimestamp();
            if (lastModificationTimestamp > lastActionTime) {
                lastActionTime = lastModificationTimestamp;
            }
            UserToolExperienceModel model = userModels.get(userName);
            if (model == null) {
                model = new UserToolExperienceModel(userName, sensorSpace);
                userModels.put(userName, model);
            }
            model.setToolTime(tool, expTime);
            model.setToolTID(tool, tuple.getTupleID());
        }

        Field timeStampField = new Field(long.class);
        timeStampField.setLowerBound(lastActionTime + 1l);
        Tuple tupleTemplate = new Tuple(new Field(String.class), timeStampField, new Field(String.class), new Field(String.class), new Field(String.class), new Field(String.class), new Field(String.class), new Field(String.class));
        Tuple[] actionTuples = actionSpace.readAll(tupleTemplate);
        System.out.println("Have to process: " + actionTuples.length + " Tuples.");
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
            System.out.println("Now after rebuilding:");
            Set<String> toolsList = m.getToolsList();
            for (String string : toolsList) {

                System.out.println(m.getUserName() + " has exp in Tool: " + string + " of " + m.getExperience(string));
            }

        }

    }

    private void startedInitialization() {
        initializing = true;
    }

    private void finishedInitialization() throws ParseException {
        while (!actionQueue.isEmpty()) {
            processAction(actionQueue.remove(0), false);
        }
        initializing = false;
    }

    private synchronized void processAction(Action a, boolean queued) throws ParseException {
        processActionAttempts++;
        System.out.println("Process Action Attempts: " + processActionAttempts);
        logger.log(Level.FINE, "Action to process occured, queed-flag is set to " + queued);
        if (!initializing && queued) {
            actionQueue.add(a);
            return;
        }
        if (a.getType().equals("tool started")) {
            String sessionid = a.getContext(ContextConstants.session);
            String user = a.getUser();
            UserToolExperienceModel userModel = userModels.get(a.getUser());
            if (userModel == null) {
                userModel = new UserToolExperienceModel(a.getUser(), sensorSpace);
                userModels.put(a.getUser(), userModel);
                System.out.println("new usermodel for " + a.getUser() + " created");
            }
            sessionUserMap.put(sessionid, user);
            System.out.println("Tool started with user: " + a.getUser() + "and SessionID: " + sessionid);
        } else if (a.getType().equals("tool stopped")) {
            String sessionid = a.getContext(ContextConstants.session);
            sessionUserMap.remove(sessionid);
            UserToolExperienceModel exp = userModels.get(a.getUser());
            exp.setToolInactive(a.getContext(ContextConstants.tool), TimeFormatHelper.getInstance().getISO8601AsLong(a.getTime()));
            System.out.println("Tool stopped with user: " + a.getUser() + "and SessionID: " + sessionid);

        } else if (a.getType().equals("focus gained")) {
            String sessionid = a.getContext(ContextConstants.session);
            long focusTime = TimeFormatHelper.getInstance().getISO8601AsLong(a.getTime());
            sessionWithFocusMap.put(sessionid, focusTime);
            UserToolExperienceModel exp = userModels.get(a.getUser());
            exp.setActiveTool(a.getContext(ContextConstants.tool), focusTime);
            System.out.println("Focus gained with user: " + a.getUser() + "and SessionID: " + sessionid);
        } else if (a.getType().equals("focus lost")) {
            String sessionid = a.getContext(ContextConstants.session);
            // TODO fix
            long focusStartTime = sessionWithFocusMap.remove(sessionid);
            long focusEndTime = TimeFormatHelper.getInstance().getISO8601AsLong(a.getTime());
            Long onlineTime = userOnlineTimeMap.get(a.getUser());
            if (onlineTime == null) {
                onlineTime = 0L;
            }
            onlineTime += focusEndTime - focusStartTime;
            userOnlineTimeMap.put(a.getUser(), onlineTime);
            UserToolExperienceModel exp = userModels.get(a.getUser());
            exp.setToolInactive(a.getContext(ContextConstants.tool), focusEndTime);
            System.out.println("Focus lost with user: " + a.getUser() + "and SessionID: " + sessionid);
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
        // TODO Insert ident here
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
            try {
                element = DocumentHelper.parseText((String) afterTuple.getField(7).getValue()).getRootElement();
                Action a = (Action) new ActionXMLTransformer(element).getActionAsPojo();
                try {
                    processAction(a, false);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } catch (DocumentException e1) {
                e1.printStackTrace();
            }
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // logger.log(Level.FINE, "actionPerformed");
        HashMap<String, UserToolExperienceModel> clone = (HashMap<String, UserToolExperienceModel>) userModels.clone();
        for (Entry<String, UserToolExperienceModel> userModel : clone.entrySet()) {
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

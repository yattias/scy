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
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.Timer;

import org.dom4j.DocumentException;

import eu.scy.actionlogging.Action;
import eu.scy.actionlogging.ActionTupleTransformer;
import eu.scy.actionlogging.Context;
import eu.scy.actionlogging.api.ContextConstants;
import eu.scy.actionlogging.api.IContext;
import eu.scy.agents.api.AgentLifecycleException;
import eu.scy.agents.impl.AbstractThreadedAgent;
import eu.scy.agents.impl.AgentProtocol;

public class ToolExperienceSensor extends AbstractThreadedAgent implements ActionListener {

    private static final int UPDATE_INTERVAL = 5 * 1000;

    private static final boolean SHOULD_UPDATE = true;

    private TupleSpace actionSpace;

    private TupleSpace commandSpace;

    private HashMap<String, UserToolExperienceModel> userModels;

    private List<Integer> callbacks;

    // private static final Level DEBUGLEVEL = Level.FINE;

    private static final Logger logger = Logger.getLogger(ToolExperienceSensor.class.getName());

    private Timer timer;

    private boolean initializing;

    private boolean isStopped;

    private List<Action> actionQueue;
    

    public ToolExperienceSensor(Map<String, Object> map) {
        super(ToolExperienceSensor.class.getName(), (String) map.get(AgentProtocol.PARAM_AGENT_ID), (String) map.get(AgentProtocol.TS_HOST), (Integer) map.get(AgentProtocol.TS_PORT));
        try {
            commandSpace = new TupleSpace(new User(getSimpleName()), host, port, false, false, AgentProtocol.COMMAND_SPACE_NAME);
            actionSpace = new TupleSpace(new User(getSimpleName()), host, port, false, false, AgentProtocol.ACTION_SPACE_NAME);
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
        actionQueue = new Vector<Action>();
        userModels = new HashMap<String, UserToolExperienceModel>();
        callbacks = new ArrayList<Integer>();
        startedInitialization();
        SessionCallback cb = new SessionCallback();
        Tuple tupleTemplate = new Tuple("action", Field.createWildCardField());
        callbacks.add(actionSpace.eventRegister(Command.WRITE, tupleTemplate, cb, false));
        rebuildFromSpace();
        finishedInitialization();
        if (SHOULD_UPDATE) {
            timer = new Timer(UPDATE_INTERVAL, this);
            timer.setRepeats(true);
            timer.start();
        }  
    }

    private synchronized void rebuildFromSpace() throws TupleSpaceException, DocumentException, ParseException, IOException {

        Tuple[] userExpTuples = commandSpace.readAll(new Tuple("user_exp", Field.createWildCardField()));
        long lastActionTime = 0L;
        for (Tuple tuple : userExpTuples) {
            String userName = (String) tuple.getField(1).getValue();
            String tool = (String) tuple.getField(2).getValue();
            String eloUri = (String) tuple.getField(5).getValue();
            long expTime = (Long) tuple.getField(7).getValue();
            int starts = (Integer) tuple.getField(8).getValue();
            int stops = (Integer) tuple.getField(9).getValue();

            if (starts > stops) {
                logger.log(Level.FINE, "It seemed that the tool " + tool + " of the user " + userName + " crashed the last time...try to fix that!");
                stops = starts;
            }

            long lastModificationTimestamp = tuple.getLastModificationTimestamp();
            if (lastModificationTimestamp > lastActionTime) {
                lastActionTime = lastModificationTimestamp;
            }
            UserToolExperienceModel model = userModels.get(userName+tool);
            if (model == null) {
                IContext context = new Context(tool,"n/a","n/a",eloUri);
                
                model = new UserToolExperienceModel(userName,context, commandSpace, starts, stops);
                userModels.put(userName+eloUri, model);
            }
            model.setToolTime(tool, expTime);
            model.setToolTID(tool, tuple.getTupleID());
        }

        Field timeStampField = new Field(long.class);
        timeStampField.setLowerBound(lastActionTime + 1l);
        Tuple tupleTemplate = new Tuple("action", new Field(String.class), timeStampField, Field.createWildCardField());
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
            Action action = (Action) ActionTupleTransformer.getActionFromTuple(tuple);
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
        if (!a.getContext(ContextConstants.tool).equals("simulator")){
            return;
        }
        if (!initializing && queued) {
            actionQueue.add(a);
            return;
        }
        if (a.getType().equals("tool_opened")) {
            String sessionid = a.getContext(ContextConstants.session);
            UserToolExperienceModel userModel = userModels.get(a.getUser()+a.getContext(ContextConstants.eloURI));
            if (userModel == null) {
                userModel = new UserToolExperienceModel(a.getUser(), a.getContext(), commandSpace, 1, 0);
                userModels.put(a.getUser()+a.getContext(ContextConstants.eloURI), userModel);
                logger.log(Level.FINE, "new usermodel for " + a.getUser() + " created");
            } else {
                userModel.setStarts(userModel.getStarts() + 1);
            }
            if (userModel.getStarts() - 1 > userModel.getStops()) {
                logger.log(Level.WARNING, "[processAction]It seemed that the tool " + a.getContext(ContextConstants.tool) + " of the user " + a.getUser() + " crashed the last time...try to fix that!");
                userModel.setStarts(userModel.getStops());
            }
            logger.log(Level.FINE, "Tool started with user: " + a.getUser() + " and SessionID: " + sessionid);
//          if (!initializing){
//              timer.start();
//          }
        } else if (a.getType().equals("tool_closed")) {
            String sessionid = a.getContext(ContextConstants.session);
            UserToolExperienceModel exp = userModels.get(a.getUser()+a.getContext(ContextConstants.eloURI));
            exp.setToolInactive(a.getContext(ContextConstants.tool), a.getTimeInMillis(), true);
            logger.log(Level.FINE, "Tool stopped with user: " + a.getUser() + " and SessionID: " + sessionid);
//            if (!initializing){
//                timer.stop();
//            }

        } else if (a.getType().equals("tool_got_focus")) {
            String sessionid = a.getContext(ContextConstants.session);
            long focusTime = a.getTimeInMillis();
            UserToolExperienceModel exp = userModels.get(a.getUser()+a.getContext(ContextConstants.eloURI));
            if (exp == null) {
                exp = new UserToolExperienceModel(a.getUser(), a.getContext(),commandSpace, 1, 0);
                userModels.put(a.getUser()+a.getContext(ContextConstants.eloURI), exp);
                logger.log(Level.FINE, "new usermodel for " + a.getUser() + " created");
            }
            exp.setActiveTool(a.getContext(ContextConstants.tool), focusTime, false);
            logger.log(Level.FINE, "Focus gained with user: " + a.getUser() + " and SessionID: " + sessionid);
//            if (!initializing){
//                timer.start();
//            }
        } else if (a.getType().equals("tool_lost_focus")) {
            String sessionid = a.getContext(ContextConstants.session);
            long focusEndTime = a.getTimeInMillis();
            UserToolExperienceModel exp = userModels.get(a.getUser()+a.getContext(ContextConstants.eloURI));
           if (exp==null){
               //Q&D hack 4 review
               return;
           }
            exp.setToolInactive(a.getContext(ContextConstants.tool), focusEndTime, false);
            logger.log(Level.FINE, "Focus lost with user: " + a.getUser() + " and SessionID: " + sessionid);
//            if (!initializing){
//                timer.stop();
//            }
        } else if (a.getType().equals("add_row")) {
            String sessionid = a.getContext(ContextConstants.session);
            UserToolExperienceModel exp = userModels.get(a.getUser()+a.getContext(ContextConstants.eloURI));
            exp.startExpPhase();
            logger.log(Level.FINE, "startExpPhase with user: " + a.getUser() + " and SessionID: " + sessionid);
//            if (!initializing){
//                timer.stop();
//            }
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
        try {
            for (Integer cbSeq : callbacks) {
                actionSpace.eventDeRegister(cbSeq);
            }
            timer.stop();
            actionSpace.disconnect();
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

    class SessionCallback implements Callback {

        @Override
        public void call(Command cmd, int seqnum, Tuple afterTuple, Tuple beforeTuple) {
            if (afterTuple==null){
                return;
            }
            Action a = (Action) ActionTupleTransformer.getActionFromTuple(afterTuple);
            try {
                processAction(a, false);
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (TupleSpaceException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //Cloning to prevent ConcurrentModificationExceptions
        Set<Entry<String, UserToolExperienceModel>> clonedEntrySet = userModels.entrySet();
        for (Entry<String, UserToolExperienceModel> userModel : clonedEntrySet) {
            UserToolExperienceModel user = userModel.getValue();
            user.updateActiveToolExperience(UPDATE_INTERVAL, System.currentTimeMillis());
        }
    }

}

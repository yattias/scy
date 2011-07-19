package eu.scy.agents.authoring.workflow;

import eu.scy.actionlogging.ActionTupleTransformer;
import eu.scy.actionlogging.api.ContextConstants;
import eu.scy.actionlogging.api.IAction;
import eu.scy.agents.api.AgentLifecycleException;
import eu.scy.agents.api.IRepositoryAgent;
import eu.scy.agents.authoring.workflow.paths.Path;
import eu.scy.agents.authoring.workflow.paths.PathAnalyzer;
import eu.scy.agents.authoring.workflow.paths.TimeExcess;
import eu.scy.agents.impl.AbstractThreadedAgent;
import eu.scy.agents.impl.ActionConstants;
import eu.scy.agents.impl.AgentProtocol;
import eu.scy.agents.impl.AgentRooloServiceImpl;
import eu.scy.agents.util.time.DefaultTimer;
import eu.scy.agents.util.time.Duration;
import eu.scy.agents.util.time.Timer;
import info.collide.sqlspaces.commons.Field;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;
import roolo.api.IRepository;
import roolo.elo.api.IMetadataTypeManager;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class WorkflowRecordingAgent extends AbstractThreadedAgent implements
        IRepositoryAgent {

    static String NAME = WorkflowRecordingAgent.class.getName();

    public static final String ALARM_EXCESS_DURATION = "AlarmExcessDuration";
    private static final String LAS = "newLasId";
    private static final String OLD_LAS = "oldLasId";
    private static final String TIMER = "Timer";
    private static final String CHECK_PERIOD = "CheckPeriod";

    private int checkPeriod = 10;

    private int listenerId;

    private Map<String, String> mission2workflows;
    private Map<String, Workflow> workflows;

    private AgentRooloServiceImpl rooloService;

    private Map<String, Path> paths;

    private int logoutListenerId;
    private Timer timer;

    public WorkflowRecordingAgent(Map<String, Object> params) {
        super(NAME, (String) params.get(AgentProtocol.PARAM_AGENT_ID));
        if ( params.containsKey(AgentProtocol.TS_HOST) ) {
            host = (String) params.get(AgentProtocol.TS_HOST);
        }
        if ( params.containsKey(AgentProtocol.TS_PORT) ) {
            port = (Integer) params.get(AgentProtocol.TS_PORT);
        }
        if ( params.containsKey(TIMER) ) {
            timer = (Timer) params.get(TIMER);
        } else {
            timer = new DefaultTimer();
        }
        if ( params.containsKey(CHECK_PERIOD) ) {
            checkPeriod = (Integer) params.get(CHECK_PERIOD);
        }


        mission2workflows = new ConcurrentHashMap<String, String>();
        workflows = new ConcurrentHashMap<String, Workflow>();
        paths = new ConcurrentHashMap<String, Path>();


        rooloService = new AgentRooloServiceImpl();

        try {
            listenerId = getCommandSpace().eventRegister(
                    Command.WRITE,
                    new Tuple(ActionConstants.ACTION, String.class, Long.class,
                            ActionConstants.ACTION_LAS_CHANGED, Field
                            .createWildCardField()), this, true);

            logoutListenerId = getCommandSpace().eventRegister(
                    Command.WRITE,
                    new Tuple(ActionConstants.ACTION, String.class, Long.class,
                            ActionConstants.ACTION_LOG_OUT, Field
                            .createWildCardField()), this, true);
        } catch ( TupleSpaceException e ) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doRun() throws TupleSpaceException, AgentLifecycleException,
            InterruptedException {

        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleAtFixedRate(new TimeConstraintsChecker(this), 0, checkPeriod, TimeUnit.MINUTES);

        while ( status == Status.Running ) {
            sendAliveUpdate();
            Thread.sleep(AgentProtocol.ALIVE_INTERVAL / 2);
        }

        service.shutdownNow();
    }

    @Override
    protected void doStop() throws AgentLifecycleException {
        status = Status.Stopping;
    }

    @Override
    protected Tuple getIdentifyTuple(String queryId) {
        return null;
    }

    @Override
    public boolean isStopped() {
        return status == Status.Stopping;
    }

    @Override
    public void call(Command command, int seq, Tuple afterTuple,
                     Tuple beforeTuple) {
        if ( this.listenerId == seq ) {
            IAction action = ActionTupleTransformer.getActionFromTuple(afterTuple);
            // String oldLas = action.getAttribute(OLD_LAS);
            long timeStamp = action.getTimeInMillis();
            String las = action.getAttribute(LAS);
            String mission = action.getContext(ContextConstants.mission);
            String user = action.getUser();

            Workflow workflow = getWorkflow(mission);
            if ( workflow == null ) {
                logger.warn("could not get workflow for " + mission);
                return;
            }
            WorkflowItem item = workflow.getItem(las);
            Path path = getPath(user);
            path.addPathComponent(item, timeStamp);
        } else if ( logoutListenerId == seq ) {
            IAction action = ActionTupleTransformer
                    .getActionFromTuple(afterTuple);
            long timeStamp = action.getTimeInMillis();
            Path path = getPath(action.getUser());
            path.stopTiming(timeStamp);
        } else {
            logger.debug("Callback passed to Superclass.");
            super.call(command, seq, afterTuple, beforeTuple);
            return;
        }
    }

    private synchronized Path getPath(String user) {
        Path path = paths.get(user);
        if ( path == null ) {
            path = new Path();
            path.setTimer(timer);
            paths.put(user, path);
        }
        return path;
    }

    private synchronized Workflow getWorkflow(String mission) {
        String workflowName = mission2workflows.get(mission);
        if ( workflowName == null ) {
            WorkflowCreator workflowCreator = new WorkflowCreator(rooloService);
            try {
                Workflow workflow = workflowCreator.createWorkflow(new URI(mission));
                workflowName = workflow.getName();
                workflows.put(workflowName, workflow);
                mission2workflows.put(mission, workflowName);
            } catch ( URISyntaxException e ) {
                e.printStackTrace();
            }
        }
        Workflow workflow = workflows.get(workflowName);
        return workflow;
    }

    @Override
    public void setRepository(IRepository rep) {
        rooloService.setRepository(rep);
    }

    @Override
    public void setMetadataTypeManager(IMetadataTypeManager manager) {
        rooloService.setMetadataTypeManager(manager);
    }

    private static class TimeConstraintsChecker implements Runnable {

        private Integer alarmExcessDuration;
        private WorkflowRecordingAgent agent;

        TimeConstraintsChecker(WorkflowRecordingAgent agent) {
            this.agent = agent;
        }

        @Override
        public void run() {
            alarmExcessDuration = agent.configuration.getParameter(ALARM_EXCESS_DURATION);
            if ( alarmExcessDuration == null ) {
                alarmExcessDuration = 0;
            }
            for ( Workflow workflow : agent.workflows.values() ) {
                PathAnalyzer analyzer = new PathAnalyzer(workflow);
                for ( String user : agent.paths.keySet() ) {
                    Path path = agent.paths.get(user);
                    List<TimeExcess> timeExcesses = analyzer.getTimeExcesses(path);
                    if ( !timeExcesses.isEmpty() ) {
                        List<Tuple> report = generateReport(user, timeExcesses);
                        sendReport(report);
                    }
                }
            }
        }

        private List<Tuple> generateReport(String user, List<TimeExcess> timeExcesses) {
            List<Tuple> reportTuples = new ArrayList<Tuple>();
            for ( TimeExcess timeExcess : timeExcesses ) {
                if ( timeExcess.getTimeExcess().greater(Duration.fromMinutes(alarmExcessDuration)) ) {
                    reportTuples.add(new Tuple("time_excess", user, timeExcess.getItem().getId(), timeExcess.getTimeExcess().toMinutes()));
                }
            }
            return reportTuples;
        }

        private void sendReport(List<Tuple> report) {
            for ( Tuple tuple : report ) {
                try {
                    agent.getSessionSpace().write(tuple);
                } catch ( TupleSpaceException e ) {
                    e.printStackTrace();
                }
            }
        }
    }
}

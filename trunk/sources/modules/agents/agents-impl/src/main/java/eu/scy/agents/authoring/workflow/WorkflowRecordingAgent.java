package eu.scy.agents.authoring.workflow;

import eu.scy.actionlogging.ActionTupleTransformer;
import eu.scy.actionlogging.api.ContextConstants;
import eu.scy.actionlogging.api.IAction;
import eu.scy.agents.api.AgentLifecycleException;
import eu.scy.agents.api.IRepositoryAgent;
import eu.scy.agents.authoring.workflow.paths.Path;
import eu.scy.agents.impl.AbstractThreadedAgent;
import eu.scy.agents.impl.ActionConstants;
import eu.scy.agents.impl.AgentProtocol;
import eu.scy.agents.impl.AgentRooloServiceImpl;
import info.collide.sqlspaces.commons.Field;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;
import roolo.api.IRepository;
import roolo.elo.api.IMetadataTypeManager;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WorkflowRecordingAgent extends AbstractThreadedAgent implements
        IRepositoryAgent {

    static String NAME = WorkflowRecordingAgent.class.getName();

    private static final String LAS = "newLasId";
    private static final String OLD_LAS = "oldLasId";

    private int listenerId;

    private Map<String, String> mission2workflows;
    private Map<String, Workflow> workflows;

    private AgentRooloServiceImpl rooloService;

    private Map<String, Path> paths;

    private int logoutListenerId;

    public WorkflowRecordingAgent(Map<String, Object> params) {
        super(NAME, (String) params.get(AgentProtocol.PARAM_AGENT_ID));
        if (params.containsKey(AgentProtocol.TS_HOST)) {
            host = (String) params.get(AgentProtocol.TS_HOST);
        }
        if (params.containsKey(AgentProtocol.TS_PORT)) {
            port = (Integer) params.get(AgentProtocol.TS_PORT);
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
        } catch (TupleSpaceException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doRun() throws TupleSpaceException, AgentLifecycleException,
            InterruptedException {
        while (status == Status.Running) {
            // check time constraints here
            // set time > plan == notification as parameter
            sendAliveUpdate();
            Thread.sleep(AgentProtocol.ALIVE_INTERVAL / 3);
        }
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
        if (this.listenerId == seq) {
            IAction action = ActionTupleTransformer.getActionFromTuple(afterTuple);
            // String oldLas = action.getAttribute(OLD_LAS);
            String las = action.getAttribute(LAS);
            String mission = action.getContext(ContextConstants.mission);
            String user = action.getUser();

            Workflow workflow = getWorkflow(mission);
            if (workflow == null) {
                logger.warn("could not get workflow for " + mission);
                return;
            }
            WorkflowItem item = workflow.getItem(las);
            Path path = getPath(user);
            path.addPathComponent(item);
        } else if (logoutListenerId == seq) {
            IAction action = ActionTupleTransformer
                    .getActionFromTuple(afterTuple);
            Path path = getPath(action.getUser());
            path.stopTiming();
        } else {
            logger.debug("Callback passed to Superclass.");
            super.call(command, seq, afterTuple, beforeTuple);
            return;
        }

    }

    private synchronized Path getPath(String user) {
        Path path = paths.get(user);
        if (path == null) {
            path = new Path();
            paths.put(user, path);
        }
        return path;
    }

    private synchronized Workflow getWorkflow(String mission) {
        String workflowName = mission2workflows.get(mission);
        if (workflowName == null) {
            WorkflowCreator workflowCreator = new WorkflowCreator(rooloService);
            try {
                Workflow workflow = workflowCreator.createWorkflow(new URI(mission));
                workflowName = workflow.getName();
                workflows.put(workflowName, workflow);
                mission2workflows.put(mission, workflowName);
            } catch (URISyntaxException e) {
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
}

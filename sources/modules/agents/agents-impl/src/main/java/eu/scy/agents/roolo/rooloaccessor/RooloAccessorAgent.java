package eu.scy.agents.roolo.rooloaccessor;

import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Callback;
import info.collide.sqlspaces.commons.Field;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;
import info.collide.sqlspaces.commons.User;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import org.apache.log4j.Logger;

import roolo.api.IRepository;
import roolo.elo.api.IELO;
import roolo.elo.api.IMetadata;
import roolo.elo.api.IMetadataTypeManager;
import eu.scy.agents.api.AgentLifecycleException;
import eu.scy.agents.api.IRepositoryAgent;
import eu.scy.agents.impl.AbstractThreadedAgent;
import eu.scy.agents.impl.AgentProtocol;

public class RooloAccessorAgent extends AbstractThreadedAgent implements IRepositoryAgent {

    private static final String ROOLO_RESPONSE = "roolo-response";

    private static final Logger logger = Logger.getLogger(RooloAccessorAgent.class.getName());

    private static final String AGENT_NAME = "roolo-agent";

    private boolean isStopped;

    private TupleSpace commandSpace;

    private IRepository repo;

    private IMetadataTypeManager manager;

    protected RooloAccessorAgent(Map<String, Object> map) {
        super(RooloAccessorAgent.class.getName(), (String) map.get(AgentProtocol.PARAM_AGENT_ID), (String) map.get(AgentProtocol.TS_HOST), (Integer) map.get(AgentProtocol.TS_PORT));
        try {
            commandSpace = new TupleSpace(new User(getSimpleName()), host, port, false, false, AgentProtocol.COMMAND_SPACE_NAME);
            Callback arc = new AccessRooloCallback();
            commandSpace.eventRegister(Command.WRITE, new Tuple(String.class, AGENT_NAME, String.class, Field.createWildCardField()), arc, false);
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
    protected void doStop() throws AgentLifecycleException {
        try {
            commandSpace.disconnect();
        } catch (TupleSpaceException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Tuple getIdentifyTuple(String arg0) {
        return null;
    }

    @Override
    public boolean isStopped() {
        return isStopped;
    }

    class AccessRooloCallback implements Callback {

        private static final String ELO = "elo";

        private static final String METADATA = "metadata";

        @Override
        public void call(Command cmd, int seqnum, Tuple afterTuple, Tuple beforeTuple) {
            try {
                String requestUID = afterTuple.getField(0).getValue().toString();
                String type = afterTuple.getField(2).getValue().toString();
                if (requestUID == null || requestUID.isEmpty()) {
                    logger.debug("UID of request is null or empty.");
                }
                if (type == null || type.isEmpty()) {
                    logger.debug("Type of request is null or empty." + " RequestID was: " + requestUID);
                }
                URI eloURI;
                eloURI = new URI(afterTuple.getField(3).getValue().toString());
                if (type.equals(METADATA)) {
                    if (repo != null) {
                        logger.debug("Request to fetch metadata for ELO: " + eloURI + " RequestID was: " + requestUID);
                        IMetadata metadata = repo.retrieveMetadata(eloURI);
                        sendResponse(metadata, requestUID);
                    } else {
                        logger.debug("Request to fetch metadata for ELO, but Repository is null: " + eloURI + " RequestID was: " + requestUID);
                    }
                } else if (type.equals(ELO)) {
                    if (repo != null) {
                        logger.debug("Request to fetch ELO: " + eloURI + " RequestID was: " + requestUID);
                        IELO elo = repo.retrieveELO(eloURI);
                        sendResponse(elo, requestUID);
                    } else {
                        logger.debug("Request to fetch ELO, but Repository is null: " + eloURI + " RequestID was: " + requestUID);
                    }
                } else {
                    logger.debug("Unknown Type in Request-Tuple: " + type + " RequestID was: " + requestUID);
                }
            } catch (URISyntaxException e) {
                logger.debug("Cannot parse URI from Tuple: " + afterTuple.getField(2).getValue().toString());
                e.printStackTrace();
            }
        }
    }

    public void sendResponse(IELO elo, String requestUID) {
        if (elo != null) {
            Tuple answerTuple = new Tuple(requestUID, ROOLO_RESPONSE, elo.getXml());
            try {
                commandSpace.write(answerTuple);
            } catch (TupleSpaceException e) {
                e.printStackTrace();
            }
        } else {
            logger.debug("Repository returned null for ELO request with id: " + requestUID);
        }
    }

    public void sendResponse(IMetadata metadata, String requestUID) {
        if (metadata != null) {
            Tuple answerTuple = new Tuple(requestUID, ROOLO_RESPONSE, metadata.getXml());
            try {
                commandSpace.write(answerTuple);
            } catch (TupleSpaceException e) {
                e.printStackTrace();
            }
        } else {
            logger.debug("Repository returned null for metadata request with id: " + requestUID);
        }
    }

    @Override
    public void setRepository(IRepository rep) {
        this.repo = rep;
    }

    @Override
    public void setMetadataTypeManager(IMetadataTypeManager manager) {
        this.manager = manager;

    }

}

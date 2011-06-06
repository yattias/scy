package eu.scy.agents.roolo.rooloaccessor;

import info.collide.sqlspaces.commons.Field;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import roolo.api.IRepository;
import roolo.elo.api.IELO;
import roolo.elo.api.IMetadata;
import roolo.elo.api.IMetadataTypeManager;
import roolo.search.ISearchResult;
import roolo.search.MetadataQueryComponent;
import roolo.search.Query;
import roolo.search.util.SearchResultUtils;
import eu.scy.agents.api.AgentLifecycleException;
import eu.scy.agents.api.IRepositoryAgent;
import eu.scy.agents.impl.AbstractThreadedAgent;
import eu.scy.agents.impl.AgentProtocol;

public class RooloAccessorAgent extends AbstractThreadedAgent implements IRepositoryAgent {

    private static final Logger logger = Logger.getLogger(RooloAccessorAgent.class.getName());

    private static final String ROOLO_RESPONSE = "roolo-response";

    private static final String ELO = "elo";

    private static final String METADATA = "metadata";

    private static final String SEARCH = "search";

    static final String AGENT_NAME = "roolo-agent";

    public static final String NAME = RooloAccessorAgent.class.getName();

    private boolean isStopped;

    private IRepository rooloServices;

    private IMetadataTypeManager metadataTypeManager;

    private int listenerId;

    public RooloAccessorAgent(Map<String, Object> map) {
        super(NAME, (String) map.get(AgentProtocol.PARAM_AGENT_ID), (String) map.get(AgentProtocol.TS_HOST), (Integer) map.get(AgentProtocol.TS_PORT));
        Tuple activationTuple = new Tuple(String.class, AGENT_NAME, String.class, Field.createWildCardField());
        try {
            listenerId = getCommandSpace().eventRegister(Command.WRITE, activationTuple, this, true);
        } catch (TupleSpaceException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doRun() throws TupleSpaceException, AgentLifecycleException, InterruptedException {
        while (status == Status.Running) {
            sendAliveUpdate();
            try {
                Thread.sleep(AgentProtocol.ALIVE_INTERVAL / 3);
            } catch (InterruptedException e) {
                throw new AgentLifecycleException(e.getMessage(), e);
            }
        }
    }

    @Override
    protected void doStop() throws AgentLifecycleException {
        try {
            getCommandSpace().eventDeRegister(listenerId);
        } catch (TupleSpaceException e) {
            throw new AgentLifecycleException("Could not deregister listener", e);
        }
        status = Status.Stopping;
    }

    @Override
    protected Tuple getIdentifyTuple(String arg0) {
        return null;
    }

    @Override
    public boolean isStopped() {
        return isStopped;
    }

    public void processTuple(Tuple tuple) {
        try {
            String requestUID = tuple.getField(0).getValue().toString();
            String type = tuple.getField(2).getValue().toString();
            if (requestUID == null || requestUID.isEmpty()) {
                logger.debug("UID of request is null or empty.");
                return;
            }
            if (type == null || type.isEmpty()) {
                logger.debug("Type of request is null or empty." + " RequestID was: " + requestUID);
                return;
            }

            if (type.equals(METADATA)) {
                URI eloURI = new URI(tuple.getField(3).getValue().toString());
                if (rooloServices != null) {
                    logger.debug("Request to fetch metadata for ELO: " + eloURI + " RequestID was: " + requestUID);
                    IMetadata metadata = rooloServices.retrieveMetadata(eloURI);
                    sendResponse(metadata, requestUID);
                } else {
                    logger.debug("Request to fetch metadata for ELO, but Repository is null: " + eloURI + " RequestID was: " + requestUID);
                }
            } else if (type.equals(ELO)) {
                URI eloURI = new URI(tuple.getField(3).getValue().toString());
                if (rooloServices != null) {
                    logger.debug("Request to fetch ELO: " + eloURI + " RequestID was: " + requestUID);
                    IELO elo = rooloServices.retrieveELO(eloURI);
                    sendResponse(elo, requestUID);
                } else {
                    logger.debug("Request to fetch ELO, but Repository is null: " + eloURI + " RequestID was: " + requestUID);
                }
            } else if (type.equals(SEARCH)) {
                String keywords = new String(tuple.getField(3).getValue().toString());
                if (rooloServices != null) {
                    logger.debug("Request to search for: " + keywords + " RequestID was: " + requestUID);
                    List<ISearchResult> searchResults = rooloServices.search(new Query(new MetadataQueryComponent("contents", keywords)));
                    sendResponse(searchResults, requestUID);
                } else {
                    logger.debug("Request to search for ELOs, but Repository is null: " + keywords + " RequestID was: " + requestUID);
                }
            }

            else {
                logger.debug("Unknown Type in Request-Tuple: " + type + " RequestID was: " + requestUID);
            }
        } catch (URISyntaxException e) {
            logger.debug("Cannot parse URI from Tuple: " + tuple.getField(2).getValue().toString());
            e.printStackTrace();
        }
    }

    private void sendResponse(List<ISearchResult> searchResults, String requestUID) {
        if (searchResults != null) {
            String xmlResults = SearchResultUtils.createXmlStringFromSearchResults(searchResults);
            Tuple answerTuple = new Tuple(requestUID, ROOLO_RESPONSE, xmlResults);
            try {
                getCommandSpace().write(answerTuple);
            } catch (TupleSpaceException e) {
                e.printStackTrace();
            }
        } else {
            logger.debug("Repository returned null for ELO request with id: " + requestUID);
        }

    }

    public void sendResponse(IELO elo, String requestUID) {
        if (elo != null) {
            Tuple answerTuple = new Tuple(requestUID, ROOLO_RESPONSE, elo.getXml());
            try {
                getCommandSpace().write(answerTuple);
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
                getCommandSpace().write(answerTuple);
            } catch (TupleSpaceException e) {
                e.printStackTrace();
            }
        } else {
            logger.debug("Repository returned null for metadata request with id: " + requestUID);
        }
    }

    @Override
    public void setRepository(IRepository rep) {
        rooloServices = rep;
    }

    @Override
    public void setMetadataTypeManager(IMetadataTypeManager manager) {
        metadataTypeManager = manager;
    }

    @Override
    public void call(Command command, int seq, Tuple tuple, Tuple beforeTuple) {
        if (listenerId != seq) {
            logger.debug("Callback passed to Superclass.");
            super.call(command, seq, tuple, beforeTuple);
            return;
        } else {
            processTuple(tuple);
        }
    }

}

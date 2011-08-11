package eu.scy.agents.roolo.rooloaccessor;

import info.collide.sqlspaces.commons.Field;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import roolo.api.IRepository;
import roolo.elo.JDomBasicELOFactory;
import roolo.elo.api.IELO;
import roolo.elo.api.IMetadata;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.IMetadataTypeManager;
import roolo.elo.api.IMetadataValueContainer;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;
import roolo.elo.metadata.BasicMetadata;
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

    private static final String HIT_COUNT = "hit-count";

    private static final String ADD_METADATA = "addmetadata";

    static final String AGENT_NAME = "roolo-agent";

    public static final String NAME = RooloAccessorAgent.class.getName();

    private boolean isStopped;

    private IRepository rooloServices;

    private IMetadataTypeManager metadataTypeManager;

    private int listenerId;

    private JDomBasicELOFactory jDomBasicELOFactory;

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
                long start = System.currentTimeMillis();
                long uricreationStarted = System.currentTimeMillis();
                URI eloURI = new URI(tuple.getField(3).getValue().toString());
                long uricreationFinished = System.currentTimeMillis();
                logger.debug("URI creation took: " + (uricreationFinished - uricreationStarted) + "ms");
                if (rooloServices != null) {
                    logger.debug("Request to fetch ELO: " + eloURI + " RequestID was: " + requestUID);
                    long retrievingStarted = System.currentTimeMillis();
                    IELO elo = rooloServices.retrieveELO(eloURI);
                    long end = System.currentTimeMillis();
                    logger.debug("Retrieving ELO (without response) took " + (end - retrievingStarted) + " ms.");
                    long responseStarted = System.currentTimeMillis();
                    sendResponse(elo, requestUID);
                    long responseFinished = System.currentTimeMillis();

                    logger.debug("Answering took: " + (responseFinished - responseStarted) + "ms");
                }
                long end = System.currentTimeMillis();
                logger.debug("Retrieving ELO AND sendResponse took " + (end - start) + " ms.");
            } else if (type.equals(SEARCH)) {
                String keywords = new String(tuple.getField(3).getValue().toString());
                if (rooloServices != null) {
                    logger.debug("Request to search for: " + keywords + " RequestID was: " + requestUID);
                    List<ISearchResult> searchResults = rooloServices.search(new Query(new MetadataQueryComponent("contents", keywords)));
                    sendResponse(searchResults, requestUID);
                } else {
                    logger.debug("Request to search for ELOs, but Repository is null: " + keywords + " RequestID was: " + requestUID);
                }
            } else if (type.equals(HIT_COUNT)) {
                String keywords = new String(tuple.getField(3).getValue().toString());
                if (rooloServices != null) {
                    logger.debug("Request hit number for search: " + keywords + " RequestID was: " + requestUID);
                    Query query = new Query(new MetadataQueryComponent("contents", keywords));
                    query.setMaxResults(Integer.MAX_VALUE);
                    int hits = rooloServices.getHits(query);
                    sendResponse(hits, requestUID);
                } else {
                    logger.debug("Request to search for ELOs, but Repository is null: " + keywords + " RequestID was: " + requestUID);
                }
            } else if (type.equals(ADD_METADATA)) {
                String elouri = new String(tuple.getField(3).getValue().toString());
                String key = new String(tuple.getField(4).getValue().toString());
                String value = new String(tuple.getField(4).getValue().toString());
                if (rooloServices != null) {
                    logger.debug("Request to add metadata to elo: " + elouri + " RequestID was: " + requestUID + "Key: " + key + " Value: " + value);
                    IMetadata metadata = addStringMetadata(new URI(elouri), key, value);
                    sendResponse(metadata, requestUID);
                } else {
                    logger.debug("Request to addmetadata to ELO " + elouri + ", but Repository is null: " + key + " RequestID was: " + requestUID);
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

    private IMetadata addStringMetadata(URI uri, String key, String value) {
        IMetadata newMetadata = jDomBasicELOFactory.createMetadata();
        IMetadataKey metadataKey = metadataTypeManager.getMetadataKey(key);
        IMetadataValueContainer valueContainer = newMetadata.getMetadataValueContainer(metadataKey);
        valueContainer.addValue(value);
        newMetadata.addMetadataPair(metadataKey, valueContainer);
        rooloServices.addMetadata(uri, newMetadata);
        return rooloServices.retrieveMetadata(uri);

        //
    }

    private void sendResponse(int hitCount, String requestUID) {
        Tuple answerTuple = new Tuple(requestUID, ROOLO_RESPONSE, hitCount);
        try {
            getCommandSpace().write(answerTuple);
        } catch (TupleSpaceException e) {
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
        jDomBasicELOFactory = new JDomBasicELOFactory(metadataTypeManager);
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

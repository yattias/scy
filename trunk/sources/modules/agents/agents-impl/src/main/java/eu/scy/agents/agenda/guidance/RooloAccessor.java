package eu.scy.agents.agenda.guidance;

import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Field;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;

import java.rmi.dgc.VMID;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import roolo.elo.BasicELO;
import roolo.elo.JDomBasicELOFactory;
import roolo.search.ISearchResult;
import roolo.search.util.SearchResultUtils;

public class RooloAccessor {

    // RooloAccessorAgent
    private static final String ROOLO_AGENT_NAME = "roolo-agent";
    private static final String ROOLO_AGENT_RESPONSE = "roolo-response";
    private static final String ROOLO_AGENT_SEARCH = "search";
    private static final String ROOLO_AGENT_ELO = "elo";
    private static final int ROOLO_AGENT_TIMEOUT = 5000;

    private final TupleSpace commandSpace;
    private final Logger logger;
	
	
    public RooloAccessor(TupleSpace commandSpace, Logger logger) {
		this.commandSpace = commandSpace;
		this.logger = logger;
	}
    
    /**
     * Returns a list of all ELO URIs for a certain user in a certain mission.
     * Returns null if RooloAccessorAgent did not respond within a certain time peroid.
     * @param mission
     * @param userName
     * @return
     * @throws TupleSpaceException
     */
    public List<String> getEloUris(String mission, String userName) throws TupleSpaceException {
        String uniqueID = new VMID().toString();
        String query = String.format("mission:\"%s\" AND user:\"%s\"", mission, userName);
        Tuple requestTuple = new Tuple(uniqueID, ROOLO_AGENT_NAME, ROOLO_AGENT_SEARCH, query);
        commandSpace.write(requestTuple);
        logger.debug(String.format("Sent query %s to RooloAccessorAgent", query));

        Tuple responseTuple = commandSpace.waitToTake(new Tuple(uniqueID, ROOLO_AGENT_RESPONSE, Field.createWildCardField()), ROOLO_AGENT_TIMEOUT);
        if (responseTuple != null) {
            String xmlResults = responseTuple.getField(2).getValue().toString();
            List<ISearchResult> result = SearchResultUtils.createSearchResultsFromXML(xmlResults);
            
            List<String> eloUris = new ArrayList<String>();
            for(ISearchResult res : result) {
            	String eloUri = res.getUri().toString();
            	eloUris.add(eloUri);
            }
            return eloUris;
        }
        return null;
    }
	
    public BasicELO getBasicElo(String eloUri) throws TupleSpaceException {
    	String uniqueID = new VMID().toString();
    	Tuple requestTuple = new Tuple(uniqueID, ROOLO_AGENT_NAME, ROOLO_AGENT_ELO, eloUri);
    	commandSpace.write(requestTuple);
    	
    	Tuple responseTuple = commandSpace.waitToTake(new Tuple(uniqueID, ROOLO_AGENT_RESPONSE, Field.createWildCardField()), ROOLO_AGENT_TIMEOUT);
    	JDomBasicELOFactory jDomBasicELOFactory = new JDomBasicELOFactory();
    	if (responseTuple != null) {
    		String eloXml = responseTuple.getField(2).getValue().toString();
    		return (BasicELO) jDomBasicELOFactory.createELOFromXml(eloXml);
    	}
    	return null;
    }
    
}

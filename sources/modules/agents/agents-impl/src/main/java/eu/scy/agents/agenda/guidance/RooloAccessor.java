package eu.scy.agents.agenda.guidance;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import roolo.api.IRepository;
import roolo.elo.api.IELO;
import roolo.elo.api.IMetadata;
import roolo.search.ISearchResult;
import roolo.search.MetadataQueryComponent;
import roolo.search.Query;
import eu.scy.agents.agenda.exception.NoRepositoryAvailableException;

public class RooloAccessor {

    // RooloAccessorAgent
//  private static final String ROOLO_AGENT_NAME = "roolo-agent";
//  private static final String ROOLO_AGENT_RESPONSE = "roolo-response";
//  private static final String ROOLO_AGENT_SEARCH = "search";
//  private static final String ROOLO_AGENT_ELO = "elo";
//  private static final int ROOLO_AGENT_TIMEOUT = 5000;

//  private final TupleSpace commandSpace;
//	private JDomBasicELOFactory jDomBasicELOFactory;
//	private IMetadataTypeManager typeManager;
	
	private static final Logger logger = Logger.getLogger(RooloAccessor.class.getName());
	
    private IRepository repository;
    
    public RooloAccessor() {
	}
    
    public void setRepository(IRepository rep) {
    	this.repository = rep;
    }

    /**
     * Returns a list of all ELO URIs for a certain user and mission.
     *
     * @param missionId the mission id
     * @param userName the user name
     * @return the list of ELO URIs for mission runtime
     * @throws NoRepositoryAvailableException the no repository available exception
     */
    public List<String> getEloUrisForMissionRuntime(String missionId, String userName) throws NoRepositoryAvailableException {
        if (repository != null) {
        	throw new NoRepositoryAvailableException("Could not get ELO URIs because no repository available");
        }
        String query = String.format("missionId:\"%s\" AND author:\"%s\" AND template:\"false\" AND lastVersion:\"true\"", missionId, userName);
        List<String> eloUris = new ArrayList<String>();
        logger.debug(String.format("Retrieving users ELO URIs for current mission [ user: %s | missionId: %s ]", userName, missionId));
        
        List<ISearchResult> searchResults = this.repository.search(new Query(new MetadataQueryComponent("contents", query)));
        for(ISearchResult result : searchResults) {
        	String eloUri = result.getUri().toString();
        	eloUris.add(eloUri);
        }
        return eloUris;
    }

    public IMetadata getMetadata(String eloUri) throws URISyntaxException, NoRepositoryAvailableException {
        if (repository == null) {
        	throw new NoRepositoryAvailableException("Could not retrieve metadata because no repository available");
        }
        URI eloURI = new URI(eloUri);
        logger.debug(String.format("Retrieving metadata [ ELO Uri: %s ]", eloUri));
        return repository.retrieveMetadata(eloURI);
    }

    public IELO getElo(String eloUri) throws URISyntaxException, NoRepositoryAvailableException {
    	if (repository == null) {
    		throw new NoRepositoryAvailableException("Could not retrieve ELO because no repository available");
    	}
    	URI eloURI = new URI(eloUri);
    	logger.debug(String.format("Retrieving ELO [ ELO Uri: %s ]", eloUri));
    	return repository.retrieveELO(eloURI);
    }
    
    public IELO getEloFirstVersion(String eloUri) throws URISyntaxException, NoRepositoryAvailableException {
    	if (repository == null) {
    		throw new NoRepositoryAvailableException("Could not retrieve ELO because no repository available");
    	}
    	URI eloURI = new URI(eloUri);
    	logger.debug(String.format("Retrieving ELO [ ELO Uri: %s ]", eloUri));
    	return repository.retrieveELOFirstVersion(eloURI);
    }

//    public void setTypeManager(IMetadataTypeManager typeManager) {
//    	this.typeManager = typeManager;
//    	jDomBasicELOFactory = new JDomBasicELOFactory(this.typeManager);
//	}

//    public List<String> getEloUris(String mission, String userName) throws TupleSpaceException {
//        String uniqueID = new VMID().toString();
//        String query = String.format("mission:\"%s\" AND user:\"%s\"", mission, userName);
//        Tuple requestTuple = new Tuple(uniqueID, ROOLO_AGENT_NAME, ROOLO_AGENT_SEARCH, query);
//        commandSpace.write(requestTuple);
//        logger.debug(String.format("Sent query to RooloAccessorAgent. Query: %s", query));
//
//        Tuple responseTuple = commandSpace.waitToTake(new Tuple(uniqueID, ROOLO_AGENT_RESPONSE, Field.createWildCardField()), ROOLO_AGENT_TIMEOUT);
//        if (responseTuple != null) {
//            String xmlResults = responseTuple.getField(2).getValue().toString();
//            List<ISearchResult> result = SearchResultUtils.createSearchResultsFromXML(xmlResults);
//            
//            List<String> eloUris = new ArrayList<String>();
//            for(ISearchResult res : result) {
//            	String eloUri = res.getUri().toString();
//            	eloUris.add(eloUri);
//            }
//            return eloUris;
//        }
//        return null;
//    }
    
//    public IELO getBasicElo(String eloUri) throws TupleSpaceException {
//    	String uniqueID = new VMID().toString();
//    	Tuple requestTuple = new Tuple(uniqueID, ROOLO_AGENT_NAME, ROOLO_AGENT_ELO, eloUri);
//    	commandSpace.write(requestTuple);
//    	
//    	Tuple responseTuple = commandSpace.waitToTake(new Tuple(uniqueID, ROOLO_AGENT_RESPONSE, Field.createWildCardField()), ROOLO_AGENT_TIMEOUT);
//    	// TODO needs IMetadataTypeManager...
//    	JDomBasicELOFactory jDomBasicELOFactory = new JDomBasicELOFactory();
//    	if (responseTuple != null) {
//    		String eloXml = responseTuple.getField(2).getValue().toString();
//    		return jDomBasicELOFactory.createELOFromXml(eloXml);
//    	}
//    	return null;
//    }
    
}

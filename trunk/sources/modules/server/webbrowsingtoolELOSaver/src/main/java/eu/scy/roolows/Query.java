package eu.scy.roolows;

import java.net.URI;
import java.util.List;
import java.util.Vector;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import roolo.api.search.AndQuery;
import roolo.api.search.IQuery;
import roolo.api.search.ISearchResult;
import roolo.cms.repository.mock.BasicMetadataQuery;
import roolo.cms.repository.search.BasicSearchOperations;
import roolo.elo.api.IMetadata;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.IMetadataValueContainer;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;
import roolo.elo.metadata.keys.Contribute;

/**
 * REST Web Service
 *
 * @author __SVEN__
 */
@Path("/query")
public class Query {

    @Context
    private static final Beans beans = Beans.getInstance();
    private final static Logger log = Logger.getLogger(Query.class.getName());
    private IMetadataKey titleKey;
    private IMetadataKey typeKey;
    private IMetadataKey dateCreatedKey;
    private IMetadataKey authorKey;
    private IMetadataKey identifierKey;
    private IMetadataKey descriptionKey;

    /** Creates a new instance of SaveELO */
    public Query() {
    	initMetadataKeys();
    }

    /**
     * GET method for performing AND-queries on metadata via HTTP Query String
     * @return a JSONArray of all results as JSONObjects. Every Object includes the following JSON params
     * <ul>
     * <li> uri
     * <li> title
     * <li> type
     * <li> author
     * <li> description
     * <li> relevance
     * </ul>
     */
    @GET
    @Produces("text/xml")
    public String andQueryAsXML(@Context UriInfo ui) {
        initMetadataKeys();
        
        StringBuffer xmlString = new StringBuffer();
        List<ISearchResult> results = doAndQuery(ui);
        log.info("found " + results.size() + " hits");
        xmlString.append("<results count=\""+ results.size()+"\">");
        for (ISearchResult result : results) {
            IMetadata resultMetadata = result.getMetadata();
            xmlString.append("<elo>");
            xmlString.append("<uri>" + resultMetadata.getMetadataValueContainer(identifierKey) + "</uri>");
            xmlString.append("<title>" + resultMetadata.getMetadataValueContainer(titleKey) + "</title>");
            xmlString.append("<type>" + resultMetadata.getMetadataValueContainer(typeKey) + "</type>");
            xmlString.append("<author>" + resultMetadata.getMetadataValueContainer(authorKey) + "</author>");
            xmlString.append("<description>" + resultMetadata.getMetadataValueContainer(descriptionKey) + "</description>");
            xmlString.append("<relevance>" + result.getRelevance() + "</relevance>");
            xmlString.append("</elo>");
        }
        xmlString.append("</results>");
        return xmlString.toString();
    }
    
    /**
     * GET method for performing AND-queries on metadata via HTTP Query String
     * @return a JSONArray of all results as JSONObjects. Every Object includes the following JSON params
     * <ul>
     * <li> uri
     * <li> title
     * <li> type
     * <li> author
     * <li> description
     * <li> relevance
     * </ul>
     */
    @GET
    @Produces("application/json")
    public JSONArray andQueryAsJson(@Context UriInfo ui) {
        JSONArray queriedURIs = new JSONArray();
        List<ISearchResult> results = doAndQuery(ui);
        log.info("found " + results.size() + " hits");
        try {
            for (ISearchResult result : results) {
                JSONObject resultAsJson = new JSONObject();
                IMetadata resultMetadata = result.getMetadata();
                resultAsJson.put("uri", resultMetadata.getMetadataValueContainer(identifierKey));
                resultAsJson.put("title", resultMetadata.getMetadataValueContainer(titleKey));
                resultAsJson.put("type", resultMetadata.getMetadataValueContainer(typeKey));
                resultAsJson.put("author", resultMetadata.getMetadataValueContainer(authorKey));
                resultAsJson.put("description", resultMetadata.getMetadataValueContainer(descriptionKey));
                resultAsJson.put("relevance", result.getRelevance());
                resultAsJson.put("dateCreated", resultMetadata.getMetadataValueContainer(dateCreatedKey));
                queriedURIs.put(resultAsJson);
            }
        } catch (JSONException ex) {
            log.error(ex.getMessage());
        }
        return queriedURIs;
    }


    private List<ISearchResult> doAndQuery(UriInfo ui) {
        MultivaluedMap<String, String> params = ui.getQueryParameters();
        log.info("params: " + params);
        String[] keys = new String[params.keySet().size()];
        params.keySet().toArray(keys);
        String value = params.getFirst(keys[0]);
        log.info("value: "+value);

        IQuery query = new BasicMetadataQuery(beans.getTypeManager().getMetadataKey(CoreRooloMetadataKeyIds.valueOf(keys[0])), BasicSearchOperations.EQUALS, value, null);
		// Uncomment this line for final deployment with JPA RoOLO
        // IQuery query = new BasicMetadataQuery(beans.getTypeManager().getMetadataKey(CoreRooloMetadataKeyIds.valueOf(keys[0])), BasicSearchOperations.EQUALS, value, null);
        List<IQuery> queries = new Vector<IQuery>();
        for (int i = 1; i < keys.length; i++) {
            IQuery newQuery = new BasicMetadataQuery(beans.getTypeManager().getMetadataKey(CoreRooloMetadataKeyIds.valueOf(keys[i])), BasicSearchOperations.EQUALS, params.getFirst(keys[i]), null);
            // Uncomment this line for final deployment with JPA RoOLO
            // IQuery newQuery = new BasicMetadataQuery(beans.getTypeManager().getMetadataKey(CoreRooloMetadataKeyIds.valueOf(keys[i])), BasicSearchOperations.EQUALS, params.getFirst(keys[i]));
            queries.add(newQuery);
        }
        IQuery[] queriesArray = new IQuery[queries.size()];
        queries.toArray(queriesArray);
        IQuery andQuery;
        if (queries.size() == 0) {
            andQuery = query;
        }
        andQuery = new AndQuery(query, queriesArray);

        return beans.getRepository().search(andQuery);

    }

    /**
     * POST method for querying RoOLO for ELOs. As a plain text parameter, a lucene query string is submitted
     * 
     * @param queryString The lucene query string
     * @return a JSONArray of all results as JSONObjects. Every Object includes the following JSON params
     * <ul>
     * <li> uri
     * <li> title
     * <li> type
     * <li> author
     * <li> description
     * <li> relevance
     * </ul>
     */
    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public JSONArray query(JSONObject params) {
    	JSONArray queriedURIs = null;
    	try {
    		JSONObject query = (JSONObject) params.get("query");
			String querykey = query.getString("metadatakey");
			String queryvalue = query.getString("metadatavalue");
			
			JSONArray metadataKeys = (JSONArray) params.get("metadata");
			
			queriedURIs = new JSONArray();
			
			if (querykey != null && queryvalue != null) {
				//query roolo
				IQuery metadataquery = new BasicMetadataQuery(beans.getTypeManager().getMetadataKey(querykey), BasicSearchOperations.EQUALS, queryvalue, null);
				// Uncomment this line for final deployment with JPA RoOLO
				// IQuery metadataquery = new BasicMetadataQuery(beans.getTypeManager().getMetadataKey(querykey), BasicSearchOperations.EQUALS, queryvalue, null);
				final List<ISearchResult> results = beans.getRepository().search(metadataquery);
				for (ISearchResult result : results) {
					JSONObject resultAsJson = new JSONObject();
					URI uri = result.getUri();
					// query for metadata of ELO
					IMetadata metadata = beans.getRepository().retrieveMetadata(uri);
					if (metadata != null) {
						// fill result with requested metadata values
						for (int i = 0; i < metadataKeys.length(); i++) {
							String metadataKey = metadataKeys.getString(i);
							IMetadataKey key = beans.getTypeManager().getMetadataKey(metadataKey);
							if (key != null) {
								IMetadataValueContainer valueContainer = metadata.getMetadataValueContainer(key);
								if (valueContainer != null) {
									if (key.equals(authorKey)) {
										resultAsJson.put(metadataKey, ((Contribute) valueContainer.getValue()).getVCard());
									} else {
										resultAsJson.put(metadataKey, valueContainer.getValue().toString());
									}
								}
							}
						}
					// fallback: just return the uri of the ELO
					} else {
						resultAsJson.put("uri", result.getUri());
					}
					queriedURIs.put(resultAsJson);
				}
			}
		} catch (JSONException ex) {
			log.error(ex.getMessage());
		}
		return queriedURIs;
    }

    private void initMetadataKeys() {
        authorKey = beans.getTypeManager().getMetadataKey(CoreRooloMetadataKeyIds.AUTHOR.getId());
        typeKey = beans.getTypeManager().getMetadataKey(CoreRooloMetadataKeyIds.TECHNICAL_FORMAT.getId());
        titleKey = beans.getTypeManager().getMetadataKey(CoreRooloMetadataKeyIds.TITLE.getId());
        dateCreatedKey = beans.getTypeManager().getMetadataKey(CoreRooloMetadataKeyIds.DATE_CREATED.getId());
        identifierKey = beans.getTypeManager().getMetadataKey(CoreRooloMetadataKeyIds.IDENTIFIER.getId());
        descriptionKey = beans.getTypeManager().getMetadataKey(CoreRooloMetadataKeyIds.DESCRIPTION.getId());
    }
}

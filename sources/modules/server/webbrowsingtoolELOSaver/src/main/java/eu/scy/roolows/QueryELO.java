package eu.scy.roolows;

import java.net.URI;
import java.util.List;
import java.util.Locale;
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

import roolo.search.SearchOperation;
import roolo.search.MetadataQueryComponent;
import roolo.search.IQueryComponent;
import roolo.search.IQuery;
import roolo.search.Query;
import roolo.search.AndQuery;
import roolo.search.ISearchResult;


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
public class QueryELO {

    @Context
    private static final Beans beans = Beans.getInstance();
    private final static Logger log = Logger.getLogger(QueryELO.class.getName());
    private IMetadataKey titleKey;
    private IMetadataKey typeKey;
    private IMetadataKey dateCreatedKey;
    private IMetadataKey authorKey;
    private IMetadataKey identifierKey;
    private IMetadataKey descriptionKey;

    /** Creates a new instance of SaveELO */
    public QueryELO() {
        initMetadataKeys();
    }

    /**
     * GET method for performing AND-queries on metadata via HTTP QueryELO String
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

        StringBuilder xmlString = new StringBuilder();
        List<ISearchResult> results = doAndQuery(ui);
        log.info("found " + results.size() + " hits");
        xmlString.append("<results count=\"" + results.size() + "\">");
        for (ISearchResult result : results) {
            xmlString.append("<elo>");
            xmlString.append("<uri>" + result.getUri() + "</uri>");
            //FIXME hack!!!!111!1 introduce json fields to get the locale
            String title = result.getTitle(result.getTitles().keySet().iterator().next());
            xmlString.append("<title>" + title + "</title>");
//            xmlString.append("<title>" + result.getTitle() + "</title>");
            xmlString.append("<type>" + result.getTechnicalFormat() + "</type>");
            //FIXME not just returning the first author!!!
            xmlString.append("<author>" + result.getAuthors().get(0) + "</author>");
            //FIXME hack!!!!111!1 introduce json fields to get the locale
            String description = result.getDescription(result.getDescriptions().keySet().iterator().next());
            xmlString.append("<description>" + description + "</description>");
//            xmlString.append("<description>" + result.getDescription() + "</description>");
            xmlString.append("<relevance>" + result.getRelevance() + "</relevance>");
            xmlString.append("</elo>");
        }
        xmlString.append("</results>");
        return xmlString.toString();
    }

    /**
     * GET method for performing AND-queries on metadata via HTTP QueryELO String
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
                resultAsJson.put("uri", result.getUri());
                 //FIXME hack!!!!111!1 introduce json fields to get the locale
                String title = result.getTitle(result.getTitles().keySet().iterator().next());
                resultAsJson.put("title", title);
//                resultAsJson.put("title", result.getTitle());
                resultAsJson.put("type", result.getTechnicalFormat());
                //FIXME Authors: not everytime just one!!!
                resultAsJson.put("author", result.getAuthors().get(0));
                //FIXME hack!!!!111!1 introduce json fields to get the locale
                String description = result.getDescription(result.getDescriptions().keySet().iterator().next());
                resultAsJson.put("description", description);
//                resultAsJson.put("description", result.getDescription());
                resultAsJson.put("relevance", result.getRelevance());
                resultAsJson.put("dateCreated", result.getDateCreated());
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
        log.info("value: " + value);

        IQueryComponent queryComponent = new MetadataQueryComponent(beans.getTypeManager().getMetadataKey(CoreRooloMetadataKeyIds.valueOf(keys[0])), SearchOperation.EQUALS, value);
        // Uncomment this line for final deployment with JPA RoOLO
        // IQuery query = new BasicMetadataQuery(beans.getTypeManager().getMetadataKey(CoreRooloMetadataKeyIds.valueOf(keys[0])), BasicSearchOperations.EQUALS, value, null);
        List<IQueryComponent> queries = new Vector<IQueryComponent>();
        for (int i = 1; i < keys.length; i++) {
            IQueryComponent newQuery = new MetadataQueryComponent(beans.getTypeManager().getMetadataKey(CoreRooloMetadataKeyIds.valueOf(keys[i])), SearchOperation.EQUALS, params.getFirst(keys[i]));
            // Uncomment this line for final deployment with JPA RoOLO
            // IQuery newQuery = new BasicMetadataQuery(beans.getTypeManager().getMetadataKey(CoreRooloMetadataKeyIds.valueOf(keys[i])), BasicSearchOperations.EQUALS, params.getFirst(keys[i]));
            queries.add(newQuery);
        }
        IQueryComponent[] queriesArray = new IQueryComponent[queries.size()];
        queries.toArray(queriesArray);
        IQueryComponent andQuery;
        if (queries.isEmpty()) {
            andQuery = queryComponent;
        }
        andQuery = new AndQuery(queryComponent, queriesArray);
        IQuery query = new Query(andQuery);
        return beans.getRepository().search(query);

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
            JSONObject queryAsJSON = (JSONObject) params.get("query");
            String querykey = queryAsJSON.getString("metadatakey");
            String queryvalue = queryAsJSON.getString("metadatavalue");

            JSONArray metadataKeys = (JSONArray) params.get("metadata");

            queriedURIs = new JSONArray();

            if (querykey != null && queryvalue != null) {
                //query roolo
                IQueryComponent metadataquery = new MetadataQueryComponent(beans.getTypeManager().getMetadataKey(querykey), SearchOperation.EQUALS, queryvalue);
                // Uncomment this line for final deployment with JPA RoOLO
                // IQuery metadataquery = new BasicMetadataQuery(beans.getTypeManager().getMetadataKey(querykey), BasicSearchOperations.EQUALS, queryvalue, null);
                IQuery query = new Query(metadataquery);
                final List<ISearchResult> results = beans.getRepository().search(query);
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
                                    	if (valueContainer.getValue() != null) {
                                    		resultAsJson.put(metadataKey, ((Contribute) valueContainer.getValue()).getVCard());
                                    	} else {
                                    		resultAsJson.put(metadataKey, "no author"); // this is only a quick fix
                                    	}
                                    } else {
                                    	if (valueContainer.getValue() != null) {
                                    		resultAsJson.put(metadataKey, valueContainer.getValue().toString());
                                    	} else {
                                    		resultAsJson.put(metadataKey, "");
                                    	}
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

package eu.scy.roolows;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.WebAppDescriptor;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Locale;
import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import roolo.search.IQuery;
import roolo.search.IQueryComponent;
import roolo.search.MetadataQueryComponent;
import roolo.search.Query;
import roolo.search.ISearchResult;
import roolo.search.SearchOperation;
import roolo.elo.api.IContent;
import roolo.elo.api.IELO;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;
import roolo.elo.metadata.keys.BasicMetadataKey;
import roolo.elo.metadata.keys.Contribute;
import roolo.elo.metadata.keys.ContributeMetadataKey;
import roolo.elo.metadata.keys.LongMetadataKey;
import roolo.elo.metadata.keys.StringMetadataKey;
import roolo.elo.metadata.keys.UriMetadataKey;


/** 
 * Tests the REST Webservice as a client using jersey testframework and jUnit 4
 * 
 * @author __SVEN__
 */
public class TestClient extends JerseyTest {

    private static final Beans beans = Beans.getInstance();
    private static final Logger logger = Logger.getLogger(TestClient.class.getName());
    private IMetadataKey authorKey;
    private IMetadataKey typeKey;
    private IMetadataKey titleKey;
    private IMetadataKey dateCreatedKey;
    private IMetadataKey descriptionKey;
    private IMetadataKey identifierKey;

    public static IELO parseJsonELO(JSONObject json) throws URISyntaxException {
        try {
            JSONObject metadata = json.getJSONObject("metadata");
            String contentString = json.getString("content");
            final Iterator keys = metadata.keys();
            IELO elo = beans.getEloFactory().createELO();
            //set elo metadata
            while (keys.hasNext()) {
                final String key = (String) keys.next();
                final Object value = metadata.get(key);
                final IMetadataKey metadatakey = beans.getTypeManager().getMetadataKey(key);
                if (value == JSONObject.NULL) {
                    elo.getMetadata().getMetadataValueContainer(metadatakey).setValue(null);
                } else {
                    if (metadatakey instanceof StringMetadataKey) {
                        elo.getMetadata().getMetadataValueContainer(metadatakey).setValue((String) value);
                    } else if (metadatakey instanceof UriMetadataKey) {
                        elo.getMetadata().getMetadataValueContainer(metadatakey).setValue(new URI((String) value));
                    } else if (metadatakey instanceof ContributeMetadataKey) {
                        Contribute contribute = new Contribute();
                        contribute.setVCard(((JSONObject) value).getString("vcard"));
                        contribute.setDate(((JSONObject) value).getLong("date"));
                        elo.getMetadata().getMetadataValueContainer(metadatakey).setValue(contribute);
                    } else if (metadatakey instanceof LongMetadataKey) {
                        ///this has to be done, because JCR-RoOLO uses String version numbers and RoOLO-Mock uses Integer version numbers and the DateCreated-Key uses Long
                        if (value instanceof Integer || value instanceof Long) {
                            elo.getMetadata().getMetadataValueContainer(metadatakey).setValue(value);
                        } else {
                            elo.getMetadata().getMetadataValueContainer(metadatakey).setValue(Long.parseLong((String) value));
                        }
                    } else if (metadatakey instanceof BasicMetadataKey) {
                        //last exit
                        elo.getMetadata().getMetadataValueContainer(metadatakey).setValue(value.toString());
                    }
                }
            }
            //set elo content
            IContent eloContent = beans.getEloFactory().createContent();
            //contentLanguages -> parse JSONArray to Locales
            JSONArray contentLanguagesAsJson = json.getJSONArray("contentLanguages");
            List<Locale> contentLanguages = new ArrayList<Locale>();
            for (int i = 0; i < contentLanguagesAsJson.length(); i++) {
                contentLanguages.add(new Locale(contentLanguagesAsJson.getString(i)));
            }
            eloContent.setLanguages(contentLanguages);
            logger.info("ELO-content: " + contentString);
            eloContent.setXmlString(contentString);
            elo.setContent(eloContent);

            return elo;
        } catch (JSONException ex) {
            logger.error(ex.getMessage());
        }
        return null;
    }

    public TestClient() {
        super(new WebAppDescriptor.Builder("eu.scy.roolows").contextPath("roolo-ws").build());
        initMetadataKeys();
    }

    /**
     * Test that the expected response is sent back. This is testing the Echo ("Hello World") via HTTP GET.
     * @throws java.lang.Exception
     */
    @Test
    public void testEchoAlive() throws Exception {
        WebResource webResource = resource();
        String responseMsg = webResource.path("saveELO").get(String.class);
        Assert.assertEquals("<html><body><h1>Hello World!</body></h1></html>", responseMsg);
    }

    /**
     * Test that the ELO is saved. The response should return an URI of the ELO
     * @throws java.lang.Exception
     */
    @Test
    public void testSaveELO() throws Exception {
        //create JSON from test-ELO
        IELO elo = getExampleElo();
        JSONObject jsonData = getExampleEloAsJson(elo);

        WebResource webResource = resource();
        webResource.accept("text/plain");
        ClientResponse response = webResource.path("saveELO").type("application/json").post(ClientResponse.class, jsonData);
        String responseEntity = null;
        if (response.hasEntity()) {
            responseEntity = response.getEntity(String.class);
        }
        logger.info("response entity: " + responseEntity);

        Assert.assertNotNull(responseEntity);
    }

    @Ignore
    /**
     * This is a test of the full cycle of saving and retrieving an ELO
     * 1. creates an ELO
     * 2. saves that ELO via /saveELO. The response is the ELO URI of the saved ELO
     * 3. retrieve the ELO via /getELO. The response will be the ELO as JSON
     * 4. test the ELOs' XML for equality (The ELOs itself dont have a working equals()-method)
     * @throws java.lang.Exception
     */
    @Test
    public void testSaveAndRetrieveELO() throws Exception {
        //1. create JSON from test-ELO
        IELO elo = getExampleElo();
        JSONObject jsonData = getExampleEloAsJson(elo);
        IELO retrievedELO = null;

        //2. save ELO on path /saveELO
        WebResource saveELOResource = resource();
        saveELOResource.accept("text/plain");
        ClientResponse saveELOResponse = saveELOResource.path("saveELO").type("application/json").post(ClientResponse.class, jsonData);
        String eloUri = null;
        if (saveELOResponse.hasEntity()) {
            eloUri = saveELOResponse.getEntity(String.class);
            logger.info("eloUri: " + eloUri);
            //the original ELO has no identifier, so the ELO has to be retrieved directly to be compared to the webservice ELO
            elo = beans.getRepository().retrieveELO(new URI(eloUri));

            //3. retrieve ELO on path /getELO
            WebResource getELOResource = resource();
            getELOResource.accept("application/json");
            ClientResponse getELOResponse = getELOResource.path("getELO").type("text/plain").post(ClientResponse.class, eloUri);
            if (getELOResponse.hasEntity()) {
                JSONObject retrievedELOAsJson = getELOResponse.getEntity(JSONObject.class);
                retrievedELO = parseJsonELO(retrievedELOAsJson);

                logger.info("**********************************************");
                logger.info("retrievedELO (XML): \n" + retrievedELO.getXml());
                logger.info("**********************************************");
                logger.info("sourceELO (XML): \n" + elo.getXml());
                logger.info("**********************************************");
            }
        }
        //ELOs dont have a working equals()-method
        Assert.assertEquals(retrievedELO.getXml(), elo.getXml());
    }

    @Ignore
     /**
     * This is a test of querying roolo with lucene search strings
     * 1. creates an ELO
     * 2. saves that ELO, so that the repository has at least one ELO for the search.
     * 3. query it via webservice!
     * 4. query it directly
     * 5. compare both searches (only by URIs)
     * @throws java.lang.Exception
     */
    @Test
    public void testQueryingELOs() throws Exception{
        //create JSON from test-ELO
        IELO elo = getExampleElo();
        final String savedEloUri = ((URI)beans.getRepository().addNewELO(elo).getMetadataValueContainer(identifierKey).getValue()).toString();
        logger.info("saved ELO, URI: "+savedEloUri);
        logger.info("repository contains: "+beans.getRepository().retrieveELO(new URI(savedEloUri)));

        String searchString = "*:*";
        WebResource webResource = resource();
        webResource.accept("text/plain");
        ClientResponse response = webResource.path("query").type("text/plain").post(ClientResponse.class, searchString);
        JSONArray responseEntity = null;
        if (response.hasEntity()) {
            responseEntity = response.getEntity(JSONArray.class);
        }
        logger.info("found "+responseEntity.length()+" results via webservice");

        //Make a Hashset of all URIs (-> retrieved via Webservice)
        HashSet<String> resultsViaWebservice = new HashSet<String>();
        for(int i=0; i<responseEntity.length();i++){
            String uri = responseEntity.getJSONObject(i).getString("uri");
            if(!resultsViaWebservice.add(uri)){
                throw new Exception("Search results contain same uri twice (or more).");
            }
        }
        logger.info("response entity: " + responseEntity);

        //final List<ISearchResult> searchResults = beans.getRepository().search(new LuceneQuery(searchString));
        final List<ISearchResult> searchResults = new ArrayList<ISearchResult>();
        logger.info("found "+searchResults.size()+" results in repo");
        //Make a Hashset of all URIs (-> retrieved directly from repository)
        HashSet<String> resultsFromRepository = new HashSet<String>();
        for (ISearchResult result : searchResults){
            resultsFromRepository.add(result.getUri().toString());
        }

        //Alternative search - to see if the repo could return ELOs via alternative search engine
        IQueryComponent queryComponent = new MetadataQueryComponent(typeKey, SearchOperation.EQUALS, "scy/webresourcer");
        IQuery query = new Query(queryComponent);
        logger.info("Alternative search brought "+beans.getRepository().search(query).size()+" results.");

        Assert.assertEquals(resultsFromRepository.size(), resultsViaWebservice.size());
        Assert.assertEquals(resultsFromRepository, resultsViaWebservice);
    }

//    @Test
//    public void testQueryingELOs() {
//        IELO elo = getExampleElo();
//        beans.getRepository().addNewELO(elo);
//        IQuery query = new BasicMetadataQuery(typeKey, BasicSearchOperations.EQUALS, (String) elo.getMetadata().getMetadataValueContainer(typeKey).getValue(), null);
//        //TODO replace by /search Webservice
//        final List<ISearchResult> searchResults = beans.getRepository().search(query);
//        for (ISearchResult searchResult : searchResults) {
//            logger.info("SearchResult URI: " + searchResult.getUri());
//        }
//        logger.info("SearchResult URI (size): " + searchResults.size());
//    }
    public IELO getExampleElo() {
        IELO elo = beans.getEloFactory().createELO();

        //ELO content
        IContent content = beans.getEloFactory().createContent();
        Locale locale = new Locale("de");
        content.setLanguage(locale);
        content.setXmlString("<test>this is testcontent</test>");
        elo.setContent(content);

        //ELO metadata
        elo.getMetadata().getMetadataValueContainer(typeKey).setValue("scy/webresourcer");
        Contribute contribute = new Contribute();
        contribute.setVCard("testuser");
        elo.getMetadata().getMetadataValueContainer(authorKey).addValue(contribute);
        elo.getMetadata().getMetadataValueContainer(titleKey).setValue("Test Title");
        elo.getMetadata().getMetadataValueContainer(dateCreatedKey).setValue(System.currentTimeMillis());
        elo.getMetadata().getMetadataValueContainer(descriptionKey).setValue("Description Test");
        return elo;
    }

    private JSONObject getExampleEloAsJson(IELO elo) throws JSONException {
        JSONObject jsonData = new JSONObject();
        jsonData.put("content", elo.getContent().getXmlString());
        jsonData.put("description", (String) elo.getMetadata().getMetadataValueContainer(descriptionKey).getValue());
        jsonData.put("author", ((Contribute) elo.getMetadata().getMetadataValueContainer(authorKey).getValue()).getVCard());
        jsonData.put("title", (String) elo.getMetadata().getMetadataValueContainer(titleKey).getValue());
        jsonData.put("type", (String) elo.getMetadata().getMetadataValueContainer(typeKey).getValue());
        jsonData.put("dateCreated", ((Long) elo.getMetadata().getMetadataValueContainer(dateCreatedKey).getValue()));
        jsonData.put("language", elo.getContent().getLanguages());
        jsonData.put("username", "testuser");
        jsonData.put("password", "testpassword");

        return jsonData;
    }

    private void initMetadataKeys() {
        authorKey = beans.getTypeManager().getMetadataKey(CoreRooloMetadataKeyIds.AUTHOR.getId());
        typeKey = beans.getTypeManager().getMetadataKey(CoreRooloMetadataKeyIds.TECHNICAL_FORMAT.getId());
        titleKey = beans.getTypeManager().getMetadataKey(CoreRooloMetadataKeyIds.TITLE.getId());
        dateCreatedKey = beans.getTypeManager().getMetadataKey(CoreRooloMetadataKeyIds.DATE_CREATED.getId());
        descriptionKey = beans.getTypeManager().getMetadataKey(CoreRooloMetadataKeyIds.DESCRIPTION.getId());
        identifierKey = beans.getTypeManager().getMetadataKey(CoreRooloMetadataKeyIds.IDENTIFIER.getId());
    }
}

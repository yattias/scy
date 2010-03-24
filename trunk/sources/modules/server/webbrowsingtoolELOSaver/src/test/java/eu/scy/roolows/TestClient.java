package eu.scy.roolows;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.WebAppDescriptor;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.Locale;
import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import roolo.elo.api.IContent;
import roolo.elo.api.IELO;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;
import roolo.elo.metadata.keys.BasicMetadataKey;
import roolo.elo.metadata.keys.Contribute;
import roolo.elo.metadata.keys.ContributeMetadataKey;
import roolo.elo.metadata.keys.LongMetadataKey;
import roolo.elo.metadata.keys.RelationMetadataKey;
import roolo.elo.metadata.keys.StringMetadataKey;
import roolo.elo.metadata.keys.UriMetadataKey;

/**
 * Tests the REST Webservice as a client using jersey testframework and jUnit 4
 * 
 * @author __SVEN__
 */
public class TestClient extends JerseyTest {

    private static final ConfigLoader configLoader = ConfigLoader.getInstance();
    private static final Logger logger = Logger.getLogger(TestClient.class.getName());
    private IMetadataKey authorKey;
    private IMetadataKey typeKey;
    private IMetadataKey titleKey;
    private IMetadataKey dateCreatedKey;
    private IMetadataKey descriptionKey;

    public static IELO parseJsonELO(JSONObject json) throws URISyntaxException {
        try {
            JSONObject metadata = json.getJSONObject("metadata");
            String contentString = json.getString("content");
            final Iterator keys = metadata.keys();
            IELO elo = configLoader.getEloFactory().createELO();
            //set elo metadata
            while (keys.hasNext()) {
                final String key = (String) keys.next();
                final Object value = metadata.get(key);
                final IMetadataKey metadatakey = configLoader.getTypeManager().getMetadataKey(key);
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
                        ///this has to be done, because JCR-RoOLO uses String version numbers and RoOLO-Mock uses Integer version numbers
                        if (value instanceof Integer) {
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
            IContent eloContent = configLoader.getEloFactory().createContent();
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
        super(new WebAppDescriptor.Builder("eu.scy.roolows").contextPath("scy-roolo-ws").build());
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
            elo = configLoader.getRepository().retrieveELO(new URI(eloUri));

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

//    @Test
//    public void testQueryingELOs() {
//        IELO elo = getExampleElo();
//        configLoader.getRepository().addNewELO(elo);
//        IQuery query = new BasicMetadataQuery(typeKey, BasicSearchOperations.EQUALS, (String) elo.getMetadata().getMetadataValueContainer(typeKey).getValue(), null);
//        //TODO replace by /search Webservice
//        final List<ISearchResult> searchResults = configLoader.getRepository().search(query);
//        for (ISearchResult searchResult : searchResults) {
//            logger.info("SearchResult URI: " + searchResult.getUri());
//        }
//        logger.info("SearchResult URI (size): " + searchResults.size());
//    }
    public IELO getExampleElo() {
        IELO elo = configLoader.getEloFactory().createELO();

        //ELO content
        IContent content = configLoader.getEloFactory().createContent();
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
        Locale locale = new Locale("de");
        elo.setDefaultLanguage(locale);
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
        jsonData.put("language", elo.getDefaultLanguage());
        jsonData.put("username", "testuser");
        jsonData.put("password", "testpassword");

        return jsonData;
    }

    private void initMetadataKeys() {
        authorKey = configLoader.getTypeManager().getMetadataKey(CoreRooloMetadataKeyIds.AUTHOR.getId());
        typeKey = configLoader.getTypeManager().getMetadataKey(CoreRooloMetadataKeyIds.TECHNICAL_FORMAT.getId());
        titleKey = configLoader.getTypeManager().getMetadataKey(CoreRooloMetadataKeyIds.TITLE.getId());
        dateCreatedKey = configLoader.getTypeManager().getMetadataKey(CoreRooloMetadataKeyIds.DATE_CREATED.getId());
        descriptionKey = configLoader.getTypeManager().getMetadataKey(CoreRooloMetadataKeyIds.DESCRIPTION.getId());
    }
}

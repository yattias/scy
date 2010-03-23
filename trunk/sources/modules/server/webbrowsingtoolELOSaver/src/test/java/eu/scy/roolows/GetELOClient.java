package eu.scy.roolows;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.WebAppDescriptor;
import eu.scy.webbrowsingtoolelosaver.ConfigLoader;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import javax.ws.rs.core.MediaType;
import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import roolo.api.search.IQuery;
import roolo.api.search.ISearchResult;
import roolo.elo.BasicMetadataQuery;
import roolo.elo.BasicSearchOperations;
import roolo.elo.api.IContent;
import roolo.elo.api.IELO;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;

/**
 * Hello world!
 *
 */
public class GetELOClient extends JerseyTest {

    private static final ConfigLoader configLoader = ConfigLoader.getInstance();
    private static final Logger logger = Logger.getLogger(GetELOClient.class.getName());

    public static IELO parseJsonELO(JSONObject json) {
        try {
            JSONObject metadata = json.getJSONObject("metadata");
            String contentString = json.getString("content");
            final Iterator keys = metadata.keys();
            IELO elo = configLoader.getEloFactory().createELO();
            //set elo metadata
            while (keys.hasNext()) {
                final Object key = keys.next();
                elo.getMetadata().getMetadataValueContainer(configLoader.getTypeManager().getMetadataKey((String) key)).setValue(metadata.get((String) key));
            }
            //set elo content
            IContent eloContent = configLoader.getEloFactory().createContent();
            eloContent.setXmlString(contentString);
            elo.setContent(eloContent);

            return elo;
        } catch (JSONException ex) {
            logger.error(ex.getMessage());
        }
        return null;
    }

    public GetELOClient() {
        super(new WebAppDescriptor.Builder("eu.scy.webbrowsingtoolelosaver").contextPath("scy-roolo-ws").build());
    }

    /**
     * Test that the expected response is sent back.
     * @throws java.lang.Exception
     */
    @Test
    public void testEchoAlive() throws Exception {
        WebResource webResource = resource();
        String responseMsg = webResource.path("saveELO").get(String.class);
        Assert.assertEquals("<html><body><h1>Hello World!</body></h1></html>", responseMsg);
    }

    public static void main(String[] args) throws IOException {

        //search an ELO URI

        Client client = Client.create();
        WebResource webResource = client.resource("http://localhost:33605/roolo-ws/webresources/getELO");
        String eloUri = "roolo://memory/512/0/new_Empty_concept_map_1.scymapping";
//        webResource.entity(eloUri, MediaType.TEXT_PLAIN);
        webResource.accept(MediaType.APPLICATION_JSON_TYPE);
        ClientResponse response = webResource.type("text/plain").post(ClientResponse.class, eloUri);
        if (response.hasEntity()) {
            System.out.println("response.getType(): " + response.getType());
            final JSONObject entity = response.getEntity(JSONObject.class);
            final IELO parsedELO = parseJsonELO(entity);
            System.out.println("response: " + entity.toString());
            IMetadataKey typeKey = configLoader.getTypeManager().getMetadataKey(CoreRooloMetadataKeyIds.TECHNICAL_FORMAT.getId());
            final Object technicalFormat = parsedELO.getMetadata().getMetadataValueContainer(typeKey).getValue();
            logger.info("ELO-type: " + technicalFormat);
            IQuery query = new BasicMetadataQuery(typeKey, BasicSearchOperations.EQUALS, technicalFormat, null);
            final List<ISearchResult> searchResults = configLoader.getRepository().search(query);
            for (ISearchResult searchResult : searchResults) {
                logger.info("SearchResult URI: " + searchResult.getUri());
            }
            logger.info("SearchResult URI (size): " + searchResults.size());
        }
    }
//    @Test
//     public static void testSaveAndRetrieve(String[] args) throws IOException {
//        Client client = Client.create();
//        WebResource webResource = client.resource("http://localhost:33605/roolo-ws/webresources/getELO");
//        String eloUri = "roolo://memory/512/0/new_Empty_concept_map_1.scymapping";
////        webResource.entity(eloUri, MediaType.TEXT_PLAIN);
//        webResource.accept(MediaType.APPLICATION_JSON_TYPE);
//        ClientResponse response = webResource.type("text/plain").post(ClientResponse.class, eloUri);
//        if (response.hasEntity()) {
//            System.out.println("response.getType(): " + response.getType());
//            final JSONObject entity = response.getEntity(JSONObject.class);
//            final IELO parsedELO = parseJsonELO(entity);
//            System.out.println("response: " + entity.toString());
//
//        }
//    }
}

package eu.scy.roolows;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import roolo.elo.api.IELO;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.IMetadataValueContainer;
import roolo.elo.api.metadata.MetadataValueCount;
import roolo.elo.metadata.keys.Contribute;

/**
 * REST Web Service
 * 
 * @author __SVEN__
 */
@Path("/getELO")
public class GetELO {

    private static final Beans beans = Beans.getInstance();
    private final static Logger logger = Logger.getLogger(GetELO.class.getName());
    @Context
    private UriInfo context;

    /** Creates a new instance of SaveELOResource */
    public GetELO() {
    }

    /**
     * GET method for testing purpose: returns a "Hello World" html-doc
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("text/html")
    public String echoAlive() {
        return "<html><body><h1>Hello World!</body></h1></html>";
    }

    /**
     * POST method for retrieving an ELO from RoOLO (specified by config) as JSON.
     * This service consumes <b>text/plain</b> consisting of the URI of the ELO
     * @param uri The String of the ELO to retrieve
     * @return The ELO mapped to JSON
     */
    @POST
    @Consumes("text/plain")
    @Produces("application/json")
    public JSONObject getELO(String uri) {
        JSONObject eloAsJson = new JSONObject();
        JSONObject metadata = new JSONObject();

        try {
            IELO elo = beans.getRepository().retrieveELO(new URI(uri));
            if (elo != null) {
                //TODO: do the flattening specific for different types of metadata keys
                for (IMetadataKey metadataKey : elo.getMetadata().getAllMetadataKeys()) {
                	IMetadataValueContainer container = elo.getMetadata().getMetadataValueContainer(metadataKey);
                	if (container.getKey().getMetadataValueCount() == MetadataValueCount.SINGLE) {
                		final Object value = container.getValue();
                		if (value == null) {
                			metadata.put(metadataKey.getId(), JSONObject.NULL);
                		} else {
            				metadata.put(metadataKey.getId(), value);
                		}
                	} else {
                		final List<?> valueList = container.getValueList();
                		if (valueList == null) {
                			metadata.put(metadataKey.getId(), JSONObject.NULL);
                		} else {
                			JSONArray array = new JSONArray();
                    		for (Object value : valueList) {
                    			if (value instanceof Contribute) {
                    				JSONObject contribute = new JSONObject();
                    				contribute.put("vcard", ((Contribute) value).getVCard());
                    				contribute.put("date", ((Contribute) value).getDate());
                    				array.put(contribute);
                    			} else {
                    				array.put(value);
                    			}
    						}
            				metadata.put(metadataKey.getId(), array);
                		}
                	}
                }
                String contentString = elo.getContent().getXmlString();
                JSONArray contentLanguages =new JSONArray(elo.getContent().getLanguages());

                eloAsJson.put("metadata", metadata);
                eloAsJson.put("content", contentString);
                eloAsJson.put("contentLanguages", contentLanguages);
                logger.info("retrieved ELO: " + eloAsJson);
                return eloAsJson;
            }
        } catch (JSONException ex) {
            logger.error(ex.getMessage());
        } catch (URISyntaxException ex) {
            logger.error(ex.getMessage());
        }
        return null;
    }
}

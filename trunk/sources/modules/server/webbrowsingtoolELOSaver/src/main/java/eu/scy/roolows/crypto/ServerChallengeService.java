package eu.scy.roolows.crypto;

import eu.scy.roolows.*;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import org.apache.log4j.Logger;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import roolo.elo.api.exceptions.ELONotAddedException;

/**
 * REST Web Service
 *
 * @author __SVEN__
 */
@Path("/serverChallenge")
public class ServerChallengeService {

    @Context
    private UriInfo context;
    private static final Beans beans = Beans.getInstance();
    private static final Logger log = Logger.getLogger(ServerChallengeService.class.getName());
    private static final LoginManager loginManager = LoginManager.getInstance();

    /** Creates a new instance of ChallengeResponse */
    public ServerChallengeService() {
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
     * POST method for creating a new ELO in RoOLO (specified by config). 
     * This service consumes <b>JSON data</b> consisting of the following parameters:
     * <ul>
     * <li>  content: the content of the ELO
     * <li>  username: username for authentication
     * <li>  password: password of the user for authentication
     * <li>  language: language of the ELO (ISO-639)
     * <li>  title: title of the ELO
     * <li>  type: technical format of the ELO, e.g. "scy/webresourcer" for SCYLighter/Webresourcer ELOs
     * </ul>
     * and these optional parameters:
     * <ul>
     * <li>  uri: the URI of the ELO to update. If specified, not a new ELO will be created, but an existing one will be updated
     * <li>  country: country code (ISO-3166)
     * <li>  description: a short description of the ELO
     * <li>  dateCreated: The date of ELO creation - if this is not specified, it will be created.
     * </ul>
     * @param jsonData The data formed as json, of which the ELO will be created - or updated
     * @return the URI of the saved/updated ELO
     */
    @POST
    @Consumes("application/json")
    @Produces("text/plain")
    public String challengeRespsonse(JSONObject jsonData) {
        String username = null;
        String cc = null;
        try {
            username = jsonData.getString("username");
            cc = jsonData.optString("cc", null);
            String sc = createSC(username);
            if (cc != null) {
                loginManager.getChallenge(username).setCc(cc);
            }
            return sc;
        } catch (JSONException ex) {
            log.error(ex.getMessage());
        } catch (ELONotAddedException ex) {
            log.error(ex.getMessage());
        } catch (Exception ex) {
            //uncaught Exceptions, for example from Spring
            log.error(ex);
        }
        return null;
    }

    private String createSC(String username) {
        String sc = loginManager.addChallenge(username);
        log.info("created challenge for user " + username + ", value:" + sc);
        return sc;
    }

    private String createSR(String username) {
        String sr = loginManager.getChallenge(username).calculateSr();
        log.info("created server response for user " + username + ", value:" + sr);
        return sr;
    }
}

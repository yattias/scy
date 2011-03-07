package eu.scy.roolows;

import eu.scy.actionlogging.api.ContextConstants;
import eu.scy.actionlogging.api.IAction;
import eu.scy.actionlogging.Action;
import eu.scy.roolows.crypto.ChallengeEntity;
import eu.scy.roolows.crypto.LoginManager;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Locale;

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

import roolo.elo.api.IContent;
import roolo.elo.api.IELO;
import roolo.elo.api.IMetadata;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.IMetadataValueContainer;
import roolo.elo.api.exceptions.ELONotAddedException;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;
import roolo.elo.metadata.keys.Contribute;
import roolo.elo.metadata.value.containers.MetadataSingleUniversalValueContainer;

/**
 * REST Web Service
 *
 * @author __SVEN__
 */
@Path("/saveELO")
public class SaveELO {

    @Context
    private UriInfo context;
    private static final Beans beans = Beans.getInstance();
    private static final Logger log = Logger.getLogger(SaveELO.class.getName());
    private static final LoginManager loginManager = LoginManager.getInstance();
    private IELO elo;
    private IMetadataKey titleKey;
    private IMetadataKey typeKey;
    private IMetadataKey dateCreatedKey;
    private IMetadataKey authorKey;
    private IMetadataKey identifierKey;
    private IMetadataKey descriptionKey;

    /** Creates a new instance of SaveELO */
    public SaveELO() {
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
    public String saveELO(JSONObject jsonData) {
        initMetadataKeys();

        String content = null;
        String username = null;
        String language = null;
        String title = null;
        String type = null;
        String uri = null;
        String country = null;
        String description = null;
        String dateCreated = null;
        String password = null;
        String authType = null;
        try {
            //non-optional parameters:
            content = jsonData.getString("content");
            username = jsonData.getString("username");
            language = jsonData.getString("language");
            title = jsonData.getString("title");
            type = jsonData.getString("type");
            //optional parameters:
            uri = jsonData.optString("uri", null);
            description = jsonData.optString("description", null);
            country = jsonData.optString("country", null);
            dateCreated = jsonData.optString("DateCreated", null);
            authType = jsonData.optString("authType", null);
            password = jsonData.optString("password", null);

            //TODO authenticate User!
            boolean authenticated = auth(authType, username, password);
            if (authenticated) {
                //Authentication ok
                if (uri != null) {
                    //update ELO
                    elo = beans.getRepository().retrieveELO(new URI(uri));
                    if (elo != null) {
                        elo.setContent(createELOContent(language, country, content));
                        beans.getRepository().updateELO(elo);
                        log.info("Updated ELO with uri: " + uri);
                        logSavedELOAction(username, uri, "elo_updated", type);
                    } else {
                        log.error("could not update! <- Could not retrieve ELO (ELO is null)");
                        //since the ELO could not be retrieved, the URI isnt correct
                        uri = null;
                    }
                } else {
                    //save ELO
                    elo = beans.getEloFactory().createELO();
                    elo.setMetadata(createELOMetadata(username, title, type, dateCreated, description));
                    elo.setContent(createELOContent(language, country, content));
                    IMetadata metadata = beans.getRepository().addNewELO(elo);
                    uri = ((URI) metadata.getMetadataValueContainer(identifierKey).getValue()).toString();
                    log.info("Saved ELO with uri: " + uri);
                    logSavedELOAction(username, uri, "elo_saved", type);
                }
            } else {
                log.error("authentication of user " + username + " failed.");
            }
        } catch (JSONException ex) {
            log.error(ex.getMessage());
        } catch (URISyntaxException ex) {
            log.error(ex.getMessage());
        } catch (ELONotAddedException ex) {
            log.error(ex.getMessage());
        } catch (Exception ex) {
            //uncaught Exceptions, for example from Spring
            log.error(ex);
        } finally {
            //uri is null if the whole thing isnt working
            return uri;
        }
    }

    private void initMetadataKeys() {
        authorKey = beans.getTypeManager().getMetadataKey(CoreRooloMetadataKeyIds.AUTHOR.getId());
        typeKey = beans.getTypeManager().getMetadataKey(CoreRooloMetadataKeyIds.TECHNICAL_FORMAT.getId());
        titleKey = beans.getTypeManager().getMetadataKey(CoreRooloMetadataKeyIds.TITLE.getId());
        dateCreatedKey = beans.getTypeManager().getMetadataKey(CoreRooloMetadataKeyIds.DATE_CREATED.getId());
        identifierKey = beans.getTypeManager().getMetadataKey(CoreRooloMetadataKeyIds.IDENTIFIER.getId());
        descriptionKey = beans.getTypeManager().getMetadataKey(CoreRooloMetadataKeyIds.DESCRIPTION.getId());
    }

    private IContent createELOContent(String language, String country, String content) {
        IContent eloContent = beans.getEloFactory().createContent();
        Locale defaultLocale;
        if (country != null) { //country code is optional
            defaultLocale = new Locale(language, country);
        } else {
            defaultLocale = new Locale(language);
        }
        eloContent.setLanguage(defaultLocale);
        eloContent.setXmlString(content);
        return eloContent;
    }

    private IMetadata createELOMetadata(String username, String title, String type, String dateCreated, String description) {
        IMetadata metadata = beans.getEloFactory().createMetadata();
        Contribute contribute = new Contribute(username, System.currentTimeMillis());
        metadata.getMetadataValueContainer(authorKey).addValue(contribute);

        IMetadataValueContainer container = new MetadataSingleUniversalValueContainer(metadata, typeKey);
        if (!beans.getTypeManager().isMetadataKeyRegistered(typeKey)) {
            beans.getTypeManager().registerMetadataKey(typeKey);
        }
        metadata.addMetadataPair(typeKey, container);
        metadata.getMetadataValueContainer(titleKey).setValue(title);
        metadata.getMetadataValueContainer(typeKey).setValue(type);
        if (dateCreated == null) {
            //dateCreated is optional
            metadata.getMetadataValueContainer(dateCreatedKey).setValue(System.currentTimeMillis());
        } else {
            metadata.getMetadataValueContainer(dateCreatedKey).setValue(dateCreated);
        }
        if (description != null) {
            //description is optional
            metadata.getMetadataValueContainer(descriptionKey).setValue(description);
        }
        return metadata;
    }

    private void logSavedELOAction(String username, String eloUri, String type, String elo_type) {
        IAction action = new Action();
        action.setType(type);
        action.setUser(getSmackName(username));
        action.addContext(ContextConstants.eloURI, eloUri);
        action.addContext(ContextConstants.tool, "roolo-ws");
        action.addAttribute("elo_type", elo_type);
        action.addAttribute("elo_uri", eloUri);
        beans.getActionLogger().log(action);
    }

    private String getSmackName(String username) {
        return (username + "@" + beans.getServerConfig().getOpenFireHost() + "/Smack");
    }

    private boolean auth(String authType, String username, String password) {
        if ("challenge/response".equals(authType)) {
            //authentication was done before via challenge/response
            //now just remove the security token
            ChallengeEntity challenge = loginManager.getChallenge(username);
            if (challenge.isAuthorized()) {
                log.info("User "+username+" is authorized");
                loginManager.removeChallenge(username);
                return true;
            }
            log.warn("User "+username+" is NOT authorized");
            loginManager.removeChallenge(username);
            return false;
        } else {
            log.info("No AuthType chosen -> User "+username+" is authorized");
            return true;
        }
    }
}

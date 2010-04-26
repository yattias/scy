/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common
 * Development and Distribution License("CDDL") (collectively, the
 * "License"). You may not use this file except in compliance with the
 * License. You can obtain a copy of the License at
 * http://www.netbeans.org/cddl-gplv2.html
 * or nbbuild/licenses/CDDL-GPL-2-CP. See the License for the
 * specific language governing permissions and limitations under the
 * License.  When distributing the software, include this License Header
 * Notice in each file and include the License file at
 * nbbuild/licenses/CDDL-GPL-2-CP.  Sun designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the GPL Version 2 section of the License file that
 * accompanied this code. If applicable, add the following below the
 * License Header, with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 * 
 * Contributor(s):
 * 
 * The Original Software is NetBeans. The Initial Developer of the Original
 * Software is Sun Microsystems, Inc. Portions Copyright 1997-2007 Sun
 * Microsystems, Inc. All Rights Reserved.
 * 
 * If you wish your version of this file to be governed by only the CDDL
 * or only the GPL Version 2, indicate your decision by adding
 * "[Contributor] elects to include this software in this distribution
 * under the [CDDL or GPL Version 2] license." If you do not indicate a
 * single choice of license, a recipient has the option to distribute
 * your version of this file under either the CDDL, the GPL Version 2 or
 * to extend the choice of license to its licensees as provided above.
 * However, if you add GPL Version 2 code and therefore, elected the GPL
 * Version 2 license, then the option applies only if the new code is
 * made subject to such option by the copyright holder.
 */
package eu.scy.roolows;

import com.sun.jersey.spi.resource.Singleton;
import eu.scy.actionlogging.api.ContextConstants;
import eu.scy.actionlogging.api.IAction;
import eu.scy.actionlogging.Action;
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
    private static final Beans beans  = Beans.getInstance();
    private static final Logger log = Logger.getLogger(SaveELO.class.getName());
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
        String password = null;
        String language = null;
        String title = null;
        String type = null;
        String uri = null;
        String country = null;
        String description = null;
        String dateCreated = null;
        try {
            //non-optional parameters:
            content = jsonData.getString("content");
            username = jsonData.getString("username");
            password = jsonData.getString("password");
            language = jsonData.getString("language");
            title = jsonData.getString("title");
            type = jsonData.getString("type");
            //optional parameters:
            uri = jsonData.optString("uri", null);
            description = jsonData.optString("description", null);
            country = jsonData.optString("country", null);
            dateCreated = jsonData.optString("DateCreated", null);

            //TODO authenticate User!
            if (true) {
                //Authentication ok
                if (uri != null) {
                    //update ELO
                    elo = beans.getRepository().retrieveELO(new URI(uri));
                    if (elo != null) {
                        elo.setContent(createELOContent(language, country, content));
                        beans.getRepository().updateELO(elo);
                        log.info("Updated ELO with uri: " + uri);
                        logSavedELOAction(username, uri, "elo_update",type);
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
                    log.info("Elo created. Metadata and content set");
                    IMetadata metadata = beans.getRepository().addNewELO(elo);
                    uri = ((URI) metadata.getMetadataValueContainer(identifierKey).getValue()).toString();
                    log.info("Saved ELO with uri: " + uri);
                    logSavedELOAction(username, uri, "elo_save",type);
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
            log.error("**********CATCH ALL**************");
            log.error(ex);
            ex.printStackTrace();
            log.error("*********************************");
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

    private void logSavedELOAction(String username, String eloUri, String type,String elo_type) {
        IAction action = new Action();
        action.setType(type);
        action.setUser(getSmackName(username));
        action.addContext(ContextConstants.eloURI, eloUri);
        action.addContext(ContextConstants.tool, "roolo-ws");
        action.addAttribute("elo_type", elo_type);
        beans.getActionLogger().log(action);
    }

    private String getSmackName(String username) {
        return (username + "@" + beans.getServerConfig().getOpenFireHost() + "/Smack");
    }
}

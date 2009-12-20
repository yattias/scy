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
package eu.scy.webbrowsingtoolelosaver;

import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import org.apache.log4j.BasicConfigurator;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import roolo.elo.BasicELO;
import roolo.elo.api.IELO;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.IMetadataValueContainer;
import roolo.elo.api.exceptions.ELONotAddedException;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;
import roolo.elo.content.BasicContent;
import roolo.elo.metadata.keys.Contribute;
import roolo.elo.metadata.keys.ContributeMetadataKey;
import roolo.elo.metadata.value.containers.MetadataSingleUniversalValueContainer;

/**
 * REST Web Service
 *
 * @author __SVEN__
 */
@Path("/saveELO")
public class SaveELOResource {

    @Context
    private UriInfo context;
    private static final ConfigLoader configLoader = ConfigLoader.getInstance();
    private final static Logger log = Logger.getLogger(SaveELOResource.class.getName());
    private IELO elo;
    private IMetadataKey titleKey;
    private IMetadataKey typeKey;
    private IMetadataKey dateCreatedKey;
    private IMetadataKey missionKey;
    private ContributeMetadataKey authorKey;

    /** Creates a new instance of SaveELOResource */
    public SaveELOResource() {
        //configure the Logger
        BasicConfigurator.configure();
    }

    /**
     * Retrieves representation of an instance of saveelo.SaveELOResource
     * For testing purpose!
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("text/html")
    public String getXml() {
        return "<html><body><h1>Hello World!</body></h1></html>";
    }

    /**
     * PUT method for updating or creating an instance of SaveELOResource
     * For testing purpose!
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Consumes("application/xml")
    public void putXml(String content) {
    }

    /**
     * POST method for creating a new ELO Resource
     * @param summary the summary representation of the ELO content
     * @param username the username of the ELO's creator
     * @param password the password of the ELO's creator
     * @return an HTTP response with content of the updated or created resource.
     */
    @POST
    @Consumes("application/json")
    @Produces("text/plain")
    public String saveHtmlELO(JSONObject jsonData) {

        String annotations = null;
        String html = null;
        String preview = null;
        String username = null;
        String password = null;
        String language = null;
        String title = null;
        try {
            annotations = jsonData.getString("annotations");
            html = jsonData.getString("html");
            preview = jsonData.getString("preview");
            username = jsonData.getString("username");
            password = jsonData.getString("password");
            language = jsonData.getString("language");
            title = jsonData.getString("title");
        } catch (JSONException ex) {
            Logger.getLogger(SaveELOResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            //User user = new User(saveBean.getUsername(),saveBean.getPassword());
            //TODO authenticate User!
            if (true) { //XXX replace by real User-Management
                //Authentication ok

                //Creating the ELO
                elo = new BasicELO();

                log.info("ELO created");
                Locale defaultLocale = new Locale(language);
                elo.setDefaultLanguage(defaultLocale);

                //Authenticate the user and set real user name, not user-id
                authorKey = (ContributeMetadataKey)configLoader.getTypeManager().getMetadataKey(CoreRooloMetadataKeyIds.AUTHOR.getId());
                //FIXME replace username by vcard
                Contribute contribute = new Contribute(username, new Date().getTime());
                elo.getMetadata().getMetadataValueContainer(authorKey).addValue(contribute);

                typeKey = configLoader.getTypeManager().getMetadataKey(CoreRooloMetadataKeyIds.TECHNICAL_FORMAT.getId());
                IMetadataValueContainer container = new MetadataSingleUniversalValueContainer(elo.getMetadata(), typeKey);
                if (!configLoader.getTypeManager().isMetadataKeyRegistered(typeKey)) {
                    configLoader.getTypeManager().registerMetadataKey(typeKey);
                }
                elo.getMetadata().addMetadataPair(typeKey, container);
                
                titleKey = configLoader.getTypeManager().getMetadataKey(CoreRooloMetadataKeyIds.TITLE.getId());
                dateCreatedKey = configLoader.getTypeManager().getMetadataKey(CoreRooloMetadataKeyIds.DATE_CREATED.getId());

                //FIXME missionKey not set yet
                //                missionKey = configLoader.getTypeManager().getMetadataKey(CoreRooloMetadataKeyIds.);

                elo.getMetadata().getMetadataValueContainer(titleKey).setValue(title);

                configLoader.getExtensionManager().registerExtension("scy/html", ".xml");
                elo.getMetadata().getMetadataValueContainer(typeKey).setValue("scy/html");
                elo.getMetadata().getMetadataValueContainer(dateCreatedKey).setValue(new Date());
                log.info("Metadata set");
                
                //The content consists of a summary (annotations and excerpt), the whole html doc and the preview (the styled summary)
                String content = "<preview><![CDATA["+preview+"]]></preview>"+"<annotations> <![CDATA["+annotations+"]]></annotations>"+"\n <html> \n <![CDATA["+html+"]]> \n </html>";
                elo.setContent(new BasicContent(content));
                try {
                    configLoader.getRepository().addNewELO(elo);
                } catch (ELONotAddedException e) {
                    log.warning(e.getMessage());
                } catch (Exception e) {
                    log.warning(e.getMessage());
                }
                log.info("Added ELO to repository");

                //return simplified codes for easier localization!
                //ELO saved
                return "eloSaved";
            } else {//Authentication failed!
                //return simplified codes for easier localization!
                //Login failed. Please Check Your Login-Data
                return "loginFailedCheckLoginData";
            }
        } catch (Exception e) {//Exception: Filewriting didnt work
            log.warning(e.getMessage());
            //return simplified codes for easier localization!
            //Server-Error during saving ELO. The Admin should clean up his harddisk
            return "serverErrorSaving";
        }
    }
}

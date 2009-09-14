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

import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
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
import roolo.elo.api.I18nType;
import roolo.elo.api.IELO;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.IMetadataValueContainer;
import roolo.elo.api.exceptions.ELONotAddedException;
import roolo.elo.api.metadata.MetadataValueCount;
import roolo.elo.content.BasicContent;
import roolo.elo.metadata.keys.StringMetadataKey;
import roolo.elo.metadata.value.containers.MetadataSingleUniversalValueContainer;
import roolo.elo.metadata.value.validators.StringValidator;

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
    private IMetadataKey uriKey;
    private IMetadataKey titleKey;
    private IMetadataKey typeKey;
    private IMetadataKey dateCreatedKey;
    private IMetadataKey missionKey;
//    private IMetadataKey authorKey;
    private IMetadataKey technicalFormat;

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
     * @param xml the xml representation of the ELO content
     * @param username the username of the ELO's creator
     * @param password the password of the ELO's creator
     * @return an HTTP response with content of the updated or created resource.
     */
    @POST
    @Consumes("application/json")
    @Produces("text/plain")
    public String saveHtmlELO(JSONObject jsonData) {

        String xml = null;
        String username = null;
        String password = null;
        try {
            xml = jsonData.getString("xml");
            username = jsonData.getString("username");
            password = jsonData.getString("password");
        } catch (JSONException ex) {
            Logger.getLogger(SaveELOResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            //User user = new User(saveBean.getUsername(),saveBean.getPassword());
            //TODO authenticate User!
            if (true) { //XXX replace by real User-Management
                //Authentication ok

                //Creating Timestamp for title or filename, safe for Filesystem!!!
                Calendar calendar = new GregorianCalendar();
                DateFormat df = DateFormat.getDateInstance();
                df.setCalendar(calendar);
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                String timestamp = Integer.toString(year) + "_" + Integer.toString(month) + "_" + Integer.toString(day) + "__" + hour + "-" + minute;
                /*String directory = "D:\\";
                String filename = username;
                String fileType = ".xml";*/

                //Creating the ELO
                elo = configLoader.getConfig().getEloFactory().createELO();

                log.info("ELO created");
                elo.setDefaultLanguage(Locale.ENGLISH);

                typeKey = new StringMetadataKey("type", "/testpath/", I18nType.UNIVERSAL, MetadataValueCount.SINGLE, new StringValidator());
                IMetadataValueContainer container = new MetadataSingleUniversalValueContainer(elo.getMetadata(), typeKey);
                if (!configLoader.getTypeManager().isMetadataKeyRegistered(typeKey)) {
                    configLoader.getTypeManager().registerMetadataKey(typeKey);
                }
                elo.getMetadata().addMetadataPair(typeKey, container);
                String docName = "Webbrowsing-ELO by " + username + " at " + timestamp;

                /*//Logging the Key IDs...
                log.warning("MetadataKeys-Size: " + configLoader.getTypeManager().getMetadataKeys().size());
                Collection col = configLoader.getTypeManager().getMetadataKeys();
                for (Iterator it = col.iterator(); it.hasNext();) {
                    IMetadataKey key = (IMetadataKey) it.next();
                    log.info("ID of the Key: " + key.getId());
                }*/

                //FIX Enum hardcoded values from RooloMetadataKeys from jcr-elo lib, which collides to the elo-api
                titleKey = configLoader.getTypeManager().getMetadataKey("title");
                typeKey = configLoader.getTypeManager().getMetadataKey("type");
                dateCreatedKey = configLoader.getTypeManager().getMetadataKey("dateCreated");
                missionKey = configLoader.getTypeManager().getMetadataKey("mission");
//                authorKey = configLoader.getTypeManager().getMetadataKey("author");
                technicalFormat = configLoader.getTypeManager().getMetadataKey("technicalFormat");

                elo.getMetadata().getMetadataValueContainer(titleKey).setValue(docName);
//                authorKey = new StringMetadataKey(id, path, type, metadataValueCount, validator);

                elo.getMetadata().getMetadataValueContainer(titleKey).setValue(docName, Locale.CANADA);

                configLoader.getExtensionManager().registerExtension("scy/html", ".xml");
                elo.getMetadata().getMetadataValueContainer(typeKey).setValue("scy/html");
                elo.getMetadata().getMetadataValueContainer(technicalFormat).setValue("scy/text");
                elo.getMetadata().getMetadataValueContainer(dateCreatedKey).setValue(new Long(System.currentTimeMillis()));
                try {
                    elo.getMetadata().getMetadataValueContainer(missionKey).setValue(new URI("roolo://somewhere/myMission.mission"));
//                    elo.getMetadata().getMetadataValueContainer(authorKey).setValue(new Contribute("my vcard", System.currentTimeMillis()));
//                    log.info("Value of authorKey: " + elo.getMetadata().getMetadataValueContainer(authorKey).getValue().toString());
                } catch (URISyntaxException e) {
                    log.log(Level.WARNING, "failed to create uri", e);
                }
                log.info("Metadata set");
                elo.setContent(new BasicContent(xml));
                try {
                    configLoader.getRepository().addNewELO(elo);
                } catch (ELONotAddedException e) {
                    log.warning(e.getMessage());
                } catch (Exception e) {
                    log.warning(e.getMessage());
                }
                log.info("Added ELO to repository");

                /*File file = new File(directory + filename + timestamp + fileType);
                FileWriter fw = new FileWriter(file);
                fw.write(xml);
                fw.close();*/
                
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

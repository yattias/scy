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
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
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
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import roolo.api.search.IMetadataQuery;
import roolo.api.search.ISearchResult;
import roolo.cms.repository.mock.BasicMetadataQuery;
import roolo.cms.repository.search.BasicSearchOperations;
import roolo.elo.api.I18nType;
import roolo.elo.api.IELO;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.metadata.MetadataValueCount;
import roolo.elo.metadata.keys.StringMetadataKey;
import roolo.elo.metadata.value.validators.StringValidator;

/**
 * REST Web Service
 *
 * @author __SVEN__
 */
@Path("/getELOList")
public class GetELOResource {

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
    IMetadataQuery query;
    private Vector<IELO> retrievedELOs;

    /** Creates a new instance of SaveELOResource */
    public GetELOResource() {
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
    @Produces("application/json")
    public JSONObject getHtmlELO(JSONObject jsonData) {

        String author = null;
        String title = null;
        String date = null;
        String keywords = null;
        String username = null;
//        String password = null;
        try {
            author = jsonData.getString("author");
            title = jsonData.getString("title");
            date = jsonData.getString("date");
            keywords = jsonData.getString("keywords");
            username = jsonData.getString("username");
//            password = jsonData.getString("password");
        } catch (JSONException ex) {
            Logger.getLogger(SaveELOResource.class.getName()).log(Level.SEVERE, null, ex);
        }

        typeKey = new StringMetadataKey("type", "/testpath/", I18nType.UNIVERSAL, MetadataValueCount.SINGLE, new StringValidator());
        query = new BasicMetadataQuery(typeKey, BasicSearchOperations.EQUALS, "scy/html", null);
        log.info(query.toString());

        try {
            //User user = new User(saveBean.getUsername(),saveBean.getPassword());
            //TODO authenticate User!
            if (true) { //XXX replace by real User-Management
                //Authentication ok

                //This query was only for testing purpose. Real queries would be passed to this service.
                IMetadataKey searchKey = configLoader.getTypeManager().getMetadataKey("technicalFormat");
                IMetadataQuery metadataQuery = new BasicMetadataQuery(searchKey, BasicSearchOperations.EQUALS, "scy/text", null);

                //make a query to the repository, retrieve a list of search results, create a List of ELOS
                List<ISearchResult> searchResultList = configLoader.getRepository().search(metadataQuery);
                log.info(searchResultList.size() + " search results foung.");
                
                JSONArray elos = new JSONArray();

                for (Iterator<ISearchResult> it = searchResultList.iterator(); it.hasNext();) {
                    ISearchResult searchResult = it.next();
                    IELO retrievedElo = configLoader.getRepository().retrieveELO(searchResult.getUri());
                    URI uri = searchResult.getUri();

                    IMetadataKey titleKey = configLoader.getTypeManager().getMetadataKey("title");
                    IMetadataKey dateCreatedKey = configLoader.getTypeManager().getMetadataKey("dateCreated");

                    String eloTitle = configLoader.getRepository().retrieveELO(uri).getMetadata().getMetadataValueContainer(titleKey).getValue().toString();
                    String eloDate = configLoader.getRepository().retrieveELO(uri).getMetadata().getMetadataValueContainer(dateCreatedKey).getValue().toString();

                    JSONObject eloAsJson = new JSONObject();
                    eloAsJson.put("title",eloTitle);
                    eloAsJson.put("date",eloDate);
                    eloAsJson.put("uri", searchResult.getUri().toString());
                    eloAsJson.put("author", "Sven");

                    elos.put(eloAsJson);
                    log.info("Retrieved ELO with content: \n" + retrievedElo.getContent() + "\nfrom searchResults");
                }
                
                JSONObject output = new JSONObject();
                output.accumulate("errors", "none");
                output.accumulate("elos", elos);

                log.warning("===========================================");
                log.warning("json-String of the output");
                log.warning("===========================================");
                log.warning(output.toString());
                log.warning("===========================================");
                return output;
            } else {//Authentication failed!

                JSONObject output = new JSONObject();
                output.accumulate("errors", "authentication_failed");
                output.accumulate("elos", null);
                return output;
                //return simplified codes for easier localization!
                //Login failed. Please Check Your Login-Data
            }
        } catch (Exception e) {//Exception: Filewriting didnt work
            log.warning(e.getMessage());
            //return simplified codes for easier localization!
            //Server-Error during saving ELO. The Admin should clean up his harddisk
            JSONObject output = new JSONObject();
            try {
                output.accumulate("errors", "server_error");
                output.accumulate("elos", null);

            } catch (JSONException ex) {
                log.warning(ex.getMessage());
            }

            return output;
        }
    }
}

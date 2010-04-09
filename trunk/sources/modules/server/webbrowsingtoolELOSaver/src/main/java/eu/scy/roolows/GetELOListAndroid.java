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

import java.net.URI;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import roolo.api.search.IMetadataQuery;
import roolo.api.search.ISearchResult;
import roolo.elo.BasicMetadataQuery;
import roolo.elo.BasicSearchOperations;
import roolo.elo.api.IELO;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;

/**
 * REST Web Service
 *
 * @author __SVEN__
 */
@Deprecated
@Path("/getELOListAndroid")
public class GetELOListAndroid {

	private final static Logger logger = Logger.getLogger(SaveELOResource.class.getName());

	private static final ConfigLoader configLoader = ConfigLoader.getInstance();
	
    @Context
    private UriInfo context;

    private IMetadataKey titleKey;
    private IMetadataKey typeKey;
    private IMetadataKey authorKey;
    private IMetadataKey descriptionKey;
    
    private IMetadataQuery query;

    /** Creates a new instance of SaveELOResource */
    public GetELOListAndroid() {
        
        //configure keys
        typeKey = configLoader.getTypeManager().getMetadataKey(CoreRooloMetadataKeyIds.TECHNICAL_FORMAT.getId());
        titleKey = configLoader.getTypeManager().getMetadataKey(CoreRooloMetadataKeyIds.TITLE.getId());
        descriptionKey = configLoader.getTypeManager().getMetadataKey(CoreRooloMetadataKeyIds.DESCRIPTION.getId());
        authorKey = configLoader.getTypeManager().getMetadataKey(CoreRooloMetadataKeyIds.AUTHOR.getId());
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

    	query = new BasicMetadataQuery(typeKey, BasicSearchOperations.EQUALS, "scy/form", null);
        logger.info(query.toString());

        try {
            //User user = new User(saveBean.getUsername(),saveBean.getPassword());
            //TODO authenticate User!
            if (true) { //XXX replace by real User-Management
                //Authentication ok

                //make a query to the repository, retrieve a list of search results, create a List of ELOS
                List<ISearchResult> searchResultList = configLoader.getRepository().search(query);
                logger.info(searchResultList.size() + " search results found.");
                
                JSONArray elos = new JSONArray();

                for (Iterator<ISearchResult> it = searchResultList.iterator(); it.hasNext();) {
                    ISearchResult searchResult = it.next();
                    URI uri = searchResult.getUri();
                    IELO retrievedElo = configLoader.getRepository().retrieveELO(uri);
                    String eloTitle = retrievedElo.getMetadata().getMetadataValueContainer(titleKey).getValue().toString();
                    String eloDescription = retrievedElo.getMetadata().getMetadataValueContainer(descriptionKey).getValue().toString();
                    String eloAuthor = retrievedElo.getMetadata().getMetadataValueContainer(authorKey).getValue().toString();

                    JSONObject eloAsJson = new JSONObject();
                    eloAsJson.put("title",eloTitle);
                    eloAsJson.put("description",eloDescription);
                    eloAsJson.put("uri", searchResult.getUri().toString());
                    eloAsJson.put("author", eloAuthor);

                    elos.put(eloAsJson);
                    logger.info("Retrieved ELO with content: \n" + retrievedElo.getContent() + "\nfrom searchResults");
                }
                
                JSONObject output = new JSONObject();
                output.accumulate("errors", "none");
                output.accumulate("elos", elos);
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
            logger.warning(e.getMessage());
            //return simplified codes for easier localization!
            //Server-Error during saving ELO. The Admin should clean up his harddisk
            JSONObject output = new JSONObject();
            try {
                output.accumulate("errors", "server_error");
                output.accumulate("elos", null);

            } catch (JSONException ex) {
                logger.warning(ex.getMessage());
            }
            return output;
        }
    }
}

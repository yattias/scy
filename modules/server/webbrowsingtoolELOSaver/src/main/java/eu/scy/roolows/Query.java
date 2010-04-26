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
import java.util.List;
import java.util.Vector;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;
import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONArray;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import roolo.api.search.AndQuery;
import roolo.api.search.IQuery;
import roolo.api.search.ISearchResult;
import roolo.elo.BasicMetadataQuery;
import roolo.elo.BasicSearchOperations;

import roolo.elo.api.IELO;
import roolo.elo.api.IMetadata;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;
import roolo.search.LuceneQuery;

/**
 * REST Web Service
 *
 * @author __SVEN__
 */
@Path("/query")
public class Query {

    @Context
    private UriInfo context;
    private static final Beans beans = Beans.getInstance();
    private final static Logger log = Logger.getLogger(Query.class.getName());
    private IELO elo;
    private IMetadataKey titleKey;
    private IMetadataKey typeKey;
    private IMetadataKey dateCreatedKey;
    private IMetadataKey authorKey;
    private IMetadataKey identifierKey;
    private IMetadataKey descriptionKey;

    /** Creates a new instance of SaveELO */
    public Query() {
    }

    /**
     * GET method for performing AND-queries on metadata via HTTP Query String
     * @return a JSONArray of all results as JSONObjects. Every Object includes the following JSON params
     * <ul>
     * <li> uri
     * <li> title
     * <li> type
     * <li> author
     * <li> description
     * <li> relevance
     * </ul>
     */
    @GET
    @Produces("text/xml")
    public String andQueryAsXML(@Context UriInfo ui) {
        initMetadataKeys();
        StringBuffer xmlString = new StringBuffer();
        List<ISearchResult> results = doAndQuery(ui);
        log.info("found " + results.size() + " hits");
        xmlString.append("<results count=\""+ results.size()+"\">");
        for (ISearchResult result : results) {
            IMetadata resultMetadata = result.getMetadata();
            xmlString.append("<elo>");
            xmlString.append("<uri>" + resultMetadata.getMetadataValueContainer(identifierKey) + "</uri>");
            xmlString.append("<title>" + resultMetadata.getMetadataValueContainer(titleKey) + "</title>");
            xmlString.append("<type>" + resultMetadata.getMetadataValueContainer(typeKey) + "</type>");
            xmlString.append("<author>" + resultMetadata.getMetadataValueContainer(authorKey) + "</author>");
            xmlString.append("<description>" + resultMetadata.getMetadataValueContainer(descriptionKey) + "</description>");
            xmlString.append("<relevance>" + result.getRelevance() + "</relevance>");
            xmlString.append("</elo>");
        }
        xmlString.append("</results>");
        return xmlString.toString();
    }
    
    /**
     * GET method for performing AND-queries on metadata via HTTP Query String
     * @return a JSONArray of all results as JSONObjects. Every Object includes the following JSON params
     * <ul>
     * <li> uri
     * <li> title
     * <li> type
     * <li> author
     * <li> description
     * <li> relevance
     * </ul>
     */
    @GET
    @Produces("application/json")
    public JSONArray andQueryAsJson(@Context UriInfo ui) {
        JSONArray queriedURIs = new JSONArray();
        List<ISearchResult> results = doAndQuery(ui);
        log.info("found " + results.size() + " hits");
        try {
            for (ISearchResult result : results) {
                JSONObject resultAsJson = new JSONObject();
                IMetadata resultMetadata = result.getMetadata();
                resultAsJson.put("uri", resultMetadata.getMetadataValueContainer(identifierKey));
                resultAsJson.put("title", resultMetadata.getMetadataValueContainer(titleKey));
                resultAsJson.put("type", resultMetadata.getMetadataValueContainer(typeKey));
                resultAsJson.put("author", resultMetadata.getMetadataValueContainer(authorKey));
                resultAsJson.put("description", resultMetadata.getMetadataValueContainer(descriptionKey));
                resultAsJson.put("relevance", result.getRelevance());
                queriedURIs.put(resultAsJson);
            }
        } catch (JSONException ex) {
            log.error(ex.getMessage());
        } finally {
            return queriedURIs;
        }
    }


    private List<ISearchResult> doAndQuery(UriInfo ui) {
        MultivaluedMap<String, String> params = ui.getQueryParameters();
        log.info("params: " + params);
        String[] keys = new String[params.keySet().size()];
        params.keySet().toArray(keys);
        String value = params.getFirst(keys[0]);
        log.info("value: "+value);

        IQuery query = new BasicMetadataQuery(beans.getTypeManager().getMetadataKey(CoreRooloMetadataKeyIds.valueOf(keys[0])), BasicSearchOperations.EQUALS, value, null);
        List<IQuery> queries = new Vector<IQuery>();
        for (int i = 1; i < keys.length; i++) {
            IQuery newQuery = new BasicMetadataQuery(beans.getTypeManager().getMetadataKey(CoreRooloMetadataKeyIds.valueOf(keys[i])), BasicSearchOperations.EQUALS, params.getFirst(keys[i]), null);
            queries.add(newQuery);
        }
        IQuery[] queriesArray = new IQuery[queries.size()];
        queries.toArray(queriesArray);
        IQuery andQuery;
        if (queries.size() == 0) {
            andQuery = query;
        }
        andQuery = new AndQuery(query, queriesArray);

        return beans.getRepository().search(andQuery);

    }

    /**
     * POST method for querying RoOLO for ELOs. As a plain text parameter, a lucene query string is submitted
     * 
     * @param queryString The lucene query string
     * @return a JSONArray of all results as JSONObjects. Every Object includes the following JSON params
     * <ul>
     * <li> uri
     * <li> title
     * <li> type
     * <li> author
     * <li> description
     * <li> relevance
     * </ul>
     */
    @POST
    @Consumes("text/plain")
    @Produces("application/json")
    public JSONArray query(String queryString) {
        JSONArray queriedURIs = new JSONArray();
        try {

            //query roolo
            IQuery query = new LuceneQuery(queryString);
            final List<ISearchResult> results = beans.getRepository().search(query);
            for (ISearchResult result : results) {
                JSONObject resultAsJson = new JSONObject();
                IMetadata resultMetadata = result.getMetadata();
                resultAsJson.put("uri", resultMetadata.getMetadataValueContainer(identifierKey));
                resultAsJson.put("title", resultMetadata.getMetadataValueContainer(titleKey));
                resultAsJson.put("type", resultMetadata.getMetadataValueContainer(typeKey));
                resultAsJson.put("author", resultMetadata.getMetadataValueContainer(authorKey));
                resultAsJson.put("description", resultMetadata.getMetadataValueContainer(descriptionKey));
                resultAsJson.put("relevance", result.getRelevance());
                queriedURIs.put(resultAsJson);
            }
        } catch (JSONException ex) {
            log.error(ex.getMessage());
        } finally {
            return queriedURIs;
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
}

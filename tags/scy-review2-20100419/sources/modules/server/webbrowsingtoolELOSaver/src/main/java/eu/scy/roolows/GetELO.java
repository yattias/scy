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
import java.net.URI;
import java.net.URISyntaxException;
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
import roolo.elo.metadata.keys.Contribute;

/**
 * REST Web Service
 * 
 * @author __SVEN__
 */
@Singleton
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
                    final Object value = elo.getMetadata().getMetadataValueContainer(metadataKey).getValue();
                    if (value == null) {
                        metadata.put(metadataKey.getId(), JSONObject.NULL);
                    } else {
                        if (value instanceof Contribute) {
                            JSONObject contribute = new JSONObject();
                            contribute.put("vcard", ((Contribute) value).getVCard());
                            contribute.put("date", ((Contribute) value).getDate());
                            metadata.put(metadataKey.getId(), contribute);
                        } else {
                            metadata.put(metadataKey.getId(), value);
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

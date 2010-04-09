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

import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import roolo.api.search.IMetadataQuery;
import roolo.elo.api.IELO;
import roolo.elo.api.IMetadataKey;

/**
 * REST Web Service
 *
 * @author __SVEN__
 */
@Path("/getELOPreview")
@Deprecated
public class GetELOPreviewFromURI {

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
    public GetELOPreviewFromURI() {
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
    public JSONObject getHtmlELO(JSONObject jsonData) throws ParserConfigurationException, URISyntaxException, SAXException, IOException {

        log.info("Trying to load the Preview of selected ELO");

        String uri = null;
//        String password = null;
        try {
            uri = jsonData.getString("uri");
//            password = jsonData.getString("password");
        } catch (JSONException ex) {
            Logger.getLogger(SaveELOResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        elo = configLoader.getRepository().retrieveELO(new URI(uri));

        String contentString = elo.getContent().getXml();
        log.info("content: \n=====================\n"+contentString+"\n=====================");

        //Get an Instance of the DocumentBuilder for parsing the xml-String
        DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = documentBuilder.parse(new InputSource(new StringReader(contentString)));
        log.info("doc.getNodeName(): "+doc.getNodeName());
        log.info("doc.getDocumentElement():"+doc.getDocumentElement());
        log.info("doc.getDocumentElement().getElementsByTagName(\"annotations\").item(0).getTextContent(): "+ doc.getDocumentElement().getElementsByTagName("annotations").item(0).getTextContent());
        
        //The content of the html-ELO consists of 3 parts...
        String annotations = doc.getDocumentElement().getElementsByTagName("annotations").item(0).getTextContent();
        log.info(annotations);
        String preview = doc.getDocumentElement().getElementsByTagName("preview").item(0).getTextContent();
        log.info(preview);
        String html = doc.getDocumentElement().getElementsByTagName("html").item(0).getTextContent();
//        System.out.println(annotations);


        //TODO Fix the XML-Structure (seems to be not well-formed) and correct the parsing
//        String test = doc.getElementById(uri).getNodeValue();
//        log.warning(doc.getDocumentElement().getElementsByTagName("preview").item(0).getNodeValue());
//        log.warning(String.valueOf(((Element)doc.getRootElement().getChildren().get(0)).getChildren().size()));
//        String preview = doc.getDocumentElement().getElementsByTagName("preview").item(0).getNodeValue();
//        log.warning("Preview: \n **********************"+preview+"\n **********************");

        JSONObject output = new JSONObject();
        try {
            output.put("preview", preview);
        } catch (JSONException ex) {
            Logger.getLogger(GetELOPreviewFromURI.class.getName()).log(Level.SEVERE, null, ex);
        }

        return output;
    }
}

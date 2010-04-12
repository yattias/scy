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
import java.net.URISyntaxException;
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

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import roolo.elo.api.IELO;

/**
 * REST Web Service
 * 
 * @author __SVEN__
 */
@Path("/getELOAndroid")
@Deprecated
public class GetELOAndroid {

	private final static Logger logger = Logger.getLogger(GetELOAndroid.class.getName());

	@Context
	private UriInfo context;

	/** Creates a new instance of SaveELOResource */
	public GetELOAndroid() {
	}

	/**
	 * Retrieves representation of an instance of saveelo.SaveELOResource For
	 * testing purpose!
	 * 
	 * @return an instance of java.lang.String
	 */
	@GET
	@Produces("text/html")
	public String getXml() {
		return "<html><body>This RESTful Web Service provides access to RoOLO for Androids!</body></html>";
	}

	/**
	 * PUT method for updating or creating an instance of SaveELOResource For
	 * testing purpose!
	 * 
	 * @param content
	 *            representation for the resource
	 * @return an HTTP response with content of the updated or created resource.
	 */
	@PUT
	@Consumes("application/xml")
	public void putXml(String content) {
	}

	/**
	 * POST method for creating a new ELO Resource
	 * 
	 * @param xml
	 *            the xml representation of the ELO content
	 * @param username
	 *            the username of the ELO's creator
	 * @param password
	 *            the password of the ELO's creator
	 * @return an HTTP response with content of the updated or created resource.
	 */
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public JSONObject getHtmlELO(JSONObject jsonData) {
		JSONObject output = new JSONObject();

		String uri = null;
		try {
			uri = jsonData.getString("uri");
			IELO elo = Beans.getInstance().getRepository().retrieveELO(new URI(uri));
			String contentString = elo.getContent().getXmlString();
			output.put("content", contentString);
		} catch (JSONException ex) {
			logger.log(Level.SEVERE, "Could not store ELO xml string inside JSON object", ex);
			try {
				output.put("error", ex.toString());
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} catch (URISyntaxException ex) {
			logger.log(Level.SEVERE, "ELO URI does not match the URI syntax", ex);
		}

		return output;
	}
}

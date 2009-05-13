/*
 *
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://jersey.dev.java.net/CDDL+GPL.html
 * or jersey/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at jersey/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 *
 * Contributor(s):
 *
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package eu.scy.ws.example;

import com.sun.grizzly.http.SelectorThread;
import com.sun.grizzly.tcp.StaticResourcesAdapter;
import com.sun.grizzly.tcp.Adapter;
import com.sun.jersey.api.container.grizzly.GrizzlyWebContainerFactory;

import javax.ws.rs.core.UriBuilder;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class SCYWebservice {

    private static int getPort(int defaultPort) {
        String port = System.getenv("JERSEY_HTTP_PORT");
        if (null != port) {
            try {
                return Integer.parseInt(port);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return defaultPort;
    }

    private static URI getBaseURI() {
        return UriBuilder.fromUri("http://localhost/").port(getPort(9998)).build();
    }

    public static final URI BASE_URI = getBaseURI();

    protected static SelectorThread startServer() throws IOException {
        final Map<String, String> initParams = new HashMap<String, String>();

        initParams.put("com.sun.jersey.config.property.packages",
                "eu.scy.ws.example");

        System.out.println("Starting grizzly...");

        return GrizzlyWebContainerFactory.create(BASE_URI, initParams);
    }


    public static void main(String[] args) throws IOException {
        SelectorThread threadSelector = startServer();
        System.out.println(String.format("Jersey app started with WADL available at %sapplication.wadl\nHit enter to stop it...", BASE_URI));

        //noinspection ResultOfMethodCallIgnored
        System.in.read();
        threadSelector.stopEndpoint();
    }
}
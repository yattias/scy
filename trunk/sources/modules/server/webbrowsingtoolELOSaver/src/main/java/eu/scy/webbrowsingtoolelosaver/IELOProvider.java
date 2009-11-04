/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.webbrowsingtoolelosaver;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Vector;
import java.util.logging.Logger;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import roolo.elo.api.IELO;

/**
 *
 * @author Sven
 */
@Provider
@Produces("application/vnd.data")
public class IELOProvider implements MessageBodyWriter<Vector<IELO>>{
    //An implementation of a message body writer to transfer objects to a webservice
    //maybe not relevant anymore

    public boolean isWriteable(Class type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return true;
    }

    public long getSize(Vector<IELO> t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
           return 666;
    }

    public void writeTo(Vector<IELO> t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException, WebApplicationException {
        Logger.getAnonymousLogger().warning("writeTo() called.");
    }

}

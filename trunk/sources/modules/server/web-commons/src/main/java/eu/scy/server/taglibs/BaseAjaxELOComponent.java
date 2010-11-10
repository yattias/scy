package eu.scy.server.taglibs;

import eu.scy.common.scyelo.RooloServices;
import eu.scy.common.scyelo.ScyElo;

import javax.servlet.jsp.tagext.TagSupport;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URLDecoder;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 03.nov.2010
 * Time: 12:44:56
 * To change this template use File | Settings | File Templates.
 */
public class BaseAjaxELOComponent extends TagSupport {

    private static Logger log = Logger.getLogger("BaseAjaxELOComponent.class");

    private String eloURI;
    private String property;
    private RooloServices rooloServices;

    public String getEloURI() {
        return eloURI;
    }

    public void setEloURI(String eloURI) {
        this.eloURI = eloURI;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public RooloServices getRooloServices() {
        return rooloServices;
    }

    public void setRooloServices(RooloServices rooloServices) {
        this.rooloServices = rooloServices;
    }

    protected String executeGetter(Object object, String property) {
        try {
            String uriString = URLDecoder.decode((String)object, "UTF-8");
            log.info("Executing getter: uri:" + uriString + " property: " + property);
            URI uri = new URI(uriString);
            ScyElo scyElo = ScyElo.loadElo(uri, getRooloServices());
            String firstLetter = property.substring(0, 1);
            firstLetter = firstLetter.toUpperCase();

            property = firstLetter + property.substring(1, property.length());

            Method method = getRooloServices().getClass().getMethod("get" + property, ScyElo.class);
            Object returnValue = method.invoke(getRooloServices(), scyElo);
            if(returnValue instanceof String) return (String) returnValue;
            else if(returnValue instanceof Integer) return String.valueOf(returnValue); 

        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        throw new RuntimeException("DID NOT WORK!");
    }
}

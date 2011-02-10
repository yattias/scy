package eu.scy.server.controllers;

import eu.scy.common.scyelo.ContentTypedScyElo;
import eu.scy.common.scyelo.RooloServices;
import eu.scy.common.scyelo.ScyElo;
import eu.scy.server.controllers.xml.ActionLoggingService;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
import roolo.elo.api.IELO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URLDecoder;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 03.nov.2010
 * Time: 13:08:18
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractAjaxELOController extends AbstractController {

    public final static String ID = "id";
    public final static String PROPERTY = "property";
    public final static String URI = "uri";

    private RooloServices rooloServices;

    private ActionLoggingService actionLoggingService;


    protected void executeSetter(String uriValue, String property, String value) {
        try {
            uriValue = URLDecoder.decode(uriValue, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        try {
            IELO elo = rooloServices.getRepository().retrieveELOLastVersion(new java.net.URI(uriValue));
            String firstLetter = property.substring(0, 1);
            firstLetter = firstLetter.toUpperCase();

            property = firstLetter + property.substring(1, property.length());

            Method method = rooloServices.getClass().getMethod("set" + property, ScyElo.class, Object.class);
            logger.info("METHOD IS: " + method + " method name: " + method.getName());

            if (elo != null) {
                ScyElo scyElo = ScyElo.loadElo(elo.getUri(), rooloServices);

                method.invoke(rooloServices, scyElo, value);
            } else {
                logger.info("ELO IS NULL FUKA!");
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public RooloServices getRooloServices() {
        return rooloServices;
    }

    public void setRooloServices(RooloServices rooloServices) {
        this.rooloServices = rooloServices;
    }

    public ActionLoggingService getActionLoggingService() {
        return actionLoggingService;
    }

    public void setActionLoggingService(ActionLoggingService actionLoggingService) {
        this.actionLoggingService = actionLoggingService;
    }

    public String getCurrentUserName(HttpServletRequest request) {
       org.springframework.security.userdetails.User user = (org.springframework.security.userdetails.User) request.getSession().getAttribute("CURRENT_USER");
       return user.getUsername();
   }
    
}

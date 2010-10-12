package eu.scy.server.spring;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 08.feb.2010
 * Time: 12:49:33
 * To change this template use File | Settings | File Templates.
 */
public class AppUrlHandler {
    private static Logger log = Logger.getLogger("AppUrlHandler.class");
    private SimpleUrlHandlerMapping urlHandlerMapping;

    public AppUrlHandler() {
        addUrlsToUrlHandler();
    }

    private void addUrlsToUrlHandler() {
        SimpleUrlHandlerMapping urlHandlerMapping = getUrlHandlerMapping();
        Map map = urlHandlerMapping.getHandlerMap();
        Set keys = map.keySet();
        Iterator it = keys.iterator();
        while (it.hasNext()) {
            Object o = it.next();
            log.info("*****************************************KEY: " + o);
        }


    }

    public SimpleUrlHandlerMapping getUrlHandlerMapping() {
        return urlHandlerMapping;
    }

    public void setUrlHandlerMapping(SimpleUrlHandlerMapping urlHandlerMapping) {
        this.urlHandlerMapping = urlHandlerMapping;
    }
}

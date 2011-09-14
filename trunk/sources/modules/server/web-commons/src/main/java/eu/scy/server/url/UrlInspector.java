package eu.scy.server.url;

import eu.scy.common.mission.MissionRuntimeElo;
import eu.scy.common.mission.MissionSpecificationElo;
import eu.scy.common.scyelo.ScyElo;
import eu.scy.core.AjaxPersistenceService;
import eu.scy.core.roolo.MissionELOService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 09.apr.2010
 * Time: 09:45:20
 * To change this template use File | Settings | File Templates.
 */
public class UrlInspector {

    private AjaxPersistenceService service;
    private MissionELOService missionELOService;

    private static Logger log = Logger.getLogger("UrlInspector.class");

    public Object instpectRequest(HttpServletRequest request, HttpServletResponse httpServletResponse) {
        log.info(request.getQueryString());
        Enumeration parameterNames = request.getParameterNames();
        System.out.println("REQUEST START");
        System.out.println(request.getRequestURL().toString());
        System.out.println(request.getQueryString());
        Map parameterMap = request.getParameterMap();
        while (parameterNames.hasMoreElements()) {
            Object o = parameterNames.nextElement();
            Object[] v = (Object[]) parameterMap.get(o);
            String value = Arrays.deepToString(v);
            System.out.println("--[" + o + "]-- [" + value + "]");
        }
        System.out.println("DONE PARAMETERS");
        
        String modelString = request.getParameter("model");
        log.info("MODEL STRING: " + modelString);

        if (modelString != null) {
            String type = modelString.substring(0, modelString.indexOf("_"));
            String id = modelString.substring(modelString.indexOf("_") + 1, modelString.length());
            try {
                Class clazz = Class.forName(type);
                Object model = getService().get(clazz, id);

                request.setAttribute("modelObject", model);

                return model;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        String eloURI = request.getParameter("eloURI");
        //if(eloURI == null) eloURI = request.getParameter("missionURI");
        log.info("URI: " + eloURI);
        if (eloURI != null && eloURI.length() > 0) {
            try {
                String uri = URLDecoder.decode(eloURI, "UTF-8");
                URI realURI = new URI(uri);
                request.setAttribute("eloURI", uri);
                ScyElo scyElo =  ScyElo.loadElo(realURI, getMissionELOService());
                if(scyElo.getTechnicalFormat().equals("scy/missionspecification")) {
                    log.info("Loaded a missionSpecification");
                    scyElo =  MissionSpecificationElo.loadElo(realURI, getMissionELOService());
                }
                else if(scyElo.getTechnicalFormat().equals("scy/missionruntime")) {
                    log.info("Loaded a mission runtime elo");
                    scyElo = MissionRuntimeElo.loadElo(realURI, getMissionELOService());
                }
                ScyElo.loadMetadata(scyElo.getUri(), getMissionELOService());
                return scyElo;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public AjaxPersistenceService getService() {
        return service;
    }

    public void setService(AjaxPersistenceService service) {
        this.service = service;
    }

    public MissionELOService getMissionELOService() {
        return missionELOService;
    }

    public void setMissionELOService(MissionELOService missionELOService) {
        this.missionELOService = missionELOService;
    }
}

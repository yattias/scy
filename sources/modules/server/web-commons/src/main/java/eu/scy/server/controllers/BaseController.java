package eu.scy.server.controllers;

import eu.scy.common.mission.MissionSpecificationElo;
import eu.scy.common.scyelo.ScyElo;
import eu.scy.core.ServerService;
import eu.scy.core.UserService;
import eu.scy.core.model.ScyBase;
import eu.scy.core.model.Server;
import eu.scy.core.model.User;
import eu.scy.core.model.transfer.PedagogicalPlanTransfer;
import eu.scy.core.model.transfer.TransferElo;
import eu.scy.core.roolo.MissionELOService;
import eu.scy.server.controllers.ui.OddEven;
import eu.scy.server.url.UrlInspector;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 16.feb.2010
 * Time: 09:47:07
 * To change this template use File | Settings | File Templates.
 */
public abstract class BaseController extends AbstractController {

    public static final String OBLIGATORY_ANCHOR_ELOS_SESSION_PARAMETER = "OBLIGATORY_ANCHOR_ELOS_SESSION_PARAMETER";

    public static final String ELO_URI = "eloURI";

    private ServerService serverService;
    private ScyBase model;
    private UrlInspector urlInspector;
    private ScyElo scyElo;
    private UserService userService;
    private MissionELOService missionELOService;

    public ScyElo getScyElo() {
        return scyElo;
    }

    public void setScyElo(ScyElo scyElo) {
        this.scyElo = scyElo;
    }

    public ScyBase getModel() {
        return model;
    }

    public void setModel(ScyBase model) {
        logger.info("SETTING MODEL : " + model);
        this.model = model;
    }

    public Server getServer() {
        if (getServerService() != null) {
            return getServerService().getServer();
        }
        return null;

    }

    private void populateView(HttpServletRequest request, HttpServletResponse httpServletResponse, ModelAndView modelAndView) {
        modelAndView.addObject("model", getModel());
        modelAndView.addObject("oddEven", new OddEven());
        modelAndView.addObject("baseUrl", request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath());
        Locale locale = request.getLocale();
        modelAndView.addObject("language", locale.getLanguage());
        if (getServer() != null) modelAndView.addObject("css", getServer().getServerCSS());
    }

    @Override
    protected final ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse httpServletResponse) throws Exception {
        logger.info("NEW REQUEST ARRIVED:");

        RequestContextUtils.getLocaleResolver(request).setLocale(request, httpServletResponse, getCurrentLocale(request));

        Enumeration parameEnumeration = request.getParameterNames();
        while (parameEnumeration.hasMoreElements()) {
            String param = (String) parameEnumeration.nextElement();
            logger.info("** ** ** RECEIVED PARAM: " + param + " " + request.getParameter(param));

        }

        ModelAndView modelAndView = new ModelAndView();

        instpectRequest(request, httpServletResponse);

        handleRequest(request, httpServletResponse, modelAndView);
        populateView(request, httpServletResponse, modelAndView);
        return modelAndView;
    }

    protected void instpectRequest(HttpServletRequest request, HttpServletResponse httpServletResponse) {
        logger.info("----------------------------------------------------- " + getClass().getName());
        if (getUrlInspector() != null) {
            Object model = getUrlInspector().instpectRequest(request, httpServletResponse);
            if (model instanceof ScyElo) {
                logger.info("Setting ELO: " + ((ScyElo) model).getTechnicalFormat());
                setScyElo((ScyElo) model);
            } else {
                setModel((ScyBase) model);
            }

        }
        logger.info("*******************************************************" + getClass().getName());
    }

    protected abstract void handleRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView);

    public ServerService getServerService() {
        return serverService;
    }

    public void setServerService(ServerService serverService) {
        this.serverService = serverService;
    }

    public UrlInspector getUrlInspector() {
        return urlInspector;
    }

    public void setUrlInspector(UrlInspector urlInspector) {
        this.urlInspector = urlInspector;
    }

    public String getCurrentUserName(HttpServletRequest request) {
        org.springframework.security.userdetails.User user = (org.springframework.security.userdetails.User) request.getSession().getAttribute("CURRENT_USER");
        if(user != null) {
            return user.getUsername();
        }
        return null;

    }

    public User getCurrentUser(HttpServletRequest request) {
        if (getUserService() != null) {
            return getUserService().getUser(getCurrentUserName(request));
        }
        return null;
    }


    public Locale getCurrentLocale(HttpServletRequest request) {
        User user = getCurrentUser(request);
        String localeString = "en";
        if (user != null) {
            if (user.getUserDetails().getLocale() != null) localeString = user.getUserDetails().getLocale();
        }
        return new Locale(localeString);
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    protected URI getURI(String uri) {
        if (uri != null) {
            try {
                uri = URLDecoder.decode(uri, "UTF-8");
                URI _uri = new URI(uri);
                return _uri;
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }

        }
        return null;

    }

    protected String getEncodedUri(String parameter) {
        try {
            return URLEncoder.encode(parameter, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    public MissionELOService getMissionELOService() {
        return missionELOService;
    }

    public void setMissionELOService(MissionELOService missionELOService) {
        this.missionELOService = missionELOService;
    }

    public List<TransferElo> getObligatoryAnchorElos(HttpServletRequest request, MissionSpecificationElo missionSpecificationElo, PedagogicalPlanTransfer pedagogicalPlanTransfer) {
        HttpSession session = request.getSession();
        if(session.getAttribute(OBLIGATORY_ANCHOR_ELOS_SESSION_PARAMETER) == null) {
            if(getMissionELOService() != null) {
                logger.info("THE OBLIGATORY ELOS ARE NOT IN SESSION, ADDING THEM NOW!");
                session.setAttribute(OBLIGATORY_ANCHOR_ELOS_SESSION_PARAMETER, getMissionELOService().getObligatoryAnchorELOs(missionSpecificationElo, pedagogicalPlanTransfer));
            }
        } else {
            logger.info("OBLIGATORY ELOS IN SESSION - WEEE NEEED FOR SPEED!");
        }

        return (List<TransferElo>) session.getAttribute(OBLIGATORY_ANCHOR_ELOS_SESSION_PARAMETER);

    }
}

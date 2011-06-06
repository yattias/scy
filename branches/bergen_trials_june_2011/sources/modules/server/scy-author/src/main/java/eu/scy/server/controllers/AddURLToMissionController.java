package eu.scy.server.controllers;

import eu.scy.core.LearningMaterialService;
import eu.scy.core.MissionService;
import eu.scy.core.model.impl.pedagogicalplan.WebLinkImpl;
import eu.scy.core.model.pedagogicalplan.Mission;
import eu.scy.core.model.pedagogicalplan.WebLink;
import org.springframework.web.servlet.ModelAndView;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;


/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 03.mai.2010
 * Time: 05:47:21
 * To change this template use File | Settings | File Templates.
 */
public class AddURLToMissionController extends BaseController {

    private MissionService missionService;
    private LearningMaterialService learningMaterialService;
    private final String TITLE = "TITLE";
    private final String FAVICON = "FAVICON";

    @Override
    protected void handleRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {
        if (request.getParameter("model") != null) {
            Mission mission = getMissionService().getMission(request.getParameter("model"));
            setModel(mission);
            modelAndView.addObject("model", mission);
        }
        if (request.getParameter("action") != null) {
            logger.info("ADDING NEW URL " + request.getParameter("url"));
            String url = request.getParameter("url");
            if (url != null && url.length() > 0) {
                String title = validateURL(url, TITLE);

                logger.info("TITLE: " + title);
                WebLink webLink = new WebLinkImpl();
                webLink.setName(title);
                webLink.setUrl(url);
                webLink.setIcon((getFavico(url)));
                if(webLink.getName() == null || webLink.getName().length() == 0) webLink.setName(url);

                getLearningMaterialService().save(webLink);
                if(getModel() != null) {
                    ((Mission)getModel()).addLearningMaterial(webLink);
                    getMissionService().save(getModel());
                }
                
            }
        }
    }

    private String getFavico(String url) {
        if (!url.startsWith("http://")) url = "http://" + url;
        String favIcon = validateURL(url, FAVICON);
        try {
            URL u = new URL(url);
            String host = u.getHost();
            String favico = "http://" + host + "/" + favIcon;
            return favico;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return "";
    }

    private String validateURL(String inputURL, String element) {
        try {
            //weak fix of url
            if (!inputURL.startsWith("http://")) inputURL = "http://" + inputURL;
            logger.info("URL : " + inputURL);
            URL url = new URL(inputURL);
            URLConnection connection = url.openConnection();
            InputStream is = connection.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);

            XMLReader parser = XMLReaderFactory.createXMLReader("org.ccil.cowan.tagsoup.Parser");
            HTMLParser htmlDoc = new HTMLParser();
            parser.setContentHandler(htmlDoc);
            parser.parse(new InputSource(is));
            if (element.equals(TITLE)) return htmlDoc.getDocumentTitle();
            else if(element.equals(FAVICON)) return htmlDoc.getFaviconurl();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public MissionService getMissionService() {
        return missionService;
    }

    public void setMissionService(MissionService missionService) {
        this.missionService = missionService;
    }

    public LearningMaterialService getLearningMaterialService() {
        return learningMaterialService;
    }

    public void setLearningMaterialService(LearningMaterialService learningMaterialService) {
        this.learningMaterialService = learningMaterialService;
    }
}

package eu.scy.server.eportfolio.xml;

import com.thoughtworks.xstream.XStream;
import eu.scy.common.mission.MissionRuntimeElo;
import eu.scy.common.scyelo.ScyElo;
import eu.scy.core.roolo.MissionELOService;
import eu.scy.server.controllers.xml.XMLStreamerController;
import eu.scy.server.controllers.xml.transfer.Portfolio;
import eu.scy.server.controllers.xml.transfer.TransferElo;
import eu.scy.server.eportfolio.xml.utilclasses.ELOSearchResult;
import eu.scy.server.eportfolio.xml.utilclasses.LearningGoals;
import eu.scy.server.eportfolio.xml.utilclasses.ServiceExceptionMessage;
import eu.scy.server.url.UrlInspector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.net.URLDecoder;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 17.jan.2011
 * Time: 05:34:59
 * To change this template use File | Settings | File Templates.
 */
public abstract class MissionRuntimeEnabledXMLService extends XMLStreamerController {

    private MissionELOService missionELOService;
    private UrlInspector urlInspector;


    @Override
    protected final Object getObjectToStream(HttpServletRequest request, HttpServletResponse httpServletResponse) {
        try {
            String missionURI = request.getParameter("missionURI");
            logger.info("MIssionURI: " + missionURI);
            if (missionURI != null) {
                missionURI = URLDecoder.decode(missionURI, "UTF-8");

                ScyElo scyElo = (ScyElo) getUrlInspector().instpectRequest(request, httpServletResponse);
                MissionRuntimeElo missionRuntimeElo = MissionRuntimeElo.loadElo(new URI(missionURI), getMissionELOService());
                return getObject(missionRuntimeElo, request, httpServletResponse);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ServiceExceptionMessage("This service requires that you set missionURI parameter in order to load");
    }

    @Override
    protected void addAliases(XStream xStream) {
        super.addAliases(xStream);    //To change body of overridden methods use File | Settings | File Templates.
        xStream.alias("elo", TransferElo.class);
        xStream.alias("searchresult", ELOSearchResult.class);
        xStream.aliasField("createddate", TransferElo.class, "createdDate");
        xStream.aliasField("lastmodified", TransferElo.class, "lastModified");
        xStream.aliasField("uri".toLowerCase(), TransferElo.class, "uri");
        xStream.aliasField("catname".toLowerCase(), TransferElo.class, "catname");
        xStream.aliasField("thumbnail".toLowerCase(), TransferElo.class, "thumbnail");
        xStream.aliasField("fullsize".toLowerCase(), TransferElo.class, "fullsize");
        xStream.aliasField("myname".toLowerCase(), TransferElo.class, "myname");
        xStream.aliasField("modified".toLowerCase(), TransferElo.class, "modified");
        xStream.aliasField("studentDescription".toLowerCase(), TransferElo.class, "studentDescription");
        xStream.aliasField("studentReflection".toLowerCase(), TransferElo.class, "studentReflection");
        xStream.aliasField("studentInquiry".toLowerCase(), TransferElo.class, "studentInquiry");
        xStream.aliasField("assessed".toLowerCase(), TransferElo.class, "assessed");
        xStream.aliasField("grade".toLowerCase(), TransferElo.class, "grade");
        xStream.aliasField("assessmentComment".toLowerCase(), TransferElo.class, "assessmentComment");
        xStream.aliasField("reflectionComment".toLowerCase(), TransferElo.class, "reflectionComment");
        xStream.aliasField("createdDate".toLowerCase(), TransferElo.class, "createdDate");
        xStream.aliasField("lastModified".toLowerCase(), TransferElo.class, "lastModified");
        xStream.aliasField("createdBy".toLowerCase(), TransferElo.class, "createdBy");


        xStream.aliasField("portfoliostatus", Portfolio.class, "portfolioStatus");
        xStream.aliasField("missionname", Portfolio.class, "missionName");
        xStream.aliasField("reflectioncollaboration", Portfolio.class, "reflectionCollaboration");
        xStream.aliasField("reflectioninquiry", Portfolio.class, "reflectionInquiry");
        xStream.aliasField("reflectioneffort", Portfolio.class, "reflectionEffort");
        xStream.aliasField("reflectionmission", Portfolio.class, "reflectionMission");
        xStream.aliasField("assessmentportfoliocomment", Portfolio.class, "assessmentPortfolioComment");
        xStream.aliasField("assessmentportfoliorating", Portfolio.class, "assessmentPortfolioRating");
        xStream.aliasField("mission", Portfolio.class, "missionName");

        xStream.aliasField("generallearninggoals ", LearningGoals.class, "generalLearningGoals ");
        xStream.aliasField("specificlearninggoals  ", LearningGoals.class, "specificLearningGoals ");

    }

    protected abstract Object getObject(MissionRuntimeElo missionRuntimeElo, HttpServletRequest request, HttpServletResponse response);

    public MissionELOService getMissionELOService() {
        return missionELOService;
    }

    public void setMissionELOService(MissionELOService missionELOService) {
        this.missionELOService = missionELOService;
    }

    public UrlInspector getUrlInspector() {
        return urlInspector;
    }

    public void setUrlInspector(UrlInspector urlInspector) {
        this.urlInspector = urlInspector;
    }
}

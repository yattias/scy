package eu.scy.server.controllers.xml;

import com.thoughtworks.xstream.XStream;
import eu.scy.server.controllers.xml.ToolURLContainer;
import eu.scy.server.controllers.xml.XMLStreamerController;
import eu.scy.core.roolo.RooloAccessor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 06.okt.2010
 * Time: 05:29:46
 * To change this template use File | Settings | File Templates.
 */
public class ToolURLProvider extends XMLStreamerController {

    private RooloAccessor rooloAccessor;

    @Override
    protected Object getObjectToStream(HttpServletRequest request, HttpServletResponse httpServletResponse) {

        return new ToolURLContainer(getCurrentUserName(request));
    }

    @Override
    protected void addAliases(XStream xStream) {
        super.addAliases(xStream);  
        xStream.aliasField("userInfoURL".toLowerCase(), ToolURLContainer.class, "userInfoURL");
        xStream.aliasField("curentMissionProgressOverview".toLowerCase(), ToolURLContainer.class, "curentMissionProgressOverview");
        xStream.aliasField("eportfolioELOSearch".toLowerCase(), ToolURLContainer.class, "eportfolioELOSearch");
        xStream.aliasField("obligatoryELOsInMission".toLowerCase(), ToolURLContainer.class, "obligatoryELOsInMission");
        xStream.aliasField("runtimeElosList".toLowerCase(), ToolURLContainer.class, "runtimeElosList");
        xStream.aliasField("currentMissionRuntimeInfo".toLowerCase(), ToolURLContainer.class, "currentMissionRuntimeInfo");
        xStream.aliasField("assessmentService".toLowerCase(), ToolURLContainer.class, "assessmentService");
        xStream.aliasField("portfolioLoader".toLowerCase(), ToolURLContainer.class, "portfolioLoader");
        xStream.aliasField("learningGoalsLoader".toLowerCase(), ToolURLContainer.class, "learningGoalsLoader");
        xStream.aliasField("savePortfolio".toLowerCase(), ToolURLContainer.class, "savePortfolio");
        xStream.aliasField("eloSearchService".toLowerCase(), ToolURLContainer.class, "eloSearchService");
        xStream.aliasField("addEloToPortfolio".toLowerCase(), ToolURLContainer.class, "addEloToPortfolio");
        xStream.aliasField("userName".toLowerCase(), ToolURLContainer.class, "userName");
        xStream.aliasField("metaData".toLowerCase(), ToolURLContainer.class, "metaData");
    }

    public RooloAccessor getRooloAccessor() {
        return rooloAccessor;
    }

    public void setRooloAccessor(RooloAccessor rooloAccessor) {
        this.rooloAccessor = rooloAccessor;
    }
}

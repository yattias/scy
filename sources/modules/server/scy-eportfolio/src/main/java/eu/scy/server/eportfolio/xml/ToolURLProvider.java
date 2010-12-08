package eu.scy.server.eportfolio.xml;

import eu.scy.common.scyelo.LearningActivity;
import eu.scy.common.scyelo.ScyElo;
import eu.scy.server.controllers.xml.XMLStreamerController;
import eu.scy.server.eportfolio.xml.utilclasses.ToolURLContainer;
import eu.scy.server.roolo.RooloAccessor;
import org.roolo.search.BasicMetadataQuery;
import org.roolo.search.BasicSearchOperations;
import org.roolo.search.SearchResult;
import roolo.api.IRepository;
import roolo.api.search.IQuery;
import roolo.elo.api.IELO;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.IMetadataTypeManager;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

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

    public RooloAccessor getRooloAccessor() {
        return rooloAccessor;
    }

    public void setRooloAccessor(RooloAccessor rooloAccessor) {
        this.rooloAccessor = rooloAccessor;
    }
}

package eu.scy.server.eportfolio.xml;

import eu.scy.common.scyelo.LearningActivity;
import eu.scy.common.scyelo.ScyElo;
import eu.scy.server.controllers.xml.XMLStreamerController;
import eu.scy.server.eportfolio.xml.utilclasses.ToolURLContainer;
import eu.scy.server.roolo.RooloAccessor;
import org.roolo.rooloimpljpa.repository.search.BasicMetadataQuery;
import org.roolo.rooloimpljpa.repository.search.BasicSearchOperations;
import org.roolo.rooloimpljpa.repository.search.SearchResult;
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
        /*if(getRooloAccessor().getRepository() == null) logger.info("REPO IS NULL!");

        List eloSearchResult =  getRooloAccessor().getELOSWithTechnicalFormat("scy/interview");
        logger.info("FOUND :"  + eloSearchResult.size() + " ELOS!");
        for (int i = 0; i < eloSearchResult.size(); i++) {
            SearchResult searchResult = (SearchResult) eloSearchResult.get(i);
            IELO elo = searchResult.getELO();
            ScyElo scyELO = ScyElo.loadElo(elo.getUri(), rooloAccessor);
            logger.info("SCYELO: " + scyELO.getTitle() + "--> ");
            List <String> authors = scyELO.getAuthors();
            for (int j = 0; j < authors.size(); j++) {
                String author = authors.get(j);
                logger.info("   " + author);
                LearningActivity learningActivity = scyELO.getLearningActivity();
                logger.info(learningActivity);
                if(scyELO.getThumbnail() != null) logger.info("elo has thumbnail");
                else logger.info("NO thumbnail present!");

            }
        } */
        return new ToolURLContainer();
    }

    public RooloAccessor getRooloAccessor() {
        return rooloAccessor;
    }

    public void setRooloAccessor(RooloAccessor rooloAccessor) {
        this.rooloAccessor = rooloAccessor;
    }
}

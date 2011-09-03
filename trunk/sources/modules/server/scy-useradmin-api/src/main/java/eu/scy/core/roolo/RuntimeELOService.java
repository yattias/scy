package eu.scy.core.roolo;

import eu.scy.common.mission.MissionRuntimeElo;
import eu.scy.core.BaseELOService;
import roolo.search.ISearchResult;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 14.des.2010
 * Time: 06:34:34
 * To change this template use File | Settings | File Templates.
 */
public interface RuntimeELOService extends BaseELOService {

        List <ISearchResult> getRuntimeElosForUser(String userName);

        public void deleteRuntimeElosForUser(String userName);
}

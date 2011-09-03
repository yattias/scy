package eu.scy.core.roolo;

import eu.scy.common.mission.MissionEloType;
import eu.scy.common.mission.MissionRuntimeElo;
import eu.scy.common.scyelo.Access;
import eu.scy.common.scyelo.ScyElo;
import eu.scy.core.model.transfer.UserActivityInfo;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;
import roolo.search.*;

import java.util.*;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 14.des.2010
 * Time: 06:35:00
 * To change this template use File | Settings | File Templates.
 */
public class RuntimeELOServiceImpl extends BaseELOServiceImpl implements RuntimeELOService {

    private static Logger log = Logger.getLogger("RuntimeELOServiceImpl.class");

    @Override
    public List<ISearchResult> getRuntimeElosForUser(String userName) {
        final IMetadataKey technicalFormatKey = getMetaDataTypeManager().getMetadataKey(CoreRooloMetadataKeyIds.TECHNICAL_FORMAT);
        IQueryComponent missionRuntimeQueryComponent = new MetadataQueryComponent(technicalFormatKey, SearchOperation.EQUALS, MissionEloType.MISSION_RUNTIME.getType());
        IQuery missionRuntimeQuery = new Query(missionRuntimeQueryComponent);
        missionRuntimeQuery.setMaxResults(500);
        Set userNames = new HashSet();
        userNames.add(userName);
        missionRuntimeQuery.setIncludedUsers(userNames);
        return getRepository().search(missionRuntimeQuery);
    }

    @Override
    public void deleteRuntimeElosForUser(String userName) {
        log.info("deleting runtime elos for user: " + userName);
        List <ISearchResult> runtimeEloSearchResult = getRuntimeElosForUser(userName);
        for (int i = 0; i < runtimeEloSearchResult.size(); i++) {
            MissionRuntimeElo missionRuntimeElo = MissionRuntimeElo.loadLastVersionElo(runtimeEloSearchResult.get(i).getUri(), this);
            log.info("DELETING MISSION: " + missionRuntimeElo.getUri());
            missionRuntimeElo.setAccess(Access.DELETED);
            missionRuntimeElo.updateElo();

        }
    }

}

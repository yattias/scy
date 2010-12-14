package eu.scy.core.roolo;

import eu.scy.common.mission.MissionEloType;
import eu.scy.common.mission.MissionSpecificationElo;
import eu.scy.core.BaseELOService;
import org.roolo.search.BasicMetadataQuery;
import org.roolo.search.BasicSearchOperations;
import roolo.api.search.IQuery;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 14.des.2010
 * Time: 06:26:23
 * To change this template use File | Settings | File Templates.
 */
public class BaseELOServiceImpl extends RooloAccessorImpl implements BaseELOService {

    private IMetadataKey authorKey;

    @Override
    public List getRuntimeElos(MissionSpecificationElo missionSpecificationElo) {
        final IMetadataKey technicalFormatKey = getMetaDataTypeManager().getMetadataKey(CoreRooloMetadataKeyIds.TECHNICAL_FORMAT);
        IQuery missionRuntimeQuery = new BasicMetadataQuery(technicalFormatKey, BasicSearchOperations.EQUALS, MissionEloType.MISSION_RUNTIME.getType());
        return getELOs(missionRuntimeQuery);
    }

    public IMetadataKey getAuthorKey() {
        return authorKey;
    }

    public void setAuthorKey(IMetadataKey authorKey) {
        this.authorKey = authorKey;
    }
    
}

package eu.scy.server.roolo;

import eu.scy.common.mission.Las;
import eu.scy.common.mission.MissionSpecificationElo;
import eu.scy.common.scyelo.ScyElo;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 26.okt.2010
 * Time: 06:16:22
 * To change this template use File | Settings | File Templates.
 */
public interface MissionELOService extends RooloAccessor{


    MissionSpecificationElo createMissionSpecification(String title, String description, String author);

    List getMissionSpecifications();

    List getMissionSpecifications(String author);

    void createMissionSpecification(MissionSpecificationElo missionSpecificationElo, String authorUserName);

    void setGlobalMissionScaffoldingLevel(ScyElo missionSpecificationElo, Object scaffoldingLevel);

    Integer getGlobalMissionScaffoldingLevel(ScyElo missionSpecificationElo);

    List <Las> getLasses(MissionSpecificationElo missionSpecificationElo);

    void setTitle(ScyElo scyElo, Object value);

    String getTitle(ScyElo scyElo);
}

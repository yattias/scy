package eu.scy.core.roolo;

import eu.scy.common.mission.Las;
import eu.scy.common.mission.MissionRuntimeElo;
import eu.scy.common.mission.MissionSpecificationElo;
import eu.scy.common.scyelo.ScyElo;
import eu.scy.core.BaseELOService;
import eu.scy.core.model.transfer.*;
import eu.scy.core.roolo.RooloAccessor;
import roolo.search.ISearchResult;

import java.net.URI;
import java.util.List;
import java.util.Locale;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 26.okt.2010
 * Time: 06:16:22
 * To change this template use File | Settings | File Templates.
 */
public interface MissionELOService extends BaseELOService {


    MissionSpecificationElo createMissionSpecification(String title, String description, String author);

    List <ISearchResult> getMissionSpecifications();

    List <ISearchResult> getMissionSpecifications(String author);

    void createMissionSpecification(MissionSpecificationElo missionSpecificationElo, String authorUserName);

    void setGlobalMissionScaffoldingLevel(ScyElo missionSpecificationElo, Object scaffoldingLevel);

    Integer getGlobalMissionScaffoldingLevel(ScyElo missionSpecificationElo);

    List<Las> getLasses(MissionSpecificationElo missionSpecificationElo);

    List getAnchorELOs(MissionSpecificationElo missionSpecificationElo);

    List<TransferElo> getObligatoryAnchorELOs(MissionSpecificationElo missionSpecificationElo, PedagogicalPlanTransfer pedagogicalPlan);

    void setTitle(ScyElo scyElo, Object value);

    String getTitle(ScyElo scyElo);

    List getAssignedUserNamesFor(MissionSpecificationElo missionSpecificationElo);


    MissionSpecificationElo getMissionSpecificationELO(URI missionSpecificationURI);

    MissionSpecificationElo getMissionSpecificationELOForRuntume(MissionRuntimeElo missionRuntimeElo);

    List findElosFor(String username);

    List<Portfolio> getPortfoliosThatAreReadyForAssessment(MissionSpecificationElo missionSpecificationElo);

    public Portfolio getPortfolio(MissionRuntimeElo missionRuntimeElo); 

    NewestElos getNewestElosForFeedback(MissionRuntimeElo missionRuntimeElo, String username);

    List <ISearchResult> getFeedback();

    NewestElos getMyElosWithFeedback(MissionRuntimeElo missionRuntimeElo, String currentUserName);

    NewestElos getFeedbackElosWhereIHaveContributed(MissionRuntimeElo missionRuntimeElo, String currentUserName);

    void clearAllPortfolios();

    MissionSpecificationElo getMissionSpecificationElo(String missionURI);

    TransferElo getTransferElo(ScyElo scyElo);

    public List<ISearchResult> getElosWithTechnicalType(String technicalFormat, String username);


}

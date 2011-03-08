package eu.scy.core.roolo;

import eu.scy.common.mission.*;
import eu.scy.common.scyelo.ScyElo;
import org.roolo.search.BasicMetadataQuery;
import org.roolo.search.BasicSearchOperations;
import roolo.api.search.AndQuery;
import roolo.api.search.IQuery;
import roolo.api.search.ISearchResult;
import roolo.elo.api.IMetadata;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;

import java.net.URI;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 26.okt.2010
 * Time: 06:16:46
 * To change this template use File | Settings | File Templates.
 */
public class MissionELOServiceImpl extends BaseELOServiceImpl implements MissionELOService {

    private static Logger log = Logger.getLogger("MissionELOServiceImpl.class");

    private static final String GLOBAL_MISSION_SCAFFOLDING_LEVEL = "globalMissionScaffoldingLevel";

    private static final RuntimeSettingKey globalMissionScaffoldingLevelKey = new RuntimeSettingKey(GLOBAL_MISSION_SCAFFOLDING_LEVEL, null, null);


    @Override
    public MissionSpecificationElo createMissionSpecification(String title, String description, String author) {
        MissionSpecificationElo missionSpecificationElo = MissionSpecificationElo.createElo(this);
        missionSpecificationElo.setDescription(description);
        missionSpecificationElo.setTitle(title);

        missionSpecificationElo.addAuthor(author);

        ScyElo missionRuntimeElo = getElo(missionSpecificationElo.getMissionRuntimeEloUri());

        IMetadata newMissionRuntimeEloMetadata = getRepository().addForkedELO(missionRuntimeElo.getElo());


        IMetadata metaData = getRepository().addForkedELO(missionSpecificationElo.getElo());


        missionSpecificationElo.saveAsNewElo();


        return missionSpecificationElo;
    }

    @Override
    public List getMissionSpecifications(String author) {
        return getMissionSpecificationsByAuthor(author);
    }

    @Override
    public void createMissionSpecification(MissionSpecificationElo missionSpecificationElo, String authorUserName) {

        missionSpecificationElo.saveAsForkedElo();
        missionSpecificationElo.setTitle("New plan");
        missionSpecificationElo.addAuthor(authorUserName);

        RuntimeSettingsElo runtimeSettingsElo = RuntimeSettingsElo.loadElo(missionSpecificationElo.getTypedContent().getRuntimeSettingsEloUri(), this);
        runtimeSettingsElo.setTitle("runtime for " + missionSpecificationElo.getTitle());
        runtimeSettingsElo.saveAsForkedElo(); //new copy of runtime settings has been created

        missionSpecificationElo.getTypedContent().setRuntimeSettingsEloUri(runtimeSettingsElo.getUri());
        missionSpecificationElo.updateElo();
        setGlobalMissionScaffoldingLevel(missionSpecificationElo, 2);

    }

    @Override
    public void setTitle(ScyElo scyElo, Object value) {
        scyElo.setTitle((String) value);
        scyElo.updateElo();
    }

    @Override
    public String getTitle(ScyElo scyElo) {
        return scyElo.getTitle();
    }

    @Override
    public List getAssignedUserNamesFor(MissionSpecificationElo missionSpecificationElo) {
        List<ScyElo> runtimeModels = getRuntimeElos(missionSpecificationElo);
        List userNames = new LinkedList();

        for (int i = 0; i < runtimeModels.size(); i++) {
            MissionRuntimeElo missionRuntimeElo = new MissionRuntimeElo(runtimeModels.get(i).getElo(), this);
            log.info(missionRuntimeElo.getContent().getXmlString());
            if (missionRuntimeElo != null) {
                if (missionRuntimeElo.getTitle().equals(missionSpecificationElo.getTitle())) {
                    String userName = missionRuntimeElo.getMissionRunning();
                    userNames.add(userName);
                } else {
                    log.info("TITLE " + missionRuntimeElo.getTitle() + " DOES NOT EQUAL: " + missionSpecificationElo.getTitle());
                    //HE HE : NOT MY PROUDEST MOMENT
                }
            }


        }

        return userNames;

    }


    @Override
    public MissionSpecificationElo getMissionSpecificationELO(URI missionSpecificationURI) {
        if (missionSpecificationURI != null) {
            MissionSpecificationElo missionSpecificationElo = MissionSpecificationElo.loadElo(missionSpecificationURI, this);
            ScyElo elo = MissionSpecificationElo.loadElo(missionSpecificationElo.getUri(), this);
            ScyElo.loadMetadata(elo.getUri(), this);
        } else {
            log.info("THE MISSION SPECIFICATION URI IS NULL!");
        }

        return null;
    }

    @Override
    public MissionSpecificationElo getMissionSpecificationELOForRuntume(MissionRuntimeElo missionRuntimeElo) {
        List missionSpecifications = getMissionSpecifications();
        for (int i = 0; i < missionSpecifications.size(); i++) {
            ScyElo missionSpeicification = (ScyElo) missionSpecifications.get(i);
            if(missionRuntimeElo.getTitle().equals(missionRuntimeElo.getTitle())) {
                return MissionSpecificationElo.loadElo(missionSpeicification.getUri(),  this);
            }

        }
        return null;
    }


    @Override
    public void setGlobalMissionScaffoldingLevel(ScyElo scyElo, Object value) {
        if (value instanceof String) value = Integer.valueOf((String) value);
        Integer scaffoldingLevel = (Integer) value;

        MissionSpecificationElo missionSpecificationElo = MissionSpecificationElo.loadLastVersionElo(scyElo.getUri(), this);
        RuntimeSettingsElo runtimeSettingsElo = getRuntimeSettingsElo(missionSpecificationElo);
        ((RuntimeSettingsHelper) runtimeSettingsElo.getPropertyAccessor()).setScaffoldingLevel(scaffoldingLevel);
    }

    @Override
    public Integer getGlobalMissionScaffoldingLevel(ScyElo scyElo) {
        MissionSpecificationElo missionSpecificationElo = MissionSpecificationElo.loadLastVersionElo(scyElo.getUri(), this);
        RuntimeSettingsElo elo = getRuntimeSettingsElo(missionSpecificationElo);
        return ((RuntimeSettingsHelper) elo.getPropertyAccessor()).getScaffoldingLevel();
    }

    @Override
    public List<Las> getLasses(MissionSpecificationElo missionSpecificationElo) {
        MissionModelElo missionModel = MissionModelElo.loadLastVersionElo(missionSpecificationElo.getTypedContent().getMissionMapModelEloUri(), this);
        return missionModel.getTypedContent().getLasses();//what is  this? A getter??
    }

    @Override
    public List getAnchorELOs(MissionSpecificationElo missionSpecificationElo) {
        MissionModelElo missionModel = MissionModelElo.loadLastVersionElo(missionSpecificationElo.getTypedContent().getMissionMapModelEloUri(), this);
        missionModel.getMissionModel().loadMetadata(this);
        List lasses = missionModel.getTypedContent().getLasses();//what is  this? A getter??

        List missionAnchors = new LinkedList();
        for (int i = 0; i < lasses.size(); i++) {
            Las las = (Las) lasses.get(i);
            MissionAnchor missionAnchor = las.getMissionAnchor();
            if (missionAnchor != null) {
                ScyElo missionScyElo = ScyElo.loadLastVersionElo(missionAnchor.getEloUri(), this);
                missionAnchors.add(missionScyElo);
                if (missionScyElo != null) {
                    log.info("MISSION ANCHOR: " +missionScyElo.getTitle());
                } else {
                    log.info("MISSION SCY ELO IS NULL:" + missionAnchor.getIconType());
                }

            }

        }

        return missionAnchors;
    }

    private RuntimeSettingsElo getRuntimeSettingsElo(MissionSpecificationElo missionSpecificationElo) {
        return RuntimeSettingsElo.loadLastVersionElo(missionSpecificationElo.getTypedContent().getRuntimeSettingsEloUri(), this);
    }

    @Override
    public List getMissionSpecifications() {
        final IMetadataKey technicalFormatKey = getMetaDataTypeManager().getMetadataKey(CoreRooloMetadataKeyIds.TECHNICAL_FORMAT);
        IQuery missionSpecificationQuery = new BasicMetadataQuery(technicalFormatKey, BasicSearchOperations.EQUALS, MissionEloType.MISSION_SPECIFICATIOM.getType());
        return getELOs(missionSpecificationQuery);
    }

    public List getMissionSpecificationsByAuthor(String author) {
        return getMissionSpecifications();
        /*final IMetadataKey authorKey = getMetaDataTypeManager().getMetadataKey(CoreRooloMetadataKeyIds.AUTHOR.getId());
        IQuery missionSpecificationQuery = new BasicMetadataQuery(authorKey, BasicSearchOperations.EQUALS, author);
        return getELOs(missionSpecificationQuery);
        */
    }

    @Override
    public List findElosFor(String mission, String username)  {

        BasicMetadataQuery bmq1 = new BasicMetadataQuery(getMetaDataTypeManager().getMetadataKey("mission"), BasicSearchOperations.EQUALS, "ecomission"); //e.g. mission = "ecoMission"
        BasicMetadataQuery bmq2 = new BasicMetadataQuery(getMetaDataTypeManager().getMetadataKey(CoreRooloMetadataKeyIds.AUTHOR), BasicSearchOperations.EQUALS, username);   //e.g. author = "jan"


        AndQuery q = new AndQuery(bmq1, bmq2);
        List<ISearchResult> results = getRepository().search(q);
        List elos = new LinkedList();
        for (int i = 0; i < results.size(); i++) {
            ISearchResult searchResult = results.get(i);
            elos.add(searchResult.getELO());
        }

        return elos;
    }

}
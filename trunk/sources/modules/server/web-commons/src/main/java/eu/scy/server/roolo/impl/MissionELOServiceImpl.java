package eu.scy.server.roolo.impl;

import eu.scy.common.mission.*;
import eu.scy.common.scyelo.ScyElo;
import eu.scy.server.roolo.MissionELOService;
import org.roolo.search.BasicMetadataQuery;
import org.roolo.search.BasicSearchOperations;
import roolo.api.search.IQuery;
import roolo.elo.api.IMetadata;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;

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
public class MissionELOServiceImpl extends RooloAccessorImpl implements MissionELOService {

    private static Logger log = Logger.getLogger("MissionELOServiceImpl.class");

    private static final String GLOBAL_MISSION_SCAFFOLDING_LEVEL = "globalMissionScaffoldingLevel";

    private static final RuntimeSettingKey globalMissionScaffoldingLevelKey = new RuntimeSettingKey(GLOBAL_MISSION_SCAFFOLDING_LEVEL, null, null);
    private IMetadataKey authorKey;

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
    public List getRuntimeElos(MissionSpecificationElo missionSpecificationElo) {
        final IMetadataKey technicalFormatKey = getMetaDataTypeManager().getMetadataKey(CoreRooloMetadataKeyIds.TECHNICAL_FORMAT);
        IQuery missionRuntimeQuery = new BasicMetadataQuery(technicalFormatKey, BasicSearchOperations.EQUALS, MissionEloType.MISSION_RUNTIME.getType());
        return getELOs(missionRuntimeQuery);
    }

    @Override
    public List getAssignedUserNamesFor(MissionSpecificationElo missionSpecificationElo) {
        List<ScyElo> runtimeModels = getRuntimeElos(missionSpecificationElo);
        List userNames = new LinkedList();

        for (int i = 0; i < runtimeModels.size(); i++) {
            MissionRuntimeElo missionRuntimeElo = new MissionRuntimeElo(runtimeModels.get(i).getElo(), this);
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
    public List getRuntimeElosForUser(String userName) {
        List runtimeElos = new LinkedList();
        List<ScyElo> runtimeModels = getRuntimeElos(null);
        for (int i = 0; i < runtimeModels.size(); i++) {
            MissionRuntimeElo missionRuntimeElo = new MissionRuntimeElo(runtimeModels.get(i).getElo(), this);
            if (missionRuntimeElo != null) {
                String missionRunningHAHAHA = missionRuntimeElo.getMissionRunning();
                if (missionRunningHAHAHA != null && missionRunningHAHAHA.equals(userName)) {
                    runtimeElos.add(missionRuntimeElo);
                }
            }
        }
        return runtimeElos;
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

    public IMetadataKey getAuthorKey() {
        return authorKey;
    }

    public void setAuthorKey(IMetadataKey authorKey) {
        this.authorKey = authorKey;
    }
}

package eu.scy.server.roolo.impl;

import eu.scy.common.mission.*;
import eu.scy.common.scyelo.ScyElo;
import eu.scy.server.roolo.ELOWebSafeTransporter;
import eu.scy.server.roolo.MissionELOService;
import org.roolo.rooloimpljpa.repository.search.BasicMetadataQuery;
import org.roolo.rooloimpljpa.repository.search.BasicSearchOperations;
import roolo.api.search.IQuery;
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
public class MissionELOServiceImpl extends RooloAccessorImpl implements MissionELOService {

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
        List<eu.scy.common.mission.MissionSpecificationElo> allMissions = getMissionSpecifications();
        List result = new LinkedList();
        for (int i = 0; i < allMissions.size(); i++) {
            eu.scy.common.mission.MissionSpecificationElo missionSpecificationElo = allMissions.get(i);
            if (missionSpecificationElo.getAuthors().contains(author)) result.add(missionSpecificationElo);
        }

        return result;
    }

    @Override
    public void createMissionSpecification(MissionSpecificationElo missionSpecificationElo) {

        missionSpecificationElo.saveAsForkedElo();
        missionSpecificationElo.setTitle("Why does the freakin' settings not work? FFFFFFFF##");

        RuntimeSettingsElo runtimeSettingsElo = RuntimeSettingsElo.loadElo(missionSpecificationElo.getTypedContent().getRuntimeSettingsEloUri(), this);
        runtimeSettingsElo.setTitle("runtime for " +missionSpecificationElo.getTitle());
        runtimeSettingsElo.saveAsForkedElo(); //new copy of runtime settings has been created

        missionSpecificationElo.getTypedContent().setRuntimeSettingsEloUri(runtimeSettingsElo.getUri());
        missionSpecificationElo.updateElo();
        setGlobalMissionScaffoldingLevel(missionSpecificationElo, 2);

    }

    @Override
    public void setGlobalMissionScaffoldingLevel(ScyElo scyElo, Object value) {
        log.info("SETTING MISSION SCAFFOLDING LEVEL: " + value);
        if(value instanceof String ) value = Integer.valueOf((String) value);
        Integer scaffoldingLevel = (Integer) value;
        MissionSpecificationElo missionSpecificationElo = MissionSpecificationElo.loadLastVersionElo(scyElo.getUri(), this);
        RuntimeSettingsElo runtimeSettingsElo = getRuntimeSettingsElo(missionSpecificationElo);
        runtimeSettingsElo.getTypedContent().addSetting(globalMissionScaffoldingLevelKey, String.valueOf(scaffoldingLevel));
        runtimeSettingsElo.updateElo();
    }

    @Override
    public Integer getGlobalMissionScaffoldingLevel(ScyElo scyElo) {
        MissionSpecificationElo missionSpecificationElo = MissionSpecificationElo.loadLastVersionElo(scyElo.getUri(), this);
        RuntimeSettingsElo elo = getRuntimeSettingsElo(missionSpecificationElo);
        List <RuntimeSetting> settings = elo.getTypedContent().getAllSettings();
        String stringLevel = null;
        for (int i = 0; i < settings.size(); i++) {
            RuntimeSetting runtimeSetting = settings.get(i);
            log.info("---> " + runtimeSetting.getKey().getName() + "::: " + runtimeSetting.getValue());
            if(runtimeSetting.getKey().getName().equals(GLOBAL_MISSION_SCAFFOLDING_LEVEL)) stringLevel = runtimeSetting.getValue();
        }

        //String stringLevel = elo.getTypedContent().getSetting(globalMissionScaffoldingLevelKey);
        //log.info("XML:"  + elo.getElo().getXml());

        if(stringLevel == null) return 0;

        return new Integer(stringLevel);
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


}

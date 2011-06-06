package eu.scy.common.mission.impl;

import eu.scy.common.mission.*;

import java.util.List;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 18.nov.2010
 * Time: 11:55:51
 * To change this template use File | Settings | File Templates.
 */
public class RuntimeSettingsHelperImpl extends BasicRuntimeSettingsEloContent implements RuntimeSettingsHelper {

    private static Logger log = Logger.getLogger("RuntimeSettingsHelperImpl.class");

    private static final String GLOBAL_MISSION_SCAFFOLDING_LEVEL = "globalMissionScaffoldingLevel";

    private static final RuntimeSettingKey globalMissionScaffoldingLevelKey = new RuntimeSettingKey(GLOBAL_MISSION_SCAFFOLDING_LEVEL, null, null);
    private RuntimeSettingsEloContent typedContent;
    private RuntimeSettingsElo runtimeSettingsElo;

    public RuntimeSettingsHelperImpl(RuntimeSettingsEloContent typedContent, RuntimeSettingsElo runtimeSettingsElo) {
        this.typedContent = typedContent;
        this.runtimeSettingsElo = runtimeSettingsElo;
    }

    @Override
    public void setScaffoldingLevel(Integer scaffoldingLevel) {
        typedContent.addSetting(globalMissionScaffoldingLevelKey, String.valueOf(scaffoldingLevel));

        runtimeSettingsElo.updateElo();
    }

    @Override
    public Integer getScaffoldingLevel() {
        String stringLevel = typedContent.getSetting(globalMissionScaffoldingLevelKey);
        if(stringLevel != null && stringLevel.length() > 0) return new Integer(stringLevel);
        return -1;
    }
}

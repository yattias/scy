package eu.scy.common.mission;

import java.util.List;

public interface RuntimeSettingsEloContent extends RuntimeSettingsManager
{
//   public String getSetting(RuntimeSettingKey key);
//
//   public void addSetting(RuntimeSettingKey key, String value);
   
   public List<RuntimeSetting> getAllSettings();
}

package eu.scy.common.mission;

import java.util.List;

public interface RuntimeSettingsManager
{
   public String getSetting(RuntimeSettingKey key);

   public List<String> getSettings(List<RuntimeSettingKey> keys);

   public void addSetting(RuntimeSettingKey key, String value);

   public void addSetting(RuntimeSetting runtimeSetting);

   public void addSettings(List<RuntimeSetting> runtimeSettings);
}

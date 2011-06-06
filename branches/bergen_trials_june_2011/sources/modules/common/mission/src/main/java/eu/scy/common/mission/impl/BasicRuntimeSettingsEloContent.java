package eu.scy.common.mission.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import eu.scy.common.mission.RuntimeSetting;
import eu.scy.common.mission.RuntimeSettingKey;
import eu.scy.common.mission.RuntimeSettingsEloContent;

public class BasicRuntimeSettingsEloContent implements RuntimeSettingsEloContent
{
   private Map<RuntimeSettingKey, String> settings = new ConcurrentHashMap<RuntimeSettingKey, String>();

   @Override
   public void addSetting(RuntimeSettingKey key, String value)
   {
      assert key != null;
      assert value != null;
      settings.put(key, value);
   }

   @Override
   public String getSetting(RuntimeSettingKey key)
   {
      assert key != null;
      return settings.get(key);
   }

   @Override
   public List<String> getSettings(List<RuntimeSettingKey> keys)
   {
      List<String> values = new ArrayList<String>(keys.size());
      for (RuntimeSettingKey key : keys)
      {
         values.add(getSetting(key));
      }
      return values;
   }

   @Override
   public List<RuntimeSetting> getAllSettings()
   {
      List<RuntimeSetting> settingsList = new ArrayList<RuntimeSetting>();
      for (Map.Entry<RuntimeSettingKey, String> entry : settings.entrySet())
      {
         settingsList.add(new RuntimeSetting(entry.getKey(), entry.getValue()));
      }
      return settingsList;
   }

   @Override
   public void addSetting(RuntimeSetting runtimeSetting)
   {
      addSetting(runtimeSetting.getKey(), runtimeSetting.getValue());
   }

   @Override
   public void addSettings(List<RuntimeSetting> runtimeSettings)
   {
      for (RuntimeSetting runtimeSetting : runtimeSettings)
      {
         addSetting(runtimeSetting);
      }
   }

}

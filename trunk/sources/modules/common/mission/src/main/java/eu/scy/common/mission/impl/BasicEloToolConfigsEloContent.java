/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.common.mission.impl;

import java.util.HashMap;
import java.util.List;

import eu.scy.common.mission.EloToolConfig;
import eu.scy.common.mission.EloToolConfigsEloContent;

/**
 * 
 * @author SikkenJ
 */
public class BasicEloToolConfigsEloContent implements EloToolConfigsEloContent
{

   private List<EloToolConfig> eloToolConfigs;
   private HashMap<String, EloToolConfig> eloToolConfigsMap = new HashMap<String, EloToolConfig>();
   private final ApplyEloToolConfigDefaults applyEloToolConfigDefaults = new ApplyEloToolConfigDefaults();

   @Override
   public String toString()
   {
      return "BasicEloToolConfigsEloContent{" + "eloToolConfigs=" + eloToolConfigs + '}';
   }

   @Override
   public List<EloToolConfig> getEloToolConfigs()
   {
      return eloToolConfigs;
   }

   @Override
   public void setEloToolConfigs(List<EloToolConfig> eloToolConfigs)
   {
      this.eloToolConfigs = eloToolConfigs;
      eloToolConfigsMap.clear();
      for (EloToolConfig eloToolConfig : eloToolConfigs)
      {
         eloToolConfigsMap.put(eloToolConfig.getEloType(), eloToolConfig);
      }
      EloToolConfig defaultEloToolConfigs = eloToolConfigsMap
               .get(ApplyEloToolConfigDefaults.defaultEloToolConfigType);
      if (defaultEloToolConfigs != null)
      {
         eloToolConfigsMap.remove(ApplyEloToolConfigDefaults.defaultEloToolConfigType);
      }
      applyEloToolConfigDefaults.setDefaultEloToolConfig(defaultEloToolConfigs);
   }

   @Override
   public EloToolConfig getEloToolConfig(String eloType)
   {
      EloToolConfig eloToolConfig = eloToolConfigsMap.get(eloType);
      if (eloToolConfig != null)
      {
         eloToolConfig = applyEloToolConfigDefaults.applyDefaults(eloToolConfig);
      }
      return eloToolConfig;
   }

}

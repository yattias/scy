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
   private HashMap<String,EloToolConfig> eloToolConfigsMap = new HashMap<String, EloToolConfig>();

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
      for (EloToolConfig eloToolConfig : eloToolConfigs){
         eloToolConfigsMap.put(eloToolConfig.getEloType(), eloToolConfig);
      }
   }

   @Override
   public EloToolConfig getEloToolConfig(String eloType)
   {
      return eloToolConfigsMap.get(eloType);
   }
}

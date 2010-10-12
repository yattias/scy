/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.common.mission;

import java.util.List;

/**
 *
 * @author SikkenJ
 */
public interface EloToolConfigsEloContent
{

   public List<EloToolConfig> getEloToolConfigs();

   public void setEloToolConfigs(List<EloToolConfig> eloToolConfigs);

   public EloToolConfig getEloToolConfig(String eloType);
}

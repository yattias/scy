/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.desktop.scydesktop.elofactory;

import eu.scy.common.mission.EloToolConfig;

/**
 *
 * @author sikken
 */
public interface EloConfigManager
{
   public EloToolConfig getEloToolConfig(String eloType);
   public void addDebugCreatorId(String creatorId);
   public void setDebug(boolean debug);
}

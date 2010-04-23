/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.desktop.scydesktop.elofactory;

import eu.scy.client.desktop.scydesktop.config.EloConfig;

/**
 *
 * @author sikken
 */
public interface EloConfigManager
{
   public EloConfig getEloConfig(String eloType);
   public void addDebugCreatorId(String creatorId);
   public void setDebug(boolean debug);
}

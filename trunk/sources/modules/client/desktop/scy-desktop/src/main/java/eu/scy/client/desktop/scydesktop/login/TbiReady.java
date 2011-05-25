/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.desktop.scydesktop.login;

import eu.scy.client.desktop.scydesktop.mission.Missions;
import eu.scy.toolbrokerapi.ToolBrokerAPI;

/**
 *
 * @author SikkenJ
 */
public interface TbiReady {

   public void tbiReady(ToolBrokerAPI tbi, Missions mission);

   public void tbiFailed(Exception e);
}

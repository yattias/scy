/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.desktop.scydesktop.tools.mission.springimport;

import eu.scy.client.desktop.scydesktop.config.BasicMissionAnchor;
import eu.scy.client.desktop.scydesktop.config.BasicMissionMap;
import java.net.URI;
import java.util.List;

/**
 *
 * @author SikkenJ
 */
public interface MissionConfigInput {

   public BasicMissionMap getBasicMissionMap();
   public List<BasicMissionAnchor> getBasicMissionAnchors();

   public List<EloToolConfig> getEloToolConfigs();

   public List<URI> getTemplateEloUris();
}

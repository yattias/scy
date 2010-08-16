/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.desktop.scydesktop.tools.mission.springimport;

import eu.scy.client.desktop.scydesktop.config.BasicMissionAnchor;
import eu.scy.client.desktop.scydesktop.config.BasicMissionMap;
import eu.scy.client.desktop.scydesktop.config.DisplayNames;
import eu.scy.client.desktop.scydesktop.config.NewEloDescription;
import java.net.URI;
import java.util.List;

/**
 *
 * @author SikkenJ
 */
public interface MissionConfigInput {

   public BasicMissionMap getBasicMissionMap();
   public List<BasicMissionAnchor> getBasicMissionAnchors();

   public List<URI> getAllMissionEloUris();

//   public List<BasicMissionAnchorConfig> getBasicMissionAnchorConfigs();
//   public String getMissionId();
//   public String getMissionName();
//   public URI getActiveMissionAnchorUri();

   public List<NewEloDescription> getNewEloDescriptions();

   public List<URI> getTemplateEloUris();

   public DisplayNames getLogicalTypeDisplayNames();
   public DisplayNames getFunctionalTypeDisplayNames();

}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.desktop.scydesktop.tools.mission.springimport;

import eu.scy.common.mission.EloToolConfig;
import eu.scy.common.mission.MissionModelEloContent;
import java.net.URI;
import java.util.List;

/**
 *
 * @author SikkenJ
 */
public interface MissionConfigInput {

   public URI getColorSchemesEloUri();

   public URI getMissionDescriptionUri();
   
   public MissionModelEloContent getMissionModelEloContent();

   public List<EloToolConfig> getEloToolConfigs();

   public List<URI> getTemplateEloUris();

   public List<String> getErrors();
}

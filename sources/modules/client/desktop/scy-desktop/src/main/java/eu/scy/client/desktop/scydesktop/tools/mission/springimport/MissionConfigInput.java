/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.desktop.scydesktop.tools.mission.springimport;

import eu.scy.common.mission.EloToolConfig;
import eu.scy.common.mission.MissionModelEloContent;
import java.net.URI;
import java.util.List;
import java.util.Locale;

/**
 *
 * @author SikkenJ
 */
public interface MissionConfigInput {

   public URI getColorSchemesEloUri();

   public URI getAgentModelsEloUri();

   public URI getMissionDescriptionUri();
   
   public MissionModelEloContent getMissionModelEloContent();

   public List<EloToolConfig> getEloToolConfigs();

   public List<URI> getTemplateEloUris();

   public List<String> getErrors();

   public String getMissionId();

   public String getXhtmlVersionId();

   public Locale getLanguage();

   public String getMissionTitle();
}

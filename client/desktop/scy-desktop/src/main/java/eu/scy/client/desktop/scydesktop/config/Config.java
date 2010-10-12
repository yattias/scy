/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.desktop.scydesktop.config;

import eu.scy.client.desktop.scydesktop.elofactory.RegisterContentCreators;
import eu.scy.common.mission.impl.BasicEloToolConfig;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import java.io.File;
import java.net.URI;
import java.util.List;
import roolo.api.IExtensionManager;
import roolo.api.IRepository;
import roolo.elo.api.IELOFactory;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.IMetadataTypeManager;

/**
 *
 * @author sikkenj
 */
public interface Config {

   public IRepository getRepository();
   public IExtensionManager getExtensionManager();
   public IMetadataTypeManager getMetadataTypeManager();
   public IELOFactory getEloFactory();

   public ToolBrokerAPI getToolBrokerAPI();

   public IMetadataKey getTitleKey();
   public IMetadataKey getTechnicalFormatKey();

   public RegisterContentCreators[] getRegisterContentCreators();

   public BasicEloToolConfig getEloToolConfig(String eloType);

   public BasicMissionMap getBasicMissionMap();
   public List<BasicMissionAnchor> getBasicMissionAnchors();

   public List<URI> getAllMissionEloUris();

//   public List<BasicMissionAnchorConfig> getBasicMissionAnchorConfigs();
//   public String getMissionId();
//   public String getMissionName();
//   public URI getActiveMissionAnchorUri();

   public List<String> getNewEloTypes();

   public List<URI> getTemplateEloUris();

   public DisplayNames getLogicalTypeDisplayNames();
   public DisplayNames getFunctionalTypeDisplayNames();

   public File getLoggingDirectory();
   public boolean isRedirectSystemStreams();

   public String getBackgroundImageFileName();
   public boolean isBackgroundImageFileNameRelative();
}

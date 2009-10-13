/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.desktop.scydesktop.config;

import eu.scy.client.desktop.scydesktop.elofactory.RegisterContentCreators;
import eu.scy.client.desktop.scydesktop.tools.corner.missionmap.MissionAnchor;
import eu.scy.client.desktop.scydesktop.missionmap.MissionModelCreator;
import java.io.File;
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

   public IMetadataKey getTitleKey();
   public IMetadataKey getTechnicalFormatKey();

   public MissionModelCreator getMissionModelCreator();
   public RegisterContentCreators[] getRegisterContentCreators();

   public EloConfig getEloConfig(String eloType);

   public List<BasicMissionAnchorConfig> getBasicMissionAnchorConfigs();
   public String getActiveMissionAnchorUri();
   public List<MissionAnchor> getMissionAnchors();

   public List<NewEloDescription> getNewEloDescriptions();

   public File getLoggingDirectory();
   public boolean isRedirectSystemStreams();

   public String getBackgroundImageFileName();
   public boolean isBackgroundImageFileNameRelative();
}

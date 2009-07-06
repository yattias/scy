/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.desktop.scydesktop.config;

import eu.scy.client.desktop.scydesktop.elofactory.RegisterWindowContentCreators;
import eu.scy.client.desktop.scydesktop.missionmap.MissionModelCreator;
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

   public MissionModelCreator getMissionModelCreator();
   public RegisterWindowContentCreators[] getRegisterWindowContentCreators();
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.roolows.config;

import eu.scy.actionlogging.api.IActionLogger;
import eu.scy.common.configuration.Configuration;
import java.io.File;

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

   public File getLoggingDirectory();
   public boolean isRedirectSystemStreams();

   public String getBackgroundImageFileName();
   public boolean isBackgroundImageFileNameRelative();

   public Configuration getServerConfig();
   public IActionLogger getActionLogger();

   public String getPasswordServiceURL();
}

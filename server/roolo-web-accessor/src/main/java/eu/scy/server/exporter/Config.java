/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.server.exporter;

import roolo.api.IExtensionManager;
import roolo.api.IRepository;
import roolo.elo.api.IELOFactory;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.IMetadataTypeManager;
import eu.scy.common.configuration.Configuration;

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

   public Configuration getServerConfig();
}

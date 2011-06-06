package eu.scy.common.scyelo;

import roolo.api.IExtensionManager;
import roolo.api.IRepository;
import roolo.elo.api.IELOFactory;
import roolo.elo.api.IMetadataTypeManager;


/**
 * This interface is used to pass on all four RoOLO services.
 *  
 * @author SikkenJ
 *
 */
public interface RooloServices
{
   /**
    * This method returns the repository instance
    * 
    * @return repository the repository instance
    */
   public IRepository getRepository();

   /**
    * @return the metaDataTypeManager
    */
   public IMetadataTypeManager getMetaDataTypeManager();

   /**
    * @return the extensionManager
    */
   public IExtensionManager getExtensionManager();

   public IELOFactory getELOFactory();
   

}

package eu.scy.utils.missioncopier
import eu.scy.common.scyelo.RooloServices
import roolo.api.IRepository
import roolo.api.IExtensionManager
import roolo.elo.api.IELOFactory
import roolo.elo.api.IMetadataTypeManager

class SimpleRooloServices(repository: IRepository, metadataTypeManager: IMetadataTypeManager, extensionManager: IExtensionManager, eloFactory: IELOFactory) extends RooloServices {

   def getRepository() = repository
   
   def getMetaDataTypeManager() = metadataTypeManager
   
   def getExtensionManager() = extensionManager
   
   def getELOFactory() = eloFactory
}
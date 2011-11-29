package eu.scy.utils.missioncopier

import console.ConsoleModel
import roolo.elo.api.IMetadataTypeManager
import roolo.elo.api.IELOFactory
import roolo.api.IRepository
import roolo.api.IExtensionManager
import eu.scy.common.scyelo.RooloServices

class StateModel extends ConsoleModel {
	var metadataTypeManager: IMetadataTypeManager = null
	var extensionManager: IExtensionManager = null
	var eloFactory: IELOFactory = null
	var sourceRepository:IRepository = null
	var destinationRepository:IRepository = null
	var sourceRooloServices:RooloServices = null
	var destinationRooloServices:RooloServices = null
  var sourceIsRooloMock = false
  var destinationIsRooloMock = false
  var sourceName = ""
  var destinationName = ""
}
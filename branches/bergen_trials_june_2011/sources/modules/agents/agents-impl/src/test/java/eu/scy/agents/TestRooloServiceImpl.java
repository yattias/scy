package eu.scy.agents;

import roolo.api.IExtensionManager;
import roolo.api.IRepository;
import roolo.elo.api.IELOFactory;
import roolo.elo.api.IMetadataTypeManager;
import eu.scy.common.scyelo.RooloServices;

public class TestRooloServiceImpl implements RooloServices {

	private IRepository repository;
	private IMetadataTypeManager metadataTypeManager;
	private IExtensionManager extensionManager;
	private IELOFactory eloFactory;

	public TestRooloServiceImpl(IRepository repository) {
		this.repository = repository;
	}

	@Override
	public IRepository getRepository() {
		return repository;
	}

	@Override
	public IMetadataTypeManager getMetaDataTypeManager() {
		return metadataTypeManager;
	}

	@Override
	public IExtensionManager getExtensionManager() {
		return extensionManager;
	}

	@Override
	public IELOFactory getELOFactory() {
		return eloFactory;
	}

	public IMetadataTypeManager getMetadataTypeManager() {
		return metadataTypeManager;
	}

	public void setMetadataTypeManager(IMetadataTypeManager metadataTypeManager) {
		this.metadataTypeManager = metadataTypeManager;
	}

	public void setEloFactory(IELOFactory eloFactory) {
		this.eloFactory = eloFactory;
	}

	public void setRepository(IRepository repository) {
		this.repository = repository;
	}

	public void setExtensionManager(IExtensionManager extensionManager) {
		this.extensionManager = extensionManager;
	}

}

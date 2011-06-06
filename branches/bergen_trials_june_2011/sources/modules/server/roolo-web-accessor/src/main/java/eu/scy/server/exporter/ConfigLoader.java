package eu.scy.server.exporter;

import org.apache.log4j.BasicConfigurator;

import roolo.api.IExtensionManager;
import roolo.api.IRepository;
import roolo.elo.api.IELOFactory;
import roolo.elo.api.IMetadataTypeManager;
import eu.scy.common.configuration.Configuration;

/**
 * 
 * @author Sven
 */
public class ConfigLoader {

    private Config config;
    private IRepository repository;
    private IMetadataTypeManager typeManager;
    private IExtensionManager extensionManager;
    private IELOFactory eloFactory;
    private Configuration serverConfig;
    private static ConfigLoader instance;

    private ConfigLoader() {
        this("eu/scy/server/exporter/beans.xml");
    }

    private ConfigLoader(String path) {
        BasicConfigurator.configure();

        // reading the beans
        SpringConfigFactory springConfigFactory = new SpringConfigFactory();
        springConfigFactory.initFromClassPath(path);

        config = springConfigFactory.getConfig();
        repository = config.getRepository();
        typeManager = config.getMetadataTypeManager();
        extensionManager = config.getExtensionManager();
        eloFactory = config.getEloFactory();
        serverConfig = config.getServerConfig();
    }

    public static ConfigLoader getInstance() {
        if (instance == null) {
            instance = new ConfigLoader();
        }
        return instance;
    }

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    public IRepository getRepository() {
        return repository;
    }

    public void setRepository(IRepository repository) {
        this.repository = repository;
    }

    public IExtensionManager getExtensionManager() {
        return extensionManager;
    }

    public void setExtensionManager(IExtensionManager extensionManager) {
        this.extensionManager = extensionManager;
    }

    public IMetadataTypeManager getTypeManager() {
        return typeManager;
    }

    public void setTypeManager(IMetadataTypeManager typeManager) {
        this.typeManager = typeManager;
    }

    public IELOFactory getEloFactory() {
        return eloFactory;
    }

    public void setEloFactory(IELOFactory eloFactory) {
        this.eloFactory = eloFactory;
    }

    public Configuration getServerConfig() {
        return serverConfig;
    }

    public void setServerConfig(Configuration serverConfig) {
        this.serverConfig = serverConfig;
    }


}

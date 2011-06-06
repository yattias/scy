/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.roolows;

import eu.scy.actionlogging.api.IActionLogger;
import eu.scy.common.configuration.Configuration;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import roolo.api.IExtensionManager;
import roolo.api.IRepository;
import roolo.elo.api.IELOFactory;
import roolo.elo.api.IMetadataTypeManager;
import eu.scy.roolows.config.Config;
import eu.scy.roolows.config.SpringConfigFactory;

/**
 * 
 * @author Sven
 */
public class Beans {

    private final static Logger logger = Logger.getLogger(Beans.class.getName());
    private Config config;
    private IRepository repository;
    private IMetadataTypeManager typeManager;
    private IExtensionManager extensionManager;
    private IELOFactory eloFactory;
    private Configuration serverConfig;
    private IActionLogger actionLogger;
    private static Beans instance;
    private String passwordServiceURL;

    private Beans() {
        this("beans.xml");
//		this("localRooloConfig.xml");
    }

    private Beans(String path) {
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
        actionLogger = config.getActionLogger();
        passwordServiceURL = config.getPasswordServiceURL();
    }

    public static Beans getInstance() {
        if (instance == null) {
            instance = new Beans();
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

    public IActionLogger getActionLogger() {
        return actionLogger;
    }

    public void setActionLogger(IActionLogger actionLogger) {
        this.actionLogger = actionLogger;
    }

    public String getPasswordServiceURL() {
        return passwordServiceURL;
    }

    public void setPasswordServiceURL(String passwordServiceURL) {
        this.passwordServiceURL = passwordServiceURL;
    }

}

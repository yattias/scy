/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.webbrowsingtoolelosaver;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import roolo.api.IExtensionManager;
import roolo.api.IRepository;
import roolo.elo.api.IMetadataTypeManager;
import eu.scy.webbrowsingtoolelosaver.config.Config;
import eu.scy.webbrowsingtoolelosaver.config.SpringConfigFactory;

/**
 *
 * @author Sven
 */
public class ConfigLoader {

    private Config config;
    private IRepository repository;
    private IMetadataTypeManager typeManager;
    private IExtensionManager extensionManager;
    private final static Logger log = Logger.getLogger(SaveELOResource.class.getName());


    private static ConfigLoader instance;

    private ConfigLoader(){
        this("beans.xml");
    }


    private ConfigLoader(String path){
        BasicConfigurator.configure();

        //reading the beans
        SpringConfigFactory springConfigFactory = new SpringConfigFactory();
//        springConfigFactory.initFromFileSystem(path);
        springConfigFactory.initFromClassPath(path);

        config = springConfigFactory.getConfig();
        repository = config.getRepository();
        typeManager = config.getMetadataTypeManager();
        extensionManager = config.getExtensionManager();
    }

    public static ConfigLoader getInstance(){
        if (instance == null){
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

    
    

}

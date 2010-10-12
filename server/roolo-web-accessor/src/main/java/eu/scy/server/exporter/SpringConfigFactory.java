/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.server.exporter;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import eu.scy.common.configuration.Configuration;

import roolo.api.IExtensionManager;
import roolo.api.IRepository;
import roolo.elo.api.IELOFactory;
import roolo.elo.api.IMetadataTypeManager;

/**
 *
 * @author sikkenj
 */

public class SpringConfigFactory {
   
   private final static Logger logger = Logger.getLogger(SpringConfigFactory.class);

   private ApplicationContext context;

   private Config config;

   public SpringConfigFactory()
   {

   }

   public void initFromClassPath(String location)
   {
      context = new ClassPathXmlApplicationContext(location);
      if (context==null){
         throw new IllegalArgumentException("failed to load context from classpath: " + location);
      }
   }


   private void readBeans(){
      BasicConfig config = new BasicConfig();
      config.setMetadataTypeManager((IMetadataTypeManager) getBean("metadataTypeManager"));
      config.setExtensionManager((IExtensionManager) getBean("extensionManager"));
      config.setEloFactory((IELOFactory) getBean("eloFactory"));
      config.setRepository((IRepository) getBean("repository"));
      config.setServerConfig((Configuration) getBean("serverConfig"));
      
      this.config = config;
   }

   private Object getBean(String name)
   {
      try{
         return context.getBean(name);
      }
      catch (NoSuchBeanDefinitionException e)
      {
         logger.info("failed to find bean named: " + name);
      }
      return null;
   }

   public Config getConfig()
   {
      if (config==null){
         readBeans();
      }
      return config;
   }
}

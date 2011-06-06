/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.roolows.config;

import eu.scy.actionlogging.api.IActionLogger;
import eu.scy.common.configuration.Configuration;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

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

   public void initFromFileSystem(String location)
   {
      context = new FileSystemXmlApplicationContext(location);
      if (context==null){
         throw new IllegalArgumentException("failed to load context from file system: " + location);
      }
   }

   public void addFromClassPath(String location)
   {
      String[] contextPaths = new String[] { location };
      ApplicationContext newContext = new ClassPathXmlApplicationContext(contextPaths,context);
      context = newContext;
   }

   public void addFromFileSystem(String location)
   {
      String[] contextPaths = new String[] { location };
      ApplicationContext newContext = new FileSystemXmlApplicationContext(contextPaths,context);
      context = newContext;
   }

   private void readBeans(){

      BasicConfig newConfig = new BasicConfig();
      newConfig.setMetadataTypeManager((IMetadataTypeManager) getBean("metadataTypeManager"));
      newConfig.setExtensionManager((IExtensionManager) getBean("extensionManager"));
      newConfig.setEloFactory((IELOFactory) getBean("eloFactory"));
      newConfig.setRepository((IRepository) getBean("repository"));
      newConfig.setServerConfig((Configuration) getBean("serverConfig"));
      newConfig.setActionLogger((IActionLogger) getBean("actionLogger"));
      newConfig.setPasswordServiceURL((String) getBean("passwordServiceURL"));
      this.config = newConfig;
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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.webbrowsingtoolelosaver.config;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import eu.scy.webbrowsingtoolelosaver.SaveELOResource;

/**
 *
 * @author sikkenj
 */

public class SpringConfigFactory {
   
   private final static Logger logger = Logger.getLogger(SaveELOResource.class.getName());

   private ApplicationContext context;

   private Config config;

   public SpringConfigFactory()
   {

   }

   public void initFromClassPath(String location)
   {
       BasicConfigurator.configure();
       context = new ClassPathXmlApplicationContext(location);
       
       

      if (context==null){
         throw new IllegalArgumentException("failed to load context from classpath: " + location);
      }
      readBeans();
   }

   public void initFromFileSystem(String location)
   {
      context = new FileSystemXmlApplicationContext(location);
      if (context==null){
         throw new IllegalArgumentException("failed to load context from file system: " + location);
      }
      readBeans();
   }

   private void readBeans(){

      config = (Config) getBean("saveEloConfig");
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
      return config;
   }
}

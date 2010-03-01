/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.desktop.scydesktop.config;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

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
//      readBeans();
   }

   public void initFromFileSystem(String location)
   {
      context = new FileSystemXmlApplicationContext(location);
      if (context==null){
         throw new IllegalArgumentException("failed to load context from file system: " + location);
      }
//      readBeans();
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

      config = (Config) getBean("scyDesktopConfig");
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

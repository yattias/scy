/*
 * ApplicationParameters.fx
 *
 * Created on 20-nov-2009, 11:02:51
 */

package eu.scy.client.desktop.scydesktop;

import eu.scy.client.desktop.desktoputils.log4j.Logger;

import java.lang.IllegalArgumentException;

/**
 * @author sikken
 */

public class ApplicationParameters {
   def logger = Logger.getLogger(this.getClass());

   public-read var servicesClassPathConfigLocation:String;
   public-read var servicesFileSystemConfigLocation:String;
   public-read var configClassPathConfigLocation:String;
   public-read var configFileSystemConfigLocation:String;

   def servicesClassPathConfigLocationOption = "servicesClassPathConfigLocation";
   def servicesFileSystemConfigLocationOption = "servicesFileSystemConfigLocation";
   def configClassPathConfigLocationOption = "configClassPathConfigLocation";
   def configFileSystemConfigLocationOption = "configFileSystemConfigLocation";

   init{
      parseApplicationParameters();
      parseWebstartParameters();
   }

   function parseApplicationParameters(){
      var args = FX.getArguments();
      if (args==null){
         return;
      }
      var i=0;
      while (i<args.size()){
         var lcArg = args[i].toLowerCase();
         if (lcArg.startsWith('-')){
            var option = lcArg.substring(1);
            if (option==servicesClassPathConfigLocationOption.toLowerCase()){
               ++i;
					if (i >= args.size())
					{
						throw new IllegalArgumentException("cannot find {servicesClassPathConfigLocationOption}");
					}
               servicesClassPathConfigLocation = args[i];
               servicesFileSystemConfigLocation = "";
               logger.info("app: {servicesClassPathConfigLocationOption}: {servicesClassPathConfigLocation}");
            }
            else if (option==servicesFileSystemConfigLocation.toLowerCase()){
               ++i;
					if (i >= args.size())
					{
						throw new IllegalArgumentException("cannot find {servicesFileSystemConfigLocation}");
					}
               servicesFileSystemConfigLocation = args[i];
               servicesClassPathConfigLocation = "";
               logger.info("app: {servicesFileSystemConfigLocation}: {servicesFileSystemConfigLocation}");
            }
            else if (option==configClassPathConfigLocationOption.toLowerCase()){
               ++i;
					if (i >= args.size())
					{
						throw new IllegalArgumentException("cannot find {configClassPathConfigLocationOption}");
					}
               configClassPathConfigLocation = args[i];
               configFileSystemConfigLocation = "";
               logger.info("app: {configClassPathConfigLocationOption}: {configClassPathConfigLocation}");
            }
            else if (option==configFileSystemConfigLocationOption.toLowerCase()){
               ++i;
					if (i >= args.size())
					{
						throw new IllegalArgumentException("cannot find {configFileSystemConfigLocationOption}");
					}
               configFileSystemConfigLocation = args[i];
               configClassPathConfigLocation = "";
               logger.info("app: {configFileSystemConfigLocationOption}: {configFileSystemConfigLocation}");
            }
            else{
               logger.info("Unknown option: {option}");
            }
         }
         else{
            logger.info("ignored parameter: {args[i]}");
         }

         i++;
      }
   }

   function parseWebstartParameters(){
      servicesClassPathConfigLocation = getWebstartParameterValue(servicesClassPathConfigLocationOption,servicesClassPathConfigLocation);
      servicesFileSystemConfigLocation = getWebstartParameterValue(servicesFileSystemConfigLocationOption,servicesFileSystemConfigLocation);
      configClassPathConfigLocation = getWebstartParameterValue(configClassPathConfigLocationOption,configClassPathConfigLocation);
      configFileSystemConfigLocation = getWebstartParameterValue(configFileSystemConfigLocationOption,configFileSystemConfigLocation);
   }

   function getWebstartParameterValue(name:String,default:String){
      var webstartValue = FX.getArgument(name) as String;
      if (isEmpty(webstartValue)){
         return default;
      }
      logger.info("ws: {name}: {webstartValue}");
      return webstartValue;
   }

   public function returnValueIfNotEmpty(value:String, default:String):String{
      if (isEmpty(value)){
         return default;
      }
      return value;
   }

   public function isEmpty(string:String):Boolean{
      return string==null or string.length()==0;
   }

}

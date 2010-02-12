/*
 * Initializer.fx
 *
 * Created on 9-dec-2009, 10:28:50
 */
package eu.scy.client.desktop.scydesktop;

import javafx.scene.image.Image;
import eu.scy.client.desktop.scydesktop.utils.log4j.InitLog4JFX;
import eu.scy.client.desktop.scydesktop.utils.InitJavaUtilLogging;
import java.io.File;
import java.lang.IllegalArgumentException;
import java.lang.System;
import java.io.FileOutputStream;
import java.io.PrintStream;
import eu.scy.client.desktop.scydesktop.utils.ExceptionCatcher;
import java.lang.Thread;
import java.lang.Exception;
import javax.swing.UIManager;
import javafx.scene.image.ImageView;
import javafx.scene.Scene;
import eu.scy.client.desktop.scydesktop.login.ToolBrokerLogin;
import eu.scy.client.desktop.scydesktop.dummy.LocalToolBrokerLogin;
import eu.scy.client.desktop.scydesktop.utils.log4j.Logger;
import eu.scy.client.desktop.scydesktop.login.RemoteToolBrokerLogin;
import eu.scy.client.desktop.scydesktop.utils.RedirectSystemStreams;
import javax.jnlp.ServiceManager;
import eu.scy.common.configuration.Configuration;
//import javax.swing.UIManager.LookAndFeelInfo;

/**
 * @author sikken
 */
// place your code here

public class Initializer {
   def logger = Logger.getLogger(this.getClass());

   public-init var log4JInitFile = "";
   public-init var javaUtilLoggingInitFile = "";
   public-init var backgroundImageUrl = "{__DIR__}images/bckgrnd2l.jpg";
//   public-init var backgroundImageUrl = "http://www.scy-lab.eu/content/backgrounds/bckgrnd2.jpg"; // "{__DIR__}images/bckgrnd2.jpg";
   public-init var enableLocalLogging = true;
   public-init var loggingDirectoryName = "logging";
   public-init var redirectSystemStream = false;
   public-init var lookAndFeel = "nimbus";
   public-init var loginType = "local";
   public-init var localToolBrokerLoginConfigFile: String = "/config/localScyServices.xml";
   public-init var remoteToolBrokerLoginConfigFile: String = "/config/remoteScyServices.xml";
   public-init var defaultUserName: String;
   public-init var defaultPassword: String;
   public-init var autoLogin = false;
   public-init var scyDesktopConfigFile: String;
   public-init var storeElosOnDisk = true;
   public-init var createPersonalMissionMap = true;
   public-init var eloImagesPath = "{__DIR__}imagewindowstyler/images/";
   public-init var scyServerHost:String;
   public-init var useWebStartHost=true;
//   public-init var eloImagesPath = "http://www.scy-lab.eu/content/backgrounds/eloIcons/";
   public-read var backgroundImage: Image;
   public-read var localLoggingDirectory: File = null;
   public-read var toolBrokerLogin: ToolBrokerLogin;
   def systemOutFileName = "systemOut";
   def systemErrFileName = "systemErr";
   def enableLocalLoggingKey = "enableLocalLogging";
   def loggingDirectoryKey = "loggingDirectory";
   def storeElosOnDiskKey = "storeElosOnDisk";
   def scyServerNameKey = "serverName";
   def sqlspacesServerKey = "sqlspacesServer";
   // parameter option names
   def log4JInitFileOption = "log4JInitFile";
   def backgroundImageUrlOption = "backgroundImageUrl";
   def enableLocalLoggingOption = "enableLocalLogging";
   def loggingDirectoryNameOption = "loggingDirectoryName";
   def redirectSystemStreamOption = "redirectSystemStream";
   def lookAndFeelOption = "lookAndFeel";
   def loginTypeOption = "loginType";
   def localToolBrokerLoginConfigFileOption = "localToolBrokerLoginConfigFile";
   def remoteToolBrokerLoginConfigFileOption = "remoteToolBrokerLoginConfigFile";
   def defaultUserNameOption = "defaultUserName";
   def defaultPasswordOption = "defaultPassword";
   def autoLoginOption = "autoLogin";
   def scyDesktopConfigFileOption = "scyDesktopConfigFile";
   def storeElosOnDiskOption = "storeElosOnDisk";
   def createPersonalMissionMapOption = "createPersonalMissionMap";
   def eloImagesPathOption = "eloImagesPath";
   def scyServerHostOption = "scyServerHost";
   def useWebStartHostOption = "useWebStartHost";

   init {
      JavaProperties.writePropertiesForApplication();
      parseApplicationParameters();
      parseWebstartParameters();
      Thread.setDefaultUncaughtExceptionHandler(new ExceptionCatcher("SCY-LAB"));
      setupBackgroundImage();
      System.setProperty(enableLocalLoggingKey, "{enableLocalLogging}");
      var loggingDirectoryKeyValue = "";
      if (enableLocalLogging) {
         localLoggingDirectory = findLocalLoggingDirectory();
         if (localLoggingDirectory != null) {
            if (redirectSystemStream) {
               doRedirectSystemStream();
            }
            loggingDirectoryKeyValue = localLoggingDirectory.getAbsolutePath();
         }
      }
      System.setProperty(loggingDirectoryKey, loggingDirectoryKeyValue);
      System.setProperty(storeElosOnDiskKey, "{storeElosOnDisk}");
      setupCodeLogging();
      setLookAndFeel();
      setupScyServerHost();
      setupToolBrokerLogin();
   }

   function parseApplicationParameters() {
      var argumentsList = ArgumentsList {
                 arguments: FX.getArguments()
              }
      while (argumentsList.hasMoreArguments()) {
         var argument = argumentsList.nextArgument();
         var lcArg = argument.toLowerCase();
         if (lcArg.startsWith('-')) {
            var option = lcArg.substring(1);
            if (option == log4JInitFileOption.toLowerCase()) {
               log4JInitFile = argumentsList.nextStringValue(log4JInitFileOption);
               logger.info("app: {log4JInitFileOption}: {log4JInitFile}");
            } else if (option == backgroundImageUrlOption.toLowerCase()) {
               backgroundImageUrl = argumentsList.nextStringValue(backgroundImageUrlOption);
               logger.info("app: {backgroundImageUrlOption}: {backgroundImageUrl}");
            } else if (option == enableLocalLoggingOption.toLowerCase()) {
               enableLocalLogging = argumentsList.nextBooleanValue(enableLocalLoggingOption);
               logger.info("app: {enableLocalLoggingOption}: {enableLocalLogging}");
            } else if (option == loggingDirectoryNameOption.toLowerCase()) {
               loggingDirectoryName = argumentsList.nextStringValue(loggingDirectoryNameOption);
               logger.info("app: {loggingDirectoryNameOption}: {loggingDirectoryName}");
            } else if (option == redirectSystemStreamOption.toLowerCase()) {
               redirectSystemStream = argumentsList.nextBooleanValue(redirectSystemStreamOption);
               logger.info("app: {redirectSystemStreamOption}: {redirectSystemStream}");
            } else if (option == lookAndFeelOption.toLowerCase()) {
               lookAndFeel = argumentsList.nextStringValue(lookAndFeelOption);
               logger.info("app: {lookAndFeelOption}: {lookAndFeel}");
            } else if (option == loginTypeOption.toLowerCase()) {
               loginType = argumentsList.nextStringValue(loginTypeOption);
               logger.info("app: {loginTypeOption}: {loginType}");
            } else if (option == localToolBrokerLoginConfigFileOption.toLowerCase()) {
               localToolBrokerLoginConfigFile = argumentsList.nextStringValue(localToolBrokerLoginConfigFileOption);
               logger.info("app: {localToolBrokerLoginConfigFileOption}: {localToolBrokerLoginConfigFile}");
            } else if (option == remoteToolBrokerLoginConfigFileOption.toLowerCase()) {
               remoteToolBrokerLoginConfigFile = argumentsList.nextStringValue(remoteToolBrokerLoginConfigFileOption);
               logger.info("app: {remoteToolBrokerLoginConfigFileOption}: {remoteToolBrokerLoginConfigFile}");
            } else if (option == defaultUserNameOption.toLowerCase()) {
               defaultUserName = argumentsList.nextStringValue(defaultUserNameOption);
               logger.info("app: {defaultUserNameOption}: {defaultUserName}");
            } else if (option == defaultPasswordOption.toLowerCase()) {
               defaultPassword = argumentsList.nextStringValue(defaultPasswordOption);
               logger.info("app: {defaultPasswordOption}: {defaultPassword}");
            } else if (option == autoLoginOption.toLowerCase()) {
               autoLogin = argumentsList.nextBooleanValue(autoLoginOption);
               logger.info("app: {autoLoginOption}: {autoLogin}");
            } else if (option == scyDesktopConfigFileOption.toLowerCase()) {
               scyDesktopConfigFile = argumentsList.nextStringValue(scyDesktopConfigFileOption);
               logger.info("app: {scyDesktopConfigFileOption}: {scyDesktopConfigFile}");
            } else if (option == storeElosOnDiskOption.toLowerCase()) {
               storeElosOnDisk = argumentsList.nextBooleanValue(storeElosOnDiskOption);
               logger.info("app: {storeElosOnDiskOption}: {storeElosOnDisk}");
            } else if (option == createPersonalMissionMapOption.toLowerCase()) {
               createPersonalMissionMap = argumentsList.nextBooleanValue(createPersonalMissionMapOption);
               logger.info("app: {createPersonalMissionMapOption}: {createPersonalMissionMap}");
            } else if (option == eloImagesPathOption.toLowerCase()) {
               eloImagesPath = argumentsList.nextStringValue(eloImagesPathOption);
               logger.info("app: {eloImagesPathOption}: {eloImagesPath}");
            } else if (option == scyServerHostOption.toLowerCase()) {
               scyServerHost = argumentsList.nextStringValue(scyServerHostOption);
               logger.info("app: {scyServerHostOption}: {scyServerHost}");
            } else if (option == useWebStartHostOption.toLowerCase()) {
               useWebStartHost = argumentsList.nextBooleanValue(useWebStartHostOption);
               logger.info("app: {useWebStartHostOption}: {useWebStartHost}");
            } else {
               logger.info("Unknown option: {option}");
            }
         } else {
            logger.info("ignored parameter: {argument}");
         }
      }
   }

   function parseWebstartParameters() {
      log4JInitFile = getWebstartParameterStringValue(log4JInitFileOption, log4JInitFile);
      backgroundImageUrl = getWebstartParameterStringValue(backgroundImageUrlOption, backgroundImageUrl);
      enableLocalLogging = getWebstartParameterBooleanValue(enableLocalLoggingOption, enableLocalLogging);
      loggingDirectoryName = getWebstartParameterStringValue(loggingDirectoryNameOption, loggingDirectoryName);
      redirectSystemStream = getWebstartParameterBooleanValue(redirectSystemStreamOption, redirectSystemStream);
      lookAndFeel = getWebstartParameterStringValue(lookAndFeelOption, lookAndFeel);
      loginType = getWebstartParameterStringValue(loginTypeOption, loginType);
      localToolBrokerLoginConfigFile = getWebstartParameterStringValue(localToolBrokerLoginConfigFileOption, localToolBrokerLoginConfigFile);
      remoteToolBrokerLoginConfigFile = getWebstartParameterStringValue(remoteToolBrokerLoginConfigFileOption, remoteToolBrokerLoginConfigFile);
      defaultUserName = getWebstartParameterStringValue(defaultUserNameOption, defaultUserName);
      defaultPassword = getWebstartParameterStringValue(defaultPasswordOption, defaultPassword);
      autoLogin = getWebstartParameterBooleanValue(autoLoginOption, autoLogin);
      scyDesktopConfigFile = getWebstartParameterStringValue(scyDesktopConfigFileOption, scyDesktopConfigFile);
      storeElosOnDisk = getWebstartParameterBooleanValue(storeElosOnDiskOption, storeElosOnDisk);
      createPersonalMissionMap = getWebstartParameterBooleanValue(createPersonalMissionMapOption, createPersonalMissionMap);
      eloImagesPath = getWebstartParameterStringValue(eloImagesPathOption, eloImagesPath);
      scyServerHost = getWebstartParameterStringValue(scyServerHostOption, scyServerHost);
      useWebStartHost = getWebstartParameterBooleanValue(useWebStartHostOption, useWebStartHost);
   }

   function getWebstartParameterStringValue(name: String, default: String): String {
      var webstartValue = FX.getArgument(name) as String;
      if (isEmpty(webstartValue)) {
         return default;
      }
      logger.info("ws: {name}: {webstartValue}");
      return webstartValue;
   }

   function getWebstartParameterBooleanValue(name: String, default: Boolean): Boolean {
      var webstartValue = FX.getArgument(name) as String;
      if (isEmpty(webstartValue)) {
         return default;
      }
      var boolValue = "true".equalsIgnoreCase(webstartValue);
      logger.info("ws: {name}: {boolValue}");
      return boolValue;
   }

   public function isEmpty(string: String): Boolean {
      return string == null or string.length() == 0;
   }

   public function getBackgroundImageView(scene: Scene): ImageView {
      var backgroundImageView: ImageView;
      if (backgroundImage != null) {
         backgroundImageView = ImageView {
            image: backgroundImage
            fitWidth: bind scene.width
            fitHeight: bind scene.height
            preserveRatio: false
            cache: true
         }
      }
      return backgroundImageView;
   }

   function setupCodeLogging() {
      if (log4JInitFile.length() > 0) {
         InitLog4JFX.initLog4J(log4JInitFile);
      } else {
         InitLog4JFX.initLog4J();
      }
      if (javaUtilLoggingInitFile.length() > 0) {
         InitJavaUtilLogging.initJavaUtilLogging(javaUtilLoggingInitFile);
      } else {
         InitJavaUtilLogging.initJavaUtilLogging();
      }
   }

   function setupBackgroundImage() {
      if (backgroundImageUrl.length() > 0) {
         logger.info("loading background image: {backgroundImageUrl}");
         backgroundImage = Image {
            url: backgroundImageUrl
         }
         logger.info("background image, error: {backgroundImage.error}, progress: {backgroundImage.progress}");
      }
      else{
         logger.info("no background image specified");
      }

   }

   function findLocalLoggingDirectory(): File {
      var logDirectory: File;
      if (loggingDirectoryName.length() > 0) {
         logDirectory = new File(loggingDirectoryName);
         if (not logDirectory.exists()) {
            throw new IllegalArgumentException("logging directory does not exists: {logDirectory.getAbsolutePath()}");
         }
         if (not logDirectory.isDirectory()) {
            throw new IllegalArgumentException("logging directory does not a directory: {logDirectory.getAbsolutePath()}");
         }
      }
      return logDirectory;
   }

   function doRedirectSystemStream() {
      System.setOut(createPrintStream(systemOutFileName));
      System.setErr(createPrintStream(systemErrFileName));
   }

   function createPrintStream(fileName: String): PrintStream// throws IOException
   {
//      var fileCount = 0;
//      var streamFile = new File(loggingDirectory, "{fileName}_{fileCount}.txt");
//      while (streamFile.exists()) {
//         ++fileCount;
//         streamFile = new File(loggingDirectory, "{fileName}_{fileCount}.txt");
//      }
      var outputStream = new FileOutputStream(RedirectSystemStreams.getLogFile(localLoggingDirectory,fileName,".txt"), false);
      return new PrintStream(outputStream, true);
   }

   function setLookAndFeel() {
      if (lookAndFeel.length() > 0) {
         var lookAndFeelClassName = findLookAndFeelClassName();
         if (lookAndFeelClassName != null) {
            try {
               UIManager.setLookAndFeel(lookAndFeelClassName);
               logger.info("set lookAndFeel to {lookAndFeel}, class {lookAndFeelClassName}");
            } catch (e: Exception) {
               logger.error("problems with setting the look and feel: {lookAndFeel}", e);
            }
         }
      }
   }

   function findLookAndFeelClassName(): String {
      if (lookAndFeel.indexOf(".") >= 0) {
         // its a class name
         return lookAndFeel;
      } else {
         var lookAndFeelInfos: javax.swing.UIManager.LookAndFeelInfo[] = UIManager.getInstalledLookAndFeels();
         for (lookAndFeelInfo in lookAndFeelInfos) {
            if (lookAndFeel.equalsIgnoreCase(lookAndFeelInfo.getName())) {
               return lookAndFeelInfo.getClassName();
            }
         }
         logger.info("failed to find look and feel named: {lookAndFeel}");
         return null;
      }
   }

   function setupScyServerHost(){
      var newScyServerHost:String;
      if (scyServerHost.length()>0){
         newScyServerHost = scyServerHost;
      }
      else if (useWebStartHost){
         try{
            var webstartServiceNames = javax.jnlp.ServiceManager.getServiceNames();
            logger.info("Available web start services: {webstartServiceNames}");
            var basicService = ServiceManager.lookup("javax.jnlp.BasicService") as javax.jnlp.BasicService;
            if (basicService!=null){
               var codeBase = basicService.getCodeBase();
               logger.info("webstart codeBase: {codeBase}");
               newScyServerHost = codeBase.getHost();
            }
         }
         catch (e:javax.jnlp.UnavailableServiceException){
            logger.info("cannot get scy server host from web start, as web start is not being used: {e}");
         }
      }
      if (newScyServerHost.length()>0){
         logger.info("setting scy server host to {newScyServerHost}");
         System.setProperty(scyServerNameKey, newScyServerHost);
         System.setProperty(sqlspacesServerKey, newScyServerHost);
         Configuration.getInstance().setScyServerHost(newScyServerHost);
      }
   }


   function setupToolBrokerLogin(){
      if ("local".equalsIgnoreCase(loginType)){
         var localToolBrokerLogin = new LocalToolBrokerLogin();
         localToolBrokerLogin.setSpringConfigFile(localToolBrokerLoginConfigFile);
         toolBrokerLogin = localToolBrokerLogin;
      }
      else if ("remote".equalsIgnoreCase(loginType)){
         var remoteToolBrokerLogin = new RemoteToolBrokerLogin();
         remoteToolBrokerLogin.setSpringConfigFile(remoteToolBrokerLoginConfigFile);
         toolBrokerLogin = remoteToolBrokerLogin;
      }
      else {
         throw new IllegalArgumentException("unknown login type: {loginType}");
      }
   }

}

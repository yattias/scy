/*
 * Initializer.fx
 *
 * Created on 9-dec-2009, 10:28:50
 */
package eu.scy.client.desktop.scydesktop;

import javafx.scene.image.Image;
import eu.scy.client.desktop.scydesktop.utils.log4j.InitLog4JFX;
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
import org.apache.log4j.Logger;
import eu.scy.client.desktop.scydesktop.login.RemoteToolBrokerLogin;
import javax.swing.UIManager.LookAndFeelInfo;

/**
 * @author sikken
 */
// place your code here
def logger = Logger.getLogger("eu.scy.client.desktop.scydesktop.Initializer");

public class Initializer {

   public-init var log4JInitFile = "";
   public-init var backgroundImageUrl = "{__DIR__}images/bckgrnd2.jpg";
   public-init var enableLogging = false;
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
   public-read var backgroundImage: Image;
   public-read var loggingDirectory: File = null;
   public-read var toolBrokerLogin: ToolBrokerLogin;
   def systemOutFileName = "systemOut";
   def systemErrFileName = "systemErr";
   def enableLoggingKey = "enableLogging";
   def loggingDirectoryKey = "loggingDirectory";
   // parameter option names
   def log4JInitFileOption = "log4JInitFile";
   def backgroundImageUrlOption = "backgroundImageUrl";
   def enableLoggingOption = "enableLogging";
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

   init {
      parseApplicationParameters();
      parseWebstartParameters();
      Thread.setDefaultUncaughtExceptionHandler(new ExceptionCatcher("SCY-LAB"));
      setupLog4J();
//      if (isEmpty(scyDesktopConfigFile)){
//         throw new IllegalArgumentException("{scyDesktopConfigFileOption} may not be empty");
//      }
      setupBackgroundImage();
      System.setProperty(enableLoggingKey, "{enableLogging}");
      var loggingDirectoryKeyValue = "";
      if (enableLogging) {
         loggingDirectory = findLoggingDirectory();
         if (loggingDirectory != null) {
            if (redirectSystemStream) {
               doRedirectSystemStream();
            }
            loggingDirectoryKeyValue = loggingDirectory.getAbsolutePath();
         }
      }
      System.setProperty(loggingDirectoryKey, loggingDirectoryKeyValue);
      setLookAndFeel();
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
            } else if (option == enableLoggingOption.toLowerCase()) {
               enableLogging = argumentsList.nextBooleanValue(enableLoggingOption);
               logger.info("app: {enableLoggingOption}: {enableLogging}");
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
      enableLogging = getWebstartParameterBooleanValue(enableLoggingOption, enableLogging);
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

   function setupLog4J() {
      if (log4JInitFile.length() > 0) {
         InitLog4JFX.initLog4J(log4JInitFile);
      } else {
         InitLog4JFX.initLog4J();
      }
   }

   function setupBackgroundImage() {
      if (backgroundImageUrl.length() > 0) {
         backgroundImage = Image {
            url: backgroundImageUrl
         }
      }
   }

   function findLoggingDirectory(): File {
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
      var fileCount = 0;
      var streamFile = new File(loggingDirectory, "{fileName}_{fileCount}.txt");
      while (streamFile.exists()) {
         ++fileCount;
         streamFile = new File(loggingDirectory, "{fileName}_{fileCount}.txt");
      }
      var outputStream = new FileOutputStream(streamFile, false);
      return new PrintStream(outputStream, true);
   }

   function setLookAndFeel() {
      if (lookAndFeel.length() > 0) {
         var lookAndFeelClassName = findLookAndFeelClassName();
         if (lookAndFeelClassName != null) {
            try {
               UIManager.setLookAndFeel(lookAndFeelClassName);
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
         var lookAndFeelInfos: LookAndFeelInfo[] = UIManager.getInstalledLookAndFeels();
         for (lookAndFeelInfo in lookAndFeelInfos) {
            if (lookAndFeel.equalsIgnoreCase(lookAndFeelInfo.getName())) {
               return lookAndFeelInfo.getClassName();
            }
         }
         logger.info("failed to find look and feel named: {lookAndFeel}");
         return null;
      }
   }

   function setupToolBrokerLogin(){
      if ("local".equalsIgnoreCase(loginType)){
         var localToolBrokerLogin = new LocalToolBrokerLogin();
         localToolBrokerLogin.setSpringConfigFile(localToolBrokerLoginConfigFile);
         toolBrokerLogin = localToolBrokerLogin;
      }
      if ("remote".equalsIgnoreCase(loginType)){
         var remoteToolBrokerLogin = new RemoteToolBrokerLogin();
         remoteToolBrokerLogin.setSpringConfigFile(remoteToolBrokerLoginConfigFile);
         toolBrokerLogin = remoteToolBrokerLogin;
      }
      else {
         throw new IllegalArgumentException("unknown login type: {loginType}");
      }
   }

}

/*
 * Initializer.fx
 *
 * Created on 9-dec-2009, 10:28:50
 */
package eu.scy.client.desktop.scydesktop;

import eu.scy.client.desktop.scydesktop.utils.log4j.InitLog4JFX;
import eu.scy.client.desktop.scydesktop.utils.InitJavaUtilLogging;
import java.io.File;
import java.lang.IllegalArgumentException;
import java.lang.System;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.lang.Thread;
import java.lang.Exception;
import javax.swing.UIManager;
import javafx.scene.Scene;
import eu.scy.client.desktop.scydesktop.utils.log4j.Logger;
import eu.scy.client.desktop.scydesktop.login.RemoteToolBrokerLogin;
import eu.scy.client.desktop.scydesktop.utils.RedirectSystemStreams;
import eu.scy.client.desktop.scydesktop.utils.SetupLoggingToFiles;
import javax.jnlp.ServiceManager;
import eu.scy.common.configuration.Configuration;
import javax.swing.JOptionPane;
import java.util.Date;
import java.net.InetAddress;
import java.io.PrintWriter;
import java.io.StringWriter;
import eu.scy.client.desktop.scydesktop.login.LoginDialog;
import eu.scy.client.desktop.scydesktop.uicontrols.DynamicTypeBackground;
import java.util.StringTokenizer;
import javafx.util.Sequences;
import javafx.util.StringLocalizer;
import eu.scy.client.desktop.scydesktop.utils.FilteringExceptionCatcher;
import eu.scy.client.desktop.scydesktop.scywindows.window.MouseBlocker;
import eu.scy.client.desktop.scydesktop.tooltips.impl.SimpleTooltipManager;
import eu.scy.client.common.scyi18n.UriLocalizer;
import eu.scy.toolbrokerapi.ToolBrokerLogin;
import eu.scy.client.desktop.localtoolbroker.LocalToolBrokerLogin;
import eu.scy.client.desktop.localtoolbroker.LocalMultiUserToolBrokerLogin;
import eu.scy.client.desktop.scydesktop.mission.MissionRunConfigs;
import eu.scy.client.desktop.scydesktop.tools.mission.EloXmlEditor;
import eu.scy.client.desktop.scydesktop.scywindows.ModalDialogLayer;
import eu.scy.client.desktop.scydesktop.scywindows.window.ProgressOverlay;
import eu.scy.client.desktop.scydesktop.utils.ActivityTimer;
import eu.scy.client.desktop.scydesktop.draganddrop.impl.SimpleDragAndDropManager;
import eu.scy.client.desktop.scydesktop.uicontrols.MouseOverDisplay;
//import javax.swing.UIManager.LookAndFeelInfo;

/**
 * @author sikken
 */
// place your code here
public class Initializer {

   def logger = Logger.getLogger(this.getClass());
   public-read def launchTimer = new ActivityTimer("launching","startup");
   public-read def loadTimer = new ActivityTimer("loading");
   public-init var log4JInitFile = "";
   public-init var javaUtilLoggingInitFile = "";
   public-init var enableLocalLogging = true;
   public-init var loggingDirectoryName = "logging";
   public-init var redirectSystemStream = false;
   public-init var writeJavaLoggingToFile = true;
   public-init var lookAndFeel = "metal";
   public-init var loginType = "local";
   public-init var localPasswordCheckMethod = "same";
   public-init var localToolBrokerLoginConfigFile: String = "/config/localScyServices.xml";
   public-init var remoteToolBrokerLoginConfigFile: String = "/config/remoteScyServices.xml";
   public-init var defaultUserName: String;
   public-init var defaultPassword: String;
   public-init var autoLogin = false;
   public-init var scyDesktopConfigFile: String;
   public-init var storeElosOnDisk = true;
   public-init var createPersonalMissionMap = true;
   public-init var eloImagesPath = "{__DIR__}imagewindowstyler/images/";
   public-init var scyServerHost: String;
   public-init var scyServerPort: Integer = -1;
   public-init var useWebStartHost = true;
   public-init var windowPositioner = "functionalRole";
   public-init var debugMode = false;
   public-init var authorMode = false;
   public-init var indicateOnlineStateByOpacity = true;
   public-init var showEloRelations = true;
//   public-init var eloImagesPath = "http://www.scy-lab.eu/content/backgrounds/eloIcons/";
   public-init var showOfflineContacts = true;
   public-init var languageList = "en,nl,et,fr,el";
   public-init var missionMapSelectedImageScale = 1.5;
   public-init var missionMapNotSelectedImageScale = 1.0;
   public-init var missionMapPositionScale = 1.0;
   public-init var localUriReplacements = "";
   public-init var usingRooloCache = false;
   public-init var defaultMission = "";
   public-init var minimumRooloNewVersionListId = "";
   public-init var localAuthorRootPath = "";
   public-init var disableRooloVersioning = false;
   public-init var dontUseMissionRuntimeElos = false;
   public-init var useBigMissionMap = true;
   public-read var languages: String[];
   public-read var localLoggingDirectory: File = null;
   public-read var toolBrokerLogin: ToolBrokerLogin;
   public-read var usingWebStart = false;
   public-read var offlineMode = false;
   def systemOutFileName = "systemOut";
   def systemErrFileName = "systemErr";
   def enableLocalLoggingKey = "enableLocalLogging";
   def loggingDirectoryKey = "loggingDirectory";
   def storeElosOnDiskKey = "storeElosOnDisk";
    def scyServerPortKey = "httpPort";
   def scyServerNameKey = "serverName";
   def sqlspacesServerKey = "sqlspacesServer";
   def minimumRooloNewVersionListIdKey = "minimumRooloNewVersionListId";
   def disableRooloVersioningKey = "disableRooloVersioning";
   // parameter option names
   def log4JInitFileOption = "log4JInitFile";
   def enableLocalLoggingOption = "enableLocalLogging";
   def loggingDirectoryNameOption = "loggingDirectoryName";
   def redirectSystemStreamOption = "redirectSystemStream";
   def writeJavaLoggingToFileOption = "writeJavaLoggingToFile";
   def lookAndFeelOption = "lookAndFeel";
   def loginTypeOption = "loginType";
   def localPasswordCheckMethodOption = "localPasswordCheckMethod";
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
   def scyServerPortOption = "scyServerPort";
   def useWebStartHostOption = "useWebStartHost";
   def windowPositionerOption = "windowPositioner";
   def debugModeOption = "debugMode";
   def authorModeOption = "authorMode";
   def showOfflineContactsOption = "showOfflineContacts";
   def indicateOnlineStateByOpacityOption = "indicateOnlineStateByOpacity";
   def showEloRelationsOption = "showEloRelations";
   def languageListOption = "languageList";
   def missionMapSelectedImageScaleOption = "missionMapSelectedImageScale";
   def missionMapNotSelectedImageScaleOption = "missionMapNotSelectedImageScale";
   def missionMapPositionScaleOption = "missionMapPositionScale";
   def localUriReplacementsOption = "localUriReplacements";
   def usingRooloCacheOption = "usingRooloCache";
   def defaultMissionOption = "defaultMission";
   def minimumRooloNewVersionListIdOption = "minimumRooloNewVersionListId";
   def localAuthorRootPathOption = "localAuthorRootPath";
   def disableRooloVersioningOption = "disableRooloVersioning";
   def dontUseMissionRuntimeElosOption = "dontUseMissionRuntimeElos";
   def useBigMissionMapOption = "useBigMissionMap";
   var setupLoggingToFiles: SetupLoggingToFiles;
   package var background: DynamicTypeBackground;
   public-read var loginTypeEnum: LoginType;

   init {
      StringLocalizer.associate("languages.scydesktop", "eu.scy.client.desktop.scydesktop");

      Thread.setDefaultUncaughtExceptionHandler(new FilteringExceptionCatcher("SCY-Lab"));
      parseApplicationParameters();
      parseWebstartParameters();
      loginTypeEnum = LoginType.convertToLoginType(loginType);
      usingWebStart = System.getProperty("javawebstart.version") != null;
      offlineMode = loginType.toLowerCase().startsWith("local");
      setupLanguages();
      if (LoginType.LOCAL_MULTI_USER != loginTypeEnum) {
         setupLogging(null);
      }

//      System.setProperty(enableLocalLoggingKey, "{enableLocalLogging}");
//      var loggingDirectoryKeyValue = "";
//      if (enableLocalLogging) {
//         localLoggingDirectory = findLocalLoggingDirectory();
//         if (localLoggingDirectory != null) {
//            setupLoggingToFiles = new SetupLoggingToFiles(localLoggingDirectory);
//            if (redirectSystemStream) {
//               setupLoggingToFiles.redirectSystemStreams();
//            }
//            loggingDirectoryKeyValue = localLoggingDirectory.getAbsolutePath();
//         }
//      }
//      System.setProperty(loggingDirectoryKey, loggingDirectoryKeyValue);
//      System.setProperty(storeElosOnDiskKey, "{storeElosOnDisk}");
//      setupCodeLogging();
//      logProperties();
      setLocalUriReplacement();
      setLookAndFeel();
      setupScyServerHost();
      setupToolBrokerLogin();
   }

   function prepareLogging(userName: String) {
      System.setProperty(enableLocalLoggingKey, "{enableLocalLogging}");
      var loggingDirectoryKeyValue = "";
      if (enableLocalLogging) {
         localLoggingDirectory = findLocalLoggingDirectory(userName);
         if (localLoggingDirectory != null) {
            setupLoggingToFiles = new SetupLoggingToFiles(localLoggingDirectory);
            if (redirectSystemStream) {
               setupLoggingToFiles.redirectSystemStreams();
            }
            loggingDirectoryKeyValue = localLoggingDirectory.getAbsolutePath();
         }
      }
      System.setProperty(loggingDirectoryKey, loggingDirectoryKeyValue);
      System.setProperty(storeElosOnDiskKey, "{storeElosOnDisk}");
   }

   public function setupLogging(userName: String) {
      prepareLogging(userName);
      setupCodeLogging();
      logProperties();
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
            // 110112, temporary fix, because henrik uses a wrong option name
            if ("mission"==option){
               option = defaultMissionOption.toLowerCase();
            }

            if (option == log4JInitFileOption.toLowerCase()) {
               log4JInitFile = argumentsList.nextStringValue(log4JInitFileOption);
               logger.info("app: {log4JInitFileOption}: {log4JInitFile}");
            } else if (option == enableLocalLoggingOption.toLowerCase()) {
               enableLocalLogging = argumentsList.nextBooleanValue(enableLocalLoggingOption);
               logger.info("app: {enableLocalLoggingOption}: {enableLocalLogging}");
            } else if (option == loggingDirectoryNameOption.toLowerCase()) {
               loggingDirectoryName = argumentsList.nextStringValue(loggingDirectoryNameOption);
               logger.info("app: {loggingDirectoryNameOption}: {loggingDirectoryName}");
            } else if (option == redirectSystemStreamOption.toLowerCase()) {
               redirectSystemStream = argumentsList.nextBooleanValue(redirectSystemStreamOption);
               logger.info("app: {redirectSystemStreamOption}: {redirectSystemStream}");
            } else if (option == writeJavaLoggingToFileOption.toLowerCase()) {
               writeJavaLoggingToFile = argumentsList.nextBooleanValue(writeJavaLoggingToFileOption);
               logger.info("app: {writeJavaLoggingToFileOption}: {writeJavaLoggingToFile}");
            } else if (option == lookAndFeelOption.toLowerCase()) {
               lookAndFeel = argumentsList.nextStringValue(lookAndFeelOption);
               logger.info("app: {lookAndFeelOption}: {lookAndFeel}");
            } else if (option == loginTypeOption.toLowerCase()) {
               loginType = argumentsList.nextStringValue(loginTypeOption);
               logger.info("app: {loginTypeOption}: {loginType}");
            } else if (option == localPasswordCheckMethodOption.toLowerCase()) {
               localPasswordCheckMethod = argumentsList.nextStringValue(localPasswordCheckMethodOption);
               logger.info("app: {localPasswordCheckMethodOption}: {localPasswordCheckMethod}");
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
            } else if (option == scyServerPortOption.toLowerCase()) {
               scyServerPort = argumentsList.nextIntegerValue(scyServerPortOption);
               logger.info("app: {scyServerPortOption}: {scyServerPort}");
            } else if (option == useWebStartHostOption.toLowerCase()) {
               useWebStartHost = argumentsList.nextBooleanValue(useWebStartHostOption);
               logger.info("app: {useWebStartHostOption}: {useWebStartHost}");
            } else if (option == windowPositionerOption.toLowerCase()) {
               windowPositioner = argumentsList.nextStringValue(windowPositionerOption);
               logger.info("app: {windowPositionerOption}: {windowPositioner}");
            } else if (option == debugModeOption.toLowerCase()) {
               debugMode = argumentsList.nextBooleanValue(debugModeOption);
               logger.info("app: {debugModeOption}: {debugMode}");
            } else if (option == authorModeOption.toLowerCase()) {
               authorMode = argumentsList.nextBooleanValue(authorModeOption);
               logger.info("app: {authorModeOption}: {authorMode}");
            } else if (option == showOfflineContactsOption.toLowerCase()) {
               showOfflineContacts = argumentsList.nextBooleanValue(showOfflineContactsOption);
               logger.info("app: {showOfflineContactsOption}: {showOfflineContacts}");
            } else if (option == indicateOnlineStateByOpacityOption.toLowerCase()) {
               indicateOnlineStateByOpacity = argumentsList.nextBooleanValue(indicateOnlineStateByOpacityOption);
               logger.info("app: {indicateOnlineStateByOpacityOption}: {indicateOnlineStateByOpacity}");
            } else if (option == showEloRelationsOption.toLowerCase()) {
               showEloRelations = argumentsList.nextBooleanValue(showEloRelationsOption);
               logger.info("app: {showEloRelationsOption}: {showEloRelations}");
            } else if (option == languageListOption.toLowerCase()) {
               languageList = argumentsList.nextStringValue(languageListOption);
               logger.info("app: {languageListOption}: {languageList}");
            } else if (option == missionMapSelectedImageScaleOption.toLowerCase()) {
               missionMapSelectedImageScale = argumentsList.nextNumberValue(missionMapSelectedImageScaleOption);
               logger.info("app: {missionMapSelectedImageScaleOption}: {missionMapSelectedImageScale}");
            } else if (option == missionMapNotSelectedImageScaleOption.toLowerCase()) {
               missionMapNotSelectedImageScale = argumentsList.nextNumberValue(missionMapNotSelectedImageScaleOption);
               logger.info("app: {missionMapNotSelectedImageScaleOption}: {missionMapNotSelectedImageScale}");
            } else if (option == missionMapPositionScaleOption.toLowerCase()) {
               missionMapPositionScale = argumentsList.nextNumberValue(missionMapPositionScaleOption);
               logger.info("app: {missionMapPositionScaleOption}: {missionMapPositionScale}");
            } else if (option == localUriReplacementsOption.toLowerCase()) {
               localUriReplacements = argumentsList.nextStringValue(localUriReplacementsOption);
               logger.info("app: {localUriReplacementsOption}: {localUriReplacements}");
            } else if (option == usingRooloCacheOption.toLowerCase()) {
               usingRooloCache = argumentsList.nextBooleanValue(usingRooloCacheOption);
               logger.info("app: {usingRooloCacheOption}: {usingRooloCache}");
            } else if (option == defaultMissionOption.toLowerCase()) {
               defaultMission = argumentsList.nextStringValue(defaultMissionOption);
               logger.info("app: {defaultMissionOption}: {defaultMission}");
            } else if (option == minimumRooloNewVersionListIdOption.toLowerCase()) {
               minimumRooloNewVersionListId = "{argumentsList.nextIntegerValue(minimumRooloNewVersionListIdOption)}";
               logger.info("app: {minimumRooloNewVersionListIdOption}: {minimumRooloNewVersionListId}");
            } else if (option == localAuthorRootPathOption.toLowerCase()) {
               localAuthorRootPath = argumentsList.nextStringValue(localAuthorRootPathOption);
               logger.info("app: {localAuthorRootPath}: {localAuthorRootPath}");
            } else if (option == disableRooloVersioningOption.toLowerCase()) {
               disableRooloVersioning = argumentsList.nextBooleanValue(disableRooloVersioningOption);
               logger.info("app: {disableRooloVersioning}: {disableRooloVersioning}");
            } else if (option == dontUseMissionRuntimeElosOption.toLowerCase()) {
               dontUseMissionRuntimeElos = argumentsList.nextBooleanValue(dontUseMissionRuntimeElosOption);
               logger.info("app: {dontUseMissionRuntimeElos}: {dontUseMissionRuntimeElos}");
            } else if (option == useBigMissionMapOption.toLowerCase()) {
               useBigMissionMap = argumentsList.nextBooleanValue(useBigMissionMapOption);
               logger.info("app: {useBigMissionMap}: {useBigMissionMap}");
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
      enableLocalLogging = getWebstartParameterBooleanValue(enableLocalLoggingOption, enableLocalLogging);
      loggingDirectoryName = getWebstartParameterStringValue(loggingDirectoryNameOption, loggingDirectoryName);
      redirectSystemStream = getWebstartParameterBooleanValue(redirectSystemStreamOption, redirectSystemStream);
      writeJavaLoggingToFile = getWebstartParameterBooleanValue(writeJavaLoggingToFileOption, writeJavaLoggingToFile);
      lookAndFeel = getWebstartParameterStringValue(lookAndFeelOption, lookAndFeel);
      loginType = getWebstartParameterStringValue(loginTypeOption, loginType);
      localPasswordCheckMethod = getWebstartParameterStringValue(localPasswordCheckMethodOption, localPasswordCheckMethod);
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
      scyServerPort = getWebstartParameterIntegerValue(scyServerPortOption, scyServerPort);
      useWebStartHost = getWebstartParameterBooleanValue(useWebStartHostOption, useWebStartHost);
      windowPositioner = getWebstartParameterStringValue(windowPositionerOption, windowPositioner);
      debugMode = getWebstartParameterBooleanValue(debugModeOption, debugMode);
      authorMode = getWebstartParameterBooleanValue(authorModeOption, authorMode);
      showOfflineContacts = getWebstartParameterBooleanValue(showOfflineContactsOption, showOfflineContacts);
      indicateOnlineStateByOpacity = getWebstartParameterBooleanValue(indicateOnlineStateByOpacityOption, indicateOnlineStateByOpacity);
      showEloRelations = getWebstartParameterBooleanValue(showEloRelationsOption, showEloRelations);
      languageList = getWebstartParameterStringValue(languageListOption, languageList);
      missionMapSelectedImageScale = getWebstartParameterNumberValue(missionMapSelectedImageScaleOption, missionMapSelectedImageScale);
      missionMapNotSelectedImageScale = getWebstartParameterNumberValue(missionMapNotSelectedImageScaleOption, missionMapNotSelectedImageScale);
      missionMapPositionScale = getWebstartParameterNumberValue(missionMapPositionScaleOption, missionMapPositionScale);
      localUriReplacements = getWebstartParameterStringValue(localUriReplacementsOption, localUriReplacements);
      usingRooloCache = getWebstartParameterBooleanValue(usingRooloCacheOption, usingRooloCache);
      defaultMission = getWebstartParameterStringValue(defaultMissionOption, defaultMission);
      minimumRooloNewVersionListId = getWebstartParameterIntegerValueAsString(minimumRooloNewVersionListIdOption, minimumRooloNewVersionListId);
      localAuthorRootPath = getWebstartParameterStringValue(localAuthorRootPathOption, localAuthorRootPath);
      disableRooloVersioning = getWebstartParameterBooleanValue(disableRooloVersioningOption, disableRooloVersioning);
      dontUseMissionRuntimeElos = getWebstartParameterBooleanValue(dontUseMissionRuntimeElosOption, dontUseMissionRuntimeElos);
      useBigMissionMap = getWebstartParameterBooleanValue(useBigMissionMapOption, useBigMissionMap);
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

   function getWebstartParameterNumberValue(name: String, default: Number): Number {
      var webstartValue = FX.getArgument(name) as String;
      if (isEmpty(webstartValue)) {
         return default;
      }
      var numberValue = Float.parseFloat(webstartValue);
      logger.info("ws: {name}: {numberValue}");
      return numberValue;
   }

   function getWebstartParameterIntegerValue(name: String, default: Integer): Integer {
      var webstartValue = FX.getArgument(name) as String;
      if (isEmpty(webstartValue)) {
         return default;
      }
      var integerValue = Integer.parseInt(webstartValue);
      logger.info("ws: {name}: {integerValue}");
      return integerValue;
   }

   function getWebstartParameterIntegerValueAsString(name: String, default: String): String {
      var webstartValue = FX.getArgument(name) as String;
      if (isEmpty(webstartValue)) {
         return default;
      }
      var integerValue = Integer.parseInt(webstartValue);
      logger.info("ws: {name}: {integerValue}");
      return "{integerValue}";
   }

   function printInitializerValues(printWriter: PrintWriter) {
      printWriter.println("Initializer properties:");
      printWriter.println("- log4JInitFile: {log4JInitFile}");
      printWriter.println("- javaUtilLoggingInitFile: {javaUtilLoggingInitFile}");
      printWriter.println("- enableLocalLogging: {enableLocalLogging}");
      printWriter.println("- loggingDirectoryName: {loggingDirectoryName}");
      printWriter.println("- redirectSystemStream: {redirectSystemStream}");
      printWriter.println("- writeJavaLoggingToFile: {writeJavaLoggingToFile}");
      printWriter.println("- lookAndFeel: {lookAndFeel}");
      printWriter.println("- loginType: {loginTypeEnum}");
      printWriter.println("- localPasswordCheckMethod: {localPasswordCheckMethod}");
      printWriter.println("- localToolBrokerLoginConfigFile: {localToolBrokerLoginConfigFile}");
      printWriter.println("- remoteToolBrokerLoginConfigFile: {remoteToolBrokerLoginConfigFile}");
      printWriter.println("- defaultUserName: {defaultUserName}");
      printWriter.println("- defaultPassword: {defaultPassword}");
      printWriter.println("- autoLogin: {autoLogin}");
      printWriter.println("- defaultMission: {defaultMission}");
      printWriter.println("- scyDesktopConfigFile: {scyDesktopConfigFile}");
      printWriter.println("- storeElosOnDisk: {storeElosOnDisk}");
      printWriter.println("- createPersonalMissionMap: {createPersonalMissionMap}");
      printWriter.println("- eloImagesPath: {eloImagesPath}");
      printWriter.println("- scyServerHost: {scyServerHost}");
      printWriter.println("- scyServerPort: {scyServerPort}");
      printWriter.println("- useWebStartHost: {useWebStartHost}");
      printWriter.println("- windowPositioner: {windowPositioner}");
      printWriter.println("- debugMode: {debugMode}");
      printWriter.println("- authorMode: {authorMode}");
      printWriter.println("- usingWebStart: {usingWebStart}");
      printWriter.println("- offlineMode: {offlineMode}");
      printWriter.println("- showOfflineContacts: {showOfflineContacts}");
      printWriter.println("- indicateOnlineStateByOpacity: {indicateOnlineStateByOpacity}");
      printWriter.println("- showEloRelations: {showEloRelations}");
      printWriter.println("- languages: {for (lan in languages) '{lan},'}");
      printWriter.println("- missionMapSelectedImageScale: {missionMapSelectedImageScale}");
      printWriter.println("- missionMapNotSelectedImageScale: {missionMapNotSelectedImageScale}");
      printWriter.println("- missionMapPositionScale: {missionMapPositionScale}");
      printWriter.println("- localUriReplacements: {localUriReplacements}");
      printWriter.println("- usingRooloCache: {usingRooloCache}");
      printWriter.println("- minimumRooloNewVersionListId: {minimumRooloNewVersionListId}");
      printWriter.println("- localAuthorRootPath: {localAuthorRootPath}");
      printWriter.println("- disableRooloVersioning: {disableRooloVersioning}");
      printWriter.println("- dontUseMissionRuntimeElos: {dontUseMissionRuntimeElos}");
      printWriter.println("- useBigMissionMap: {useBigMissionMap}");
   }

   public function isEmpty(string: String): Boolean {
      return string == null or string.length() == 0;
   }

   public function getScene(createScyDesktop: function(missionRunConfigs: MissionRunConfigs): ScyDesktop): Scene {
      var scene = Scene {
         };
      background = DynamicTypeBackground {
         };
      // add all component groups to the scene, as adding them dynamicly later might case problems
      scene.content = [
            background,
            LoginDialog {
               createScyDesktop: createScyDesktop
               initializer: this;
            }
            ScyDesktop.scyDektopGroup,
            ModalDialogLayer.layer,
            MouseBlocker.mouseBlockNode,
            MouseOverDisplay.mouseOverGroup,
            SimpleTooltipManager.tooltipGroup,
            SimpleDragAndDropManager.dragAndDropLayer,
            ProgressOverlay.showProgressNode
         ];
//      scene.stylesheets = "{__DIR__}css/scy-desktop.css";
      scene
   }

   function setupLanguages(): Void {
      var tokenizer = new StringTokenizer(languageList, ", ");
      while (tokenizer.hasMoreTokens()) {
         var language = tokenizer.nextToken();
         if (language != "" and Sequences.indexOf(languages, language) < 0) {
            insert language into languages;
         }
      }
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
      println("writeJavaLoggingToFile: {writeJavaLoggingToFile}");
      if (writeJavaLoggingToFile) {
         println("setupLoggingToFiles.setupJavaUtilLogFile(): {setupLoggingToFiles}");
         setupLoggingToFiles.setupJavaUtilLogFile();
         setupLoggingToFiles.setuplog4JLogFile();
      }

   }

   function findLocalLoggingDirectory(userName: String): File {
      try {
         var logDirectory: File;
         if (loggingDirectoryName.length() > 0) {
            if (usingWebStart) {
               var userHome = System.getProperty("user.home");
               logDirectory = new File(userHome, "SCY-Lab/{loggingDirectoryName}");
               if (not logDirectory.mkdirs()) {
                  println("failed to create the web start log directory: {logDirectory.getAbsolutePath()}");
               } else {
                  println("created the web start log directory: {logDirectory.getAbsolutePath()}");
               }
            }
            if (logDirectory == null) {
               if (loginTypeEnum.LOCAL_MULTI_USER == loginTypeEnum) {
                  def usersDirectory = new File("store/users");
                  if (usersDirectory.isDirectory()) {

                     def globalUserDirectory = new File(usersDirectory, if (userName != null) userName else "_global_");
                     if (not globalUserDirectory.isDirectory()) {
                        globalUserDirectory.mkdir();
                     }
                     logDirectory = new File(globalUserDirectory, loggingDirectoryName);
                     if (not logDirectory.isDirectory()) {
                        logDirectory.mkdir();
                     }
                  } else {
                     throw new IllegalArgumentException("users directory does not exists: {usersDirectory.getAbsolutePath()}");
                  }
               } else {
                  logDirectory = new File(loggingDirectoryName);
               }
            }
            if (not logDirectory.exists()) {
               throw new IllegalArgumentException("logging directory does not exists: {logDirectory.getAbsolutePath()}");
            }
            if (not logDirectory.isDirectory()) {
               throw new IllegalArgumentException("logging directory does not a directory: {logDirectory.getAbsolutePath()}");
            }
         }
         println("logDirectory: {logDirectory.getAbsolutePath()}");
         return logDirectory;
      }
      catch (e: Exception) {
         JOptionPane.showMessageDialog(null, "An exception occured during finding the logging directory. ""No logging will be written to the local disk.\n\nException: {e.getMessage()}",
         "Problems with logging directory", JOptionPane.ERROR_MESSAGE);
      }
      return null;
   }

   function doRedirectSystemStream() {
      System.setOut(createPrintStream(systemOutFileName));
      System.setErr(createPrintStream(systemErrFileName));
   }

   function createPrintStream(fileName: String): PrintStream// throws IOException
   {
      var outputStream = new FileOutputStream(RedirectSystemStreams.getLogFile(localLoggingDirectory, fileName, ".txt"), false);
      return new PrintStream(outputStream, true);
   }

   function logProperties(): Void {
      var stringWriter = new StringWriter();
      var printWriter = new PrintWriter(stringWriter);
      printWriter.println("\nStarting SCY-Lab on {new Date()}\n");
      printWriter.println("Client IP address: {InetAddress.getLocalHost().getHostAddress()}\n");
      JavaProperties.writePropertiesForApplication(printWriter);
      JavaProperties.writeApplicationParameters(FX.getArguments(), printWriter);
      printInitializerValues(printWriter);
      logger.info("System information:\n{stringWriter.toString()}");
   }

   function setLocalUriReplacement() {
      UriLocalizer.localUriReplacements = UriLocalizer.createLocalUriReplacementFromString(localUriReplacements);
   }

   function setLookAndFeel() {
      if (lookAndFeel.length() > 0) {
         var lookAndFeelClassName = findLookAndFeelClassName();
         if (lookAndFeelClassName != null) {
            try {
               UIManager.setLookAndFeel(lookAndFeelClassName);
               logger.info("set lookAndFeel to {lookAndFeel}, class {lookAndFeelClassName}");
            }
            catch (e: Exception) {
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

   function setupScyServerHost() {
      var newScyServerHost: String;
      var newScyServerPort: Integer = -1;
      if (useWebStartHost){
         try {
            var basicService = ServiceManager.lookup("javax.jnlp.BasicService") as javax.jnlp.BasicService;
            var webstartServiceNames = javax.jnlp.ServiceManager.getServiceNames();
            logger.info("Available web start services: {webstartServiceNames}");
            if (basicService != null) {
               var codeBase = basicService.getCodeBase();
               logger.info("webstart codeBase: {codeBase}");
               newScyServerHost = codeBase.getHost();
               newScyServerPort = codeBase.getPort();
               if (newScyServerPort<0){
                  newScyServerPort = 80;
               }
               logger.info("newScyServerHost: {newScyServerHost}, newScyServerPort: {newScyServerPort}");
            }
         }
         catch (e: javax.jnlp.UnavailableServiceException) {
            logger.info("cannot get scy server host from web start, as web start is not being used: {e}");
         }
      }
      if (scyServerHost.length() > 0) {
         newScyServerHost = scyServerHost;
      }
      if (scyServerPort > 0) {
         newScyServerPort = scyServerPort;
      }
      if (newScyServerHost.length() > 0) {
         logger.info("setting scy server host to {newScyServerHost}");
         System.setProperty(scyServerNameKey, newScyServerHost);
         System.setProperty(sqlspacesServerKey, newScyServerHost);
         Configuration.getInstance().setScyServerHost(newScyServerHost);
      }
      if (newScyServerPort>0) {
         logger.info("setting scy server port to {newScyServerPort}");
         System.setProperty(scyServerPortKey, "{newScyServerPort}");
         Configuration.getInstance().setScyServerPort(newScyServerPort);
      }
   }

   function setupToolBrokerLogin() {
      if (LoginType.LOCAL == loginTypeEnum) {
         System.setProperty(minimumRooloNewVersionListIdKey, minimumRooloNewVersionListId);
         System.setProperty(disableRooloVersioningKey, Boolean.toString(disableRooloVersioning));
         var localToolBrokerLogin = new LocalToolBrokerLogin();
         if (localAuthorRootPath!=""){
            def localAuthorRoot = new File(localAuthorRootPath);
            localToolBrokerLogin = new LocalToolBrokerLogin(localAuthorRoot);
            // set the default directory, where to look for the mission model specification
            def missionSpecificationDir = new File(localAuthorRoot.getParentFile(),"missionModels");
            if (missionSpecificationDir.isDirectory()){
               EloXmlEditor.lastUsedDirectory = missionSpecificationDir;
            }
            else{
               EloXmlEditor.lastUsedDirectory = localAuthorRoot;
            }
         }
         localToolBrokerLogin.setPasswordChecker(localPasswordCheckMethod);
         localToolBrokerLogin.setSpringConfigFile(localToolBrokerLoginConfigFile);
         toolBrokerLogin = localToolBrokerLogin;
      } else if (loginTypeEnum.LOCAL_MULTI_USER == loginTypeEnum) {
         System.setProperty(minimumRooloNewVersionListIdKey, minimumRooloNewVersionListId);
         System.setProperty(disableRooloVersioningKey, Boolean.toString(disableRooloVersioning));
         def localMultiUserToolBrokerLogin = new LocalMultiUserToolBrokerLogin();
         localMultiUserToolBrokerLogin.setPasswordChecker(localPasswordCheckMethod);
         localMultiUserToolBrokerLogin.setSpringConfigFile(localToolBrokerLoginConfigFile);
         toolBrokerLogin = localMultiUserToolBrokerLogin;
      } else if (loginTypeEnum.REMOTE == loginTypeEnum) {
         def remoteToolBrokerLogin = new RemoteToolBrokerLogin();
         remoteToolBrokerLogin.setSpringConfigFile(remoteToolBrokerLoginConfigFile);
         toolBrokerLogin = remoteToolBrokerLogin;
      } else {
         throw new IllegalArgumentException("unknown login type: {loginType}");
      }
      toolBrokerLogin.prepare();
   }

}

/*
 * Initializer.fx
 *
 * Created on 9-dec-2009, 10:28:50
 */
package eu.scy.client.desktop.scydesktop;

import javafx.scene.image.Image;
import javafx.scene.Node;
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
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import java.util.StringTokenizer;
import javafx.util.Sequences;
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
    public-init var writeJavaLoggingToFile = true;
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
    public-init var scyServerHost: String;
    public-init var useWebStartHost = true;
    public-init var windowPositioner = "roleArea";
    public-init var debugMode = false;
    public-init var authorMode = false;
    public-init var indicateOnlineStateByOpacity = true;
    public-init var showEloRelations = true;
//   public-init var eloImagesPath = "http://www.scy-lab.eu/content/backgrounds/eloIcons/";
    public-init var showOfflineContacts = true;
    public-init var languageList = "nl,en,et,fr,el";
    public-read var languages:String[];
    public-read var backgroundImage: Image;
    public-read var localLoggingDirectory: File = null;
    public-read var toolBrokerLogin: ToolBrokerLogin;
    public-read var usingWebStart = false;
    public-read var offlineMode = false;
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
    def writeJavaLoggingToFileOption = "writeJavaLoggingToFile";
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
    def windowPositionerOption = "windowPositioner";
    def debugModeOption = "debugMode";
    def authorModeOption = "authorMode";
    def showOfflineContactsOption = "showOfflineContacts";
    def indicateOnlineStateByOpacityOption = "indicateOnlineStateByOpacity";
    def showEloRelationsOption = "showEloRelations";
    def languageListOption = "languageList";

    var setupLoggingToFiles:SetupLoggingToFiles;
    package var background:DynamicTypeBackground;

    init {
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionCatcher("SCY-Lab"));
        parseApplicationParameters();
        parseWebstartParameters();
        setupBackgroundImage();
        usingWebStart = System.getProperty("javawebstart.version") != null;
        offlineMode = loginType == "local";
        System.setProperty(enableLocalLoggingKey, "{enableLocalLogging}");
        var loggingDirectoryKeyValue = "";
        if (enableLocalLogging) {
            localLoggingDirectory = findLocalLoggingDirectory();
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
        setupLanguages();
        setupCodeLogging();
        logProperties();
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
                } else if (option == writeJavaLoggingToFileOption.toLowerCase()) {
                    writeJavaLoggingToFile = argumentsList.nextBooleanValue(writeJavaLoggingToFileOption);
                    logger.info("app: {writeJavaLoggingToFileOption}: {writeJavaLoggingToFile}");
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
        writeJavaLoggingToFile = getWebstartParameterBooleanValue(writeJavaLoggingToFileOption, writeJavaLoggingToFile);
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
        windowPositioner = getWebstartParameterStringValue(windowPositionerOption, windowPositioner);
        debugMode = getWebstartParameterBooleanValue(debugModeOption, debugMode);
        authorMode = getWebstartParameterBooleanValue(authorModeOption, authorMode);
        showOfflineContacts = getWebstartParameterBooleanValue(showOfflineContactsOption, showOfflineContacts);
        indicateOnlineStateByOpacity = getWebstartParameterBooleanValue(indicateOnlineStateByOpacityOption, indicateOnlineStateByOpacity);
        showEloRelations = getWebstartParameterBooleanValue(showEloRelationsOption, showEloRelations);
        languageList = getWebstartParameterStringValue(languageListOption, languageList);

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

    function printInitializerValues(printWriter:PrintWriter) {
        printWriter.println("Initializer properties:");
        printWriter.println("- log4JInitFile: {log4JInitFile}");
        printWriter.println("- javaUtilLoggingInitFile: {javaUtilLoggingInitFile}");
        printWriter.println("- backgroundImageUrl: {backgroundImageUrl}");
        printWriter.println("- enableLocalLogging: {enableLocalLogging}");
        printWriter.println("- loggingDirectoryName: {loggingDirectoryName}");
        printWriter.println("- redirectSystemStream: {redirectSystemStream}");
        printWriter.println("- writeJavaLoggingToFile: {writeJavaLoggingToFile}");
        printWriter.println("- lookAndFeel: {lookAndFeel}");
        printWriter.println("- loginType: {loginType}");
        printWriter.println("- localToolBrokerLoginConfigFile: {localToolBrokerLoginConfigFile}");
        printWriter.println("- remoteToolBrokerLoginConfigFile: {remoteToolBrokerLoginConfigFile}");
        printWriter.println("- defaultUserName: {defaultUserName}");
        printWriter.println("- defaultPassword: {defaultPassword}");
        printWriter.println("- autoLogin: {autoLogin}");
        printWriter.println("- scyDesktopConfigFile: {scyDesktopConfigFile}");
        printWriter.println("- storeElosOnDisk: {storeElosOnDisk}");
        printWriter.println("- createPersonalMissionMap: {createPersonalMissionMap}");
        printWriter.println("- eloImagesPath: {eloImagesPath}");
        printWriter.println("- scyServerHost: {scyServerHost}");
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

    public function getBackground(scene: Scene): Node {
        if (backgroundImage != null) {
           println("scene: {scene}");
           Background{
              defaultBackgroundImage:backgroundImage;
              displayWith:bind scene.width;
              displayHeight:bind scene.height;
              useScene:scene;
           }
        }
        return null;
    }

    public function getScene(createScyDesktop: function( tbi:  ToolBrokerAPI,userName: String): ScyDesktop):Scene{
       var scene = Scene{

       };
       background = DynamicTypeBackground{

       };

      scene.content=[
            background,
            LoginDialog {
              createScyDesktop: createScyDesktop
              initializer:this;
            }
         ];
       scene
    }

    function setupLanguages(): Void{
      var tokenizer = new StringTokenizer(languageList,", ");
      while (tokenizer.hasMoreTokens()){
         var language = tokenizer.nextToken();
         if (language!="" and Sequences.indexOf(languages,language)<0){
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
        if (writeJavaLoggingToFile){
           println("setupLoggingToFiles.setupJavaUtilLogFile(): {setupLoggingToFiles}");
           setupLoggingToFiles.setupJavaUtilLogFile();
           setupLoggingToFiles.setuplog4JLogFile();
        }

    }

    function setupBackgroundImage() {
        if (backgroundImageUrl.length() > 0) {
            logger.info("loading background image: {backgroundImageUrl}");
            backgroundImage = Image {
                url: backgroundImageUrl
            }
            logger.info("background image, error: {backgroundImage.error}, progress: {backgroundImage.progress}");
        } else {
            logger.info("no background image specified");
        }
    }

    function findLocalLoggingDirectory(): File {
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
                    logDirectory = new File(loggingDirectoryName);
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
        } catch (e: Exception) {
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
        //      var fileCount = 0;
        //      var streamFile = new File(loggingDirectory, "{fileName}_{fileCount}.txt");
        //      while (streamFile.exists()) {
        //         ++fileCount;
        //         streamFile = new File(loggingDirectory, "{fileName}_{fileCount}.txt");
        //      }
        var outputStream = new FileOutputStream(RedirectSystemStreams.getLogFile(localLoggingDirectory, fileName, ".txt"), false);
        return new PrintStream(outputStream, true);
    }

    function logProperties():Void{
      var stringWriter = new StringWriter();
      var printWriter = new PrintWriter(stringWriter);
      printWriter.println("\nStarting SCY-Lab on {new Date()}\n");
      printWriter.println("Client IP address: {InetAddress.getLocalHost().getHostAddress()}\n");
      JavaProperties.writePropertiesForApplication(printWriter);
      JavaProperties.writeApplicationParameters(FX.getArguments(),printWriter);
      printInitializerValues(printWriter);
      logger.info("System information:\n{stringWriter.toString()}");
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

    function setupScyServerHost() {
        var newScyServerHost: String;
        if (scyServerHost.length() > 0) {
            newScyServerHost = scyServerHost;
        } else if (useWebStartHost) {
            try {
                var webstartServiceNames = javax.jnlp.ServiceManager.getServiceNames();
                logger.info("Available web start services: {webstartServiceNames}");
                var basicService = ServiceManager.lookup("javax.jnlp.BasicService") as javax.jnlp.BasicService;
                if (basicService != null) {
                    var codeBase = basicService.getCodeBase();
                    logger.info("webstart codeBase: {codeBase}");
                    newScyServerHost = codeBase.getHost();
                }
            } catch (e: javax.jnlp.UnavailableServiceException) {
                logger.info("cannot get scy server host from web start, as web start is not being used: {e}");
            }
        }
        if (newScyServerHost.length() > 0) {
            logger.info("setting scy server host to {newScyServerHost}");
            System.setProperty(scyServerNameKey, newScyServerHost);
            System.setProperty(sqlspacesServerKey, newScyServerHost);
            Configuration.getInstance().setScyServerHost(newScyServerHost);
        }
    }

    function setupToolBrokerLogin() {
        if ("local".equalsIgnoreCase(loginType)) {
            var localToolBrokerLogin = new LocalToolBrokerLogin();
            localToolBrokerLogin.setSpringConfigFile(localToolBrokerLoginConfigFile);
            toolBrokerLogin = localToolBrokerLogin;
        } else if ("remote".equalsIgnoreCase(loginType)) {
            var remoteToolBrokerLogin = new RemoteToolBrokerLogin();
            remoteToolBrokerLogin.setSpringConfigFile(remoteToolBrokerLoginConfigFile);
            toolBrokerLogin = remoteToolBrokerLogin;
        } else {
            throw new IllegalArgumentException("unknown login type: {loginType}");
        }
    }

}

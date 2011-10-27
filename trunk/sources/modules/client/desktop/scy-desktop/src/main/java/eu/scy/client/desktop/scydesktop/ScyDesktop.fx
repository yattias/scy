/*
 * ScyDesktop.fx
 *
 * Created on 26-jun-2009, 12:15:46
 */
package eu.scy.client.desktop.scydesktop;

import eu.scy.client.desktop.scydesktop.scywindows.WindowManager;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindowControl;
import eu.scy.client.desktop.scydesktop.scywindows.WindowStyler;
import eu.scy.client.desktop.scydesktop.tools.corner.missionmap.MissionModelFX;
import eu.scy.client.desktop.scydesktop.corners.Corner;
import eu.scy.client.desktop.scydesktop.config.Config;
import eu.scy.client.desktop.scydesktop.elofactory.NewEloCreationRegistry;
import eu.scy.client.desktop.scydesktop.elofactory.DrawerContentCreatorRegistryFX;
import eu.scy.client.desktop.scydesktop.scywindows.window.MouseBlocker;
import eu.scy.client.desktop.scydesktop.scywindows.EloSavedActionHandler;
import javafx.scene.Node;
import java.lang.Object;
import eu.scy.client.desktop.desktoputils.RedirectSystemStreams;
import eu.scy.client.desktop.scydesktop.scywindows.WindowPositioner;
import eu.scy.client.desktop.scydesktop.scywindows.window_positions.SimpleWindowPositioner;
import eu.scy.client.desktop.desktoputils.log4j.Logger;
import eu.scy.client.desktop.scydesktop.tooltips.impl.SimpleTooltipManager;
import eu.scy.client.desktop.scydesktop.scywindows.NewTitleGenerator;
import eu.scy.client.desktop.scydesktop.draganddrop.DragAndDropManager;
import eu.scy.client.desktop.scydesktop.draganddrop.impl.SimpleDragAndDropManager;
import eu.scy.client.desktop.scydesktop.tooltips.TooltipManager;
import eu.scy.client.desktop.scydesktop.elofactory.impl.ScyToolFactory;
import eu.scy.client.desktop.scydesktop.elofactory.ScyToolCreatorRegistryFX;
import eu.scy.notification.api.INotifiable;
import eu.scy.client.desktop.scydesktop.scywindows.window_positions.RoleAreaWindowPositioner;
import javafx.lang.FX;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import eu.scy.client.desktop.scydesktop.config.SpringConfigFactory;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import eu.scy.client.desktop.scydesktop.scywindows.window.ScyToolsList;
import eu.scy.client.desktop.scydesktop.scywindows.window.StandardScyWindow;
import eu.scy.client.desktop.desktoputils.log4j.InitLog4JFX;
import java.lang.IllegalStateException;
import eu.scy.client.desktop.scydesktop.elofactory.EloConfigManager;
import javafx.scene.Group;
import eu.scy.client.desktop.scydesktop.edges.EdgesManager;
import eu.scy.client.desktop.scydesktop.hacks.RepositoryWrapper;
import eu.scy.collaboration.api.CollaborationStartable;
import java.lang.Void;
import eu.scy.client.desktop.scydesktop.edges.IEdgesManager;
import eu.scy.client.desktop.scydesktop.scywindows.scydesktop.SimpleMyEloChanged;
import eu.scy.client.desktop.scydesktop.scywindows.scydesktop.SimpleScyDesktopEloSaver;
import eu.scy.client.desktop.scydesktop.scywindows.scydesktop.WindowManagerImpl;
import javafx.scene.effect.Effect;
import java.lang.System;
import eu.scy.client.desktop.scydesktop.remotecontrol.RemoteCommandRegistryFX;
import javafx.scene.paint.Color;
import eu.scy.client.desktop.scydesktop.corners.BottomLeftCorner;
import eu.scy.client.desktop.scydesktop.corners.BottomRightCorner;
import eu.scy.client.desktop.scydesktop.corners.TopLeftCorner;
import eu.scy.client.desktop.scydesktop.corners.TopRightCorner;
import eu.scy.client.desktop.scydesktop.scywindows.scydesktop.NumberedNewTitleGenerator;
import eu.scy.client.desktop.scydesktop.scywindows.scydesktop.ScyWindowControlImpl;
import eu.scy.client.desktop.scydesktop.tools.corner.contactlist.ContactList;
import eu.scy.client.desktop.scydesktop.tools.corner.missionmap.MissionMap;
import eu.scy.common.mission.impl.jdom.JDomStringConversion;
import eu.scy.notification.api.INotification;
import java.io.Closeable;
import java.lang.IllegalArgumentException;
import java.lang.Integer;
import java.lang.String;
import java.lang.Exception;
import java.net.URI;
import eu.scy.common.scyelo.ScyRooloMetadataKeyIds;
import eu.scy.client.desktop.scydesktop.mission.MissionRunConfigs;
import eu.scy.client.desktop.scydesktop.scywindows.scydesktop.EloRuntimeSettingsRetriever;
import eu.scy.common.scyelo.EloFunctionalRole;
import eu.scy.common.mission.RuntimeSettingsManager;
import java.util.StringTokenizer;
import eu.scy.client.desktop.scydesktop.scywindows.moreinfomanager.MoreInfoManagerImpl;
import eu.scy.client.desktop.scydesktop.scywindows.moreinfomanager.TestMoreInfoToolFactory;
import eu.scy.client.desktop.scydesktop.scywindows.MoreInfoToolFactory;
import eu.scy.client.desktop.scydesktop.tools.corner.missionmap.BigMissionMap;
import eu.scy.client.desktop.scydesktop.tools.corner.missionmap.BigMissionMapControl;
import eu.scy.client.desktop.scydesktop.scywindows.window_positions.FunctionalRoleWindowPositioner;
import eu.scy.client.desktop.desktoputils.ShutdownHook;
import eu.scy.client.desktop.desktoputils.OSXAdapter;
import eu.scy.client.desktop.desktoputils.BareBonesBrowserLaunch;
import eu.scy.common.configuration.Configuration;
import eu.scy.client.desktop.scydesktop.scywindows.window.ProgressOverlay;
import java.net.URLEncoder;
import eu.scy.client.desktop.desktoputils.XFX;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import eu.scy.client.desktop.scydesktop.scywindows.scydesktop.DialogBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import roolo.elo.api.IMetadata;
import roolo.elo.api.IMetadataValueContainer;
import eu.scy.common.mission.impl.BasicEloToolConfig;
import eu.scy.client.desktop.scydesktop.uicontrols.EloIconButton;
import eu.scy.actionlogging.api.IAction;
import eu.scy.actionlogging.Action;
import eu.scy.actionlogging.api.ContextConstants;
import eu.scy.client.desktop.scydesktop.corners.assessment.EportfolioButton;
import eu.scy.client.desktop.scydesktop.corners.elomanagement.EloManagement;
import java.lang.Thread;
import eu.scy.client.desktop.desktoputils.art.javafx.LogoEloIcon;
import eu.scy.client.desktop.scydesktop.scywindows.scydesktop.DialogType;
import eu.scy.client.desktop.scydesktop.tooltips.BubbleManager;
import eu.scy.client.desktop.scydesktop.tooltips.impl.SimpleBubbleManager;
import eu.scy.client.desktop.scydesktop.scywindows.scydesktop.ModalDialogBox;

/**
 * @author sikkenj
 */
public def scyDektopGroup = Group {
           visible: true
        }

public class ScyDesktop extends /*CustomNode,*/ INotifiable {

   def logger = Logger.getLogger(this.getClass());
   public var initializer: Initializer;
   public-init var missionRunConfigs: MissionRunConfigs;
   public var config: Config;
   public def scene = scyDektopGroup.scene;
   public var missionModelFX: MissionModelFX = MissionModelFX {};
   public var windowStyler: WindowStyler;
   public var scyToolCreatorRegistryFX: ScyToolCreatorRegistryFX;
   public var newEloCreationRegistry: NewEloCreationRegistry;
   public var drawerContentCreatorRegistryFX: DrawerContentCreatorRegistryFX;
   public var eloConfigManager: EloConfigManager;
   public var templateEloUris: URI[];
   public var topLeftCornerTool: Node on replace {
              topLeftCorner.content = topLeftCornerTool
           };
   public var topRightCornerTool: Node on replace {
              topRightCorner.content = topRightCornerTool
           };
   public var bottomRightCornerTool: Node; // TODO, still hard coded to missionMap
   public var bottomLeftCornerTool: Node on replace {
              bottomLeftCorner.content = bottomLeftCornerTool
           };
   public-init var edgesManager: IEdgesManager = EdgesManager {
              repository: config.getRepository();
              metadataTypeManager: config.getMetadataTypeManager();
              windowManager: bind windows;
              showEloRelations: initializer.showEloRelations
           };
   public def remoteCommandRegistryFX: RemoteCommandRegistryFX = RemoteCommandRegistryFX {
              scyDesktop: this
           };
   public def windows: WindowManager = WindowManagerImpl {
              scyDesktop: this
           //       activeAnchor: bind missionModelFX.activeAnchor;
           };
   public var fullscreenWindow: ScyWindow = bind windows.fullscreenWindow on replace {
              if (fullscreenWindow != null) {
                 FX.deferAction(function() {
                    def tempEdgesManager = edgesManager;
                    def tempTopLeftCorner = topLeftCorner;
                    def tempTopRightCorner = topRightCorner;
                    def tempBottomRightCorner = bottomRightCorner;
                    def tempBottomLeftCorner = bottomLeftCorner;
                    Timeline {
                       keyFrames: [KeyFrame {
                             time: 300ms
                             values: [
                                tempEdgesManager.opacity => 0,
                                tempTopLeftCorner.opacity => 0,
                                tempTopRightCorner.opacity => 0,
                                tempBottomRightCorner.opacity => 0,
                                tempBottomLeftCorner.opacity => 0
                             ]
                             action: function() {
                                tempEdgesManager.visible = false;
                                tempTopLeftCorner.visible = false;
                                tempTopRightCorner.visible = false;
                                tempBottomRightCorner.visible = false;
                                tempBottomLeftCorner.visible = false;
                             }
                          }
                       ]
                    }.playFromStart();
                 });
              } else {
                 FX.deferAction(function() {
                    def tempEdgesManager = edgesManager;
                    def tempTopLeftCorner = topLeftCorner;
                    def tempTopRightCorner = topRightCorner;
                    def tempBottomRightCorner = bottomRightCorner;
                    def tempBottomLeftCorner = bottomLeftCorner;
                    tempEdgesManager.visible = true;
                    tempTopLeftCorner.visible = true;
                    tempTopRightCorner.visible = true;
                    tempBottomRightCorner.visible = true;
                    tempBottomLeftCorner.visible = true;
                    Timeline {
                       keyFrames: [KeyFrame {
                             time: 300ms
                             values: [
                                tempEdgesManager.opacity => 1,
                                tempTopLeftCorner.opacity => 1,
                                tempTopRightCorner.opacity => 1,
                                tempBottomRightCorner.opacity => 1,
                                tempBottomLeftCorner.opacity => 1
                             ]
                          }
                       ]
                    }.playFromStart();
                 });
              }
           }
   public def bubbleManager: BubbleManager = SimpleBubbleManager {
              tbi: missionRunConfigs.tbi
              activateBubbleManager: initializer.showHelpBubbles
              scene: scene
              windowManager:windows
              nomberOfTimesStarted:missionRunConfigs.nomberOfTimesStarted
           };
   public def tooltipManager: TooltipManager = SimpleTooltipManager {
            bubbleManager: bubbleManager
         };
   public def dragAndDropManager: DragAndDropManager = SimpleDragAndDropManager {
              windowManager: windows;
              bubbleManager: bubbleManager
           };
   public var scyToolFactory: ScyToolFactory;
   var windowPositioner: WindowPositioner;
   public-read var scyWindowControl: ScyWindowControl;
   public-read var newTitleGenerator: NewTitleGenerator;
   var missionMap: MissionMap;
   public-read var topLeftCorner: Corner;
   public-read var topRightCorner: Corner;
   public-read var bottomRightCorner: Corner;
   public-read var bottomLeftCorner: Corner;
   public-read var lowDebugGroup = Group {};
   public-read var highDebugGroup = Group {};
   public-read var contactlist: ContactList;
   var missionRuntimeSettingsManager: RuntimeSettingsManager;
   def cornerToolEffect: Effect = null;
//    def cornerToolEffect = DropShadow {
//         offsetX: 5
//         offsetY: 5
//         color: Color.GRAY
//         radius: 5
//      }
   def mucIdKey = config.getMetadataTypeManager().getMetadataKey(ScyRooloMetadataKeyIds.MUC_ID.getId());
   var backgroundUpdater: BackgroundUpdater;
   public var moreInfoToolFactory: MoreInfoToolFactory = TestMoreInfoToolFactory {};
   public-read def moreInfoManager = MoreInfoManagerImpl {
              scene: scene
              windowStyler: windowStyler
              tbi: missionRunConfigs.tbi
              moreInfoToolFactory: bind moreInfoToolFactory
              activeLas: bind missionModelFX.activeLas
              tooltipManager: tooltipManager
              bubbleManager: bubbleManager
           }
   public-init var preventShutdown = true;
   def shutdownHook = ShutdownHook {
              stage: scene.stage
              shutdownFunction: scyDesktopShutdownAction
              preventShutdown: preventShutdown
           }
   def runningOnMac : Boolean = System.getProperty("os.name").toLowerCase().startsWith("mac os x");
   public var scyFeedbackGiveButton: EloIconButton;
   public var scyFeedbackGetButton: EloIconButton;
   public var eportfolioButton: EportfolioButton;
   var cornerGroup: Group;
   public def desktopButtonSize = 25.0;
   public def desktopButtonActionScheme = 1;
   public var bigMissionMapControl: BigMissionMapControl;
   public var eloManagement: EloManagement;

   public function getActiveLasId():String{
       return missionModelFX.activeLas.id;
   }

   init {
      if (config.isRedirectSystemStreams() and config.getLoggingDirectory() != null) {
         RedirectSystemStreams.redirect(config.getLoggingDirectory());
      }

      // for Mac OS X CMD-Q
      if (runningOnMac) {
         OSXAdapter.setQuitHandler(this, getClass().getDeclaredMethod("quitFromMacOSXQuitHandler"));
      }
      
      DialogBox.windowStyler = windowStyler;
      ModalDialogBox.bubbleManager = bubbleManager;
      scyWindowControl.missionModel = missionModelFX;
      missionModelFX.showMoreInfo = moreInfoManager;
      FX.deferAction(initialWindowPositioning);
      FX.deferAction(initMouseBlocker);
      logger.info("repository class: {config.getRepository().getClass()}");
      if (config.getRepository() instanceof RepositoryWrapper) {
         var repositoryWrapper = config.getRepository() as RepositoryWrapper;
         var eloSavedActionHandler = EloSavedActionHandler {
                    scyDesktop: this;
                    tbi: missionRunConfigs.tbi
                 }
         repositoryWrapper.addEloSavedListener(eloSavedActionHandler);
         repositoryWrapper.setUserId(config.getToolBrokerAPI().getLoginUserName());
         repositoryWrapper.setMissionRuntimeEloUri(missionRunConfigs.missionRuntimeModel.getMissionRuntimeElo().getUriFirstVersion());
         repositoryWrapper.setMissionSpecificationEloUri(missionRunConfigs.missionRuntimeModel.getMissionRuntimeElo().getMissionSpecificationElo());
         repositoryWrapper.setMissionId(missionRunConfigs.missionRuntimeModel.getMissionRuntimeElo().getTypedContent().getMissionId());
         logger.info("Added eloSavedActionHandler as EloSavedListener to the repositoryWrapper");
      }
      create();
      if (initializer.singleEloMode) {
         loadSingleScyElo();
      }
   }

   public function quitFromMacOSXQuitHandler() : Boolean {
       FX.deferAction(scyDesktopShutdownAction);
       return false;
   }


   function initMouseBlocker(): Void {
      var theStage = scene.stage;
      if (theStage == null) {
         System.err.println("defering initMouseBlocker, because of the missing stage");
         FX.deferAction(initMouseBlocker);
      } else {
         MouseBlocker.initMouseBlocker(scene.stage);
         ProgressOverlay.initOverlay(scene.stage);
      }
   }

   function initialWindowPositioning() {
   //      scyWindowControl.positionWindows();
   }

   function checkProperties() {
      var errors = 0;
      errors += checkIfNull(config, "config");
      errors += checkIfNull(missionModelFX, "missionModel");
      errors += checkIfNull(windowStyler, "windowStyler");
      errors += checkIfNull(newEloCreationRegistry, "newEloCreationRegistry");
      if (errors > 0) {
         throw new IllegalArgumentException("One or more properties of ScyDesktop are null");
      }
   }

   function checkIfNull(object: Object, label: String): Integer {
      if (object == null) {
         logger.error("ScyDesktop property {label} may not be null");
         return 1;
      }
      return 0;
   }

   public function showContent(): Void {
      cornerGroup.visible = true;
   }

   function createElements() {

      newTitleGenerator = new NumberedNewTitleGenerator(newEloCreationRegistry);
      scyToolFactory = ScyToolFactory {
                 scyToolCreatorRegistryFX: scyToolCreatorRegistryFX;
                 drawerContentCreatorRegistryFX: drawerContentCreatorRegistryFX;
                 config: config;
                 initializer: initializer
                 newTitleGenerator: newTitleGenerator;
                 showMoreInfo: moreInfoManager
              }
      missionRuntimeSettingsManager = missionRunConfigs.runtimeSettingsManager;
      if (not initializer.singleEloMode) {
         createMultiEloElements();
      }
      if (initializer.windowPositioner.equalsIgnoreCase("simple")) {
      // this is the default
      } else if (initializer.windowPositioner.equalsIgnoreCase("roleArea")) {
         windowPositioner = RoleAreaWindowPositioner {
                    width: bind scene.width;
                    height: bind scene.height;
                    scyDesktop: this
                    showAreas: false
                 }
      } else if (initializer.windowPositioner.equalsIgnoreCase("roleAreaDebug")) {
         windowPositioner = RoleAreaWindowPositioner {
                    width: bind scene.width;
                    height: bind scene.height;
                    scyDesktop: this
                    showAreas: true
                 }
      } else if (initializer.windowPositioner.equalsIgnoreCase("functionalRole")) {
         windowPositioner = FunctionalRoleWindowPositioner {
                    scyDesktop: this;
                 }
      } else if (initializer.windowPositioner.equalsIgnoreCase("functionalRoleDebug")) {
         windowPositioner = FunctionalRoleWindowPositioner {
                    scyDesktop: this;
                    debug: true;
                 }
      } else if (initializer.windowPositioner.equalsIgnoreCase("functionalRoleIgnoreResources")) {
         windowPositioner = FunctionalRoleWindowPositioner {
                    scyDesktop: this;
                    ignoreResources: true;
                 }
      } else {
         logger.error("unknown windowPositioner specified: {initializer.windowPositioner}");
      }
      if (windowPositioner == null) {
         windowPositioner = SimpleWindowPositioner {
                    forbiddenNodes: [
                       topLeftCorner,
                       topRightCorner,
                       bottomRightCorner,
                       bottomLeftCorner
                    ];
                    width: bind scene.width;
                    height: bind scene.height;
                 }
      }
      scyWindowControl = ScyWindowControlImpl {
                 windowContentFactory: scyToolFactory;
                 windowManager: windows;
                 windowPositioner: windowPositioner;
                 missionModel: missionModelFX;
                 missionMap: missionMap;
                 windowStyler: windowStyler;
                 tbi: config.getToolBrokerAPI()
                 setScyContent: fillScyWindowNow;
                 tooltipManager: tooltipManager
                 bubbleManager: bubbleManager
                 dragAndDropManager: dragAndDropManager
                 repositoryWrapper: if (config.getRepository() instanceof RepositoryWrapper) config.getRepository() as RepositoryWrapper else null;
                 showEloInfoDisplay: initializer.debugMode
                 eloConfigManager: eloConfigManager
                 exceptionCatcher: initializer.exceptionCatcher
              }
      missionMap.scyWindowControl = scyWindowControl;
      bigMissionMapControl.scyWindowControl = scyWindowControl;
      eloManagement.scyWindowControl = scyWindowControl;
      backgroundUpdater = BackgroundUpdater {
                 windowStyler: windowStyler;
                 background: initializer.background
                 activeLas: bind missionModelFX.activeLas
              }
   }

   function createMultiEloElements() {
      //The frontend to thecontact list
      contactlist = ContactList {
                 columns: 2
                 contacts: []
                 dragAndDropManager: dragAndDropManager
                 tooltipManager: tooltipManager
                 windowStyler: windowStyler
                 scyDesktop: this
                 height: 250
                 showOfflineContacts: initializer.showOfflineContacts
                 width: 300
                 stateIndicatorOpacity: initializer.indicateOnlineStateByOpacity
              };
      contactlist.height = 250;
      missionMap = MissionMap {
                 missionModel: missionModelFX
                 bigMissionMap: false
                 tooltipManager: tooltipManager
                 dragAndDropManager: dragAndDropManager
                 runtimeSettingsRetriever: EloRuntimeSettingsRetriever {
                    eloUri: null;
                    runtimeSettingsManager: missionRuntimeSettingsManager
                 }
                 scyDesktop: this
                 metadataTypeManager: config.getMetadataTypeManager()
                 showLasId: initializer.debugMode
                 selectedScale: initializer.missionMapSelectedImageScale
                 notSelectedScale: initializer.missionMapNotSelectedImageScale
                 positionScale: initializer.missionMapPositionScale
              }
      def bigMissionMap = BigMissionMap {
                 missionModel: missionModelFX
                 bigMissionMap: true
                 tooltipManager: tooltipManager
                 bubbleManager: bubbleManager
                 dragAndDropManager: dragAndDropManager
                 runtimeSettingsRetriever: EloRuntimeSettingsRetriever {
                    eloUri: null;
                    runtimeSettingsManager: missionRuntimeSettingsManager
                 }
                 scyDesktop: this
                 metadataTypeManager: config.getMetadataTypeManager()
                 showLasId: initializer.debugMode
                 selectedScale: initializer.missionMapSelectedImageScale
                 notSelectedScale: initializer.missionMapNotSelectedImageScale
                 positionScale: initializer.missionMapPositionScale
              }
      bigMissionMapControl = BigMissionMapControl {
                 bigMissionMap: bigMissionMap
                 windowStyler: windowStyler
                 scyWindowControl: scyWindowControl
                 missionModel: missionModelFX
                 initializer: initializer
                 tooltipManager: tooltipManager
                 bubbleManager: bubbleManager
                 scyDesktop: this
                 buttonSize: 2.0*desktopButtonSize
                 moreInfoToolFactory: bind moreInfoToolFactory
                 moreInfoManager: moreInfoManager
              }

      topLeftCorner = TopLeftCorner {
                 content: contactlist;
                 color: Color.RED;
                 effect: cornerToolEffect
              }
      //        topRightCorner = TopRightCorner {
      //            content: topRightCornerTool;
      //            color: Color.GREEN;
      //            effect: cornerToolEffect
      //        }
      scyFeedbackGiveButton = EloIconButton {
                 eloIcon: windowStyler.getScyEloIcon("give_feedback")
                 size: desktopButtonSize
                 actionScheme: desktopButtonActionScheme
                 disableButton: initializer.offlineMode
                 tooltipManager: tooltipManager
                 tooltip: if (initializer.offlineMode) ##"Feedback is only available when working online" else ##"Give Feedback"
                 action: function(): Void {
                    def conf: Configuration = Configuration.getInstance();
                    def eloUriEncoded = URLEncoder.encode(missionRunConfigs.missionRuntimeModel.getMissionRuntimeElo().getUri().toString(), "UTF-8");
                    def feedbackURL = "{conf.getFeedbackProtocol()}://{conf.getFeedbackServer()}:{conf.getFeedbackPort()}{conf.getFeedbackContext()}FeedbackToolIndex.html?eloURI={eloUriEncoded}&action=give";
                    try {
                       var basicService = javax.jnlp.ServiceManager.lookup("javax.jnlp.BasicService") as javax.jnlp.BasicService;
                       if (basicService != null) {
                          var url: java.net.URL = new java.net.URL(feedbackURL);
                          basicService.showDocument(url);
                       }
                    } catch (e: javax.jnlp.UnavailableServiceException) {
                       BareBonesBrowserLaunch.openURL(feedbackURL);
                    }
                    scyFeedbackGiveButton.eloIcon = windowStyler.getScyEloIcon("give_feedback");
                 }
              }

      scyFeedbackGetButton = EloIconButton {
                 eloIcon: windowStyler.getScyEloIcon("get_feedback")
                 size: desktopButtonSize
                 actionScheme: desktopButtonActionScheme
                 disableButton: initializer.offlineMode
                 tooltipManager: tooltipManager
                 tooltip: if (initializer.offlineMode) ##"Feedback is only available when working online" else ##"Get Feedback"
                 action: function(): Void {
                    def conf: Configuration = Configuration.getInstance();
                    def eloUriEncoded = URLEncoder.encode(missionRunConfigs.missionRuntimeModel.getMissionRuntimeElo().getUri().toString(), "UTF-8");
                    def feedbackURL = "{conf.getFeedbackProtocol()}://{conf.getFeedbackServer()}:{conf.getFeedbackPort()}{conf.getFeedbackContext()}FeedbackToolIndex.html?eloURI={eloUriEncoded}&action=get";
                    try {
                       var basicService = javax.jnlp.ServiceManager.lookup("javax.jnlp.BasicService") as javax.jnlp.BasicService;
                       if (basicService != null) {
                          var url: java.net.URL = new java.net.URL(feedbackURL);
                          basicService.showDocument(url);
                       }
                    } catch (e: javax.jnlp.UnavailableServiceException) {
                       BareBonesBrowserLaunch.openURL(feedbackURL);
                    }
                    scyFeedbackGetButton.eloIcon = windowStyler.getScyEloIcon("get_feedback");
                 }
              }
      eportfolioButton = EportfolioButton { scyDesktop: this }
      dragAndDropManager.addDropTaget(eportfolioButton);
      def ePortfolioSpacer = Rectangle {
                 x: 0, y: 0
                 width: 1, height: 5
                 fill: Color.TRANSPARENT
              }

      topRightCorner = TopRightCorner {
                 content: VBox {
                    content: [
                       scyFeedbackGetButton,
                       scyFeedbackGiveButton,
                       ePortfolioSpacer,
                       eportfolioButton
                    ]
                    spacing: 0
                 };
              //               effect: cornerToolEffect
              }
      //}

      bottomRightCorner = BottomRightCorner {
                 // TODO, replace with specified tool
                 content: if (initializer.useBigMissionMap) bigMissionMapControl else missionMap
                 //            content:missionMap
                 //            content: HBox {
                 //               spacing: 5.0
                 //               content: [
                 //                  missionMap,
                 //                  bigMissionMapControl
                 //               ]
                 //            }
                 color: Color.BLUE;
                 effect: cornerToolEffect
              }
      bottomLeftCorner = BottomLeftCorner {
                 content: bottomLeftCornerTool;
                 color: Color.GRAY;
                 effect: cornerToolEffect
              }
      eloManagement = EloManagement {
                 scyDesktop: this;
                 repository: config.getRepository();
                 metadataTypeManager: config.getMetadataTypeManager();
                 titleKey: config.getTitleKey();
                 technicalFormatKey: config.getTechnicalFormatKey();
              }
      bottomLeftCornerTool = eloManagement;
   }

   def jdomStringConversion = new JDomStringConversion();

   function create(): Void {
      logger.info("create");
      checkProperties();
      createElements();
      scyDektopGroup.content = [
                 ///Testing only
                 //            Rectangle{width:bind boundsInLocal.width,
                 //                height:bind boundsInLocal.height,
                 //                fill:Color.BLACK
                 //            },
                 //backgroundImageView,
                 lowDebugGroup,
                 edgesManager,
                 windows.scyWindows,
                 cornerGroup = Group {
                            visible: false
                            content: [
                               topLeftCorner,
                               topRightCorner,
                               bottomRightCorner,
                               bottomLeftCorner,
                            ]
                         }
                 moreInfoManager.getControlNode(),
                 highDebugGroup,
              ]
   }

   function loadSingleScyElo() {
      def window = scyWindowControl.addOtherScyWindow(missionRunConfigs.scyEloToLoad);
      window.openFixedFullScreen();
      deferLoadTimer();
   }

   var deferLoadTimerCount = 5;

   function deferLoadTimer(): Void {
      if (deferLoadTimerCount <= 0) {
         initializer.loadTimer.endActivity();
      //         FX.exit();
      } else {
         initializer.loadTimer.startActivity("deferLoadTimer {deferLoadTimerCount}");
         --deferLoadTimerCount;
         FX.deferAction(deferLoadTimer);
      }
   }

   function fillScyWindowNow(window: ScyWindow): Void {
      def startNanos = System.nanoTime();
      window.isScyContentSet = true;
      var mucid = window.scyElo.getMucId();
      if (mucid == null) {
         mucid = window.mucId;
      }
      if (mucid != null and (initializer == null or not initializer.offlineMode)) {
         (window.eloToolConfig as BasicEloToolConfig).setContentCollaboration(true);
         installCollaborationTools(window, mucid);
      } else {
         realFillNewScyWindow2(window, false);
      }
      window.scyToolsList.onOpened();
      def usedNanos = System.nanoTime()-startNanos;
      println("fillScyWindowNow({window.eloUri}) took {usedNanos/1e6} ms.");
   }

   public function uninstallCollaborationTools(window: ScyWindow): Void {
      window.mucId = null;
      window.isCollaborative = false;
      if (window.scyContent instanceof CollaborationStartable) {
         (window.scyContent as CollaborationStartable).stopCollaboration();
      }
      if (window.rightDrawerTool instanceof CollaborationStartable) {
         (window.rightDrawerTool as CollaborationStartable).stopCollaboration();
      }
      window.scyElo.setMucId(null);
      var metadata: IMetadata = window.tbi.getELOFactory().createMetadata();
      var mucIdKey = window.tbi.getMetaDataTypeManager().getMetadataKey(ScyRooloMetadataKeyIds.MUC_ID);
      var mvc: IMetadataValueContainer = metadata.getMetadataValueContainer(mucIdKey);
      mvc.setValue(null);
      window.rightDrawerTool = null;
   }

   public function installCollaborationTools(window: ScyWindow, mucId: String): Void {
      window.mucId = mucId;
      realFillNewScyWindow2(window, true);
      def toolNode: Node = window.scyContent;
      // persist the mucid in the ELO
      var metadata: IMetadata = window.tbi.getELOFactory().createMetadata();
      var mucIdKey = window.tbi.getMetaDataTypeManager().getMetadataKey(ScyRooloMetadataKeyIds.MUC_ID);
      var mvc: IMetadataValueContainer = metadata.getMetadataValueContainer(mucIdKey);
      mvc.setValue(mucId);
      window.tbi.getRepository().addMetadata(window.eloUri, metadata);
      window.scyElo.setMucId(mucId);
      if (toolNode instanceof CollaborationStartable) {
         // tell the tool to configure for collaboration
         (toolNode as CollaborationStartable).startCollaboration(mucId);
         window.isCollaborative = true;
      }
      if (not initializer.singleEloMode) {
         if (window.isCollaborative and window.topDrawerTool instanceof CollaborationStartable) {
            (window.topDrawerTool as CollaborationStartable).startCollaboration(mucId);
         }
         if (window.isCollaborative and window.rightDrawerTool instanceof CollaborationStartable) {
            (window.rightDrawerTool as CollaborationStartable).startCollaboration(mucId);
         }
         if (window.isCollaborative and window.bottomDrawerTool instanceof CollaborationStartable) {
            (window.bottomDrawerTool as CollaborationStartable).startCollaboration(mucId);
         }
         for (leftDrawerTool in window.leftDrawerTools) {
            if (window.isCollaborative and leftDrawerTool instanceof CollaborationStartable) {
               (leftDrawerTool as CollaborationStartable).startCollaboration(mucId);
            }
         }
      }
   }

   function realFillNewScyWindow2(window: ScyWindow, collaboration: Boolean): Void {
      logger.info("realFillNewScyWindow2({window.eloUri},{collaboration})");

      def eloConfig = window.eloToolConfig;
      if (eloConfig == null) {
         logger.error("Can't find eloConfig for {window.eloUri} of type {window.eloType}");
         return;
      }

      // don't place the window content tool in the window, let the please wait message stay until every thing is created
      def scyToolsList = ScyToolsList {
         exceptionCatcher: initializer.exceptionCatcher
      }

      // create the tools
      scyToolsList.actionLoggerTool = ScyToolActionLogger {
         window: window;
         config: config
         missionRuntimeEloUri: missionRunConfigs.missionRuntimeModel.getMissionRuntimeElo().getUri()
      };
      if (not collaboration and eloConfig.isContentCollaboration()) {
         // currently, the content tool must be created on the first call, which is with collaboration false
         // otherwise the first set of tools are not informed of the elo load messages
         throw new IllegalStateException("the content tool may not be a collaboration only tool");
      }
      scyToolsList.windowContentTool = scyToolFactory.createNewScyToolNode(eloConfig.getContentCreatorId(), window.eloType, window.eloUri, window, false);
      if (not initializer.singleEloMode) {
         if (window.topDrawerTool == null) {
            scyToolsList.topDrawerTool = scyToolFactory.createNewScyToolNode(eloConfig.getTopDrawerCreatorId(), window.eloType, window.eloUri, window, true);
         }
         if (window.rightDrawerTool == null) {
            scyToolsList.rightDrawerTool = scyToolFactory.createNewScyToolNode(eloConfig.getRightDrawerCreatorId(), window.eloType, window.eloUri, window, true);
         }
         if (window.bottomDrawerTool == null) {
            scyToolsList.bottomDrawerTool = scyToolFactory.createNewScyToolNode(eloConfig.getBottomDrawerCreatorId(), window.eloType, window.eloUri, window, true);
         }
         if (eloConfig.getLeftDrawerCreatorId() != null) {
            def creatorTokenizer = new StringTokenizer(eloConfig.getLeftDrawerCreatorId());
            while (creatorTokenizer.hasMoreTokens()) {
               var leftDrawerCreatorId = creatorTokenizer.nextToken();
               // TODO, remove these hard coded hacks!!!
               var addDrawer = true;
               if (leftDrawerCreatorId.equals("feedbackQuestion") and not window.scyElo.getAuthors().contains(config.getToolBrokerAPI().getLoginUserName())) {
                  addDrawer = false;
               }
               if (leftDrawerCreatorId.equals("assingmentInfo") and window.scyElo.getAssignmentUri() == null) {
                  addDrawer = false;
               }
               if (leftDrawerCreatorId.equals("resourcesInfo") and window.scyElo.getResourcesUri() == null) {
                  addDrawer = false;
               }
               if (addDrawer) {
                  insert scyToolFactory.createNewScyToolNode(leftDrawerCreatorId, window.eloType, window.eloUri, window, true) into scyToolsList.leftDrawerTools;
               }
            };
         }
      }

      // all tools are created and placed in the window
      // now do the ScyTool initialisation
      def myEloChanged = SimpleMyEloChanged {
         window: window;
      }

      // assigning fucntional roles
      var functionalRoles: EloFunctionalRole[];
      if (eloConfig.getEloFunctionalRoles() != null and eloConfig.getEloFunctionalRoles().size() > 0) {
         functionalRoles = for (object in eloConfig.getEloFunctionalRoles()) {
            object as EloFunctionalRole
         }
      } else {
         functionalRoles = EloFunctionalRole.values()
      }

      // creating the elo saver to handle elo events
      def myEloSaver = SimpleScyDesktopEloSaver {
         config: config
         toolBrokerAPI: config.getToolBrokerAPI()
         repository: config.getRepository()
         eloFactory: config.getEloFactory()
         titleKey: config.getTitleKey()
         technicalFormatKey: config.getTechnicalFormatKey()
         window: window;
         myEloChanged: myEloChanged;
         newTitleGenerator: newTitleGenerator;
         windowStyler: windowStyler;
         scyToolActionLogger: scyToolsList.actionLoggerTool as ScyToolActionLogger
         authorMode: initializer.authorMode
         functionalRoles: functionalRoles
         loginName: config.getToolBrokerAPI().getLoginUserName()
      };

      def runtimeSettingsRetriever = EloRuntimeSettingsRetriever {
         eloUri: bind window.eloUri
         runtimeSettingsManager: missionRuntimeSettingsManager
      }

      // place the logger first
      if (scyToolsList.actionLoggerTool != null) {
         window.scyToolsList.actionLoggerTool = scyToolsList.actionLoggerTool;
      }
      // do the initialize cycle on the created tools
      scyToolsList.setEloSaver(myEloSaver);
      scyToolsList.setMyEloChanged(myEloChanged);
      scyToolsList.setRuntimeSettingsRetriever(runtimeSettingsRetriever);
      scyToolsList.setReadOnly(initializer.globalReadOnlyMode);
      scyToolsList.setWindowColorScheme(window.windowColorScheme);
      scyToolsList.setTitleBarButtonManager(window.titleBarButtonManager);
      scyToolsList.initialize();
      scyToolsList.postInitialize();
      XFX.deferActionAndWait(function() {
         // place the tools in the window
         if (scyToolsList.windowContentTool != null) {
            window.scyContent = scyToolsList.windowContentTool;
         }
         if (scyToolsList.topDrawerTool != null) {
            window.topDrawerTool = scyToolsList.topDrawerTool;
            if (collaboration) {
               window.openDrawer("top");
            }
         }
         if (scyToolsList.rightDrawerTool != null) {
            window.rightDrawerTool = scyToolsList.rightDrawerTool;
            if (collaboration) {
               window.openDrawer("right");
            }
         }
         if (scyToolsList.bottomDrawerTool != null) {
            window.bottomDrawerTool = scyToolsList.bottomDrawerTool;
            if (collaboration) {
               window.openDrawer("bottom");
            }
         }
         if (scyToolsList.leftDrawerTools != null) {
            window.leftDrawerTools = scyToolsList.leftDrawerTools;
            if (collaboration) {
               window.openDrawer("left");
            }
         }
      });
      // if the window content tool is defined, meaning a new or existing elo is loaded, report this
      if (window.scyContent != null) {
         // make sure that the tool itself in the scene graph
         FX.deferAction(function(): Void {
            if (window.eloUri != null) {
               scyToolsList.loadElo(window.eloUri);
            } else {
               scyToolsList.newElo();
            }
            scyToolsList.loadedEloChanged(window.eloUri);
         });
      }
   }

   public override function processNotification(notification: INotification): Boolean {
      return false;
   }

   var showingQuitDialog = false;

   function scyDesktopShutdownAction(): Void {
      bubbleManager.pauze();
      if (preventShutdown) {
         if (showingQuitDialog) {
            // we are asking for quit now, don't show a second dialog
            return;
         }
         var windowsShutdownMessage = "";
         def shutdownPermissionFromWindows = askShutdownPermissionFromWindows();
         if (not shutdownPermissionFromWindows) {
            windowsShutdownMessage = "\n\n{##"One or more products strongly suggest NOT to quit."}";
         }
         showingQuitDialog = true;
         if (initializer.showQuitConfirmation) {
            var allSavedMessage = "";
            if (not initializer.globalReadOnlyMode){
               allSavedMessage = ##"\nEverything will be saved.";
            }
            def dialogMessage = "{##"Are you sure you want to quit SCY-Lab?"}{allSavedMessage}{windowsShutdownMessage}";

            DialogBox.showOptionDialog(LogoEloIcon {}, DialogType.YES_NO_DIALOG, dialogMessage, ##"Confirm quit", 300, true, true, saveAndCloseEverything, doNotShutdown, null);
         } else {
            saveAndCloseEverything()
         }
      } else {
         saveAndCloseEverything();
      }
   }

   function askShutdownPermissionFromWindows(): Boolean {
      try {
         return scyWindowControl.askShutdownPermissionFromWindows();
      } catch (e: Exception) {
         initializer.exceptionCatcher.showAndLogUncaughtException(Thread.currentThread(), e);
      }
      return true;
   }

   function doNotShutdown(): Void {
      showingQuitDialog = false;
      bubbleManager.resume();
   }

   function saveAndCloseEverything(): Void {
      showingQuitDialog = false;
      ProgressOverlay.startShowWorking();
      println("Scy desktop is shutting down....");
      logger.info("Scy desktop is shutting down....");
      if (not initializer.globalReadOnlyMode) {
         XFX.runActionInBackgroundAndCallBack(saveAll, function(o:Object){ logAndExit() });
      } else {
         logAndExit();
      }
   }

   function logAndExit():Void{
      try {
         logLoggedOut();
      } catch (e: Exception) {
         logger.error("an exception occured during logLoggedOut", e);
      }
      closeIfPossible(config.getToolBrokerAPI(), "tool broker");
      FX.exit();
   }


   function logLoggedOut(): Void {
      def action: IAction = new Action();
      action.setUser(missionRunConfigs.tbi.getLoginUserName());
      action.setType("logged_out");
      action.addContext(ContextConstants.tool, "scy-desktop");
      action.addContext(ContextConstants.mission, missionRunConfigs.missionRuntimeModel.getMissionRuntimeElo().getUri().toString());
      action.addContext(ContextConstants.session, "n/a");
      action.addContext(ContextConstants.eloURI, "n/a");
      missionRunConfigs.tbi.getActionLogger().log(action);
      logger.info("logged logge_out-action: {action}");
   }

   function saveAll(): Void {
      println("Now saving all ELOs....");
      logger.info("Now saving all ELOs....");
      missionModelFX.autoUpdateMissionModelElo = false;
      try {
         scyWindowControl.saveBeforeQuit();
      } catch (e: Exception) {
         logger.error("an exception occured during the saving of elos", e);
      }
      logger.info("Now saving current window state....");
      try {
         scyWindowControl.saveCurrentWindowState();
      } catch (e: Exception) {
         logger.error("an exception occured during the saving of the window state", e);
      }
      logger.info("Now saving mission model....");
      try {
         missionModelFX.updateElo();
      } catch (e: Exception) {
         logger.error("an exception occured during the update of mission map model elo", e);
      }
   }

   function closeIfPossible(object: Object, label: String): Void {
      if (object instanceof Closeable) {
         try {
            (object as Closeable).close();
         } catch (e: Exception) {
            logger.error("an exception occured during the close of {label}", e);
         }
      }
   }

}

function run() {
   InitLog4JFX.initLog4J();
   var springConfigFactory = new SpringConfigFactory();
   springConfigFactory.initFromClassPath("config/scyDesktopTestConfig.xml");
   var config = springConfigFactory.getConfig();
   //   InitLog4j.init();
   //   var anchor0 = MissionAnchorFX{
   //       title: "0";
   //       xPos: 00;
   //       yPos: 20;
   //       color: Color.BLUE;
   //       eloUri: new URI("test://anchor0.tst");
   //   }
   //   var anchor1 = MissionAnchorFX{
   //       title: "1";
   //       xPos: 40;
   //       yPos: 00;
   //       color: Color.BLUE;
   //       eloUri: new URI("test://anchor1.tst");
   //   }
   //   var anchor2 = MissionAnchorFX{
   //       title: "2";
   //       xPos: 80;
   //       yPos: 00;
   //       color: Color.GREEN;
   //       eloUri: new URI("test://anchor2.tst");
   //   }
   //   var anchor3 = MissionAnchorFX{
   //       title: "3";
   //       xPos: 40;
   //       yPos: 40;
   //       color: Color.RED;
   //       eloUri: new URI("test://anchor3.tst");
   //   }
   //   var anchor4 = MissionAnchorFX{
   //       title: "4";
   //       xPos: 80;
   //       yPos: 40;
   //       color: Color.ORANGE;
   //       eloUri: new URI("test://anchor4.tst");
   //   }
   //   var anchor5 = MissionAnchorFX{
   //       title: "5";
   //       xPos: 120;
   //       yPos: 20;
   //       color: Color.ORANGE;
   //       eloUri: new URI("test://anchor5.tst");
   //   }
   //   anchor0.nextAnchors=[anchor1,anchor2,anchor3,anchor4];
   //   anchor1.nextAnchors=[anchor2,anchor3,anchor4];
   //   anchor2.nextAnchors=[anchor1,anchor3,anchor4,anchor5];
   //   anchor3.nextAnchors=[anchor1,anchor2,anchor4];
   //   anchor4.nextAnchors=[anchor1,anchor2,anchor3,anchor5];

   var missionModel = MissionModelFX {
           //       anchors: [anchor0,anchor1,anchor2,anchor3,anchor4,anchor5];
           //       activeAnchor:anchor0
           }
   missionModel = MissionModelFX {
           //       anchors: [];
           }
   var newWindowCounter = 0;
   var newWindowButton: Button = Button {
              text: "New Window"
              action: function() {
                 var title = "new_{++newWindowCounter}";
                 var window: ScyWindow = StandardScyWindow {
                            title: title
                            id: "new://{title}"
                            allowClose: true;
                            allowResize: true;
                            allowRotate: true;
                            allowMinimize: true;
                         }
                 scyDesktop.windows.addScyWindow(window);
              }
           }
//   var newScyWindowTool = NewScyWindowTool {
//              repository: config.getRepository();
//              titleKey: config.getTitleKey();
//              technicalFormatKey: config.getTechnicalFormatKey();
//           }
   //   var windowContentCreatorRegistryFX:WindowContentCreatorRegistryFX =WindowContentCreatorRegistryFXImpl{
   //         };
   //
   //   windowContentCreatorRegistryFX.registerWindowContentCreator(new SwingSizeTestPanelCreator(), "size");
   //
   //   var drawerContentCreatorRegistryFX:DrawerContentCreatorRegistryFX =DrawerContentCreatorRegistryFXImpl{
   //         };

   //   drawerContentCreatorRegistryFX.registerDrawerContentCreator(new EloXmlViewerCreator(), "xmlViewer");
   var scyDesktop: ScyDesktop = ScyDesktop {
              config: config;
              missionModelFX: missionModel;
           //      windowContentCreatorRegistryFX:windowContentCreatorRegistryFX;
           //      newEloCreationRegistry: NewEloCreationRegistryImpl{};
           //      drawerContentCreatorRegistryFX:drawerContentCreatorRegistryFX;
           //      topLeftCornerTool:MissionMap{
           //         missionModel: missionModel
           //      }
           //      bottomRightCornerTool:MissionMap{
           //         missionModel: missionModel
           //      }
           //      bottomLeftCornerTool:newWindowButton;
           //        bottomLeftCornerTool: newScyWindowTool;
           }
//   newScyWindowTool.scyDesktop = scyDesktop;
//   scyDesktop.bottomLeftCornerTool = newScyWindowTool;
   //   scyDesktop.bottomLeftCornerTool = Rectangle{x:10,y:10,width:100,height:100,fill:Color.BLACK};

   scyDesktop.newEloCreationRegistry.registerEloCreation("test");
   scyDesktop.newEloCreationRegistry.registerEloCreation("tst");
   scyDesktop.newEloCreationRegistry.registerEloCreation("size");
   Stage {
      title: "ScyDestop Test"
      scene: Scene {
         width: 800
         height: 600
         content: [ //                scyDesktop
         ]
      }
   }
}

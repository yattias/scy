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
import eu.scy.client.desktop.scydesktop.scywindows.EloInfoControl;
import eu.scy.client.desktop.scydesktop.elofactory.WindowContentCreatorRegistryFX;
import eu.scy.client.desktop.scydesktop.corners.Corner;
import eu.scy.client.desktop.scydesktop.config.Config;
import eu.scy.client.desktop.scydesktop.elofactory.NewEloCreationRegistry;
import eu.scy.client.desktop.scydesktop.elofactory.DrawerContentCreatorRegistryFX;
import eu.scy.client.desktop.scydesktop.scywindows.window.MouseBlocker;
import eu.scy.client.desktop.scydesktop.scywindows.EloSavedActionHandler;
import javafx.scene.Node;
import java.lang.Object;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import eu.scy.client.desktop.scydesktop.utils.RedirectSystemStreams;
import eu.scy.client.desktop.scydesktop.scywindows.WindowPositioner;
import eu.scy.client.desktop.scydesktop.scywindows.window_positions.SimpleWindowPositioner;
import eu.scy.client.desktop.scydesktop.utils.log4j.Logger;
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
import eu.scy.client.desktop.scydesktop.corners.tools.NewScyWindowTool;
import eu.scy.client.desktop.scydesktop.dummy.DummyEloInfoControl;
import eu.scy.client.desktop.scydesktop.dummy.DummyWindowStyler;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import eu.scy.client.desktop.scydesktop.scywindows.window.ScyToolsList;
import eu.scy.client.desktop.scydesktop.scywindows.window.StandardScyWindow;
import eu.scy.client.desktop.scydesktop.utils.log4j.InitLog4JFX;
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
import eu.scy.client.desktop.scydesktop.scywindows.EloDisplayTypeControl;
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
import eu.scy.client.desktop.scydesktop.tools.corner.contactlist.Contact;
import eu.scy.client.desktop.scydesktop.tools.corner.contactlist.ContactList;
import eu.scy.client.desktop.scydesktop.tools.corner.contactlist.OnlineState;
import eu.scy.client.desktop.scydesktop.tools.corner.missionmap.MissionMap;
import eu.scy.client.desktop.scydesktop.uicontrols.MultiImageButton;
import eu.scy.common.mission.impl.jdom.JDomStringConversion;
import eu.scy.notification.api.INotification;
import java.io.Closeable;
import java.lang.IllegalArgumentException;
import java.lang.Integer;
import java.lang.String;
import org.jdom.Element;
import roolo.api.search.AndQuery;
import roolo.api.search.ISearchResult;
import org.roolo.rooloimpljpa.repository.search.BasicMetadataQuery;
import org.roolo.rooloimpljpa.repository.search.BasicSearchOperations;
import java.lang.Exception;
import java.net.URI;
import eu.scy.common.scyelo.ScyRooloMetadataKeyIds;
import eu.scy.client.desktop.scydesktop.mission.MissionRunConfigs;
import eu.scy.common.mission.RuntimeSettingsElo;
import eu.scy.common.mission.impl.MissionRuntimeSettingsManager;
import java.util.HashSet;
import eu.scy.client.desktop.scydesktop.scywindows.scydesktop.EloRuntimeSettingsRetriever;
import eu.scy.common.mission.MissionSpecificationElo;
import eu.scy.common.scyelo.EloFunctionalRole;
import eu.scy.common.mission.MissionModelElo;

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
   public var eloInfoControl: EloInfoControl;
   public var eloDisplayTypeControl: EloDisplayTypeControl;
   public var windowStyler: WindowStyler;
   public var scyToolCreatorRegistryFX: ScyToolCreatorRegistryFX;
   public var newEloCreationRegistry: NewEloCreationRegistry;
   public var windowContentCreatorRegistryFX: WindowContentCreatorRegistryFX;
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
   public def tooltipManager: TooltipManager = SimpleTooltipManager {};
   public def dragAndDropManager: DragAndDropManager = SimpleDragAndDropManager {
         windowManager: windows;
      };
   var scyToolFactory: ScyToolFactory;
//   var windowContentFactory: WindowContentFactory;
//   var drawerContentFactory: DrawerContentFactory;
   var windowPositioner: WindowPositioner;
   public-read var scyWindowControl: ScyWindowControl;
   public-read var newTitleGenerator: NewTitleGenerator;
   var missionMap: MissionMap;
   public-read var topLeftCorner: Corner;
   public-read var topRightCorner: Corner;
   public-read var bottomRightCorner: Corner;
   public-read var bottomLeftCorner: Corner;
   var backgroundImage: Image;
   var backgroundImageView: ImageView;
   public-read var lowDebugGroup = Group {};
   public-read var highDebugGroup = Group {};
   var missionRuntimeSettingsManager: MissionRuntimeSettingsManager;
   def cornerToolEffect: Effect = null;
//    def cornerToolEffect = DropShadow {
//         offsetX: 5
//         offsetY: 5
//         color: Color.GRAY
//         radius: 5
//      }
   def mucIdKey = config.getMetadataTypeManager().getMetadataKey(ScyRooloMetadataKeyIds.MUC_ID.getId());
   var backgroundUpdater: BackgroundUpdater;

   init {
      if (config.isRedirectSystemStreams() and config.getLoggingDirectory() != null) {
         RedirectSystemStreams.redirect(config.getLoggingDirectory());
      }
      //      missionMap.missionModel = missionModelFX;

      scyWindowControl.missionModel = missionModelFX;
      FX.deferAction(initialWindowPositioning);
      FX.deferAction(initMouseBlocker);
      //        FX.deferAction(function () {
      //            MouseBlocker.initMouseBlocker(scene.stage);
      //        });
      logger.info("repository class: {config.getRepository().getClass()}");
      if (config.getRepository() instanceof RepositoryWrapper) {
         var repositoryWrapper = config.getRepository() as RepositoryWrapper;
         var eloSavedActionHandler = EloSavedActionHandler {
               scyWindowControl: this.scyWindowControl;
            }
         repositoryWrapper.addEloSavedListener(eloSavedActionHandler);
         repositoryWrapper.setUserId(config.getToolBrokerAPI().getLoginUserName());
         repositoryWrapper.setMissionRuntimeEloUri(missionRunConfigs.missionRuntimeElo.getUriFirstVersion());
         logger.info("Added eloSavedActionHandler as EloSavedListener to the repositoryWrapper");
      }
      FX.addShutdownAction(scyDesktopShutdownAction);
      create();
   }

   function initMouseBlocker(): Void {
      var theStage = scene.stage;
      if (theStage == null) {
         System.err.println("defering initMouseBlocker, because of the missing stage");
         FX.deferAction(initMouseBlocker);
      } else {
         MouseBlocker.initMouseBlocker(scene.stage);
      }
   }

   function initialWindowPositioning() {
   //      scyWindowControl.positionWindows();
   }

   function checkProperties() {
      var errors = 0;
      errors += checkIfNull(config, "config");
      errors += checkIfNull(missionModelFX, "missionModel");
      errors += checkIfNull(eloInfoControl, "eloInfoControl");
      errors += checkIfNull(windowStyler, "windowStyler");
      errors += checkIfNull(windowContentCreatorRegistryFX, "windowContentCreatorRegistryFX");
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

   function createElements() {
      var backgroundImageUrl;
      //      if (config.getBackgroundImageFileName()!=null){
      //         if (config.isBackgroundImageFileNameRelative()){
      //            backgroundImageUrl = "{__DIR__}{config.getBackgroundImageFileName()}";
      //         }
      //         else{
      //            backgroundImageUrl = config.getBackgroundImageFileName();
      //         }
      //         backgroundImage = Image {
      //             url: backgroundImageUrl
      //         }
      //         logger.info("background image: {backgroundImage.url}, error: {backgroundImage.error}");
      //         backgroundImageView = ImageView {
      //            image: backgroundImage
      //            fitWidth: bind scene.width
      //            fitHeight:bind scene.height
      //            preserveRatio:false
      //            cache:true
      //         }
      //      }

      newTitleGenerator = new NumberedNewTitleGenerator(newEloCreationRegistry);
      scyToolFactory = ScyToolFactory {
            scyToolCreatorRegistryFX: scyToolCreatorRegistryFX;
            windowContentCreatorRegistryFX: windowContentCreatorRegistryFX;
            drawerContentCreatorRegistryFX: drawerContentCreatorRegistryFX;
            config: config;
            initializer: initializer
            newTitleGenerator: newTitleGenerator;
         }
      var specificationRuntimeSettingsElo:RuntimeSettingsElo = null;
      var specificationMissionMapModelEloUriSet = new HashSet();
      def missionSpecificationEloUri = missionRunConfigs.missionRuntimeElo.getTypedContent().getMissionSpecificationEloUri();
      if (missionSpecificationEloUri != null) {
         def missionSpecificationElo = MissionSpecificationElo.loadElo(missionSpecificationEloUri, missionRunConfigs.tbi);
         def specificationRuntimeSettingsEloUri = missionSpecificationElo.getTypedContent().getRuntimeSettingsEloUri();
         if (specificationRuntimeSettingsEloUri != null) {
            specificationRuntimeSettingsElo = RuntimeSettingsElo.loadElo(specificationRuntimeSettingsEloUri, missionRunConfigs.tbi);
         }
         if (missionSpecificationElo.getTypedContent().getMissionMapModelEloUri() != null) {
            def specificationMissionMapModelElo = MissionModelElo.loadElo(missionSpecificationElo.getTypedContent().getMissionMapModelEloUri(), missionRunConfigs.tbi);
            specificationMissionMapModelEloUriSet.addAll(specificationMissionMapModelElo.getMissionModel().getEloUris(true));
         }
      }

      missionRuntimeSettingsManager = new MissionRuntimeSettingsManager(specificationRuntimeSettingsElo, missionRunConfigs.runtimeSettingsElo, specificationMissionMapModelEloUriSet, missionRunConfigs.tbi);
      //      windowContentFactory = WindowContentFactory{
      //         windowContentCreatorRegistryFX:windowContentCreatorRegistryFX;
      //         config:config;
      //         newTitleGenerator:newTitleGenerator;
      //      }
      //      drawerContentFactory = DrawerContentFactory{
      //         drawerContentCreatorRegistryFX:drawerContentCreatorRegistryFX;
      //         config:config;
      //      }
      //TODO remove contacts and connect to user management
      def contact1 = Contact {
            currentMission: "Testmission";
            imageURL: "img/buddyicon.png";
            name: "Sven Manske";
            onlineState: OnlineState.ONLINE;
            progress: 1.0;
         };
      def contact2 = Contact {
            currentMission: "Another Mission";
            imageURL: "img/buddyicon.png";
            name: "Adam G";
            onlineState: OnlineState.AWAY;
            progress: 0.1;
         };
      //The contact list (users)
      def contactContent = [contact1, contact2];
      //The frontend to thecontact list
      def contactList: ContactList = ContactList {
            columns: 2
            contacts: contactContent
            dragAndDropManager: dragAndDropManager
            tooltipManager: tooltipManager
            scyDesktop: this
            height: 250
            showOfflineContacts: initializer.showOfflineContacts
            width: 300
            stateIndicatorOpacity: initializer.indicateOnlineStateByOpacity
         };
      contactList.height = 250;
      missionMap = MissionMap {
            missionModel: missionModelFX
            tooltipManager: tooltipManager
            dragAndDropManager: dragAndDropManager
            runtimeSettingsRetriever: EloRuntimeSettingsRetriever {
               eloUri: null;
               runtimeSettingsManager: missionRuntimeSettingsManager
            }
            scyDesktop: this
            metadataTypeManager: config.getMetadataTypeManager()
            showLasId: initializer.debugMode
            eloDisplayTypeControl: eloDisplayTypeControl
            selectedScale: initializer.missionMapSelectedImageScale
            notSelectedScale: initializer.missionMapNotSelectedImageScale
            positionScale: initializer.missionMapPositionScale
         //         translateX:40;
         //         translateY:40;
         }
      missionMap.scyWindowControl = scyWindowControl;
      topLeftCorner = TopLeftCorner {
            content: contactList;
            color: Color.RED;
            effect: cornerToolEffect
         }
      //        topRightCorner = TopRightCorner {
      //            content: topRightCornerTool;
      //            color: Color.GREEN;
      //            effect: cornerToolEffect
      //        }
      var SPTButton = MultiImageButton {
            imageName: "planning"
            disable: initializer.offlineMode
            //                    toolTip: "Open the Student Planning Tool!";
            //                    tooltipManager: tooltipManager;
            //                    normalImage: Image { url: "{__DIR__}planningtoolicon.png" };
            //                    selectImage: Image { url: "{__DIR__}planningtooliconhighlight.png" };
            action: function(): Void {

               var userName = config.getToolBrokerAPI().getLoginUserName();
               var missionId = config.getToolBrokerAPI().getMission();
               var typeQuery = new BasicMetadataQuery(config.getTechnicalFormatKey(), BasicSearchOperations.EQUALS, "scy/studentplanningtool");
               var titleQuery = new BasicMetadataQuery(config.getTitleKey(), BasicSearchOperations.EQUALS, userName);
               var andQuery = new AndQuery(typeQuery, titleQuery);
               //var missionId = config.getBasicMissionMap().getId();
               if (missionId != null) {
                  var missionIdQuery = new BasicMetadataQuery(config.getMetadataTypeManager().getMetadataKey(ScyRooloMetadataKeyIds.MISSION.getId()), BasicSearchOperations.EQUALS, missionId);
                  andQuery.addQuery(missionIdQuery);
               }
               var results = config.getRepository().search(andQuery);
               logger.info("NUMBER OF SPT elos found: {results.size()}");
               var sptELO;
               if (results.size() == 1) {
                  var searchResult = results.get(0) as ISearchResult;
                  sptELO = config.getRepository().retrieveELO(searchResult.getUri());

               } else {
                  logger.info("OK, let's create a new one");
                  //we need to create a new one
                  sptELO = config.getEloFactory().createELO();

                  sptELO.getMetadata().getMetadataValueContainer(config.getTitleKey()).setValue(userName);
                  sptELO.getMetadata().getMetadataValueContainer(config.getTechnicalFormatKey()).setValue("scy/studentplanningtool");
                  var missionIdKy = config.getMetadataTypeManager().getMetadataKey(ScyRooloMetadataKeyIds.MISSION.getId());
                  sptELO.getMetadata().getMetadataValueContainer(missionIdKy).setValue(missionId);

                  var sptMetadata = config.getRepository().addNewELO(sptELO);
                  config.getEloFactory().updateELOWithResult(sptELO, sptMetadata);

               }
               def newWindow = scyWindowControl.addOtherScyWindow(sptELO.getUri());
               newWindow.openWindow(700, 600);
            }
         }
      topRightCorner = TopRightCorner {
            content: SPTButton;
            effect: cornerToolEffect
         }
      bottomRightCorner = BottomRightCorner {
            // TODO, replace with specified tool
            content: missionMap;
            color: Color.BLUE;
            effect: cornerToolEffect
         }
      bottomLeftCorner = BottomLeftCorner {
            content: bottomLeftCornerTool;
            color: Color.GRAY;
            effect: cornerToolEffect
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
            eloInfoControl: eloInfoControl;
            windowStyler: windowStyler;
            tbi: config.getToolBrokerAPI()
            setScyContent: fillNewScyWindow2;
            tooltipManager: tooltipManager
            dragAndDropManager: dragAndDropManager
            repositoryWrapper: if (config.getRepository() instanceof RepositoryWrapper) config.getRepository() as RepositoryWrapper else null;
            showEloInfoDisplay: initializer.debugMode
         }
      backgroundUpdater = BackgroundUpdater {
            eloDisplayTypeControl: eloDisplayTypeControl
            background: initializer.background
            activeLas: bind missionModelFX.activeLas
         }
   }

   def jdomStringConversion = new JDomStringConversion();

   function textToEloContentXml(text: String): String {
      var textElement = new Element("SPTString");
      textElement.setText(text);
      return jdomStringConversion.xmlToString(textElement);
   }

//    public override function create(): Node {
//        logger.info("create");
//        checkProperties();
//        createElements();
//        Group {
//            content: [
//                ///Testing only
//                //            Rectangle{width:bind boundsInLocal.width,
//                //                height:bind boundsInLocal.height,
//                //                fill:Color.BLACK
//                //            },
//                //backgroundImageView,
//                lowDebugGroup,
//                edgesManager,
//                windows.scyWindows,
//                topLeftCorner,
//                topRightCorner,
//                bottomRightCorner,
//                bottomLeftCorner,
//                highDebugGroup,
//                Rectangle { fill: Color.BLACK, x: 100, y: 100, width: boundsInLocal.width, height: boundsInLocal.height },
//            /*
//            Button {
//            text: "add an edge ";
//            translateX: 210;
//            action: function() {
//            edgesManager.addEdge((windows.scyWindows.content[0] as ScyWindow), (windows.scyWindows.content[1] as ScyWindow));
//            }
//            }
//             */
//            ]
//        }
//    }
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
            topLeftCorner,
            topRightCorner,
            bottomRightCorner,
            bottomLeftCorner,
            highDebugGroup,
         //                Rectangle { fill: Color.BLACK, x: 100, y: 100, width: boundsInLocal.width, height: boundsInLocal.height },
         /*
         Button {
         text: "add an edge ";
         translateX: 210;
         action: function() {
         edgesManager.addEdge((windows.scyWindows.content[0] as ScyWindow), (windows.scyWindows.content[1] as ScyWindow));
         }
         }
          */
         ]
   }

   public function fillNewScyWindow2(window: ScyWindow): Void {
      if (window.eloUri != null) {
         var metadata = config.getRepository().retrieveMetadata(window.eloUri);
         var mucId = metadata.getMetadataValueContainer(mucIdKey).getValue() as String;
         if (mucId != null) {
            window.mucId = mucId;
         }
      }

      var pleaseWait = PleaseWait {};
      window.scyContent = pleaseWait;
      FX.deferAction(function() {
         // one defer does not seem to be enough to show the please wait content
         FX.deferAction(function() {
            fillScyWindowNow(window);
         });
      });
   }

   function fillScyWindowNow(window: ScyWindow): Void {
      realFillNewScyWindow2(window, false);
      if (window.mucId.length() > 0 and not initializer.offlineMode) {
         installCollaborationTools(window);
      }
      window.scyToolsList.onOpened();
   }

   public function installCollaborationTools(window: ScyWindow): Void {
      realFillNewScyWindow2(window, true);
      def toolNode: Node = window.scyContent;
      if (toolNode instanceof CollaborationStartable) {
         (toolNode as CollaborationStartable).startCollaboration(window.mucId);
      }
   }

//    function fillNewScyWindowCollaborative(window: ScyWindow, mucid: String): Void {
//        var pleaseWait = PleaseWait { };
//        window.scyContent = pleaseWait;
//        FX.deferAction(function () {
//            // one defer does not seem to be enough to show the please wait content
//            FX.deferAction(function () {
//                realFillNewScyWindow2(window, true);
//                def toolNode: Node = window.scyContent;
//                if (toolNode instanceof CollaborationStartable) {
//                    (toolNode as CollaborationStartable).startCollaboration(mucid);
//                }
//            });
//        });
//    }
   function realFillNewScyWindow2(window: ScyWindow, collaboration: Boolean): Void {
      logger.info("realFillNewScyWindow2({window.eloUri},{collaboration})");
      def eloConfig = eloConfigManager.getEloToolConfig(window.eloType);
      if (eloConfig == null) {
         logger.error("Can't find eloConfig for {window.eloUri} of type {window.eloType}");
         return;
      }
      // don't place the window content tool in the window, let the please wait message stay until every thing is created
      def scyToolsList = ScyToolsList {
         }
      // create the tools
      scyToolsList.actionLoggerTool = ScyToolActionLogger {
            window: window;
            config: config
            missionRuntimeEloUri: missionRunConfigs.missionRuntimeElo.getUri()
         };
      if (not collaboration and eloConfig.isContentCollaboration()) {
         // currently, the content tool must be created on the first call, which is with collaboration false
         // otherwise the first set of tools are not informed of the elo load messages
         throw new IllegalStateException("the content tool may not be a collaboration only tool");
      }
      if (eloConfig.isContentCollaboration() == collaboration) {
         scyToolsList.windowContentTool = scyToolFactory.createNewScyToolNode(eloConfig.getContentCreatorId(), window.eloType, window.eloUri, window, false);
      }
      if (eloConfig.isTopDrawerCollaboration() == collaboration) {
         scyToolsList.topDrawerTool = scyToolFactory.createNewScyToolNode(eloConfig.getTopDrawerCreatorId(), window.eloType, window.eloUri, window, true);
      }
      if (eloConfig.isRightDrawerCollaboration() == collaboration) {
         scyToolsList.rightDrawerTool = scyToolFactory.createNewScyToolNode(eloConfig.getRightDrawerCreatorId(), window.eloType, window.eloUri, window, true);
      }
      if (eloConfig.isBottomDrawerCollaboration() == collaboration) {
         scyToolsList.bottomDrawerTool = scyToolFactory.createNewScyToolNode(eloConfig.getBottomDrawerCreatorId(), window.eloType, window.eloUri, window, true);
      }
      if (eloConfig.isLeftDrawerCollaboration() == collaboration) {
         scyToolsList.leftDrawerTool = scyToolFactory.createNewScyToolNode(eloConfig.getLeftDrawerCreatorId(), window.eloType, window.eloUri, window, true);
      }

      // all tools are created and placed in the window
      // now do the ScyTool initialisation
      def myEloChanged = SimpleMyEloChanged {
            window: window;
            titleKey: config.getTitleKey()
            technicalFormatKey: config.getTechnicalFormatKey();
         }
      var functionalRoles: EloFunctionalRole[];
      if (eloConfig.getEloFunctionalRoles() != null) {
         functionalRoles = for (object in eloConfig.getEloFunctionalRoles()) {
               object as EloFunctionalRole
            }
      }

      def myEloSaver = SimpleScyDesktopEloSaver {
            config: config
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
      scyToolsList.initialize();
      scyToolsList.postInitialize();
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
      if (scyToolsList.leftDrawerTool != null) {
         window.leftDrawerTool = scyToolsList.leftDrawerTool;
         if (collaboration) {
            window.openDrawer("left");
         }
      }
      // if the window content tool is defined, meaning a new or existing elo is loaded, report this
      if (window.scyContent != null) {
         if (window.eloUri != null) {
            scyToolsList.loadElo(window.eloUri);
         } else {
            scyToolsList.newElo();
         }
         scyToolsList.loadedEloChanged(window.eloUri);
      }
   }

   public override function processNotification(notification: INotification): Void {
      FX.deferAction(function() {

      });
   }

   function scyDesktopShutdownAction(): Void {
      println("Scy desktop is shutting down....");
      saveAll();
      closeIfPossible(config.getToolBrokerAPI(), "tool broker");
   }

   function saveAll() {
      for (window in windows.getScyWindows()) {
         if (window.eloUri != null) {
            try {
               window.scyToolsList.onQuit();
            }
            catch (e: Exception) {
               logger.error("an exception occured during the aboutToClose of {window.eloUri}", e);
            }
         }
      }
      try {
         missionModelFX.updateElo();
      }
      catch (e: Exception) {
         logger.error("an exception occured during the update of mission map model elo", e);
      }
   }

   function closeIfPossible(object: Object, label: String): Void {
      if (object instanceof Closeable) {
         try {
            (object as Closeable).close();
         }
         catch (e: Exception) {
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
   var newScyWindowTool = NewScyWindowTool {
         repository: config.getRepository();
         titleKey: config.getTitleKey();
         technicalFormatKey: config.getTechnicalFormatKey();
      }
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
         eloInfoControl: DummyEloInfoControl {
         };
         windowStyler: DummyWindowStyler {
         };
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
   newScyWindowTool.scyDesktop = scyDesktop;
   scyDesktop.bottomLeftCornerTool = newScyWindowTool;
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

/*
 * ScyDesktop.fx
 *
 * Created on 26-jun-2009, 12:15:46
 */
package eu.scy.client.desktop.scydesktop;

import eu.scy.client.desktop.scydesktop.scywindows.WindowManager;
import javafx.scene.CustomNode;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindowControl;
import eu.scy.client.desktop.scydesktop.scywindows.WindowStyler;
import eu.scy.client.desktop.scydesktop.tools.corner.missionmap.MissionModelFX;
import eu.scy.client.desktop.scydesktop.tools.corner.missionmap.MissionMap;
import javafx.scene.Scene;
import javafx.stage.Stage;
import eu.scy.client.desktop.scydesktop.utils.log4j.InitLog4JFX;
import eu.scy.client.desktop.scydesktop.scywindows.scydesktop.EloInfoControl;
import eu.scy.client.desktop.scydesktop.elofactory.WindowContentCreatorRegistryFX;
import eu.scy.client.desktop.scydesktop.dummy.DummyEloInfoControl;
import eu.scy.client.desktop.scydesktop.dummy.DummyWindowStyler;
import eu.scy.client.desktop.scydesktop.corners.Corner;
import javafx.scene.control.Button;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import eu.scy.client.desktop.scydesktop.scywindows.window.StandardScyWindow;
import eu.scy.client.desktop.scydesktop.config.Config;
import eu.scy.client.desktop.scydesktop.config.SpringConfigFactory;
import eu.scy.client.desktop.scydesktop.elofactory.NewEloCreationRegistry;
import eu.scy.client.desktop.scydesktop.corners.tools.NewScyWindowTool;
import eu.scy.client.desktop.scydesktop.elofactory.DrawerContentCreatorRegistryFX;
import eu.scy.client.desktop.scydesktop.scywindows.window.MouseBlocker;
import eu.scy.client.desktop.scydesktop.scywindows.EloSavedActionHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import eu.scy.client.desktop.scydesktop.corners.BottomLeftCorner;
import eu.scy.client.desktop.scydesktop.corners.BottomRightCorner;
import eu.scy.client.desktop.scydesktop.corners.TopLeftCorner;
import eu.scy.client.desktop.scydesktop.corners.TopRightCorner;
import eu.scy.client.desktop.scydesktop.hacks.RepositoryWrapper;
import eu.scy.client.desktop.scydesktop.scywindows.scydesktop.WindowManagerImpl;
import java.lang.IllegalArgumentException;
import java.lang.Integer;
import java.lang.Object;
import java.lang.String;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import eu.scy.client.desktop.scydesktop.utils.RedirectSystemStreams;
import eu.scy.client.desktop.scydesktop.scywindows.scydesktop.ScyWindowControlImpl;
import eu.scy.client.desktop.scydesktop.scywindows.WindowPositioner;
import eu.scy.client.desktop.scydesktop.scywindows.window_positions.SimpleWindowPositioner;
import javafx.scene.shape.Rectangle;
import eu.scy.client.desktop.scydesktop.utils.log4j.Logger;
import eu.scy.client.desktop.scydesktop.tooltips.impl.SimpleTooltipManager;
import eu.scy.client.desktop.scydesktop.scywindows.NewTitleGenerator;
import eu.scy.client.desktop.scydesktop.scywindows.scydesktop.NumberedNewTitleGenerator;
import eu.scy.client.desktop.scydesktop.draganddrop.DragAndDropManager;
import eu.scy.client.desktop.scydesktop.draganddrop.impl.SimpleDragAndDropManager;
import eu.scy.client.desktop.scydesktop.tooltips.TooltipManager;
import eu.scy.client.desktop.scydesktop.elofactory.impl.ScyToolFactory;
import eu.scy.client.desktop.scydesktop.elofactory.ScyToolCreatorRegistryFX;
import eu.scy.client.desktop.scydesktop.scywindows.scydesktop.SimpleMyEloChanged;
import eu.scy.client.desktop.scydesktop.scywindows.scydesktop.ScyDesktopEloSaver;
import eu.scy.client.desktop.scydesktop.tools.corner.contactlist.Contact;
import eu.scy.client.desktop.scydesktop.tools.corner.contactlist.OnlineState;
import eu.scy.client.desktop.scydesktop.tools.corner.contactlist.ContactList;
import eu.scy.client.desktop.scydesktop.edges.EdgesManager;
import eu.scy.notification.api.INotifiable;
import eu.scy.notification.api.INotification;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import java.net.URI;
import eu.scy.client.desktop.scydesktop.config.EloConfig;
import eu.scy.client.desktop.scydesktop.elofactory.ScyToolCreatorFX;
import eu.scy.client.desktop.scydesktop.tools.ScyTool;
import eu.scy.collaboration.api.CollaborationStartable;
import javax.swing.JOptionPane;


/**
 * @author sikkenj
 */
public class ScyDesktop extends CustomNode, INotifiable {

    def logger = Logger.getLogger(this.getClass());
    public var config: Config;
    def desktopScene = bind scene;
    public var missionModelFX: MissionModelFX = MissionModelFX { };
    public var eloInfoControl: EloInfoControl;
    public var windowStyler: WindowStyler;
    public var scyToolCreatorRegistryFX: ScyToolCreatorRegistryFX;
    public var newEloCreationRegistry: NewEloCreationRegistry;
    public var windowContentCreatorRegistryFX: WindowContentCreatorRegistryFX;
    public var drawerContentCreatorRegistryFX: DrawerContentCreatorRegistryFX;
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
    def windows: WindowManager = WindowManagerImpl {
                scyDesktop: this
            //       activeAnchor: bind missionModelFX.activeAnchor;
            };
    public def tooltipManager: TooltipManager = SimpleTooltipManager { };
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
    var topLeftCorner: Corner;
    var topRightCorner: Corner;
    var bottomRightCorner: Corner;
    var bottomLeftCorner: Corner;
    var backgroundImage: Image;
    var backgroundImageView: ImageView;

    init {
        if (config.isRedirectSystemStreams() and config.getLoggingDirectory() != null) {
            RedirectSystemStreams.redirect(config.getLoggingDirectory());
        }
//      missionMap.missionModel = missionModelFX;
        scyWindowControl.missionModel = missionModelFX;
        FX.deferAction(initialWindowPositioning);
        FX.deferAction(function () {
            MouseBlocker.initMouseBlocker(scene.stage);
        });
        logger.info("repository class: {config.getRepository().getClass()}");
        if (config.getRepository() instanceof RepositoryWrapper) {
            var repositoryWrapper = config.getRepository() as RepositoryWrapper;
            var eloSavedActionHandler = EloSavedActionHandler {
                        scyWindowControl: this.scyWindowControl;
                    }
            repositoryWrapper.addEloSavedListener(eloSavedActionHandler);
            repositoryWrapper.setMissionId(missionModelFX.id);
            repositoryWrapper.setUserId(config.getToolBrokerAPI().getLoginUserName());
            logger.info("Added eloSavedActionHandler as EloSavedListener to the repositoryWrapper");
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
            newTitleGenerator: newTitleGenerator;
        }
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
                    showOfflineContacts: true
                    width: 300
                };
        contactList.height = 250;
        missionMap = MissionMap {
            missionModel: missionModelFX
            tooltipManager: tooltipManager
            dragAndDropManager: dragAndDropManager
            scyDesktop: this
            metadataTypeManager: config.getMetadataTypeManager()
        //         translateX:40;
        //         translateY:40;
        }
        missionMap.scyWindowControl = scyWindowControl;
        topLeftCorner = TopLeftCorner {
            content: contactList;
            color: Color.RED;
        }
        topRightCorner = TopRightCorner {
            content: topRightCornerTool;
            color: Color.GREEN;
        }
        bottomRightCorner = BottomRightCorner {
            // TODO, replace with specified tool
            content: missionMap;
            color: Color.BLUE;
        }
        bottomLeftCorner = BottomLeftCorner {
            content: bottomLeftCornerTool;
            color: Color.GRAY;
        }

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
      def contactList:ContactList = ContactList {
              columns:2
              contacts: contactContent
              dragAndDropManager:dragAndDropManager
              tooltipManager:tooltipManager
              scyDesktop:this
              height:250
              showOfflineContacts:true
              width: 300
              };

      contactList.height = 250;

      missionMap = MissionMap{
         missionModel: missionModelFX
         tooltipManager:tooltipManager
         dragAndDropManager:dragAndDropManager
         scyDesktop:this
         metadataTypeManager:config.getMetadataTypeManager()
//         translateX:40;
//         translateY:40;
      }
      missionMap.scyWindowControl=scyWindowControl;

      topLeftCorner = TopLeftCorner{
         content:contactList;
         color:Color.RED;
      }


      var SPTButton = ImageButton {

                toolTip: "Open the Student Planning Tool!";
                tooltipManager: tooltipManager;
                normalImage: Image { url: "{__DIR__}planningtoolicon.png" };
                selectImage: Image { url: "{__DIR__}planningtooliconhighlight.png" };

                onMouseClicked: function(e) {
                    println("ouch u clicked my tool!")
                }
        }


      topRightCorner = TopRightCorner{
         content:  SPTButton;
     
      }
      bottomRightCorner = BottomRightCorner{
         // TODO, replace with specified tool
         content:missionMap;
         color:Color.BLUE;
      }
      bottomLeftCorner = BottomLeftCorner{
         content:bottomLeftCornerTool;
         color:Color.GRAY;
      }
      windowPositioner = SimpleWindowPositioner{
          forbiddenNodes:[
               topLeftCorner,
               topRightCorner,
               bottomRightCorner,
               bottomLeftCorner
            ];
            width: bind scene.width;
            height: bind scene.height;
        }
        scyWindowControl = ScyWindowControlImpl {
            windowContentFactory: scyToolFactory;
            windowManager: windows;
            windowPositioner: windowPositioner;
            missionModel: missionModelFX;
            missionMap: missionMap;
            eloInfoControl: eloInfoControl;
            windowStyler: windowStyler;
            extensionManager: config.getExtensionManager();
            repository: config.getRepository();
            metadataTypeManager: config.getMetadataTypeManager();
            setScyContent: fillNewScyWindow2;
            tooltipManager: tooltipManager
            dragAndDropManager: dragAndDropManager
            repositoryWrapper: if (config.getRepository() instanceof RepositoryWrapper) config.getRepository() as RepositoryWrapper else null;
        }
    }

    var edgesManager: EdgesManager = EdgesManager { }

    public override function create(): Node {
        logger.info("create");
        checkProperties();
        createElements();
        Group {
            content: [
                ///Testing only
                //            Rectangle{width:bind boundsInLocal.width,
                //                height:bind boundsInLocal.height,
                //                fill:Color.BLACK
                //            },
                backgroundImageView,
                edgesManager,
                windows.scyWindows,
                topLeftCorner,
                topRightCorner,
                bottomRightCorner,
                bottomLeftCorner,
                Rectangle { fill: Color.BLACK, x: 100, y: 100, width: boundsInLocal.width, height: boundsInLocal.height },
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
    }

    function fillNewScyWindow2(window: ScyWindow): Void {
        var pleaseWait = PleaseWait { };
        window.scyContent = pleaseWait;
        FX.deferAction(function () {
            // one defer does not seem to be enough to show the please wait content
            FX.deferAction(function () {
                realFillNewScyWindow2(window);
            });
        });
    }

    public override function processNotification(notification: INotification): Void {
        def notificationType: String = notification.getFirstProperty("type");
        if (not (notificationType == null)) {
            if (notificationType == "collaboration_request") {
                logger.debug("********************collaboration_request*************************");
                def user: String = notification.getFirstProperty("proposing_user");
                //XXX submit user-nickname instead of extracting it
                def userNickname = user.substring(0, user.indexOf("@"));
                def eloUri: String = notification.getFirstProperty("elo");
                def option = JOptionPane.showConfirmDialog(null, "{userNickname} wants to start a collaboration with you. Accept?", "Confirm", JOptionPane.OK_CANCEL_OPTION);
                if (option == JOptionPane.OK_OPTION){
                    config.getToolBrokerAPI().answerCollaborationProposal(true,user,eloUri);
                } else if (option == JOptionPane.NO_OPTION){
                    config.getToolBrokerAPI().answerCollaborationProposal(false,user,eloUri);
                }
            } else if (notificationType == "collaboration_response") {
                logger.debug("********************collaboration_response*************************");
                def accepted: String = notification.getFirstProperty("request_accepted");
                def eloUri: String = notification.getFirstProperty("elo");
                if (accepted == "true") {
                    JOptionPane.showConfirmDialog(null, "Your request for collaboration is accepted!", "Info", JOptionPane.OK_OPTION);
                    def mucid: String = notification.getFirstProperty("mucid");
                    def collaborationWindow: ScyWindow = scyWindowControl.windowManager.findScyWindow(new URI(eloUri));
                    def toolNode: Node = collaborationWindow.scyContent;
                    if (toolNode instanceof CollaborationStartable) {
                        (toolNode as CollaborationStartable).startCollaboration(mucid);
                        }
                    } else {
                        JOptionPane.showConfirmDialog(null, "Your request for collaboration was not accepted!", "Info", JOptionPane.OK_OPTION);
                        logger.debug("collaboration not accepted");
                    }
            }
        }
    }

    function realFillNewScyWindow2(window: ScyWindow): Void {
        var eloConfig = config.getEloConfig(window.eloType);
        if (eloConfig == null) {
            logger.error("Can't find eloConfig for {window.eloUri} of type {window.eloType}");
            return ;
        }
        // don't place the window content tool in the window, let the please wait message stay until every thing is created
        var scyContent = scyToolFactory.createNewScyToolNode(eloConfig.getContentCreatorId(), window.eloType, window.eloUri, window, false);
        window.topDrawerTool = scyToolFactory.createNewScyToolNode(eloConfig.getTopDrawerCreatorId(), window.eloType, window.eloUri, window, true);
        window.rightDrawerTool = scyToolFactory.createNewScyToolNode(eloConfig.getRightDrawerCreatorId(), window.eloType, window.eloUri, window, true);
        window.bottomDrawerTool = scyToolFactory.createNewScyToolNode(eloConfig.getBottomDrawerCreatorId(), window.eloType, window.eloUri, window, true);
        window.leftDrawerTool = scyToolFactory.createNewScyToolNode(eloConfig.getLeftDrawerCreatorId(), window.eloType, window.eloUri, window, true);
        window.scyContent = scyContent;
        // all tools are created and placed in the window
        // now do the ScyTool initialisation
        var myEloChanged = SimpleMyEloChanged {
                    window: window;
                    titleKey: config.getTitleKey()
                    technicalFormatKey: config.getTechnicalFormatKey();
                }
        var optionPaneEloSaver = ScyDesktopEloSaver {
                    config: config
                    repository: config.getRepository()
                    eloFactory: config.getEloFactory()
                    titleKey: config.getTitleKey()
                    technicalFormatKey: config.getTechnicalFormatKey()
                    window: window;
                    myEloChanged: myEloChanged;
                    newTitleGenerator: newTitleGenerator
                    windowStyler: windowStyler
                };
        window.scyToolsList.setEloSaver(optionPaneEloSaver);
        window.scyToolsList.setMyEloChanged(myEloChanged);
        window.scyToolsList.initialize();
        window.scyToolsList.postInitialize();
        if (window.eloUri != null) {
            window.scyToolsList.loadElo(window.eloUri);
        } else {
            window.scyToolsList.newElo();
        }
        window.scyToolsList.loadedEloChanged(window.eloUri);
    }

//   function XfillNewScyWindow(window: ScyWindow):Void{
//      var eloConfig = config.getEloConfig(window.eloType);
//      if (window.eloUri==null){
//         var pleaseWait = Text {
//               font : Font {
//                  size: 14
//               }
//               x: 5, y: 20
//               content: "Loading, please wait..."
//            }
//         window.scyContent = pleaseWait;
//
//         FX.deferAction(function(){
//               windowContentFactory.fillWindowContent(window,eloConfig.getContentCreatorId());
//            });
//      }
//      else{
//         windowContentFactory.fillWindowContent(window.eloUri,window,eloConfig.getContentCreatorId());
//      }
//      addDrawerTools(window,eloConfig);
//   }
//
//    function addDrawerTools(window:ScyWindow,eloConfig:EloConfig):Void{
//       if (window.eloUri==null){
//          // no elo, no drawer tools
//          //return;
//       }
//       if (window.eloType==null){
//
//       }
////       println("retrieving eloConfig for type {window.eloType}");
//       if (eloConfig==null){
//          //return;
//       }
//       window.topDrawerTool = drawerContentFactory.createDrawerTool(eloConfig.getTopDrawerCreatorId(), window);
//       window.rightDrawerTool = drawerContentFactory.createDrawerTool(eloConfig.getRightDrawerCreatorId(), window);
//       window.bottomDrawerTool = drawerContentFactory.createDrawerTool(eloConfig.getBottomDrawerCreatorId(), window);
//       window.leftDrawerTool = drawerContentFactory.createDrawerTool(eloConfig.getLeftDrawerCreatorId(), window);
//    }
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
                action: function () {
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

    scyDesktop.newEloCreationRegistry.registerEloCreation("test", "test");
    scyDesktop.newEloCreationRegistry.registerEloCreation("tst", "tst");
    scyDesktop.newEloCreationRegistry.registerEloCreation("size", "Size test");
    Stage {
        title: "ScyDestop Test"
        scene: Scene {
            width: 800
            height: 600
            content: [
                scyDesktop
            ]
        }
    }

}

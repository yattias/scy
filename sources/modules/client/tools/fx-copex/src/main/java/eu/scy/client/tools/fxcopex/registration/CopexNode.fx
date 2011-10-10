/*
 * CopexNode.fx
 *
 * Created on 13 janv. 2010, 17:20:24
 */

package eu.scy.client.tools.fxcopex.registration;

import java.net.URI;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.CustomNode;
import javafx.scene.layout.Resizable;
import javafx.scene.layout.Container;

import eu.scy.client.desktop.scydesktop.tools.ScyToolFX;
import eu.scy.client.desktop.scydesktop.tools.EloSaverCallBack;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import eu.scy.client.desktop.scydesktop.ScyToolActionLogger;
import java.util.logging.Logger;
import eu.scy.client.desktop.desktoputils.jdom.JDomStringConversion;
import eu.scy.client.desktop.scydesktop.swingwrapper.ScySwingWrapper;
import eu.scy.client.desktop.desktoputils.UiUtils;
import eu.scy.client.common.scyi18n.ResourceBundleWrapper;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import eu.scy.notification.api.INotifiable;
import eu.scy.notification.api.INotification;
import roolo.api.IRepository;
import roolo.elo.api.IELOFactory;
import roolo.elo.api.IMetadataTypeManager;
import roolo.elo.api.IELO;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import java.awt.image.BufferedImage;
import java.awt.Dimension;
import eu.scy.client.desktop.scydesktop.imagewindowstyler.ImageWindowStyler;
import eu.scy.client.desktop.desktoputils.art.WindowColorScheme;
import eu.scy.client.desktop.scydesktop.corners.elomanagement.ModalDialogNode;
import eu.scy.client.desktop.desktoputils.i18n.Composer;
import eu.scy.client.desktop.scydesktop.scywindows.scydesktop.ModalDialogBox;
import eu.scy.client.desktop.desktoputils.EmptyBorderNode;
import roolo.elo.metadata.BasicMetadata;
import eu.scy.client.desktop.scydesktop.scywindows.window.StandardScyWindow;
import eu.scy.common.scyelo.ScyElo;
import eu.scy.client.desktop.scydesktop.tools.TitleBarButton;
import eu.scy.client.desktop.scydesktop.tools.TitleBarButtonManager;
import javafx.util.StringLocalizer;
import eu.scy.client.desktop.scydesktop.tools.corner.contactlist.ContactFrame;
import eu.scy.collaboration.api.CollaborationStartable;
import eu.scy.client.common.datasync.ISyncSession;


/**
 * copex node,
 * output elos: xproc, experimental procedure
 * accepts dnd from rich text editor, that allows to fill automaticaly the hypothesis or the question part of the procedure,
 * depends of the functional role of the text elo (hypothesis or research question)
 * the notification button allows to show to the user the notification of the SCY Hypothesis evaluation agent
 * @author Marjolaine
 */

public class CopexNode extends CustomNode, Resizable, ScyToolFX, EloSaverCallBack, INotifiable, CollaborationStartable {
   def logger = Logger.getLogger(CopexNode.class.getName());
   def scyCopexType = "scy/xproc";
   def jdomStringConversion = new JDomStringConversion();

   public-init var scyCopexPanel:ScyCopexPanel;
   public-init var scyWindow: ScyWindow;
   public var eloFactory:IELOFactory;
   public var metadataTypeManager: IMetadataTypeManager;
   public var repository:IRepository;
   public var toolBrokerAPI: ToolBrokerAPI;

   public override var width on replace {resizeContent()};
   public override var height on replace {resizeContent()};

   var wrappedCopexPanel:Node;
   var technicalFormatKey: IMetadataKey;
   var collaborative: Boolean = false;
   var elo:IELO;

   def bundle:ResourceBundleWrapper = new ResourceBundleWrapper(this);;

   var notificationDialog: NotificationDialog;
   // manager of the title bar to show/hide the notification button
   var copexTitleBarButtonManager:TitleBarButtonManager;
   def saveTitleBarButton = TitleBarButton {
              actionId: TitleBarButton.saveActionId
              action: doSaveElo
           }
   def saveAsTitleBarButton = TitleBarButton {
              actionId: TitleBarButton.saveAsActionId
              action: doSaveAsElo
           }

   def exportToPdfTitleBarButton = TitleBarButton {
              actionId: "exportToPdf"
              iconType: "export"
              action: exportToPdf
              tooltip:bundle.getString("FX-COPEX.EXPORT_TO_PDF_TOOLTIP")
           }

   def notificationTitleBarButton = TitleBarButton {
	  actionId: "notify"
	  iconType: "information2"
	  action: doNotify
	  tooltip: getBundleString("FX-COPEX.TOOLTIP_NOTIFICATION");
    }

   public override function initialize(windowContent: Boolean):Void{
       metadataTypeManager = toolBrokerAPI.getMetaDataTypeManager();
       repository = toolBrokerAPI.getRepository();
       eloFactory = toolBrokerAPI.getELOFactory();

      technicalFormatKey = metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.TECHNICAL_FORMAT);
      scyCopexPanel.setTBI(toolBrokerAPI);
      scyCopexPanel.setEloUri((scyWindow.scyToolsList.actionLoggerTool as ScyToolActionLogger).getURI());
      scyCopexPanel.initActionLogger();
      scyCopexPanel.initCopex();
      //toolBrokerAPI.registerForNotifications(this as INotifiable);
   }

   public override function setTitleBarButtonManager(titleBarButtonManager: TitleBarButtonManager, windowContent: Boolean): Void {
      copexTitleBarButtonManager = titleBarButtonManager;
      if (windowContent) {
         titleBarButtonManager.titleBarButtons = [
                    saveTitleBarButton,
                    saveAsTitleBarButton,
                    exportToPdfTitleBarButton
                 ]
      }
   }

   // show the notification button
   function addNotificationInTitleBar(){
    copexTitleBarButtonManager.titleBarButtons = [
                    saveTitleBarButton,
                    saveAsTitleBarButton,
                    notificationTitleBarButton
                    exportToPdfTitleBarButton
                 ]
   }

   //hide the notification button
   function removeNotificationInTitleBar(){
    copexTitleBarButtonManager.titleBarButtons = [
                    saveTitleBarButton,
                    saveAsTitleBarButton
                    exportToPdfTitleBarButton
                 ]
   }


   public override function loadElo(uri:URI){
      doLoadElo(uri);
   }

   public override function create(): Node {
//      bundle = new ResourceBundleWrapper(this);
      wrappedCopexPanel = ScySwingWrapper.wrap(scyCopexPanel);
   }

   function doLoadElo(eloUri:URI)
   {
      logger.info("Trying to load elo {eloUri}");
      var newElo = repository.retrieveELO(eloUri);
      if (newElo != null)
      {
         scyCopexPanel.setEloUri(eloUri.toString());
         scyCopexPanel.loadELO(newElo.getContent().getXmlString());
         logger.info("elo loaded");
         elo = newElo;
      }
   }

   function doSaveElo(){
      eloSaver.eloUpdate(getElo(),this);
      scyCopexPanel.setEloUri(elo.getUri().toString());
   }

   function doSaveAsElo(){
      eloSaver.eloSaveAs(getElo(),this);
   }

   function getElo():IELO{
      if (elo==null){
         elo = eloFactory.createELO();
         elo.getMetadata().getMetadataValueContainer(technicalFormatKey).setValue(scyCopexType);
      }
      elo.getContent().setXmlString(jdomStringConversion.xmlToString(scyCopexPanel.getExperimentalProcedure()));
      return elo;
   }

    override public function eloSaveCancelled (elo : IELO) : Void {
    }

    override public function eloSaved (elo : IELO) : Void {
        this.elo = elo;
        scyCopexPanel.setEloUri(elo.getUri().toString());
    }

    function exportToPdf(){
       scyCopexPanel.exportToPdf();
    }

   function resizeContent(){
      Container.resizeNode(wrappedCopexPanel,width,height);
   }

   public override function getPrefHeight(height: Number) : Number{
      return Container.getNodePrefHeight(wrappedCopexPanel, height);
   }

   public override function getPrefWidth(width: Number) : Number{
      return Container.getNodePrefWidth(wrappedCopexPanel, width);
   }


   public override function getMinHeight() : Number{
      return 350;
   }

   public override function getMinWidth() : Number{
      return 550;
   }

   public function getBundleString(key:String) : String{
       return bundle.getString(key);
   }

   /* returns the thumbnail of copexe*/
   public override function getThumbnail(width: Integer, height: Integer): BufferedImage {
      if (scyCopexPanel != null) {
        return UiUtils.createThumbnail(scyCopexPanel.getInterfacePanel(), scyCopexPanel.getRealSize(), new Dimension(width, height));
      } else {
        return null;
      }
    }

    public function testThumbnail(): Void {
        var thumbnail = getThumbnail(64, 64);
        var icon = new ImageIcon(thumbnail);
        JOptionPane.showMessageDialog(null,
            "Look at this!",
            "thumbnail test",
            JOptionPane.INFORMATION_MESSAGE,
            icon);
    }

    public override function onQuit():Void{
      if (elo!=null){
         def oldContentXml = elo.getContent().getXmlString();
         def newContentXml = getElo().getContent().getXmlString();
         if (oldContentXml==newContentXml){
            // nothing changed
            return;
         }
      }
      doSaveElo();
   }

   /* received a notification*/
   override public function processNotification(note: INotification): Boolean {
        var success: Boolean = false;
        if (scyCopexPanel != null and note.getFirstProperty(CopexNotificationManager.keyMessage) != null) {
            success = true;
            logger.info("process notification, forwarding to copex");
            addNotificationInTitleBar();
            FX.deferAction(function (): Void {
                scyCopexPanel.processNotification(note);
            });
        } else {
            logger.info("notification not processed, copex == null or notification-message == null");
        }
        return success;
    }

    /*opens the notification dialog*/
    function doNotify(){
        notificationDialog = NotificationDialog {
            okayAction: okayNotificationDialog
            cancelAction: cancelNotificationDialog
            bundle:bundle
            notificationText:getNotification();
        }
        createModalDialog(scyWindow.windowManager.scyDesktop.windowStyler.getWindowColorScheme(ImageWindowStyler.generalNew), getBundleString("FX-COPEX.NOTIFICATION_TITLE"), notificationDialog);
    }

    function createModalDialog(windowColorScheme: WindowColorScheme, title: String, modalDialogNode: ModalDialogNode): Void {
        Composer.localizeDesign(modalDialogNode.getContentNodes(), StringLocalizer{});
        modalDialogNode.modalDialogBox = ModalDialogBox {
            content: EmptyBorderNode {
                content: Group {
                    content: modalDialogNode.getContentNodes();
                }
            }
            targetScene: scyWindow.windowManager.scyDesktop.scene
            title: title
            windowColorScheme: windowColorScheme
            closeAction: function (): Void {
            }
        }
    }

    function cancelNotificationDialog(): Void {
        notificationDialog.modalDialogBox.close();
        removeNotificationInTitleBar();
        FX.deferAction(function () {
            scyCopexPanel.keepNotification(true);
        })
    }

    function okayNotificationDialog(): Void {
        notificationDialog.modalDialogBox.close();
        removeNotificationInTitleBar();
        FX.deferAction(function () {
            scyCopexPanel.keepNotification(false);
        })
    }

    function getNotification(): String{
        if(scyCopexPanel == null){
            return getBundleString("FX-COPEX.MSG_ERROR_NOTIFICATION");
        }else{
            return scyCopexPanel.getNotification();
        }
    }

    // copex accepts drop from rich text editor
    public override function canAcceptDrop(object: Object): Boolean {
        logger.info("copex-canAcceptDrop? {object.getClass()}");
        if (object instanceof BasicMetadata) {
            var b: BasicMetadata = object as BasicMetadata;
            var textElo = repository.retrieveELO(getEloUri(b));
            var scyElo = new ScyElo(textElo, toolBrokerAPI);
            var technicalFormat = scyElo.getTechnicalFormat();
            return technicalFormat != null and technicalFormat.equals("scy/rtf");
        }
        if (object instanceof StandardScyWindow) {
            var w: StandardScyWindow = object as StandardScyWindow;
            var textElo = repository.retrieveELO(w.eloUri);
            var scyElo = new ScyElo(textElo, toolBrokerAPI);
            var technicalFormat = scyElo.getTechnicalFormat();
            return technicalFormat != null and technicalFormat.equals("scy/rtf");
        }
        // collaboration
//        if (object instanceof ContactFrame) {
//            var c: ContactFrame = object as ContactFrame;
//            if (not scyWindow.ownershipManager.isOwner(c.contact.name)) {// condition to remove as soon as the user can stop the collaboration, for now, the collaboration is autoreestablished
//                return true;
//            }
//        }
        return false;
    }

    public override function acceptDrop(object: Object) {
        logger.info("drop accepted.");
        if(object instanceof ContactFrame){
            var c: ContactFrame = object as ContactFrame;
            logger.info("acceptDrop user: {c.contact.name}");
            scyWindow.ownershipManager.addPendingOwner(c.contact.name);
            scyWindow.windowManager.scyDesktop.config.getToolBrokerAPI().proposeCollaborationWith("{c.contact.awarenessUser.getJid()}", scyWindow.eloUri.toString(), scyWindow.mucId);
            return;
        }
        if (object instanceof BasicMetadata) {
            var b: BasicMetadata = object as BasicMetadata;
            var textElo = repository.retrieveELO(getEloUri(b));
            scyCopexPanel.acceptDrop(textElo);
            return;
        }
        if (object instanceof StandardScyWindow) {
            var w: StandardScyWindow = object as StandardScyWindow;
            var textElo = repository.retrieveELO(w.eloUri);
            scyCopexPanel.acceptDrop(textElo);
            return;
        }
        
    }

    function getEloUri(object: BasicMetadata): URI{
        def identifierKey = toolBrokerAPI.getMetaDataTypeManager().getMetadataKey(CoreRooloMetadataKeyIds.IDENTIFIER);
        return object.getMetadataValueContainer(identifierKey).getValue() as URI;
   }

   // start Collaboration
   public override function startCollaboration(mucid: String) {
        if (not collaborative) {
            collaborative = true;
            FX.deferAction(function(): Void {
                def session : ISyncSession = scyCopexPanel.joinSession(mucid);
                session.addCollaboratorStatusListener(scyWindow.ownershipManager);
                session.refreshOnlineCollaborators();
                scyCopexPanel.startCollaboration();
                logger.info("joined session, mucid: {mucid}");
            });
        }
    }

    public override function stopCollaboration() : Void {
        if (collaborative) {
            collaborative = false;
            FX.deferAction(function(): Void {
                scyCopexPanel.leaveSession(null);
            });
        }

    }


    /**
    * Set the read only mode.
    */
   public override function setReadOnly(readOnly: Boolean){
       if(scyCopexPanel != null){
            scyCopexPanel.setReadOnly(readOnly);
       }
   }


}

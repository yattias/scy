package eu.scy.client.tools.fxfitex.registration;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.CustomNode;
import javafx.scene.layout.Resizable;
import javafx.scene.layout.Container;

import eu.scy.client.desktop.scydesktop.tools.ScyToolFX;
import eu.scy.client.desktop.scydesktop.ScyToolActionLogger;
import eu.scy.client.desktop.scydesktop.tools.EloSaverCallBack;
import java.util.logging.Logger;
import eu.scy.client.desktop.desktoputils.jdom.JDomStringConversion;
import eu.scy.client.desktop.scydesktop.scywindows.scydesktop.ModalDialogBox;
import eu.scy.client.desktop.scydesktop.corners.elomanagement.ModalDialogNode;
import eu.scy.client.desktop.scydesktop.scywindows.DatasyncAttribute;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import eu.scy.client.desktop.scydesktop.swingwrapper.ScySwingWrapper;
import eu.scy.client.desktop.desktoputils.art.WindowColorScheme;
import eu.scy.client.desktop.scydesktop.imagewindowstyler.ImageWindowStyler;
import eu.scy.client.desktop.desktoputils.EmptyBorderNode;
import eu.scy.client.desktop.desktoputils.i18n.Composer;
import eu.scy.client.desktop.scydesktop.scywindows.scydesktop.AcceptSyncModalDialog;
import eu.scy.client.desktop.scydesktop.edges.DatasyncEdge;
import eu.scy.client.desktop.desktoputils.UiUtils;
import eu.scy.client.common.datasync.ISynchronizable;
import eu.scy.client.common.datasync.DummySyncListener;
import eu.scy.client.common.scyi18n.ResourceBundleWrapper;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import roolo.api.IRepository;
import roolo.elo.api.IELOFactory;
import roolo.elo.api.IMetadataTypeManager;
import roolo.elo.api.IELO;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;

import java.net.URI;
import org.jdom.Element;
import javax.swing.JOptionPane;
import javax.swing.ImageIcon;
import java.awt.image.BufferedImage;
import java.awt.Dimension;
import eu.scy.client.desktop.scydesktop.tools.TitleBarButton;
import eu.scy.client.desktop.scydesktop.tools.TitleBarButtonManager;
import eu.scy.client.desktop.scydesktop.tools.corner.contactlist.ContactFrame;
import eu.scy.collaboration.api.CollaborationStartable;
import eu.scy.client.common.datasync.ISyncSession;
import roolo.elo.metadata.BasicMetadata;
import eu.scy.client.desktop.scydesktop.scywindows.window.StandardScyWindow;
import eu.scy.common.scyelo.ScyElo;
import javafx.util.StringLocalizer;

/**
 * fitex node
 * output elos: pds, processed dataset elos
 * accepts dnd from other fitex node, in order to merge 2 datasets
 * sync. with SCYSimulator, by dnd the "cable", dataset header and dataset rows
 * @author Marjolaine
 */

public class FitexNode extends ISynchronizable, CustomNode, Resizable, ScyToolFX, EloSaverCallBack, CollaborationStartable {
   def logger = Logger.getLogger(FitexNode.class.getName());
   def scyFitexType = "scy/pds";
   def jdomStringConversion = new JDomStringConversion();

   public-init var fitexPanel:FitexPanel;
   public-init var scyWindow: ScyWindow;
   public var eloFactory:IELOFactory;
   public var metadataTypeManager: IMetadataTypeManager;
   public var repository:IRepository;
   public var toolBrokerAPI: ToolBrokerAPI;
   public override var width on replace {resizeContent()};
   public override var height on replace {resizeContent()};
   var wrappedFitexPanel:Node;
   var technicalFormatKey: IMetadataKey;
   var syncAttrib: DatasyncAttribute;
   var datasyncEdge: DatasyncEdge;
   var acceptDialog: AcceptSyncModalDialog;
   var elo:IELO;
   var collaborative: Boolean = false;

   var bundle:ResourceBundleWrapper;
   def saveTitleBarButton = TitleBarButton {
              actionId: TitleBarButton.saveActionId
              action: doSaveElo
           }
   def saveAsTitleBarButton = TitleBarButton {
              actionId: TitleBarButton.saveAsActionId
              action: doSaveAsElo
           }

   // accepts drop if its a simulator or fitex for sync. or ContactFrame for collaboration
   public override function canAcceptDrop(object: Object): Boolean {
       logger.info("fitex-canAcceptDrop? {object.getClass()}");
        //sync.
        if (object instanceof ISynchronizable) {
            if ((object as ISynchronizable).getToolName().equals("simulator")) {
                return true;
            }
        }
        if (object instanceof BasicMetadata) {
            var b: BasicMetadata = object as BasicMetadata;
            var draggedElo = repository.retrieveELO(getEloUri(b));
            var scyElo = new ScyElo(draggedElo, toolBrokerAPI);
            var technicalFormat = scyElo.getTechnicalFormat();
            return technicalFormat != null and (technicalFormat.equals("scy/pds") or technicalFormat.equals("scy/dataset"));
        }
        if (object instanceof StandardScyWindow) {
            var w: StandardScyWindow = object as StandardScyWindow;
            var draggedElo = repository.retrieveELO(w.eloUri);
            var scyElo = new ScyElo(draggedElo, toolBrokerAPI);
            var technicalFormat = scyElo.getTechnicalFormat();
            return technicalFormat != null and (technicalFormat.equals("scy/pds")or technicalFormat.equals("scy/dataset"));
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

    function getEloUri(object: BasicMetadata): URI{
        def identifierKey = toolBrokerAPI.getMetaDataTypeManager().getMetadataKey(CoreRooloMetadataKeyIds.IDENTIFIER);
        return object.getMetadataValueContainer(identifierKey).getValue() as URI;
    }

    public function isDnDSimulator(object: ISynchronizable): Boolean{
        return (object as ISynchronizable).getToolName().equals("simulator");
    }

    public function isDnDFitex(object: ISynchronizable): Boolean{
        return (object as ISynchronizable).getToolName().equals("fitex");
    }

    // drop accepted: synchronizing or merging, depending of the dropped tool, or collaboration
    public override function acceptDrop(object: Object) {
        logger.info("drop accepted.");
        if(object instanceof ContactFrame){
            if (isSynchronizing()){// quit the sync. with the simulator
                leave(null);
            }
            var c: ContactFrame = object as ContactFrame;
            logger.info("acceptDrop user: {c.contact.name}");
            scyWindow.ownershipManager.addPendingOwner(c.contact.name);
            scyWindow.windowManager.scyDesktop.config.getToolBrokerAPI().proposeCollaborationWith("{c.contact.awarenessUser.getJid()}", scyWindow.eloUri.toString(), scyWindow.mucId);
            return;
        }
        if(object instanceof ISynchronizable){
            var isSync = isSynchronizingWith(object as ISynchronizable);
            if (isSync) {
                // remove the sync.
                removeDatasync(object as ISynchronizable);
            }else {
                if(isDnDSimulator(object as ISynchronizable)){
                    acceptDialog = AcceptSyncModalDialog {
                            object: object as ISynchronizable
                            okayAction: initializeDatasync
                            cancelAction: cancelDialog
                        }
                    createModalDialog(scyWindow.windowManager.scyDesktop.windowStyler.getWindowColorScheme(ImageWindowStyler.generalNew), ##"Synchronise?", acceptDialog);
                }
            }
        }
        if (object instanceof BasicMetadata) {
            var b: BasicMetadata = object as BasicMetadata;
            var draggedElo = repository.retrieveELO(getEloUri(b));
            askForMerging(draggedElo);
            return;
        }
        if (object instanceof StandardScyWindow) {
            var w: StandardScyWindow = object as StandardScyWindow;
            var draggedElo = repository.retrieveELO(w.eloUri);
            askForMerging(draggedElo);
            return;
        }
    }

    function askForMerging(draggedElo: IELO):Void{
         var yesNoOptions = [getBundleString("FX-FITEX.YES"), getBundleString("FX-FITEX.NO")];
         var n = -1;
         //var question = getBundleString("FX-FITEX.MSG_MERGE_DATASET");
         if(isSynchronizing()){
            var question = getBundleString("FX-FITEX.MSG_STOP_SYNC_BEFORE_MERGE");
            n = JOptionPane.showOptionDialog( null,
                            question,               // question
                            getBundleString("FX-FITEX.TITLE_DIALOG_MERGE"),           // title
                            JOptionPane.YES_NO_CANCEL_OPTION,
                            JOptionPane.QUESTION_MESSAGE,  // icon
                            null, yesNoOptions,yesNoOptions[0] );
            if (n == 0) {
                leave(null);
                mergeDataset(draggedElo);
            }
         }else{
            //leave(null);
            mergeDataset(draggedElo);
         }
    }


    function cancelDialog(): Void {
            acceptDialog.modalDialogBox.close();
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

   /* return true is fitex is synchronizing with scysimulator with this sessionID*/
   function isSynchronizingWith(simulator : ISynchronizable) : Boolean {
       if(isDnDSimulator(simulator) and simulator.getSessionID() != null and getSessionID() != null and getSessionID().equals(simulator.getSessionID())){
            return true;
       }else{
            return false;
       }
   }

   /* return true is fitex is synchronizing: sessionID not null*/
   function isSynchronizing() : Boolean {
       return getSessionID() != null;
   }

   public function initializeDatasync(simulator: ISynchronizable): Void {
        var datasyncsession = toolBrokerAPI.getDataSyncService().createSession(new DummySyncListener(), "fitex");
        datasyncEdge = scyWindow.windowManager.scyDesktop.edgesManager.addDatasyncLink(scyWindow, simulator.getScyWindow() as ScyWindow);
        this.join(datasyncsession.getId());
        simulator.join(datasyncsession.getId(), datasyncEdge as Object);
        datasyncEdge.join(datasyncsession.getId(), toolBrokerAPI);
        acceptDialog.modalDialogBox.close();
	syncAttrib.setTooltipText("drag to disconnect");
    }

    public function removeDatasync(simulator: ISynchronizable) {
        scyWindow.windowManager.scyDesktop.edgesManager.removeDatasyncLink(datasyncEdge);
        datasyncEdge = null;
        this.leave(simulator.getSessionID());
        simulator.leave(simulator.getSessionID());
	syncAttrib.setTooltipText("drag to connect");
    }

    public override function getScyWindow(): ScyWindow {
        scyWindow;
    }

   public override function join(mucID: String) {
        var s = fitexPanel.joinSession(mucID);
        if(s != null){
            fitexPanel.readAllSyncObjects();
        }
    }

    public override function join(mucID: String, edge: Object) {
        this.datasyncEdge = edge as DatasyncEdge;
        var s = fitexPanel.joinSession(mucID);
        if(s != null){
            fitexPanel.readAllSyncObjects();
        }
    }

    public override function leave(mucID: String) {
        fitexPanel.leaveSession(mucID);
    }

    public override function getSessionID() {
        return fitexPanel.getSessionID();
    }

    public override function getToolName() {
        return "fitex";
    }

    public function mergeDataset(fitexElo: IELO){
        fitexPanel.mergeELO(new JDomStringConversion().stringToXml(fitexElo.getContent().getXmlString()));
    }

    public function getDataset():Element{
        return fitexPanel.getPDS();
    }

    public override function initialize(windowContent: Boolean):Void{
        metadataTypeManager = toolBrokerAPI.getMetaDataTypeManager();
        repository = toolBrokerAPI.getRepository();
        eloFactory = toolBrokerAPI.getELOFactory();
        technicalFormatKey = metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.TECHNICAL_FORMAT);
        fitexPanel.setTBI(toolBrokerAPI);
        fitexPanel.setEloUri((scyWindow.scyToolsList.actionLoggerTool as ScyToolActionLogger).getURI());
        syncAttrib = DatasyncAttribute {
                        scyWindow: scyWindow
                        dragAndDropManager: scyWindow.dragAndDropManager;
                        dragObject: this
                        tooltipManager: scyWindow.tooltipManager
			tooltipText: "drag to connect"
                        };
        insert syncAttrib into scyWindow.scyWindowAttributes;
        fitexPanel.initActionLogger();
        fitexPanel.initFitex();
   }

   public override function setTitleBarButtonManager(titleBarButtonManager: TitleBarButtonManager, windowContent: Boolean): Void {
      if (windowContent) {
         titleBarButtonManager.titleBarButtons = [
                    saveTitleBarButton,
                    saveAsTitleBarButton
                 ]
      }
   }

   public override function loadElo(uri:URI){
      doLoadElo(uri);
   }

   public override function create(): Node {
      bundle = new ResourceBundleWrapper(this);
      wrappedFitexPanel = ScySwingWrapper.wrap(fitexPanel);
   }

   function doLoadElo(eloUri:URI)
   {
      logger.info("Trying to load elo {eloUri}");
      var newElo = repository.retrieveELO(eloUri);
      if (newElo != null)
      {
         fitexPanel.setEloUri(eloUri.toString());
         fitexPanel.loadELO(newElo.getContent().getXmlString());
         logger.info("elo loaded");
         elo = newElo;
      }
   }

   function doSaveElo(){
      eloSaver.eloUpdate(getElo(),this);
      fitexPanel.setEloUri(elo.getUri().toString());
   }

   function doSaveAsElo(){
      eloSaver.eloSaveAs(getElo(),this);
   }

   function getElo():IELO{
      if (elo==null){
         elo = eloFactory.createELO();
         elo.getMetadata().getMetadataValueContainer(technicalFormatKey).setValue(scyFitexType);
      }
      elo.getContent().setXmlString(jdomStringConversion.xmlToString(fitexPanel.getPDS()));
      return elo;
   }

    override public function eloSaveCancelled (elo : IELO) : Void {
    }

    override public function eloSaved (elo : IELO) : Void {
        this.elo = elo;
        fitexPanel.setEloUri(elo.getUri().toString());
    }


   function resizeContent(){
      Container.resizeNode(wrappedFitexPanel,width,height);
   }

   public override function getPrefHeight(height: Number) : Number{
      return Container.getNodePrefHeight(wrappedFitexPanel, height);
   }

   public override function getPrefWidth(width: Number) : Number{
      return Container.getNodePrefWidth(wrappedFitexPanel, width);
   }
   
   public override function getMinHeight() : Number{
      return 300;
   }

   public override function getMinWidth() : Number{
      return 580;
   }

   public function getBundleString(key:String) : String{
       return bundle.getString(key);
   }

   public override function getThumbnail(width: Integer, height: Integer): BufferedImage {
      if (fitexPanel != null) {
        return UiUtils.createThumbnail(fitexPanel.getInterfacePanel(), fitexPanel.getRealSize(), new Dimension(width, height));
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

   // start Collaboration
   public override function startCollaboration(mucid: String) {
        if (not collaborative) {
            collaborative = true;
            FX.deferAction(function(): Void {
                def session : ISyncSession = fitexPanel.joinSession(mucid);
                session.addCollaboratorStatusListener(scyWindow.ownershipManager);
                session.refreshOnlineCollaborators();
                fitexPanel.startCollaboration();
                logger.info("joined session, mucid: {mucid}");
            });
        }
    }

    public override function stopCollaboration() : Void {
        if (collaborative) {
            collaborative = false;
            FX.deferAction(function(): Void {
                fitexPanel.leaveSession(null);
            });
        }
    }


    /**
    * Set the read only mode.
    */
   public override function setReadOnly(readOnly: Boolean){
       if(fitexPanel != null){
            fitexPanel.setReadOnly(readOnly);
       }
   }
   
}

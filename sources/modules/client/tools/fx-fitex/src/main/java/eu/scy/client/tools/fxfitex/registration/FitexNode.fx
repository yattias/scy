package eu.scy.client.tools.fxfitex.registration;

import javafx.scene.Group;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.CustomNode;
import javafx.scene.layout.Resizable;
import javafx.scene.layout.Container;

import eu.scy.client.desktop.scydesktop.tools.ScyToolFX;
import eu.scy.client.desktop.scydesktop.ScyToolActionLogger;
import eu.scy.client.desktop.scydesktop.tools.EloSaverCallBack;
import eu.scy.client.desktop.scydesktop.utils.log4j.Logger;
import eu.scy.client.desktop.scydesktop.utils.jdom.JDomStringConversion;
import eu.scy.client.desktop.scydesktop.scywindows.scydesktop.ModalDialogBox;
import eu.scy.client.desktop.scydesktop.corners.elomanagement.ModalDialogNode;
import eu.scy.client.desktop.scydesktop.scywindows.DatasyncAttribute;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import eu.scy.client.desktop.scydesktop.swingwrapper.ScySwingWrapper;
import eu.scy.client.desktop.scydesktop.art.WindowColorScheme;
import eu.scy.client.desktop.scydesktop.imagewindowstyler.ImageWindowStyler;
import eu.scy.client.desktop.scydesktop.utils.EmptyBorderNode;
import eu.scy.client.desktop.scydesktop.utils.i18n.Composer;
import eu.scy.client.desktop.scydesktop.scywindows.scydesktop.AcceptSyncModalDialog;
import eu.scy.client.desktop.scydesktop.edges.DatasyncEdge;
import eu.scy.client.desktop.scydesktop.utils.UiUtils;
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

/**
 * @author Marjolaine
 */

public class FitexNode extends ISynchronizable, CustomNode, Resizable, ScyToolFX, EloSaverCallBack {
   def logger = Logger.getLogger(this.getClass());
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
   def spacing = 5.0;

   var bundle:ResourceBundleWrapper;
   def saveTitleBarButton = TitleBarButton {
              actionId: TitleBarButton.saveActionId
              action: doSaveElo
           }
   def saveAsTitleBarButton = TitleBarButton {
              actionId: TitleBarButton.saveAsActionId
              action: doSaveAsElo
           }

  public override function canAcceptDrop(object: Object): Boolean {
        if (object instanceof ISynchronizable) {
            if ((object as ISynchronizable).getToolName().equals("simulator") or (object as ISynchronizable).getToolName().equals("fitex")) {
                return true;
            }
        }
        return false;
    }

    public function isDnDSimulator(object: ISynchronizable): Boolean{
        return (object as ISynchronizable).getToolName().equals("simulator");
    }

    public function isDnDFitex(object: ISynchronizable): Boolean{
        return (object as ISynchronizable).getToolName().equals("fitex");
    }

    public override function acceptDrop(object: Object) {
        logger.debug("drop accepted.");
        var isSync = isSynchronizingWith(object as ISynchronizable);
        if (isSync) {
            removeDatasync(object as ISynchronizable);
        } else {
             if(isDnDSimulator(object as ISynchronizable)){
                acceptDialog = AcceptSyncModalDialog {
                        object: object as ISynchronizable
                        okayAction: initializeDatasync
                        cancelAction: cancelDialog
                    }
                createModalDialog(scyWindow.windowManager.scyDesktop.windowStyler.getWindowColorScheme(ImageWindowStyler.generalNew), ##"Synchronise?", acceptDialog);
            }else if(isDnDFitex(object as ISynchronizable)){
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
                            mergeDataset(object as ISynchronizable);
                        }
                    }else{
                        //leave(null);
                        mergeDataset(object as ISynchronizable);
                    }

           }
        }
    }

    function cancelDialog(): Void {
            acceptDialog.modalDialogBox.close();
    }

    function createModalDialog(windowColorScheme: WindowColorScheme, title: String, modalDialogNode: ModalDialogNode): Void {
        Composer.localizeDesign(modalDialogNode.getContentNodes());
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

   public function initializeDatasync(simulator: ISynchronizable) {
        var datasyncsession = toolBrokerAPI.getDataSyncService().createSession(new DummySyncListener());
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
        fitexPanel.joinSession(mucID);
    }

    public override function join(mucID: String, edge: Object) {
        this.datasyncEdge = edge as DatasyncEdge;
        fitexPanel.joinSession(mucID);
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

    public function mergeDataset(fitex: ISynchronizable){
        if(fitex instanceof FitexNode)
            fitexPanel.mergeELO((fitex as FitexNode).getDataset());
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
      return 320;
   }

   public override function getMinWidth() : Number{
      return 550;
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
   
}

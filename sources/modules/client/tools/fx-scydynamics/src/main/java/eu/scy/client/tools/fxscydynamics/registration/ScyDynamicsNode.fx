package eu.scy.client.tools.fxscydynamics.registration;

import java.net.URI;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.CustomNode;
import javafx.scene.layout.Resizable;
import eu.scy.client.desktop.scydesktop.tools.ScyToolFX;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import roolo.api.IRepository;
import eu.scy.client.desktop.desktoputils.jdom.JDomStringConversion;
import eu.scy.client.desktop.scydesktop.tools.EloSaverCallBack;
import roolo.elo.api.IELOFactory;
import roolo.elo.api.IMetadataTypeManager;
import roolo.elo.api.IELO;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;
import eu.scy.client.tools.scydynamics.editor.ModelEditor;
import eu.scy.actionlogging.api.IActionLogger;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import javafx.scene.layout.Container;
import eu.scy.client.desktop.scydesktop.swingwrapper.ScySwingWrapper;
import java.awt.image.BufferedImage;
import java.awt.Dimension;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import eu.scy.client.desktop.scydesktop.imagewindowstyler.ImageWindowStyler;
import eu.scy.client.desktop.desktoputils.art.WindowColorScheme;
import eu.scy.client.desktop.desktoputils.i18n.Composer;
import eu.scy.client.desktop.desktoputils.EmptyBorderNode;
import eu.scy.client.desktop.scydesktop.corners.elomanagement.ModalDialogNode;
import eu.scy.client.desktop.scydesktop.scywindows.scydesktop.ModalDialogBox;
import eu.scy.elo.contenttype.dataset.DataSet;
import eu.scy.client.desktop.scydesktop.tools.TitleBarButton;
import eu.scy.client.desktop.scydesktop.tools.TitleBarButtonManager;
import org.apache.log4j.Logger;
import roolo.elo.metadata.keys.KeyValuePair;
import java.util.Set;
import javafx.util.StringLocalizer;
import org.jdom.input.SAXBuilder;
import java.io.StringReader;
import eu.scy.client.desktop.scydesktop.scywindows.scaffold.IScaffoldChangeListener;
import eu.scy.client.desktop.scydesktop.scywindows.scaffold.ScaffoldManager;
import eu.scy.collaboration.api.CollaborationStartable;
import eu.scy.client.common.datasync.ISyncSession;
import eu.scy.client.desktop.desktoputils.XFX;

public class ScyDynamicsNode extends CollaborationStartable, IScaffoldChangeListener, CustomNode, Resizable, ScyToolFX, EloSaverCallBack {

   var previousMode;
   var infoDialog: SCYDynamicsInfoDialog;
   def logger = Logger.getLogger(this.getClass());
   def scyDynamicsType = "scy/model";
   def datasetType = "scy/dataset";
   def jdomStringConversion = new JDomStringConversion();
   public-init var modelEditor: ModelEditor;
   public-init var scyWindow: ScyWindow;
   public var eloFactory: IELOFactory;
   public var metadataTypeManager: IMetadataTypeManager;
   public var repository: IRepository;
   public var toolBrokerAPI: ToolBrokerAPI;
   public var actionLogger: IActionLogger;
   public override var width on replace { resizeContent() };
   public override var height on replace { resizeContent() };
   var wrappedModelEditor: Node;
   var technicalFormatKey: IMetadataKey;
   var keywordsKey: IMetadataKey;
   var eloModel: IELO;
   var eloDataset: IELO;
   def spacing = 5.0;
   var collaborative: Boolean = false;
   def saveTitleBarButton = TitleBarButton {
              actionId: "save"
              iconType: "save"
              action: doSaveElo
              tooltip: "save ELO"
           }
   def saveAsTitleBarButton = TitleBarButton {
              actionId: "saveAs"
              iconType: "save_as"
              action: doSaveAsElo
              tooltip: "save copy of ELO"
           }
   def saveAsDatasetTitleBarButton = TitleBarButton {
              actionId: "saveAsDataset"
              iconType: "save_as_dataset"
              action: doSaveAsDataset
              tooltip: "save copy of ELO as dataset"
           }

   public override function initialize(windowContent: Boolean): Void {
      ScaffoldManager.getInstance().addScaffoldListener(this);
      repository = toolBrokerAPI.getRepository();
      metadataTypeManager = toolBrokerAPI.getMetaDataTypeManager();
      eloFactory = toolBrokerAPI.getELOFactory();
      actionLogger = toolBrokerAPI.getActionLogger();
      technicalFormatKey = metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.TECHNICAL_FORMAT);
      keywordsKey = metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.KEYWORDS);
      modelEditor.setToolBroker(toolBrokerAPI);
      previousMode = modelEditor.getMode();
      //modelEditor.setActionLogger(toolBrokerAPI.getActionLogger(), toolBrokerAPI.getLoginUserName(), toolBrokerAPI.getMissionRuntimeURI().toString());
      logger.debug("scaffold level {ScaffoldManager.getInstance().getScaffoldLevel()}");
      this.scaffoldLevelChanged(ScaffoldManager.getInstance().getScaffoldLevel());
   }

   public override function canAcceptDrop(object: Object): Boolean {
      // not ready for usage yet
      return false;

//		logger.debug("canAcceptDrop of {object.getClass()}");
//        if (object instanceof ContactFrame) {
//            var c: ContactFrame = object as ContactFrame;
//            if (not scyWindow.ownershipManager.isOwner(c.contact.name)) {
//                return true;
//            }
//        }
//        return false;
   }

   public override function acceptDrop(object: Object): Void {
   // not ready for usage yet

   //        logger.debug("acceptDrop of {object.getClass()}");
   //        var c: ContactFrame = object as ContactFrame;
   //        logger.debug("acceptDrop user: {c.contact.name}");
   //        scyWindow.ownershipManager.addPendingOwner(c.contact.name);
   //        scyWindow.windowManager.scyDesktop.config.getToolBrokerAPI().proposeCollaborationWith("{c.contact.awarenessUser.getJid()}", scyWindow.eloUri.toString(), scyWindow.mucId);
   //        logger.debug("scyDesktop: {scyWindow.windowManager.scyDesktop}");
   }

   public override function startCollaboration(mucid: String) {
      if (not collaborative) {
         collaborative = true;
         FX.deferAction(function(): Void {
            def session: ISyncSession = modelEditor.joinSession(mucid);
            session.addCollaboratorStatusListener(scyWindow.ownershipManager);
            session.refreshOnlineCollaborators();
            logger.debug("joined session, mucid: {mucid}");
         });
      }
   }

   public override function stopCollaboration(): Void {
      if (collaborative) {
         collaborative = false;
         FX.deferAction(function(): Void {
            modelEditor.leaveSession();
         });
      }
   }

   public override function setTitleBarButtonManager(titleBarButtonManager: TitleBarButtonManager, windowContent: Boolean): Void {
      if (windowContent) {
         titleBarButtonManager.titleBarButtons = [
                    saveTitleBarButton,
                    saveAsTitleBarButton,
                    saveAsDatasetTitleBarButton
                 ]
      }
   }

   public override function loadElo(uri: URI) {
      XFX.runActionInBackground(function(): Void {
         doLoadElo(uri);
      })
   }

   public override function getThumbnail(width: Integer, height: Integer): BufferedImage {
      if (modelEditor != null) {
         return eu.scy.client.desktop.desktoputils.UiUtils.createThumbnail(modelEditor.getCanvas(), modelEditor.getCanvas().getSize(), new Dimension(width, height));
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

   public override function create(): Node {
      // note: the injected services are not yet available here
      // e.g., use the initialize(..) method
      if (eloModel.getUri() == null) {
      //modelEditor.setEloUri((scyWindow.scyToolsList.actionLoggerTool as ScyToolActionLogger).getURI());
      } else {
      //modelEditor.setEloUri(eloModel.getUri().toString());
      }
      wrappedModelEditor = ScySwingWrapper.wrap(modelEditor);
      wrappedModelEditor
   }

   function doLoadElo(eloUri: URI): Void {
      logger.info("Trying to load elo {eloUri}");
      var newElo = repository.retrieveELO(eloUri);
      if (newElo != null) {
         FX.deferAction(function(): Void {
            modelEditor.setNewModel();
            var builder = new SAXBuilder();
            var doc = builder.build(new StringReader(newElo.getContent().getXmlString()));
            modelEditor.setModel(doc.getRootElement());
            previousMode = modelEditor.getMode();
            logger.info("elo loaded");
            eloModel = newElo;
         })
      }
   }

   function doSaveAsDataset() {
      var dataset: DataSet = modelEditor.getDataSet();
      if (dataset.getValues() == null or dataset.getValues().size() == 0) {
         showEmptyDatasetInfobox();
      } else {
         eloSaver.otherEloSaveAs(getDataset(), this);
      }
   }

   public override function onQuit(): Void {
      if (eloModel != null) {
         def oldContentXml = eloModel.getContent().getXmlString();
         def newContentXml = getElo().getContent().getXmlString();
         if (oldContentXml == newContentXml) {
            // nothing changed
            return;
         }
      }
      doSaveElo();
   }

   function doSaveElo() {
      eloSaver.eloUpdate(getElo(), this);
   }

   function doSaveAsElo() {
      eloSaver.eloSaveAs(getElo(), this);
   }

   function getElo(): IELO {
      if (eloModel == null) {
         eloModel = eloFactory.createELO();
         eloModel.getMetadata().getMetadataValueContainer(technicalFormatKey).setValue(scyDynamicsType);
      }
      var xmlString = modelEditor.getModelXML();
      if (xmlString.startsWith("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")) {
         xmlString = xmlString.substring(39);
      }
      eloModel.getContent().setXmlString(xmlString);

      // setting node-names as keywords
      var namesSet: Set;
      namesSet = modelEditor.getModel().getNodes().keySet();
      for (name in namesSet) {
         eloModel.getMetadata().getMetadataValueContainer(keywordsKey).addValue(new KeyValuePair(name as String, "1.0"));
      }
      return eloModel;
   }

   function getDataset(): IELO {
      if (eloDataset == null) {
         eloDataset = eloFactory.createELO();
         eloDataset.getMetadata().getMetadataValueContainer(technicalFormatKey).setValue(datasetType);
      }
      eloDataset.getContent().setXmlString(jdomStringConversion.xmlToString(modelEditor.getDataSet().toXML()));
      return eloDataset;
   }

   function showEmptyDatasetInfobox(): Void {
      infoDialog = SCYDynamicsInfoDialog {
                 okayAction: cancelDialog
              }
      createModalDialog(scyWindow.windowManager.scyDesktop.windowStyler.getWindowColorScheme(ImageWindowStyler.generalNew), "Info", infoDialog);
   }

   function createModalDialog(windowColorScheme: WindowColorScheme, title: String, modalDialogNode: ModalDialogNode): Void {
      Composer.localizeDesign(modalDialogNode.getContentNodes(), StringLocalizer {});
      modalDialogNode.modalDialogBox = ModalDialogBox {
                 content: EmptyBorderNode {
                    content: Group {
                       content: modalDialogNode.getContentNodes();
                    }
                 }
                 targetScene: scyWindow.windowManager.scyDesktop.scene
                 title: title
                 eloIcon: scyWindow.windowManager.scyDesktop.windowStyler.getScyEloIcon(scyWindow.eloType)
                 windowColorScheme: windowColorScheme
                 closeAction: function(): Void {
                 }
              }
   }

   function cancelDialog(): Void {
      infoDialog.modalDialogBox.close();
   }

   override public function eloSaveCancelled(elo: IELO): Void {
   }

   override public function eloSaved(elo: IELO): Void {
      this.eloModel = elo;
   }

   function resizeContent() {
      Container.resizeNode(wrappedModelEditor, width, height - wrappedModelEditor.boundsInParent.minY - spacing);
   }

   public override function getPrefHeight(height: Number): Number {
      return Container.getNodePrefHeight(wrappedModelEditor, height) + wrappedModelEditor.boundsInParent.minY + spacing;
   }

   public override function getPrefWidth(width: Number): Number {
      return Container.getNodePrefWidth(wrappedModelEditor, width);
   }

   public override function getMinHeight(): Number {
      return 300;
   }

   public override function getMinWidth(): Number {
      return 300;
   }

   public override function setReadOnly(newReadOnly: Boolean): Void {
      if (newReadOnly != readOnly) {
         (super as ScyToolFX).setReadOnly(newReadOnly);
         if (readOnly) {
            previousMode = modelEditor.getMode();
            modelEditor.setMode(ModelEditor.Mode.CLEAR_BOX);
         } else {
            modelEditor.setMode(previousMode);
         }
      }
   }

   public override function scaffoldLevelChanged(newLevel: java.lang.Integer): Void {
      if (true) {
         // scaffold level changes are changed in the current implementation,
         // as they are not used in the missions
         logger.info("ignoring scaffold level change.");
         return;
      }
      logger.info("setting scaffold to {newLevel}");
      //println("*** ScyDynamicsNode.scaffoldLevelChanged to {newLevel}");
      if (newLevel == ScaffoldManager.SCAFFOLD_OFF) {
         modelEditor.setMode(ModelEditor.Mode.QUANTITATIVE_MODELLING);
      } else if (newLevel == ScaffoldManager.SCAFFOLD_MEDIUM) {
         modelEditor.setMode(ModelEditor.Mode.QUANTITATIVE_MODELLING);
      } else if (newLevel == ScaffoldManager.SCAFFOLD_HIGH) {
         modelEditor.setMode(ModelEditor.Mode.CLEAR_BOX);
      }
   }

}

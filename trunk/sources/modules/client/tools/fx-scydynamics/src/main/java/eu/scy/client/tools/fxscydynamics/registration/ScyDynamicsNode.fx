package eu.scy.client.tools.fxscydynamics.registration;

import java.net.URI;
import javafx.scene.Group;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.CustomNode;
import javafx.scene.layout.Resizable;
import colab.um.xml.model.JxmModel;
import eu.scy.client.desktop.scydesktop.tools.ScyToolFX;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import roolo.api.IRepository;
import eu.scy.client.desktop.scydesktop.utils.log4j.Logger;
import eu.scy.client.desktop.scydesktop.utils.jdom.JDomStringConversion;
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
import eu.scy.client.desktop.scydesktop.art.WindowColorScheme;
import eu.scy.client.desktop.scydesktop.utils.i18n.Composer;
import eu.scy.client.desktop.scydesktop.utils.EmptyBorderNode;
import eu.scy.client.desktop.scydesktop.corners.elomanagement.ModalDialogNode;
import eu.scy.client.desktop.scydesktop.scywindows.scydesktop.ModalDialogBox;
import eu.scy.elo.contenttype.dataset.DataSet;
import eu.scy.client.desktop.scydesktop.ScyToolActionLogger;

public class ScyDynamicsNode extends CustomNode, Resizable, ScyToolFX, EloSaverCallBack {

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
    public override var width on replace {resizeContent()};
    public override var height on replace {resizeContent()};
    var wrappedModelEditor: Node;
    var technicalFormatKey: IMetadataKey;
    var eloModel: IELO;
    var eloDataset: IELO;
    def spacing = 5.0;

    public override function initialize(windowContent: Boolean): Void {
        repository = toolBrokerAPI.getRepository();
        metadataTypeManager = toolBrokerAPI.getMetaDataTypeManager();
        eloFactory = toolBrokerAPI.getELOFactory();
        actionLogger = toolBrokerAPI.getActionLogger();
        technicalFormatKey = metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.TECHNICAL_FORMAT);
        modelEditor.setActionLogger(toolBrokerAPI.getActionLogger(), "dummy_user");
    }

    public override function loadElo(uri: URI) {
        doLoadElo(uri);
    }

    public override function getThumbnail(width: Integer, height: Integer): BufferedImage {
        if (modelEditor != null) {
            return eu.scy.client.desktop.scydesktop.utils.UiUtils.createThumbnail(modelEditor.getCanvas(), modelEditor.getCanvas().getSize(), new Dimension(width, height));
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
            modelEditor.setEloUri((scyWindow.scyToolsList.actionLoggerTool as ScyToolActionLogger).getURI());
        } else {
            modelEditor.setEloUri(eloModel.getUri().toString());
        }
        wrappedModelEditor = ScySwingWrapper.wrap(modelEditor);
        return Group {
            blocksMouse: true;
            content: [
                VBox {
                    translateY: spacing;
                    spacing: spacing;
                    content: [
                        HBox {
                            translateX: spacing;
                            spacing: spacing;
                            content: [
                                Button {
                                    text: "Save model"
                                    action: function() {
                                        doSaveElo();
                                    }
                                }
                                Button {
                                    text: "Save as model"
                                    action: function() {
                                        doSaveAsElo();
                                    }
                                }
                                Button {
                                    text: "Save as dataset"
                                    action: function() {
                                        var dataset:DataSet = modelEditor.getDataSet();
                                        if (dataset.getValues() == null or dataset.getValues().size() == 0) {
                                            showEmptyDatasetInfobox();
                                        } else {
                                            eloSaver.otherEloSaveAs(getDataset(), this);
                                        }
                                    }
                                }
//                                Button {
//                                text: "test thumbnail"
//                                action: function() {
//                                    testThumbnail();
//                                }
//                            }
                            ]
                        }
                        wrappedModelEditor
                    ]
                }
            ]
        };
    }

    function doLoadElo(eloUri: URI) {
        logger.info("Trying to load elo {eloUri}");
        var newElo = repository.retrieveELO(eloUri);
        if (newElo != null) {
            modelEditor.setNewModel();
            modelEditor.setXmModel(JxmModel.readStringXML(newElo.getContent().getXmlString()));
            logger.info("elo loaded");
            eloModel = newElo;
        }
    }

   public override function onQuit():Void{
      if (eloModel!=null){
         def oldContentXml = eloModel.getContent().getXmlString();
         def newContentXml = getElo().getContent().getXmlString();
         if (oldContentXml==newContentXml){
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
        Composer.localizeDesign(modalDialogNode.getContentNodes());
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

}

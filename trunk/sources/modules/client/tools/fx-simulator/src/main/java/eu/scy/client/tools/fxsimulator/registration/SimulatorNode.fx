package eu.scy.client.tools.fxsimulator.registration;

import java.net.URI;
import javafx.scene.Group;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.CustomNode;
import javafx.scene.layout.Resizable;
import java.awt.Dimension;
import eu.scy.client.desktop.scydesktop.tools.ScyToolFX;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import roolo.api.IRepository;
import eu.scy.client.desktop.scydesktop.utils.jdom.JDomStringConversion;
import eu.scy.client.desktop.scydesktop.tools.EloSaverCallBack;
import roolo.elo.api.IELOFactory;
import roolo.elo.api.IMetadataTypeManager;
import roolo.elo.api.IELO;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;
import javax.swing.JPanel;
import javax.swing.JComponent;
import java.awt.event.ActionListener;
import javax.swing.JTextArea;
import eu.scy.client.tools.scysimulator.DataCollector;
import eu.scy.client.tools.scysimulator.SimConfig;
import sqv.SimQuestViewer;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import java.awt.event.ActionEvent;
import eu.scy.client.desktop.scydesktop.scywindows.scydesktop.AcceptSyncModalDialog;
import javax.swing.JLabel;
import eu.scy.notification.api.INotifiable;
import eu.scy.notification.api.INotification;
import eu.scy.client.desktop.scydesktop.scywindows.DatasyncAttribute;
import javax.swing.JOptionPane;
import eu.scy.client.common.datasync.ISynchronizable;
import eu.scy.client.desktop.scydesktop.ScyToolActionLogger;
import javafx.scene.layout.Container;
import eu.scy.client.desktop.scydesktop.swingwrapper.ScySwingWrapper;
import eu.scy.client.common.datasync.DummySyncListener;
import eu.scy.client.desktop.scydesktop.art.WindowColorScheme;
import eu.scy.client.desktop.scydesktop.corners.elomanagement.ModalDialogNode;
import eu.scy.client.desktop.scydesktop.edges.DatasyncEdge;
import eu.scy.client.desktop.scydesktop.imagewindowstyler.ImageWindowStyler;
import eu.scy.client.desktop.scydesktop.scywindows.scydesktop.ModalDialogBox;
import eu.scy.client.desktop.scydesktop.utils.EmptyBorderNode;
import eu.scy.client.desktop.scydesktop.utils.i18n.Composer;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import java.util.logging.Logger;
import eu.scy.client.common.scyi18n.UriLocalizer;
import java.net.URL;
import java.util.List;
import java.util.ArrayList;
import eu.scy.client.tools.scysimulator.SimConfig.MODE;
import eu.scy.client.desktop.scydesktop.tools.TitleBarButton;
import eu.scy.client.desktop.scydesktop.tools.TitleBarButtonManager;

public class SimulatorNode
    extends ISynchronizable, CustomNode, Resizable, ScyToolFX, EloSaverCallBack, ActionListener, INotifiable {

    var simquestViewer: SimQuestViewer;
    def logger = Logger.getLogger(SimulatorNode.class.getName());
    def simconfigType = "scy/simconfig";
    def datasetType = "scy/dataset";
    public-init var simquestPanel: JPanel;
    public-init var scyWindow: ScyWindow;
    public var eloFactory: IELOFactory;
    public var metadataTypeManager: IMetadataTypeManager;
    public var repository: IRepository;
    public var toolBrokerAPI: ToolBrokerAPI;
    public override var width on replace {
                resizeContent()
            };
    public override var height on replace {
                resizeContent()
            };
    def rotation = bind scyWindow.rotate on replace {
        if (dataCollector != null) {
            //logger.info("setting rotation to {rotation}");
            dataCollector.setRotation(rotation);
        }
    };

    var fixedDimension = new Dimension(575, 275);
    var displayComponent: JComponent;
    var wrappedSimquestPanel: Node;
    var technicalFormatKey: IMetadataKey;
    var keywordsKey: IMetadataKey;
    var newSimulationPanel: NewSimulationPanel;
    var eloSimconfig: IELO;
    var eloDataset: IELO;
    var dataCollector: DataCollector;
    var syncAttrib: DatasyncAttribute;
    var datasyncEdge: DatasyncEdge;
    var acceptDialog: AcceptSyncModalDialog;
    var jdomStringConversion: JDomStringConversion = new JDomStringConversion();
    def spacing = 5.0;
    def lostPixels = 20.0;
    var split: JSplitPane;
    var scroller: JScrollPane;
    def simulatorContent = Group {};

    def saveTitleBarButton = TitleBarButton {
	  actionId: TitleBarButton.saveActionId
	  action: doSaveSimconfig
    }

   def saveAsTitleBarButton = TitleBarButton {
	  actionId: TitleBarButton.saveAsActionId
	  action: doSaveAsSimconfig
    }

    def saveAsDatasetTitleBarButton = TitleBarButton {
	  actionId: "saveAsDataset"
	  iconType: "save_as_dataset"
	  action: doSaveAsDataset
	  tooltip: "save copy of ELO as dataset"
    }

//    var saveDatasetButton =
//	    Button {text: ##"SaveAs Dataset"
//	    action: function() {
//	    doSaveAsDataset();
//    }};

   public override function setTitleBarButtonManager(titleBarButtonManager: TitleBarButtonManager, windowContent: Boolean): Void {
      if (windowContent) {
         titleBarButtonManager.titleBarButtons = [
                    saveTitleBarButton,
                    saveAsTitleBarButton,
		    saveAsDatasetTitleBarButton
                 ]
      }
   }

    public override function canAcceptDrop(object: Object): Boolean {
	logger.severe("object received: {object.getClass()}");
        if (object instanceof ISynchronizable) {
            if ((object as ISynchronizable).getToolName().equals("fitex")) {
                return true;
            }
        }
        return false;
    }

    public override function acceptDrop(object: Object) {
        logger.info("drop accepted");
        var isSync = isSynchronizingWith(object as ISynchronizable);
        if (isSync) {
            removeDatasync(object as ISynchronizable);
        } else {
            acceptDialog = AcceptSyncModalDialog {
                        object: object as ISynchronizable
                        okayAction: initializeDatasync
                        cancelAction: cancelDialog
                    }
            createModalDialog(scyWindow.windowManager.scyDesktop.windowStyler.getWindowColorScheme(ImageWindowStyler.generalNew), ##"Synchronise?", acceptDialog);
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
                    closeAction: function(): Void {
                    }
                }
    }

    public function acceptDrop_(object: Object) {
        logger.info("drop accepted");
        var isSync = isSynchronizingWith(object as ISynchronizable);
        if (isSync) {
            removeDatasync(object as ISynchronizable);
        } else {
            var yesNoOptions = [##"Yes", ##"No"];
            var n = -1;
            n = JOptionPane.showOptionDialog(null,
                    ##"Do you want to synchronise\nwith the Dataprocessing tool?", // question
                    ##"Synchronise?", // title
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE, // icon
                    null, yesNoOptions, yesNoOptions[0]);
            if (n == 0) {
                initializeDatasync(object as ISynchronizable);
            }
        }
    }

    /* return true is scysimulator is synchronizing with fitex with this sessionID*/
    function isSynchronizingWith(fitex: ISynchronizable): Boolean {
        if (fitex.getSessionID() != null and getSessionID() != null and getSessionID().equals(fitex.getSessionID())) {
            return true;
        } else {
            return false;
        }
    }

    public function initializeDatasync(fitex: ISynchronizable): Void {
        datasyncEdge = scyWindow.windowManager.scyDesktop.edgesManager.addDatasyncLink(fitex.getDatasyncAttribute() as DatasyncAttribute, syncAttrib);
        var datasyncsession = toolBrokerAPI.getDataSyncService().createSession(new DummySyncListener());
        fitex.join(datasyncsession.getId(), datasyncEdge as Object);
        this.join(datasyncsession.getId());
        datasyncEdge.join(datasyncsession.getId(), toolBrokerAPI);
        acceptDialog.modalDialogBox.close();
    }

    public function removeDatasync(fitex: ISynchronizable) {
        scyWindow.windowManager.scyDesktop.edgesManager.removeDatasyncLink(datasyncEdge);
        datasyncEdge = null;
        this.leave(dataCollector.getSessionID());
        fitex.leave(fitex.getSessionID());
    }

    public override function getDatasyncAttribute(): DatasyncAttribute {
        return syncAttrib;
    }

    public override function join(mucID: String) {
        dataCollector.join(mucID);
    }

    public override function join(mucID: String, edge: Object) {
        dataCollector.join(mucID);
        this.datasyncEdge = edge as DatasyncEdge;
    }

    public override function leave(mucID: String) {
        dataCollector.leave();
        this.datasyncEdge = null;
    }

    public override function getSessionID(): String {
        return dataCollector.getSessionID();
    }

    public override function getToolName(): String {
        return "simulator";
    }

    public override function getThumbnail(width: Integer, height: Integer): BufferedImage {
        if (simquestViewer != null) {
            return eu.scy.client.desktop.scydesktop.utils.UiUtils.createThumbnail(simquestViewer.getInterfacePanel(), simquestViewer.getRealSize(), new Dimension(width, height));
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

    public override function initialize(windowContent: Boolean): Void {
        repository = toolBrokerAPI.getRepository();
        metadataTypeManager = toolBrokerAPI.getMetaDataTypeManager();
        eloFactory = toolBrokerAPI.getELOFactory();
        technicalFormatKey = metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.TECHNICAL_FORMAT);
    }

    public override function newElo() {
	saveAsDatasetTitleBarButton.enabled = false;
	saveAsTitleBarButton.enabled = false;
	saveTitleBarButton.enabled = false;
        newSimulationPanel = new NewSimulationPanel(this);
        switchSwingDisplayComponent(newSimulationPanel);
    }

    public override function actionPerformed(evt: ActionEvent) {
        if (evt.getActionCommand().equals("loadsimulation")) {
            logger.info("load {newSimulationPanel.getSimulationURI()}");
            switchSwingDisplayComponent(new JLabel(##"Please wait while the simulation is loaded, this may take some seconds."));
            FX.deferAction(function(): Void {
		var simConfig: SimConfig = new SimConfig(newSimulationPanel.getSimulationURI(), newSimulationPanel.getMode());
                loadSimulation(simConfig);
            });
        }
    }

    override public function processNotification(notification: INotification): Boolean {
        var success: Boolean = false;
        if ((notification.getFirstProperty("message")!=null) or
         (notification.getFirstProperty("level")!=null and notification.getFirstProperty("type")!=null)) {
            if (dataCollector != null) {
                logger.info("process notification, forwarding to DataCollector");
                success = true;
                FX.deferAction(function (): Void {
                    dataCollector.processNotification(notification);
                })
            } else {
                logger.info("notification not processed, DataCollector == null");
            }
        }
        return success;
    }

    public override function  loadElo(uri:URI) {
        doLoadElo(uri);
    }

    public override  function  create ( ): Node {
        switchSwingDisplayComponent(simquestPanel);
	simulatorContent
//	return Group {
//            blocksMouse:true;
//            // cache: bind scyWindow.cache
//            content  : [
//            VBox {
//                    translateY:  spacing    ;
//                            spacing: spacing;
//                            content: [
//                                HBox {
//                                    translateX: spacing;
//                                    spacing: spacing;
//                                    content: [
//                                        Button {
//                                            text: ##"Save Simconfig"
//                                            action: function() {
//                                                doSaveSimconfig();
//                                            }
//                                        }
//                                        Button {
//                                            text: ##"SaveAs Simconfig"
//                                            action: function() {
//                                                doSaveAsSimconfig();
//                                            }
//                                        }
//					saveDatasetButton
//
//                                    /*Button {
//                                    text: "test thumbnail"
//                                    action: function () {
//                                    testThumbnail();
//                                    }
//                                    }*/
//                                    ]
//                                }
//                                simulatorContent
//                            ]
//                        }
//                    ]
//                };;
    }

    function switchSwingDisplayComponent(newComponent: JComponent): Void {
        displayComponent = newComponent;
        wrappedSimquestPanel = ScySwingWrapper.wrap(displayComponent);
        simulatorContent.content = wrappedSimquestPanel;
        resizeContent();
    }

    function doLoadElo(eloUri: URI) {
        logger.info("trying to load elo {eloUri}");
        var newElo = repository.retrieveELO(eloUri);
        if (newElo != null) {
            var simConfig: SimConfig = new SimConfig(newElo.getContent().getXmlString());
            loadSimulation(simConfig);
            if (dataCollector != null) {
                logger.info("setting simconfig {newElo}");
                dataCollector.setVariableValues(simConfig.getVariables());
		dataCollector.setRelevantVariables(simConfig.getRelevantVariables());
                dataCollector.setEloURI(eloUri.toString());
            } else {
                logger.info("datacollector == null, can't load ");
            }
            logger.info("elo loaded");
            eloSimconfig = newElo;
        }
    }

    function loadSimulation(simConfig: SimConfig) {
        // the flag "false" configures the SQV for memory usage (instead of disk usage)
        simquestViewer = new SimQuestViewer(false);
        logger.severe("trying to load simulation: {simConfig.getSimulationUri()}");
        //
        // testing uri localization
        var uriLocalizer = new UriLocalizer();
        var url = uriLocalizer.localizeUrl(new URL(simConfig.getSimulationUri()));
        var fileUri = new URI(url.toString());
	logger.severe("uri transformed to: {fileUri.toString()}");
        simquestViewer.setFile(fileUri);
        simquestViewer.setLookAndFeel(false);
        simquestViewer.createFrame(false);
        dataCollector = null;
        try {
            simquestViewer.run();
            //--- creating a splitpane
            // creating the datacollector
            dataCollector = new DataCollector(simquestViewer, toolBrokerAPI, (scyWindow.scyToolsList.actionLoggerTool as ScyToolActionLogger).getURI());
            var simulationViewer = simquestViewer.getInterfacePanel();
            simulationViewer.setPreferredSize(simquestViewer.getRealSize());
            // adding simulation and datacollector to splitpane
            scroller = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            scroller.setViewportView(simquestViewer.getInterfacePanel());
            split = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
            split.setDividerLocation(0.75);
            split.setResizeWeight(0.75);

	    dataCollector.setVariableValues(simConfig.getVariables());
	    dataCollector.setRelevantVariables(simConfig.getRelevantVariables());
	    saveAsDatasetTitleBarButton.enabled = true;
	    saveAsTitleBarButton.enabled = true;
	    saveTitleBarButton.enabled = true;
	    setMode(simConfig, dataCollector);

	    split.setBottomComponent(dataCollector);
            split.setTopComponent(scroller);
            dataCollector.setPreferredSize(new Dimension(100, 300));
            // adding the splitcomponent to the simquestpanel

            split.setEnabled(true);

            //toolBrokerAPI.registerForNotifications(this as INotifiable);
            fixedDimension = simquestViewer.getRealSize();
            switchSwingDisplayComponent(split);

            if (fixedDimension.width < 555) {
                fixedDimension.width = 555;
            }
            fixedDimension.height = fixedDimension.height + 260;
            scyWindow.open();
            syncAttrib = DatasyncAttribute {
                        scyWindow: scyWindow
                        dragAndDropManager: scyWindow.dragAndDropManager;
                        dragObject: this };
            insert syncAttrib into scyWindow.scyWindowAttributes;
        } catch (e: java.lang.Exception) {
            logger.info("exception caught: {e.getMessage()}");
            var info = new JTextArea(4, 42);
            info.append("Simulation could not be loaded.\n");
            info.append("Probably the simulation file was not found,\n");
            info.append("it was expected at:\n");
            info.append(fileUri.toString());
            simquestPanel.add(info);
        }
    }

    function setMode(simConfig: SimConfig, dataCollector: DataCollector): Void {
	logger.info("setting mode to {simConfig.getMode().toString()}");
	dataCollector.setMode(simConfig.getMode());
	if (simConfig.getMode().equals(MODE.explore_only)) {
	    // only simulation is visible
	    // datasets cannot be stored
	    //saveDatasetButton.visible = false;
	    saveAsDatasetTitleBarButton.enabled = false;
	} else if (simConfig.getMode().equals(MODE.explore_simple_data)) {
	    // only simulation is visible
	    // saving a dataset = saving one row of selected variables
	    //saveDatasetButton.visible = true;
	    saveAsDatasetTitleBarButton.enabled = true;
	} else if (simConfig.getMode().equals(MODE.collect_simple_data)) {
	    // datacollector is visible, but not "select variables button"
	    // uses pre-defined set of relevant variables
	    //saveDatasetButton.visible = true;
	    saveAsDatasetTitleBarButton.enabled = true;
	} else if (simConfig.getMode().equals(MODE.collect_data)) {
	    // full features
	    //saveDatasetButton.visible = true;
    	    saveAsDatasetTitleBarButton.enabled = true;
	}
    }


    function doSaveSimconfig(): Void {
        eloSaver.eloUpdate(createNewSimconfigElo(), this);
    }

    function doSaveAsSimconfig(): Void {
        eloSaver.eloSaveAs(createNewSimconfigElo(), this);
    }

    function doSaveAsDataset(): Void {
	if (eloDataset == null) {
            eloDataset = eloFactory.createELO();
            eloDataset.getMetadata().getMetadataValueContainer(technicalFormatKey).setValue(datasetType);
        }
	if (dataCollector.getMode().equals(MODE.explore_simple_data)) {
	    dataCollector.cleanDataSet();
	    dataCollector.addCurrentDatapoint();
	    logger.info("dataCollector cleaned and one row added.");
	}
        eloDataset.getContent().setXmlString(jdomStringConversion.xmlToString(dataCollector.getDataSet().toXML()));
        eloSaver.otherEloSaveAs(eloDataset, this);
    }

    function createNewSimconfigElo(): IELO {
        if (eloSimconfig == null) {
            eloSimconfig = eloFactory.createELO();
            eloSimconfig.getMetadata().getMetadataValueContainer(technicalFormatKey).setValue(simconfigType);
        }
        eloSimconfig.getContent().setXmlString(jdomStringConversion.xmlToString(dataCollector.getSimConfig().toXML()));
        return eloSimconfig;
    }

//    function createNewDatasetElo(): IELO {
//        if (eloDataset == null) {
//            eloDataset = eloFactory.createELO();
//            eloDataset.getMetadata().getMetadataValueContainer(technicalFormatKey).setValue(datasetType);
//        }
//        eloDataset.getContent().setXmlString(jdomStringConversion.xmlToString(dataCollector.getDataSet().toXML()));
//        return eloDataset;
//    }

    override public function eloSaveCancelled(elo: IELO): Void {}

    override public function eloSaved(elo: IELO): Void {
        if (elo.getMetadata().getMetadataValueContainer(technicalFormatKey).getValue().equals(simconfigType)) {
            this.eloSimconfig = elo;
            dataCollector.setEloURI(elo.getUri().toString());
        } else if (elo.getMetadata().getMetadataValueContainer(technicalFormatKey).getValue().equals(datasetType)) {
            this.eloDataset = elo;
        }
    }

    public override function onUnMinimized(): Void {
        logger.info("onUnMinimized");
    }

    public override function onOpened(): Void {
        Timeline {
            repeatCount: Timeline.INDEFINITE
            keyFrames: [
                KeyFrame {
                    time: 0.01s
                    action: function(): Void {
                        split.setDividerLocation(0.75);
                    //                      split.setVisible(true);
                    }
                }
            ];
        }.play();

    }

    function resizeContent() {
        Container.resizeNode(wrappedSimquestPanel, width, height - wrappedSimquestPanel.boundsInParent.minY - spacing - lostPixels);
    }

    public override function getPrefHeight(height: Number): Number {
        return Container.getNodePrefHeight(wrappedSimquestPanel, height) + wrappedSimquestPanel.boundsInParent.minY + spacing + lostPixels;
    }

    public override function getPrefWidth(width: Number): Number {
        Container.getNodePrefWidth(wrappedSimquestPanel, width);
    }

    public override function getMinWidth(): Number {
        400;
    }

    public override function getMinHeight(): Number {
        500;
    }

    public function getNodePrefWidth(): Number {
        400;
    }

    public function getNodePrefHeight(): Number {
        500;
    }

}

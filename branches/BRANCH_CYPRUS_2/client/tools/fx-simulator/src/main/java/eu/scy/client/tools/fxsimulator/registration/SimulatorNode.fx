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
import java.awt.BorderLayout;
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
import eu.scy.client.common.scyi18n.UriLocalizer;
import java.net.URL;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.RenderingHints;

public class SimulatorNode extends ISynchronizable, CustomNode, Resizable, ScyToolFX, EloSaverCallBack, ActionListener, INotifiable {

    var simquestViewer: SimQuestViewer;
    def logger = Logger.getLogger(this.getClass());
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
    def simulatorContent = Group{}

    public override function canAcceptDrop(object: Object): Boolean {
        if (object instanceof ISynchronizable) {
            if ((object as ISynchronizable).getToolName().equals("fitex")) {
                return true;
            }
        }
        return false;
    }

    public override function acceptDrop(object: Object) {
        logger.debug("drop accepted.");
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
            closeAction: function (): Void {
            }
        }
    }

    public function acceptDrop_(object: Object) {
        logger.debug("drop accepted.");
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

    public function initializeDatasync(fitex: ISynchronizable):Void {
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
        technicalFormatKey = metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.TECHNICAL_FORMAT);
    }

    public override function newElo() {
        newSimulationPanel = new NewSimulationPanel(this);
//        simquestPanel.add(newSimulationPanel, BorderLayout.NORTH);
         switchSwingDisplayComponent(newSimulationPanel);
    }

    public override function actionPerformed(evt: ActionEvent) {
        if (evt.getActionCommand().equals("loadsimulation")) {
            logger.info("load {newSimulationPanel.getSimulationURI()}");
//            newSimulationPanel.remove(newSimulationPanel.load);
//            newSimulationPanel.add(new JLabel(##"Please wait while the simulation is loaded, this may take some seconds."));
            switchSwingDisplayComponent(new JLabel(##"Please wait while the simulation is loaded, this may take some seconds."));
            FX.deferAction(function (): Void {
                loadSimulation(newSimulationPanel.getSimulationURI());
            });
        }
    }

    override public function processNotification(note: INotification): Void {
        if (dataCollector != null) {
            logger.info("process notification, forwarding to DataCollector");
            FX.deferAction(function () {
                dataCollector.processNotification(note);
            })
        } else {
            logger.info("notification not processed, DataCollector == null");
        }
    }

    public override function loadElo(uri: URI) {
        doLoadElo(uri);
    }

    public override function create(): Node {
        switchSwingDisplayComponent(simquestPanel);
        return Group {
                    blocksMouse: true;
                    // cache: bind scyWindow.cache
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
                                            text: ##"Save Simconfig"
                                            action: function () {
                                                doSaveSimconfig();
                                            }
                                        }
                                        Button {
                                            text: ##"SaveAs Simconfig"
                                            action: function () {
                                                doSaveAsSimconfig();
                                            }
                                        }  
                                        Button {
                                            text: ##"SaveAs Dataset"
                                            action: function () {
                                                doSaveAsDataset();
                                            }
                                        }
                                        /*Button {
                                            text: "test thumbnail"
                                            action: function () {
                                                testThumbnail();
                                            }
                                        }*/
                                    ]
                                }
                                simulatorContent
                            ]
                        }
                    ]
                };
    }

    function switchSwingDisplayComponent(newComponent : JComponent):Void{
      displayComponent = newComponent;
      wrappedSimquestPanel = ScySwingWrapper.wrap(displayComponent);
      simulatorContent.content = wrappedSimquestPanel;
      resizeContent();
    }

    function doLoadElo(eloUri: URI) {
        logger.info("Trying to load elo {eloUri}");
        var newElo = repository.retrieveELO(eloUri);
        if (newElo != null) {
            var simConfig: SimConfig = new SimConfig(newElo.getContent().getXmlString());
            loadSimulation(simConfig.getSimulationUri());
            if (dataCollector != null) {
                logger.info("setting simconfig {newElo}");
                dataCollector.setSimConfig(simConfig);
                dataCollector.setEloURI(eloUri.toString());
            } else {
                logger.info("datacollector == null, can't load ");
            }
            logger.info("elo loaded");
            eloSimconfig = newElo;
        }
    }

    function loadSimulation(simulationUri: String) {
        var fileUri = new URI(simulationUri);
        // the flag "false" configures the SQV for memory usage (instead of disk usage)
        simquestViewer = new SimQuestViewer(false);
        logger.info("trying to load simulation: {fileUri.toString()}");
        //
        // testing uri localization
        //var uriLocalizer = new UriLocalizer();
        //var url = uriLocalizer.localizeUrl(new URL("http://www.scy-lab.eu/sqzx/co2_house_en.sqzx"));
        //logger.info("*** url after: {url.toString()}");
        //
        simquestViewer.setFile(fileUri);
        simquestViewer.setLookAndFeel(false);
        simquestViewer.createFrame(false);
        dataCollector = null;
        try {
            simquestViewer.run();
//            simquestPanel.setLayout(new BorderLayout());
//            simquestPanel.removeAll();
//            simquestPanel.add(simquestViewer.getInterfacePanel(), BorderLayout.CENTER);
//            dataCollector = new DataCollector(simquestViewer, toolBrokerAPI, (scyWindow.scyToolsList.actionLoggerTool as ScyToolActionLogger).getURI());
//            toolBrokerAPI.registerForNotifications(this as INotifiable);
//            simquestPanel.add(dataCollector, BorderLayout.SOUTH);
            //--- creating a splitpane
            var split = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
            // creating the datacollector
            dataCollector = new DataCollector(simquestViewer, toolBrokerAPI, (scyWindow.scyToolsList.actionLoggerTool as ScyToolActionLogger).getURI());
            var simulationViewer = simquestViewer.getInterfacePanel();
            simulationViewer.setPreferredSize(simquestViewer.getRealSize());
            // adding simulation and datacollector to splitpane
            var scroller = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            scroller.setViewportView(simquestViewer.getInterfacePanel());
            split.setTopComponent(scroller);
            //dataCollector.setPreferredSize(new Dimension(100, 300));
            split.setBottomComponent(dataCollector);
            // adding the splitcomponent to the simquestpanel
            split.setEnabled(true);

            toolBrokerAPI.registerForNotifications(this as INotifiable);
            fixedDimension = simquestViewer.getRealSize();
            switchSwingDisplayComponent(split);
            split.setDividerLocation(0.66);
            split.setResizeWeight(1.0);

            if (fixedDimension.width < 555) {
                fixedDimension.width = 555;
            }
            fixedDimension.height = fixedDimension.height + 260;
            scyWindow.open();
            syncAttrib = DatasyncAttribute {
                        scyWindow:scyWindow
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

    function doSaveSimconfig() {
        eloSaver.eloUpdate(getSimconfig(), this);
    }

    function doSaveAsSimconfig() {
        eloSaver.eloSaveAs(getSimconfig(), this);
    }

    //function doSaveDataset() {
    //eloSaver.eloUpdate(getDataset(), this);
    //}
    function doSaveAsDataset() {
        eloSaver.otherEloSaveAs(getDataset(), this);
    }

    function getSimconfig(): IELO {
        if (eloSimconfig == null) {
            eloSimconfig = eloFactory.createELO();
            eloSimconfig.getMetadata().getMetadataValueContainer(technicalFormatKey).setValue(simconfigType);
        }
        eloSimconfig.getContent().setXmlString(jdomStringConversion.xmlToString(dataCollector.getSimConfig().toXML()));
        return eloSimconfig;
    }

    function getDataset(): IELO {
        if (eloDataset == null) {
            eloDataset = eloFactory.createELO();
            eloDataset.getMetadata().getMetadataValueContainer(technicalFormatKey).setValue(datasetType);
            //eloDataset.getMetadata().getMetadataValueContainer()
        }
        eloDataset.getContent().setXmlString(jdomStringConversion.xmlToString(dataCollector.getDataSet().toXML()));
        return eloDataset;
    }

    override public function eloSaveCancelled(elo: IELO): Void {
    }

    override public function eloSaved(elo: IELO): Void {
        if (elo.getMetadata().getMetadataValueContainer(technicalFormatKey).getValue().equals(simconfigType)) {
            this.eloSimconfig = elo;
            dataCollector.setEloURI(elo.getUri().toString());
        } else if (elo.getMetadata().getMetadataValueContainer(technicalFormatKey).getValue().equals(datasetType)) {
            this.eloDataset = elo;
        }
    }

    function resizeContent() {
        Container.resizeNode(wrappedSimquestPanel,width,height-wrappedSimquestPanel.boundsInParent.minY-spacing-lostPixels);
    }

    public override function getPrefHeight(height: Number): Number {
       // TODO, calculate the correct preferred height
       // the bottom part is not displayed
       return Container.getNodePrefHeight(wrappedSimquestPanel, height)+wrappedSimquestPanel.boundsInParent.minY+spacing+lostPixels;
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

}

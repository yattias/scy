package eu.scy.client.tools.fxsimulator.registration;

import java.net.URI;
import javafx.ext.swing.SwingComponent;
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
import java.awt.event.ActionListener;
import javax.swing.JTextArea;
import eu.scy.client.tools.scysimulator.DataCollector;
import eu.scy.client.tools.scysimulator.SimConfig;
import sqv.SimQuestViewer;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import java.awt.event.ActionEvent;
import eu.scy.client.desktop.scydesktop.scywindows.scydesktop.ModalDialogBox;
import javax.swing.JLabel;
import eu.scy.notification.api.INotifiable;
import java.lang.UnsupportedOperationException;
import eu.scy.notification.api.INotification;
import eu.scy.client.desktop.scydesktop.scywindows.DatasyncAttribute;
import javax.swing.JOptionPane;
import eu.scy.client.common.datasync.ISynchronizable;
import eu.scy.client.common.datasync.ISyncSession;
import eu.scy.client.common.datasync.DummySyncListener;

public class SimulatorNode extends ISynchronizable, CustomNode, Resizable, ScyToolFX, EloSaverCallBack, ActionListener, INotifiable {

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
    var fixedDimension = new Dimension(575, 275);
    var wrappedSimquestPanel: SwingComponent;
    var technicalFormatKey: IMetadataKey;
    var newSimulationPanel: NewSimulationPanel;
    var eloSimconfig: IELO;
    var eloDataset: IELO;
    var dataCollector: DataCollector;
    var jdomStringConversion: JDomStringConversion = new JDomStringConversion();
    def spacing = 5.0;

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
        var yesNoOptions = ["Yes", "No"];
        var n = -1;
        n = JOptionPane.showOptionDialog(null,
        "Do you want to synchronise\nwith the Dataprocessing tool?", // question
        "Synchronise?", // title
        JOptionPane.YES_NO_CANCEL_OPTION,
        JOptionPane.QUESTION_MESSAGE, // icon
        null, yesNoOptions, yesNoOptions[0]);
        if (n == 0) {
            initializeDatasync(object as ISynchronizable);
        }
    }

    public function initializeDatasync(fitex: ISynchronizable) {
        var datasyncsession = toolBrokerAPI.getDataSyncService().createSession(new DummySyncListener());
        fitex.join(datasyncsession.getId());
        this.join(datasyncsession.getId());
    }

    public override function join(mucID: String) {
        dataCollector.join(mucID);
    }

    public override function leave(mucID: String) {
        dataCollector.leave();
    }

    public override function getSessionID(): String {
        return dataCollector.getSessionID();
    }

    public override function getToolName(): String {
        return "simulator";
    }

    public override function initialize(windowContent: Boolean): Void {
        technicalFormatKey = metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.TECHNICAL_FORMAT);
    }

    public override function newElo() {
        newSimulationPanel = new NewSimulationPanel(this);
        simquestPanel.add(newSimulationPanel, BorderLayout.NORTH);
    }

    public override function actionPerformed(evt: ActionEvent) {
        if (evt.getActionCommand().equals("loadsimulation")) {
            logger.info("load {newSimulationPanel.getSimulationURI()}");
            newSimulationPanel.add(new JLabel("Please wait while the simulation is loaded, this may take some seconds."));
            FX.deferAction(function (): Void {
                loadSimulation(newSimulationPanel.getSimulationURI());
            });
        }
    }

    override public function processNotification(note: INotification): Void {
        if (dataCollector != null) {
            logger.info("process notification, forwarding to DataCollector");
            dataCollector.processNotification(note);
        } else {
            logger.info("notification not processed, DataCollector == null");
        }
    }

    public override function loadElo(uri: URI) {
        doLoadElo(uri);
    }

    public override function create(): Node {
        wrappedSimquestPanel = SwingComponent.wrap(simquestPanel);
        return Group {
                    blocksMouse: true;
                    //         cache: bind scyWindow.cache
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
                                            text: "Save Simconfig"
                                            action: function () {
                                                doSaveSimconfig();
                                            }
                                        }
                                        Button {
                                            text: "SaveAs Simconfig"
                                            action: function () {
                                                doSaveAsSimconfig();
                                            }
                                        }
                                        /*Button {
                                        text: "Save Dataset"
                                        action: function () {
                                        doSaveDataset();
                                        }
                                        }*/
                                        Button {
                                            text: "SaveAs Dataset"
                                            action: function () {
                                                doSaveAsDataset();
                                            }
                                        }
                                    ]
                                }
                                wrappedSimquestPanel
                            ]
                        }
                    ]
                };
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
        var simquestViewer = new SimQuestViewer(false);
        logger.info("trying to load simulation: {fileUri.toString()}");
        simquestViewer.setFile(fileUri);
        simquestViewer.createFrame(false);
        dataCollector = null;
        try {
            simquestViewer.run();
            simquestPanel.setLayout(new BorderLayout());
            // TODO: infering correct dimension rather than guessing
            //simquestViewer.getInterfacePanel().setMinimumSize(new Dimension(450, 450));
            //suestViewer.getInterfacePanel().
            simquestPanel.removeAll();
            simquestPanel.add(simquestViewer.getInterfacePanel(), BorderLayout.CENTER);
            dataCollector = new DataCollector(simquestViewer, toolBrokerAPI);
            toolBrokerAPI.registerForNotifications(this as INotifiable);
            simquestPanel.add(dataCollector, BorderLayout.SOUTH);
            fixedDimension = simquestViewer.getRealSize();
            if (fixedDimension.width < 555) {
                fixedDimension.width = 555;
            }
            fixedDimension.height = fixedDimension.height + 240;
            scyWindow.open();
            var syncAttrib = DatasyncAttribute {
                        dragAndDropManager: scyWindow.dragAndDropManager;
                        dragObject: this };
            insert syncAttrib into scyWindow.scyWindowAttributes;
        } catch (e: java.lang.Exception) {
            logger.info("SimQuestNode.createSimQuestNode(). exception caught: {e.getMessage()}");
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
        }
        eloDataset.getContent().setXmlString(jdomStringConversion.xmlToString(dataCollector.getDataSet().toXML()));
        return eloDataset;
    }

    override public function eloSaveCancelled(elo: IELO): Void {
    }

    override public function eloSaved(elo: IELO): Void {
        if (elo.getMetadata().getMetadataValueContainer(technicalFormatKey).getValue().equals(simconfigType)) {
            this.eloSimconfig = elo;
        } else if (elo.getMetadata().getMetadataValueContainer(technicalFormatKey).getValue().equals(datasetType)) {
            this.eloDataset = elo;
        }
    }

    function resizeContent() {
        var size = new Dimension(width, height - wrappedSimquestPanel.boundsInParent.minY - spacing);
        // setPreferredSize is needed
        simquestPanel.setPreferredSize(size);
        // setSize is not visual needed
        // but set it, so the component react to it
        simquestPanel.setSize(size);
    }

    public override function getPrefHeight(width: Number): Number {
        return fixedDimension.height;
    }

    public override function getPrefWidth(width: Number): Number {
        return fixedDimension.width;
    }

    public override function getMinHeight(): Number {
        return fixedDimension.height;
    }

    public override function getMinWidth(): Number {
        return fixedDimension.width;
    }

}

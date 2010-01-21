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

public class SimulatorNode extends CustomNode, Resizable, ScyToolFX, EloSaverCallBack, ActionListener {

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
            
    var wrappedSimquestPanel: SwingComponent;
    var technicalFormatKey: IMetadataKey;
    var newSimulationPanel: NewSimulationPanel;
    var elo: IELO;
    var dataCollector: DataCollector;
    var jdomStringConversion:JDomStringConversion = new JDomStringConversion();
    def spacing = 5.0;

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
                                        Button {
                                            text: "Save Dataset"
                                            action: function () {
                                                doSaveDataset();
                                            }
                                        }
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
                logger.info("datacollector = null, can't load ");
            }

            logger.info("elo loaded");
            elo = newElo;
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
                        simquestViewer.getInterfacePanel().setMinimumSize(new Dimension(450, 450));
                        simquestPanel.removeAll();
                        simquestPanel.add(simquestViewer.getInterfacePanel(), BorderLayout.CENTER);
                        dataCollector = new DataCollector(simquestViewer, toolBrokerAPI);
                        simquestPanel.add(dataCollector, BorderLayout.SOUTH);
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
        eloSaver.eloUpdate(getSimconfig(),this);
    }

    function doSaveAsSimconfig() {
        eloSaver.eloSaveAs(getSimconfig(),this);
    }

    function doSaveDataset() {
        eloSaver.eloUpdate(getDataset(),this);
    }

    function doSaveAsDataset() {
        eloSaver.eloSaveAs(getDataset(),this);
    }

    function getSimconfig(): IELO {
        if (elo == null) {
            elo = eloFactory.createELO();
            elo.getMetadata().getMetadataValueContainer(technicalFormatKey).setValue(simconfigType);
        }
        elo.getContent().setXmlString(jdomStringConversion.xmlToString(dataCollector.getSimConfig().toXML()));
        return elo;
    }

    function getDataset(): IELO {
        if (elo == null) {
            elo = eloFactory.createELO();
            elo.getMetadata().getMetadataValueContainer(technicalFormatKey).setValue(datasetType);
        }
        elo.getContent().setXmlString(jdomStringConversion.xmlToString(dataCollector.getDataSet().toXML()));
        return elo;
    }

    override public function eloSaveCancelled(elo: IELO): Void {
    }

    override public function eloSaved(elo: IELO): Void {
        this.elo = elo;
    }

    function resizeContent() {
        var size = new Dimension(width, height - wrappedSimquestPanel.boundsInParent.minY - spacing);
        // setPreferredSize is needed
        simquestPanel.setPreferredSize(size);
        // setSize is not visual needed
        // but set it, so the component react to it
        simquestPanel.setSize(size);
//      println("resized whiteboardPanel to ({width},{height})");
    }

    public override function getPrefHeight(width: Number): Number {
        return simquestPanel.getPreferredSize().getHeight();
    }

    public override function getPrefWidth(width: Number): Number {
        return simquestPanel.getPreferredSize().getWidth();
    }

    public override function getMinHeight(): Number {
        return 256;
    }

    public override function getMinWidth(): Number {
        return 580;
    }
}

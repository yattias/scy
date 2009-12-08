package eu.scy.client.tools.fxsimulator.registration;

import eu.scy.client.desktop.scydesktop.elofactory.WindowContentCreatorFX;
import javafx.scene.Node;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import java.net.URI;
import java.lang.System;
import roolo.api.IRepository;
import roolo.elo.api.IELOFactory;
import roolo.elo.api.IMetadataTypeManager;
import sqv.SimQuestViewer;
import javax.swing.JPanel;
import eu.scy.client.tools.scysimulator.DataCollector;
import eu.scy.client.tools.scysimulator.SimConfig;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JTextArea;
import roolo.elo.api.IELO;
import eu.scy.toolbroker.ToolBrokerImpl;

public class SimulatorContentCreator extends WindowContentCreatorFX {
   public var eloFactory:IELOFactory;
   public var metadataTypeManager: IMetadataTypeManager;
   public var repository:IRepository;
   public var simulationUriString:String = "http://scy.collide.info/balance.sqzx";

   public override function getScyWindowContent(eloUri:URI, scyWindow:ScyWindow):Node{
      var newElo:IELO  = repository.retrieveELO(eloUri);
      var simConfig:SimConfig = new SimConfig(newElo.getContent().getXmlString());
      simulationUriString = simConfig.getSimulationUri();
      var simulatorNode:SimulatorNode = createSimulatorNode(scyWindow);
      simulatorNode.loadElo(eloUri);
      return simulatorNode;
   }

   public override function getScyWindowContentNew(scyWindow:ScyWindow):Node{
      return createSimulatorNode(scyWindow);
   }

   function createSimulatorNode(scyWindow:ScyWindow):SimulatorNode{
      setWindowProperties(scyWindow);
      var fileUri = new URI(simulationUriString);
        // the flag "false" configures the SQV for memory usage (instead of disk usage)
        var simquestViewer = new SimQuestViewer(false);
        //var fileName = new FileName("src/main/java/eu/scy/elobrowser/tool/simquest/balance.sqzx");
        //fileUri = fileName.toURI();
        //var fileName = new FileName("E:/netbeans-workspaces/elo-browser/src/main/java/eu/scy/elobrowser/tool/simquest/balance.sqzx");
        //var fileName = new FileName("src/main/java/eu/scy/elobrowser/tool/simquest/co2house_0.9.sqzx");
        System.out.println("SimQuestNode.createSimQuestNode(). trying to load: {fileUri.getHost()} {fileUri.getPath()} {fileUri.getQuery()}");
        simquestViewer.setFile(fileUri);
        simquestViewer.createFrame(false);
        
        var tbi:ToolBrokerImpl = new ToolBrokerImpl("Jan", "jan");
        //var tbi.registerForNotifications(this);

        var simquestPanel = new JPanel();
        var dataCollector:DataCollector;
        var eloSimQuestWrapper:EloSimQuestWrapper;

        try {
            simquestViewer.run();

            simquestPanel.setLayout(new BorderLayout());
        	// TODO: infering correct dimension rather than guessing
        	simquestViewer.getInterfacePanel().setMinimumSize(new Dimension(450,450));
            simquestPanel.add(simquestViewer.getInterfacePanel(), BorderLayout.CENTER);

            dataCollector = new DataCollector(simquestViewer, tbi);
            simquestPanel.add(dataCollector, BorderLayout.SOUTH);

            eloSimQuestWrapper = new EloSimQuestWrapper(dataCollector);
            eloSimQuestWrapper.setRepository(repository);
	    eloSimQuestWrapper.setMetadataTypeManager(metadataTypeManager);
	    eloSimQuestWrapper.setEloFactory(eloFactory);
       eloSimQuestWrapper.setDocName(scyWindow.title);
        } catch (e:java.lang.Exception) {
        	System.out.println("SimQuestNode.createSimQuestNode(). exception caught:");
            e.printStackTrace();

            var info = new JTextArea(4,42);
        	info.append("Simulation could not be loaded.\n");
        	info.append("Probably the simulation file was not found,\n");
        	info.append("it was expected at:\n");
        	info.append(fileUri.getPath().toString());
            simquestPanel.add(info);
        }

        return SimulatorNode{
            simquestPanel:simquestPanel;
            eloSimQuestWrapper:eloSimQuestWrapper;
    }
	}

   function setWindowProperties(scyWindow:ScyWindow){
      scyWindow.minimumWidth = 320;
      scyWindow.minimumHeight = 100;
   }


}

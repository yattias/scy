/*
 * DrawingNode.fx
 *
 * Created on 18-dec-2008, 15:19:52
 */

package eu.scy.elobrowser.tool.scysimulator;

import eu.scy.elobrowser.main.Roolo;
import eu.scy.client.tools.scysimulator.DataCollector;
import eu.scy.client.tools.scysimulator.EloSimQuestWrapper;
import eu.scy.elobrowser.ui.CommandText;
import eu.scy.scywindows.ScyWindow;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.lang.Object;
import java.lang.System;
import java.net.URI;
import javafx.ext.swing.SwingComponent;
import javafx.scene.CustomNode;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Resizable;
import javafx.scene.layout.VBox;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import sqv.SimQuestViewer;
import utils.FileName;

/**
 * @author sikkenj
 */

 // place your code here
public class SimQuestNode extends CustomNode, Resizable {
    // preferredWidth and -height are used in Resizable and in ScyWindow
    override var preferredWidth = 552;
    override var preferredHeight = 686;

    var simquestPanel:JPanel;
    var eloSimQuestWrapper:EloSimQuestWrapper;

	public var scyWindow:ScyWindow on replace {
		setScyWindowTitle("")};

   public function doRotate(angle:Number){
		//System.out.println("SimQuestNode.doRotate: {angle}");
        eloSimQuestWrapper.getDataCollector().setRotation(angle);
	}

    public function loadElo(uri:URI){
        eloSimQuestWrapper.loadElo(uri);
		setScyWindowTitle("SimConfig: ");
    }

	function setScyWindowTitle(prefix:String){
		if (scyWindow == null)
            return;
		scyWindow.title = "{prefix}{eloSimQuestWrapper.getDocName()}";
//		var eloUri = eloSimQuestWrapper.getEloUri();
//		if (eloUri != null)
//        scyWindow.id = eloUri.toString()
//		else
//        scyWindow.id = "";
	}

    public override function create(): Node {
        return Group {
            blocksMouse:true;
            content: [
                VBox{
                    translateY:5;
                    spacing:5;
                    content:[
                        HBox{
                            translateX:5;
                            spacing:5;
                            content:[
                                CommandText{
                                    label:"New"
                                    clickAction:function( e: MouseEvent ):Void {
                                        eloSimQuestWrapper.newAction();
										setScyWindowTitle("DataSet: ");
                                    }
                                }
                                CommandText{
                                    label:"Load Config"
                                    clickAction:function( e: MouseEvent ):Void {
                                        eloSimQuestWrapper.loadSimConfigAction();
                                        setScyWindowTitle("Config: ");
                                    }
                                }
                                CommandText{
                                    label:"Save DataSet"
                                    clickAction:function( e: MouseEvent ):Void {
                                        eloSimQuestWrapper.saveDataSetAction();
										setScyWindowTitle("DataSet: ");
                                    }
                                }
                                CommandText{
                                    label:"SaveAs DataSet"
                                    clickAction:function( e: MouseEvent ):Void {
                                        eloSimQuestWrapper.saveAsDataSetAction();
										setScyWindowTitle("DataSet: ");
                                    }
                                }
                                CommandText{
                                    label:"Save Config"
                                    clickAction:function( e: MouseEvent ):Void {
                                        eloSimQuestWrapper.saveSimConfigAction();
										setScyWindowTitle("Config: ");
                                    }
                                }
                                CommandText{
                                    label:"SaveAs Config"
                                    clickAction:function( e: MouseEvent ):Void {
                                        eloSimQuestWrapper.saveAsSimConfigAction();
										setScyWindowTitle("Config: ");
                                    }
                                }
                            ]
                        }
                        SwingComponent.wrap(simquestPanel)
                    ]
                }
            ]
        };
    }
}


    public function createSimQuestNode(roolo:Roolo):SimQuestNode{
        var fileUri = new URI("http://scy.collide.info/balance.sqzx");
        // the flag "false" configures the SQV for memory usage (instead of disk usage)
        var simquestViewer = new SimQuestViewer(false);
        //var fileName = new FileName("src/main/java/eu/scy/elobrowser/tool/simquest/balance.sqzx");
        //fileUri = fileName.toURI();
        //var fileName = new FileName("E:/netbeans-workspaces/elo-browser/src/main/java/eu/scy/elobrowser/tool/simquest/balance.sqzx");
        //var fileName = new FileName("src/main/java/eu/scy/elobrowser/tool/simquest/co2house_0.9.sqzx");
        System.out.println("SimQuestNode.createSimQuestNode(). trying to load: {fileUri.getPath().toString()}");
        simquestViewer.setFile(fileUri);
        simquestViewer.createFrame(false);

        var simquestPanel = new JPanel();
        var dataCollector:DataCollector;
        var eloSimQuestWrapper = new EloSimQuestWrapper();

        try {
            simquestViewer.run();
        	
            simquestPanel.setLayout(new BorderLayout());
        	// TODO: infering correct dimension rather than guessing
        	simquestViewer.getInterfacePanel().setMinimumSize(new Dimension(450,450));
            simquestPanel.add(simquestViewer.getInterfacePanel(), BorderLayout.CENTER);

            dataCollector = new DataCollector(simquestViewer);
            simquestPanel.add(dataCollector, BorderLayout.SOUTH);

            eloSimQuestWrapper.setDataCollector(dataCollector);
            eloSimQuestWrapper.setRepository(roolo.repository);
			eloSimQuestWrapper.setMetadataTypeManager(roolo.metadataTypeManager);
			eloSimQuestWrapper.setEloFactory(roolo.eloFactory);
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

        return SimQuestNode{
            simquestPanel:simquestPanel;
            eloSimQuestWrapper:eloSimQuestWrapper;
    }
    }

    public function createSimQuestWindow(roolo:Roolo):ScyWindow{
    return
    createSimQuestWindow(SimQuestNode.createSimQuestNode(roolo));
    }

    public function createSimQuestWindow(simquestNode:SimQuestNode):ScyWindow{
        var simquestWindow = ScyWindow{
            translateX: 10;
            color:Color.GREEN;
            title:"SimQuest";
            scyContent: simquestNode;
            minimumWidth:320;
            minimumHeight:100;
            cache:true;
        }
		simquestWindow.openWindow(552,685);
        simquestNode.scyWindow = simquestWindow;
    return simquestWindow;
    }

function run(){
    Stage {
		title: "SimQuest node test"
		width: 250
		height: 250
		scene: Scene {
			content:SimQuestNode{
            }

		}
	}

}

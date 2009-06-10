package eu.scy.elobrowser.tool.scydynamics;

import eu.scy.client.tools.scydynamics.editor.ModelEditor;
import eu.scy.elobrowser.main.Roolo;
import eu.scy.elobrowser.ui.CommandText;
import eu.scy.scywindows.ScyWindow;
import java.lang.Object;
import java.net.URI;
import javafx.ext.swing.SwingComponent;
import javafx.scene.CustomNode;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ScyDynamicsNode extends CustomNode {
    // preferredWidth and -height are used in Resizable and in ScyWindow
    //override var preferredWidth = 552;
    //override var preferredHeight = 685;

    var modelEditor:ModelEditor;
    var scyDynamicsWrapper:ScyDynamicsWrapper;

	public var scyWindow:ScyWindow on replace {
		setScyWindowTitle("")};

    public function loadElo(uri:URI){
        scyDynamicsWrapper.loadElo(uri);
		setScyWindowTitle("Model: ");
    }

	function setScyWindowTitle(prefix:String){
		if (scyWindow == null)
            return;
		scyWindow.title = "{prefix}{scyDynamicsWrapper.getDocName()}";
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
                                        scyDynamicsWrapper.newAction();
										setScyWindowTitle("Model: ");
                                    }
                                }
                                CommandText{
                                    label:"Load Model"
                                    clickAction:function( e: MouseEvent ):Void {
                                        scyDynamicsWrapper.loadModelAction();
                                        setScyWindowTitle("Model: ");
                                    }
                                }
                                CommandText{
                                    label:"Save Model"
                                    clickAction:function( e: MouseEvent ):Void {
                                        scyDynamicsWrapper.saveModelAction();
										setScyWindowTitle("Model: ");
                                    }
                                }
                                CommandText{
                                    label:"SaveAs Model"
                                    clickAction:function( e: MouseEvent ):Void {
                                        scyDynamicsWrapper.saveAsModelAction();
										setScyWindowTitle("Model: ");
                                    }
                                }
                                CommandText{
                                    label:"Save DataSet"
                                    clickAction:function( e: MouseEvent ):Void {
                                        scyDynamicsWrapper.saveDatasetAction();
										setScyWindowTitle("Dataset: ");
                                    }
                                }
                                CommandText{
                                    label:"SaveAs DataSet"
                                    clickAction:function( e: MouseEvent ):Void {
                                        scyDynamicsWrapper.saveAsDatasetAction();
										setScyWindowTitle("DataSet: ");
                                    }
                                }
                            ]
                        }
                        SwingComponent.wrap(modelEditor)
                    ]
                }
            ]
        };
    }
}


    public function createScyDynamicsNode(roolo:Roolo):ScyDynamicsNode{
        var modelEditor = new ModelEditor();
        var scyDynamicsWrapper = new ScyDynamicsWrapper(modelEditor);
        scyDynamicsWrapper.setRepository(roolo.repository);
		scyDynamicsWrapper.setMetadataTypeManager(roolo.metadataTypeManager);
		scyDynamicsWrapper.setEloFactory(roolo.eloFactory);
        
        return ScyDynamicsNode{
            modelEditor:modelEditor;
            scyDynamicsWrapper:scyDynamicsWrapper;
    }
    }

    public function createScyDynamicsWindow(roolo:Roolo):ScyWindow{
    return
    createScyDynamicsWindow(ScyDynamicsNode.createScyDynamicsNode(roolo));
    }

    public function createScyDynamicsWindow(scyDynamicsNode:ScyDynamicsNode):ScyWindow{
        var scyDynamicsWindow = ScyWindow{
            translateX: 10;
            color:Color.GREEN;
            title:"SimQuest";
            scyContent: scyDynamicsNode;
            minimumWidth:320;
            minimumHeight:100;
            cache:true;
        }
	scyDynamicsWindow.openWindow(810,540);
        scyDynamicsNode.scyWindow = scyDynamicsWindow;
    return scyDynamicsWindow;
    }

function run(){
    Stage {
		title: "ScyDynamics node test"
		width: 250
		height: 250
		scene: Scene {
			content:ScyDynamicsNode{
            }

		}
	}

}

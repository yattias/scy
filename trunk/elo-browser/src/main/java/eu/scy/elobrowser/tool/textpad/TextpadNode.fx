/*
 * DrawingNode.fx
 *
 * Created on 18-dec-2008, 15:19:52
 */

package eu.scy.elobrowser.tool.textpad;

import eu.scy.elobrowser.main.Roolo;
import eu.scy.elobrowser.tool.textpad.EloTextpadWrapper;
import eu.scy.elobrowser.tool.textpad.TextpadNode;
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
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * @author weinbrenner
 */

public class TextpadNode extends CustomNode {

    public-init var textarea : JTextArea;

    public-init var eloTextpadActionWrapper:EloTextpadWrapper;

	public var scyWindow:ScyWindow on replace {
		setScyWindowTitle()
    };

     public function loadElo(uri:URI){
        eloTextpadActionWrapper.loadElo(uri);
        setScyWindowTitle();
     }

	function setScyWindowTitle(){
		if (scyWindow == null)
		return;
		scyWindow.title = "Textpad: {eloTextpadActionWrapper.getDocName()}";
		scyWindow.title = "Textpad";
		var eloUri = eloTextpadActionWrapper.getEloUri();
		if (eloUri != null)
        scyWindow.id = eloUri.toString()
		else
        scyWindow.id = "";

    }

    public override function create(): Node {
        var scrollpane = new JScrollPane(textarea);
        var textareaNode : Node;
        var g = Group {
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
                                        eloTextpadActionWrapper.newTextAction();
										setScyWindowTitle();
                                    }
                                }
                                CommandText{
                                    label:"Load"
                                    clickAction:function( e: MouseEvent ):Void {
                                        eloTextpadActionWrapper.loadTextAction();
										setScyWindowTitle();
                                    }
                                }
                                CommandText{
                                    label:"Save"
                                    clickAction:function( e: MouseEvent ):Void {
                                        eloTextpadActionWrapper.saveTextpadAction();
                                        setScyWindowTitle();
                                    }
                                }
                                CommandText{
                                    label:"Save as"
                                    clickAction:function( e: MouseEvent ):Void {
                                        eloTextpadActionWrapper.saveAsTextpadAction();
										setScyWindowTitle();
                                    }
                                }
                            ]
                        },
                        textareaNode = SwingComponent.wrap(scrollpane)
                    ]
                }
            ]
        };
        textareaNode.translateX = 5;
        return g;
    }
}


    public function createTextpadNode(roolo:Roolo):TextpadNode{
        var textarea = new JTextArea(10, 25);
        textarea.setOpaque(false);
        textarea.setWrapStyleWord(true);
        textarea.setLineWrap(true);

        var eloTextpadActionWrapper= new EloTextpadWrapper(textarea);
        eloTextpadActionWrapper.setRepository(roolo.repository);
        eloTextpadActionWrapper.setMetadataTypeManager(roolo.metadataTypeManager);
        eloTextpadActionWrapper.setEloFactory(roolo.eloFactory);
        return TextpadNode{
            textarea : textarea
            eloTextpadActionWrapper:eloTextpadActionWrapper;
    }
    }

    public function createTextpadWindow(roolo:Roolo):ScyWindow{
    return
        createTextpadWindow(TextpadNode.createTextpadNode(roolo));
    }

    public function createTextpadWindow(textpadNode:TextpadNode):ScyWindow{
        var textpadWindow = ScyWindow{
            color:Color.ORANGE
            title:"Textpad"
            scyContent: textpadNode
            cache:true;
      }
        textpadWindow.openWindow(320, 240);
        textpadNode.scyWindow = textpadWindow;
    return textpadWindow;
    }

function run(){
    Stage {
        title: "Textpad"
        width: 250
        height: 250
        scene: Scene {
            content:TextpadNode{
            }

        }
    }

}

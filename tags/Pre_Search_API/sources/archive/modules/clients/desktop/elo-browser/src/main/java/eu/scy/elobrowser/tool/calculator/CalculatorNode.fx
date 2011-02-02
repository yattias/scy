/*
 * DrawingNode.fx
 *
 * Created on 18-dec-2008, 15:19:52
 */

package eu.scy.elobrowser.tool.calculator;

import eu.scy.elobrowser.main.Roolo;
import eu.scy.elobrowser.tool.calculator.EloCalculatorWrapper;
import eu.scy.elobrowser.tool.calculator.CalculatorNode;
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

import org.jfxtras.scene.layout.Grid;
import org.jfxtras.scene.layout.Row;
import org.jfxtras.scene.layout.Cell;
import eu.scy.elobrowser.tool.calculator.CalculatorDisplay;
import eu.scy.elobrowser.tool.calculator.CalculatorKey;
import eu.scy.elobrowser.tool.calculator.CalculatorModel;

/**
 * @author Jeremy Toussaint
 */

public class CalculatorNode extends CustomNode {

    public-init var textarea : JTextArea;

    public-init var eloCalculatorActionWrapper:EloCalculatorWrapper;

	public var scyWindow:ScyWindow on replace {
		setScyWindowTitle()
    };

     public function loadElo(uri:URI){
        eloCalculatorActionWrapper.loadElo(uri);
        setScyWindowTitle();
     }

	function setScyWindowTitle(){
		if (scyWindow == null)
		return;
		scyWindow.title = "Calculator: {eloCalculatorActionWrapper.getDocName()}";
		scyWindow.title = "Calculator";
		var eloUri = eloCalculatorActionWrapper.getEloUri();
		if (eloUri != null)
        scyWindow.id = eloUri.toString()
		else
        scyWindow.id = "";

    }
/*
    public override function create(): Node {
                // The model
        def model = CalculatorModel {};
        def columns = 4;
        def keyPositionMap = [
            7,  8,  9, 10,
            4,  5,  6, 11,
            1,  2,  3, 12,
            14,  0, 15, 13
        ];


        var scrollpane = new JScrollPane(textarea);
        var textareaNode : Node;
        var g = Group {
            blocksMouse:true;
            content: [ 
                    Grid {
                    border: 10
                    hgap: 10
                    vgap: 20
                    rows: [
                        Row {
                            cells: Cell {
                                columnSpan: columns
                                content: CalculatorDisplay {
                                    text: bind model.display
                                    width: 280
                                    height: 50
                                }
                            }
                        },
                        for (i in [0..<(
                            sizeof keyPositionMap / columns)]) {
                            Row {
                                cells:
                                for (j in [0..<columns]) {
                                    CalculatorKey {
                                        var keyIndex = keyPositionMap[
                                        i * columns + j];
                                        var keyModel = model.keys[keyIndex];
                                        character: keyModel.character
                                        action: keyModel.action
                                        styleClass: "calculatorKey"
                                        id: keyModel.description
                                    }
                                }
                            }
                        }
                    ]
                },
                textareaNode = SwingComponent.wrap(scrollpane)
            ]
        };
        return g;
    }
   */
    public override function create(): Node {
        def model = CalculatorModel {};
        def columns = 4;
        def keyPositionMap = [
            7,  8,  9, 10,
            4,  5,  6, 11,
            1,  2,  3, 12,
            14,  0, 15, 13
        ];

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
                                Grid {
                                    border: 10
                                    hgap: 10
                                    vgap: 20
                                    rows: [
                                        Row {
                                            cells: Cell {
                                                columnSpan: columns
                                                content: CalculatorDisplay {
                                                    text: bind model.display
                                                    width: 280
                                                    height: 50
                                                }
                                            }
                                        },
                                        for (i in [0..<(
                                            sizeof keyPositionMap / columns)]) {
                                            Row {
                                                cells:
                                                for (j in [0..<columns]) {
                                                    CalculatorKey {
                                                        var keyIndex = keyPositionMap[
                                                        i * columns + j];
                                                        var keyModel = model.keys[keyIndex];
                                                        character: keyModel.character
                                                        action: keyModel.action
                                                        styleClass: "calculatorKey"
                                                        id: keyModel.description
                                                    }
                                                }
                                            }
                                        }
                                    ]
                                }
                            ]
                        },
                        textareaNode = SwingComponent.wrap(scrollpane)
                    ]
                }
            ]
        };
        textareaNode.translateX = 5;
        textareaNode.translateY = 10;
        return g;
    }
}

    public function createCalculatorNode(roolo:Roolo):CalculatorNode{
        var textarea = new JTextArea(10, 25);
        textarea.setOpaque(false);
        textarea.setWrapStyleWord(true);
        textarea.setLineWrap(true);

        var eloCalculatorActionWrapper= new EloCalculatorWrapper(textarea);
        eloCalculatorActionWrapper.setRepository(roolo.repository);
        eloCalculatorActionWrapper.setMetadataTypeManager(roolo.metadataTypeManager);
        eloCalculatorActionWrapper.setEloFactory(roolo.eloFactory);
        return CalculatorNode{
            textarea : textarea
            eloCalculatorActionWrapper:eloCalculatorActionWrapper;
    }
    }

    public function createCalculatorWindow(roolo:Roolo):ScyWindow{
        return createCalculatorWindow(CalculatorNode.createCalculatorNode(roolo));
    }

    public function createCalculatorWindow(calculatorNode:CalculatorNode):ScyWindow{
        var calculatorWindow = ScyWindow {
            color:Color.ORANGE
            title:"Calculator"
            scyContent: calculatorNode
            cache:true;
      }
        calculatorWindow.openWindow(320, 360);
        calculatorNode.scyWindow = calculatorWindow;
    return calculatorWindow;
    }

function run(){
    Stage {

        title: "Calculator"
        width: 320
        height: 360
        scene: Scene {
            content:CalculatorNode{
            }

        }
    }

}

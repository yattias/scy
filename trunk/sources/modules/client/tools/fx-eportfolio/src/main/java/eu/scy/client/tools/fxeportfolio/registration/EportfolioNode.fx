package eu.scy.client.tools.fxeportfolio.registration;

import javafx.scene.CustomNode;
import javafx.scene.Node;
import javafx.scene.layout.Resizable;
import eu.scy.client.desktop.scydesktop.tools.ScyToolFX;
import eu.scy.client.desktop.scydesktop.tools.EloSaverCallBack;
import roolo.elo.api.IELO;
import java.net.URI;
import javafx.scene.Group;
import javafx.scene.layout.Stack;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.control.Label;
import javafx.geometry.HPos;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.Button;
import javafx.scene.text.TextAlignment;
import javafx.geometry.VPos;
import javafx.geometry.Insets;
import java.util.Random;
import java.lang.System;


public class EportfolioNode extends CustomNode, Resizable, ScyToolFX, EloSaverCallBack {

    public override var width on replace {resizeContent()};
    public override var height on replace {resizeContent()};

    var stageGroup:Group;
    public var userName:String;
    var list = ["Select ...", "My obligatory ELO no. 1", "My obligatory ELO no. 2", "My obligatory ELO no. 3", "My obligatory ELO no. 4"];
    public var statusString:String = "creating";
    var count = 0;

    var obligatoryElosChoiceBox:ChoiceBox;
    var addObligatoryElosButton:Button;
    var obligatoryEloHBox: HBox;
    var toolSelectionHBox: HBox;
    var toolInstructionsHBox: HBox;
    var toolViewHBox: HBox;
    var addButtonVisible:Boolean = false;
    var currentNode:String = "box0";


    def obligatoryElosChoiceBoxHandler = bind obligatoryElosChoiceBox.selectedItem on replace {
        if(obligatoryElosChoiceBox.selectedIndex > 0) {
            addButtonVisible = true;
        }
        else {
            addButtonVisible = false;
        }

        var oldNode:Node = stageGroup.lookup(currentNode);
        oldNode.visible = false;
        currentNode = "box{obligatoryElosChoiceBox.selectedIndex}";
        var newNode:Node = stageGroup.lookup(currentNode);
        newNode.visible = true;
    };
    
    public override function initialize(windowContent:Boolean):Void{
    }

    public override function loadElo(uri:URI){
    }

    override public function eloSaveCancelled (elo : IELO) : Void {
    }

    override public function eloSaved (elo : IELO) : Void {
    }

    override function postInitialize():Void {
    }

    public override function getPrefHeight(height: Number) : Number{
        //return Container.getNodePrefHeight(wrappedRichTextEditor, height)+wrappedRichTextEditor.boundsInParent.minY+spacing;
        return getMinHeight();
    }

    public override function getPrefWidth(width: Number) : Number{
        //return Container.getNodePrefWidth(wrappedRichTextEditor, width);
        return getMinWidth();
    }

    public override function getMinHeight() : Number {
        return 400;
    }

    public override function getMinWidth() : Number {
        return 600;
    }

    function resizeContent(): Void{
        //Container.resizeNode(wrappedRichTextEditor,width,height-wrappedRichTextEditor.boundsInParent.minY-spacing);
    }

    var bgRect = Rectangle {
        width: bind this.width;
        height: 50;
        fill: LinearGradient {
            startX: 0.0, startY: 0.0, endX: 0.0, endY: 1.0
            proportional: true
            stops: [ Stop { offset: 0.0 color: Color.BLACK },
                     Stop { offset: 1.0 color: Color.GREY } ]
        };
        strokeWidth: 1.0;
    };
    
    def colors = [
        Color.web("#d02020"),
        Color.web("#ff8010"),
        Color.web("#d0e000"),
        Color.web("#10c010"),
        Color.web("#3030f0"),
        Color.web("#d050ff"),
    ];

    def rand = new Random();

    function randColor():Color {
        colors[rand.nextInt(sizeof colors)]
    }


    public override function create(): Node {
        return stageGroup = Group {
            blocksMouse:true;
            content: [
                VBox {
                    content: [
                        Stack {
                            content: [
                                bgRect,
                                Label {
                                    width: bind this.width;
                                    text: "Hey {userName}\nThis is your eportfolio tool. Here you can do very cool things!!"
                                    font: Font { size: 13 }
                                    textFill: Color.WHITE;
                                    hpos: HPos.CENTER;
                                    textAlignment: TextAlignment.CENTER;
                                }
                            ]
                        },
                    ]
                },
                toolSelectionHBox = HBox {
                    translateY: 60;
                    spacing: 10;
                    width: bind this.width;
                    content: [
                        obligatoryEloHBox = HBox {
                            translateX: 10;
                            spacing: 10;
                            content: [
                                obligatoryElosChoiceBox = ChoiceBox {
                                    items: bind list;
                                },
                                addObligatoryElosButton = Button {
                                    text: "ADD";
                                    visible: bind addButtonVisible;
                                    action: function() {
                                    }
                                }
                            ]
                        },
                        HBox {
                            width: this.width - (obligatoryEloHBox.translateX + obligatoryEloHBox.width);
                            nodeHPos: HPos.RIGHT;
                            nodeVPos: VPos.BASELINE;
                            content: [
                                HBox {
                                    content: [
                                        Label {
                                            text: "Status: ";
                                            textFill: Color.BLACK;
                                            textAlignment: TextAlignment.LEFT;
                                        },
                                        Label {
                                            text: "{statusString}";
                                            textFill: Color.GREEN;
                                            textAlignment: TextAlignment.LEFT;
                                        }
                                    ]
                                }
                            ]
                        }
                    ]
                },
                toolInstructionsHBox = HBox {
                    id: "box0";
                    translateY: bind (toolSelectionHBox.translateY + toolSelectionHBox.height);
                    width: bind this.width;
                    height: bind (this.height - (toolSelectionHBox.translateY+toolSelectionHBox.height+toolViewHBox.height));
                    nodeVPos: VPos.CENTER;
                    nodeHPos: HPos.CENTER;
                    content: [
                        Stack {
                            content: [
                                Rectangle {
                                    width: 400;
                                    height: 200;
                                    fill: LinearGradient {
                                        startX: 0.0, startY: 0.0, endX: 0.0, endY: 1.0
                                        proportional: true;
                                        stops: [
                                            Stop { offset: 0.0 color: Color.BLACK },
                                            Stop { offset: 1.0 color: Color.GREY }
                                        ]
                                    };
                                    strokeWidth: 1.0;
                                },
                                Label {
                                    width: 380;
                                    text: "Choose an obligatory ELO from the comboBox and add ELOs to your portfolio."
                                    font: Font { size: 18 }
                                    textFill: Color.WHITE;
                                    textWrap: true;
                                    textAlignment: TextAlignment.CENTER;
                                }
                            ]
                        }
                    ]
                },
                for (b in list) {
                    count++;
                    HBox {
                        id: "box{count}";
                        visible: false;
                        translateY: bind (toolSelectionHBox.translateY + toolSelectionHBox.height);
                        width: bind this.width;
                        height: bind (this.height - (toolSelectionHBox.translateY+toolSelectionHBox.height+toolViewHBox.height));
                        nodeVPos: VPos.CENTER;
                        nodeHPos: HPos.CENTER;
                        content: [
                            Stack {
                                content: [
                                    Rectangle {
                                        width: bind toolInstructionsHBox.width;
                                        height: bind toolInstructionsHBox.height;
                                        fill: randColor();
                                    }
                                ]
                            }
                        ]
                    }
                }
                toolViewHBox = HBox {
                    width: bind this.width;
                    translateY: bind (this.height - toolViewHBox.height);
                    nodeHPos: HPos.CENTER;
                    padding: Insets { bottom: 10 top: 10 }
                    content: [
                        Stack {
                            content: [
                                Button {
                                    text: "VIEW";
                                    action: function() {
                                    }
                                }
                            ]
                        }
                    ]
                }
            ]
        };
    }
}

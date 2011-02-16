/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.tools.fxsocialtaggingtool;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.CustomNode;
import eu.scy.client.desktop.scydesktop.tools.ScyToolFX;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextBox;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.scene.control.Button;
import java.lang.Runtime;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.layout.Stack;
import javafx.scene.layout.HBox;
import eu.scy.client.tools.fxsocialtaggingtool.UIElements.*;
import javafx.scene.layout.LayoutInfo;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.ListView;
import javafx.scene.layout.Flow;
import javafx.scene.control.Hyperlink;
//import eu.scy.client.tools.fxsocialtaggingtool.Tag;
//import eu.scy.client.tools.fxsocialtaggingtool.UIElements;

public class SocialTaggingDrawer extends CustomNode, ScyToolFX {

    public var scyWindow: ScyWindow;
    def valueOffset = 0.0; //70.0;
    def spacing = 3.0;
    def border = 0.0; //5.0;
    var cacheCheckbox: CheckBox;
    var layoutXValue: TextBox;
    var layoutYValue: TextBox;

    public override function create(): Node {
        var taggingDisplay = createSocialTaggingDisplay();
        return Group {
                    content: [
                        Rectangle {
                            x: 0, y: 0
                            width: taggingDisplay.layoutBounds.width + 2 * border,
                            height: taggingDisplay.layoutBounds.height + 2 * border - spacing
                            fill: Color.YELLOW
                        }
                        taggingDisplay
                    ]
                };
    }

    function createSocialTaggingDisplay(): Node {

        def headingFont = Font {
                    size: 24
                }

//        def taggingPanelDescription = Text {
//                    font: headingFont
//                    content: "Tagging drawer:"
//                }
        def tagCloudDescription = Text {
                    font: headingFont
                    content: "Tag cloud:"
                }

        def testTags = [
                    Tag {
                        tagname: "ecology";
                        ayevoters: ["Astrid", "Wilhelm"];
                        nayvoters: ["John", "Mary"];
                    }
                    Tag {
                        tagname: "environment";
                        ayevoters: ["Astrid", "Mary", "Wilhelm"];
                        nayvoters: ["John"];
                    }
                    Tag {
                        tagname: "language";
                        ayevoters: ["John"];
                        nayvoters: ["Mary", "Wilhelm"];
                    }
                    Tag {
                        tagname: "temperature";
                        ayevoters: ["John"];
                    }
                    Tag {
                        tagname: "energy"
                        ayevoters: ["Mary"];
                    }
                    Tag {
                        tagname: "carbon dioxide"
                        ayevoters: ["Mary"];
                    }
                ];

        def tagLines = for (tag in testTags) {
                    def ayevoterslist = for (voter in tag.ayevoters)
                                HBox { content: [SmallPlus {}, HBox { content: [Text { content: voter
                                                    layoutInfo: LayoutInfo {
                                                        width: 50
                                                        vpos: VPos.CENTER
                                                        hpos: HPos.LEFT } }]
                                            layoutInfo: LayoutInfo {
                                                width: 100
                                                hpos: HPos.LEFT }
                                        }] }
                    def nayvoterslist = for (voter in tag.nayvoters)
                                HBox { content: [SmallMinus {}, HBox { content: [Text { content: voter
                                                    layoutInfo: LayoutInfo {
                                                        width: 50
                                                        vpos: VPos.CENTER
                                                        hpos: HPos.LEFT } }]
                                            layoutInfo: LayoutInfo {
                                                width: 100
                                                hpos: HPos.LEFT }
                                        }] }
                    def winning = bind if (tag.ayevoters.size() < tag.nayvoters.size())
                                Minus {} else Plus {};

                    VBox {
                        content: [
                            HBox {
                                width: 50
                                nodeVPos: VPos.TOP
                                content: [

                                    Label {
                                        layoutInfo: LayoutInfo {
                                            width: labelWidth
                                            vpos: VPos.CENTER
                                        }
                                        font: Font {
                                            size: 16
                                        }

                                        text: tag.tagname
                                    },

                                    winning,

                                    ThumbsUp {},

                                    ThumbsDown {}] }
                            VBox { layoutInfo: LayoutInfo { height: bind if (winning.hover) then 100 else -1 }
                                content: [HBox {
                                        content: [
                                            HBox {
                                                layoutInfo: LayoutInfo {
                                                    width: labelWidth
                                                } },
                                            VBox {
                                                layoutInfo: LayoutInfo {
                                                    hpos: HPos.LEFT
                                                }
                                                content: ayevoterslist }]
                                        layoutInfo: LayoutInfo {
                                            hpos: HPos.LEFT
                                        } },
                                    HBox { content: [
                                            HBox {
                                                layoutInfo: LayoutInfo {
                                                    width: labelWidth
                                                } },
                                            VBox {
                                                layoutInfo: LayoutInfo {
                                                    hpos: HPos.LEFT
                                                }
                                                content: nayvoterslist }]
                                        layoutInfo: LayoutInfo {
                                            hpos: HPos.LEFT
                                        } }] }
                        //VBox { content: nayvoterslist }
                        ] } }
        def tagGroup = ListView {
                    items: tagLines
                }
        def newTagBox = TextBox { text: "New tags here"
                    font: Font {
                        size: 16
                    } }

        def tagCloud = Flow {
                    //                    layoutInfo: LayoutInfo {
                    //                        width: 300
                    //                    }
                    hgap: 10
                    content: for (tag in testTags) {
                        Hyperlink { text: tag.tagname
                            font: Font {
                                //size:Math.max(8, (8 * (tag.ayevoters.size() - tag.nayvoters.size())))
                                size: 12 * (tag.ayevoters.size())
                            }
                        }

                    }
                }

        def taggingPanel = VBox {
                    layoutX: border;
                    layoutY: border;
                    spacing: spacing
                    content: [tagGroup, newTagBox]
                }

        def tagCloudPanel = VBox {
                    content: [tagCloud]
                }
        //        Stage {
        //            title: "Tag Cloud for SCY ELOs"
        //            scene: Scene {
        //                content: VBox {
        //                    content: [
        //                        tagCloudPanel]
        //                }
        //            }
        //        }
        VBox {
            spacing: spacing
            layoutX: valueOffset
            //layoutY: layoutY
            content: [
                HBox {
                    content: [
                        Text {
                            font: Font {
                                size: 16
                            }
                            x: 10
                            y: 30
                        // content: "SCY Tagging Tool"
                        }] }
                VBox {
                    content: [
                        tagCloudPanel, taggingPanel
                    ]
                }]
        }

//        Stage {
//            title: "SCY Tagging Tool"
//            scene: Scene {
//                width: 1000
//                height: 600
//                content: [
//                    VBox {
//                        spacing: 30
//                        layoutX: 50
//                        layoutY: 50
//                        content: [
//                            HBox {
//                                content: [
//                                    Text {
//                                        font: Font {
//                                            size: 16
//                                        }
//                                        x: 10
//                                        y: 30
//                                    // content: "SCY Tagging Tool"
//                                    }] }
//                            HBox {
//                                content: [
//                                    ELO, taggingPanel
//                                ]
//                            }]
//                    }
//                ]
//            }
//        }
    }

    function createPropertiesDisplay(): Node {

        def window = scyWindow;
        println("window: {window}");
        VBox {
            layoutX: border;
            layoutY: border;
            spacing: spacing
            content: [
                Group {
                    content: [
                        Label {
                            text: "cache"
                        }
                        cacheCheckbox = CheckBox {
                                    layoutX: valueOffset;
                                    text: ""
                                    allowTriState: false
                                    selected: bind window.cache with inverse
                                }
                    ]
                }
                Group {
                    content: [
                        Label {
                            text: "layoutX"
                        }
                        layoutXValue = TextBox {
                                    layoutX: valueOffset;
                                    text: bind "{window.layoutX}"
                                    columns: 12
                                    selectOnFocus: true
                                    editable: false
                                }
                    ]
                }
                Group {
                    content: [
                        Label {
                            text: "layoutY"
                        }
                        layoutYValue = TextBox {
                                    layoutX: valueOffset;
                                    text: bind "{window.layoutY}"
                                    columns: 12
                                    selectOnFocus: true
                                    editable: false
                                }
                    ]
                }
                Button {
                    text: "GC"
                    action: function() {
                        Runtime.getRuntime().gc();
                    }
                }
            ]
        }

    }

}

function run() {

    Stage {
        title: "Social Tagging"
        scene: Scene {
            width: 200
            height: 200
            content: [
                SocialTaggingDrawer {
                }
            ]
        }
    }

}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.tools.fxsocialtaggingtool;

import javafx.scene.control.*;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.CustomNode;
import eu.scy.client.desktop.scydesktop.tools.ScyToolFX;
import javafx.scene.layout.VBox;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import javafx.stage.Stage;
import javafx.scene.Scene;
import java.lang.Runtime;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.layout.HBox;
import eu.scy.client.tools.fxsocialtaggingtool.UIElements.*;
import javafx.scene.layout.LayoutInfo;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.layout.Flow;
import javafx.util.Math;
import javafx.scene.layout.Resizable;

public class SocialTaggingDrawer extends CustomNode, ScyToolFX, Resizable {

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
                        taggingDisplay
                    ]
                };
    }

    function createSocialTaggingDisplay(): Node {

        def headingFont = Font {
                // size: 22
                }

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
                                Flow { content: [SmallPlus {}, HBox { content: [Text { content: voter
                                                    layoutInfo: LayoutInfo {
                                                        //width: 50
                                                        vpos: VPos.CENTER
                                                        hpos: HPos.LEFT } }]
                                            layoutInfo: LayoutInfo {
                                                //width: 100
                                                hpos: HPos.LEFT }
                                        }] }
                    def nayvoterslist = for (voter in tag.nayvoters)
                                Flow { content: [SmallMinus {}, HBox { content: [Text { content: voter
                                                    layoutInfo: LayoutInfo {
                                                        //width: 50
                                                        vpos: VPos.CENTER
                                                        hpos: HPos.LEFT } }]
                                            layoutInfo: LayoutInfo {
                                                //width: 100
                                                hpos: HPos.LEFT }
                                        }] }
                    def winning = bind if (tag.ayevoters.size() < tag.nayvoters.size())
                                Minus { tag: tag } else Plus { tag: tag };
                    def tagline =
                            Flow {
                                //width: 50
                                nodeVPos: VPos.TOP
                                hgap: 5
                                content: [
                                    winning,

                                    ThumbsUp { tag: tag },

                                    ThumbsDown { tag: tag },
                                    Label {
                                        layoutInfo: LayoutInfo {
                                            width: labelWidth
                                            vpos: VPos.CENTER
                                        }
                                        font: Font {
                                        //size: 16
                                        }

                                        text: tag.tagname
                                    },
                                ] }
                    def taglinedetails = VBox { // Detailed tag view
                                //layoutInfo: LayoutInfo { height: bind if (winning.hover) 10000 else 0 }
                                content: [HBox {
                                        content: [
                                            //                                            HBox {
                                            //                                                layoutInfo: LayoutInfo {
                                            //                                                    width: labelWidth
                                            //                                                } },
                                            VBox {
                                                layoutInfo: LayoutInfo {
                                                    hpos: HPos.LEFT
                                                }
                                                content: ayevoterslist }]
                                        layoutInfo: LayoutInfo {
                                            hpos: HPos.LEFT
                                        } },
                                    HBox { content: [
                                            //                                            HBox {
                                            //                                                layoutInfo: LayoutInfo {
                                            //                                                    width: labelWidth
                                            //                                                } },
                                            VBox {
                                                layoutInfo: LayoutInfo {
                                                    hpos: HPos.LEFT
                                                }
                                                content: nayvoterslist }]
                                        layoutInfo: LayoutInfo {
                                            hpos: HPos.LEFT
                                        } }] }
                    VBox { content: bind if (winning.hover) [tagline, taglinedetails] else [tagline] } }

        def tagGroup = ListView {
                    items: tagLines
                }
        def newTagBox = TextBox { text: "New tags here"
                    font: Font {
                    //size: 16
                    } }

        def tagCloud = Flow {
                    //                    layoutInfo: LayoutInfo {
                    //                        width: 300
                    //                    }
                    hgap: 10

                    content: for (tag in testTags)
                        Hyperlink { text: tag.tagname
                            font: Font {
                                //size:Math.max(8, (8 * (tag.ayevoters.size() - tag.nayvoters.size())))
                                size: 12 * (Math.max(0, (tag.ayevoters.size() - tag.nayvoters.size())))
                            }
                        }
                }

        def taggingPanel = VBox {
                    content: [tagGroup, newTagBox]
                }

        def tagCloudPanel = VBox {
                    content: [tagCloud]
                }
        ScrollView {
            layoutInfo: LayoutInfo {
                //    height: 1000 //this.height;
                //    width: 1000 //bind this.width;
                hfill: true
                vfill: true
            }
            //
            //            hbarPolicy: ScrollBarPolicy.NEVER
            //            vbarPolicy: ScrollBarPolicy.ALWAYS

            node: VBox {
                content: [
                    tagCloudPanel, taggingPanel
                ]
            }
        }

    }

    public override function getPrefHeight(height: Number): Number {
        return 200;
    }

    public override function getPrefWidth(width: Number): Number {
        return 200;
    }

}

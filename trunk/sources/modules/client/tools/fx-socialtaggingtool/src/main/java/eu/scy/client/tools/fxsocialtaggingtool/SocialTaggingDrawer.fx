/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.tools.fxsocialtaggingtool;

import javafx.scene.control.*;
import javafx.scene.Node;
import javafx.scene.CustomNode;
import eu.scy.client.desktop.scydesktop.tools.ScyToolFX;
import javafx.scene.layout.VBox;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
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

public class SocialTaggingDrawer
        extends
        CustomNode, ScyToolFX, Resizable {

    public var scyWindow: ScyWindow;
    def valueOffset = 0.0;
    def spacing = 3.0;
    def border = 0.0;
    def eloInterface = ELOInterface {
                tbi: scyWindow.tbi;
                eloUri: scyWindow.eloUri;
            }
    def currentUser = bind eloInterface.getCurrentUser();
    var cacheCheckbox: CheckBox;
    var layoutXValue: TextBox;
    var layoutYValue: TextBox;
    var taggingDisplay: Node;
    var tagGroup: ListView;
    var tagLines: Object[];
    var panelWidth = 200;

    public override function create(): Node {
        this.taggingDisplay = createSocialTaggingDisplay();

        return this.taggingDisplay;
    }

    function createTagLines(tags: Tag[]): Object[] {
        def tagLines = for (tag in tags) {
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
                    def ayeVoter = (tag.ayevoters[v | v.equals(currentUser)]).size() > 0;
                    def nayVoter = (tag.nayvoters[v | v.equals(currentUser)]).size() > 0;

                    def tagline =
                            Flow {
                                //width: 50
                                nodeVPos: VPos.TOP
                                hgap: 5

                                content: [
                                    winning,

                                    ThumbsUp { tag: tag
                                        alwaysOn: bind ayeVoter
                                        onMouseClicked: function(e) {
                                            // Remove a vote first (this should really be handled
                                            // in the eloInterface
                                            eloInterface.addVoteForTag(true, tag);
                                            this.updateTagLines();
                                        }
                                    }

                                    ThumbsDown { tag: tag
                                        alwaysOn: bind nayVoter
                                        onMouseClicked: function(e) {
                                            // Remove a vote first (this should really be handled
                                            // in the eloInterface
                                            eloInterface.addVoteForTag(false, tag);
                                            this.updateTagLines();
                                        }
                                    }
                                    Label {
                                        layoutInfo: LayoutInfo {
                                            //width: labelWidth
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
                                            VBox {
                                                layoutInfo: LayoutInfo {
                                                    hpos: HPos.LEFT
                                                }
                                                content: nayvoterslist }]
                                        layoutInfo: LayoutInfo {
                                            hpos: HPos.LEFT
                                        } }] }
                    VBox { content: bind if (winning.hover) [tagline, taglinedetails] else [tagline] } }
    }

    function updateTagLines(): Object[] {
        def tags = bind eloInterface.getAllTags();
        return this.tagLines = createTagLines(tags);
    }

    function createSocialTaggingDisplay(): Node {

        def headingFont = Font {
                // size: 22
                }

        def tagCloudDescription = Text {
                    font: headingFont
                    content: "Tag cloud:"
                }

        tagGroup = ListView {
                    //layoutInfo: LayoutInfo{width:panelWidth}
                    items: bind this.tagLines
                }
        def newTagBox = TextBox {
                    text: ""
                    selectOnFocus: true
                    font: Font {
                    }
                    tooltip: Tooltip {
                        text: "New tags can be entered here"
                    }
                }

        def newTagButton = Button {
                    text: "Add"
                    tooltip: Tooltip {
                        text: "Adds a tag, and your vote for that tag"
                    }
                    action: function() {
                        eloInterface.addVoteForString(true, newTagBox.text);
                        this.updateTagLines();
                    }
                }

//        def tagCloud = Flow {
//                    hgap: 10
//
//                    content: for (tag in tags)
//                        Hyperlink { text: tag.tagname
//                            font: Font {
//                                //size:Math.max(8, (8 * (tag.ayevoters.size() - tag.nayvoters.size())))
//                                size: 12 * (Math.max(0, (tag.ayevoters.size() - tag.nayvoters.size())))
//                            }
//                        }
//                }
        this.updateTagLines();

        var taggingPanel = VBox {
                    content: [tagGroup, HBox {
                            content: [newTagBox, newTagButton] }]
                }

        ScrollView {
            //width: bind scene.width
            //width: 300
            //height: bind scene.height
            //   width: bind this.width
            //   height: bind this.height
            layoutInfo: LayoutInfo { width: bind this.width
                height: bind this.height }
            //width:bind this.width
            fitToWidth: true
            fitToHeight: true
            node: /* tagCloud, */ taggingPanel
        }
    }

    public override function getPrefHeight(height: Number): Number {
        return this.height;
    //return scyWindow.height
    }

    public override function getPrefWidth(width: Number): Number {
        //return 200;
        //return getNo   dePreferredWidth(tagGroup);
        return this.panelWidth;
    }

}

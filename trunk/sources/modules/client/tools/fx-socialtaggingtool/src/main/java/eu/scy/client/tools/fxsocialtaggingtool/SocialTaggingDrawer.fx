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
import javafx.scene.layout.Resizable;
import javafx.scene.text.FontWeight;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Container;
import javafx.geometry.Insets;
import eu.scy.collaboration.api.CollaborationStartable;
import eu.scy.client.desktop.scydesktop.tools.DrawerUIIndicator;

public class SocialTaggingDrawer
        extends
        CustomNode, ScyToolFX, Resizable, CollaborationStartable {

    public var scyWindow: ScyWindow;
    def eloInterface = ELOInterface {
        tbi: scyWindow.tbi;
        eloUri: scyWindow.eloUri;
        view: this;
    }
    def currentUser = bind eloInterface.getCurrentUser();
    var tagGroup: Node;
    var mainBox: VBox;
    var tagLines: Node[];
    var newTagBox: TextBox;
    def spacing = 5.0;


    public override function create(): Node {
        return createSocialTaggingDisplay();
    }

    function createTagLines(tags: Tag[]): Node[] {
        def tagLines = for (tag in tags) {
                    def ayevoterslist = for (voter in tag.ayevoters)
                                Flow { content: [SmallPlus {}, HBox { content: [Text { content: voter
                                                    layoutInfo: LayoutInfo {
                                                        vpos: VPos.CENTER
                                                        hpos: HPos.LEFT } }]
                                            layoutInfo: LayoutInfo {
                                                hpos: HPos.LEFT }
                                        }] }
                    def nayvoterslist = for (voter in tag.nayvoters)
                                Flow { content: [SmallMinus {}, HBox { content: [Text { content: voter
                                                    layoutInfo: LayoutInfo {
                                                        vpos: VPos.CENTER
                                                        hpos: HPos.LEFT } }]
                                            layoutInfo: LayoutInfo {
                                                hpos: HPos.LEFT }
                                        }] }
                    def winning = bind if (tag.ayevoters.size() < tag.nayvoters.size())
                                Minus { tag: tag } else Plus { tag: tag };
                    def ayeVoter = (tag.ayevoters[v | v.equals(currentUser)]).size() > 0;
                    def nayVoter = (tag.nayvoters[v | v.equals(currentUser)]).size() > 0;

                    def tagline =
                            HBox {
                                nodeVPos: VPos.CENTER
                                spacing: 5
                                nodeHPos: HPos.LEFT
                                content: [
                                    winning,

                                    ThumbsUp { tag: tag
                                        layoutInfo: LayoutInfo {
                                            hfill: false
                                            hgrow: Priority.NEVER
                                            hpos: HPos.LEFT
                                        }
                                        alwaysOn: bind ayeVoter
                                        onMouseClicked: function(e) {
                                            // Remove a vote first (this should really be handled
                                            // in the eloInterface
                                            eloInterface.addVoteForTag(true, tag);
                                            this.updateTagLines();
                                        }
                                    }

                                    ThumbsDown { 
                                        tag: tag
                                        layoutInfo: LayoutInfo {
                                            hfill: false
                                            hgrow: Priority.NEVER
                                            hpos: HPos.LEFT
                                        }
                                        alwaysOn: bind nayVoter
                                        onMouseClicked: function(e) {
                                            // Remove a vote first (this should really be handled
                                            // in the eloInterface
                                            eloInterface.addVoteForTag(false, tag);
                                            this.updateTagLines();
                                        }
                                    }
                                    Label {
                                        text: tag.tagname
                                        layoutInfo: LayoutInfo {
                                            hfill: true
                                            hgrow: Priority.ALWAYS
                                            hpos: HPos.LEFT
                                        }
                                    },
                                ] }
                    def taglinedetails = VBox { // Detailed tag view
                                content: [HBox {
                                        content: [
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
                    VBox {
                        content: bind if (winning.hover) [tagline, taglinedetails] else [tagline]
                    }
                }
    }

    public function updateTagLines(): Void {
        def tags : Tag[] = eloInterface.getAllTags();
        tagLines = createTagLines(tags);
    }


    function createSocialTaggingDisplay(): Node {

        def tagDrawerDescription = Text {
                    // Most of this is taken from scy/sources/modules/client/desktop/scy-desktop/src/main/java/eu/scy/client/desktop/scydesktop/scywindows/window/WindowTitleBar.fx
                    // A better solution would have been to have this in a resource accessible from everywhere, but right now
                    // the information is private to WindowTitleBar.
                    // Just Subclassing it would have required extensive overriding, since WindowTitleBar contains elements like icons etc.
                    font: Font.font("Verdana", FontWeight.BOLD, 12);
                    //textOrigin: TextOrigin.BOTTOM
                    //x: iconSize + textIconSpace+textInset
                    //y: iconSize-textInset
                    //clip: clipRect
                    fill: scyWindow.windowColorScheme.mainColor
                    content: "Tags"
                }

        tagGroup = VBox {
                    content: bind tagLines
                    layoutInfo: LayoutInfo {
                        vfill: false
                        hfill: false
                        vgrow: Priority.NEVER
                        hgrow: Priority.NEVER
                    }
                }
        newTagBox = TextBox {
                    text: ""
                    selectOnFocus: true
                    font: Font {
                    }
                    tooltip: Tooltip {
                        text: "New tags can be entered here"
                    }
                    action: addTag
                }

        def newTagButton = Button {
                    text: "Add"
                    tooltip: Tooltip {
                        text: "Adds a tag, and your vote for that tag"
                    }
                    disable: bind newTagBox.rawText==""
                    action: addTag
                }

        this.updateTagLines();

      var scrollView = ScrollView {
        style: "-fx-background-color: white; -fx-border-color: grey;"
        layoutInfo: LayoutInfo {
            hfill: true
            vfill: true
            hgrow: Priority.ALWAYS
            vgrow: Priority.ALWAYS
        }
        hbarPolicy: ScrollBarPolicy.AS_NEEDED
        vbarPolicy: ScrollBarPolicy.AS_NEEDED
        node: tagGroup
      };

      return mainBox = VBox {
               managed: false
               spacing: spacing
               nodeHPos: HPos.CENTER
               padding: Insets{
                  top: spacing
                  right: spacing
                  bottom: spacing
                  left: spacing
               }

               content: [
                  tagDrawerDescription,
                  scrollView,
                  HBox {
                    layoutInfo: LayoutInfo {
                        vfill: true
                        vpos: VPos.BOTTOM
                    }
                    spacing: spacing
                    content: [newTagBox, newTagButton]
                  }
               ]
            };
    }
    
    function addTag() : Void {
        eloInterface.addVoteForString(true, newTagBox.text);
        this.updateTagLines();
        newTagBox.text = "";
    }
    

    function sizeChanged(): Void {
      Container.resizeNode(mainBox, width, height);
    }

    public override function getPrefHeight(h: Number): Number {
        return 300;
    }

    public override function getPrefWidth(w: Number): Number {
        return 160;
    }

    public override function startCollaboration(mucId: String) : Void {
        eloInterface.joinSession(mucId);
    }

    public override function stopCollaboration() : Void {
        eloInterface.leaveSession();
    }


    public override var width on replace { sizeChanged() };
   
    public override var height on replace { sizeChanged() };

   public override function getDrawerUIIndicator(): DrawerUIIndicator{
      return DrawerUIIndicator.SOCIAL_TAGGING;
   }
}

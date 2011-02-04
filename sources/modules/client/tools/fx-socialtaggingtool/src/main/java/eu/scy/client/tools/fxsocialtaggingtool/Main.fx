/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.tools.fxsocialtaggingtool;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.control.ListView;
import javafx.scene.layout.Stack;
import javafx.scene.shape.Circle;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.layout.LayoutInfo;
import javafx.scene.input.TextInput;
import javafx.scene.control.TextBox;
import javafx.scene.control.Hyperlink;
import javafx.util.Math;
import javafx.scene.layout.Tile;
import javafx.scene.layout.Panel;
import javafx.scene.layout.Flow;

import eu.scy.client.desktop.scydesktop.ScyDesktopCreator;
import eu.scy.client.tools.fxsocialtaggingtool.registration.SocialTaggingToolCreatorFX;
//
import eu.scy.client.desktop.scydesktop.tools.drawers.xmlviewer.EloXmlViewerCreator;
import eu.scy.client.desktop.scydesktop.Initializer;
import eu.scy.client.desktop.scydesktop.ScyDesktop;
import eu.scy.client.desktop.scydesktop.tools.propertiesviewer.PropertiesViewerCreator;
import eu.scy.client.desktop.scydesktop.tools.scytoolviewer.ScyToolViewerCreator;
import eu.scy.client.desktop.scydesktop.corners.elomanagement.EloManagement;
import eu.scy.client.desktop.scydesktop.mission.MissionRunConfigs;


/**
 * @author sindre
 */

 var initializer = Initializer {
           scyDesktopConfigFile: "config/scyDesktopDrawingTestConfig.xml"
           storeElosOnDisk:false;
           loginType:"local"
           authorMode:true
        }

function createScyDesktop(missionRunConfigs: MissionRunConfigs): ScyDesktop {
   def scyDrawingId = "drawing";
   def eloXmlViewerId = "xmlViewer";
   def scyToolViewerId = "scyToolViewer";
   def propertiesViewerId = "propertiesViewer";

   var scyDesktopCreator = ScyDesktopCreator {
              initializer: initializer;
              missionRunConfigs: missionRunConfigs;
           }
   scyDesktopCreator.scyToolCreatorRegistryFX.registerScyToolCreatorFX(SocialTaggingToolCreatorFX{}, scyDrawingId);
   scyDesktopCreator.scyToolCreatorRegistryFX.registerScyToolCreator(new EloXmlViewerCreator(), eloXmlViewerId);
   scyDesktopCreator.scyToolCreatorRegistryFX.registerScyToolCreatorFX(ScyToolViewerCreator{}, scyToolViewerId);
   scyDesktopCreator.scyToolCreatorRegistryFX.registerScyToolCreatorFX(new PropertiesViewerCreator(), propertiesViewerId);

   var scyDesktop = scyDesktopCreator.createScyDesktop();

   scyDesktop.bottomLeftCornerTool = EloManagement {
      scyDesktop: scyDesktop;
      repository: scyDesktopCreator.config.getRepository();
      metadataTypeManager:scyDesktopCreator.config.getMetadataTypeManager();
      titleKey: scyDesktopCreator.config.getTitleKey();
      technicalFormatKey: scyDesktopCreator.config.getTechnicalFormatKey();
   }

   return scyDesktop;
}



class TagVote {

    /**
     * This is a vote for a Tag
     * The value is binary, as users can select to vote yes or no to an existing tag.
     *
     * Adding a tag entails creating a tag and voting yes to it.
     */
    var username: String;
    var value: Boolean;
}

class Tag {

    var tagname: String;
    var ayevoters: String[];
    var nayvoters: String[];
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
                ayevoters: ["Merete"];
            }
            Tag {
                tagname: "carbon dioxide"
                ayevoters: ["Merete"];
            }


        ];
def iconRadius = 16;
def iconFontSize = 32;
def labelWidth = 100;
def ELOHeading = Text {
            font: Font {
                size: 24
            }
            x: 10
            y: 30
            content: "ELO to be tagged:"
        }
def ELOBody = Rectangle {
            x: 50 y: 50
            width: 300 height: 400
            arcWidth: 20 arcHeight: 20
            fill: Color.GRAY
            stroke: Color.SLATEGREY
            strokeWidth:1

        }
def ELO = VBox { content: [ELOHeading, ELOBody] }

def headingFont =  Font {
                size: 24
            }

def taggingPanelDescription = Text {
            font:headingFont
            content: "Tagging drawer:"
        }

def tagCloudDescription = Text {
            font:headingFont
            content: "Tag cloud:"
        }

class Plus extends Stack {

    override var content = [
                Circle {
                    radius: iconRadius
                    fill: Color.GREEN
                },
                Text {
                    font: Font {
                        size: iconFontSize
                    }
                    fill: Color.WHITE
                    content: "+"
                }
            ]
}

class Minus extends Stack {

    override var content = [
                Circle {
                    radius: iconRadius
                    fill: Color.RED
                },
                Text {
                    font: Font {
                        size: iconFontSize
                    }
                    fill: Color.WHITE
                    content: "-"
                }
            ]
}

class smallPlus extends Stack {

    override var content = [
                Circle {
                    radius: iconRadius / 2
                    fill: Color.GREEN
                },
                Text {
                    font: Font {
                        size: iconFontSize / 2
                    }
                    fill: Color.WHITE
                    content: "+"
                }
            ]
}

class smallMinus extends Stack {

    override var content = [
                Circle {
                    radius: iconRadius / 2
                    fill: Color.RED
                },
                Text {
                    font: Font {
                        size: iconFontSize / 2
                    }
                    fill: Color.WHITE
                    content: "-"
                }
            ]
}

class ThumbsUp extends Stack {

    override var content = [
                Circle {
                    radius: iconRadius
                    fill: Color.GREEN
                },
                Text {
                    font: Font {
                        size: iconFontSize
                        name: "wingdings"
                    }
                    fill: Color.WHITE
                    content: "C"
                }
            ]
}

class ThumbsDown extends Stack {

    override var content = [
                Circle {
                    radius: iconRadius
                    fill: Color.RED
                },
                Text {
                    font: Font {
                        size: iconFontSize
                        name: "wingdings"
                    }
                    fill: Color.WHITE
                    content: "D"
                }
            ]
}
def tagLines = for (tag in testTags) {
            def ayevoterslist = for (voter in tag.ayevoters)
                        HBox { content: [smallPlus {}, HBox { content: [Text { content: voter
                                            layoutInfo: LayoutInfo {
                                                width: 50
                                                vpos: VPos.CENTER
                                                hpos: HPos.LEFT } }]
                                    layoutInfo: LayoutInfo {
                                        width: 100
                                        hpos: HPos.LEFT }
                                }] }
            def nayvoterslist = for (voter in tag.nayvoters)
                        HBox { content: [smallMinus {}, HBox { content: [Text { content: voter
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
                  VBox {layoutInfo: LayoutInfo {height: bind if (winning.hover) then 100 else -1}
                      content:[HBox {
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
                        } }]}
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
    layoutInfo: LayoutInfo {
        width: 300
    }
    hgap: 10
    content: for (tag in testTags) {
          Hyperlink {text:tag.tagname
                font:Font {
                   //size:Math.max(8, (8 * (tag.ayevoters.size() - tag.nayvoters.size())))
                   size: 12 * (tag.ayevoters.size())
                 }
                }


        }
}


def taggingPanel = VBox {
            content: [taggingPanelDescription, tagGroup, newTagBox]
        }

def tagCloudPanel = VBox {
    content: [tagCloud]

}


Stage {
    title: "SCY Tagging Tool"
    scene: Scene {
        width: 1000
        height: 600
        content: [
            VBox {
                spacing: 30
                layoutX: 50
                layoutY: 50
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
                    HBox {
                        content: [
                            ELO, taggingPanel
                        ]
                    }]
            }
        ]
    }

}

Stage {
    title: "Tag Cloud for SCY ELOs"
    scene: Scene {
        content:VBox {
            content: [
                tagCloudPanel]
        }

    }
}

var stage: Stage;
var scene: Scene;

stage = Stage {
   title: "SCY desktop with drawing tool"
   width: 400
   height: 300
	scene: initializer.getScene(createScyDesktop);
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.scywindows.moreinfomanager;

import javafx.scene.CustomNode;
import javafx.scene.layout.VBox;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.Resizable;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.layout.LayoutInfo;
import javafx.scene.control.Label;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Container;
import javafx.scene.control.ScrollView;
import javafx.scene.control.ScrollBarPolicy;
import javafx.geometry.Insets;
import java.util.HashMap;
import javafx.util.Sequences;
import javafx.scene.text.Text;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.lang.System;
import java.text.DateFormat;
import java.util.Comparator;
import javafx.scene.shape.Rectangle;
import javafx.geometry.VPos;
import javafx.scene.control.Separator;
import javafx.geometry.HPos;
import javafx.scene.text.TextAlignment;

/**
 * @author weinbrenner
 */
public class AgendaNode extends CustomNode, Resizable {

    var logEntries: Node[];
    var mainNode: Node;
    var entryUrlMap: HashMap;
    def dateFormat = SimpleDateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
    var messageEntries: Node[];

    public override function create(): Node {
        entryUrlMap = new HashMap();
        var messageScrollView = ScrollView {
                    fitToWidth: true
                    hbarPolicy: ScrollBarPolicy.AS_NEEDED
                    vbarPolicy: ScrollBarPolicy.AS_NEEDED
                    node: VBox {
                        spacing: 5
                        padding: Insets {
                            left: 5
                            right: 5
                            top: 3
                        }
                        layoutInfo: LayoutInfo {
                            hfill: true
                            vfill: false
                            hgrow: Priority.ALWAYS
                            vgrow: Priority.NEVER
                        }
                        content: bind messageEntries;
                    }
                };
        var tableScrollView = ScrollView {
                    fitToWidth: true
                    hbarPolicy: ScrollBarPolicy.AS_NEEDED
                    vbarPolicy: ScrollBarPolicy.AS_NEEDED
                    node: VBox {
                        spacing: 5
                        padding: Insets {
                            left: 5
                            right: 5
                            top: 3
                        }
                        layoutInfo: LayoutInfo {
                            hfill: true
                            vfill: false
                            hgrow: Priority.ALWAYS
                            vgrow: Priority.NEVER
                        }
                        content: bind logEntries;
                    }
                };

        var messageNode = VBox {
                    layoutInfo: LayoutInfo {
                        hfill: true
                        vfill: true
                        hgrow: Priority.ALWAYS
                        vgrow: Priority.ALWAYS
                    }
                    content: [
                        Label {
                            text: "Messages:"
                        },
                        messageScrollView
                    ]
                }
        var tableHeader = HBox {
                    layoutInfo: LayoutInfo {
                        vfill: false
                        vgrow: Priority.NEVER
                    }
                    content: [
                        Label {
                            layoutInfo: LayoutInfo {
                                hgrow: Priority.ALWAYS
                                hshrink: Priority.NEVER
                                hfill: true
                                hpos: HPos.LEFT
                            }
                            textAlignment: TextAlignment.LEFT
                            text: "State"
                        },
                        Label {
                            layoutInfo: LayoutInfo {
                                hgrow: Priority.ALWAYS
                                hfill: true
                                hpos: HPos.CENTER
                            }
                            textAlignment: TextAlignment.CENTER
                            text: "Activity"
                        },
                        Label {
                            layoutInfo: LayoutInfo {
                                hgrow: Priority.ALWAYS
                                hshrink: Priority.ALWAYS
                                hpos: HPos.RIGHT
                            }
                            textAlignment: TextAlignment.RIGHT
                            text: "Finished"
                        }
                    ]
                };
        var tableNode = VBox {
                    layoutInfo: LayoutInfo {
                        hfill: true
                        vfill: true
                        hgrow: Priority.ALWAYS
                        vgrow: Priority.ALWAYS
                    }
                    content: [
                        Label {
                            text: "Work Progress:"
                        },
                        VBox {
                            layoutInfo: LayoutInfo {
                                hfill: true
                                vfill: true
                                hgrow: Priority.ALWAYS
                                vgrow: Priority.ALWAYS
                            }
                            content: [tableHeader, tableScrollView]
                        }
                    ]
                }

        return mainNode = VBox {
                            spacing: 5
                            layoutInfo: LayoutInfo {
                                hfill: true
                                vfill: true hgrow: Priority.ALWAYS
                                vgrow: Priority.ALWAYS
                            }
                            managed: false
                            content: [messageNode, tableNode]
                        };
    }

    function sizeChanged(): Void {
        Container.resizeNode(mainNode, width, height);
    }

    public override function getPrefHeight(h: Number): Number {
        return 500;
    }

    public override function getPrefWidth(w: Number): Number {
        return 500;
    }

    public override var width on replace { sizeChanged()
            };
    public override var height on replace { sizeChanged()
            };

    public function addMessageEntry(timestamp: Long, text: String) {
        var messageEntry = Text {
                    content: "{dateFormat.format(new Date(timestamp))}: {text}"
                }
        insert messageEntry before messageEntries[0];
        insert Separator {} after messageEntries[0];
    }

    public function addLogEntry(timestamp: Long, text: String, state: AgendaEntryState, elouri: String) {
        var logEntry = HBox {
                    spacing: 10
                    vpos: VPos.CENTER
                    content: [
                        Rectangle {
                            width: 15, height: 15
                            arcHeight: 5, arcWidth: 5
                            fill: state.getColor()
                        },
                        Label {
                            layoutInfo: LayoutInfo {
                                hgrow: Priority.ALWAYS
                                hshrink: Priority.NEVER
                                hfill: true
                            }
                            font: Font {
                                size: 14
                            }
                            text: text
                        },
                        Text {
                            font: Font {
                                size: 14
                            }
                            layoutInfo: LayoutInfo {
                                hgrow: Priority.NEVER
                                hfill: true
                            }
                            content: if (timestamp == 0) "{dateFormat.format(new Date(timestamp))}" else ""
                        }
                    ]
                }
        var prevEntry = entryUrlMap.get(elouri) as Node;
        if (prevEntry != null) {
            delete prevEntry from logEntries;
        }
        insert logEntry into logEntries;
        entryUrlMap.put(elouri, logEntry);

        logEntries = Sequences.sort(logEntries, Comparator {
                    public override function compare(o1: Object, o2: Object): Integer {
                        var hb1 = o1 as HBox;
                        var t1 = hb1.content[2] as Text;
                        var hb2 = o2 as HBox;
                        var t2 = hb2.content[2] as Text;
                        if (t1.visible == t2.visible) {
                            return t1.content.compareTo(t2.content);
                        } else if (t1.visible) {
                            return -1;
                        } else if (t2.visible) {
                            return 1;
                        }
                        return 0;
                    }
                }) as Node[];
    }

    public function test() {
        Timeline {
            repeatCount: Timeline.INDEFINITE
            keyFrames: [
                KeyFrame {
                    time: 3s
                    action: function() {
                        addLogEntry(System.currentTimeMillis() - 1900000, "You could start to work on ELO 2", AgendaEntryState.ACTIVE, "elo2");
                    }
                },
                KeyFrame {
                    time: 6s
                    action: function() {
                        addLogEntry(System.currentTimeMillis() - 1600000, "You could start to work on ELO 1", AgendaEntryState.COMPLETED, "elo1");
                    }
                },
                KeyFrame {
                    time: 7s
                    action: function() {
                        addMessageEntry(System.currentTimeMillis() - 1500000, "You could start to work on ELO 1");
                    }
                },
                KeyFrame {
                    time: 9s
                    action: function() {
                        addLogEntry(System.currentTimeMillis() - 1300000, "You finished ELO 2", AgendaEntryState.ENABLED, "elo2");
                    }
                },
                KeyFrame {
                    time: 12s
                    action: function() {
                        addLogEntry(System.currentTimeMillis() - 1200000, "You could start to work on ELO 3", AgendaEntryState.REVOKED, "elo3");
                    }
                },
                KeyFrame {
                    time: 13s
                    action: function() {
                        addMessageEntry(System.currentTimeMillis() - 1100000, "You could start to work on ELO 2");
                    }
                },
                KeyFrame {
                    time: 15s
                    action: function() {
                        addLogEntry(System.currentTimeMillis() - 1000000, "You finished ELO 1", AgendaEntryState.ACTIVE, "elo1");
                    }
                }
            ]
        }.play();
    }

}

function run() {
    var a = AgendaNode {
            }

    Stage {
        title: "Test"
        width: 800
        height: 600
        scene: Scene {
            content: [
                a
            ]
        }
    }
    a.test();

}

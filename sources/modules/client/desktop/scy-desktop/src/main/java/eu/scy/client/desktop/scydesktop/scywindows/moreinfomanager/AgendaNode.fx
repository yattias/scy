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
import javafx.geometry.HPos;
import javafx.scene.control.Label;
import javafx.scene.control.CheckBox;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Container;
import javafx.scene.control.ScrollView;
import javafx.scene.control.ScrollBarPolicy;
import javafx.geometry.Insets;
import javafx.scene.paint.Color;
import java.util.HashMap;
import javafx.util.Sequences;
import javafx.scene.text.Text;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.lang.System;
import java.text.DateFormat;
import java.util.Comparator;

/**
 * @author weinbrenner
 */
public class AgendaNode extends CustomNode, Resizable {

    var logEntries: Node[];
    var scrollView: ScrollView;
    var entryUrlMap: HashMap;
    def dateFormat = SimpleDateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);

    public override function create(): Node {
        entryUrlMap = new HashMap();
        scrollView = ScrollView {
                    managed: false
                    layoutInfo: LayoutInfo {
                        hfill: true
                        vfill: true
                        hgrow: Priority.ALWAYS
                        vgrow: Priority.ALWAYS
                    }
                    fitToWidth: true
                    hbarPolicy: ScrollBarPolicy.AS_NEEDED
                    vbarPolicy: ScrollBarPolicy.AS_NEEDED
                    node: VBox {
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
    }

    function sizeChanged(): Void {
        Container.resizeNode(scrollView, width, height);
        println("Size changed of Agenda node to {width}/{height}");
    }

    public override function getPrefHeight(h: Number): Number {
        return 2000;
    }

    public override function getPrefWidth(w: Number): Number {
        return 2000;
    }

    public override var width on replace { sizeChanged() };
    public override var height on replace { sizeChanged() };

    public function addLogEntry(timestamp: Long, text: String, done: Boolean, elouri: String) {
        var logEntry = HBox {
                    spacing: 10
                    content: [
                        Text {
                            font: Font {
                                size: 14
                                oblique: true
                            }
                            layoutInfo: LayoutInfo {
                                hgrow: Priority.NEVER
                                hfill: true
                            }
                            content: "[ {dateFormat.format(new Date(timestamp))} ]: "
                            visible: done
                        }, Label {
                            layoutInfo: LayoutInfo {
                                hgrow: Priority.ALWAYS
                                hshrink: Priority.NEVER
                                hfill: true
                            }
                            textFill: if (done) Color.BLACK else Color.GREY
                            font: Font {
                                size: 14
                            }
                            text: text
                        }, CheckBox {
                            layoutInfo: LayoutInfo {
                                hgrow: Priority.NEVER
                                hpos: HPos.RIGHT
                                hshrink: Priority.ALWAYS
                            }
                            override var selected = done on replace {
                                        if (selected) {
                                            disable = true;
                                            // TODO log an "i'm finished" action
                                        }
                                    }
                            disable: done
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
                        var t1 = hb1.content[0] as Text;
                        var hb2 = o2 as HBox;
                        var t2 = hb2.content[0] as Text;
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
                        addLogEntry(System.currentTimeMillis() - 1900000, "You could start to work on ELO 2", false, "elo2");
                    }
                },
                KeyFrame {
                    time: 6s
                    action: function() {
                        addLogEntry(System.currentTimeMillis() - 1600000, "You could start to work on ELO 1", false, "elo1");
                    }
                },
                KeyFrame {
                    time: 9s
                    action: function() {
                        addLogEntry(System.currentTimeMillis() - 1300000, "You finished ELO 2", true, "elo2");
                    }
                },
                KeyFrame {
                    time: 12s
                    action: function() {
                        addLogEntry(System.currentTimeMillis() - 1200000, "You could start to work on ELO 3", false, "elo3");
                    }
                },
                KeyFrame {
                    time: 15s
                    action: function() {
                        addLogEntry(System.currentTimeMillis() - 1000000, "You finished ELO 1", true, "elo1");
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
        width: 600
        height: 400
        scene: Scene {
            content: [
                a
            ]
        }
    }
    println(System.currentTimeMillis() + (1000 * 60 * 25));
    println(System.currentTimeMillis() + (1000 * 60 * 64));
    println(System.currentTimeMillis() + (1000 * 60 * 79));
    println(System.currentTimeMillis() + (1000 * 60 * 121));
    a.test();

}

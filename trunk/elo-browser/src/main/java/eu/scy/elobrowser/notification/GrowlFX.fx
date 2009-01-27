/*
 * GrowlFX.fx
 *
 * Created on 22.01.2009, 14:09:34
 */

package eu.scy.elobrowser.notification;

import eu.scy.notification.api.INotification;
import eu.scy.notification.api.INotificationCallback;
import eu.scy.notification.NotificationService;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.CustomNode;
import javafx.scene.effect.DropShadow;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * @author Giemza
 */

public class GrowlFX extends CustomNode, INotificationCallback {

    var notificationService = new NotificationService("scy.collide.info", 2525, "notifications");

    var text :String on replace {
        if(fadein.running) {
           fadein.stop();
        }
        fadein.playFromStart();
    };

    public var width = 400;

    public var height = 50;

    var content : Group;

    public override function create(): Node {
        notificationService.registerCallback("adam", this);
        content = Group {
            content: [Rectangle {
                    x: 10,
                    y: 10
                    width: bind width,
                    height: bind height
                    fill: Color.BLACK
                    arcWidth: 50
                    arcHeight: 50
                }, Text {
                    font: Font {
                        size: 20
                    }
                    x: 28,
                    y: 40
                    fill: Color.WHITE
                    content: bind text
                }]
        };
        content.effect = DropShadow {
            radius: 15
        }
        return content;
    }

    override function onNotification(notification :INotification) {
        var errnum = notification.getProperty("errors");
        println("Received errors: {errnum}");
        
        FX.deferAction(function() :Void {
            text = "You have {errnum} errors in your last saved text!";
        });
    }

    var fadein = Timeline {
        keyFrames: [
            KeyFrame {
                time: 1s
                canSkip: false
                values: [
                    opacity => 0.7 tween Interpolator.LINEAR
                ]
            },
            KeyFrame {
                time: 6s
                action: function() {
                    fadeout.playFromStart();
                }
            }
        ]
    }

    var fadeout = Timeline {
        keyFrames: [
            KeyFrame {
                time: 1s
                canSkip: true
                values: [
                    opacity => 0.0 tween Interpolator.LINEAR
                ]
            }
        ]
    }

}
